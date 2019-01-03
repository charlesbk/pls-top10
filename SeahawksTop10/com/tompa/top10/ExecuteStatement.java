package com.tompa.top10;

import com.tompa.top10.Pool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExecuteStatement {

   public static ArrayList<ArrayList<String>> executeQuery(String query, String ... params) {
      ArrayList ret = new ArrayList();
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement(query);

         int e;
         for(e = 0; e < params.length; ++e) {
            ps.setString(e + 1, params[e]);
         }

         rs = ps.executeQuery();
         e = rs.getMetaData().getColumnCount();

         while(rs.next()) {
            ArrayList row = new ArrayList();

            for(int i = 1; i <= e; ++i) {
               String s = rs.getString(i);
               if(s == null) {
                  s = "";
               }

               row.add(s);
            }

            ret.add(row);
         }
      } catch (SQLException var26) {
         var26.printStackTrace();
      } finally {
         try {
            rs.close();
         } catch (Exception var25) {
            ;
         }

         try {
            ps.close();
         } catch (Exception var24) {
            ;
         }

         try {
            conn.close();
         } catch (Exception var23) {
            ;
         }

      }

      return ret;
   }

   public static int executeUpdate(String statement, String ... params) {
      int ret = 0;
      Connection conn = null;
      PreparedStatement ps = null;
      Object rs = null;

      System.out.println("==== executeUpdate: " + statement);

      try {
         conn = Pool.getConnection();

         System.out.println(statement);

         ps = conn.prepareStatement(statement);

         for(int e = 0; e < params.length; ++e) {
            ps.setString(e + 1, params[e]);
         }

         ret = ps.executeUpdate();
      } catch (SQLException var23) {
         var23.printStackTrace();
      } finally {
         try {
            ((ResultSet)rs).close();
         } catch (Exception var22) {
            ;
         }

         try {
            ps.close();
         } catch (Exception var21) {
            ;
         }

         try {
            conn.close();
         } catch (Exception var20) {
            ;
         }

      }

      return ret;
   }
}
