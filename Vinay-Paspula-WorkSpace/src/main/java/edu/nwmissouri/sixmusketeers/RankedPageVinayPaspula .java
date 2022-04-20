package edu.nwmissouri.sixmusketeers;

import java.util.ArrayList;

public class RankedPageVinayPaspula  {
    public String key;
    public ArrayList<VotingPageVinayPaspula> voters;
    public RankedPageVinayPaspula(String key, ArrayList<VotingPageVinayPaspula> voters) {
        this.key = key;
        this.voters = voters;
    }
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public  ArrayList<VotingPageVinayPaspula> getVoters(){
        return voters;
    }
    public  void setVoters(ArrayList<VotingPageVinayPaspula> voters){
        this.voters = voters;
    }

}
