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
// function : the wizard panel
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.code ;

import java.io.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.wizard.* ;
import net.sf.quercus.helpsystem.* ;
import net.sf.langproper.gui.filechooser.*;
import net.sf.langproper.scanner.*;
import net.sf.langproper.engine.project.*;

public class Step1Panel extends WizardPanel implements ActionListener
{
  private JLabel welcomeTitle ;
  private JPanel contentPanel ;

  private JLabel iconLabel ;
  private ImageIcon icon ;

  private JLabel pathLabel = new JLabel( "source directory " ) ;
  private JTextField pathField = new JTextField( 35 ) ;
//  private JButton pathButton = new JButton( GUIGlobals.folderOpenIcon ) ;
  private JButton pathButton = new JButton( "..." ) ;

  private JLabel parserLabel = new JLabel( "use parser" ) ;
  private JComboBox parserCombo ;

  private JFileChooser dirChooser = new JFileChooser() ;

  public Step1Panel(WizardData wzd)
  {
    super(wzd) ;
    iconLabel = new JLabel() ;
    contentPanel = getContentPanel() ;
    contentPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    icon = new ImageIcon( GUIGlobals.ciWallPaperName ) ;

    setLayout( new java.awt.BorderLayout() ) ;

    if ( icon != null )
    {
      iconLabel.setIcon( icon ) ;
    }

    iconLabel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) ) ;

    add( iconLabel, BorderLayout.WEST ) ;

    JPanel secondaryPanel = new JPanel() ;
    secondaryPanel.add( contentPanel, BorderLayout.NORTH ) ;
    add( secondaryPanel, BorderLayout.CENTER ) ;

    // fill default values
    TProject project = (TProject) wzd.getData() ;
    String path = project.getScannerData().getSourcePath() ;
    if (path == null)
      path = TGlobal.runtime.config.getLastWorkDirectory() ;
    else if (path.length() < 1)
      path = TGlobal.runtime.config.getLastWorkDirectory() ;

    File current = new File( path ) ;
    pathField.setText( current.toString() );

    // the panel has a valid status
    this.setValidStatus(true);

    // generate the directory chooser
    dirChooser.setCurrentDirectory( current ) ;

    ProjectFileFilter filter = new ProjectFileFilter() ;
    filter.addExtension( "java" ) ;
    filter.setDescription( "java source file" ) ;
    dirChooser.setFileFilter( filter ) ;
  }

  private JPanel getContentPanel()
  {

    JPanel contentPanel1 = new JPanel() ;

    welcomeTitle = new JLabel() ;

    contentPanel1.setLayout( new java.awt.BorderLayout() ) ;

    welcomeTitle.setFont( new java.awt.Font( "MS Sans Serif", Font.BOLD, 11 ) ) ;
    welcomeTitle.setText( "Select a source path!" ) ;
    contentPanel1.add( welcomeTitle, BorderLayout.NORTH ) ;

    // ------------------------------------------------------------------------
    JPanel mainPanel = new JPanel() ;
    SpringLayout layout = new SpringLayout() ;
    mainPanel.setLayout( layout ) ;
//    mainPanel.setMaximumSize( new Dimension(100, 100) );


    JTextArea infoText = new JTextArea() ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( mainPanel.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText("codeinsight", "site1") );

    parserCombo = new JComboBox( TGlobal.codeScanner.getRegisteredParser() ) ;
    parserCombo.setEditable( false ) ;
    parserCombo.setEnabled(false);

    pathButton.addActionListener(this);

    //Create and add the components.
    mainPanel.add( infoText ) ;
    mainPanel.add( pathLabel ) ;
    mainPanel.add( pathField ) ;
    mainPanel.add( pathButton ) ;
    mainPanel.add( parserLabel ) ;
    mainPanel.add( parserCombo ) ;

    // infoText
    layout.putConstraint( SpringLayout.WEST, infoText,
                          5,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          5,
                          SpringLayout.NORTH, mainPanel ) ;

    // pathLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, pathLabel,
                          17,
                          SpringLayout.SOUTH, infoText ) ;

    layout.putConstraint( SpringLayout.WEST, pathLabel,
                          0,
                          SpringLayout.WEST, infoText ) ;

    // pathField
    layout.putConstraint( SpringLayout.WEST, pathField,
                          5,
                          SpringLayout.EAST, pathLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, pathField,
                          13,
                          SpringLayout.SOUTH, infoText ) ;

    // pathButton
    layout.putConstraint( SpringLayout.WEST, pathButton,
                          5,
                          SpringLayout.EAST, pathField ) ;
    layout.putConstraint( SpringLayout.NORTH, pathButton,
                          10,
                          SpringLayout.SOUTH, infoText ) ;

    // parserLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, parserLabel,
                          20,
                          SpringLayout.SOUTH, pathField ) ;

    layout.putConstraint( SpringLayout.WEST, parserLabel,
                          0,
                          SpringLayout.WEST, pathLabel ) ;

    // parserCombo
    layout.putConstraint( SpringLayout.WEST, parserCombo,
                          5,
                          SpringLayout.EAST, pathLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, parserCombo,
                          15,
                          SpringLayout.SOUTH, pathField ) ;

    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, mainPanel,
                          5,
                          SpringLayout.EAST, pathButton ) ;
    layout.putConstraint( SpringLayout.SOUTH, mainPanel,
                          5,
                          SpringLayout.SOUTH, parserCombo ) ;

//    layout.layoutContainer( mainPanel ) ;
    contentPanel1.add( mainPanel, BorderLayout.CENTER ) ;

    return contentPanel1 ;
  }

  // --------------------------------------------------------------------------
  // ActionListener -----------------------------------------------------------
  // --------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if (sender == this.pathButton)
    {
      dirChooser.setDialogTitle( "source directory" ) ;
      dirChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY ) ;
      if ( dirChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
      {
        pathField.setText( dirChooser.getSelectedFile().toString() ) ;
      }
    }
  }

  // --------------------------------------------------------------------------
  // Panel I/O ----------------------------------------------------------------
  // --------------------------------------------------------------------------


  public String getSourceDir()
  {
    return pathField.getText() ;
  }

  public String getParserType()
  {
    return (String) parserCombo.getSelectedItem() ;
  }

  public JFileChooser getDirChooser()
  {
    return dirChooser ;
  }
}
