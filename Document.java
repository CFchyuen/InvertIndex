public class Document {
   private int docID;
   private String title;
   private String abs;
   private String author;
   private String pubDate;
   
   public Document () {
      docID = 0;
      title = "";
      author = "";
      abs = "";
      pubDate = "";
   }
   
   public void setDocID(int d) {
      docID = d;
   }
   
   public void setTitle(String t) {
      title = t;
   }
   
   public void setAuth(String au){
      author += au;
   }
   
   public void setAbs(String ab){
      abs += " " + ab;
   }
   
   public void setPubDate(String p) {
      pubDate += p;
   }
   
   public int getDocID() {
      return docID;
   }
   
   public String getTitle() {
      return title;
   }
   
   public String getAuth(){
      return author;
   }
   
   public String getAbs(){
      return abs;
   }
   
   public String getPubDate() {
      return pubDate;
   }
   
}