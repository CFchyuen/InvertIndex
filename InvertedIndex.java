
import java.util.*;
import java.io.*;

public class InvertedIndex {
  
   public static void main(String [] args) 
   {
      File documentCollection = new File("cacm.all");
      
      /**
      if(args.length > 0) {
         documentCollection = new File(args[0]);
      } else {
         System.out.println("Invalid input");
      } **/
      
      
      Map<String, String> dictionary = makeDictionary(documentCollection);
   }


   public static Map<String,String> makeDictionary(File file) {
      Map<String, String> dictionary = new HashMap<String, String>();
      
      
      
      
      return dictionary;
   }
   
   public void sortAlphabetically(Map<String, String> dictionary)
   {
   
   }
   
   
   
}