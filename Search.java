import java.util.*;
import java.io.*;

public class Search {
   private static Map<Integer,double[]> docVector = new HashMap<Integer,double[]>();
   private static int dicSize = 0;
   private static final int NUM_DOCS = 3204;

   public static void main(String[] args) {
      File postingList = new File("postingLists.txt");
      File dictionary = new File("dictionary.txt");
      
      Scanner input = new Scanner(System.in);
      String query;
      
      System.out.println("Please Enter Query");
      query = input.next();

      Map<String,Integer> dictionaryMap = createDicMap(dictionary);
      
      createVector(postingList, dictionaryMap);
      
      for (Map.Entry<Integer,double[]> entry : docVector.entrySet()) {
         int docID = entry.getKey();
         double[] vector = entry.getValue();
         
         
         for(double v : vector)
            if(v != 0)
            //System.out.print(v + ", ");
         
         
         System.out.println();
      }
      
   }
   
   public static Map<String,Integer> createDicMap(File dictionary){
      Map<String,Integer> dictionaryMap = new HashMap<String,Integer>();
      try {
         Scanner input = new Scanner(dictionary);
         int count = 0; //doc #

         while (input.hasNext()){
            String term = input.next();
            int df = input.nextInt();

            dictionaryMap.put(term,count);
            count++;
            dicSize++;
         }
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      
      return dictionaryMap;
         
   }
   
   public static void createVector(File postingList, Map<String,Integer> dictionaryMap){
      try {
         Scanner input = new Scanner(postingList);
         int docIter = 1; //iterates through all document numbers 1-3024
         int count = 0; //for iterating through vector elements
         
         
         while (docIter != NUM_DOCS) {
            double[] vector = new double[dicSize];
         
            for (int i = 0 ; i < dicSize ;i++){
               vector[i] = 0;
            }
            while (input.hasNext()) {
               String term = input.next();
               int docID = input.nextInt();
               int freq = input.nextInt();
               double termFreq = input.nextDouble();
               double weight = input.nextDouble();
               if (docID == docIter){
                  vector[dictionaryMap.get(term)] = weight;
                  System.out.println("docID: " + docID + " term: " + dictionaryMap.get(term));
               }
            }
            docVector.put(docIter,vector);
            docIter++;
         }
      
       } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
   }
}