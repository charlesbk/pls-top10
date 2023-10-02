package com.tompa.top10;

import com.tompa.top10.FtpTransfer;
import com.tompa.top10.ProgressMessage;
import com.tompa.top10.Worker;

public class FtpTransferResponsive extends FtpTransfer implements Worker, ProgressMessage {

   private String path;
   private ProgressMessage callback;


   public FtpTransferResponsive(String path) {
      this.path = path;
   }

   public void postMessage(String msg) {
      super.postMessage(msg);
      this.callback.postMessage(msg);
   }

   public boolean doTheJob(ProgressMessage callback) {
      this.callback = callback;
      return this.uploadFolder(this.path);
   }
}
