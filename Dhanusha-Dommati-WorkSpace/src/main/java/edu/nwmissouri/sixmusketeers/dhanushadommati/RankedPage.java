package edu.nwmissouri.sixmusketeers.dhanushadommati;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import org.apache.beam.sdk.values.KV;

public class RankedPage implements Serializable, Comparator<KV<Double,String>> {
    public String name;
    public ArrayList<VotingPage> voters;
    public Double rank;

    public RankedPage(String name, ArrayList<VotingPage> voters) {
        this.name = name;
        this.voters = voters;
        this.rank=1.0;
    }
    public RankedPage(String voter,Double rank, ArrayList<VotingPage> voters){
        this.name = voter;
        this.voters = voters;
        this.rank = rank;
    }  
    public RankedPage() {
        name = "";
        rank = 0.0;
    }

    public String getname(){
        return name;
    }

    public  ArrayList<VotingPage> getVoters(){
        return voters;
    }

    public void setname(String name){
        this.name = name;
    }

    public  void setVoters(ArrayList<VotingPage> voters){
        this.voters = voters;
    }
     public double getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
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