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

// created by : r.nagel 31.05.2005
//
// function : save only selected languages
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import java.awt.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.* ;
import net.sf.quercus.*;

public class TSaveSingleFileAction extends TAbstractGUIAction
{
  public TSaveSingleFileAction()
  {
    super("Save single...",
          "select a language and save it separately") ;
  }

  public void actionPerformed( ActionEvent e )
  {
    TProjectData current = TGlobal.projects.getCurrentData() ;
     // if any data available
    if (current != null)
    {
      if ( GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG == null )
        GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG = new TLangSelectionDialog() ;

      if ( GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG.showModal() == TInternalDialog.MR_OKAY )
      {

        int sel[] = GUIGlobals.PROJECT_SAVE_SINGLE_DIALOG.getSelectedIndices() ;
        if (sel != null)
        {
          if (sel.length > 0)
          {
            for (int t = 0 ; t < sel.length ; t++)
            {
              int id = sel[t] ;

              // calculate the langID (language ID)
              if (current.hasDefaultEntry())
              {
                if (id == 0)
                  id = TLanguageList.DEFAULT_LANG ;
                else
                  id-- ;
              }
//              System.out.println("langID " +id  +" | "
//                                 +TGlobal.current.getAvailableLangs().get(id).getFullLanguageName() ) ;
              TLanguageFile vFile = current.getAvailableLangs().get(id) ;
              current.saveSingleFile(vFile, id);
            }
          }
        }
      }
    }
  }
}

