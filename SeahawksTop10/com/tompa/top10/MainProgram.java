package com.tompa.top10;

import com.tompa.top10.SwimFormatter;
import java.io.IOException;
import java.text.ParseException;

public class MainProgram {

   static void testTimeParse(String s) {
      try {
         double e = SwimFormatter.parseTime(s);
         System.out.println("VALUE: " + e);
      } catch (ParseException var3) {
         System.out.println("PARSING EXCEPTION: " + var3.getMessage());
      }

   }

   public static void main(String[] args) throws IOException {
      testTimeParse("18:22.65");
      testTimeParse("1:02.33");
      testTimeParse("22.37");
      testTimeParse(":22.37");
      testTimeParse("65:22.37");
      testTimeParse("5:62.37");
      testTimeParse("32.3a");
      testTimeParse("32.37");
      testTimeParse("4:15.00");
      testTimeParse("1:59.23");
      double d = 119.23D;
      System.out.println("119.23 " + d);
   }
}
