package edu.nwmissouri.sixmusketeers.keerthimuli;

import java.util.ArrayList;

public class RankedPageKeerthiMuli {
    public String name;
    public ArrayList<VotingPageKeerthiMuli> voters;
    public RankedPageKeerthiMuli(String nameIn, ArrayList<VotingPageKeerthiMuli> votersIn) {
        this.name = nameIn;
        this.voters = votersIn;
    }
    public String getName(){
        return name;
    }
    public void setName(String nameIn){
        this.name = nameIn;
    }
    public  ArrayList<VotingPageKeerthiMuli> getVoters(){
        return voters;
    }
    public  void setVoters(ArrayList<VotingPageKeerthiMuli> voters){
        this.voters = voters;
    }

}