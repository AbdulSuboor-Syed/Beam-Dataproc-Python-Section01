package edu.nwmissouri.sixmusketeers.keerthimuli;

public class VotingPageKeerthiMuli {
    public String voterName;
    public Integer contributorVotes;
    public VotingPageKeerthiMuli(String voterName, Integer contributorVotes) {
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
}
