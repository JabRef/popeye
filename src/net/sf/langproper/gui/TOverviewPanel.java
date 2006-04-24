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

// created by : r.nagel 4.4.2005
//
// function : contains the JTable (all available entries) and the
//            JTextFieldPanel (all translations for one entry)
//
// todo     :
//
//
// modified :


package net.sf.langproper.gui ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.gui.listener.* ;
import net.sf.langproper.gui.table.* ;

public class TOverviewPanel
    extends JPanel
    implements ComponentListener, SortingListener, TableModelListener
{
  private BorderLayout borderLayout1 = new BorderLayout() ;
  public TablePanel propTable = new TablePanel();
  private JSplitPane splitter = new JSplitPane( JSplitPane.VERTICAL_SPLIT) ;

  private TableEntryMenu tableMenu ;
  private TTabbedFields tabbed = new TTabbedFields() ;

  private TStatusLine statusLine = new TStatusLine();

  public TOverviewPanel()
  {
    this.setLayout( borderLayout1 ) ;

    this.addComponentListener( this );

    // connect the table and textfield area
    propTable.addListSelectionListener( tabbed );

    propTable.addSortingListener( this );
    TGlobal.projects.getCurrentData().addTableModelListener(this);

    splitter.add( propTable, JSplitPane.TOP) ;
    splitter.add( tabbed, JSplitPane.BOTTOM) ;
    splitter.setOneTouchExpandable(true) ;

    statusLine.updateSortingDirectionLabel();

    this.add( splitter, BorderLayout.CENTER) ;

    this.add( statusLine, BorderLayout.SOUTH) ;

    // popup menu for table
    tableMenu = new TableEntryMenu( propTable ) ;
    propTable.setPopupMenu( tableMenu );

    // insert a listener for the "select next or prev item" action
    tabbed.addViewUpdateListener( propTable );
  }

  /** update the table view, some columns could be changed */
  public void updateStructure()
  {
    propTable.initView();
    tabbed.updateStructure();
    updateEntryCounter();
    statusLine.updateSortingDirectionLabel();
    resizeIT() ;
  }

  /** some visibility options (marking of inclompete lines) has been changed */
  public void updateView()
  {
    propTable.saveSelection();
     TGlobal.projects.updateCurrentData();
    propTable.restoreSelection(true);
  }

  /** some languages became visible/invisible */
  public void updateDataVisibilty()
  {
    TGlobal.projects.updateCurrentStructure(true);
    updateStructure();
  }

  /** update some visibility parameters of the translation-table */
  public void updateTabelParameter()
  {
    propTable.updateParameter();
  }


  /** initialize the view, selects the first item and updates the data models */
  public void rebuildView()
  {
    propTable.updateModel();
    updateStructure() ;
    propTable.selectRow(0);
  }

  /** selects the row with index <index> and grab the focus into the textfields*/
  public void selectAndGrabIndex(int index)
  {
      propTable.selectRow(index);
      tabbed.requestFocus();
  }

  /** selects the key into the table */
  public void setSelectedEntry(TMultiLanguageEntry entry)
  {
    if (entry != null)
    {
//      propTable.selectRow() ;
      propTable.setSelectedEntry( entry ) ;
//      tabbed.requestFocus() ;
    }
  }


  /** selects the language with id=index in the editor area and mark the text */
  public void selectAndMark(int index, int offset, int end)
  {
      propTable.selectRow(index);
      tabbed.selectKeyText(offset, end);
  }

  /** update the comment into the statusline */
  public void updateEntryComment(String comment)
  {
    statusLine.setComment( comment );
  }


  /** Show entry source or real text.
   *  If true, the table shows the "native" data. Otherwise the utf encoding is
   *  enabled */
  public void setShowSource( boolean pShowSource )
  {
    propTable.setShowSource( pShowSource);
    tabbed.setShowSource( pShowSource);
  }

  /** inclompete table entries should be marked */
  public void setMarkIncomplete(boolean pMarkIncomplete)
  {
    propTable.setMarkIncomplete( pMarkIncomplete );
  }

  private void updateEntryCounter()
  {
    statusLine.setNumberOfEntries( TGlobal.projects.getCurrentData().getRowCount() );
  }

  private void resizeIT()
  {
    Dimension thSize = splitter.getSize() ;

    int dummy = tabbed.getRealHeigh() + tabbed.getInsets().bottom +
                 tabbed.getInsets().top ;

    int loc = splitter.getSize().height - splitter.getInsets().bottom
        - splitter.getDividerSize() - tabbed.getInsets().bottom
        - dummy ;

    // textfields panel to small
    if (dummy > tabbed.getSize().getHeight())
    {
      // only 50% of heigth allowed
      if (dummy < thSize.getHeight()/2)
      {
        splitter.setDividerLocation( loc ) ;
      }
    }
    else
    {
      splitter.setDividerLocation( loc ) ;
    }
  }

  // --------------------------------------------------------------------------
  // ComponentListener
  public void componentHidden( ComponentEvent e )
  {
  }

  public void componentMoved( ComponentEvent e )
  {
  }

  public void componentResized( ComponentEvent e )
  {
    resizeIT() ;
  }

  public void componentShown( ComponentEvent e )
  {
  }

  // --------------------------------------------------------------------------
  // SortingListener
  public void resortPerformed()
  {
    statusLine.updateSortingDirectionLabel();
  }

  // --------------------------------------------------------------------------
  // TableModelListener
  public void tableChanged( TableModelEvent e )
  {
    if ((e.getType() == TableModelEvent.DELETE) ||
        (e.getType() == TableModelEvent.INSERT))
    {
      this.updateEntryCounter();
    }
  }

}
