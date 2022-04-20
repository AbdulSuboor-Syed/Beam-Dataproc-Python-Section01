package edu.nwmissouri.sixmusketeers.keerthimuli;

import java.util.ArrayList;

public class RankedPageKeerthiMuli {
    public String key;
    public ArrayList<VotingPageKeerthiMuli> voters;
    public RankedPageKeerthiMuli(String key, ArrayList<VotingPageKeerthiMuli> voters) {
        this.key = key;
        this.voters = voters;
    }
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public  ArrayList<VotingPageKeerthiMuli> getVoters(){
        return voters;
    }
    public  void setVoters(ArrayList<VotingPageKeerthiMuli> voters){
        this.voters = voters;
    }

}
