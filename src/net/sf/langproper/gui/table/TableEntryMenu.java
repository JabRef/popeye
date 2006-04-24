/**
 Popeye - Java (Language) Properties File Editor

 Copyright (C) 2006 Raik Nagel <kiar@users.sourceforge.net>
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


// created by : r.nagel 22.04.2006
//
// function : a customized popupmenu for the translation table
//
// todo     :
//
// modified :


package net.sf.langproper.gui.table ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.gui.actions.* ;
import net.sf.quercus.* ;
import net.sf.langproper.engine.*;
import net.sf.langproper.gui.*;
import javax.swing.event.*;

public class TableEntryMenu extends JPopupMenu implements PopupMenuListener
{
  private TablePanel table ;
  private TEntryHandle rowCol ;

  private SpecialCopyAction scAction = new SpecialCopyAction();

  public TableEntryMenu(TablePanel propTable)
  {
    table = propTable ;
    rowCol = new TEntryHandle(table) ;

    add("Entries Menu").setEnabled(false);
    addSeparator();


    TNewEntryAction newAction = TNewEntryAction.getInstance() ;
    newAction.setTable( propTable );
    add( newAction ) ;

    JMenu menu = new JMenu("Copy") ;
    add( menu ) ;

    menu.add( new CopyAction() ) ;
    menu.add( scAction ) ;

    TRemoveEntryAction delAction = TRemoveEntryAction.getInstance() ;
    delAction.setTable( propTable );
    add( delAction ) ;

    TShredEntryAction shredAction = TShredEntryAction.getInstance() ;
    shredAction.setTable( propTable );
    add( shredAction ) ;

    addPopupMenuListener( this ) ;
  }


  public void show( Component invoker, int x, int y)
  {
    rowCol.update(x, y);

    if (rowCol.getRow() != table.getSelectedIndex())
    {
      scAction.setEnabled(true);
    }
    else
    {
      scAction.setEnabled(false);
    }

    table.markCell(rowCol.getRow(), rowCol.getColumn() );
    super.show( invoker, x, y);
  }

  // --------------------------------------------------------------------------
  // PopupMenuListener
  // --------------------------------------------------------------------------
  public void popupMenuCanceled( PopupMenuEvent e )
  {
  }

  public void popupMenuWillBecomeInvisible( PopupMenuEvent e )
  {
    table.markCell(-1, -1);
  }

  public void popupMenuWillBecomeVisible( PopupMenuEvent e )
  {
  }

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  /** fill the empty fields of the current selected entry with the field content
  /** fill the empty fields of the current selected entry with the field content
   *  of the clicked entry
   */
  private class SpecialCopyAction extends AbstractAction
  {
    public SpecialCopyAction()
    {
      super( "fill empty fields of the selected entry" ) ;
    }

    public void actionPerformed( ActionEvent e )
    {
      TMultiLanguageEntry sel = table.getSelectedEntry() ;
      if (sel != null)
      {
        // try to import the data into the selected entry
        if ( sel.importTranslations( rowCol.getRowEntry() ))
        {
          // something has changed -> repaint and update the data/all components
          GUIGlobals.oPanel.updateView() ;
        }
      }
    }
  }

  /** copy the content of the cell into the clipboard */
  private class CopyAction extends AbstractAction
  {
    public CopyAction()
    {
      super( "Copy cell content" ) ;
    }

    public void actionPerformed( ActionEvent e )
    {
      String str = rowCol.getCellContent() ;
//      System.out.println( str ) ;
      if ( (str != null) && (str.length() > 0))
      {
        ClipBoardManager.clipBoard.setClipboardContents(str) ;
      }
    }
  }

}
