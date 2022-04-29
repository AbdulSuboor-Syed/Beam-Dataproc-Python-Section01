package edu.nwmissouri.sixmusketeers.dhanushadommati;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPage implements Serializable {
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



}