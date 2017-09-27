
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
      
      ArrayList<Document> parsedCollection = parseCollection(documentCollection);
      Map<String, String> dictionary = makeDictionary(documentCollection);
   }

   public static ArrayList<Document> parseCollection(File file) {
      ArrayList<Document> pc = new ArrayList<Document>();
      
      
      
      return pc;
   }
   public static Map<String,String> makeDictionary(File file) {
      Map<String, String> dictionary = new HashMap<String, String>();
      
      try { 
      
         Scanner input = new Scanner(file);

         while(input.hasNext()) {
            String nextToken = input.next();
            //or to process line by line
            String nextLine = input.nextLine();
            System.out.println(nextLine);
         }
         
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      
      return dictionary;
   }
   
   public void sortAlphabetically(Map<String, String> dictionary)
   {
   
   }
   
   
   
}