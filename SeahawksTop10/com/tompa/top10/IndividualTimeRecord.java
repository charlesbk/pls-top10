package com.tompa.top10;

import com.tompa.top10.SD3Data;
import com.tompa.top10.SwimFormatter;

class IndividualTimeRecord {

   private String id;
   private String nameOnRoster;
   private String dob;
   private int age;
   private String ageGroup;
   private String sex;
   private int dist;
   private String stroke;
   private String dateStr;
   private double time;
   private String course;


   public IndividualTimeRecord(String id, String nameOnRoster, String dob, int age, String sex, int dist, String stroke, String dateStr, double time, String course) {
      this.id = id;
      this.nameOnRoster = nameOnRoster;
      this.dob = dob;
      this.age = age;
      this.sex = sex.equals("M")?"BOYS":"GIRLS";
      this.dist = dist;
      this.stroke = stroke;
      this.dateStr = dateStr;
      this.time = time;
      this.course = (String)SD3Data.courseCodeMap.get(course);
      this.ageGroup = SwimFormatter.toAgeGroup(age);
   }

   public String getId() {
      return this.id;
   }

   public String getNameOnRoster() {
      return this.nameOnRoster;
   }

   public String getDob() {
      return this.dob;
   }

   public int getAge() {
      return this.age;
   }

   public String getAgeGroup() {
      return this.ageGroup;
   }

   public String getSex() {
      return this.sex;
   }

   public int getDist() {
      return this.dist;
   }

   public String getStroke() {
      return this.stroke;
   }

   public String getDateStr() {
      return this.dateStr;
   }

   public double getTime() {
      return this.time;
   }

   public String getCourse() {
      return this.course;
   }
}
