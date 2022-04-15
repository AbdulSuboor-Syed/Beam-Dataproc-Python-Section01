# Execution Commands(PowerShell)

mvn clean

 mvn compile exec:java -D exec.mainClass=org.apache.beam.examples.MinimalPageRankAbdulSuboor `
 -D exec.args="--inputFile=sample.txt --output=counts" -P direct-runner


 mvn compile exec:java -D exec.mainClass=edu.nwmissouri.sixmusketeers.MinimalPageRankAbdulSuboor 