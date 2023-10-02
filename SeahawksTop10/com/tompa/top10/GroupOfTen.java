package com.tompa.top10;

import com.tompa.top10.SwimmerResult;
import java.util.ArrayList;

class GroupOfTen {

   private String eventName;
   private ArrayList<SwimmerResult> arrayList;


   public GroupOfTen(String eventName) {
      this.eventName = eventName;
      this.arrayList = new ArrayList();
   }

   public void addResult(SwimmerResult result) {
      this.arrayList.add(result);
   }

   public String getEventName() {
      return this.eventName;
   }

   public ArrayList<SwimmerResult> getArrayList() {
      return this.arrayList;
   }
}
