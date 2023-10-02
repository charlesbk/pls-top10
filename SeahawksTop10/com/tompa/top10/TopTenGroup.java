package com.tompa.top10;

import com.tompa.top10.ExecuteStatement;
import com.tompa.top10.GroupOfTen;
import com.tompa.top10.SaveHelper;
import com.tompa.top10.SwimFormatter;
import com.tompa.top10.SwimmerResult;
import com.tompa.top10.SwimmerTopTenGroups;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

public class TopTenGroup {

   private static HashMap<String, SwimmerTopTenGroups> topIDs = new HashMap();


   public static StringBuilder htmlGroup(String gender, String course, String ageGroup, String distance, String stroke, String seasonRed) {
      StringBuilder ret = new StringBuilder();
      String query = "select name, timesec, season, meetdate, ussid from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec, season";
      ArrayList ten = ExecuteStatement.executeQuery(query, new String[]{gender, course, ageGroup, distance, stroke});
      if(ten.size() < 1) {
         return ret;
      } else {
         StringBuilder tenGroup = new StringBuilder();
         tenGroup.append(gender).append(" ");
         tenGroup.append(course).append(" ");
         tenGroup.append(ageGroup).append(" ");
         tenGroup.append(distance).append(" ");
         tenGroup.append(stroke);
         GroupOfTen groupOfTen = null;
         groupOfTen = new GroupOfTen(tenGroup.toString());
         ret.append(SwimFormatter.htmlFormatStartTable(gender, course, ageGroup, distance, stroke));
         int pos = 0;
         String lastSeconds = "";
         Iterator i$ = ten.iterator();
         ArrayList fullNames = new ArrayList<String>();

         while(i$.hasNext()) {
            ArrayList row = (ArrayList)i$.next();
            String name = (String)row.get(0);
            String sec = (String)row.get(1);
            String season = (String)row.get(2);
            String yyyymmdd = (String)row.get(3);
            String ussid = (String)row.get(4);
            if(pos < 10 || sec.equals(lastSeconds)) {
               if(fullNames.contains(name)) continue;
               ++pos;
               StringBuilder tableRow = SwimFormatter.htmlFormatRow(pos, name, sec, season, seasonRed, (String)null, yyyymmdd);
               groupOfTen.addResult(new SwimmerResult(name, sec, season, yyyymmdd));
               SwimmerTopTenGroups sttg = (SwimmerTopTenGroups)topIDs.get(ussid);
               if(sttg == null) {
                  sttg = new SwimmerTopTenGroups(name);
                  topIDs.put(ussid, sttg);
               }

               sttg.addGroup(groupOfTen);
               ret.append(tableRow);
               lastSeconds = sec;
               fullNames.add(name);
            }
         }

         ret.append(SwimFormatter.htmlFormatEndTable());
         return ret;
      }
   }

   public static StringBuilder htmlGenderCourseGroup(String gender, String course, String ageGroup, String seasonRed) {
      StringBuilder ret = new StringBuilder();
      String queryDistance = "select distinct(distance) from top10 where gender=? and course=? and agegroup=? order by distance * 1";
      ArrayList distance = ExecuteStatement.executeQuery(queryDistance, new String[]{gender, course, ageGroup});
      String title = SwimFormatter.genderCourseGroup(gender, course, ageGroup);
      ret.append(SwimFormatter.htmlFormatTopPage(title));
      String[] strokes = SwimFormatter.getStrokes();

      for(int i = 0; i < strokes.length; ++i) {
         Iterator i$ = distance.iterator();

         while(i$.hasNext()) {
            ArrayList distanceRow = (ArrayList)i$.next();
            String dist = (String)distanceRow.get(0);
            StringBuilder group = htmlGroup(gender, course, ageGroup, dist, strokes[i], seasonRed);
            ret.append(group);
         }
      }

      ret.append(SwimFormatter.htmlFormatBottomPage());
      return ret;
   }

