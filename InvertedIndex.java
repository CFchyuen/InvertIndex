
import java.util.*;
import java.io.*;

public class InvertedIndex {
   
   private static final String T_PREFIX = ".";
   private static final String T_ID = "I";
   private static final String T_TITLE = "T";
   private static final String T_ABS = "W";
   private static final String T_PUB = "B";
   private static final String T_AUTH = "A";
  
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
      
      System.out.println(parsedCollection.get(19).getAbs());
   }

   public static ArrayList<Document> parseCollection(File file) {
      ArrayList<Document> pc = new ArrayList<Document>();
      String state = "";
      int countDoc = 0;
      
      try { 
      
         Scanner input = new Scanner(file);

         while(input.hasNext()) {
            String line = input.nextLine();
            if (line.substring(0,1).equals(T_PREFIX)) {
               state = line.substring(1,2);
               if (state.equals(T_ID)) {
                  pc.add(new Document());
                  pc.get(countDoc).setDocID(Integer.parseInt(line.substring(2).trim()));
                  countDoc ++;
               } 
            } else {
               switch (state) {
                  case T_TITLE:
                     pc.get(countDoc-1).setTitle(line);
                     break;
                  case T_AUTH:
                     pc.get(countDoc-1).setAuth(line);
                     break;
                  case T_PUB:
                     pc.get(countDoc-1).setPubDate(line);
                     break;
                  case T_ABS:
                     pc.get(countDoc-1).setAbs(line);
                     break;
               }               
            }            
         }
         
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      
      return pc;
   }
   
   public static Map<String,String> makeDictionary(File file) {
      Map<String, String> dictionary = new HashMap<String, String>();
      
      
      
      return dictionary;
   }
   
   public void sortAlphabetically(Map<String, String> dictionary)
   {
   
   }
   
   
   
}