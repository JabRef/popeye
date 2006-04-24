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

// creates and displays the main window - sun style
// on some system configurations no frame pops up (e.g. windows XP, java 1.5)

package net.sf.langproper ;

import java.io.* ;
import java.util.logging.* ;

import javax.swing.* ;

import net.sf.langproper.engine.iso.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.theme.* ;
import net.sf.langproper.charset.*;

public class Starter
{

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI()
  {
//    Logger.global.
    if ( TLanguageNames.runtime.isAvailable() )
    {
      Logger.global.finer( "languages found" ) ;
    }
    else
    {
      Logger.global.warning( "languages NOT found" ) ;
    }

    // load icons, initialize other resources and load theme
    GUIGlobals.init() ;
    GUIGlobals.loadTheme( SimpleTheme.getTheme("Moon") );

    TMainFrame frame = new TMainFrame() ;

    GUIGlobals.APPLICATION_MAINFRAME = frame ;
//    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

    // put theme properties into frame
    GUIGlobals.updateTheme( frame ) ;

    //Display the window.
    frame.pack() ;

    // set size
//    frame.setSize( 500, 400 ) ;
    frame.setSize( TGlobal.config.getDirectInt( "mainWidth", 500 ),
                   TGlobal.config.getDirectInt( "mainHeight", 400 ) ) ;

    // center the frame on screen
    frame.setLocationRelativeTo( null ) ;

    frame.setVisible( true ) ;

    Logger.global.finest( "window visible" ) ;

    // load the last project
    if ( TGlobal.config.getAutomaticLastLoad() )
    {
      String name = TGlobal.config.getLastProject() ;
      boolean load = false ;
      if ( name != null )
      {
        if ( name.length() > 0 )
        {
          frame.loadAction.load( new File( name ));
          load = true ;
        }
      }

      // detect older popeye versions without project handling and generate a
      // project
      if ( ( !load )  && ( TGlobal.config.PREFS_VERSION > TGlobal.config.getVersion()))
      {
        if ( TGlobal.tools.createProjectFromScratch(
              TGlobal.config.getLastFileName() ) )
        {
          JOptionPane.showMessageDialog( null,
                 "New Project generated! Please use the Save Project options.",
                 "automatic project conversion",
                 JOptionPane.INFORMATION_MESSAGE ) ;
        }
      }
    }

  }

  public static void main( String[] args )
  {
//    HtmlEntities ent = new HtmlEntities() ;
//    ent.load();
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater( new Runnable()
    {
      public void run()
      {
        createAndShowGUI() ;
      }
    } ) ;

  }
}
