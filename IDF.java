import java.util.*;
import java.io.*;
import java.lang.*;

public class IDF {
   private String term;
   private int df;
   private double idf;
   
   public IDF() {
      term = "";
      df = 0;
      idf = 0;
   }
   
   public void setTerm(String t){
      term = t;
   }
   
   public void setDF(int d){
      df = d;
   }
   
   public void setIDF(int N) {
      idf = Math.log10(N/df);
   }
   
   public String getTerm(){
      return term;
   }
   
   public int getDF() {
      return df;
   }
   
   public double getIDF() {
      return idf;
   }

   
}