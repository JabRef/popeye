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
// function : a standard error dialog
//
// todo     :
//
// modified :

package net.sf.quercus.report ;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import net.sf.langproper.gui.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent ;
import net.sf.quercus.TInternalDialog;

public class ErrorReportDialog extends TInternalDialog implements ActionListener
{

  private JButton closeButton ;
  private JButton detailsButton ;
  private JButton fixButton ;

  private JLabel errorTitle ;
  private JTextArea errorDescription ;

  private JPanel detailsPanel ;
  private JList detailsList ;

  // no details visible
  private boolean fullMode = false ;

  public ErrorReportDialog()
  {
    super("error report") ;
    // basic panel
    JPanel mainPanel = new JPanel( new BorderLayout() ) ;
    mainPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    JPanel southPanel = new JPanel( new BorderLayout()) ;
    detailsPanel = this.generateDetails() ;

    southPanel.add(detailsPanel, BorderLayout.CENTER) ;
    southPanel.add(generateButtons(), BorderLayout.SOUTH) ;

    mainPanel.add( generateContent(), BorderLayout.CENTER) ;
    mainPanel.add( southPanel, BorderLayout.SOUTH) ;

    this.getContentPane().add( mainPanel ) ;

    this.pack();
  }


  private JPanel generateButtons()
  {

    JPanel buttonPanel = new JPanel() ;
    buttonPanel.setBorder( new EmptyBorder( new Insets( 5, 0, 0, 0 ) ) ) ;

    JSeparator separator = new JSeparator() ;
    Box buttonBox = new Box( BoxLayout.X_AXIS ) ;

    closeButton = new JButton("close") ;
    detailsButton = new JButton("details >>") ;

    closeButton.addActionListener( this ) ;
    detailsButton.addActionListener( this ) ;

    //  Create the buttons with a separator above them, then place them
    //  on the east side of the panel with a small amount of space between
    //  the back and the next button, and a larger amount of space between
    //  the next button and the cancel button.

    buttonPanel.setLayout( new BorderLayout() ) ;
    buttonPanel.add( separator, BorderLayout.NORTH ) ;

    buttonBox.setBorder( new EmptyBorder( new Insets( 5, 10, 5, 10 ) ) ) ;
    buttonBox.add( detailsButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    buttonBox.add( closeButton ) ;

    buttonPanel.add( buttonBox, java.awt.BorderLayout.EAST ) ;

    return buttonPanel ;
  }

  private JPanel generateContent()
  {
    JPanel mainPanel = new JPanel() ;
//    mainPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    SpringLayout layout = new SpringLayout() ;
    mainPanel.setLayout( layout ) ;

    errorTitle = new JLabel("xx errors detected in module <modname>") ;
    errorTitle.setFont( new java.awt.Font( "MS Sans Serif", Font.BOLD, 12 ) ) ;

    JLabel image = new JLabel( new ImageIcon( GUIGlobals.errorReportName1 ) ) ;

    errorDescription = new JTextArea("bla bla");
    errorDescription.setBackground( mainPanel.getBackground());
    errorDescription.setEditable(false);

    mainPanel.add( errorTitle ) ;
    mainPanel.add(image) ;
    mainPanel.add( errorDescription ) ;

    // errorTitle
    layout.putConstraint( SpringLayout.WEST, errorTitle,
                          5,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, errorTitle,
                          5,
                          SpringLayout.NORTH, mainPanel ) ;

    // image -------------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, image,
                          5,
                          SpringLayout.WEST, errorTitle ) ;
    layout.putConstraint( SpringLayout.NORTH, image,
                          5,
                          SpringLayout.SOUTH, errorTitle ) ;

    // description
    layout.putConstraint( SpringLayout.WEST, errorDescription,
                          5,
                          SpringLayout.EAST,  image) ;
    layout.putConstraint( SpringLayout.NORTH, errorDescription,
                          10,
                          SpringLayout.SOUTH, errorTitle ) ;

    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, mainPanel,
                          5,
                          SpringLayout.EAST, errorTitle) ;
    layout.putConstraint( SpringLayout.SOUTH, mainPanel,
                          5,
                          SpringLayout.SOUTH, image ) ;

    return mainPanel ;
  }

  private JPanel generateDetails()
  {
    JPanel panel = new JPanel( new BorderLayout() ) ;
    panel.setBorder( BorderFactory.createTitledBorder("errors"));

    detailsList = new JList(  ) ;
    detailsList.setVisibleRowCount(5);

    JScrollPane scroller = new JScrollPane( detailsList ) ;

    fixButton = new JButton("fix") ;
    fixButton.setEnabled(false);
    Box buttonBox = new Box( BoxLayout.Y_AXIS ) ;
    buttonBox.setBorder( new EmptyBorder( new Insets( 0, 10, 5, 10 ) ) ) ;
    buttonBox.add( fixButton ) ;

    panel.add( scroller, BorderLayout.CENTER ) ;
    panel.add( buttonBox, BorderLayout.EAST) ;
    panel.setVisible( fullMode );
    return panel ;
  }

  public void report()
  {
//    errorTitle.setText("xx errors detected in module <modname>");

    this.setVisible(true) ;
  }

  private void switchMode()
  {
    fullMode = !fullMode ;
    if (fullMode)
    {
      detailsButton.setText("no details");
    }
    else
    {
      detailsButton.setText("details >>");
    }
    detailsPanel.setVisible( fullMode );
    pack() ;
  }

  // --------------------------------------------------------------------------
  // ActionListener -----------------------------------------------------------
  // --------------------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if (sender == closeButton)
    {
      setModalResult( TInternalDialog.MR_OKAY) ;
    }
    else if (sender == detailsButton)
    {
      switchMode() ;
    }
  }
}
