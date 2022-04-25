package edu.nwmissouri.sixmusketeers;

import java.io.Serializable;

public class VotingPage implements Serializable {

    public String name = "unknown.md";
    public Double rank = 1.0;
    public Integer votes = 0;
    



    public VotingPage(String nameIn, Integer votesIn){
        this.name = nameIn;

        this.votes = votesIn;                

    }


    public VotingPage(String nameIn, Double rankIn, Integer votesIn){
        this.name = nameIn;

        this.votes = votesIn;
        
        this.rank = rankIn ;

    }

    public Double getRank(){
        return this.rank;
    }

    public String getName(){
        return this.name;
    }

    public Integer getVotes(){
        return this.votes;
    }


    @Override
    public String toString(){
        return String.format("%s,%.5f,%d", this.name,this.rank,this.votes);
    }

}
