package com.tompa.top10;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class SwimFormatter {

   private static DecimalFormat shortFormat = new DecimalFormat("##.00");
   private static DecimalFormat longFormat = new DecimalFormat();
   private static String[] strokes = new String[]{"FREE", "BACK", "BREAST", "FLY", "I.M."};
   private static String[] genders = new String[]{"BOYS", "GIRLS"};
   private static String[] courses = new String[]{"SHORT", "LONG"};
   private static String[] agegroups = new String[]{"8-UN", "10-UN", "11-12", "13-14", "15-16", "17-18", "SENR"};
   private static String[] distances = new String[]{"25", "50", "100", "200", "400", "500", "800", "1000", "1500", "1650"};
   private static Map<String, String> masterGender;
   private static Map<String, String> masterAgeGroup;
   private static Map<String, String> masterCourse;
   private static Map<String, String> masterStroke;


   public static String[] getStrokes() {
      return strokes;
   }

   public static String[] getGenders() {
      return genders;
   }

   public static String[] getCourses() {
      return courses;
   }

   public static String[] getAgegroups() {
      return agegroups;
   }

   public static String[] getDistances() {
      return distances;
   }

   public static String formatTime(double timesec) {
      String s = "???";
      if(timesec < 60.0D) {
         s = shortFormat.format(timesec);
      } else {
         double sec = timesec % 60.0D;
         double min = (double)((int)(timesec / 60.0D));
         s = longFormat.format(min * 100.0D + sec);
      }

      return s;
   }

   public static double parseTime(String s) throws ParseException {
      if(s == null) {
         throw new ParseException("Null time", -1);
      } else {
         s = s.trim();
         int len = s.length();
         if(len < 5) {
            throw new ParseException("Too short", 0);
         } else if(len > 8) {
            throw new ParseException("Too long", 0);
         } else if(len == 6) {
            throw new ParseException("Invalid length", 0);
         } else {
            int minutes = 0;
            int x;
            if(len > 5) {
               int c = len - 6;
               if(s.charAt(c) != 58) {
                  throw new ParseException("Invalid colon position", 0);
               }

               for(x = 0; x < c; ++x) {
                  char d = s.charAt(x);
                  if(!Character.isDigit(d)) {
                     throw new ParseException("Not a digit", x);
                  }

                  minutes = minutes * 10 + Character.digit(d, 10);
               }

               s = s.substring(c + 1);
            }

            char var10 = s.charAt(0);
            if(!Character.isDigit(var10)) {
               throw new ParseException("Not a digit", 0);
            } else {
               x = Character.digit(var10, 10);
               if(x > 5) {
                  throw new ParseException("Seconds over 60", 0);
               } else {
                  double var11 = 0.0D;

                  try {
                     var11 = Double.parseDouble(s);
                  } catch (NumberFormatException var9) {
                     throw new ParseException(var9.getMessage(), 0);
                  }

                  double sec = (double)(minutes * 60) + var11;
                  sec = (double)Math.round(sec * 100.0D) / 100.0D;
                  return sec;
               }
            }
         }
      }
   }

   public static String formatTime(String sec) {
      double dsec = Double.parseDouble(sec);
      return formatTime(dsec);
   }

   public static StringBuilder htmlFormatRow(int pos, String name, String time, String season, String seasonRed, String nameRed, String yyyymmdd) {
      StringBuilder row = new StringBuilder();
      boolean red = name.equals(nameRed) || season.equals(seasonRed);
      if(red) {
         row.append("<tr class=\"redbold\">");
      } else {
         row.append("<tr>");
      }

      row.append("\n<td>");
      row.append(pos);
      row.append("</td><td>");
      row.append(name);
      row.append("</td><td>");
      row.append(formatTime(time));
      row.append("</td><td>");
      String usdate = "&nbsp;";
      if(yyyymmdd != null) {
         usdate = yyyymmdd.substring(4, 6).concat("/").concat(yyyymmdd.substring(6, 8)).concat("/").concat(yyyymmdd.substring(0, 4));
      }

      row.append("<a title=\"").append(usdate).append("\">");
      row.append(season);
      row.append("</a>");
      row.append("</td>\n</tr>");
      row.append("\n");
      return row;
   }

   public static StringBuilder tabFormatRow(int pos, String name, String time, String season, String id, String date, String meet) {
      StringBuilder row = new StringBuilder();
      row.append(pos).append("\t");
      row.append(name).append("\t");
      row.append(formatTime(time)).append("\t");
      row.append(season).append("\t");
      row.append(id).append("\t");
      row.append(date).append("\t");
      row.append(meet).append("\t");
      row.append("\n");
      return row;
   }

   public static StringBuilder tabFormatRowRecord(String name, String time, String season, String date) {
      StringBuilder row = new StringBuilder();
      row.append(name).append("\t");
      row.append(formatTime(time)).append("\t");
      row.append(season).append("\t");
      row.append(date).append("\t");
      row.append("\n");
      return row;
   }

   public static StringBuilder tabMasterFormatRowRecord(String gender, String course, String ageGroup, String distance, String stroke, String name, String time, String date) {
      StringBuilder row = new StringBuilder();
      row.append((String)masterGender.get(gender)).append(',');
      row.append((String)masterAgeGroup.get(ageGroup)).append(',');
      row.append(String.format("%-5s", new Object[]{distance}));
      row.append(String.format("%-4s", new Object[]{masterCourse.get(course)}));
      row.append(String.format("%4s", new Object[]{masterStroke.get(stroke)})).append(',');
      row.append(name).append(",");
      String yy = date.substring(2, 4);
      String mm = date.substring(4, 6);
      String dd = date.substring(6, 8);
      if("01".equals(mm) && "01".equals(dd)) {
         row.append("  -  -").append(yy);
      } else {
         row.append(mm).append("-").append(dd).append("-").append(yy);
      }

      row.append(",");
      row.append(String.format("%8s", new Object[]{formatTime(time).trim()}));
      row.append("\r\n");
      return row;
   }

   public static StringBuilder printableFormatRow(int pos, String name, String time, String season) {
      StringBuilder row = new StringBuilder();
      Formatter formatter = new Formatter(row);
      formatter.format("%2d %-23s %8s %2s", new Object[]{Integer.valueOf(pos), name, formatTime(time), season.substring(2, 4)});
      return printableRow(row.toString());
   }

   public static StringBuilder htmlFormatStartTable(String caption) {
      StringBuilder ret = new StringBuilder();
      ret.append("<table>").append("\n");
      ret.append("<caption>");
      ret.append(caption);
      ret.append("</caption>").append("\n");
      ret.append("<thead>\n<tr>\n");
      ret.append("<th class=\"thPOS\">&nbsp;</th>");
      ret.append("<th class=\"thNAME\">NAME</th>");
      ret.append("<th class=\"thTIME\">TIME</th>");
      ret.append("<th class=\"thSEASON\">SEASON</th>");
      ret.append("\n");
      ret.append("</tr>\n</thead>\n");
      ret.append("<tbody>\n");
      return ret;
   }

   public static StringBuilder htmlFormatStartTable(String gender, String course, String ageGroup, String distance, String stroke) {
      StringBuilder caption = new StringBuilder();
      caption.append(gender).append(" ");
      caption.append(course).append(" ");
      caption.append(ageGroup).append(" ");
      caption.append(distance).append(" ");
      caption.append(stroke);
      return htmlFormatStartTable(caption.toString());
   }

   public static StringBuilder htmlFormatEndTable() {
      return (new StringBuilder("</tbody>\n</table>")).append("\n");
   }

   public static StringBuilder htmlFormatTopPage(String title) {
      StringBuilder ret = new StringBuilder();
      ret.append("<html>\n<head>\n<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\n<meta http-equiv=\"Expires\" content=\"-1\"/>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>\n<title>Pleasanton Seahawks Records ");
      ret.append(title);
//      ret.append("</title>\n<link rel=\"stylesheet\" href=\"top10.css\" type=\"text/css\" media=\"screen\">\n</head>\n<body>\n");
      ret.append("</title>\n");
      ret.append("<style>\ncaption {\n    font-weight: bold;\n    font-size: larger;\n    text-align: center;\n}\n\ntable {\n    border-collapse: collapse;\n    width: 100%;\n    margin-bottom:40px;\n    margin-top:20px;\n}\n\nth, td {\n    border-right: 1px solid #ccc;\n    border-bottom: 1px solid #ccc;\n    border-left: 1px solid #ccc;\n    padding: .2em;\n}\n\nthead th {\n    background: #4c9ece;\n    color: #fff;\n}\n\ntbody th {\n    font-weight: normal;\n    background: #658CB1;\n}\n\n.redbold {\n    font-weight: bold;\n    color: red;\n}\n\nbody {\n    background-image: url( \"water.jpg\" );\n    background-attachment: fixed;\n    font-family: arial, helvetica, sans-serif;\n}\n\n.thPOS {\n    width:10%;\n}\n\n.thNAME {\n    width:50%;\n}\n\n.thTIME {\n    width:20%;\n}\n\n.thSEASON {\n    width:20%;\n}\n</style>");
      ret.append("\n</head>\n<body>\n");
      return ret;
   }

   public static StringBuilder tabFormatTopPage(String title) {
      return new StringBuilder("\n".concat(title).concat("\n").concat("\tNAME\tTIME\tSEASON\tID\tDATE\tMEET").concat("\n"));
   }

   public static StringBuilder printableRow(String row) {
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb);
      formatter.format("%-38s", new Object[]{row});
      return sb;
   }

   public static StringBuilder htmlFormatBottomPage() {
      return new StringBuilder("</body>\n</html>");
   }

   public static StringBuilder htmlFormatNameIndex() {
      StringBuilder ret = new StringBuilder();
      Calendar now = Calendar.getInstance();
      DateFormat df = DateFormat.getDateTimeInstance(0, 3);
      String dateString = df.format(now.getTime());
      ret.append("<br><p align=\"right\">Generated ");
      ret.append(dateString).append("</p>").append("\n");
      ret.append("<a href=\"#A\">A</a> <a href=\"#B\">B</a> <a href=\"#C\">C</a> <a href=\"#D\">D</a> <a href=\"#E\">E</a> <a href=\"#F\">F</a> <a href=\"#G\">G</a> <a href=\"#H\">H</a> <a href=\"#I\">I</a> <a href=\"#J\">J</a> <a href=\"#K\">K</a> <a href=\"#L\">L</a> <a href=\"#M\">M</a> <a href=\"#N\">N</a> <a href=\"#O\">O</a> <a href=\"#P\">P</a> <a href=\"#Q\">Q</a> <a href=\"#R\">R</a> <a href=\"#S\">S</a> <a href=\"#T\">T</a> <a href=\"#U\">U</a> <a href=\"#V\">V</a> <a href=\"#W\">W</a> <a href=\"#X\">X</a> <a href=\"#Y\">Y</a> <a href=\"#Z\">Z</a>");
      ret.append("<br><br>\n");
      return ret;
   }

   public static StringBuilder formatNameIndexAnchor(char c) {
      return (new StringBuilder("<a name=\"")).append(c).append("\"></a>").append("\n");
   }

   public static StringBuilder formatNameIndexName(String filename, String name) {
      return (new StringBuilder("<a href=\"")).append(filename).append("\">").append(name).append("</a><br>").append("\n");
   }

   public static StringBuilder formatToday() {
      Calendar now = Calendar.getInstance();
      String s = String.format("%1$tY%1$tm%1$td", new Object[]{now});
      return new StringBuilder(s);
   }

   public static String formatCurrentSeason() {
      StringBuilder now = formatToday();
      int date = Integer.parseInt(now.toString());
      int season = date / 10000;
      if(date % 10000 > 900) {
         ++season;
      }

      return String.valueOf(season);
   }

   public static String toAgeGroup(int age) {
      String ageGroup = "SENR";
      if(age <= 8) {
         ageGroup = "8-UN";
      } else if(age <= 10) {
         ageGroup = "10-UN";
      } else if(age <= 12) {
         ageGroup = "11-12";
      } else if(age <= 14) {
         ageGroup = "13-14";
      } else if(age <= 16) {
         ageGroup = "15-16";
      } else if(age <= 18) {
         ageGroup = "17-18";
      }

      return ageGroup;
   }

   public static int computeAge(int ageUpDate, int yyyy, int mm, int dd) {
      int age = ageUpDate / 10000 - yyyy;
      if(mm * 100 + dd > ageUpDate % 10000) {
         --age;
      }

      return age;
   }
   // This block is for old id like : 081610DYLBKIAUSA0816201011M
   /*public static int computeAgeMMDDYYYY(int ageUpDate, String ussid) {
      int yy = Integer.parseInt(ussid.substring(4, 8));
      int mm = Integer.parseInt(ussid.substring(0, 2));
      int dd = Integer.parseInt(ussid.substring(2, 4));
      return computeAge(ageUpDate, yy, mm, dd);
   }

   public static int computeAgeMMDDYY(int ageUpDate, String ussid) {
      int yy = Integer.parseInt(ussid.substring(4, 6));
      int mm = Integer.parseInt(ussid.substring(0, 2));
      int dd = Integer.parseInt(ussid.substring(2, 4));
      if(yy < 50) {
         yy += 2000;
      } else {
         yy += 1900;
      }

      return computeAge(ageUpDate, yy, mm, dd);
   }*/
   //446498A2FA27AUSA0816201012M
   // This block is for new id like : 446498A2FA27AUSA0816201012M
   public static int computeAgeMMDDYYYY(int ageUpDate, String dob) {
      int yyyy = Integer.parseInt(dob.substring(4, 8));
      int mm = Integer.parseInt(dob.substring(0, 2));
      int dd = Integer.parseInt(dob.substring(2, 4));
      return computeAge(ageUpDate, yyyy, mm, dd);
   }

   public static int computeAgeMMDDYY(int ageUpDate, String ussid, String dob) {
      int yy = Integer.parseInt(dob.substring(6, 8));
      int mm = Integer.parseInt(dob.substring(0, 2));
      int dd = Integer.parseInt(dob.substring(2, 4));
      if(yy < 50) {
         yy += 2000;
      } else {
         yy += 1900;
      }

      return computeAge(ageUpDate, yy, mm, dd);
   }

   public static int computeSeason(int startDate) {
      int yyyy = startDate / 10000;
      if(startDate % 10000 >= 900) {
         ++yyyy;
      }

      return yyyy;
   }

   public static String genderCourseGroup(String gender, String course, String ageGroup) {
      return gender.concat(" ").concat(course).concat(" ").concat(ageGroup);
   }

   public static String genderCourseGroupDistanceStroke(String gender, String course, String ageGroup, String distance, String stroke) {
      return gender.concat(" ").concat(course).concat(" ").concat(ageGroup).concat(" ").concat(distance).concat(" ").concat(stroke);
   }

   static {
      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      dfs.setGroupingSeparator(':');
      longFormat.setDecimalFormatSymbols(dfs);
      longFormat.applyLocalizedPattern("##:##.00");
      masterGender = new HashMap();
      masterGender.put("BOYS", "M E N");
      masterGender.put("GIRLS", "WOMEN");
      masterAgeGroup = new HashMap();
      masterAgeGroup.put("8-UN", "0-8");
      masterAgeGroup.put("10-UN", "9-10");
      masterAgeGroup.put("11-12", "11-12");
      masterAgeGroup.put("13-14", "13-14");
      masterAgeGroup.put("15-16", "15-16");
      masterAgeGroup.put("17-18", "17-18");
      masterCourse = new HashMap();
      masterCourse.put("LONG", "M.");
      masterCourse.put("SHORT", "YD.");
      masterStroke = new HashMap();
      masterStroke.put("FREE", "FREE");
      masterStroke.put("BACK", "BACK");
      masterStroke.put("BREAST", "BRST");
      masterStroke.put("FLY", "FLY");
      masterStroke.put("I.M.", "I.M.");
   }
}
