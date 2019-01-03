package com.tompa.top10;

import com.tompa.top10.Pool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTables {

   public void createTable(String tableName, String tableFields) {
      String createStatement = "CREATE TABLE ".concat(tableName).concat(" (").concat(tableFields).concat(")");
      Connection conn = null;
      PreparedStatement ps = null;
      System.out.println("CREATING: ".concat(createStatement));

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement(createStatement);
         ps.execute();
      } catch (SQLException var19) {
         var19.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (Exception var18) {
            ;
         }

         try {
            conn.close();
         } catch (Exception var17) {
            ;
         }

      }

   }

   public static void main(String[] args) {
      String recordsFields = "gender varchar(8), course varchar(8), agegroup varchar(8), distance int, stroke varchar(8), name varchar(32), timesec decimal(10,2), season int, ussid varchar(21), meetdate int, meetname varchar(32) ";
      String rosterFields = "lastname varchar(32), firstname varchar(32), ussreg varchar(16), gender varchar(1), dob varchar(8) ";
      CreateTables createTables = new CreateTables();
      createTables.createTable("top10", recordsFields);
      createTables.createTable("meetresults", recordsFields);
      createTables.createTable("roster", rosterFields);
   }
}
