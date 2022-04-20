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
package edu.nwmissouri.sixmusketeers;

import java.util.ArrayList;

// beam-playground:
//   name: MinimalWordCount
//   description: An example that counts words in Shakespeare's works.
//   multifile: false
//   pipeline_options:
//   categories:
//     - Combiners
//     - Filtering
//     - IO
//     - Core Transforms

import java.util.Arrays;
import java.util.Collection;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;

/**
 * An example that counts words in Shakespeare.
 *
 * <p>This class, {@link MinimalWordCount}, is the first in a series of four successively more
 * detailed 'word count' examples. Here, for simplicity, we don't show any error-checking or
 * argument processing, and focus on construction of the pipeline, which chains together the
 * application of core transforms.
 *
 * <p>Next, see the {@link WordCount} pipeline, then the {@link DebuggingWordCount}, and finally the
 * {@link WindowedWordCount} pipeline, for more detailed examples that introduce additional
 * concepts.
 *
 * <p>Concepts:
 *
 * <pre>
 *   1. Reading data from text files
 *   2. Specifying 'inline' transforms
 *   3. Counting items in a PCollection
 *   4. Writing data to text files
 * </pre>
 *
 * <p>No arguments are required to run this pipeline. It will be executed with the DirectRunner. You
 * can see the results in the output files in your current working directory, with names like
 * "wordcounts-00001-of-00005. When running on a distributed service, you would use an appropriate
 * file service.
 */
public class MinimalPageRankAbdulSuboor {

  static class Job1Finalizer extends DoFn<KV<String, Iterable<String>>, KV<String, RankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, RankedPage>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<VotingPage> voters = new ArrayList<VotingPage>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new VotingPage(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new RankedPage(element.getKey(), voters)));
    }
  }
  
  static class Job2Mapper extends DoFn<KV<String, RankedPage>, KV<String, RankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, RankedPage>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<VotingPage> voters = new ArrayList<VotingPage>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new VotingPage(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new RankedPage(element.getKey(), voters)));
    }
  }

  static class Job2Updater extends DoFn<KV<String, Iterable<RankedPage>>, KV<String, RankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, RankedPage>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<VotingPage> voters = new ArrayList<VotingPage>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new VotingPage(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new RankedPage(element.getKey(), voters)));
    }
  }





  public static void main(String[] args) {
   
    PipelineOptions options = PipelineOptionsFactory.create();

    Pipeline p = Pipeline.create(options);

    String dataFolder = "web04";
    
    
   

    PCollection<KV<String,String>> pcolKVO = AbdulSuboorKVS(p,dataFolder ,"go.md");
    PCollection<KV<String,String>> pcolKVO1 = AbdulSuboorKVS(p,dataFolder, "java.md");
    PCollection<KV<String,String>> pcolKVO2 = AbdulSuboorKVS(p,dataFolder, "python.md");
    PCollection<KV<String,String>> pcolKVO3 = AbdulSuboorKVS(p,dataFolder, "README.md");


       PCollectionList<KV<String,String>> pcolListing = PCollectionList.of(pcolKVO).and(pcolKVO1).and(pcolKVO2).and(pcolKVO3);


       PCollection<KV<String,String>> mergedList = pcolListing.apply(Flatten.<KV<String,String>>pCollections());

      PCollection<KV<String,Iterable<String>>> listReducedPairs = mergedList.apply(GroupByKey.<String,String>create());

PCollection<String> output = listReducedPairs.apply(MapElements.into(TypeDescriptors.strings()).via(kv -> kv.toString()));

output.apply(TextIO.write().to("AbdulSuboor-Job02"));
    

    p.run().waitUntilFinish();
  }

  private static PCollection<KV<String,String>> AbdulSuboorKVS(Pipeline p, String dataFolder, String dataFile) {
    
    String dataPath = dataFolder + "/" + dataFile;

        PCollection<String>pcolInput = p.apply(TextIO.read().from(dataPath));
                        
        PCollection<String> pcolLinks = pcolInput.apply(Filter.by( (String line) -> line.startsWith("[") ) );

        PCollection<String> pcolMaping = pcolLinks.apply(MapElements.into( TypeDescriptors.strings()).via(
                        (String linkline) -> linkline.substring(linkline.indexOf("(")+1,linkline.length()-1) ) );        




PCollection<KV<String,String>> pcolKV =  pcolMaping.apply(MapElements.into(
  TypeDescriptors
  .kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
    .via((String outLink) -> KV.of(dataFile,outLink)));

return pcolKV;
    }




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

   
 

  




}