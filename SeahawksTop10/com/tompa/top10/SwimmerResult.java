package com.tompa.top10;


class SwimmerResult {

   private String name;
   private String time;
   private String season;
   private String meetdate;


   public SwimmerResult(String name, String time, String season, String meetdate) {
      this.name = name;
      this.time = time;
      this.season = season;
      this.meetdate = meetdate;
   }

   public String getName() {
      return this.name;
   }

   public String getTime() {
      return this.time;
   }

   public String getSeason() {
      return this.season;
   }

   public String getMeetdate() {
      return this.meetdate;
   }
}