   public static void saveTopFiles(String folder, String season) throws IOException {
      topIDs = new HashMap();
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();

      for(int i = 0; i < genders.length; ++i) {
         for(int j = 0; j < courses.length; ++j) {
            for(int k = 0; k < agegroups.length; ++k) {
               StringBuilder s = htmlGenderCourseGroup(genders[i], courses[j], agegroups[k], season);
               String filename = courses[j].concat(genders[i]).concat(agegroups[k]).concat(".html");
               SaveHelper.save(folder, filename, s);
            }
         }
      }

   }

   public static void saveTopFiles(String topFolder, String nameFolder, String indexFile) throws IOException {
      saveTopFiles(topFolder, SwimFormatter.formatCurrentSeason());
      StringBuilder indexPage = new StringBuilder();
      indexPage.append(SwimFormatter.htmlFormatTopPage("By Name"));
      indexPage.append(SwimFormatter.htmlFormatNameIndex());
      ArrayList ids = new ArrayList(topIDs.keySet());
      Collections.sort(ids);
      HashMap topNames = new HashMap();
      Iterator names = ids.iterator();

      String name;
      while(names.hasNext()) {
         String charAnchor = (String)names.next();
         SwimmerTopTenGroups i$ = (SwimmerTopTenGroups)topIDs.get(charAnchor);
         name = i$.getName();
         String c = name.replace(' ', '_').replace('.', '_').replace('\'', '_').concat(".html");
         topNames.put(name, c);
         StringBuilder data = SwimFormatter.htmlFormatTopPage(name);
         ArrayList arrayList = i$.getNameInGroups();

         for(int i = 0; i < arrayList.size(); ++i) {
            GroupOfTen g10 = (GroupOfTen)arrayList.get(i);
            data.append(SwimFormatter.htmlFormatStartTable(g10.getEventName()));
            ArrayList tenResults = g10.getArrayList();

            for(int j = 0; j < tenResults.size(); ++j) {
               SwimmerResult result = (SwimmerResult)tenResults.get(j);
               StringBuilder tableRow = SwimFormatter.htmlFormatRow(j + 1, result.getName(), result.getTime(), result.getSeason(), (String)null, name, result.getMeetdate());
               data.append(tableRow);
            }

            data.append(SwimFormatter.htmlFormatEndTable());
         }

         data.append(SwimFormatter.htmlFormatBottomPage());
         SaveHelper.save(nameFolder, c, data);
      }

      ArrayList var19 = new ArrayList(topNames.keySet());
      Collections.sort(var19);
      char var20 = 32;

      for(Iterator var21 = var19.iterator(); var21.hasNext(); indexPage.append(SwimFormatter.formatNameIndexName((String)topNames.get(name), name))) {
         name = (String)var21.next();
         char var22 = name.charAt(0);
         if(var22 != var20) {
            indexPage.append(SwimFormatter.formatNameIndexAnchor(var22));
            var20 = var22;
         }
      }

      indexPage.append(SwimFormatter.htmlFormatBottomPage());
      SaveHelper.save(nameFolder, indexFile, indexPage);
   }

   public static StringBuilder tabGroup(String gender, String course, String ageGroup, String distance, String stroke) {
      StringBuilder ret = new StringBuilder();
      String query = "select name, timesec, season, ussid, meetdate, meetname from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec, season";
      ArrayList ten = ExecuteStatement.executeQuery(query, new String[]{gender, course, ageGroup, distance, stroke});
      if(ten.size() < 1) {
         return ret;
      } else {

         Set<String> names = new HashSet<String>();

         String title = SwimFormatter.genderCourseGroupDistanceStroke(gender, course, ageGroup, distance, stroke);
         ret.append(SwimFormatter.tabFormatTopPage(title));
         int pos = 0;
         String lastSeconds = "";
         Iterator i$ = ten.iterator();

         while(i$.hasNext()) {
            ArrayList row = (ArrayList)i$.next();
            String name = (String)row.get(0);
            String sec = (String)row.get(1);
            String season = (String)row.get(2);
            String id = (String)row.get(3);
            String date = (String)row.get(4);
            String meet = (String)row.get(5);
            if(!names.contains(name) && (pos < 10 || sec.equals(lastSeconds))) {
               ++pos;
               StringBuilder tableRow = SwimFormatter.tabFormatRow(pos, name, sec, season, id, date, meet);
               ret.append(tableRow);
               lastSeconds = sec;
               names.add(name);
            }
         }

         return ret;
      }
   }

