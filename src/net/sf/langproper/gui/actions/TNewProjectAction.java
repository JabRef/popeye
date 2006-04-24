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


// created by : r.nagel 15.09.2005
//
// function : start the new project wizard
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import java.awt.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.wizard.project.* ;
import net.sf.quercus.*;
import java.io.*;

public class TNewProjectAction extends TAbstractGUIAction
{
  public TNewProjectAction()
  {
    super( "New Project",
           "create a new project") ;
//           new ImageIcon( GUIGlobals.openIconName ) ) ;
//    putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( "ctrl O" ) ) ;
  }

  public void actionPerformed( ActionEvent e )
  {
    startWizard( null, null ) ;
  }

  public void startWizard( File file, String projectType )
  {
    NewProjectWizard wzFrame = new NewProjectWizard() ;

    wzFrame.show(projectType);

    if (wzFrame.getResultCode() == TInternalDialog.MR_OKAY)
    {
        TProject project = wzFrame.getProject() ;
        if (project != null)
        {
          TProjectData data = project.getProjectData() ;
          data.reload() ;

          // data not up to date (xml project file not saved)
          project.setUnSaved(true);


          TGlobal.projects.addProject( project ) ;
          TGlobal.projects.setCurrentProject( project );


           // no xml project file
          TGlobal.projects.getCurrentProject().setProjectFile(null);


          GUIGlobals.oPanel.rebuildView() ;

          GUIGlobals.APPLICATION_MAINFRAME.enableButtons() ;

          // delete old project properties dialog
          GUIGlobals.PROJECT_PROPERTIES_DIALOG = null ;

          // delete a old
          GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG = null ;

          // new project path
          GUIGlobals.PROJECT_LOAD_SAVE_DIALOG.setCurrentDirectory(
              project.getProjectFile() );
        }
    }
  }
}
