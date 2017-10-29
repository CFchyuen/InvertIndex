import java.lang.*;

public class Posting {
   
   private String term;
   private int docID;
   private int freq;
   private double termFreq;
   private double weight;
   private int pos; 
   private String highlight; //first 10 words

   public Posting(String word){
      term = word;
      docID = 0;
      freq = 0;
      termFreq = 0;
      weight = 0;
      pos = 0;
      highlight = "";
   }
   
   public String getTerm(){
      return term;
   }
   
   public int getDocID(){
      return docID;
   }
   
   public int getFreq(){
      return freq;
   }
   
   public double getTermFreq(){
      termFreq = 1 + Math.log10(freq);
      return termFreq;
   }
   
   public double getWeight(double idf){
      return termFreq * idf;
   }
   
   public double getWeight(){
      return weight;
   }
   
   public void setTerm(String t){
      term = t;  
   }
   public void setDocID(int id){
      docID = id;  
   } 
   public void incrementFreq(){
      freq++;
   }
   
   public void setFreq(int f){
      freq = f;  
   } 
   public void setTermFreq(double tf){
      termFreq = tf; 
   } 
   
   public void setWeight(double w){
      weight = w;  
   } 
}