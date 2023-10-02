package com.tompa.top10;

import com.tompa.top10.GroupOfTen;
import java.util.ArrayList;

class SwimmerTopTenGroups {

   private String name;
   private ArrayList<GroupOfTen> nameInGroups = new ArrayList();


   public SwimmerTopTenGroups(String name) {
      this.name = name;
   }

   public ArrayList<GroupOfTen> getNameInGroups() {
      return this.nameInGroups;
   }

   public void addGroup(GroupOfTen group) {
      this.nameInGroups.add(group);
   }

   public String getName() {
      return this.name;
   }
}
