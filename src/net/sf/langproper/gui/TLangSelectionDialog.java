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
// function : dialog for selecting languages
//
// todo     : make the encoding and save mode chooseable
//
// modified :   22.03.2006, kiar
//              - insert a ListSelection Model


package net.sf.langproper.gui ;

import javax.swing.* ;
import java.awt.* ;
import net.sf.langproper.* ;
import javax.swing.event.* ;
import java.awt.event.* ;
import net.sf.langproper.engine.*;
import net.sf.langproper.engine.project.TLanguageFile;
import net.sf.quercus.*;
import net.sf.quercus.helpsystem.TInfoText;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.util.*;

public class TLangSelectionDialog
    extends TInternalDialog implements ListSelectionListener, ActionListener
{
  private JList list ;
  private TListModel model ;
  private JButton saveButton ;
  private JButton cancelButton ;

  public TLangSelectionDialog()
  {
    super( "available language selection" ) ;
    Container content = this.getContentPane() ;

    content.setLayout( new BorderLayout() );
    content.add( getListPanel(), BorderLayout.CENTER ) ;
    content.add( getButtons(), BorderLayout.PAGE_END ) ;
//    this.setSize( new Dimension(300, 300) );

    this.setResizable(false);
  }


  private JPanel getListPanel()
  {
    JPanel back = new JPanel() ;
    SpringLayout layout = new SpringLayout() ;

    back.setLayout( layout ) ;

    // set up the components -------------------------------------------
    JTextArea infoText = new JTextArea() ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( back.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText( "savelanguage", "head" ) ) ;

    model = new TListModel(TGlobal.projects.getLanguages()) ;
    list = new JList( model ) ;


    list.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) ;
    list.setSelectedIndex( 0 ) ;
    list.setVisibleRowCount( 5 ) ;
    list.setCellRenderer( new ListRenderer() );

/*
         JTable table = new JTable(  new LanguageTableModel()) ;
         table.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION ) ;
         table.setColumnSelectionAllowed(false);
         table.setRowSelectionAllowed( true ) ;

         JTableHeader tableHeader = table.getTableHeader() ;
         tableHeader.setReorderingAllowed(false);
*/

    JScrollPane scroller = new JScrollPane(list) ;
    scroller.setMinimumSize( new Dimension(300, 250) );
//    scroller.setPreferredSize( new Dimension(100, 100) );

    // insert all components into the panel ----------------------
    back.add( infoText ) ;
    back.add( scroller ) ;

    // infoText
    layout.putConstraint( SpringLayout.WEST, infoText,
                          10,
                          SpringLayout.WEST, back ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          10,
                          SpringLayout.NORTH, back ) ;

    // scroller
    layout.putConstraint( SpringLayout.WEST, scroller,
                          5,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, scroller,
                          10,
                          SpringLayout.SOUTH, infoText ) ;


    // panel edges
    layout.putConstraint( SpringLayout.EAST, back,
                          10,
                          SpringLayout.EAST, infoText ) ;
    layout.putConstraint( SpringLayout.SOUTH, back,
                          20,
                          SpringLayout.SOUTH, scroller ) ;

    return back ;
  }

  private JPanel getButtons()
  {
    cancelButton = new JButton( "cancel" ) ;
    cancelButton.setEnabled( true ) ;
    cancelButton.addActionListener( this );

    saveButton = new JButton( "save" ) ;
    saveButton.addActionListener( this ) ;

    // buttons ------------------------------------------------------
    JPanel buttonPanel = new JPanel() ;
    buttonPanel.setLayout( new BorderLayout() ) ;
    buttonPanel.setBorder( new EmptyBorder( new Insets( 5, 5, 5, 5 ) ) ) ;

    JSeparator separator = new JSeparator() ;

    Box buttonBox = new Box( BoxLayout.X_AXIS ) ;
    buttonBox.setBorder( new EmptyBorder( new Insets( 5, 5, 0, 0 ) ) ) ;
    buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    buttonBox.add( saveButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 20 ) ) ;
    buttonBox.add( cancelButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 5 ) ) ;

    buttonPanel.add( separator, BorderLayout.NORTH ) ;
    buttonPanel.add( buttonBox, BorderLayout.EAST ) ;

    return buttonPanel ;
  }

  protected void init()
  {
    model.setCurrentModel(TGlobal.projects.getLanguages());

    super.init();
  }

  public void valueChanged( ListSelectionEvent e )
  {
    if ( e.getValueIsAdjusting() == false )
    {
      if ( list.getSelectedIndex() == -1 )
      {
        //No selection, disable fire button.
        saveButton.setEnabled( false ) ;
      }
      else
      {
        //Selection, enable the fire button.
        saveButton.setEnabled( true ) ;
      }
    }
  }

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if ( sender == saveButton )
    {
//      int index = list.getSelectedIndex() ;
//      list.setSelectedIndex( index ) ;
//      list.ensureIndexIsVisible( index ) ;
      this.setModalResult( TInternalDialog.MR_OKAY) ;
    }
    else // cancelButton
    {
      this.setModalResult( TInternalDialog.MR_CANCEL) ;
    }
  }

  public int[] getSelectedIndices()
  {
//    System.out.println( "selected " + list.getSelectedValues().length) ;

    return list.getSelectedIndices() ;
  }

  public ListSelectionModel getSelection()
  {
    return list.getSelectionModel() ;
  }

  // --------------------------------------------------------------------------
  //  ListModel
  //  a wrapper class for the current project model
  //  we need a dynamic Listmodel, if the project filelist is changed
  //  the default static model can't handle this
  // --------------------------------------------------------------------------
  private class TListModel extends AbstractListModel implements ListDataListener
  {
    private ListModel currentModel ;

    public TListModel( ListModel lm )
    {
      currentModel = lm ;
    }

    public void setCurrentModel(ListModel lm)
    {
      // model not realy changed
      if (lm != currentModel)
      {
        // unregister at the underlying model
        if ( currentModel != null )
        {
          currentModel.removeListDataListener( this ) ;
        }
        currentModel = lm ;

        // register at the current model
        if ( currentModel != null )
        {
          currentModel.addListDataListener( this ) ;
        }
      }

      fireContentsChanged(this, 0, 0);
    }

    public Object getElementAt( int index )
    {
      return currentModel.getElementAt( index ) ;
    }

    public int getSize()
    {
      return currentModel.getSize() ;
    }

    public void contentsChanged( ListDataEvent e )
    {
      fireContentsChanged( e.getSource(), e.getIndex0(), e.getIndex1() ) ;
    }

    public void intervalAdded( ListDataEvent e )
    {
      fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    public void intervalRemoved( ListDataEvent e )
    {
      fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
    }

  }

  // --------------------------------------------------------------------------
  //   ListCellRenderer
  // --------------------------------------------------------------------------

  private class ListRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent(
            JList list,
            Object value,   // value to display
            int index,      // cell index
            boolean iss,    // is the cell selected
            boolean chf)    // the list and the cell have the focus
    {
      super.getListCellRendererComponent(list, value, index, iss, chf);
      if (value != null)
      {
        TLanguageFile dummy = ( TLanguageFile ) value ;
        String str = dummy.getFullLanguageName() ;
        if (str == null)
        {
          this.setText( "default" ) ;
        }
        else if (str.length() < 1)
        {
          this.setText( "default" ) ;
        }
        else
        {
          this.setText( dummy.getFullLanguageName() ) ;
        }
        this.setIcon( dummy.getFLag() );
      }
      else
      {
        this.setText("?");
        this.setIcon(null);
      }
      return this ;
    }

  }


  // --------------------------------------------------------------------------
  //   TableModel - under development
  // --------------------------------------------------------------------------

  private class LanguageTableModel implements TableModel
  {
    public int getColumnCount()
    {
      return 3 ;
    }

    public int getRowCount()
    {
      return 3 ;
    }

    public Object getValueAt( int rowIndex, int columnIndex )
    {
      return "...." ;
    }

    public String getColumnName( int columnIndex )
    {
      switch (columnIndex)
      {
        case 2 : return "java utf" ;
        case 1 : return "encoding" ;
      }
      return "language" ;
    }

    public Class getColumnClass( int columnIndex )
    {
      return String.class ;
    }

    public boolean isCellEditable( int rowIndex, int columnIndex )
    {
      return false ;
    }

    public void setValueAt( Object aValue, int rowIndex, int columnIndex )
    {
    }

    public void addTableModelListener( TableModelListener l )
    {
    }

    public void removeTableModelListener( TableModelListener l )
    {
    }

  }
}
