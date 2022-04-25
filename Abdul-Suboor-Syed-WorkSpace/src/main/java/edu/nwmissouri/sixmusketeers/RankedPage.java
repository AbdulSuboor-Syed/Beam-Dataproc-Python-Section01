package edu.nwmissouri.sixmusketeers;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPage implements Serializable {
    public String name="unkown.md";
    public Double rank=1.000;
    public ArrayList<VotingPage> voters;



    public RankedPage(String nameIn,ArrayList<VotingPage> votersIn) {
        this.name = nameIn;        
        this.voters = votersIn;
    }


    public RankedPage(String nameIn,Double rankIn, ArrayList<VotingPage> votersIn) {
        this.name = nameIn;
        this.rank=rankIn;
        this.voters = votersIn;
    }

    public Double getRank(){
        return rank;
    }

    public  ArrayList<VotingPage> getVoters(){
        return voters;
    }
    

    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
    }


}
