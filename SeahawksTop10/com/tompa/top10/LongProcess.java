package com.tompa.top10;

import com.tompa.top10.ProgressMessage;

public class LongProcess implements ProgressMessage {

   public boolean heavyComputation(int n) {
      for(int i = 0; i < n; ++i) {
         this.postMessage("Loop number ".concat(String.valueOf(i + 1)));

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         }
      }

      this.postMessage("Finished");
      return true;
   }

   public void postMessage(String msg) {
      System.out.println(msg);
   }
}
