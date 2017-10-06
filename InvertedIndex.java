
import java.util.*;
import java.io.*;

public class InvertedIndex {
   
   private static final String T_PREFIX = ".";
   private static final String T_ID = "I";
   private static final String T_TITLE = "T";
   private static final String T_ABS = "W";
   private static final String T_PUB = "B";
   private static final String T_AUTH = "A";
	private static Map<String, ArrayList<Document>> term_location;
  
   public static void main(String [] args) 
   {
      Scanner input = new Scanner(System.in);
      System.out.println("Do you want to use stop words? (0 for no / 1 for yes)");
      int stop = input.nextInt();
      System.out.println("Do you want to use Porter's Stemming algorithm? (0 for no / 1 for yes)");
      int stem = input.nextInt();
      File documentCollection = new File("cacm.all");
      ArrayList<Document> parsedCollection = parseCollection(documentCollection);
      Map<String, Integer> dictionary = makeDictionary(parsedCollection, stop, stem);
      makePostingsLists(parsedCollection, dictionary);
      //System.out.println(dictionary);
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
   
   public static Map<String, Integer> makeDictionary(ArrayList<Document> pc, int stop, int stem) {
      Map<String, Integer> dictionary = new HashMap<String, Integer>();
		term_location = new HashMap<String,ArrayList<Document>>();
      Porter stemmer = new Porter();
      File file = new File("dictionary.txt");
      File stopFile = new File("common_words");
      //parses all terms in all documents
      for (final Document d : pc) {
         String doc = d.getTitle() + d.getAbs();
         Scanner input = new Scanner(doc);
         while (input.hasNext()) {
            String token = input.next(); 
            token = token.toLowerCase();
            //token = token.replaceFirst("^[^a-zA-Z]+", "");
            //token = token.replaceAll("[^a-zA-Z]+$", "");
            //System.out.println(token);
            if(token.matches(".*\\d+.*")){
               continue;
            }  
            if(stem == 1){ //stem the words
               token = stemmer.stripAffixes(token);
            }
            if(stop == 1) {
            	if(stopWords(stopFile,token)){
						continue;
               }
            }
	    
            if (!dictionary.containsKey(token)) {
               dictionary.put(token, 0);
					term_location.put(token, new ArrayList<Document>());
            }
         }
      }
      //sorts dictionary
      dictionary = new TreeMap<String,Integer>(dictionary);
		term_location = new TreeMap<String,ArrayList<Document>>();
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
            //token = token.replaceFirst("^[^a-zA-Z]+", "");
            //token = token.replaceAll("[^a-zA-Z]+$", "");
				if(stem == 1){ //stem the words
						token = stemmer.stripAffixes(token);
					}
					if(stop == 1) {
						if(stopWords(stopFile,token)){
							continue;
						}
					}
            //if temporary dictionary does not contain token, add to temp dic
            if (!dictionaryTemp.containsKey(token)) {
               dictionaryTemp.put(token, 0);
               if (dictionary.containsKey(token)) {
                  dictionary.put(token, dictionary.get(token) +1);
						if (term_location.get(token) == null) {
							term_location.put(token, new ArrayList<Document>());
						}
						term_location.get(token).add(d);
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
   
   public static boolean stopWords(File stop, String checkTerm) {
        try {	
		Scanner sw = new Scanner(stop);
		while (sw.hasNextLine()) {
			try {
				if (sw.next().equalsIgnoreCase(checkTerm))
					return true;
			} catch (NoSuchElementException e) {
                  }
		}
	} catch(FileNotFoundException exception) {
         System.out.println(exception);
        }
	
	return false;
   }
	

     
   public static void makePostingsLists(ArrayList<Document> pc, Map<String, Integer> dictionary) {
      ArrayList<Posting> postingList = new ArrayList<Posting>();
		ArrayList<Document> list = new ArrayList<Document>();

      File file = new File("postingLists.txt");
      try {
         FileWriter fw = new FileWriter(file);
         int addToList;
         for (Map.Entry<String, ArrayList<Document>> entry : term_location.entrySet()) {
            String term = entry.getKey();
            System.out.println(term);
					for (Document d : entry.getValue()) {
						addToList = 0;
						Posting posting = new Posting(term);
						posting.setDocID(d.getDocID());
						String doc = d.getTitle() + d.getAbs();
						//Scanner input = new Scanner(doc);
						String[] words = doc.split("\\s+");
						for (String w : words) {
							if (w.equalsIgnoreCase(term)) {
								posting.incrementTermFreq();
								addToList =1;
							}
						}
					
						if(addToList == 1) {
							//postingList.add(posting);
							fw.write(posting.getTerm() + " " + posting.getDocID() + " " + posting.getTermFreq());
							fw.write("\r\n");
						}
					}
				
         }
      } catch (IOException ioe) {
        System.out.println(ioe);
      }
   }
}
