
package edu.nwmissouri.sixmusketeers.keerthimuli;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptors;

public class MinimalPageRankingKeerthiMuli {
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
  // JOB1 FINALIZER
  static class Job1Finalizer extends DoFn<KV<String, Iterable<String>>, KV<String, RankedPageKeerthiMuli>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, RankedPageKeerthiMuli>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<VotingPageKeerthiMuli> voters = new ArrayList<VotingPageKeerthiMuli>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new VotingPageKeerthiMuli(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new RankedPageKeerthiMuli(element.getKey(), voters)));
    }
  }

  
  // HELPER FUNCTIONS
  public static  void deleteFiles(){
    final File file = new File("./");
    for (File f : file.listFiles()){
     if(f.getName().startsWith("KeerthiMuli_Output")){
    f.delete();
    }
     }
   }
   // Map to KV pairs
  private static PCollection<KV<String, String>> keerthiMuliMapper1(Pipeline p, String dataFolder, String dataFile) {

    String dataPath = dataFolder + "/" + dataFile;

    PCollection<String> pColInputLines = p.apply(TextIO.read().from(dataPath));
    PCollection<String> pColLinkLines = pColInputLines.apply(Filter.by((String line) -> line.startsWith("[")));
    // README.md
    PCollection<String> pColLinkedPages = pColLinkLines.apply(MapElements.into(TypeDescriptors.strings())
        .via((String linkline) -> linkline.substring(linkline.indexOf("(") + 1, linkline.length() - 1)));
    // README.md - > KV{go.md, README.md}
    PCollection<KV<String, String>> pColKvPairs = pColLinkedPages.apply(MapElements
        .into(TypeDescriptors.kvs(
            TypeDescriptors.strings(), TypeDescriptors.strings()

        ))
        .via(outlink -> KV.of(dataFile, outlink)));
    return pColKvPairs;

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
 private static PCollection<KV<String, RankedPageKeerthiMuli>> runJob2Iteration(
      PCollection<KV<String, RankedPageKeerthiMuli>> kvReducedPairs) {
     PCollection<KV<String, RankedPageKeerthiMuli>> mappedKVs = kvReducedPairs.apply(ParDo.of(new Job2Mapper()));
   // apply(ParDo.of(new Job2Mapper()));

    // KV{README.md, README.md, 1.00000, 0, [java.md, 1.00000,1]}
    // KV{README.md, README.md, 1.00000, 0, [go.md, 1.00000,1]}
    // KV{java.md, java.md, 1.00000, 0, [README.md, 1.00000,3]}

    PCollection<KV<String, Iterable<RankedPageKeerthiMuli>>> reducedKVs = mappedKVs
        .apply(GroupByKey.<String, RankedPageKeerthiMuli>create());

    // KV{java.md, [java.md, 1.00000, 0, [README.md, 1.00000,3]]}
    // KV{README.md, [README.md, 1.00000, 0, [python.md, 1.00000,1], README.md,
    // 1.00000, 0, [java.md, 1.00000,1], README.md, 1.00000, 0, [go.md, 1.00000,1]]}

    PCollection<KV<String, RankedPageKeerthiMuli>> updatedOutput = reducedKVs.apply(ParDo.of(new Job2Updater()));

    // KV{README.md, README.md, 2.70000, 0, [java.md, 1.00000,1, go.md, 1.00000,1,
    // python.md, 1.00000,1]}
    // KV{python.md, python.md, 0.43333, 0, [README.md, 1.00000,3]}
    return updatedOutput;
  }

 // MAIN METHOD

  public static void main(String[] args) {

    PipelineOptions options = PipelineOptionsFactory.create();

    // Create the Pipeline object with the options we defined above
    Pipeline p = Pipeline.create(options);
    String dataFolder = "web04";
    // Processing all four input files
    PCollection<KV<String, String>> pCollection1 = keerthiMuliMapper1(p, dataFolder, "go.md");
    PCollection<KV<String, String>> pCollection2 = keerthiMuliMapper1(p, dataFolder, "java.md");
    PCollection<KV<String, String>> pCollection3 = keerthiMuliMapper1(p, dataFolder, "python.md");
    PCollection<KV<String, String>> pCollection4 = keerthiMuliMapper1(p, dataFolder, "README.md");
    // Make a List of PCs
    PCollectionList<KV<String, String>> pCollectionsList = PCollectionList.of(pCollection1).and(pCollection2)
        .and(pCollection3).and(pCollection4);
    // Flatten into a Merged PCollection
    PCollection<KV<String, String>> mergedPcollection = pCollectionsList
        .apply(Flatten.<KV<String, String>>pCollections());

    // Group by Key to get a single record for each page
    PCollection<KV<String, Iterable<String>>> kvReducedPairs = mergedPcollection.apply(GroupByKey.<String, String>create());

    // Convert to a custom Value object (RankedPageKeerthiMuli) in preparation for Job 2
    PCollection<KV<String, RankedPageKeerthiMuli>> job2in = kvReducedPairs.apply(ParDo.of(new Job1Finalizer()));

    PCollection<KV<String, RankedPageKeerthiMuli>> job2out = null; 
    int iterations = 2;
    for (int i = 1; i <= iterations; i++) {
      job2out= runJob2Iteration(job2in);
      job2in =job2out;
    }
    // Transform KV to Strings
   PCollection<String> mergeString = job2out.apply(
        MapElements.into(
            TypeDescriptors.strings())
            .via((kvInput) -> kvInput.toString()));
    mergeString.apply(TextIO.write().to("KeerthiMuli_Output"));

    p.run().waitUntilFinish();
  }

  }