   public static StringBuilder tabGroupRecord(String gender, String course, String ageGroup, String distance, String stroke) {
      StringBuilder ret = new StringBuilder();
      String query = "select name, timesec, season, ussid, meetdate, meetname from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec, season";
      ArrayList ten = ExecuteStatement.executeQuery(query, new String[]{gender, course, ageGroup, distance, stroke});
      if(ten.size() < 1) {
         return ret;
      } else {
         String title = SwimFormatter.genderCourseGroupDistanceStroke(gender, course, ageGroup, distance, stroke);
         ret.append(title).append("\t");
         ArrayList row = (ArrayList)ten.get(0);
         String name = (String)row.get(0);
         String sec = (String)row.get(1);
         String season = (String)row.get(2);
         String date = (String)row.get(4);
         StringBuilder rowRecord = SwimFormatter.tabFormatRowRecord(name, sec, season, date);
         ret.append(rowRecord);
         return ret;
      }
   }

   public static StringBuilder tabMasterGroupRecord(String gender, String course, String ageGroup, String distance, String stroke) {
      StringBuilder ret = new StringBuilder();
      String query = "select name, timesec, season, ussid, meetdate, meetname from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec, season";
      ArrayList ten = ExecuteStatement.executeQuery(query, new String[]{gender, course, ageGroup, distance, stroke});
      if(ten.size() < 1) {
         return ret;
      } else {
         ArrayList row = (ArrayList)ten.get(0);
         String name = (String)row.get(0);
         String sec = (String)row.get(1);
         String date = (String)row.get(4);
         StringBuilder rowRecord = SwimFormatter.tabMasterFormatRowRecord(gender, course, ageGroup, distance, stroke, name, sec, date);
         ret.append(rowRecord);
         return ret;
      }
   }

   public static ArrayList<String> printableGroup(String gender, String course, String ageGroup, String distance, String stroke) {
      ArrayList ret = new ArrayList();
      String query = "select name, timesec, season from top10 where gender=? and course=? and agegroup=? and distance=? and stroke=? order by timesec, season";
      ArrayList ten = ExecuteStatement.executeQuery(query, new String[]{gender, course, ageGroup, distance, stroke});

      Set<String> names = new HashSet<String>();

      if(ten.size() < 1) {
         return ret;
      } else {
         StringBuilder sb = new StringBuilder();
         Formatter formatter = new Formatter(sb);
         formatter.format("%6s%6s%6s %s %s", new Object[]{gender, course, ageGroup, distance, stroke});
         ret.add(SwimFormatter.printableRow(sb.toString()).toString());
         int pos = 0;
         String lastSeconds = "";
         Iterator i$ = ten.iterator();

         while(i$.hasNext()) {
            ArrayList row = (ArrayList)i$.next();
            String name = (String)row.get(0);

            String sec = (String)row.get(1);
            String season = (String)row.get(2);

            if(!names.contains(name) && (pos < 10 || sec.equals(lastSeconds))) {
               ++pos;
               StringBuilder tableRow = SwimFormatter.printableFormatRow(pos, name, sec, season);
               ret.add(tableRow.toString());
               lastSeconds = sec;
               names.add(name);
            }
         }

         while(pos++ < 13) {
            ret.add(SwimFormatter.printableRow("").toString());
         }

         return ret;
      }
   }

