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
// function : table selection for all languages
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import java.awt.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.project.* ;
import net.sf.quercus.*;

public class TProjectPropertiesAction extends TAbstractGUIAction
{
  public TProjectPropertiesAction()
  {
    super("Project Properties ...", "set the project properties") ;
  }

  public void actionPerformed( ActionEvent e )
  {
     // if any data available
    if (TGlobal.projects.getCurrentData() != null)
    {
      if ( GUIGlobals.PROJECT_PROPERTIES_DIALOG == null )
        GUIGlobals.PROJECT_PROPERTIES_DIALOG = new ProjectSettingsDialog() ;

      if ( GUIGlobals.PROJECT_PROPERTIES_DIALOG.showModal() == TInternalDialog.MR_OKAY )
      {
        if (TGlobal.projects.getLanguages().hasDataVisibiltyChanged())
        {
          GUIGlobals.oPanel.updateDataVisibilty();
        }
      }
    }
  }
}
