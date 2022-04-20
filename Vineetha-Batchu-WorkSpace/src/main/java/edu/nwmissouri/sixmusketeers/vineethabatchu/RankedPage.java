package edu.nwmissouri.sixmusketeers.vineethabatchu;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPage implements Serializable {
    String name="unkown.md";
    Double rank=1.0;
    Integer votes=0;
    

    public RankedPage(String key, ArrayList<VotingPage> voters) {
    }

    public String toString(){
        return String.format("%s.$.5f,%d", this.name,this.rank,this.votes);
    }


}
