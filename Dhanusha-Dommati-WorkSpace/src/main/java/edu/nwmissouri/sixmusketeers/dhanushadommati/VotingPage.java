package edu.nwmissouri.sixmusketeers.dhanushadommati;
import java.io.Serializable;

public class VotingPage implements Serializable{

    public String name;
    public Integer votes;
    public Double rank;

    public VotingPage(String name, Integer votes) {
        this.name = name;
        this.votes = votes;
    }
    public VotingPage(String voterName, Double pageRank,Integer contributorVotes2){
        this.name = voterName;
        this.votes = contributorVotes2;      
        this.rank = pageRank;  
    }

    public String getname(){
        return this.name;
    }

    public  Integer getVotes(){
        return votes;
    }

    public void setname(String name){
        this.name = name;
    }

    public void setVotes(Integer contributorVotes ){
        this.votes = contributorVotes;
    }
    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.votes.toString());
     
    }
    public Double getRank() {
        return this.rank;
    }
    public void setRank(Double pageRank){
        this.rank = pageRank;
    }
}