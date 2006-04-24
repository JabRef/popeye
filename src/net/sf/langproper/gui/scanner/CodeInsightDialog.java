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


// created by : r.nagel 15.09.2005
//
// function : a dialog with code scanner results (codeinsight)
//
// todo     :
//
// modified :

package net.sf.langproper.gui.scanner ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.gui.* ;
import net.sf.langproper.scanner.* ;
import net.sf.quercus.*;
import net.sf.langproper.gui.task.*;
import net.sf.langproper.engine.project.*;

public class CodeInsightDialog extends TInternalDialog implements ActionListener, ScanConsumer
{
  private JButton closeButton ;

  private TScannerData scannerData ;

  private TKeyRefTable table ;

  public CodeInsightDialog()
  {
    super("CodeInsight") ;

    scannerData = null ;

    JPanel mainPanel = new JPanel( new BorderLayout() ) ;
//    mainPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    mainPanel.add( generateContent(), BorderLayout.CENTER) ;
    mainPanel.add( generateButtons(), BorderLayout.SOUTH) ;

    this.getContentPane().add( mainPanel ) ;

    TaskManager.runtime.registerConsumer( TaskManager.runtime.scanTask, this);
  }

  public void dispose()
  {
    TaskManager.runtime.unregisterConsumer( TaskManager.runtime.scanTask, this);
    super.dispose() ;
  }

  private JPanel generateButtons()
  {

    JPanel buttonPanel = new JPanel() ;
    JSeparator separator = new JSeparator() ;
    Box buttonBox = new Box( BoxLayout.X_AXIS ) ;

    closeButton = new JButton("close") ;
    closeButton.addActionListener( this ) ;

    buttonPanel.setLayout( new BorderLayout() ) ;
    buttonPanel.add( separator, BorderLayout.NORTH ) ;

    buttonBox.setBorder( new EmptyBorder( new Insets( 5, 10, 5, 10 ) ) ) ;
//    buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    buttonBox.add( closeButton ) ;

    buttonPanel.add( buttonBox, java.awt.BorderLayout.EAST ) ;

    return buttonPanel ;
  }

  private Container generateContent()
  {
    JTabbedPane pane = new JTabbedPane() ;

    // table of keys
    JPanel tablePanel = new JPanel() ;
    tablePanel.setLayout( new BorderLayout());

    table = new TKeyRefTable() ;
    JScrollPane scroller = new JScrollPane(table) ;

    tablePanel.add( scroller, BorderLayout.CENTER ) ;

    // source file handling
    JPanel filePanel = new JPanel() ;


    pane.add("keys", tablePanel) ;
    pane.add("source files", filePanel) ;

    return pane ;
  }

  public void actionPerformed( ActionEvent e )
  {
    setModalResult( TInternalDialog.MR_OKAY) ;
  }

  public void taskReady()
  {
  }

  public void setData( TProject project )
  {
    if (project != null)
    {
      scannerData = project.getScannerData() ;
      if ( scannerData != null )
      {
        table.setModel( scannerData.getKeyList() ) ;
      }
    }
  }

}
