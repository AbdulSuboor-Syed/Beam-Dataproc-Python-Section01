package edu.nwmissouri.sixmusketeers.amulyamallepalli;

import java.io.Serializable;
import java.util.ArrayList;

public class RankedPage implements Serializable {
    String key;
    ArrayList<VotingPage> voterList = new ArrayList<>();
    public RankedPage(String key, ArrayList<VotingPage> voters) {
        this.key = key;
        this.voterList = voters;
    }
    @Override
    public String toString() {
        return "RankedPage [key=" + key + ", voterList=" + voterList + "]";
    }
    

}
