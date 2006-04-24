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

package net.sf.langproper.gui.actions ;

import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.fields.* ;
import net.sf.langproper.engine.project.*;

public class TNewEntryAction
    extends TAbstractGUIAction
{
  private static TNewEntryAction myInstance = new TNewEntryAction() ;

  private int counter = 1 ;

  public static TNewEntryAction getInstance()
  {
    return myInstance ;
  }

  public TNewEntryAction()
  {
    super( "New Entry" ) ;
    putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( "ctrl N" ) ) ;
  }

  public void actionPerformed( ActionEvent e )
  {
    // don't handle the keys in other components
    TKeyLock.keys.lock() ;

    TProject project = TGlobal.projects.getCurrentProject() ;
    TProjectData current = project.getProjectData() ;

    // project instance available
    if ( current != null )
    {
      if ( current.hasData() )
      {

        String keyValue = JOptionPane.showInputDialog( "Please input the key",
                                                       "NewEntry" +counter ) ;

        // null meaning the user canceled the input
        if ( keyValue == null )
        {
          return ;
        }

        // key invalid
        if ( keyValue.length() < 1 )
        {
          return ;
        }

        keyValue = Utils.normalizeIT(keyValue,
                                     project.getSettings().getReplaceWhitespace(),
                                     project.getSettings().getReplaceString() ) ;

        TMultiLanguageEntry entry = current.getLanguageEntry( keyValue ) ;

        //  no entry
        if ( entry == null )
        {
          entry = current.addLanguageEntry( keyValue ) ;
          counter++ ;
        }
        else // entry allready defined
        {
          JOptionPane.showMessageDialog( null,
                                         "key already defined, action aborted!",
                                         "warning",
                                         JOptionPane.WARNING_MESSAGE ) ;
        }

        // select the entry
        if ( entry != null )
        {
          int index = current.getLanguageEntryIndex( entry.getKey() ) ;
          //table.selectRow( index, true ) ;
          GUIGlobals.oPanel.selectAndGrabIndex(index);
        }
      }
    }
    // unlock the key's handling of other components
    TKeyLock.keys.unlock() ;
  }

}
