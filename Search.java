import java.util.*;
import java.io.*;

public class Search {

   public static void main(String[] args) {
      File postingList = new File("postingLists.txt");
      File dictioinary = new File("dictionary.txt");
      
      countTermFreq(postingList);
   }
   
   public static void countTermFreq(File postingList){
      Scanner input = new Scanner(postingList);
      
      while (input.hasNextLine()) {
         String term = input.next();
         int doc = input.next();
         int freq = input.next();
         
         
      }
   }
}