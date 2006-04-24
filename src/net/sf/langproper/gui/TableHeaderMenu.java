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

// created by : r.nagel 12.07.2005
//
// function : a menu for the table headers
//
// todo     :
//
//
// modified :

package net.sf.langproper.gui ;

import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.engine.sort.* ;
import net.sf.langproper.gui.table.*;

public class TableHeaderMenu implements ActionListener
{
  private JPopupMenu keyMenu = new JPopupMenu("key") ;
  private ButtonGroup sortGroup = new ButtonGroup();

  private JPopupMenu langMenu = new JPopupMenu("language") ;

  private ButtonModel sortButtonModels[] ;

  private TablePanel table ;

  private transient int columnBuf ;

  public TableHeaderMenu(TablePanel pTable)
  {
    table = pTable ;

    JMenuItem item = new JMenuItem("key header menu") ;
    item.setEnabled(false);

    keyMenu.add(item) ;
    keyMenu.addSeparator();

    JRadioButtonMenuItem sortItem ;

    SortEngine sortModules = GUIGlobals.sortDirection ;

    int len = sortModules.getSize() ;

    sortButtonModels = new ButtonModel[len] ;

    for(int t = 0 ; t < len ; t++)
    {
      TSorter sorter = sortModules.get(t) ;
      sortItem = new JRadioButtonMenuItem( sorter.getDescription() ) ;
      // actionCommand = sort + index => rebuild the sender number
      sortItem.setActionCommand("sort"+t);
      sortItem.addActionListener( this );
      sortItem.setToolTipText( sorter.getToolTip() );
      sortGroup.add( sortItem );
      keyMenu.add( sortItem) ;
      sortButtonModels[t] = sortItem.getModel() ; // register the model
    }


    item = new JMenuItem("header menu") ;
    item.setEnabled(false);

    langMenu.add(item) ;
    langMenu.addSeparator();

    item = new JMenuItem("make invisible") ;
    item.setActionCommand("visible");
    item.addActionListener(this);
    langMenu.add(item) ;

  }

  /** activate the menu */
  public void perform(int column, MouseEvent e)
  {
      // buffer the column, used by actionPerformed(..)
      columnBuf = column ;

      if (column == TProjectData.KEY_COLUMN)
      {
        // select the active sorting module
        int id = GUIGlobals.sortDirection.getActiveID() ;
        sortGroup.setSelected( sortButtonModels[id], true ) ;

        keyMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
      }
      else if (column > TProjectData.KEY_COLUMN)
      {
        langMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
      }
  }

  public void actionPerformed( ActionEvent e )
  {
    String com = e.getActionCommand() ;
    String sCom = com.substring(0,4) ;

    // the actioncommand contains the command "sort" and the id of the sender button
    if (sCom.hashCode() == "sort".hashCode())
    {
      int index = Integer.parseInt(com.substring(4)) ;
      GUIGlobals.sortDirection.setActiveID(index) ;
      table.performResort() ;
    }
    else if (com.hashCode() == "visible".hashCode())
    {
      TLanguageFile dummy = TGlobal.projects.getCurrentData().getColumnLanguage(columnBuf) ;

      if (dummy != null) // valid data ?
      {
        dummy.setVisible(false); // make invisible
        GUIGlobals.oPanel.updateDataVisibilty(); // update table
      }
//      System.out.println( TGlobal.current.getColumnLanguage(columnBuf)  ) ;
    }

    columnBuf = -1 ;
  }

  /** returns true, if any menu is visible */
  public boolean isVisible()
  {
    return (keyMenu.isVisible() || langMenu.isVisible()) ;
  }
}
