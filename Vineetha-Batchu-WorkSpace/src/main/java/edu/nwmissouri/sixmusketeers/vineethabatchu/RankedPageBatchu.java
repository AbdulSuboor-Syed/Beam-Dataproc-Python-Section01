package edu.nwmissouri.sixmusketeers.vineethabatchu;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPageBatchu implements Serializable {
    public String name;
    public ArrayList<VotingPageBatchu> voters;
    public Double rank;


    public RankedPageBatchu(String voter,Double rank, ArrayList<VotingPageBatchu> voters){
        this.name = voter;
        this.voters = voters;
        this.rank = rank;
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


}
