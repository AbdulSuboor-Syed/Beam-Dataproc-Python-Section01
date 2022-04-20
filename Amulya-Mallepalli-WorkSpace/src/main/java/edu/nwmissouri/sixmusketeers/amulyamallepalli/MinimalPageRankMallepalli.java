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
package edu.nwmissouri.sixmusketeers.amulyamallepalli;

import java.io.File;
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
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;

public class MinimalPageRankMallepalli {
  
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
    } }
  
  public static void main(String[] args) {

    PipelineOptions options = PipelineOptionsFactory.create();
    Pipeline p = Pipeline.create(options);
    String dataFolder = "web04";
    String dataFile = "go.md";
    PCollection<KV<String, String>> pcol1 = MallepalliMapper(p, dataFolder, "go.md");
    PCollection<KV<String, String>> pcol2 = MallepalliMapper(p, dataFolder, "python.md");
    PCollection<KV<String, String>> pcol3 = MallepalliMapper(p, dataFolder, "java.md");
    PCollection<KV<String, String>> pcol4 = MallepalliMapper(p, dataFolder, "README.md");

    PCollectionList<KV<String, String>> pcolList = PCollectionList.of(pcol1).and(pcol2).and(pcol3).and(pcol4);

    PCollection<KV<String, String>> mergedList = pcolList.apply(Flatten.<KV<String, String>>pCollections());
    
    PCollection<KV<String, Iterable<String>>> pcolGroupByKey = mergedList.apply(GroupByKey.create());

    PCollection<KV<String, RankedPage>> jobTwoInput = pcolGroupByKey.apply(ParDo.of(new Job1Finalizer()));

    PCollection<String> pLinksStr = jobTwoInput.apply(
        MapElements.into(
            TypeDescriptors.strings())
            .via((mergeOut) -> mergeOut.toString()));

    pLinksStr.apply(TextIO.write().to("Mallepaliout"));

    p.run().waitUntilFinish();
  }

  private static PCollection<KV<String, String>>MallepalliMapper(Pipeline p, String dataFolder, String dataFile) {
    String dataLocation = dataFolder + "/" + dataFile;
    PCollection<String> pcolInputLines = p.apply(TextIO.read().from(dataLocation));

    PCollection<String> pcolLinkLines = pcolInputLines.apply(Filter.by((String line) -> line.startsWith("[")));
    PCollection<String> pcolLinkPages = pcolLinkLines.apply(MapElements.into(TypeDescriptors.strings())
        .via(
            (String linkline) -> linkline.substring(linkline.indexOf("(") + 1, linkline.length() - 1)));
    PCollection<KV<String, String>> pcolKVpairs = pcolLinkPages.apply(MapElements
        .into(
            TypeDescriptors.kvs(
                TypeDescriptors.strings(), TypeDescriptors.strings()))
        .via(outlink -> KV.of(dataFile, outlink)));
    return pcolKVpairs;

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
private static PCollection<KV<String, RankedPage>> runJob2Iteration(
  PCollection<KV<String, RankedPage>> kvReducedPairs) {

//    PCollection<KV<String, RankedPage>> mappedKVs = kvReducedPairs.apply(ParDo.of(new Job2Mapper()));

// KV{README.md, README.md, 1.00000, 0, [java.md, 1.00000,1]}
// KV{README.md, README.md, 1.00000, 0, [go.md, 1.00000,1]}
// KV{java.md, java.md, 1.00000, 0, [README.md, 1.00000,3]}

// PCollection<KV<String, Iterable<RankedPage>>> reducedKVs = mappedKVs
//     .apply(GroupByKey.<String, RankedPage>create());

// KV{java.md, [java.md, 1.00000, 0, [README.md, 1.00000,3]]}
// KV{README.md, [README.md, 1.00000, 0, [python.md, 1.00000,1], README.md,
// 1.00000, 0, [java.md, 1.00000,1], README.md, 1.00000, 0, [go.md, 1.00000,1]]}

// PCollection<KV<String, RankedPage>> updatedOutput = reducedKVs.apply(ParDo.of(new Job2Updater()));

// KV{README.md, README.md, 2.70000, 0, [java.md, 1.00000,1, go.md, 1.00000,1,
// python.md, 1.00000,1]}
// KV{python.md, python.md, 0.43333, 0, [README.md, 1.00000,3]}

PCollection<KV<String, RankedPage>> updatedOutput = null;
return updatedOutput;
}

public static  void deleteFiles(){
  final File file = new File("./");
  for (File f : file.listFiles()){
    if(f.getName().startsWith("Mallepaliout")){
      f.delete();
    }
  }
}

  
  }


