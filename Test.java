import java.util.*;
import java.io.*;

public class Test {

   private static final String T_PREFIX = ".";
   private static final String T_ID = "I";
   private static final String T_TITLE = "T";
   private static final String T_ABS = "W";
   private static final String T_PUB = "B";
   private static final String T_AUTH = "A";

   public static void main(String[] args) {

      Boolean found;
      String query;
      String term = "";
      String title = "";
      int docFreq = 0 ;
      int docId = 0;
      int termFreq = 0;
      int validQueryCount = 0;
      long tStart;
      long tEnd;
      long tDelta;
      double elapsedSeconds = 0;
      double totalTime = 0;
      double averageTime = 0;

      if (args.length != 2) {
         System.out.println("Invalid arguments: e.g java Test dictionary.txt postingLists.txt");
      }

      File dictionary = new File(args[0]);
      File postingLists = new File(args[1]);

      File documentCollection = new File("cacm.all");
      ArrayList<Document> parsedCollection = parseCollection(documentCollection);

      Scanner input = new Scanner(System.in);
      while(true) {
         found = false;
         System.out.println("Enter your single term query:");
         query = input.nextLine();
         if (query.equals("ZZEND")){
            if(validQueryCount > 0){
               averageTime = totalTime / (double) validQueryCount;
               System.out.println("Program ending. Average valid query search time was " + averageTime + " seconds.");
            } else {
               System.out.println("Program ending. There were no valid queries.");
            }
            break;
         }
         else {
            tStart = System.currentTimeMillis();
            try {
               Scanner dict = new Scanner(dictionary);
               while(dict.hasNextLine()) {
                  try {
                     term = dict.next();
                     docFreq = dict.nextInt();
                  } catch (NoSuchElementException e) {
                  }
                  if(term.equals(query)){
                     found = true;
                     System.out.println("Your query '" + term + "' is found in " + docFreq + " documents.");
                     try {
                        Scanner post = new Scanner(postingLists);
                        while(post.hasNextLine()) {
                           try {
                              term = post.next();
                              docId = post.nextInt();
                              termFreq = post.nextInt();
                           }  catch (NoSuchElementException e) {
                           }
                           if (term.equals(query)){
                             int firstoccurr = 0;
                             String firstTen = "";
                             ArrayList<Integer> positions = new ArrayList<Integer>();
                              for (Document d : parsedCollection) {
                                firstoccurr = 0;
                                 if(d.getDocID() == docId){
                                    title = d.getTitle();
                                    String[] words = d.getAbs().split("\\s+");


                                    for (int i = 0; i < words.length; i++) {
                                      if (words[i].equalsIgnoreCase(query) && firstoccurr == 0){
                                        positions.add(i);
                                        int count = 0;
                                        firstTen = words[i];
                                        for (int j = i+1; j < words.length; j++) {
                                          if (count > 8) break;
                                          firstTen += " " + words[j];
                                          count++;
                                        }
                                        firstoccurr++;

                                      } else if (words[i].equalsIgnoreCase(query)){
                                        positions.add(i);
                                      }

                                    }
                                 }
                              }
                              System.out.println("In document #" + docId + ", '" + query + "' is found " + termFreq + " time(s).");
                              if (positions.size() == 0) {
                                System.out.println("Occurs in title: " + title);
                              } else {
                                System.out.println("Occurred in positons: " + positions);
                              }
                              System.out.println("10 terms in context to query: " + firstTen);
                              System.out.println();
                           }
                        }
                        break;
                     } catch(FileNotFoundException exception) {
                        System.out.println(exception);
                     }
                  }
               }
            } catch(FileNotFoundException exception) {
               System.out.println(exception);
            }
         }
         if(found){
            tEnd = System.currentTimeMillis();
            tDelta = tEnd - tStart;
            elapsedSeconds = tDelta / 1000.0;
            System.out.println("That took " + elapsedSeconds + " seconds.");
            totalTime += elapsedSeconds;
            validQueryCount++;
         } else {
            System.out.println("Query not found. Look for another or type ZZEND to stop the program.");
         }
      }
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
}
