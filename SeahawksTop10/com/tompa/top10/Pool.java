package com.tompa.top10;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import java.io.FileWriter;
import java.io.IOException;

public class Pool {

   private static DataSource ds;
   private static final boolean EMBEDDED = true;


   public static Connection getConnection() {
      Connection conn = null;

      try {
         conn = ds.getConnection();
      } catch (SQLException var2) {
         var2.printStackTrace();
      }

      return conn;
   }

   static {
      String dbURLEmbedded = "jdbc:derby:firstDB"; //This is for debugging at IntelliJ
      if(System.getProperty("user.dir").contains("artifacts")) {
         //This is for release and running without IntelliJ: "% java -jar SeahawksTop10.jar" under the folder of "seaHawk/pls-top10/SeahawksTop10/out/artifacts/SeahawksTop10_jar"
         //dbURLEmbedded = "jdbc:derby:../../production/SeahawksTop10/firstDB";

         //This is for release and running without IntelliJ: "% java -jar SeahawksTop10.jar" under the folder of "seaHawk/pls-top10/SeahawksTop10/out/artifacts/SeahawksTop10_jar"
         dbURLEmbedded = "jdbc:derby:../../../firstDB";
      }
      String dbClassEmbedded = "org.apache.derby.jdbc.EmbeddedDriver";
//      String dbURLNetwork = "jdbc:derby://localhost:1527/firstDB";
//      String dbClassNetwork = "org.apache.derby.jdbc.ClientDriver";
      String dbClass = dbClassEmbedded;
      DriverAdapterCPDS cpds = new DriverAdapterCPDS();
      //writeToLogFile(System.getProperty("user.dir"));
      try {
         cpds.setDriver(dbClass);
      } catch (ClassNotFoundException var8) {
         var8.printStackTrace();
         writeToLogFile(var8.getMessage());
      }

      cpds.setUrl(dbURLEmbedded);
      SharedPoolDataSource tds = new SharedPoolDataSource();
      tds.setConnectionPoolDataSource(cpds);
      tds.setMaxActive(10);
      tds.setMaxWait(50);
      ds = tds;
   }

   public static void writeToLogFile(String message) {
      FileWriter fileWriter = null;
      try {
         fileWriter = new FileWriter("LogFile.txt");
         fileWriter.write(message);
      } catch (Exception e) {
         e.printStackTrace();
      }finally {
         try {
            if (fileWriter != null) {
               fileWriter.flush();
               fileWriter.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
