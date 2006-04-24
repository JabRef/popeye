/**
Popeye - Java (Language) Properties File Editor

Copyright (C) 2005 Raik Nagel <kiar@users.sourceforge.net>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
* Neither the name of the author nor the names of its contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// created by : r.nagel 08.08.2005
//
// function : handling of all configuration stuff
//
// todo     :
//
// modified :

package net.sf.langproper.config ;

import java.util.prefs.* ;
import net.sf.langproper.* ;
import java.util.logging.Logger;
import java.io.*;

public class TUserPreferences
{
  // Version of preferences
  public static final int PREFS_VERSION = 2 ;

  private Preferences userPrefs ;

// used by the mark all/mark only visible entries feature
  public static final String
      MARK_ALL_ENTRIES = "all",
      MARK_VISIBLE_ONLY = "visible",
      MARK_COLUMNS_ONLY = "columns" ;

  // some buffer variables for "time critical" preferences
  private String markEntriesMode ;
  private boolean confirm1, confirm2 ;
  private String replaceString = "_" ;

  public TUserPreferences()
  {
    userPrefs = Preferences.userNodeForPackage( Starter.class ) ;

    init() ;
  }

  // --------------------------------------------------------------------------
  private final void init()
  {
      // read some buffered options
      readMarkEntriesMode() ;
      readConfirmDataOnFocusLost();
      readReplaceString() ;
      readReplaceSpaceInput();
  }

  /** delete all keys and restore the default settings */
  public void reset()
  {
    try
    {
      userPrefs.clear() ;
      init() ;
    }
    catch (Exception e) {}
  }

  // --------------------------------------------------------------------------
  /** detecting of older userprefs versions */
  public int getVersion()
  {
    return userPrefs.getInt("version", 1) ;
  }

  // --------------------------------------------------------------------------
  /** write the user-preferences into the system database */
  public void save() throws BackingStoreException
  {
    userPrefs.putInt("version", PREFS_VERSION);
    try
    {
      userPrefs.flush();
      userPrefs.sync() ;
    }
    catch (IllegalStateException e)
    {
      Logger.global.warning("could not save the current configuration");
      System.out.println(e.getMessage());
    }
  }

  // --------------------------------------------------------------------------

  /** reads a configuration key direct from database */
  public int getDirectInt(String key, int defaultValue)
  {
    return userPrefs.getInt(key, defaultValue) ;
  }

  /** write a configuration key direct to the database */
  public void setDirectInt(String key, int value)
  {
    userPrefs.putInt(key, value) ;
  }

  /** reads a configuration key direct from database */
  public boolean getDirectBool(String key, boolean defaultValue)
  {
    return userPrefs.getBoolean(key, defaultValue) ;
  }

  /** write a configuration key direct to the database */
  public void setDirectBool(String key, boolean value)
  {
    userPrefs.putBoolean(key, value) ;
  }

  /** reads a configuration key direct from database */
  public String getDirectString(String key, String defaultValue)
  {
    return userPrefs.get(key, defaultValue) ;
  }

  /** write a configuration key direct to the database */
  public void setDirectString(String key, String value)
  {
    userPrefs.put(key, value) ;
  }

  // --------------------------------------------------------------------------
  public void saveToFile(File file)
  {
    try
    {
      FileOutputStream os = new FileOutputStream( file ) ;
      userPrefs.exportSubtree( os );
    }
    catch (Exception e)
    {
      Logger.global.warning("error saving settings to external file ->"
                            +e.getMessage());
    }
  }

  /** load the settings from extern xml file, it returns true if no errors...*/
  public boolean loadFromFile(File file)
  {
    boolean back = true ;
    try
    {
      FileInputStream os = new FileInputStream( file ) ;
      userPrefs.importPreferences(os);
      init() ;
    }
    catch (Exception e)
    {
      Logger.global.warning("error loading settings from external file ->"
                            +e.getMessage());
      back = false ;
    }

    return back ;
  }



  // --------------------------------------------------------------------------
  /** if it returns true then saveFile in TLangProps creates a .bak file */
  public boolean makeBackupFiles()
  {
    return userPrefs.getBoolean("makeBackupFile", true) ;
  }

  public void setMakeBackupFiles(boolean value)
  {
    userPrefs.putBoolean("makeBackupFile", value) ;
  }

  // --------------------------------------------------------------------------
  /** if it returns true, then keys without a translation were saved into
   *  every translation file (empty value) .
   * Otherwise only keys with a translation were saved into the translation
   * file.
   * e.g. if languages lang1, lang2, lang3 available and there
   * are some definitions like : key=lang1, key=lang3
   * true  => key were saved into lang1, lang2 and lang3 file
   * false => key were saved into lang1 and lang3
   */
  public boolean saveEmptyKeys()
  {
    return userPrefs.getBoolean("saveEmptyKeys", true) ;
  }

  public void setSaveEmptyKeys(boolean value)
  {
    userPrefs.putBoolean("saveEmptyKeys", value) ;
  }

  // --------------------------------------------------------------------------
  /** if it returns <code>false</code>, then entries are saved using \\u escape sequence .
   * Otherwise entries are saved as a WYSIWYG chars.
   */
  public boolean saveWYSIWYGmode()
  {
    return userPrefs.getBoolean("saveWYSIWYGmode", true) ;
  }

  public void setSaveWYSIWYGmode(boolean value)
  {
    userPrefs.putBoolean("saveWYSIWYGmode", value) ;
  }

  // --------------------------------------------------------------------------
  /** if it returns true, all whitespaces between 2 words are
   *  replaced by "_" (default) or a user defined String (replaceString) */
  public boolean replaceSpaceInInput()
  {
    return confirm1 ;
  }

  public void setReplaceSpaceInput(boolean newValue)
  {
    confirm1 = newValue ;
    userPrefs.putBoolean("replaceSpace", newValue) ;
  }

  private void readReplaceSpaceInput()
  {
    confirm1 = userPrefs.getBoolean("replaceSpace", false) ;
  }

  /** the String for the replaceSpaceInInput function */
  public String getReplaceString()
  {
    return replaceString ;
  }

  /** set the String for the replaceSpaceInInput function */
  public void setReplaceString(String newValue)
  {
    replaceString = newValue ;
    userPrefs.put("replaceSpaceString", replaceString);
  }

  private void readReplaceString()
  {
    replaceString = userPrefs.get("replaceSpaceString", replaceString) ;
  }


  // --------------------------------------------------------------------------

  // ask, if some data are changed in inputfield and another component becomes
  // visible
  // all inputs in TTextFieldPanel must be confirmed by enter
  public boolean confirmDataOnFocusLost()
  {
    return confirm2 ;
  }

  public void setConfirmDataOnFocusLost(boolean newValue)
  {
    confirm2 = newValue ;
    userPrefs.putBoolean("confirmWithoutEnter", newValue) ;
  }

  public void readConfirmDataOnFocusLost()
  {
    confirm2 = userPrefs.getBoolean( "confirmWithoutEnter", true ) ;
  }

  // --------------------------------------------------------------------------
  // There are different modes for marking of inclompete entries

  /** returns true, if the mode has changed */
  public boolean setMarkEntriesMode( String newMode )
  {
    if (newMode.hashCode() != markEntriesMode.hashCode())
    {
      markEntriesMode = newMode ;
      userPrefs.put("markEntriesMode", newMode);
      return true ;
    }
    return false ;
  }

  private void readMarkEntriesMode()
  {
    markEntriesMode = userPrefs.get("markEntriesMode", this.MARK_VISIBLE_ONLY) ;
  }

  /** returns the value for markEntriesMode
   *  This value is buffered, because the TableCellRenderer needs it for
   *  every line
   * */
  public String getMarkEntriesMode( )
  {
    return markEntriesMode ;
  }

  public boolean isMarkIncomplete()
  {
    return userPrefs.getBoolean("markIncompleteEntries", true) ;
  }

  public void setMarkIncomplete(boolean newValue)
  {
    userPrefs.putBoolean("markIncompleteEntries", newValue);
  }


  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  public boolean isShowSource()
  {
    return userPrefs.getBoolean("showSource", false) ;
  }

  public void setShowSource(boolean newValue)
  {
    userPrefs.putBoolean("showSource", newValue);
  }

  // --------------------------------------------------------------------------
  //   remember last opened file(s)
  // --------------------------------------------------------------------------

  /** returns the full filename of the last loaded file
   * */
  public String getLastFileName()
  {
    return userPrefs.get("lastfile", "") ;
  }

  /** returns the full filename of the last loaded project */
  public String getLastProject()
  {
    return userPrefs.get("lastProject", "") ;
  }


  /** returns the directory of the last loaded file */
  public String getLastWorkDirectory()
  {
    String back = userPrefs.get("workingDirectory", ".") ;

    // ensure a valid result
    if (back == null)
      back = "." ;
    else if (back.length() < 1)
      back = "." ;

    return back ;
  }

  /** save all informations about the last opened file
   * */
  public void setLastFile(File file)
  {
    try
    {
      File dummy = file.getCanonicalFile() ;
      userPrefs.put("lastfile", dummy.getAbsolutePath());
      userPrefs.put("workingDirectory", dummy.getParent());
    }
    catch (Exception e) {}
  }

  /** save all informations about the last opened project */
  public void setLastProject(File file)
  {

    if (file != null)
    {
      try
      {
        File dummy = file.getCanonicalFile() ;
        userPrefs.put( "lastProject", dummy.getAbsolutePath() ) ;
        userPrefs.put( "workingDirectory", dummy.getParent() ) ;
      }
      catch ( Exception e )
      {}
    }
  }


  public boolean getAutomaticLastLoad()
  {
    return userPrefs.getBoolean("load_last_files", true) ;
  }

  public void setAutomaticLastLoad(boolean value)
  {
    userPrefs.putBoolean("load_last_files", value) ;
  }


  // --------------------------------------------------------------------------
  public boolean getShowFlagsInTable()
  {
    return userPrefs.getBoolean("flagtable", false) ;
  }

  public void setShowFlagsInTable(boolean value)
  {
    userPrefs.putBoolean("flagtable", value) ;
  }

  // --------------------------------------------------------------------------
  /** if it returns true then saveFile in TLangProps creates a .bak file */
  public boolean getConfirmShred()
  {
    return userPrefs.getBoolean("confirmShred", true) ;
  }

  public void setConfirmShred(boolean value)
  {
    userPrefs.putBoolean("confirmShred", value) ;
  }


}
