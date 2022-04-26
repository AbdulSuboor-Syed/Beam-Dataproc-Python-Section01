package edu.nwmissouri.sixmusketeers.keerthimuli;

import java.io.Serializable;
import java.util.ArrayList;
// Keeps track of a page , its rank and its voting pages.
public class RankedPageKeerthiMuli implements Serializable{
    String name = "unknown.md";
    Double rank =1.000;
    ArrayList<VotingPageKeerthiMuli> voters = new ArrayList<VotingPageKeerthiMuli>();
    /**
     * 
     * @param nameIn this page name
     * @param votersIn arraylist of pages pointing to this page
     */
    public RankedPageKeerthiMuli(String nameIn, ArrayList<VotingPageKeerthiMuli> votersIn) {
        this.name = nameIn;
        this.voters = votersIn;
    }
    /**
     * 
     * @param nameIn this page name
     * @param rankIn this page's rank
     * @param votersIn array list of pages pointing to this page
     */
    public RankedPageKeerthiMuli(String nameIn,Double rankIn, ArrayList<VotingPageKeerthiMuli> votersIn) {
        this.name = nameIn;
        this.rank= rankIn;
        this.voters = votersIn;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String nameIn){
        this.name = nameIn;
    }
    public double getRank(){
        return rank;

    }
    public  ArrayList<VotingPageKeerthiMuli> getVoters(){
        return this.voters;
    }
    public  void setVoters(ArrayList<VotingPageKeerthiMuli> voters){
        this.voters = voters;
    }
//@Override
//public String toString(){
 //  return String.format("%s,%.5f,%s", "ThisPageName = "+this.name +", ThisPageRank = "+this.rank +" ArrayListOfPages = " + this.voters.toString());

//}
@Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
    }

}