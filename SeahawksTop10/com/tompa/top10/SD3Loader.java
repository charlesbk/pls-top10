package com.tompa.top10;

import com.tompa.top10.ExecuteStatement;
import com.tompa.top10.LoadResults;
import com.tompa.top10.SD3Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SD3Loader {

   private HashMap<String, String> roster = new HashMap();
   private HashMap<String, String> notOnRoster = new HashMap();
   private HashMap<String, String> notSeahawk = new HashMap();
   private HashMap<String, String> ussidMissing = new HashMap();
   private SD3Data sd3Data = new SD3Data();
   private StringBuilder messages = new StringBuilder();


   public String getMessages() {
      return this.messages.toString();
   }

   private void loadRosterHash() {
      ArrayList rosterRows = ExecuteStatement.executeQuery("select USSREG,LASTNAME,FIRSTNAME from roster order by lastname ", new String[0]);
      Iterator i$ = rosterRows.iterator();

      while(i$.hasNext()) {
         ArrayList row = (ArrayList)i$.next();
         String id = (String)row.get(0);
         String name = ((String)row.get(1)).concat(" ").concat((String)row.get(2));
         if(id != null && id.length() > 0) {
            this.roster.put(id, name);
         } else {
            this.messages.append("USSID missing on roster: ").append(name).append("\n");
         }
      }

   }

   public void checkSelectedTeam(String ussid, String teamCode, String line, String selectedTeam) {
      String nameOnRoster;
      if(ussid == null) {
         nameOnRoster = SD3Data.sd3indtime_name(line);
         if(this.ussidMissing.get(nameOnRoster) == null) {
            this.ussidMissing.put(nameOnRoster, nameOnRoster);
            this.messages.append("USSID missing in results (skipping): ").append(nameOnRoster).append("\n");
         }

      } else {
         nameOnRoster = (String)this.roster.get(ussid);
         String name;
         if(nameOnRoster != null) {
            this.sd3Data.parseIndividualTimeRecord(ussid, line, nameOnRoster);
            if(!selectedTeam.equals(teamCode) && this.notSeahawk.get(ussid) == null) {
               name = SD3Data.sd3indtime_name(line);
               this.notSeahawk.put(ussid, name);
               this.messages.append("NOT ATTACHED (including): ").append(ussid).append(" ").append(name).append("\n");
            }
         } else if(selectedTeam.equals(teamCode) && this.notOnRoster.get(ussid) == null) {
            name = SD3Data.sd3indtime_name(line);
            this.notOnRoster.put(ussid, name);
            this.messages.append("NOT ON ROSTER (skipping): ").append(ussid).append(" ").append(name).append("\n");
         }

      }
   }

   public boolean loadTeamTimes(String filename, String selectedTeam) throws IOException {
      this.loadRosterHash();
      boolean ret = true;
      File file = new File(filename);
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;

      try {
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);
         String teamCode = "";
         String lineBeforeID = "";
         String ussid = null;
         String name = null;

         String e;
         while((e = bufferedReader.readLine()) != null) {
            if(e.startsWith("B1")) {
               this.sd3Data.parseMeetRecord(e);
            } else if(e.startsWith("C1")) {
               teamCode = SD3Data.sd3Team_Code(e);
            } else if(e.startsWith("D3")) {
               ussid = SD3Data.sd3Id_id(e);
               if(ussid != null) {
                  ussid = SD3Data.sd3Id_id(e).toUpperCase();
                  this.checkSelectedTeam(ussid, teamCode, lineBeforeID, selectedTeam);
               }
            } else if(!e.startsWith("G0") && e.startsWith("D0")) {
               String iter = SD3Data.sd3indtime_name(e);
               String loadMeet = SD3Data.sd3indtime_age(e);
               if(!iter.equals(name)) {
                  name = iter;
                  lineBeforeID = e;
                  ussid = null;
               } else {
                  this.checkSelectedTeam(ussid, teamCode, e, selectedTeam);
               }

               if(loadMeet.length() < 1 || !Character.isDigit(loadMeet.charAt(0))) {
                  this.messages.append("ERROR. Uknown age: ").append(e);
               }
            }
         }

         this.messages.append("FINISHED LOAD. Number valid of splashes: ".concat(String.valueOf(this.sd3Data.getNumValidTimes())).concat("\n"));
         System.out.println("FINISHED LOAD. Number valid of splashes: ".concat(String.valueOf(this.sd3Data.getNumValidTimes())));
         Iterator iter1 = this.sd3Data.resultsIterator();
         LoadResults loadMeet1 = new LoadResults("meetresults");
         loadMeet1.clearMeet(this.sd3Data.getMeetDate(), this.sd3Data.getMeetName());

         while(iter1.hasNext()) {
            ArrayList rec = (ArrayList)iter1.next();
            loadMeet1.insertRow(rec);
         }
      } finally {
         try {
            bufferedReader.close();
         } catch (IOException var28) {
            ;
         } catch (Exception var29) {
            ;
         }

         try {
            fileReader.close();
         } catch (IOException var26) {
            ;
         } catch (Exception var27) {
            ;
         }

      }

      return ret;
   }

   public int getMeetDate() {
      return this.sd3Data.getMeetDate();
   }

   public String getMeetName() {
      return this.sd3Data.getMeetName();
   }
}
