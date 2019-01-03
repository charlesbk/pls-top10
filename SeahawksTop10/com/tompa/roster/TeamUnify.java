package com.tompa.roster;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TeamUnify {

   private static final String ACTIVE = "*Active";
   private static final String YEARROUND = "Year-round";


   public static String dateFromID(String id) {
      if(id != null && id.length() == 14) {
         String mmddyy = id.substring(0, 6);
         String yy = mmddyy.substring(4);
         String mmdd = mmddyy.substring(0, 4);
         return yy.compareTo("54") < 0?mmdd.concat("20").concat(yy):mmdd.concat("19").concat(yy);
      } else {
         return null;
      }
   }

   public static String nameCleaner(String name) {
      name = name.replaceAll("\"", "");
      name = name.replaceAll("\\*", "");
      return name.trim();
   }

   public static String cutLast(String name) {
      return name.split(",")[0].trim();
   }

   public static String cutFirst(String name) {
      return name.split(",")[1].trim();
   }

   public static String makeD1(String last, String first, String gender, String id, String dob) {
      if(last.length() > 20) {
         last = last.substring(0, 20);
      }

      if(first.length() > 20) {
         first = first.substring(0, 20);
      }

      return String.format("D1%s%25s%20s%20s%15s%13s", new Object[]{gender, last, first, "", id, dob});
   }

   public static String parseTeamUnify(String line) {
      if(!line.startsWith("\"")) {
         return null;
      } else {
         String[] c = line.split("\t", -1);
         String name = c[0].trim();
         String nameOK = nameCleaner(name);
         String gender = c[1].trim();
         String id = c[2].trim();
         String season = c[6].trim();
         String status = c[15].trim();
         String birthdate = dateFromID(id);
         String last = cutLast(nameOK);
         String first = cutFirst(nameOK);
         return birthdate != null && "*Active".equals(status) && ("Year-round".equals(season) || season.isEmpty())?makeD1(last, first, gender, id, birthdate):null;
      }
   }

   public static void main(String[] args) {
      JFileChooser chooser = new JFileChooser();
      chooser.setDialogTitle("Select Team Unify roster file");
      int returnVal = chooser.showOpenDialog((Component)null);
      if(returnVal == 0) {
         try {
            File e = chooser.getSelectedFile();
            FileInputStream fis = new FileInputStream(e);
            Scanner scanner = new Scanner(fis, "UTF-8");
            StringBuilder sb = new StringBuilder();

            while(scanner.hasNextLine()) {
               String filter = scanner.nextLine();
               String outFile = parseTeamUnify(filter);
               if(outFile != null) {
                  sb.append(outFile).append("\n");
               }
            }

            chooser = new JFileChooser();
            chooser.setDialogTitle("Create Top10 Roster");
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("HyTek roster .hy3", new String[]{"hy3"});
            chooser.setFileFilter(filter1);
            returnVal = chooser.showSaveDialog((Component)null);
            if(returnVal != 0) {
               return;
            }

            File outFile1 = chooser.getSelectedFile();
            PrintWriter out = new PrintWriter(outFile1);
            out.print(sb);
            out.close();
            JOptionPane.showMessageDialog((Component)null, outFile1.getCanonicalFile(), "File saved as...", 1);
         } catch (Exception var10) {
            JOptionPane.showMessageDialog((Component)null, var10.getMessage(), "Error", 0);
         }

      }
   }
}
