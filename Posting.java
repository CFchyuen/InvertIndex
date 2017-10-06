public class Posting {
   
   private String term;
   private int docID;
   private int termFreq;
   private int pos; 
   private String highlight; //first 10 words

   public Posting(String word){
      term = word;
      docID = 0;
      termFreq = 0;
      pos = 0;
      highlight = "";
   }
   
   public String getTerm(){
      return term;
   }
   
   public int getDocID(){
      return docID;
   }
   
   public int getTermFreq(){
      return termFreq;
   }
   
   public void setDocID(int id){
      docID = id;  
   } 
   public void incrementTermFreq(){
      termFreq++;
   }
}