   public static StringBuilder tabGenderCourseGroup(String gender, String course, String ageGroup) {
      StringBuilder ret = new StringBuilder();
      String queryDistance = "select distinct(distance) from top10 where gender=? and course=? and agegroup=? order by distance";
      ArrayList distance = ExecuteStatement.executeQuery(queryDistance, new String[]{gender, course, ageGroup});
      String[] strokes = SwimFormatter.getStrokes();

      for(int i = 0; i < strokes.length; ++i) {
         Iterator i$ = distance.iterator();

         while(i$.hasNext()) {
            ArrayList distanceRow = (ArrayList)i$.next();
            String dist = (String)distanceRow.get(0);
            StringBuilder group = tabGroup(gender, course, ageGroup, dist, strokes[i]);
            ret.append(group);
         }
      }

      return ret;
   }

   public static StringBuilder tabGenderCourseGroupRecord(String gender, String course, String ageGroup) {
      StringBuilder ret = new StringBuilder();
      String queryDistance = "select distinct(distance) from top10 where gender=? and course=? and agegroup=? order by distance";
      ArrayList distance = ExecuteStatement.executeQuery(queryDistance, new String[]{gender, course, ageGroup});
      String[] strokes = SwimFormatter.getStrokes();

      for(int i = 0; i < strokes.length; ++i) {
         Iterator i$ = distance.iterator();

         while(i$.hasNext()) {
            ArrayList distanceRow = (ArrayList)i$.next();
            String dist = (String)distanceRow.get(0);
            StringBuilder group = tabGroupRecord(gender, course, ageGroup, dist, strokes[i]);
            ret.append(group);
         }
      }

      return ret;
   }

   public static StringBuilder tabMasterGenderCourseGroupRecord(String gender, String course, String ageGroup) {
      StringBuilder ret = new StringBuilder();
      String queryDistance = "select distinct(distance) from top10 where gender=? and course=? and agegroup=? order by distance";
      ArrayList distance = ExecuteStatement.executeQuery(queryDistance, new String[]{gender, course, ageGroup});
      String[] strokes = SwimFormatter.getStrokes();

      for(int i = 0; i < strokes.length; ++i) {
         Iterator i$ = distance.iterator();

         while(i$.hasNext()) {
            ArrayList distanceRow = (ArrayList)i$.next();
            String dist = (String)distanceRow.get(0);
            StringBuilder group = tabMasterGroupRecord(gender, course, ageGroup, dist, strokes[i]);
            ret.append(group);
         }
      }

      return ret;
   }

   public static ArrayList<ArrayList<String>> printableGenderCourseGroup(String gender, String course, String ageGroup) {
      String queryDistance = "select distinct(distance) from top10 where gender=? and course=? and agegroup=? order by distance";
      ArrayList distance = ExecuteStatement.executeQuery(queryDistance, new String[]{gender, course, ageGroup});
      ArrayList groups = new ArrayList();
      String[] strokes = SwimFormatter.getStrokes();

      for(int i = 0; i < strokes.length; ++i) {
         Iterator i$ = distance.iterator();

         while(i$.hasNext()) {
            ArrayList distanceRow = (ArrayList)i$.next();
            String dist = (String)distanceRow.get(0);
            ArrayList group = printableGroup(gender, course, ageGroup, dist, strokes[i]);
            if(group.size() > 0) {
               groups.add(group);
            }
         }
      }

      return groups;
   }

   private static StringBuilder buildExcel() {
      StringBuilder buf = new StringBuilder();
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();

      for(int i = 0; i < genders.length; ++i) {
         for(int j = 0; j < courses.length; ++j) {
            for(int k = 0; k < agegroups.length; ++k) {
               StringBuilder s = tabGenderCourseGroup(genders[i], courses[j], agegroups[k]);
               buf.append(s);
            }
         }
      }

      return buf;
   }

