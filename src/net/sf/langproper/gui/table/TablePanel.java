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


// created by : r.nagel 04.04.2005, based on TableDemo from java.sun.com
//
// function : tablepanel with translations
//
// todo     :
//
// modified : change selection directives into the
//            selectRow() and restoreSelection() methods

package net.sf.langproper.gui.table;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;
import javax.swing.table.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.listener.* ;

public class TablePanel
    extends JPanel implements FocusListener, ViewUpdateListener, KeyListener
{
  private TProjectData myModel ;
  private TDataTable table ;
  private JScrollPane scrollPane ;
  private JPopupMenu entriesPopupMenu = null ;
  private JPopupMenu infoPopupMenu = null ;

  private TableHeaderMenu headerMenu ;

  private TMultiLanguageEntry savedSelectionEntry = null ;

  private TableParameter tableParam ;

  public TablePanel()
  {
    super( new GridLayout( 1, 0 ) ) ;

    headerMenu = new TableHeaderMenu( this ) ;

    myModel = TGlobal.projects.getCurrentData() ;

    tableParam = new TableParameter() ;
    tableParam.setModel( myModel );

    // create table and renderer
    table = new TDataTable( tableParam ) ;

    table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ) ;

    table.setPreferredScrollableViewportSize( new Dimension( 500, 70 ) ) ;

    table.setRowSelectionAllowed( true ) ;

    table.addFocusListener( this ) ;

    table.addMouseListener( new PopupListener() ) ;

    table.addKeyListener(this);

    initView() ;

    if (TGlobal.config.getShowFlagsInTable())
      createButtonRenderer() ;

    // column sorting
    JTableHeader header = table.getTableHeader() ;

    header.addMouseListener( new HeaderListener( header ) ) ;
    header.setReorderingAllowed( false ) ; // no dragging of columns to another place

    //Create the scroll pane and add the table to it.
    scrollPane = new JScrollPane( table ) ;

    //Add the scroll pane to this panel.
    add( scrollPane ) ;

    infoPopupMenu = new JPopupMenu() ;
    infoPopupMenu.add( "Info Menu" ).setEnabled( false ) ;
    infoPopupMenu.addSeparator() ;
    infoPopupMenu.add( "add a comment" ).setEnabled( false ) ;
    infoPopupMenu.add( new ValidationAction() ) ;
  }

  /** insert a flag renderer */
  private void createButtonRenderer()
  {
    TableHeaderRenderer renderer = new TableHeaderRenderer();

    table.getTableHeader().setDefaultRenderer( renderer );
  }

  /** returns the current model */
  public AbstractTableModel getModel()
  {
    return myModel ;
  }

  /** returns the table row for the mouse coordinates Point <p> */
  public int rowAtPoint( Point p )
  {
    return table.rowAtPoint(p) ;
  }

  /** returns the table column for the mouse coordinates Point <p> */
  public int columnAtPoint( Point p )
  {
    return table.columnAtPoint(p) ;
  }

  // --------------------------------------------------------------------------

  /** update all visible parameter (showSource, markIncomplete) */
  public void updateParameter()
  {
    tableParam.updateParameter();
    myModel.fireTableRowsUpdated( 0, myModel.getRowCount() - 1 ) ;
  }

  public void setShowSource( boolean pShowSource )
  {
    tableParam.setShowSource( pShowSource );
    myModel.fireTableRowsUpdated( 0, myModel.getRowCount() - 1 ) ;
  }

  public void setMarkIncomplete( boolean pMarkIncomplete )
  {
    tableParam.setMarkIncomplete( pMarkIncomplete );
    myModel.fireTableRowsUpdated( 0, myModel.getRowCount() - 1 ) ;
  }

  /** mark a cell, only one cell at the same time is supported. To unmark a
   *  cell row = -1 & col = -1
   */
  public void markCell( int row, int column)
  {
     table.setMarkedCell( row, column);
  }

  // --------------------------------------------------------------------------

  public void addListSelectionListener( ListSelectionListener listener )
  {
    //Ask to be notified of selection changes.
    ListSelectionModel rowSM = table.getSelectionModel() ;
    rowSM.addListSelectionListener( listener ) ;
  }

  public void focusGained( FocusEvent e )
  {
//    System.out.println( "get focus" ) ;
  }

  public void focusLost( FocusEvent e )
  {
//    System.out.println( "lost focus" ) ;
  }

  // ---------------------------------------------------------------------------
  // ---------------  selection ------------------------------------------------
  // ---------------------------------------------------------------------------

  public void saveSelection()
  {
    ListSelectionModel lsm = table.getSelectionModel() ;
    if ( lsm.isSelectionEmpty() )
    {
      //no rows are selected
      savedSelectionEntry = null ;
    }
    else
    {
      int selectedRow = lsm.getMinSelectionIndex() ;
      savedSelectionEntry = ( TMultiLanguageEntry ) myModel.getValueAt(
          selectedRow, 0 ) ;
    }
  }

  public void restoreSelection( boolean makeSelectionVisible )
  {
    if ( savedSelectionEntry != null )
    {
      ListSelectionModel lsm = table.getSelectionModel() ;
      int index = lsm.getMinSelectionIndex() ;

      int old = myModel.getLanguageEntryIndex( savedSelectionEntry.getKey() ) ;

      if ( ( index != old ) && ( old > -1 ) )
      {
        table.changeSelection( old, 0, false, false ) ;
      }
    }
  }

  public void setSelectedIndex( int index )
  {
    table.setRowSelectionInterval( index, index ) ;
  }

  /** select the entry */
  public void setSelectedEntry( TMultiLanguageEntry entry )
  {
    int index = myModel.getLanguageEntryIndex( entry.getKey() ) ;
    if (index > -1)
    {
      table.changeSelection( index, 0, false, false ) ;
    }
  }

  /** select a table row */
  public void selectRow( int row )
  {
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
  }

  /** returns the index of the selected entry */
  public int getSelectedIndex()
  {
    int back = -1 ;
    ListSelectionModel lsm = table.getSelectionModel() ;
    if ( !lsm.isSelectionEmpty() )
    {
      back = lsm.getMinSelectionIndex() ;
    }

    return back ;
  }

  /** returns the current selected entry */
  public TMultiLanguageEntry getSelectedEntry()
  {
    int index = getSelectedIndex() ;
    if (index != -1)  // selection available
    {
       return (TMultiLanguageEntry) myModel.getValueAt(index, 0) ;
    }

    return null ;
  }

  /** returns the entry at row <index> */
  public TMultiLanguageEntry getEntryAtRow(int index)
  {
    if ( (index >= 0) && (index < myModel.getRowCount()))
    {
      return (TMultiLanguageEntry) myModel.getValueAt(index, 0) ;
    }
    return null ;
  }

  /** select the next row */
  public void selectNextRow()
  {
    int row = table.getSelectedRow() + 1 ;
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
    else
    {
      row = table.getSelectedRow() ;
      table.clearSelection();
      table.changeSelection( row, 0, false, false ) ;
    }
  }

  public void selectPrevRow()
  {
    int row = table.getSelectedRow() - 1 ;
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
    else
    {
    }
  }

  // ----------------------------------------------------------------------------

  /** a popup for the entries */
  public void setPopupMenu( JPopupMenu menu )
  {
    entriesPopupMenu = menu ;
  }

  /** initialized the table view (e.g. column size) */
  public void initView()
  {
    for (int t = 0, len = TProjectData.KEY_COLUMN ; t < len ; t++)
    {
      TableColumn column = table.getColumnModel().getColumn( t ) ;
      column.setMaxWidth( 20 ) ;
      column.setMinWidth( 16 ) ;
      column.setWidth( 16 ) ;
      column.setResizable( false ) ;
    }
  }

  public void updateModel()
  {
    myModel = TGlobal.projects.getCurrentData() ;
    if (myModel != null)
    {
      table.setModel( myModel ) ;
      // tableParam.setModel() was performed in table.setModel()
    }
  }

  // --------------------------------------------------------------------------
  // handling of table column sorting
  // taken from Java 2 Platform Std.Ed.v1.4.2 API - EventListenerList

  public void addSortingListener( SortingListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.add( SortingListener.class, listener ) ;
    }
  }

  public void removeActionListener( ActionListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.remove( ActionListener.class, listener ) ;
    }
  }

  /** resort the table data */
  public void performResort()
  {
    // save the current selction, because the sorting method will remove it
    saveSelection() ;
    myModel.resortEntries() ;
    fireReSortPerformed() ;
    restoreSelection( true ) ;
  }

  protected void fireReSortPerformed()
  {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList() ;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2 ; i >= 0 ; i -= 2 )
    {
      if ( listeners[i] == SortingListener.class )
      {
        ( ( SortingListener ) listeners[i + 1] ).resortPerformed() ;
      }
    }
  }

  /** repaint the table data */
  protected void updateView()
  {
     myModel.fireUpdateData() ;
  }


  // --------------------------------------------------------------------------
  // ViewUpdateListener
  // --------------------------------------------------------------------------

  /** jump to the next element */
  public void showNextEntry()
  {
    // select the next row
    int row = table.getSelectedRow() + 1 ;
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
  }

  /** jump to the previous element */
  public void showPrevEntry()
  {
    // select the prev row
    int row = table.getSelectedRow() - 1 ;
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
  }

  /** jump to the next incomplete element */
  public void showNextIncomplete(int id, boolean bottomUp)
  {
    int row = myModel.getNextIncompleteIndex( table.getSelectedRow(), id, bottomUp) ;
    if ( ( row > -1 ) && ( row < myModel.getRowCount() ) )
    {
      table.changeSelection( row, 0, false, false ) ;
    }
  }

  // --------------------------------------------------------------------------
  // Key Listener -------------------------------------------------------------
  // --------------------------------------------------------------------------
  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
    if (e.getKeyCode() == KeyEvent.VK_DELETE)
    {
      myModel.removeTranslation( getSelectedIndex() );
      selectNextRow() ;
    }
  }

  public void keyTyped( KeyEvent e )
  {
  }

  // ---------------------------------------------------------------------------
  // Header handling

  // header listener - sorting
  public class HeaderListener
      extends MouseAdapter
  {
    private JTableHeader header ;

    HeaderListener( JTableHeader header )
    {
      this.header = header ;
    }

    public void mousePressed( MouseEvent e )
    {
      // if a popup is open -> don't process this sequence
      // it doesn't work since 1.5 (?)
      if ( headerMenu.isVisible() )
      {
        return ;
      }

      // get the header column
      Point p = e.getPoint() ;
      int colIndex = header.columnAtPoint( p ) ;

      // PopupMenu
      if ( !showHeaderMenu( colIndex, e ) ) // PopupTrigger
      {

        if ( e.getButton() == MouseEvent.BUTTON1 ) // Left MouseKey
        {
          /*
           * If the user adjusts the header size the sort event is
           * always triggered.
           * To avoid this behaviour we check if the mouse is
           * inside the label's bounds and has a certain distance (offset)
           * to the label border.
           *
           * Sascha Hunold <hunoldinho@users.sourceforge.net>
           */
          if ( colIndex >= 0 )
          {
            final int initoffset = 3 ;
            int xoffset = initoffset ;
            for ( int i = 0 ; i < colIndex ; i++ )
            {
              xoffset += table.getColumnModel().getColumn( i ).getWidth() ;
            }
            TableColumn column = table.getColumnModel().getColumn( colIndex ) ;
            int cw = column.getWidth() ;
            int ch = header.getHeight() ;

            Rectangle r = new Rectangle() ;

            r.setBounds( xoffset, 0 /*offset*/, cw - 2 * initoffset,
                         ch
                /*-2*offset*/) ;

            if ( !r.contains( p ) )
            {
              return ;
            }
          }

          if ( colIndex == TProjectData.KEY_COLUMN )  // key column
          {
            GUIGlobals.sortDirection.getNext() ; // switch to the next sorting module
            performResort() ; // resort
            header.repaint() ;
          }
        }
      }
    }

    public void mouseReleased( MouseEvent e )
    {
      // get the header column
      Point p = e.getPoint() ;
      int colIndex = header.columnAtPoint( p ) ;

      showHeaderMenu( colIndex, e ) ;
    }

    // returns true is popup can displayed
    private boolean showHeaderMenu( int colIndex, MouseEvent e )
    {
      if ( e.isPopupTrigger() )
      {
        headerMenu.perform( colIndex, e ) ;

        return true ;
      }

      return false ;
    }
  }

  //---------------------------------------------------------------
  // table right click popup menu
  class PopupListener
      extends MouseAdapter
  {
    public void mousePressed( MouseEvent e )
    {
      maybeShowPopup( e ) ;
    }

    public void mouseReleased( MouseEvent e )
    {
      maybeShowPopup( e ) ;
    }

    private void maybeShowPopup( MouseEvent e )
    {
      if ( e.isPopupTrigger() )
      {
//        System.out.println( "column " +table.getColumnModel().getColumnIndexAtX(e.getX())) ;
        int col = table.getColumnModel().getColumnIndexAtX( e.getX() ) ;
        if ( col == 0 )
        {
          infoPopupMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
        }
        else if (col >= TProjectData.KEY_COLUMN)
        {
          entriesPopupMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
        }
      }
    }
  }

  // --------------------------------------------------------------------------

  // --------------------------------------------------------------------------
  // Table Renderer
  // handling of even/odd lines (colors) and printing the right content
  // --------------------------------------------------------------------------

  public class TableHeaderRenderer
      extends JLabel implements TableCellRenderer
  {

    public TableHeaderRenderer()
    {
      if ( GUIGlobals.theme != null)
        this.setFont( GUIGlobals.theme.getTableHeaderFont() );

//      this.setComponentOrientation( );
      this.setHorizontalAlignment( SwingConstants.LEFT);
      this.setBorder( BorderFactory.createEtchedBorder());
    }

    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column )
    {
      Icon icon = null ;
      if (column > 1)
      {
        TLanguageFile dummy = myModel.getColumnLanguage(column) ;
        if (dummy != null)
        {
          icon = dummy.getFLag() ;
        }
      }
      setIcon( icon ) ;

      String str = value.toString() ;
      if (str.length() < 1)
        this.setText(" ");
      else
        this.setText( value.toString());
      return this ;
    }

  }

}
