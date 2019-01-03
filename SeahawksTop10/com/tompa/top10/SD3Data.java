//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tompa.top10;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SD3Data {
  static final double MAX_TIME = 3600.0D;
  public static final String MEET_RECORD = "B1";
  public static final String TEAM_RECORD = "C1";
  public static final String ID_RECORD = "D3";
  public static final String SPLIT_RECORD = "G0";
  public static final String IND_TIME = "D0";
  private MeetRecord meetRecord;
  private ArrayList<IndividualTimeRecord> timeRecords = new ArrayList();
  static HashMap<String, String> strokeCodeMap = new HashMap();
  static HashMap<String, String> courseCodeMap;

  public SD3Data() {
    courseCodeMap.put("1", "UNSUPPORTED");
    courseCodeMap.put("S", "UNSUPPORTED");
    courseCodeMap.put("2", "SHORT");
    courseCodeMap.put("Y", "SHORT");
    courseCodeMap.put("3", "LONG");
    courseCodeMap.put("L", "LONG");
    courseCodeMap.put("X", "Disqualified");
  }

  public static String getStrokeName(String s) {
    return s == null ? null : (String)strokeCodeMap.get(s);
  }

  public static String extract(String line, int pos, int len) {
    --pos;
    return line.substring(pos, pos + len).trim();
  }

  private int parseDate(String date) {
    int sqlDate = 10000000;
    String sMM = date.substring(0, 2);
    String sDD = date.substring(2, 4);
    String sYYYY = date.substring(4, 8);
    sqlDate = Integer.parseInt(sYYYY) * 10000 + Integer.parseInt(sMM) * 100 + Integer.parseInt(sDD);
    return sqlDate;
  }

  private static double parseTime(String time) {
    if (time != null && time.length() != 0 && !"SCR".equals(time) && !"DQ".equals(time) && !"NS".equals(time)) {
      double d = 3600.0D;

      try {
        d = SwimFormatter.parseTime(time);
      } catch (ParseException var4) {
        var4.printStackTrace();
      }

      return d;
    } else {
      return 3600.0D;
    }
  }

  int getNumValidTimes() {
    return this.timeRecords.size();
  }

  public void parseMeetRecord(String line) {
    String meetName = extract(line, 12, 30);
    int startDate = this.parseDate(extract(line, 122, 8));
    System.out.println("DATE: ".concat(String.valueOf(startDate)));
    System.out.println("NAME: ".concat(meetName));
    this.meetRecord = new MeetRecord(meetName, startDate);
  }

  public Iterator<ArrayList<String>> resultsIterator() {
    return new Iterator<ArrayList<String>>() {
      private int current = 0;

      public boolean hasNext() {
        return SD3Data.this.timeRecords != null && !SD3Data.this.timeRecords.isEmpty() && this.current < SD3Data.this.timeRecords.size();
      }

      public ArrayList<String> next() {
        IndividualTimeRecord rec = (IndividualTimeRecord)SD3Data.this.timeRecords.get(this.current++);
        ArrayList<String> obj = new ArrayList();
        obj.add(rec.getSex());
        obj.add(rec.getCourse());
        obj.add(rec.getAgeGroup());
        obj.add(String.valueOf(rec.getDist()));
        obj.add(rec.getStroke());
        obj.add(rec.getNameOnRoster().toUpperCase());
        obj.add(String.valueOf(rec.getTime()));
        obj.add(String.valueOf(SD3Data.this.meetRecord.getMeetSeason()));
        obj.add(rec.getId());
        obj.add(String.valueOf(SD3Data.this.meetRecord.getStartDate()));
        obj.add(SD3Data.this.meetRecord.getMeetName());
        return obj;
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public void parseIndividualTimeRecord(String ussid, String line, String nameOnRoster) {
    String dob = extract(line, 56, 8);
    int reportedAge = Integer.parseInt(extract(line, 64, 2));
    int computedAge = SwimFormatter.computeAgeMMDDYY(this.meetRecord.getStartDate(), ussid);
    if (reportedAge != computedAge) {
      System.out.println("INVALID AGE for: ".concat(ussid));
    }

    String sex = extract(line, 66, 1);
    int dist = Integer.parseInt(extract(line, 68, 4));
    String stroke = getStrokeName(extract(line, 72, 1));
    String swimDateStr = extract(line, 81, 8);
    double prelimTime = parseTime(extract(line, 98, 8));
    String prelimCourse = extract(line, 106, 1);
    double swimoffTime = parseTime(extract(line, 107, 8));
    String swimoffCourse = extract(line, 115, 1);
    double finalTime = parseTime(extract(line, 116, 8));
    String finalCourse = extract(line, 124, 1);
    if (prelimTime < 3600.0D) {
      this.timeRecords.add(new IndividualTimeRecord(ussid, nameOnRoster, dob, computedAge, sex, dist, stroke, swimDateStr, prelimTime, prelimCourse));
    }

    if (swimoffTime < 3600.0D) {
      this.timeRecords.add(new IndividualTimeRecord(ussid, nameOnRoster, dob, computedAge, sex, dist, stroke, swimDateStr, swimoffTime, swimoffCourse));
    }

    if (finalTime < 3600.0D) {
      this.timeRecords.add(new IndividualTimeRecord(ussid, nameOnRoster, dob, computedAge, sex, dist, stroke, swimDateStr, finalTime, finalCourse));
    }

  }

  public static String sd3Team_Code(String line) {
    return extract(line, 12, 6);
  }

  public static String sd3Id_id(String line) {
    return line.length() < 14 ? null : extract(line, 3, 14);
  }

  public static String sd3indtime_name(String line) {
    return extract(line, 12, 28);
  }

  public static String sd3indtime_dob(String line) {
    return extract(line, 56, 8);
  }

  public static String sd3indtime_age(String line) {
    return extract(line, 64, 2);
  }

  public int getMeetDate() {
    return this.meetRecord.getStartDate();
  }

  public String getMeetName() {
    return this.meetRecord.getMeetName();
  }

  static {
    strokeCodeMap.put("1", "FREE");
    strokeCodeMap.put("2", "BACK");
    strokeCodeMap.put("3", "BREAST");
    strokeCodeMap.put("4", "FLY");
    strokeCodeMap.put("5", "I.M.");
    strokeCodeMap.put("6", "Freestyle Relay");
    strokeCodeMap.put("7", "Medley Relay");
    courseCodeMap = new HashMap();
  }
}
