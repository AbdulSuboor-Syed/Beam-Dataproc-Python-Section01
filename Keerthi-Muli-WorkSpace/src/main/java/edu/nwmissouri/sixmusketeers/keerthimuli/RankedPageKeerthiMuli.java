package edu.nwmissouri.sixmusketeers.keerthimuli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.beam.sdk.values.KV;
// Keeps track of a page , its rank and its voting pages.
public class RankedPageKeerthiMuli implements Serializable, Comparator<KV<Double,String>> {
    String name = "unknown.md";
    Double rank =1.000;
    ArrayList<VotingPageKeerthiMuli> voters = new ArrayList<VotingPageKeerthiMuli>();

    public RankedPageKeerthiMuli() {
    
    }
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
    @Override
    public int compare(KV<Double, String> r1, KV<Double, String> r2) {
        double rank1 = r1.getKey();
        double rank2 = r2.getKey();
        if (rank1 > rank2) {
            return 1;
        } else if(rank1 < rank2) {
            return -1;
        }else{
            return 0;
        }
    }
}