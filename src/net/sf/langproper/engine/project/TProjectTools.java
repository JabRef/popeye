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


// created by : r.nagel 06.09.2005
//
// function : same useful project functions
//
// todo     :
//
// modified :

package net.sf.langproper.engine.project ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import java.io.*;
import javax.swing.*;

public class TProjectTools
{
  // convert files into a project
  public boolean createProjectFromScratch( String filename )
  {
    boolean back = false ;

    TProject project = TGlobal.projects.getCurrentProject() ;

    // read the default settings
    project.getSettings().readDefaults() ;

    File file = new File( filename ) ;
    if ( TGlobal.projects.getCurrentData().collect( file ) )
    {
      TProjectData pData = project.getProjectData() ;
      pData.reload() ;

      // true => files loaded
      TGlobal.config.setLastFile( file ) ;

      GUIGlobals.oPanel.rebuildView() ;

      GUIGlobals.APPLICATION_MAINFRAME.enableButtons() ;

      // delete old project properties dialog
      GUIGlobals.PROJECT_PROPERTIES_DIALOG = null ;

      // delete a old
      GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG = null ;

      // set a project filename
      project.setProjectName(
          createProjectNameProposal(project.getProjectData().getAvailableLangs()) );

      // no xml project file
      project.setProjectFile( null ) ;

      // data not up to date (xml project file not saved)
      project.setUnSaved( true ) ;

      back = true ;
    }
    else
    {
      JOptionPane.showMessageDialog( null,
                                     "Sorry, could not load any file!",
                                     "error loading files",
                                     JOptionPane.ERROR_MESSAGE ) ;
    }

    return back ;
  }

  /** prepare a filename advice for project configuration file (xxx.xml) */
  public File createProjectFilename( TProject project )
  {
    File file = project.getProjectFile() ;
    if ( file == null )
    {
      TProjectFileList fList = project.getProjectData().getAvailableLangs() ;
      if ( fList.size() > 0 )
      {
        TLanguageFile lFile = fList.get( 0 ) ;
        if ( lFile != null )
        {
          File parent = lFile.getFileHandle().getParentFile() ;
          String str = project.getProjectName() ;
          if ( str == null )
          {
            str = lFile.getBaseName() ;
          }
          else if ( str.length() < 0 )
          {
            str = lFile.getBaseName() ;
          }

          file = new File( parent.getAbsolutePath(),
                           str + "." + TGlobal.PROJECT_FILE_EXTENSION ) ;
        }
      }
    }
    return file ;
  }

  /** prepare a projectname advice for the project, based on the filename */
  public static String createProjectNameProposal( String oldName, File file,
                                                  String projectType )
  {
    String back = oldName ;
    // it's really expensive stuff ....
    if ( ( file != null ) && ( projectType != null ) )
    {
      TProject project = TGlobal.projects.getEmptyProject( projectType, true ) ;
      if ( project != null )
      {
        TProjectFileList fileList = project.getProjectData().getAvailableLangs() ;
        TLanguageFile refFile = fileList.createFileHandle( file ) ;
        back = refFile.getBaseName() ;
      }
    }

    return back ;
  }

  /** Prepare a projectname advice for the project,
   *  based of the filename from the fileList parameter. Normally these
   *  files have a "name_language_country_variant.xxx" structure or
   *  a basename property. The methode extracts the basename as project name proposal */
  public static String createProjectNameProposal( TProjectFileList fileList  )
  {
    String baseName = null ;
    if (fileList != null)
    {
      TLanguageFile refFile = fileList.getData(0) ;
      if (refFile != null)
      {
        baseName = refFile.getBaseName() ;
      }
    }

    return baseName ;
  }
}
