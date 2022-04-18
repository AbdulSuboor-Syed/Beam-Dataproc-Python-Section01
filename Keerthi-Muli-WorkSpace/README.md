# This is Keerthi
Link to quick start: <https://beam.apache.org/get-started/quickstart-java/>

Link to Programming Guide: <https://beam.apache.org/documentation/programming-guide/>
### Command Used For Creating Project In Poweshell


mvn archetype:generate `
 -D archetypeGroupId=org.apache.beam `
 -D archetypeArtifactId=beam-sdks-java-maven-archetypes-examples `
 -D archetypeVersion=2.36.0 `
 -D groupId=org.example `
 -D artifactId=word-count-beam `
 -D version="0.1" `
 -D package=org.apache.beam.examples `
 -D interactiveMode=false

### Command Used For Executing Project In Poweshell

 mvn compile exec:java -D exec.mainClass=edu.nwmissouri.sixmusketeers.keerthimuli.MinimalPageRankingKeerthiMuli
