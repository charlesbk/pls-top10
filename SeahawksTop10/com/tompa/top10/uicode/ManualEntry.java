package com.tompa.top10.uicode;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.tompa.top10.ExecuteStatement;
import com.tompa.top10.SwimFormatter;
import com.tompa.top10.TopTenGroup;
import com.tompa.top10.TopTenUpdater;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ManualEntry {

   private HashMap<String, String> rosterUSSID;
   private HashMap<String, String> rosterDOB;
   private String meetName;
   private int meetDate;
   private JPanel manualEntry;
   private JTextField txtMeetName;
   private JTextField txtTime;
   private JComboBox cmbStroke;
   private JComboBox cmbDistance;
   private JComboBox cmbCourse;
   private JComboBox cmbGender;
   private JComboBox cmbName;
   private JButton btnEnter;
   private JLabel lblEvent;
   private JTextField txtDate;


   public JPanel getPanel() {
      return this.manualEntry;
   }

   private void updateEvent() {
      String date = this.txtDate.getText();
      String event = "";
      if(date.equals(this.verifyDate(date))) {
         String name = (String)this.cmbName.getSelectedItem();
         String dob = (String)this.rosterDOB.get(name);
         int age = SwimFormatter.computeAgeMMDDYYYY(Integer.parseInt(date), dob);
         String agegroup = SwimFormatter.toAgeGroup(age);
         event = "<html>" + TopTenGroup.htmlGroup((String)this.cmbGender.getSelectedItem(), (String)this.cmbCourse.getSelectedItem(), agegroup, (String)this.cmbDistance.getSelectedItem(), (String)this.cmbStroke.getSelectedItem(), (String)null).toString() + "</html>";
      }

      this.lblEvent.setText(event);
   }

   public ManualEntry() {
      HashMap var10001 = new HashMap();
      this.$$$setupUI$$$();
      this.rosterUSSID = var10001;
      this.rosterDOB = new HashMap();
      this.cmbCourse.addItem("SHORT");
      this.cmbCourse.addItem("LONG");
      this.cmbStroke.addItem("FREE");
      this.cmbStroke.addItem("FLY");
      this.cmbStroke.addItem("BACK");
      this.cmbStroke.addItem("BREAST");
      this.cmbStroke.addItem("I.M.");
      this.cmbGender.addItem("GIRLS");
      this.cmbGender.addItem("BOYS");
      ManualEntry.MyDocumentListener myDocumentListener = new ManualEntry.MyDocumentListener();
      this.txtDate.getDocument().addDocumentListener(myDocumentListener);
      this.selectCourse("SHORT");
      this.populateRoster();
      this.updateEvent();
      final TopTenUpdater updater = new TopTenUpdater();
      this.cmbCourse.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String selection = (String)ManualEntry.this.cmbCourse.getSelectedItem();
            ManualEntry.this.selectCourse(selection);
            ManualEntry.this.updateEvent();
         }
      });
      this.cmbGender.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ManualEntry.this.updateEvent();
         }
      });
      this.cmbDistance.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ManualEntry.this.updateEvent();
         }
      });
      this.cmbStroke.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ManualEntry.this.updateEvent();
         }
      });
      this.cmbName.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ManualEntry.this.updateEvent();
         }
      });
      this.btnEnter.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String meet = ManualEntry.this.txtMeetName.getText();
            String date = ManualEntry.this.txtDate.getText();
            String time = ManualEntry.this.txtTime.getText();
            double dTime = 0.0D;
            String error = null;
            if(meet.length() == 0) {
               error = "Meet name can not be empty";
            } else if(!date.equals(ManualEntry.this.verifyDate(date))) {
               error = "Invalid Date Format";
            } else {
               try {
                  dTime = SwimFormatter.parseTime(time);
               } catch (ParseException var15) {
                  error = "Invalid time entry";
               }
            }

            if(error != null) {
               JOptionPane.showMessageDialog(ManualEntry.this.manualEntry, error);
            } else {
               ManualEntry.this.txtMeetName.setEditable(false);
               ManualEntry.this.txtDate.setEditable(false);
               ManualEntry.this.meetName = meet;
               ManualEntry.this.meetDate = Integer.parseInt(date);
               String course = (String)ManualEntry.this.cmbCourse.getSelectedItem();
               String gender = (String)ManualEntry.this.cmbGender.getSelectedItem();
               String name = (String)ManualEntry.this.cmbName.getSelectedItem();
               String ussid = (String)ManualEntry.this.rosterUSSID.get(name);
               String distance = (String)ManualEntry.this.cmbDistance.getSelectedItem();
               String stroke = (String)ManualEntry.this.cmbStroke.getSelectedItem();
               updater.insertManually(ManualEntry.this.meetDate, ManualEntry.this.meetName, course, gender, ussid, name, distance, stroke, dTime);
               String msg = updater.getMessages();
               if(msg.length() > 0) {
                  ManualEntry.this.updateEvent();
                  JOptionPane.showMessageDialog(ManualEntry.this.manualEntry, msg);
               }

            }
         }
      });
   }

   private String verifyDate(String date) {
      String retDate = null;
      if(date.length() != 0 && date.length() == 8) {
         Calendar cal = Calendar.getInstance();
         String yy = date.substring(0, 4);
         String mm = date.substring(4, 6);
         String dd = date.substring(6, 8);

         try {
            cal.set(Integer.valueOf(yy).intValue(), Integer.valueOf(mm).intValue() - 1, Integer.valueOf(dd).intValue());
         } catch (NumberFormatException var8) {
            return null;
         }

         retDate = String.format("%1$tY%1$tm%1$td", new Object[]{cal});
         return retDate;
      } else {
         return null;
      }
   }

   private void selectCourse(String course) {
      this.cmbDistance.removeAllItems();
      if("SHORT".equals(course)) {
         this.cmbDistance.addItem("25");
         this.cmbDistance.addItem("50");
         this.cmbDistance.addItem("100");
         this.cmbDistance.addItem("200");
         this.cmbDistance.addItem("400");
         this.cmbDistance.addItem("500");
         this.cmbDistance.addItem("1000");
         this.cmbDistance.addItem("1650");
      } else {
         this.cmbDistance.addItem("50");
         this.cmbDistance.addItem("100");
         this.cmbDistance.addItem("200");
         this.cmbDistance.addItem("400");
         this.cmbDistance.addItem("800");
         this.cmbDistance.addItem("1500");
      }

   }

   private void populateRoster() {
      String query = "select lastname, firstname, ussreg, dob from roster order by lastname, firstname";
      ArrayList roster = ExecuteStatement.executeQuery(query, new String[0]);
      Iterator i$ = roster.iterator();

      while(i$.hasNext()) {
         ArrayList row = (ArrayList)i$.next();
         String key = ((String)row.get(0)).concat(" ").concat((String)row.get(1));
         String ussid = (String)row.get(2);
         String dob = (String)row.get(3);
         if(key.length() > 0 && ussid.length() > 0) {
            this.rosterUSSID.put(key, ussid);
            this.rosterDOB.put(key, dob);
            this.cmbName.addItem(key);
         }
      }

   }

   // $FF: synthetic method
   private void $$$setupUI$$$() {
      JPanel var1 = new JPanel();
      this.manualEntry = var1;
      var1.setLayout(new GridLayoutManager(9, 2, new Insets(20, 10, 20, 10), -1, -1, false, false));
      JTextField var2 = new JTextField();
      this.txtMeetName = var2;
      var1.add(var2, new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null));
      JLabel var3 = new JLabel();
      var3.setText("First Day of the Meet (YYYYMMDD):");
      var3.setHorizontalAlignment(4);
      var1.add(var3, new GridConstraints(1, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var4 = new JLabel();
      var4.setText("Meet Name:");
      var4.setHorizontalAlignment(4);
      var4.setHorizontalTextPosition(4);
      var1.add(var4, new GridConstraints(0, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var5 = new JLabel();
      var5.setText("Gender:");
      var1.add(var5, new GridConstraints(3, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var6 = new JLabel();
      var6.setText("Distance:");
      var1.add(var6, new GridConstraints(5, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var7 = new JComboBox();
      this.cmbDistance = var7;
      var1.add(var7, new GridConstraints(5, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var8 = new JLabel();
      var8.setText("Stroke:");
      var1.add(var8, new GridConstraints(6, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var9 = new JComboBox();
      this.cmbStroke = var9;
      var1.add(var9, new GridConstraints(6, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var10 = new JLabel();
      var10.setText("Time (mm:ss.ss or ss.ss):");
      var1.add(var10, new GridConstraints(7, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JTextField var11 = new JTextField();
      this.txtTime = var11;
      var1.add(var11, new GridConstraints(7, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null));
      JButton var12 = new JButton();
      this.btnEnter = var12;
      var12.setText("Enter Result");
      var1.add(var12, new GridConstraints(8, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var13 = new JComboBox();
      this.cmbName = var13;
      var1.add(var13, new GridConstraints(4, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var14 = new JComboBox();
      this.cmbCourse = var14;
      var1.add(var14, new GridConstraints(2, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var15 = new JLabel();
      var15.setText("Course:");
      var1.add(var15, new GridConstraints(2, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var16 = new JComboBox();
      this.cmbGender = var16;
      var1.add(var16, new GridConstraints(3, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var17 = new JLabel();
      var17.setText("Swimmer\'s Name:");
      var1.add(var17, new GridConstraints(4, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var18 = new JLabel();
      this.lblEvent = var18;
      var18.setText("Label");
      var1.add(var18, new GridConstraints(8, 0, 1, 1, 8, 0, 0, 0, new Dimension(300, 300), (Dimension)null, (Dimension)null));
      JTextField var19 = new JTextField();
      this.txtDate = var19;
      var1.add(var19, new GridConstraints(1, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null));
   }

   // $FF: synthetic method
   public JComponent $$$getRootComponent$$$() {
      return this.manualEntry;
   }

   class MyDocumentListener implements DocumentListener {

      public void changedUpdate(DocumentEvent e) {
         ManualEntry.this.updateEvent();
      }

      public void removeUpdate(DocumentEvent e) {
         ManualEntry.this.updateEvent();
      }

      public void insertUpdate(DocumentEvent e) {
         ManualEntry.this.updateEvent();
      }
   }
}
