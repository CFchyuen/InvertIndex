import java.util.*;
import java.io.*;

public class Search {
   private static Map<Integer,double[]> docVector = new HashMap<Integer,double[]>();
   private static int dicSize = 0;
   private static final int NUM_DOCS = 3204;

   public static void main(String[] args) {
      File postingFile = new File("postingLists.txt");
      File dictionary = new File("dictionary.txt");
      
      Scanner input = new Scanner(System.in);
      String[] query;
      
      System.out.println("Please Enter Query");
      query = input.nextLine().split("\\s+");
      
      //creates a dictionary map key = term; value = index of term in dictionary
      Map<String,Integer> dictionaryMap = createDicMap(dictionary);
      
      //creates an arraylist of all postings
      ArrayList<Posting> postingList = createPostingList(postingFile);
      
      //creates a document vector with weighted values
      createVector(postingList, dictionaryMap);
      
      //creates the query vector from user input
      double[] queryVector = createQueryVector(query, dictionaryMap);
      
      //stores the docID and value of the 
      Map<Integer,Double> docScores = createDocScores(queryVector);
      
      for (Map.Entry<Integer,Double> entry : docScores.entrySet()){
         System.out.println(entry.getKey() + ": " + entry.getValue());
      }
      
      
      /**
      try {
         FileWriter fw = new FileWriter("queryvector.txt");

         for (double q : queryVector) {
            fw.write(q + ",");
         }
         
          fw.close();
      } catch (IOException ioe) {
         System.out.println(ioe);
      }
      */
         
      
   }
   
   public static Map<String,Integer> createDicMap(File dictionary){
      Map<String,Integer> dictionaryMap = new HashMap<String,Integer>();
      try {
         Scanner input = new Scanner(dictionary);
         int count = 1; //doc #

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
   
   public static ArrayList<Posting> createPostingList(File posting) {
      ArrayList<Posting> pl = new ArrayList<Posting>();
      try{
         Scanner input = new Scanner(posting);
         int count = 0;
         while (input.hasNext()) {
            pl.add(new Posting(input.next()));
                  int docID = input.nextInt();
                  int freq = input.nextInt();
                  double termFreq = input.nextDouble();
                  double weight = input.nextDouble();

            pl.get(count).setDocID(docID);
            pl.get(count).setFreq(freq);
            pl.get(count).setTermFreq(termFreq);
            pl.get(count).setWeight(weight);
            //System.out.println("created:" + count);
            count++;
         }
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      
      return pl;
   }
   
   public static double[] createQueryVector(String[] query, Map<String,Integer> dictionaryMap){
      double[] vector = new double[dicSize+1];
       for (int i = 0 ; i < dicSize ;i++){
               vector[i] = 0;
       }
      for (String q : query){
         if (dictionaryMap.containsKey(q)) {
          vector[dictionaryMap.get(q)] = 1;
         }
      }
      
       
      return vector;
      
   }
   public static void createVector(ArrayList<Posting> pl, Map<String,Integer> dictionaryMap){
         int docIter = 1; //iterates through all document numbers 1-3024
         int count = 0; //for iterating through vector elements
         
         
         while (docIter <= NUM_DOCS) {

            double[] vector = new double[dicSize+1];
         
            for (int i = 0 ; i < dicSize ;i++){
               vector[i] = 0;
            }
            
            for (Posting p : pl) {
               String term = p.getTerm();
               int docID = p.getDocID();
               int freq = p.getFreq();
               double termFreq = p.getTermFreq();
               double weight = p.getWeight();
               if (docIter==docID){
                  vector[dictionaryMap.get(term)] = weight;
                  //System.out.println("docID: " + docID + " term #: " + dictionaryMap.get(term));
               }
            }
            docVector.put(docIter,vector);
            docIter++;
         } 
   }
   
   public static Map<Integer,Double> createDocScores(double[] queryVector) {
      Map<Integer,Double> docScores = new HashMap<Integer,Double>();
      
      for (Map.Entry<Integer,double[]> entry : docVector.entrySet()){
         int docID = entry.getKey();
         double[] vectorA = entry.getValue();
         double score = cosineSimilarity(vectorA, queryVector);
         
         docScores.put(docID, score);
      }
      
      return docScores;
   }
   
   public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
       double dotProduct = 0.0;
       double normA = 0.0;
       double normB = 0.0;
       for (int i = 0; i < vectorA.length; i++) {
           dotProduct += vectorA[i] * vectorB[i];
           normA += Math.pow(vectorA[i], 2);
           normB += Math.pow(vectorB[i], 2);
       }   
       return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
   }
}