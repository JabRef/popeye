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

// ------------- UNDER CONSTRUCTION ----------------------------------------

// created by : r.nagel 02.05.2005
//              based on TableSorterDemo.java from java.sun.com
//
// function : central managing dialog for all project languages
//            (add & remove single languages from project)
//
// todo     :  remove
//
// modified :

package net.sf.langproper.gui.project ;


import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.quercus.* ;
import net.sf.langproper.gui.*;

public class ProjectSettingsDialog extends TInternalDialog
                                   implements ActionListener, LanguageUpdateListener
{
  private JButton okButton ;

  private SettingsPanel settingsPanel ;
  private LanguageManagerPanel langPanel ;

  public ProjectSettingsDialog()
  {
    super("project properties") ;

    // control panel ----------------------------------------------------------
    JPanel buttonPanel = new JPanel() ;
    okButton = new JButton("close") ;
    okButton.addActionListener(this);
    buttonPanel.add(okButton) ;


    //Add all panes to this panel. --------------------------------------
    Container content = this.getContentPane() ;
    content.setLayout( new BorderLayout() );

    settingsPanel = new SettingsPanel();

    langPanel = new LanguageManagerPanel(TGlobal.projects.getCurrentProject(), this, false );
    langPanel.addLanguageUpdateListener( this );

    JTabbedPane tabbed = new JTabbedPane() ;
    tabbed.add("languages", langPanel ) ;
    tabbed.add("settings", settingsPanel ) ;

    content.add(tabbed, BorderLayout.CENTER ) ;
    content.add( buttonPanel, BorderLayout.SOUTH) ;

    this.pack();
  }

  /** initalized the dialog and allows the "recycle" use, see showModal method*/
  protected void init()
  {
    super.init();
    TGlobal.projects.getLanguages().resetDataFlags();
    langPanel.setProject( TGlobal.projects.getCurrentProject() );
    settingsPanel.loadSettings( TGlobal.projects.getCurrentProject() );
  }

  protected void close()
  {
    super.close() ;
    settingsPanel.saveSettings( TGlobal.projects.getCurrentProject() );
  }

  // --------------------------------------------------------------------------
  // ActionListener
  // --------------------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if ( sender == okButton)
    {
       this.setModalResult( TInternalDialog.MR_OKAY) ;
    }
  }

  // --------------------------------------------------------------------------
  // LanguageUpdateListener
  // --------------------------------------------------------------------------
  public void updateStructure()
  {
    // update the language panel into the main window
    GUIGlobals.oPanel.updateStructure() ;
  }
}
