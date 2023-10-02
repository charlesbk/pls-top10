package com.tompa.top10;

import com.tompa.top10.SwimFormatter;

class MeetRecord {

   private String meetName;
   private int startDate;


   public MeetRecord(String meetName, int startDate) {
      this.meetName = meetName;
      this.startDate = startDate;
   }

   public String getMeetName() {
      return this.meetName;
   }

   public int getStartDate() {
      return this.startDate;
   }

   public int getMeetSeason() {
      return SwimFormatter.computeSeason(this.startDate);
   }
}
