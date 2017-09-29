
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
      Map<String, Integer> dictionary = makeDictionary(parsedCollection);
      
      System.out.println(dictionary);
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
   
   public static Map<String, Integer> makeDictionary(ArrayList<Document> pc) {
      Map<String, Integer> dictionary = new HashMap<String, Integer>();
      File file = new File("dictionary.txt");
      
      //parses all terms in all documents
      for (final Document d : pc) {
         String doc = d.getTitle() + d.getAbs();
         Scanner input = new Scanner(doc);
         
         while (input.hasNext()) {
            String token = input.next(); 
            token = token.toLowerCase();
            token = token.replaceFirst("^[^a-zA-Z]+", "");
            token = token.replaceAll("[^a-zA-Z]+$", "");
            
            if (!dictionary.containsKey(token)) {
               dictionary.put(token, 0);
            }
         }
      }
      
      //sorts dictionary
      dictionary = new TreeMap<String,Integer>(dictionary);
      
      //for every document, create a temp dictionary to store terms
      //if term from temp dictionary is a term from dictionary (full), increate doc count
      int count = 0;
      for (final Document d : pc) {
         Map<String, Integer> dictionaryTemp = new HashMap<String,Integer>();
         String doc = d.getTitle() + d.getAbs();
         Scanner input = new Scanner(doc);

         while (input.hasNext()) {
            String token = input.next(); 
            token = token.toLowerCase();
            token = token.replaceFirst("^[^a-zA-Z]+", "");
            token = token.replaceAll("[^a-zA-Z]+$", "");
            if (!dictionaryTemp.containsKey(token)) {
               dictionaryTemp.put(token, 0);
               if (dictionary.containsKey(token)) {
                  dictionary.put(token, dictionary.get(token) +1);
               }
            }
         }

      }
      
      try { 
         FileWriter fw = new FileWriter(file);
         for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            fw.write(key + " " + value);     
            fw.write("\r\n");
         }
          fw.close();  
      } catch (IOException ioe) {
         System.out.println(ioe);
      }
      

      
      return dictionary;
   }
   
   public void sortAlphabetically(Map<String, String> dictionary)
   {
   
   }
   
   
   
}