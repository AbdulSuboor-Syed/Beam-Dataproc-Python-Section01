package edu.nwmissouri.sixmusketeers.vineethabatchu;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPage implements Serializable {
    public String name;
    public ArrayList<VotingPage> voters;
    public double rank;


    public RankedPage(String voter,double rank, ArrayList<VotingPage> voters){
        this.name = voter;
        this.voters = voters;
        this.rank = rank;
    }  

    public RankedPage(String key, ArrayList<VotingPage> voters) {
        this.name = key;
        this.voters = voters;
        this.rank = 1.0;
    }

    public String getKey(){
        return name;
    }

    public  ArrayList<VotingPage> getVoters(){
        return voters;
    }

    public void setKey(String key){
        this.name = key;
    }

    public  void setVoters(ArrayList<VotingPage> voters){
        this.voters = voters;
    }
    @Override
    public String toString() {
        return "RankedPage [name=" + name + ", voterList=" + voters + "rank= "+this.rank+"]";
    }
    public double getRank() {
        return this.rank;
    }


}
