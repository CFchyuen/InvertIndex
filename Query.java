public class Query {
   private int queryID;
   private String abs;
   private String pub;

   public Query () {
      queryID = 0;
      abs = "";
      pub = "";
   }
   
   public void setQueryID(int d) {
      queryID = d;
   }
   
   public void setPub(String p) {
      pub = p;
   }
   
   public void setAbs(String ab){
      abs += " " + ab;
   }
   
   public int getQueryID() {
      return queryID;
   }
   
   public String getAbs(){
      return abs;
   }
   
   public String getPub(){
      return pub;
   }
   
}