   private static StringBuilder buildRecords() {
      StringBuilder buf = new StringBuilder();
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();

      for(int i = 0; i < genders.length; ++i) {
         for(int j = 0; j < courses.length; ++j) {
            for(int k = 0; k < agegroups.length; ++k) {
               StringBuilder s = tabGenderCourseGroupRecord(genders[i], courses[j], agegroups[k]);
               buf.append(s);
            }
         }
      }

      return buf;
   }

   private static StringBuilder buildMasterRecords(String type) {
      StringBuilder buf = new StringBuilder();
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();

      for(int i = 0; i < genders.length; ++i) {
         for(int j = 0; j < courses.length; ++j) {
            for(int k = 0; k < agegroups.length; ++k) {
               if(("LCM".equals(type) && "LONG".equals(courses[j]) || "SCY".equals(type) && "SHORT".equals(courses[j])) && !"SENR".equals(agegroups[k])) {
                  StringBuilder s = tabMasterGenderCourseGroupRecord(genders[i], courses[j], agegroups[k]);
                  buf.append(s);
               }
            }
         }
      }

      return buf;
   }

   private static ArrayList<ArrayList<String>> buildList() {
      ArrayList list = new ArrayList();
      String[] genders = SwimFormatter.getGenders();
      String[] courses = SwimFormatter.getCourses();
      String[] agegroups = SwimFormatter.getAgegroups();

      for(int i = 0; i < genders.length; ++i) {
         for(int j = 0; j < courses.length; ++j) {
            for(int k = 0; k < agegroups.length; ++k) {
               ArrayList groups = printableGenderCourseGroup(genders[i], courses[j], agegroups[k]);
               list.addAll(groups);
            }
         }
      }

      return list;
   }

   private static StringBuilder buildPrintable() {
      ArrayList list = buildList();
      ArrayList rows = new ArrayList();
      ArrayList emptyEvent = new ArrayList();

      for(int lastHeader = 0; lastHeader < 13; ++lastHeader) {
         emptyEvent.add(SwimFormatter.printableRow(" ").toString());
      }

      String var14 = "";
      int colnum = 0;
      int rownum = 0;
      Iterator sb = list.iterator();

      while(sb.hasNext()) {
         ArrayList rowOnPage = (ArrayList)sb.next();
         String df = ((String)rowOnPage.get(0)).substring(0, 18);
         ArrayList date;
         if(!df.equals(var14)) {
            if(colnum > 0) {
               date = (ArrayList)rows.get(rownum);

               for(int i$ = colnum; i$ < 3; ++i$) {
                  date.add(emptyEvent);
               }

               colnum = 0;
               ++rownum;
            }

            var14 = df;
         }

         if(colnum == 0) {
            rows.add(new ArrayList());
         }

         date = (ArrayList)rows.get(rownum);
         date.add(rowOnPage);
         colnum = (colnum + 1) % 3;
         if(colnum == 0) {
            ++rownum;
         }
      }

      int var17;
      if(colnum > 0) {
         ArrayList var15 = (ArrayList)rows.get(rownum);

         for(var17 = colnum; var17 < 3; ++var17) {
            var15.add(emptyEvent);
         }
      }

      StringBuilder var16 = new StringBuilder();
      var14 = null;
      var17 = 0;
      DateFormat var18 = DateFormat.getDateInstance(3);
      String var19 = var18.format(Calendar.getInstance().getTime());
      Iterator var20 = rows.iterator();

      while(var20.hasNext()) {
         ArrayList threeEvents = (ArrayList)var20.next();
         String header = ((String)((ArrayList)threeEvents.get(0)).get(0)).substring(0, 18);
         if(!header.equals(var14)) {
            if(var14 != null) {
               var16.append("\f");
            }

            var14 = header;
            var17 = 1;
         } else {
            if(var17 == 0) {
               var16.append("\f");
            }

            var17 = (var17 + 1) % 4;
         }

         if(var17 == 1) {
            var16.append(var19).append("\n\n");
         }

         for(int i = 0; i < 13; ++i) {
            var16.append((String)((ArrayList)threeEvents.get(0)).get(i));
            var16.append("  ");
            var16.append((String)((ArrayList)threeEvents.get(1)).get(i));
            var16.append("  ");
            var16.append((String)((ArrayList)threeEvents.get(2)).get(i));
            var16.append("\n");
            if(i == 0) {
               var16.append("\n");
            }
         }
      }

      return var16;
   }

