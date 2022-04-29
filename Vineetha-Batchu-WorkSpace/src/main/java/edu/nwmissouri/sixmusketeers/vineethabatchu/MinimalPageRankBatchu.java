/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.nwmissouri.sixmusketeers.vineethabatchu;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptors;

public class MinimalPageRankBatchu {
   // DEFINE DOFNS
  // ==================================================================
  // You can make your pipeline assembly code less verbose by defining
  // your DoFns statically out-of-line.
  // Each DoFn<InputT, OutputT> takes previous output
  // as input of type InputT
  // and transforms it to OutputT.
  // We pass this DoFn to a ParDo in our pipeline.
  /**
   * DoFn Job1Finalizer takes KV(String, String List of outlinks) and transforms
   * the value into our custom RankedPage Value holding the page's rank and list
   * of voters.
   * 
   * The output of the Job1 Finalizer creates the initial input into our
   * iterative Job 2.
   */
  static class Job1Finalizer extends DoFn<KV<String, Iterable<String>>, KV<String, RankedPageBatchu>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, RankedPageBatchu>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<VotingPageBatchu> voters = new ArrayList<VotingPageBatchu>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new VotingPageBatchu(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new RankedPageBatchu(element.getKey(), voters)));
    }
  }
  
  static class Job2Mapper extends DoFn<KV<String, RankedPageBatchu>, KV<String, RankedPageBatchu>> {
    @ProcessElement
    public void processElement(@Element KV<String, RankedPageBatchu> element,
        OutputReceiver<KV<String, RankedPageBatchu>> receiver) {
      Integer votes = 0;
      ArrayList<VotingPageBatchu> voters = element.getValue().getVoters();
      if (voters instanceof Collection) {
        votes = ((Collection<VotingPageBatchu>)voters).size();
      }
      for (VotingPageBatchu vp : voters) {
        String pageName=vp.getName();
        Double pageRank=vp.getRank();
        String contributingPageName= element.getKey();
        Double contributingPageRank=element.getValue().getRank();
        VotingPageBatchu contributer=new VotingPageBatchu(contributingPageName, contributingPageRank, votes);
        ArrayList<VotingPageBatchu> arr =new ArrayList<VotingPageBatchu>();
        arr.add(contributer);
      receiver.output(KV.of(vp.getName(), new RankedPageBatchu(pageName,pageRank,arr)));
        
      }
    }
  }

  static class Job2Updater extends DoFn<KV<String, Iterable<RankedPageBatchu>>, KV<String, RankedPageBatchu>> {
    @ProcessElement
  public void processElement(@Element KV<String, Iterable<RankedPageBatchu>> element,
    OutputReceiver<KV<String, RankedPageBatchu>> receiver) {
      //Double dampingFactor = 0.85
      Double dampingFactor = 0.85;
      //Double updatedRank = (1 - dampingFactor) to start
      Double updatedRank = (1 - dampingFactor);
      //Create a  new array list for newVoters
      ArrayList<VotingPageBatchu> newVoters = new ArrayList<>();
      //For each pg in rankedPages, if pg isn't null, for each vp in pg.getVoters()
      for(RankedPageBatchu pg:element.getValue()){
        if (pg != null) {
          for(VotingPageBatchu vp:pg.getVoters()){
            newVoters.add(vp);
            updatedRank += (dampingFactor) * vp.getRank() / (double)vp.getVotes();

          }
        }
      }
      receiver.output(KV.of(element.getKey(),new RankedPageBatchu(element.getKey(), updatedRank, newVoters)));
  }
  }
  static class Job3Finder extends DoFn<KV<String, RankedPageBatchu>, KV<String, Double>> {
    @ProcessElement
    public void processElement(@Element KV<String, RankedPageBatchu> element,
        OutputReceiver<KV<String, Double>> receiver) {
      String currentPage = element.getKey();
      Double currentPageRank = element.getValue().getRank();

      receiver.output(KV.of(currentPage, currentPageRank));
    }
  }

  public static class Job3Final implements Comparator<KV<String, Double>>, Serializable {
    @Override
    public int compare(KV<String, Double> o1, KV<String, Double> o2) {
      return o1.getValue().compareTo(o2.getValue());
    }
  }

  public static void main(String[] args) {

    // Create a PipelineOptions object. This object lets us set various execution
    // options for our pipeline, such as the runner you wish to use. This example
    // will run with the DirectRunner by default, based on the class path configured
    // in its dependencies.
    deleteFiles();
    PipelineOptions options = PipelineOptionsFactory.create();

    Pipeline p = Pipeline.create(options);

   
    String folder="web04";
   PCollection<KV<String,String>> pCollectionKVList1 = batchuMapper1(p,"go.md",folder);
   PCollection<KV<String,String>> pCollectionKVList2 = batchuMapper1(p,"python.md",folder);
   PCollection<KV<String,String>> pCollectionKVList3 = batchuMapper1(p,"java.md",folder);
   PCollection<KV<String,String>> pCollectionKVList4 = batchuMapper1(p,"README.md",folder);
   PCollection<KV<String,String>> pCollectionKVList5 = batchuMapper1(p,"c.md",folder);

   
    PCollectionList<KV<String, String>> pCollList = PCollectionList.of(pCollectionKVList1).and(pCollectionKVList2).and(pCollectionKVList3).and(pCollectionKVList4).and(pCollectionKVList5);
    PCollection<KV<String, String>> mergedl = pCollList.apply(Flatten.<KV<String,String>>pCollections());
    // Group by Key to get a single record for each page
    PCollection<KV<String, Iterable<String>>> grouped =mergedl.apply(GroupByKey.<String,String>create());
        // Convert to a custom Value object (RankedPage) in preparation for Job 2
    PCollection<KV<String, RankedPageBatchu>> job2in = grouped.apply(ParDo.of(new Job1Finalizer()));
    
    PCollection<KV<String, RankedPageBatchu>> job2out = null; 
    int iterations = 50;
    for (int i = 1; i <= iterations; i++) {
      job2out= runJob2Iteration(job2in);
      job2in =job2out;
    }
    
    PCollection<String> pColLinkString = job2out.apply(
      MapElements
      .into(TypeDescriptors.strings())
      .via((mergeOut)->mergeOut.toString()));
    
    pColLinkString.apply(TextIO.write().to("batchuout"));  
    p.run().waitUntilFinish();
    //batchuMapper1(p);
      
        
  }

  private static PCollection<KV<String, String>> batchuMapper1(Pipeline p,String dataFile ,String dataFolder ) {
    String dataPath = dataFolder + "/" + dataFile;
    //p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<String> pcolInputLines=p.apply(TextIO.read().from(dataPath));
      // .apply(Filter.by((String line) -> !line.isEmpty()))
      // .apply(Filter.by((String line)->!line.equals(" ")))
      PCollection<String> pcolLinkLines= pcolInputLines.apply(Filter.by((String line)->line.startsWith("[")));
      PCollection<String> pcolLinks=pcolLinkLines.apply(MapElements
      .into(TypeDescriptors.strings())
      .via(
        (String linkline)->
        linkline.substring(linkline.indexOf("(")+1,linkline.length()-1)));

      // from README.md to Key Value Pairs
        PCollection<KV<String,String>> pColKeyValuePairs=pcolLinks.apply(MapElements

      .into(
        TypeDescriptors.kvs(
          TypeDescriptors.strings(),TypeDescriptors.strings()
          )
          )

      .via(
       outlink->KV.of(dataFile,outlink)

      ));
      return pColKeyValuePairs;

  }
  
  /**
 * Run one iteration of the Job 2 Map-Reduce process
 * Notice how the Input Type to Job 2.
 * Matches the Output Type from Job 2.
 * How important is that for an iterative process?
 * 
 * @param kvReducedPairs - takes a PCollection<KV<String, RankedPage>> with
 *                       initial ranks.
 * @return - returns a PCollection<KV<String, RankedPage>> with updated ranks.
 */
