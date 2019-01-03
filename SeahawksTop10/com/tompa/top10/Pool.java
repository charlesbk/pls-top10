package com.tompa.top10;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

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
      String dbURLEmbedded = "jdbc:derby:firstDB";
      String dbClassEmbedded = "org.apache.derby.jdbc.EmbeddedDriver";
//      String dbURLNetwork = "jdbc:derby://localhost:1527/firstDB";
//      String dbClassNetwork = "org.apache.derby.jdbc.ClientDriver";
      String dbClass = dbClassEmbedded;
      DriverAdapterCPDS cpds = new DriverAdapterCPDS();

      try {
         cpds.setDriver(dbClass);
      } catch (ClassNotFoundException var8) {
         var8.printStackTrace();
      }

      cpds.setUrl(dbURLEmbedded);
      SharedPoolDataSource tds = new SharedPoolDataSource();
      tds.setConnectionPoolDataSource(cpds);
      tds.setMaxActive(10);
      tds.setMaxWait(50);
      ds = tds;
   }
}
