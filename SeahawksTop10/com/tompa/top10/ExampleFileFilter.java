package com.tompa.top10;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

public class ExampleFileFilter extends FileFilter {

   private Hashtable<String, FileFilter> filters;
   private String description;
   private String fullDescription;
   private boolean useExtensionsInDescription;


   public ExampleFileFilter() {
      this.filters = null;
      this.description = null;
      this.fullDescription = null;
      this.useExtensionsInDescription = true;
      this.filters = new Hashtable();
   }

   public ExampleFileFilter(String extension) {
      this(extension, (String)null);
   }

   public ExampleFileFilter(String extension, String description) {
      this();
      if(extension != null) {
         this.addExtension(extension);
      }

      if(description != null) {
         this.setDescription(description);
      }

   }

   public ExampleFileFilter(String[] filters) {
      this(filters, (String)null);
   }

   public ExampleFileFilter(String[] filters, String description) {
      this();

      for(int i = 0; i < filters.length; ++i) {
         this.addExtension(filters[i]);
      }

      if(description != null) {
         this.setDescription(description);
      }

   }

   public boolean accept(File f) {
      if(f != null) {
         if(f.isDirectory()) {
            return true;
         }

         String extension = this.getExtension(f);
         if(extension != null && this.filters.get(this.getExtension(f)) != null) {
            return true;
         }
      }

      return false;
   }

   public String getExtension(File f) {
      if(f != null) {
         String filename = f.getName();
         int i = filename.lastIndexOf(46);
         if(i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase();
         }
      }

      return null;
   }

   public void addExtension(String extension) {
      if(this.filters == null) {
         this.filters = new Hashtable(5);
      }

      this.filters.put(extension.toLowerCase(), this);
      this.fullDescription = null;
   }

   public String getDescription() {
      if(this.fullDescription == null) {
         if(this.description != null && !this.isExtensionListInDescription()) {
            this.fullDescription = this.description;
         } else {
            this.fullDescription = this.description == null?"(":this.description + " (";
            Enumeration extensions = this.filters.keys();
            if(extensions != null) {
               for(this.fullDescription = this.fullDescription + "." + (String)extensions.nextElement(); extensions.hasMoreElements(); this.fullDescription = this.fullDescription + ", ." + (String)extensions.nextElement()) {
                  ;
               }
            }

            this.fullDescription = this.fullDescription + ")";
         }
      }

      return this.fullDescription;
   }

   public void setDescription(String description) {
      this.description = description;
      this.fullDescription = null;
   }

   public void setExtensionListInDescription(boolean b) {
      this.useExtensionsInDescription = b;
      this.fullDescription = null;
   }

   public boolean isExtensionListInDescription() {
      return this.useExtensionsInDescription;
   }
}
