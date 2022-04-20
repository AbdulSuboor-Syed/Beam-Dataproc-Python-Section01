package edu.nwmissouri.sixmusketeers;

import java.io.Serializable;

public class VotingPage implements Serializable {

    public String voterName;
    public Integer contributorVotes;

    public VotingPage(String voterName, Integer contributorVotes) {
        this.voterName = voterName;
        this.contributorVotes = contributorVotes;
    }

    public String getVoterName(){
        return voterName;
    }

    public  Integer getContributorVotes(){
        return contributorVotes;
    }

    public void setVoterName(String voterName){
        this.voterName = voterName;
    }

    public void setContributorVotes(Integer contributorVotes ){
        this.contributorVotes = contributorVotes;
    }
    @Override
    public String toString() {
        return "VotingPage [contributorVotes=" + contributorVotes + ", voterName=" + voterName + "]";
    }
}
