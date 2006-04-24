/*
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

// created by : r.nagel 4.4.2005
//
// function : all graphical constants and look&feel routines,
//            loading of the default icon sets
//
// todo     : theme handling
//
// modified :


package net.sf.langproper.gui ;

import java.net.* ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.plaf.metal.* ;

import net.sf.langproper.engine.sort.* ;
import net.sf.langproper.gui.project.* ;
import net.sf.langproper.gui.theme.* ;
import net.sf.langproper.TGlobal;
import java.io.File;
import net.sf.langproper.gui.filechooser.*;
import net.sf.quercus.helpsystem.THelpWindows;

public class GUIGlobals
{
  private static String pre = "/resource/images/" ;

  public static URL

  // load/save
  openIconName = GUIGlobals.class.getResource( pre + "open.png" ),
  saveIconName = GUIGlobals.class.getResource( pre + "save.png" ),
  reloadIconName = GUIGlobals.class.getResource( pre + "reload.png" ),

  // open
  fileOpenIconName = GUIGlobals.class.getResource( pre + "fileopen.png" ),
  folderOpenIconName = GUIGlobals.class.getResource( pre + "folder.png" ),

  // source/utf view
  viewSourceIconName = GUIGlobals.class.getResource( pre + "txt.png" ),
  viewIconName = GUIGlobals.class.getResource( pre + "document.png" ),

  // mark/unmark incomplete entries
  viewIncompleteIconName = GUIGlobals.class.getResource( pre + "view_detailed.png" ),
  viewCompleteIconName = GUIGlobals.class.getResource( pre + "view_text.png" ),

  // tabbed textfield panel
  viewAllEntriesIconName = GUIGlobals.class.getResource( pre + "all.png" ),
  viewSingleEntryIconName = GUIGlobals.class.getResource( pre + "single.png" ),
  viewInfoStatsName = GUIGlobals.class.getResource( pre + "services.png" ),


  // TLangTextField status
  requireIconName = GUIGlobals.class.getResource( pre + "require.png" ),
  warningIconName = GUIGlobals.class.getResource( pre + "achtung.png" ),
  errorIconName = GUIGlobals.class.getResource( pre + "problem.png" ),
  checkedIconName = GUIGlobals.class.getResource( pre + "apply.png" ),

  // Table sort direction
  tableSortUpIconName = GUIGlobals.class.getResource( pre + "1uparrow.png" ),
  tableSortDownIconName = GUIGlobals.class.getResource( pre + "1downarrow.png" ),

  // comments available
  tableEntryCommentIconName = GUIGlobals.class.getResource( pre + "attach.png" ),

  // validations
  validationsIconName = GUIGlobals.class.getResource( pre + "achtung.png"), //"info.png" ),

  // find item
  searchIconName = GUIGlobals.class.getResource( pre + "search.png" ),

  // wizard icons
  wizardOkName = GUIGlobals.class.getResource( pre + "ok.png" ),
  wizardCancelName = GUIGlobals.class.getResource( pre + "cancel.png" ),
  wizardNextName = GUIGlobals.class.getResource( pre + "forward.png" ),
  wizardPrevName = GUIGlobals.class.getResource( pre + "back.png" ),
  wizardWallPaperName = GUIGlobals.class.getResource( pre + "clouds3.jpg" ),

  ciWallPaperName = GUIGlobals.class.getResource( pre + "clouds2.jpg" ),

  // error report dialog
  errorReportName1 = GUIGlobals.class.getResource( pre + "error.png" ),

  // help dialog icons
  nextHelpIconName = GUIGlobals.class.getResource( pre + "1rightarrow.png" ),
  prevHelpIconName = GUIGlobals.class.getResource( pre + "1leftarrow.png" ),
  littleHelpIconName = GUIGlobals.class.getResource( pre + "help_small.png" ),
  homeIconName = GUIGlobals.class.getResource( pre + "gohome.png" ),


  // find item
  trashIconName = GUIGlobals.class.getResource( pre + "trash.png" ),

  // under development icons
  develIconName = GUIGlobals.class.getResource( pre + "lock.png" )
      ;

  // the default font for the language GUI compoments
  // Using the Lucida fonts: Sun's JREs contain this family of physical fonts,
  // which is also licensed for use in other implementations of the Java
  // platform. These fonts are physical fonts, but don't depend on the host
  // operating system.
  public static String defaultLanguageFontName = "Lucida" ;
  public static Font defaultLanguageFont = null ;

  public static ImageIcon viewIcon2 ;
  public static ImageIcon viewIcon1 ;
  public static ImageIcon markIcon2 ;
  public static ImageIcon markIcon1 ;
  public static ImageIcon tableSortUpIcon ;
  public static ImageIcon tableSortDownIcon ;
  public static ImageIcon tableEntryCommentIcon ;
  public static ImageIcon tableEntryValidationInfoIcon ;
  public static ImageIcon fileOpenIcon ;
  public static ImageIcon folderOpenIcon ;
  public static ImageIcon trashIcon ;

  public static Color
      SearchFieldColor = new Color( 0xf5f6a5 ) ;

  public static DefaultTheme theme = null ;

  // Application (popeye) mainframe
  public static TMainFrame APPLICATION_MAINFRAME = null ;

  // project dialog
  public static ProjectSettingsDialog PROJECT_PROPERTIES_DIALOG = null ;

  // dialog for selecting project languages
  public static TLangSelectionDialog PROJECT_SAVE_SINGLE_DIALOG = null ;

  public static TFileChooser PROJECT_LOAD_SAVE_DIALOG = null ;

  // global help window
  public static THelpWindows APPLICATION_HELP_FRAME = null ;


  // main panel
  public static TOverviewPanel oPanel = null ;

  // sorting of table entries
  public static SortEngine sortDirection = new SortEngine() ;

  /** load icons and initialize other resources */
  public static void init()
  {
    // get the preferred font
    defaultLanguageFontName = TGlobal.config.getDirectString("Font",
                                                 defaultLanguageFontName) ;
    defaultLanguageFont = new Font(defaultLanguageFontName, Font.PLAIN, 12) ;

    try
    {
      viewIcon2 = new ImageIcon( GUIGlobals.viewIconName ) ;
      viewIcon1 = new ImageIcon( GUIGlobals.viewSourceIconName ) ;
      markIcon2 = new ImageIcon( GUIGlobals.viewIncompleteIconName ) ;
      markIcon1 = new ImageIcon( GUIGlobals.viewCompleteIconName ) ;
      tableSortUpIcon = new ImageIcon( tableSortUpIconName ) ;
      tableSortDownIcon = new ImageIcon( tableSortDownIconName ) ;
      tableEntryCommentIcon = new ImageIcon( tableEntryCommentIconName ) ;
      tableEntryValidationInfoIcon = new ImageIcon( validationsIconName ) ;
      fileOpenIcon = new ImageIcon( fileOpenIconName ) ;
      folderOpenIcon = new ImageIcon( folderOpenIconName ) ;
      trashIcon = new ImageIcon( trashIconName ) ;
    }
    catch ( Exception e )
    {
      System.err.println( " Could not find icons " ) ;
    }

    PROJECT_LOAD_SAVE_DIALOG = new TFileChooser( TGlobal.DEFAULT_LOAD_PATH, TGlobal.projects ) ;

    PROJECT_LOAD_SAVE_DIALOG.setSelectedFile( new File( TGlobal.config.getLastProject()) );
  }

  public static ImageIcon getIcon( URL iconName )
  {
    ImageIcon back = null ;
    try
    {
      back = new ImageIcon( iconName ) ;
    }
    catch ( Exception e )
    {
      System.err.println( " Could not find icon " ) ;
    }

    return back ;
  }

  /** set the standard theme */
  public static void loadTheme( DefaultTheme aTheme)
  {
    theme = aTheme ;
  }

  /** update the look and feel */
  public static void updateTheme( JFrame aFrame )
  {
    // java 1.5 has some problems with the DefaultMetalTheme from java 1.4
    // -> disable custom Look&Feel
    if (! TGlobal.runtime.isJava5())
    {
      MetalLookAndFeel.setCurrentTheme( theme ) ;
      try
      {
        UIManager.setLookAndFeel( new MetalLookAndFeel() ) ;
      }
      catch ( UnsupportedLookAndFeelException ex )
      {
        System.out.println( "Cannot set new Theme for Java Look and Feel." ) ;
      }
      SwingUtilities.updateComponentTreeUI( aFrame ) ;
    }
  }
}
