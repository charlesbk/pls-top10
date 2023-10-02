package com.tompa.top10.uiforms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.tompa.top10.LoadRoster;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddUSSID {

   private JPanel addIDpanel;
   private JTextField lastName;
   private JTextField firstName;
   private JTextField txtUSSID;
   private JComboBox cmbGender;
   private JButton btnOK;


   private boolean validUSSID(String id) {
      if(id.length() != 14) {
         return false;
      } else {
         for(int i = 0; i < 6; ++i) {
            if(!Character.isDigit(id.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public AddUSSID(final JDialog parent) {
      this.$$$setupUI$$$();
      this.cmbGender.addItem("Male");
      this.cmbGender.addItem("Female");
      this.btnOK.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String last = AddUSSID.this.lastName.getText().trim();
            String first = AddUSSID.this.firstName.getText().trim();
            String id = AddUSSID.this.txtUSSID.getText().trim().toUpperCase();
            String gender = (String)AddUSSID.this.cmbGender.getSelectedItem();
            if(last.length() != 0 && first.length() != 0) {
               if(!AddUSSID.this.validUSSID(id)) {
                  JOptionPane.showMessageDialog(AddUSSID.this.addIDpanel, "Invalid USSID");
               } else {
                  LoadRoster roster = new LoadRoster();
                  if(roster.idExists(id)) {
                     JOptionPane.showMessageDialog(AddUSSID.this.addIDpanel, "USSID already exists in the database");
                  } else {
                     roster.insertUSSID(last, first, id, gender);
                     JOptionPane.showMessageDialog(AddUSSID.this.addIDpanel, "Added to roster: ".concat(id));
                     parent.setVisible(false);
                     parent.dispose();
                  }
               }
            } else {
               JOptionPane.showMessageDialog(AddUSSID.this.addIDpanel, "Last/First name can not be empty");
            }

         }
      });
   }

   public JPanel getAddIDpanel() {
      return this.addIDpanel;
   }

   // $FF: synthetic method
   private void $$$setupUI$$$() {
      JPanel var1 = new JPanel();
      this.addIDpanel = var1;
      var1.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
      JLabel var2 = new JLabel();
      var2.setText("Enter data very carefully. Copy from SD3 or CL2 file.");
      var1.add(var2, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var3 = new JLabel();
      var3.setText("Last Name");
      var3.setHorizontalAlignment(4);
      var1.add(var3, new GridConstraints(2, 0, 1, 1, 4, 0, 0, 0, new Dimension(100, -1), (Dimension)null, (Dimension)null));
      JTextField var4 = new JTextField();
      this.lastName = var4;
      var1.add(var4, new GridConstraints(2, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), new Dimension(200, -1)));
      JTextField var5 = new JTextField();
      this.firstName = var5;
      var1.add(var5, new GridConstraints(3, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), new Dimension(200, -1)));
      JLabel var6 = new JLabel();
      var6.setText("First Name");
      var1.add(var6, new GridConstraints(3, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var7 = new JLabel();
      var7.setText("USSID");
      var1.add(var7, new GridConstraints(4, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JTextField var8 = new JTextField();
      this.txtUSSID = var8;
      var1.add(var8, new GridConstraints(4, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), new Dimension(200, -1)));
      JLabel var9 = new JLabel();
      var9.setText("Gender");
      var1.add(var9, new GridConstraints(5, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JLabel var10 = new JLabel();
      var10.setText("Date of birth will be derived from USSID");
      var1.add(var10, new GridConstraints(1, 1, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
      JComboBox var11 = new JComboBox();
      this.cmbGender = var11;
      var1.add(var11, new GridConstraints(5, 1, 1, 1, 8, 1, 2, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var12 = new JButton();
      this.btnOK = var12;
      var12.setText("OK");
      var1.add(var12, new GridConstraints(6, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(100, -1)));
   }

   // $FF: synthetic method
   public JComponent $$$getRootComponent$$$() {
      return this.addIDpanel;
   }
}
