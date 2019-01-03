package com.tompa.top10;

import com.tompa.top10.ExecuteStatement;
import com.tompa.top10.SwimFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TopTenUpdater {

   private HashMap<String, Double> timeCuts = new HashMap();
   private HashMap<String, Double> recordCuts = new HashMap();
   private HashMap<String, Boolean> modifiedGroups = new HashMap();
   private StringBuilder newRecords = new StringBuilder();
   private StringBuilder messages = new StringBuilder();


   public TopTenUpdater() {
      this.retrieveAllCuts();
   }

   public void clearUpdates() {
      this.modifiedGroups = new HashMap();
      this.newRecords = new StringBuilder();
      this.messages = new StringBuilder();
   }

   public String getMessages() {
      return this.newRecords.toString().concat(this.messages.toString());
   }

   private void retrieveCuts(String gender, String course, String agegroup, String stroke, String distance) {
      String query = "select timesec from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec";
      ArrayList topTimes = ExecuteStatement.executeQuery(query, new String[]{gender, course, agegroup, distance, stroke});
      if(topTimes.size() >= 1) {
         int len = topTimes.size();
         if(len > 10) {
            len = 10;
         }

         String group = gender.concat(" ").concat(course).concat(" ").concat(agegroup).concat(" ").concat(distance).concat(" ").concat(stroke);
         String cut = (String)((ArrayList)topTimes.get(len - 1)).get(0);
         this.timeCuts.put(group, Double.valueOf(len < 10?3599.0D:Double.parseDouble(cut)));
         cut = (String)((ArrayList)topTimes.get(0)).get(0);
         this.recordCuts.put(group, Double.valueOf(Double.parseDouble(cut)));
      }
   }

   private void retrieveAllCuts() {
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();
      String[] distances = SwimFormatter.getDistances();
      String[] strokes = SwimFormatter.getStrokes();

      for(int g = 0; g < genders.length; ++g) {
         String gender = genders[g];

         for(int i = 0; i < courses.length; ++i) {
            String course = courses[i];

            for(int j = 0; j < agegroups.length; ++j) {
               String agegroup = agegroups[j];

               for(int k = 0; k < strokes.length; ++k) {
                  String stroke = strokes[k];

                  for(int l = 0; l < distances.length; ++l) {
                     String distance = distances[l];
                     this.retrieveCuts(gender, course, agegroup, stroke, distance);
                  }
               }
            }
         }
      }

   }

   private boolean isNewTopGroupSwimmer(String gender, String course, String agegroup, String stroke, String distance, String ussid) {
      String query = "select ussid from top10 where gender=? and course=? and agegroup=?  and stroke=? and distance=? and ussid=?";
      ArrayList rows = ExecuteStatement.executeQuery(query, new String[]{gender, course, agegroup, stroke, distance, ussid});
      return rows.size() == 0;
   }

   private void updateMessage(String gender, String course, String agegroup, String stroke, String distance, double timesec, String name) {
      String updatedGroup = gender + " " + course + " " + agegroup + " " + distance + " " + stroke;
      if(this.modifiedGroups.get(updatedGroup) == null) {
         this.messages.append("UPDATED: ").append(updatedGroup).append("\n");
         this.modifiedGroups.put(updatedGroup, Boolean.valueOf(true));
      }

      if(timesec < ((Double)this.recordCuts.get(updatedGroup)).doubleValue()) {
         String strTime = SwimFormatter.formatTime(timesec);
         this.newRecords.append("NEW RECORD: ").append(updatedGroup).append(" ").append(name).append(" ").append(strTime).append("\n");
      }

   }

   private boolean insertTopGroupEntry(String gender, String course, String agegroup, String stroke, String distance, String ussid, double timesec, String name, String season, int meetDate, String meetName) {
      String insert = "insert into top10 (GENDER,COURSE,AGEGROUP,DISTANCE,STROKE,NAME,TIMESEC,SEASON,USSID,MEETDATE,MEETNAME) values (?,?,?,?,?,?,?,?,?,?,?)";
      int ret = ExecuteStatement.executeUpdate(insert, new String[]{gender, course, agegroup, distance, stroke, name, String.valueOf(timesec), season, ussid, String.valueOf(meetDate), meetName});
      this.updateMessage(gender, course, agegroup, stroke, distance, timesec, name);
      return ret == 1;
   }

   private boolean improvedTime(String gender, String course, String agegroup, String stroke, String distance, String ussid, double timesec) {
      String query = "select ussid from top10 where gender=? and course=? and agegroup=?  and stroke=? and distance=? and ussid=? and timesec>?";
      ArrayList rows = ExecuteStatement.executeQuery(query, new String[]{gender, course, agegroup, stroke, distance, ussid, String.valueOf(timesec)});
      return rows.size() > 0;
   }

   private boolean updateTopGroupEntry(String gender, String course, String agegroup, String stroke, String distance, String ussid, double timesec, String season, int meetDate, String meetName, String name) {
      String update = "update top10 set TIMESEC=?,SEASON=?,MEETDATE=?,MEETNAME=? where gender=? and course=? and agegroup=? and stroke=? and distance=? and ussid=?";
      int ret = ExecuteStatement.executeUpdate(update, new String[]{String.valueOf(timesec), season, String.valueOf(meetDate), meetName, gender, course, agegroup, stroke, distance, ussid});
      this.updateMessage(gender, course, agegroup, stroke, distance, timesec, name);
      return ret == 1;
   }

   private boolean possibleNewEntry(String gender, String course, String agegroup, String stroke, String distance, String ussid, double timesec, String name, String season, int meetDate, String meetName) {
      boolean newEntry = false;
      if(this.isNewTopGroupSwimmer(gender, course, agegroup, stroke, distance, ussid)) {
         this.insertTopGroupEntry(gender, course, agegroup, stroke, distance, ussid, timesec, name, season, meetDate, meetName);
         newEntry = true;
      } else if(this.improvedTime(gender, course, agegroup, stroke, distance, ussid, timesec)) {
         this.updateTopGroupEntry(gender, course, agegroup, stroke, distance, ussid, timesec, season, meetDate, meetName, name);
         newEntry = true;
      }

      return newEntry;
   }

   public boolean updateFromDatabase(int meetDate, String meetName) {
      String query = "select gender, course, ageGroup, distance, stroke, name, timesec, season, ussid from meetresults where meetdate=? and meetname=? order by course, gender, agegroup, stroke, distance, timesec";
      ArrayList results = ExecuteStatement.executeQuery(query, new String[]{String.valueOf(meetDate), meetName});
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         ArrayList row = (ArrayList)i$.next();
         String gender = (String)row.get(0);
         String course = (String)row.get(1);
         String agegroup = (String)row.get(2);
         String distance = (String)row.get(3);
         String stroke = (String)row.get(4);
         String name = (String)row.get(5);
         double timesec = Double.parseDouble((String)row.get(6));
         String season = (String)row.get(7);
         String ussid = (String)row.get(8);
         this.applyResult(meetDate, meetName, course, gender, agegroup, distance, stroke, name, season, ussid, timesec);
      }

      return true;
   }

   public boolean applyResult(int meetDate, String meetName, String course, String gender, String agegroup, String distance, String stroke, String name, String season, String ussid, double timesec) {
      String ageGroup = gender.concat(" ").concat(course).concat(" ").concat(agegroup).concat(" ").concat(distance).concat(" ").concat(stroke);
      Double groupCut = (Double)this.timeCuts.get(ageGroup);
      boolean groupExists = false;
      double dCut;
      if(groupCut != null) {
         groupExists = true;
         dCut = groupCut.doubleValue();
         if(timesec <= dCut) {
            this.possibleNewEntry(gender, course, agegroup, stroke, distance, ussid, timesec, name, season, meetDate, meetName);
         }
      }

      if(!"SENR".equals(agegroup)) {
         ageGroup = gender.concat(" ").concat(course).concat(" ").concat("SENR").concat(" ").concat(distance).concat(" ").concat(stroke);
         groupCut = (Double)this.timeCuts.get(ageGroup);
         if(groupCut != null) {
            groupExists = true;
            dCut = groupCut.doubleValue();
            if(timesec <= dCut) {
               this.possibleNewEntry(gender, course, "SENR", stroke, distance, ussid, timesec, name, season, meetDate, meetName);
            }
         }
      }

      if(!groupExists) {
         this.messages.append("No such event in our Top Ten: ".concat(ageGroup).concat("\n"));
      }

      return true;
   }

   public boolean insertManually(int meetDate, String meetName, String course, String gender, String ussid, String name, String distance, String stroke, double timesec) {
      this.clearUpdates();
      int age = SwimFormatter.computeAgeMMDDYY(meetDate, ussid);
      String agegroup = SwimFormatter.toAgeGroup(age);
      String season = String.valueOf(SwimFormatter.computeSeason(meetDate));
      this.applyResult(meetDate, meetName, course, gender, agegroup, distance, stroke, name.toUpperCase(), season, ussid, timesec);
      if(this.getMessages().length() == 0) {
         this.messages.append("No changes to Top Ten");
      }

      return true;
   }
}
