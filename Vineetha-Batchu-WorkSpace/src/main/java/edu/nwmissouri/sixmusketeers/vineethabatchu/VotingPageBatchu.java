package edu.nwmissouri.sixmusketeers.vineethabatchu;

import java.io.Serializable;

public class VotingPageBatchu implements Serializable {

    public String name;
    public Integer votes;
    public  Double rank;

    public VotingPageBatchu(String voterName, Double pageRank,Integer contributorVotes2){
        this.name = voterName;
        this.votes = contributorVotes2;      
        this.rank = pageRank;  
    }
    public VotingPageBatchu(String voterName, Integer contributorVotes) {
        this.name = voterName;
        this.votes = contributorVotes;
        this.rank=1.0;
    }

    public String getName(){
        return name;
    }

    public  Integer getVotes(){
        return votes;
    }

    public void setName(String voterName){
        this.name = voterName;
    }

    public void setVotes(Integer contributorVotes ){
        this.votes = contributorVotes;
    }
    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.votes.toString());
      //  return "VotingPage [contributorVotes=" + contributorVotes + ", voterName=" + voterName + "]";
    }
    public double getRank() {
        return this.rank;
    }
    public void setRank(double pageRank){
        this.rank = pageRank;
    }
}
