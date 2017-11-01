import java.util.*;
import java.io.*;

public class Search {
   private static Map<Integer,double[]> docVector = new HashMap<Integer,double[]>();
   private static int dicSize = 0;
   private static final int NUM_DOCS = 3204;

   public static void main(ArrayList<Query> parsedQuery) {
      File postingFile = new File("postingLists.txt");
      File dictionary = new File("dictionary.txt");
      File idfFile = new File("IDF.txt");
      
      //Scanner input = new Scanner(System.in);
      //System.out.println("Please Enter Query");
      
		for (Query q: parsedQuery){
		String token = q.getAbs() + " " + q.getPub();
      int queryID = q.getQueryID();
      String[] query = token.split("\\s+");
		//System.out.println(query);
      for (int i = 0; i < query.length; i++ ){
         query[i] = query[i].toLowerCase();
			query[i]  = query[i].replaceFirst("^[^a-zA-Z]+", "");
         query[i]  = query[i].replaceAll("[^a-zA-Z]+$", "");
         //System.out.println(query[i]);
      }
		
      //creates a dictionary map key = term; value = index of term in dictionary
      Map<String,Integer> dictionaryMap = createDicMap(dictionary);
      
      //creates an arraylist of all postings
      ArrayList<Posting> postingList = createPostingList(postingFile);
      
      //creates a document vector with weighted values
		if (docVector.isEmpty())
      createVector(postingList, dictionaryMap);
		
		//System.out.println("done vector");
      
      //create IDF from IDF.txt
      Map<String,Double> idf = createIDF(idfFile); 
      
      //creates the query vector from user input
      double[] queryVector = createQueryVector(query, dictionaryMap,idf);
		
      
      //stores the docID and score
      Map<Integer,Double> docScores = createDocScores(queryVector);
		
		
		docScores = sortByValue(docScores);
		
		//prints doc scores and ranking for manual search
		if (queryID == -1) {
			for (Map.Entry<Integer,Double> entry : docScores.entrySet()){
				System.out.println("Doc #: " + entry.getKey() + " Score: " + entry.getValue());
			}
		}
      
      try{
         FileWriter fw = new FileWriter("qret.text",true);
			int topRanked = 0;
			
         for (Map.Entry<Integer,Double> entry : docScores.entrySet()){
				if (topRanked == 50 ) {
					break;
				}
            fw.write(queryID + " " + entry.getKey() + "\r\n");
				topRanked++;
         }
			
      fw.close();
      
      } catch (IOException ioe) {
         System.out.println(ioe);
      }
      
		}
	}
 
	public static Map sortByValue(Map unsortedMap) {
		Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
   
   public static Map<String,Double> createIDF(File idfFile) {
      Map<String,Double> idf = new HashMap<String,Double>();
      try {
         Scanner input = new Scanner(idfFile);
         while (input.hasNext()) {
            String term = input.next();
            int df = input.nextInt();
            double idfValue = input.nextDouble();

            if (idfValue > 1.69) //threshhold for IDF
               idf.put(term,idfValue);
         }
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      
      return idf;
   }
   
   public static Map<Integer,Double> getRelDocScores(Map<Integer,Double> docScores){
      
      Map<Integer,Double> relScores = new HashMap<Integer,Double>();
      
      for (Map.Entry<Integer,Double> entry : docScores.entrySet()){
         int docID = entry.getKey();
         double docScore = entry.getValue();
         
         
         
         if (docScore != 0) 
         relScores.put(docID, docScore);
      }
      relScores = new TreeMap<Integer,Double>(relScores);
      return relScores;
      
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
   
   public static double[] createQueryVector(String[] query, Map<String,Integer> dictionaryMap, Map<String,Double> idf){
      double[] vector = new double[dicSize+1];
       for (int i = 0 ; i < dicSize ;i++){
               vector[i] = 0;
       }
      for (String q : query){
         if (dictionaryMap.containsKey(q) && idf.containsKey(q) ) {
          vector[dictionaryMap.get(q)]++;
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
         
         if (Double.isNaN(score)) score = 0;
			if (score != 0) {
         	docScores.put(docID, score);
			}
      }
      
		docScores = new TreeMap<Integer,Double>(docScores);
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

class ValueComparator implements Comparator {
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
}