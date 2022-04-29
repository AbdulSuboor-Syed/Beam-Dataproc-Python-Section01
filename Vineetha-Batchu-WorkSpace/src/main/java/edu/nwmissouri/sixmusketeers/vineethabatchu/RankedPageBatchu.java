package edu.nwmissouri.sixmusketeers.vineethabatchu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.beam.sdk.values.KV;

public class RankedPageBatchu implements Serializable, Comparator<KV<Double,String>> {
    public String name;
    public ArrayList<VotingPageBatchu> voters;
    public Double rank;


    public RankedPageBatchu(String voter,Double rank, ArrayList<VotingPageBatchu> voters){
        this.name = voter;
        this.voters = voters;
        this.rank = rank;
    }  
    public RankedPageBatchu() {
        name = "";
        rank = 0.0;
    }
    public RankedPageBatchu(String key, ArrayList<VotingPageBatchu> voters) {
        this.name = key;
        this.voters = voters;
        this.rank = 1.0;
    }

    public String getKey(){
        return name;
    }

    public  ArrayList<VotingPageBatchu> getVoters(){
        return voters;
    }

    public void setKey(String key){
        this.name = key;
    }

    public  void setVoters(ArrayList<VotingPageBatchu> voters){
        this.voters = voters;
    }
    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
       // return "RankedPage [name=" + name + ", voterList=" + voters + "rank= "+this.rank+"]";
    }
    public double getRank() {
        return this.rank;
    }
    @Override
    public int compare(KV<Double, String> o1, KV<Double, String> o2) {
        double rank1 = o1.getKey();
        double rank2 = o2.getKey();
        if (rank1 > rank2) {
            return 1;
        } else if(rank1 < rank2) {
            return -1;
        }else{
            return 0;
        }
    }


}
