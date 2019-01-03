package com.tompa.top10;

import com.tompa.top10.LongProcess;
import com.tompa.top10.ProgressMessage;
import com.tompa.top10.Worker;

public class LongProcessResponsive extends LongProcess implements Worker, ProgressMessage {

   private ProgressMessage callback;
   private int n;


   public LongProcessResponsive(int n) {
      this.n = n;
   }

   public void postMessage(String msg) {
      this.callback.postMessage(msg);
   }

   public boolean doTheJob(ProgressMessage callback) {
      this.callback = callback;
      super.heavyComputation(this.n);
      this.postMessage("Finished");
      return true;
   }
}
