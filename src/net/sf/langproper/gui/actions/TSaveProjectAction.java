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
// function : save the full project
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import net.sf.langproper.gui.GUIGlobals;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import net.sf.langproper.gui.fields.TKeyLock;
import java.awt.event.ActionEvent;
import net.sf.langproper.TGlobal;
import java.io.File;

public class TSaveProjectAction extends TSaveProjectAsAction
{
  public TSaveProjectAction()
  {
    super( "Save Project",
           "Saves all project files.",
           new ImageIcon( GUIGlobals.saveIconName ) ) ;
    putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( "ctrl S" ) ) ;
  }

  public void actionPerformed( ActionEvent e )
  {
    // don't handle the keys in other components
    TKeyLock.keys.lock() ;

    // try to save the save directly or if not file specified, ask the user
    File file = TGlobal.projects.getCurrentProject().getProjectFile() ;
    if (file != null)
    {
      save(file) ;
    }
    else
    {
      saveAs() ;
    }

    // unlock the key's handling of other components
    TKeyLock.keys.unlock() ;
  }

}
