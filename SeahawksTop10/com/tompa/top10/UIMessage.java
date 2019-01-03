package com.tompa.top10;

import javax.swing.JTextArea;

class UIMessage implements Runnable {

   JTextArea textArea;
   String msg;


   public UIMessage(JTextArea textArea, String msg) {
      this.textArea = textArea;
      this.msg = msg;
   }

   public void run() {
      this.textArea.setText(this.msg);
   }
}
