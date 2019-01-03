package com.tompa.top10;

import com.tompa.roster.TeamUnify;
import com.tompa.top10.ExecuteStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class LoadRoster {

   static final String insertHY3SQL = "insert into roster (lastname,firstname,ussreg,gender,dob) values (?,?,?,?,?)";
   static final String deleteRoster = "delete from roster";
   private StringBuilder message = new StringBuilder();


   public String getMessage() {
      return this.message.toString();
   }

   public String getRoster() {
      StringBuilder ret = new StringBuilder();
      String queryAll = "select ussreg, dob, gender, lastname, firstname from roster order by lastname, firstname";
      ArrayList all = ExecuteStatement.executeQuery(queryAll, new String[0]);
      Iterator i$ = all.iterator();

      while(i$.hasNext()) {
         ArrayList arrayList = (ArrayList)i$.next();
         String sep = "";

         for(Iterator i$1 = arrayList.iterator(); i$1.hasNext(); sep = " ") {
            String s = (String)i$1.next();
            ret.append(sep);
            ret.append(s);
         }

         ret.append("\n");
      }

      return ret.toString();
   }

   public Date parseSd3Date(String mmddyyyy) {
      if(mmddyyyy != null && mmddyyyy.length() >= 8) {
         int mm = Integer.parseInt(mmddyyyy.substring(0, 2));
         int dd = Integer.parseInt(mmddyyyy.substring(2, 4));
         int yyyy = Integer.parseInt(mmddyyyy.substring(4, 8));
         return (new GregorianCalendar(yyyy, mm - 1, dd)).getTime();
      } else {
         return null;
      }
   }

   private boolean insertHY3Line(String line) {
      if(!line.startsWith("D1")) {
         return false;
      } else {
         this.message.append(line).append("\n");
         ArrayList arrayList = new ArrayList();
         String gender = line.substring(2, 3);
         String last = line.substring(8, 28).trim();
         String first = line.substring(28, 48).trim();
         String preferred = line.substring(48, 68).trim();
         String reg = line.substring(69, 83).trim().toUpperCase();
         String dob = line.substring(88, 96);
         if(preferred.length() != 0 && !preferred.equals(first)) {
            first = preferred;
         }

         System.out.println(last + "!" + first + "!" + reg + "!" + gender + "!" + dob);
         arrayList.add(last);
         arrayList.add(first);
         arrayList.add(reg);
         arrayList.add(gender);
         arrayList.add(dob);
         String[] array = new String[0];
         ExecuteStatement.executeUpdate("insert into roster (lastname,firstname,ussreg,gender,dob) values (?,?,?,?,?)", (String[])arrayList.toArray(array));
         return true;
      }
   }

   public int insertUSSID(String last, String first, String ussid, String gender) {
      ArrayList arrayList = new ArrayList();
      arrayList.add(last);
      arrayList.add(first);
      arrayList.add(ussid);
      arrayList.add(gender.substring(0, 1));
      String dob = ussid.substring(0, 4);
      String year = ussid.substring(4, 6);
      if(year.compareTo("50") < 0) {
         year = "20".concat(year);
      } else {
         year = "19".concat(year);
      }

      arrayList.add(dob.concat(year));
      String[] array = new String[0];
      return ExecuteStatement.executeUpdate("insert into roster (lastname,firstname,ussreg,gender,dob) values (?,?,?,?,?)", (String[])arrayList.toArray(array));
   }

   public boolean idExists(String id) {
      ArrayList rows = ExecuteStatement.executeQuery("select ussreg from roster where ussreg=?", new String[]{id});
      return rows != null && rows.size() > 0;
   }

   public static int deleteIdFromRoster(String id) {
      return ExecuteStatement.executeUpdate("delete from roster where ussreg=?", new String[]{id});
   }

   public void loadHY3(String filename) throws IOException {
      int n = 0;
      ExecuteStatement.executeUpdate("delete from roster", new String[0]);
      BufferedReader in = new BufferedReader(new FileReader(filename));

      String line;
      while((line = in.readLine()) != null) {
         if(this.insertHY3Line(line)) {
            ++n;
         }
      }

      in.close();
      this.message.append("Number of names inserted: ").append(String.valueOf(n));
   }

   public void loadTeamUnify(String filename) throws IOException {
      int n = 0;
      ExecuteStatement.executeUpdate("delete from roster", new String[0]);
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String fullName = "";
      String dob = "";
      String gender = "";

      String line;
      while((line = in.readLine()) != null) {
         line = TeamUnify.parseTeamUnify(line);
         if(line != null && this.insertHY3Line(line)) {
            ++n;
         }
      }

      in.close();
      this.message.append("Number of names inserted: ").append(String.valueOf(n));
   }

   public void loadTeamUnifySDIF(String filename) throws IOException {
      byte n = 0;

//      ExecuteStatement.executeUpdate("CREATE TABLE roster ( LASTNAME VARCHAR(255), FIRSTNAME VARCHAR(255), USSREG VARCHAR(255), GENDER VARCHAR(255), DOB VARCHAR(255))", new String[0]);

      ExecuteStatement.executeUpdate("delete from roster", new String[0]);
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String first = "";
      String last = "";
      String dob = "";
      String gender = "";

      String line;
      while((line = in.readLine()) != null) {
         if(line.startsWith("D0") || line.startsWith("D3")) {
            int len = Math.min(66, line.length());
            line = line.substring(0, len);
            String hy3;
            if(line.startsWith("D0")) {
               hy3 = line.substring(5, 39).trim();
               String[] arr = hy3.split(",");
               last = arr[0].trim().toUpperCase();
               first = arr[1].trim();
               dob = line.substring(55, 63).trim();
               gender = line.substring(65, 66);
            } else if(line.startsWith("D3")) {
               String id = line.substring(2, 16).trim();
               if(dob.length() == 8 && id.length() == 14) {
                  hy3 = String.format("D1%s%25s%20s%20s%15s%13s", new Object[]{gender, last, first, "", id, dob});
                  this.insertHY3Line(hy3);
               }
            }
         }
      }

      in.close();
      this.message.append("Number of names inserted: ").append(String.valueOf(n));
   }

   public static void main(String[] args) throws IOException {
      LoadRoster loadRoster = new LoadRoster();
      loadRoster.loadHY3(args[0]);
   }
}
