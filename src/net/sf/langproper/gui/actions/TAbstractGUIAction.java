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
// function : base class for all actions
//
// todo     :
//
// modified :

package net.sf.langproper.gui.actions ;

import net.sf.langproper.gui.table.TablePanel;
import javax.swing.*;

public abstract class TAbstractGUIAction extends AbstractAction
{
  private TablePanel table ;
  private ToolBarAction toolAction ;

  public TAbstractGUIAction( String label )
  {
    super( label ) ;
    toolAction = new ToolBarAction(null, this) ;
  }

  public TAbstractGUIAction( String label, String toolTip )
  {
    this(label, toolTip, null) ;
  }


  public TAbstractGUIAction( String label, Icon icon )
  {
    this(label, "", icon) ;
  }

  public TAbstractGUIAction( String label, String toolTip, Icon icon )
  {
    super( label ) ;
    toolAction = new ToolBarAction(icon, this) ;
//    toolAction.putValue( Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
  }


  // --------------------------------------------------------------------------
  public TablePanel getTable()
  {
    return table;
  }

  public void setTable(TablePanel pTable)
  {
    this.table = pTable;
  }

  // returns a cloned Action, without the name propertie (for toolbar buttons)
  public AbstractAction getToolBarAction()
  {
    return toolAction ;
  }

  // --------------------------------------------------------------------------
  public void setEnabled(boolean newValue)
  {
    super.setEnabled( newValue );
    toolAction.setEnabled( newValue );
  }

  // --------------------------------------------------------------------------
  public void putValue(String key, Object value)
  {
    // take only the description into the toolbar button - why ?
//    if ( (key == Action.LONG_DESCRIPTION) || (key == Action.SHORT_DESCRIPTION))
//    {
//      toolAction.putValue(key, value);
//    }

     // the "normal" action shout not have an icon
    if (key == Action.SMALL_ICON)
    {
      toolAction.putValue(key, value);
    }
    else if (key == Action.NAME)
    {
      super.putValue(key, value);
    }
    else // all other properties
    {
      super.putValue(key, value);
      toolAction.putValue(key, value);
    }
  }


}