   private static StringBuilder build4x4(String filter) {
      ArrayList list = buildList();
      ArrayList rows = new ArrayList();
      ArrayList emptyEvent = new ArrayList();

      for(int lastHeader = 0; lastHeader < 13; ++lastHeader) {
         emptyEvent.add(SwimFormatter.printableRow(" ").toString());
      }

      String var12 = "";
      int colnum = 0;
      int rownum = 0;
      Iterator sb = list.iterator();

      while(sb.hasNext()) {
         ArrayList i$ = (ArrayList)sb.next();
         String fourEvents = ((String)i$.get(0)).substring(0, 18);
         if(fourEvents.contains(filter)) {
            if(!fourEvents.equals(var12)) {
               var12 = fourEvents;
            }

            if(colnum == 0) {
               rows.add(new ArrayList());
            }

            ArrayList header = (ArrayList)rows.get(rownum);
            header.add(i$);
            colnum = (colnum + 1) % 4;
            if(colnum == 0) {
               ++rownum;
            }
         }
      }

      if(colnum > 0) {
         ArrayList var13 = (ArrayList)rows.get(rownum);

         for(int var15 = colnum; var15 < 4; ++var15) {
            var13.add(emptyEvent);
         }
      }

      StringBuilder var14 = new StringBuilder();
      var12 = null;
      Iterator var16 = rows.iterator();

      while(var16.hasNext()) {
         ArrayList var17 = (ArrayList)var16.next();
         String var18 = ((String)((ArrayList)var17.get(0)).get(0)).substring(0, 18);
         if(!var18.equals(var12)) {
            var12 = var18;
         }

         for(int i = 0; i < 13; ++i) {
            var14.append((String)((ArrayList)var17.get(0)).get(i));
            var14.append("  ");
            var14.append((String)((ArrayList)var17.get(1)).get(i));
            var14.append("  ");
            var14.append((String)((ArrayList)var17.get(2)).get(i));
            var14.append("  ");
            var14.append((String)((ArrayList)var17.get(3)).get(i));
            var14.append("\n");
         }
      }

      return var14;
   }

   public static StringBuilder buildBackup() {
      StringBuilder ret = new StringBuilder();
      String queryAll = "select * from top10 order by gender, course, agegroup, stroke, distance";
      ArrayList all = ExecuteStatement.executeQuery(queryAll, new String[0]);
      Iterator i$ = all.iterator();

      while(i$.hasNext()) {
         ArrayList arrayList = (ArrayList)i$.next();
         String sep = "";

         for(Iterator i$1 = arrayList.iterator(); i$1.hasNext(); sep = "\t") {
            String s = (String)i$1.next();
            ret.append(sep);
            ret.append(s);
         }

         ret.append("\n");
      }

      return ret;
   }

   public static void saveExcelTabulated(File file) throws IOException {
      SaveHelper.save(file, buildExcel());
   }

   public static void saveRecords(File file) throws IOException {
      SaveHelper.save(file, buildRecords());
   }

   public static void saveLCM(File file) throws IOException {
      SaveHelper.save(file, buildMasterRecords("LCM"));
   }

   public static void saveSCY(File file) throws IOException {
      SaveHelper.save(file, buildMasterRecords("SCY"));
   }

   public static void savePrintable(File file) throws IOException {
      SaveHelper.save(file, buildPrintable());
   }

   public static void save4x4(File file, String filter) throws IOException {
      SaveHelper.save(file, build4x4(filter));
   }

   public static void saveBackup(File file) throws IOException {
      SaveHelper.save(file, buildBackup());
   }

}
