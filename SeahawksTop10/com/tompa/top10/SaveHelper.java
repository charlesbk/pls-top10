package com.tompa.top10;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SaveHelper {

   public static void save(String path, String file, StringBuilder data) throws IOException {
      (new File(path.toString())).mkdirs();
      File out = new File(path, file);
      save(out, data);
   }

   public static void save(File out, StringBuilder data) throws IOException {
      FileOutputStream fos = null;
      OutputStreamWriter osw = null;

      try {
         fos = new FileOutputStream(out);
         osw = new OutputStreamWriter(fos, "UTF-8");
         osw.write(data.toString());
      } finally {
         if(osw != null) {
            try {
               osw.close();
            } catch (IOException var13) {
               ;
            }
         }

         if(fos != null) {
            try {
               fos.close();
            } catch (IOException var12) {
               ;
            }
         }

      }

   }
}
