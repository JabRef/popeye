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

// created by : r.nagel 02.05.2005
//
// function : load project
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import java.io.* ;

import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.fields.* ;
import net.sf.langproper.engine.project.*;
import net.sf.langproper.gui.filechooser.*;

/** @depracted */
public class TImportFilesAction
    extends TAbstractGUIAction
{
  private JFileChooser openDialog ;

  public TImportFilesAction()
  {
//      super("Load") ;
    super( "import",
           "merge with another project",
           new ImageIcon( GUIGlobals.openIconName ) ) ;
    putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( "ctrl O" ) ) ;

    openDialog = new JFileChooser( TGlobal.DEFAULT_LOAD_PATH ) ;

    ProjectFileFilter filter = new ProjectFileFilter() ;
    filter.addExtension( "properties" ) ;
    filter.setDescription( "java properties file" ) ;
    openDialog.setFileFilter( filter ) ;

//    openDialog.setSelectedFile( new File( TGlobal.config.getLastFileName()) );
  }

  public void actionPerformed( ActionEvent e )
  {
    // don't handle the keys in other components
    TKeyLock.keys.lock() ;

    int result ;
    if ( TGlobal.projects.isProjectChanged() )

    {
      result = JOptionPane.showConfirmDialog( null,
                                              "Data changed - load new data ?",
                                              "load Question/Warning",
                                              JOptionPane.YES_NO_OPTION ) ;

    }
    else
    {
      result = JOptionPane.YES_OPTION ;
    }

    if ( result == JOptionPane.YES_OPTION )
    {
      int returnVal = openDialog.showOpenDialog( null ) ;
      if ( returnVal == JFileChooser.APPROVE_OPTION )
      {
        load( openDialog.getSelectedFile() ) ;
      }
    }
    // unlock the key's handling of other components
    TKeyLock.keys.unlock() ;
  }


  public void load(File file)
  {
    
  }
}
