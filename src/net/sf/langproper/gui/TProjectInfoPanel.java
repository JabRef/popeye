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


// created by : r.nagel 17.01.2006
//
// function : project info panel
//            a report component about all project data, settings and other infos
//
// todo     :
//
// modified :

package net.sf.langproper.gui ;

import java.awt.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;

public class TProjectInfoPanel extends JPanel
{
  private JTextArea area = new JTextArea() ;

  public TProjectInfoPanel()
  {
    setLayout( new BorderLayout() ) ;
    JScrollPane scroller = new JScrollPane(area) ;
    add( scroller, BorderLayout.CENTER) ;

    area.setEditable(false);
    area.setFocusable(false);
  }

  public void updateStatus()
  {
    TProject project = TGlobal.projects.getCurrentProject();
    TProjectSettings projectSettings = project.getSettings();
    TProjectData projectData = project.getProjectData() ;
    TProjectFileList projectFiles = projectData.getAvailableLangs() ;


    area.setText("default-system-encoding: " +Utils.getDefaultHostEncoding() ) ;
    area.append("\nproject-name: " +project.getProjectName());
    area.append("\nproject-path: " +project.getWorkingDirectory() );

    int t = projectFiles.getSize() ;
    area.append("\navailable translation files (visible):" +t
                  +" (" +projectFiles.getVisibleSize() +")" );

    for( int z = 0; z < t ; z++)
    {
      area.append("\nfile "+ z +" :");

      TLanguageFile file = projectFiles.get( z ) ;
      String str = file.getFullLanguageName() ;
      if ((str != null) && (str.length() > 0))
        area.append( file.getFullLanguageName() );
      else
        area.append( "default" );

      area.append("    (" +file.getDefaultEncoding() +")" );
    }

  }
}
