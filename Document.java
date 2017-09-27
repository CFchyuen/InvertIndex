public class Document {
   private int docID;
   private String title;
   private String abs;
   private String author;
   private String pubDate;
   
   public Document () {
   }
   
   public Document(int d, String t, String au, String ab, String p){
      docID = d;
      title = t;
      author = au;
      abs = ab;
      pubDate = p;
   }
}