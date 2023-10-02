package com.tompa.top10.uicode;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.tompa.top10.ExampleFileFilter;
import com.tompa.top10.LoadResults;
import com.tompa.top10.LoadRoster;
import com.tompa.top10.SD3Loader;
import com.tompa.top10.SwimFormatter;
import com.tompa.top10.TopTenGroup;
import com.tompa.top10.TopTenUpdater;
import com.tompa.top10.uicode.ManualEntry;
import com.tompa.top10.uiforms.AddUSSID;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainPanel {

   static final String TEAM_CODE = "PCPLS";
   static final String DATA_FOLDER = "c:/temp/delme";
   static final String BYNAME_FOLDER = "c:/temp/delme".concat("/byname");
   private String meetName;
   private int meetDate;
   private JButton btnHTML;
   private JButton btnExcel;
   private JPanel mainPanel;
   private JTextArea textArea;
   private JButton btnParseMeet;
   private JButton btnApplyMeet;
   private JButton btnManual;
   private JButton btnPrint;
   private JButton btnBackup;
   private JButton btnRestore;
   private JButton btnHY3Roster;
   private JButton btnShowRoster;
   private JButton btnDeleteUSSID;
   private JButton btnAddUSSID;
   private JButton btnPrint4short;
   private JButton btnPrint4long;
   private JButton btnTeamUnifyRoster;
   private JButton btnRecords;
   private JButton btnLcm;
   private JButton btnScy;
   private JButton btnTeamUnifySDIF;
   private static final String MSG_PRINT = "\nFORMAT OPTIONS\nOrientation: Landscape\nFont face: Lucida Console\nFont size: 8pt.";
   private static final String MSG_4x4 = "\nFORMAT OPTIONS\nOrientation: Landscape\nFont face: Lucida Console\nFont size: 6pt.";


   public JPanel getMainPanel() {
      return this.mainPanel;
   }

   public MainPanel(final JFrame frame) {
      this.$$$setupUI$$$();
      this.textArea.setText("Message Area");
      this.btnPrint.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "Records3.txt";
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnPrint.setEnabled(false);

               try {
                  TopTenGroup.savePrintable(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()).concat("\nFORMAT OPTIONS\nOrientation: Landscape\nFont face: Lucida Console\nFont size: 8pt."));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnPrint.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnPrint4short.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "Records4S.txt";
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnPrint4short.setEnabled(false);

               try {
                  TopTenGroup.save4x4(chooser.getSelectedFile(), "SHORT");
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()).concat("\nFORMAT OPTIONS\nOrientation: Landscape\nFont face: Lucida Console\nFont size: 6pt."));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnPrint4short.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnPrint4long.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "Records4L.txt";
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnPrint4long.setEnabled(false);

               try {
                  TopTenGroup.save4x4(chooser.getSelectedFile(), "LONG");
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()).concat("\nFORMAT OPTIONS\nOrientation: Landscape\nFont face: Lucida Console\nFont size: 6pt."));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnPrint4long.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnExcel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = SwimFormatter.formatToday().append(".txt").toString();
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnExcel.setEnabled(false);

               try {
                  TopTenGroup.saveExcelTabulated(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnExcel.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnRecords.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "Records".concat(SwimFormatter.formatToday().append(".txt").toString());
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnRecords.setEnabled(false);

               try {
                  TopTenGroup.saveRecords(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnRecords.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnLcm.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "IPLS.lcm";
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnLcm.setEnabled(false);

               try {
                  TopTenGroup.saveLCM(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnLcm.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnScy.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "IPLS.scy";
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnScy.setEnabled(false);

               try {
                  TopTenGroup.saveSCY(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnScy.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnBackup.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            String filename = "backup".concat(SwimFormatter.formatToday().toString()).concat(".txt");
            chooser.setSelectedFile(new File(filename));
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showSaveDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnBackup.setEnabled(false);

               try {
                  TopTenGroup.saveBackup(chooser.getSelectedFile());
                  MainPanel.this.textArea.setText("File saved: ".concat(chooser.getSelectedFile().getPath()));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnBackup.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnRestore.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            int returnVal = chooser.showOpenDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               MainPanel.this.btnRestore.setEnabled(false);

               try {
                  String ex = chooser.getSelectedFile().getPath();
                  System.out.println(ex);
                  LoadResults top10 = new LoadResults("top10");
                  top10.load(ex);
                  MainPanel.this.textArea.setText("Restored from: ".concat(ex));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.btnRestore.setEnabled(true);
               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnHTML.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(1);
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            chooser.setDialogTitle("Select Directory");
            int returnVal = chooser.showDialog(MainPanel.this.mainPanel, "OK");
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               String path = chooser.getSelectedFile().getPath();

               try {
                  TopTenGroup.saveTopFiles(path.concat("/records"), path.concat("/byname"), "nameIndex.html");
                  MainPanel.this.textArea.setText("Files saved in directory: ".concat(path));
               } catch (IOException var6) {
                  MainPanel.this.textArea.setText(var6.getMessage());
                  var6.printStackTrace();
               }

               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnParseMeet.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select CL2 or SD3 file");
            chooser.setCurrentDirectory(new File("c:/temp/sd3"));
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("sd3");
            filter.addExtension("cl2");
            filter.setDescription("Meet results files");
            chooser.setFileFilter(filter);
            try {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               SD3Loader sd3Loader = new SD3Loader();
               //String folderOfCl2Files = "~/Documents/Dev/seaHawk/python/SD3_zip_files";
               String folderOfCl2Files = "/Users/huili.wang/Documents/Dev/seaHawk/python/SD3_zip_files";
               File dir = new File(folderOfCl2Files);
               File[] directoryListing = dir.listFiles();
               if (directoryListing != null) {
                  for (File file : directoryListing) {
                     sd3Loader.loadTeamTimes(file.getPath(), "PCPLS");
                     TopTenUpdater topTenUpdater = new TopTenUpdater();
                     topTenUpdater.updateFromDatabase(sd3Loader.getMeetDate(), sd3Loader.getMeetName());
                  }
               }
               MainPanel.this.textArea.setText("Applied");
               MainPanel.this.textArea.setCaretPosition(0);
            } catch (IOException var7) {
               MainPanel.this.textArea.setText(var7.getMessage());
               var7.printStackTrace();
            }
            MainPanel.this.mainPanel.setCursor((Cursor)null);

            // the following block is the original lines --Huili
            /*
            int returnVal = chooser.showOpenDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               SD3Loader sd3Loader = new SD3Loader();

               try {
                  sd3Loader.loadTeamTimes(chooser.getSelectedFile().getPath(), "PCPLS");
                  MainPanel.this.meetName = sd3Loader.getMeetName();
                  MainPanel.this.meetDate = sd3Loader.getMeetDate();
                  MainPanel.this.btnApplyMeet.setEnabled(true);
                  MainPanel.this.textArea.setText("Meet file parsed:\n".concat(MainPanel.this.meetName).concat("\n").concat(sd3Loader.getMessages()));
                  MainPanel.this.textArea.setCaretPosition(0);
               } catch (IOException var7) {
                  MainPanel.this.textArea.setText(var7.getMessage());
                  var7.printStackTrace();
               }

               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }*/

         }
      });
      this.btnApplyMeet.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
            MainPanel.this.textArea.setText("");
            TopTenUpdater topTenUpdater = new TopTenUpdater();
            MainPanel.this.btnApplyMeet.setEnabled(false);
            topTenUpdater.updateFromDatabase(MainPanel.this.meetDate, MainPanel.this.meetName);
            MainPanel.this.textArea.setText("Meet results applied to Top Ten:\n".concat(MainPanel.this.meetName).concat("\n").concat(topTenUpdater.getMessages()));
            MainPanel.this.textArea.setCaretPosition(0);
            MainPanel.this.mainPanel.setCursor((Cursor)null);
         }
      });
      this.btnShowRoster.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            LoadRoster loadRoster = new LoadRoster();
            MainPanel.this.textArea.setText(loadRoster.getRoster());
            MainPanel.this.textArea.setCaretPosition(0);
         }
      });
      this.btnHY3Roster.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select TM (HY3) Roster file");
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("hy3");
            filter.setDescription("HyTek Roster files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               LoadRoster loadRoster = new LoadRoster();

               try {
                  loadRoster.loadHY3(chooser.getSelectedFile().getPath());
                  MainPanel.this.btnApplyMeet.setEnabled(false);
                  MainPanel.this.textArea.setText(loadRoster.getMessage());
                  MainPanel.this.textArea.setCaretPosition(0);
               } catch (IOException var7) {
                  MainPanel.this.textArea.setText(var7.getMessage());
                  var7.printStackTrace();
               }

               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnTeamUnifyRoster.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Team Unify Roster file");
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("txt");
            filter.setDescription("Team Unify Roster files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               LoadRoster loadRoster = new LoadRoster();

               try {
                  loadRoster.loadTeamUnify(chooser.getSelectedFile().getPath());
                  MainPanel.this.btnApplyMeet.setEnabled(false);
                  MainPanel.this.textArea.setText(loadRoster.getMessage());
                  MainPanel.this.textArea.setCaretPosition(0);
               } catch (IOException var7) {
                  MainPanel.this.textArea.setText(var7.getMessage());
                  var7.printStackTrace();
               }

               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnTeamUnifySDIF.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Team Unify SDIF file");
            chooser.setCurrentDirectory(new File("c:/temp/delme"));
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("sd3");
            filter.setDescription("Team Unify SDIF files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(MainPanel.this.mainPanel);
            if(returnVal == 0) {
               MainPanel.this.mainPanel.setCursor(Cursor.getPredefinedCursor(3));
               LoadRoster loadRoster = new LoadRoster();

               try {
                  loadRoster.loadTeamUnifySDIF(chooser.getSelectedFile().getPath());
                  MainPanel.this.btnApplyMeet.setEnabled(false);
                  MainPanel.this.textArea.setText(loadRoster.getMessage());
                  MainPanel.this.textArea.setCaretPosition(0);
               } catch (IOException var7) {
                  MainPanel.this.textArea.setText(var7.getMessage());
                  var7.printStackTrace();
               }

               MainPanel.this.mainPanel.setCursor((Cursor)null);
            }

         }
      });
      this.btnDeleteUSSID.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String s = (String)JOptionPane.showInputDialog(frame, "Useful for college-away swimmers.\nThey may swim for other team in the same meet.", "USSID to delete from roster", -1, (Icon)null, (Object[])null, "");
            if(s != null && s.length() == 14) {
               int deleted = LoadRoster.deleteIdFromRoster(s);
               MainPanel.this.textArea.setText(s + " IDs deleted: " + deleted);
               MainPanel.this.textArea.setCaretPosition(0);
            } else {
               MainPanel.this.textArea.setText("None deleted");
            }
         }
      });
      this.btnManual.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ManualEntry manualEntry = new ManualEntry();
            JDialog dialog = new JDialog(frame, true);
            dialog.setContentPane(manualEntry.getPanel());
            dialog.setBounds(0, 0, 600, 700);
            dialog.setResizable(false);
            dialog.setTitle("Manual Time Entry");
            dialog.setVisible(true);
         }
      });
      this.btnAddUSSID.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(frame, true);
            AddUSSID addID = new AddUSSID(dialog);
            dialog.setContentPane(addID.getAddIDpanel());
            dialog.setBounds(0, 0, 400, 300);
            dialog.setResizable(false);
            dialog.setTitle("Add USSID to roster");
            dialog.setVisible(true);
         }
      });
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
      } catch (UnsupportedLookAndFeelException var5) {
         var5.printStackTrace();
      } catch (InstantiationException var6) {
         var6.printStackTrace();
      }

      JFrame frame = new JFrame("Seahawks Top Ten");
      MainPanel form = new MainPanel(frame);
      frame.setContentPane(form.getMainPanel());
      frame.pack();
      frame.setDefaultCloseOperation(2);
      frame.setResizable(false);
      frame.setVisible(true);
   }

   // $FF: synthetic method
   private void $$$setupUI$$$() {
      JPanel var1 = new JPanel();
      this.mainPanel = var1;
      var1.setLayout(new GridLayoutManager(11, 2, new Insets(20, 10, 20, 10), -1, -1, false, false));
      JScrollPane var2 = new JScrollPane();
      var1.add(var2, new GridConstraints(10, 0, 1, 1, 0, 3, 7, 7, new Dimension(600, 200), new Dimension(-1, 200), new Dimension(-1, 200)));
      JTextArea var3 = new JTextArea();
      this.textArea = var3;
      var3.setEditable(false);
      var2.setViewportView(var3);
      JButton var4 = new JButton();
      this.btnParseMeet = var4;
      var4.setText("Analyze Meet Results (SD3 or CL2)");
      var1.add(var4, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var5 = new JButton();
      this.btnManual = var5;
      var5.setText("Manual Time Entry");
      var1.add(var5, new GridConstraints(1, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var6 = new JButton();
      this.btnExcel = var6;
      var6.setText("Create Excel Compatible Output");
      var1.add(var6, new GridConstraints(2, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var7 = new JButton();
      this.btnBackup = var7;
      var7.setText("Backup");
      var1.add(var7, new GridConstraints(6, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var8 = new JButton();
      this.btnDeleteUSSID = var8;
      var8.setText("Delete USSID from Roster");
      var1.add(var8, new GridConstraints(9, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var9 = new JButton();
      this.btnLcm = var9;
      var9.setText("Export .lcm");
      var1.add(var9, new GridConstraints(5, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var10 = new JButton();
      this.btnApplyMeet = var10;
      var10.setEnabled(false);
      var10.setText("Apply Meet Results");
      var1.add(var10, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var11 = new JButton();
      this.btnHTML = var11;
      var11.setText("Create HTML Files");
      var1.add(var11, new GridConstraints(2, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var12 = new JButton();
      this.btnPrint4long = var12;
      var12.setText("Format 4x4 LONG course");
      var1.add(var12, new GridConstraints(3, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var13 = new JButton();
      this.btnPrint4short = var13;
      var13.setText("Format 4x4 SHORT course");
      var1.add(var13, new GridConstraints(3, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var14 = new JButton();
      this.btnPrint = var14;
      var14.setText("Format by age group");
      var1.add(var14, new GridConstraints(4, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var15 = new JButton();
      this.btnRecords = var15;
      var15.setText("Records");
      var1.add(var15, new GridConstraints(4, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var16 = new JButton();
      this.btnScy = var16;
      var16.setText("Export .scy");
      var1.add(var16, new GridConstraints(5, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var17 = new JButton();
      this.btnRestore = var17;
      var17.setText("Restore");
      var1.add(var17, new GridConstraints(6, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var18 = new JButton();
      this.btnShowRoster = var18;
      var18.setText("Show Roster");
      var1.add(var18, new GridConstraints(7, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var19 = new JButton();
      this.btnAddUSSID = var19;
      var19.setText("Add USSID to Roster");
      var1.add(var19, new GridConstraints(9, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var20 = new JButton();
      this.btnHY3Roster = var20;
      var20.setText("Load HY3 Roster");
      var1.add(var20, new GridConstraints(7, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var21 = new JButton();
      this.btnTeamUnifyRoster = var21;
      var21.setText("Load TeamUnify Roster");
      var1.add(var21, new GridConstraints(8, 0, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
      JButton var22 = new JButton();
      this.btnTeamUnifySDIF = var22;
      var22.setText("Load TeamUnify SDIF");
      var1.add(var22, new GridConstraints(8, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, new Dimension(200, -1)));
   }

   // $FF: synthetic method
   public JComponent $$$getRootComponent$$$() {
      return this.mainPanel;
   }
}
