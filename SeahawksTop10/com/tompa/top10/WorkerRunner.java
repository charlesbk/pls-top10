package com.tompa.top10;

import com.tompa.top10.ProgressMessage;
import com.tompa.top10.UIMessage;
import com.tompa.top10.Worker;
import java.awt.Cursor;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class WorkerRunner implements Runnable, ProgressMessage {

   private Worker worker;
   private JPanel panel;
   private JTextArea textArea;


   public WorkerRunner(Worker worker, JPanel panel, JTextArea textArea) {
      this.worker = worker;
      this.panel = panel;
      this.textArea = textArea;
   }

   public void run() {
      System.out.println("Started long process");
      this.panel.setCursor(Cursor.getPredefinedCursor(3));
      this.worker.doTheJob(this);
      this.panel.setCursor((Cursor)null);
      System.out.println("Finished long process");
   }

   public void postMessage(String msg) {
      SwingUtilities.invokeLater(new UIMessage(this.textArea, msg));
   }
}
