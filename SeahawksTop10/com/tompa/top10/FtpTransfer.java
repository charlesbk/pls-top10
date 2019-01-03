package com.tompa.top10;

import com.tompa.top10.ProgressMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.Util;

public class FtpTransfer implements ProgressMessage {

   private static final String FTP_SERVER = "tmoma.com";
   private static final String USER = "teamunify@tmoma.com";
   private static final String PASSWORD = "tuAtSea123";


   public void postMessage(String msg) {
      System.out.println(msg);
   }

   public boolean uploadFolder(String directory) {
      boolean success = false;
      FTPClient ftp = new FTPClient();

      boolean dir;
      try {
         ftp.connect("tmoma.com");
         ftp.login("teamunify@tmoma.com", "tuAtSea123");
         int e = ftp.getReplyCode();
         if(FTPReply.isPositiveCompletion(e)) {
            ftp.setFileType(2);
            File var20 = new File(directory);
            String[] contents = var20.list();

            for(int i = 0; i < contents.length; ++i) {
               String name = contents[i];
               if(name.endsWith(".html")) {
                  this.postMessage("Transferring: ".concat(name));
                  this.uploadFile(ftp, directory.concat("\\").concat(name), name);
               }
            }

            ftp.logout();
            return success;
         }

         dir = false;
      } catch (IOException var18) {
         var18.printStackTrace();
         return success;
      } finally {
         if(ftp.isConnected()) {
            try {
               ftp.disconnect();
            } catch (Exception var17) {
               ;
            }
         }

      }

      return dir;
   }

   private boolean uploadFile(FTPClient ftp, String inFileName, String outFileName) throws IOException {
      FileInputStream in = new FileInputStream(inFileName);
      OutputStream out = ftp.storeFileStream(outFileName);
      Util.copyStream(in, out);
      in.close();
      out.close();
      return ftp.completePendingCommand();
   }

   public static void main(String[] args) {
      FtpTransfer ftp = new FtpTransfer();
      ftp.uploadFolder("C:\\temp\\delme\\byname");
   }
}
