import java.util.*;
import java.io.*;
import java.lang.*;


public class Eval {
   private static final String T_PREFIX = ".";
   private static final String T_ID = "I";
   private static final String T_ABS = "W";
   private static final String T_PUB = "N";
   
	private static int N_VALUE;
	private static int QUERY_COUNT = 0;
   
   public static void main(String[] args) {
      
      File queryText = new File("query.text");
      File qretFile = new File("qret.text");
      File qrelFile = new File("qrels.text");
      ArrayList<Query> parsedQuery = parseQuery(queryText);
      
      
      Scanner input = new Scanner(System.in);
      
      System.out.println("What do you want to do?");
      System.out.println("1. Run Manual Search");
      System.out.println("2. Run query.txt");
		
		String choice = input.nextLine();
      
		if (choice.equals("1")) {
			System.out.println("Insert query to search");
			String query = input.nextLine();
			//String[] searchArgs = {"1", query};
			ArrayList<Query> searchArgs = new ArrayList<Query>();
			searchArgs.add(new Query());
			searchArgs.get(0).setAbs(query);
			searchArgs.get(0).setQueryID(-1);
			Search.main(searchArgs);
			
			
		} else if (choice.equals("2")) {
         
         //creates qret.text by running Search with query.text as input
         //for (Query q : parsedQuery) {
            //String query = q.getAbs() + " " + q.getPub();
            //String[] searchArgs = {Integer.toString(q.getQueryID()), query};
         Search.main(parsedQuery);
         //}
			
			//System.out.println("done");
			Map<Double,Integer> qret = createQret(qretFile);
      	Map<Double,Integer> qrel = createQrel(qrelFile);
         
			ArrayList<Double> recall = new ArrayList<Double>();
			ArrayList<Double> precision = new ArrayList<Double>();
			
			//Map<queryID, MAPValue>
			Map<Integer, Double> mapValues = new HashMap<Integer,Double>();
			
			//Map<queryID, R-Precision>
			Map<Integer, Double> rpValues = new HashMap<Integer,Double>();
			
			int rCount = 0;
			int pCount = 0;
			
			int trackRet = 1;
			int trackRel = 1;
			double accumulator = 0;
			int countRel = 0;
			
			double lastQueryID = 0;
			double lastAccumulator = 0;
         
			//compare qret with qrel to calculate apValues
         for (Map.Entry<Double,Integer> entry : qret.entrySet()) {
				double retQueryID = entry.getKey();
				int retDocID = entry.getValue();

				if ((int)retQueryID != trackRet) {
					//System.out.println("retQueryID: " + retQueryID + " accummulator: " + accumulator + " countRel: " + countRel);
					mapValues.put((int)(retQueryID - 1),accumulator/(double)countRel);
					rpValues.put((int)(retQueryID - 1),(double)rCount/(double)countRel);
					pCount = 0;
					accumulator = 0;
					rCount = 0;
					trackRet ++;
					trackRel ++;
				}
				countRel = 0;
				pCount++;
				for(Map.Entry<Double,Integer> entryRel : qrel.entrySet()) {
					double relQueryID = entryRel.getKey();
					int relDocID = entryRel.getValue();
					
					if ((int)relQueryID == trackRel) {
						countRel ++;
					}
					
					if ((int)retQueryID == (int)relQueryID && retDocID == relDocID) {
						rCount++;
						double r = (double)rCount / (double)QUERY_COUNT;
						double p = (double)rCount / (double)pCount;
						accumulator += p;			
					}
				}
				lastQueryID = retQueryID ;
				lastAccumulator = accumulator;
			}
			//System.out.println("retQueryID: " + lastQueryID + " accummulator: " + lastAccumulator + " countRel: " + countRel);
			mapValues.put((int)lastQueryID,lastAccumulator/(double)countRel);
			rpValues.put((int)lastQueryID,(double)rCount/(double)countRel);
			
			double averageMAP = 0;
			double averageRP= 0;
			for (Map.Entry<Integer,Double> entry : mapValues.entrySet()) {
				averageMAP += entry.getValue();
				//System.out.println(entry.getKey() + " MAP: " + entry.getValue());
			}
			for (Map.Entry<Integer,Double> entry : rpValues.entrySet()) {
				averageRP += entry.getValue();
				//System.out.println(entry.getKey() + " Precision: " + entry.getValue());
			}
			
			averageMAP = averageMAP/(double)QUERY_COUNT;
			averageRP = averageRP/(double)QUERY_COUNT;
			
			System.out.println("Average MAP: " + averageMAP);
			System.out.println("Average Precision: " + averageRP);
			
      }
   }
	
	
   
   public static Map<Double,Integer> createQret(File file) {
      Map<Double,Integer> qret = new HashMap<Double,Integer>();
      
      try {
         Scanner input = new Scanner(file);
			int count = 1;
			double queryCount = 1;
         while (input.hasNext()) {
				double queryID = input.nextDouble();
				int docID = input.nextInt();
				if ((int)queryID == count) {
					queryCount += 0.01;
				} else if ((int)queryID != count) {
					int round = (int)queryCount;
					round ++;
					count++;
					queryCount = (double)round;
				}
            qret.put(queryCount,docID);
         }
         
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
		
		qret = new TreeMap<Double,Integer>(qret);

		return qret;
      
   }
   
   public static Map<Double,Integer> createQrel(File file) {
      Map<Double,Integer> qrel = new HashMap<Double,Integer>();
      try {
         Scanner input = new Scanner(file);
			
			int count = 1;
			double queryCount = 1;
         while (input.hasNext()) {
				double queryID = input.nextDouble();
				int docID = input.nextInt();
            int zero1 = input.nextInt();
            int zero2 = input.nextInt();
				if ((int)queryID == count) {
					queryCount += 0.01;
				} else if ((int)queryID != count) {
					int round = (int)queryCount;
					round ++;
					count++;
					queryCount = (double)round;
				}
				
				QUERY_COUNT = (int)queryCount;
            qrel.put(queryCount, docID);
            
         }
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
		
		qrel = new TreeMap<Double,Integer>(qrel);
		return qrel;
   }
   public static ArrayList<Query> parseQuery(File file) {
      ArrayList<Query> pc = new ArrayList<Query>();
      String state = "";
      int countQ = 0;
      try {
         Scanner input = new Scanner(file);
         while(input.hasNext()) {
            String line = input.nextLine();
				System.out.println("test: " + line);
				if (line != null && !line.isEmpty()) {
					if (line.substring(0,1).equals(T_PREFIX)) {
						state = line.substring(1,2);
						if (state.equals(T_ID)) {
							pc.add(new Query());
							pc.get(countQ).setQueryID(Integer.parseInt(line.substring(2).trim()));
							countQ ++;
						} 
					} else {
						switch (state) {
							case T_PUB:
								pc.get(countQ-1).setPub(line);
								break;
							case T_ABS:
								pc.get(countQ-1).setAbs(line);
								break;
						}               
					}            
				}
			}
			
			N_VALUE = countQ;
			
      } catch(FileNotFoundException exception) {
         System.out.println(exception);
      }
      return pc;
   }
   
}