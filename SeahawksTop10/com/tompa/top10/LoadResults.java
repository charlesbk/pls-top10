package com.tompa.top10;

import com.tompa.top10.ExecuteStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LoadResults {

//   private static String createTable = "";
   private static String insertRow = "";
   private static String deleteMeet = "";
   private static String deleteAll = "";


   public LoadResults(String tableName) {
//      createTable = "CREATE TABLE " + tableName
//              + " ("
//              + "GENDER VARCHAR(255),"
//              + "COURSE VARCHAR(255),"
//              + "AGEGROUP VARCHAR(255),"
//              + "DISTANCE VARCHAR(255),"
//              + "STROKE VARCHAR(255),"
//              + "NAME VARCHAR(255),"
//              + "TIMESEC DOUBLE,"
//              + "SEASON VARCHAR(255),"
//              + "USSID VARCHAR(255),"
//              + "MEETDATE INT,"
//              + "MEETNAME VARCHAR(255)"
//              + ")";

      insertRow = "INSERT INTO ".concat(tableName).concat("(gender,course,agegroup,distance,stroke,name,timesec,season,ussid,meetdate,meetname) values (?,?,?,?,?,?,?,?,?,?,?)");
      deleteMeet = "DELETE FROM ".concat(tableName).concat(" WHERE meetdate=? and meetname=?");
      deleteAll = "DELETE FROM ".concat(tableName);
   }

   public void clearMeet(int meetDate, String meetName) {
      ExecuteStatement.executeUpdate(deleteMeet, new String[]{String.valueOf(meetDate), meetName});
   }

   public void insertRow(ArrayList<String> columns) {
      for(int array = columns.size(); array < 11; ++array) {
         columns.add("");
      }

      String[] var3 = new String[0];

      System.out.println("==== " + insertRow);

      ExecuteStatement.executeUpdate(insertRow, (String[])columns.toArray(var3));
   }

   public void insertLine(String line) {
      ArrayList columns = new ArrayList();
      StringTokenizer st = new StringTokenizer(line, "\t");

      while(st.hasMoreTokens()) {
         String token = st.nextToken();
         columns.add(token);
      }

      this.insertRow(columns);
   }

   public void load(String filename) throws IOException {
//      System.out.println("---- createTable " + createTable);
//      ExecuteStatement.executeUpdate(createTable, new String[0]);

      ExecuteStatement.executeUpdate(deleteAll, new String[0]);
      BufferedReader in = new BufferedReader(new FileReader(filename));

      int n;
      String line;
      for(n = 0; (line = in.readLine()) != null; ++n) {
         System.out.println(line);
         this.insertLine(line);
      }

      in.close();
      System.out.println("Lines processed: ".concat(String.valueOf(n)));
   }


   public static void main(String[] args) throws IOException {
      LoadResults top10 = new LoadResults("top10");
      top10.load(args[0]);
   }

}
