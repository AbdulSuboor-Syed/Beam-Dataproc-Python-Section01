
package edu.nwmissouri.sixmusketeers;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptors;

public class MinimalPageRankingKeerthiMuli {

  public static void main(String[] args) {

    PipelineOptions options = PipelineOptionsFactory.create();
    // JOB 1 IN PAGE RANKING

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

    // Group By Key
   PCollection<KV<String,Iterable<String>>> groupedCollection = mergedPcollection.apply(GroupByKey.<String,String>create());

    // Transform KV to Strings
    PCollection<String> mergeString = groupedCollection.apply(
        MapElements.into(
            TypeDescriptors.strings())
            .via((kvInput) -> kvInput.toString()));
    mergeString.apply(TextIO.write().to("KeerthiMuli_Output"));

    p.run().waitUntilFinish();
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
 // END OF JOB1
}
