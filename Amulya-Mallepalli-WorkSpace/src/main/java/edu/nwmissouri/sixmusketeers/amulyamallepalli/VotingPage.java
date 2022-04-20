package edu.nwmissouri.sixmusketeers.amulyamallepalli;

import java.io.Serializable;

public class VotingPage implements Serializable{
    String voterName;
    Integer contributorVotes;
    public VotingPage(String voterName, Integer contributorVotes) {
        this.voterName = voterName;
        this.contributorVotes = contributorVotes;
    }
    @Override
    public String toString() {
        return "VotingPage [contributorVotes=" + contributorVotes + ", voterName=" + voterName + "]";
    }

}