private static PCollection<KV<String, RankedPageBatchu>> runJob2Iteration(
  PCollection<KV<String, RankedPageBatchu>> kvReducedPairs) {

    PCollection<KV<String, RankedPageBatchu>> mappedKVs = kvReducedPairs.apply(ParDo.of(new Job2Mapper()));

// KV{README.md, README.md, 1.00000, 0, [java.md, 1.00000,1]}
// KV{README.md, README.md, 1.00000, 0, [go.md, 1.00000,1]}
// KV{java.md, java.md, 1.00000, 0, [README.md, 1.00000,3]}

 PCollection<KV<String, Iterable<RankedPageBatchu>>> reducedKVs = mappedKVs
     .apply(GroupByKey.<String, RankedPageBatchu>create());

// KV{java.md, [java.md, 1.00000, 0, [README.md, 1.00000,3]]}
// KV{README.md, [README.md, 1.00000, 0, [python.md, 1.00000,1], README.md,
// 1.00000, 0, [java.md, 1.00000,1], README.md, 1.00000, 0, [go.md, 1.00000,1]]}

 PCollection<KV<String, RankedPageBatchu>> updatedOutput = reducedKVs.apply(ParDo.of(new Job2Updater()));

// KV{README.md, README.md, 2.70000, 0, [java.md, 1.00000,1, go.md, 1.00000,1,
// python.md, 1.00000,1]}
// KV{python.md, python.md, 0.43333, 0, [README.md, 1.00000,3]}

//PCollection<KV<String, RankedPageBatchu>> updatedOutput = null;
return updatedOutput;
}

// HELPER FUNCTIONS
public static  void deleteFiles(){
  final File file = new File("./");
  for (File f : file.listFiles()){
   if(f.getName().startsWith("batchuout")){
  f.delete();
  }
   }
 }

}
