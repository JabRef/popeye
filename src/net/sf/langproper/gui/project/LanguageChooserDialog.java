/*
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

// ------------- UNDER CONSTRUCTION ----------------------------------------

// created by : r.nagel 02.05.2005
//              based on TableSorterDemo.java from java.sun.com
//
// function : table for language selection
//
// todo     :
//
// modified :

package net.sf.langproper.gui.project ;

import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JTable ;
import net.sf.langproper.engine.iso.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import net.sf.langproper.gui.*;
import net.sf.quercus.*;

/**
 * TableSorterDemo is like TableDemo, except that it
 * inserts a custom model -- a sorter -- between the table
 * and its data model.  It also has column tool tips.
 */
public class LanguageChooserDialog
    extends TInternalDialog implements ActionListener
{
  private JTable table ;
  private JButton okButton ;
  private JButton cancelButton ;
  private int selection ;
  private TableSorter sorter ;

  public LanguageChooserDialog(JDialog parent)
  {
    super( parent, "language selection") ;

    JPanel mainPanel = new JPanel( new BorderLayout() ) ;
    this.setContentPane( mainPanel );

    sorter = new TableSorter( TLanguageNames.runtime ) ;

    table = new JTable( sorter ) ;
    sorter.setTableHeader( table.getTableHeader() ) ;
    table.setPreferredScrollableViewportSize( new Dimension( 300, 270 ) ) ;

    //Set up tool tips for column headers.
    table.getTableHeader().setToolTipText(
        "Click to specify sorting; Control-Click to specify secondary sorting" ) ;
    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);
    table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

    if (TLanguageNames.runtime.getRowCount() > 0)
      table.setRowSelectionInterval(0, 0);

    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane( table ) ;

    JPanel buttonPanel = new JPanel() ;
    okButton = new JButton("select") ;
    okButton.addActionListener(this);
    cancelButton = new JButton("cancel") ;
    cancelButton.addActionListener(this);
    buttonPanel.add(okButton) ;
    buttonPanel.add(cancelButton) ;

    //Add the scroll pane to this panel.
    mainPanel.add( new JLabel("please select a language"), BorderLayout.NORTH) ;
    mainPanel.add( scrollPane, BorderLayout.CENTER ) ;
    mainPanel.add( buttonPanel, BorderLayout.SOUTH) ;

    selection = -1 ;

    this.pack();
  }

  public ISOLanguage getLanguage()
  {
    return (ISOLanguage) sorter.getValueAt( selection, TLanguageNames.FULL_ENTRY) ;
  }

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if ( sender == okButton)
    {
       selection = table.getSelectedRow() ;
       this.setModalResult( TInternalDialog.MR_OKAY) ;
    }
    else if (sender == cancelButton)
    {
      selection = -1 ;
      this.setModalResult( TInternalDialog.MR_CANCEL) ;
    }
  }

  public int getSelection()
  {
    return selection ;
  }

}
