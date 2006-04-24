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
// function :
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.project ;

import java.io.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.quercus.helpsystem.* ;
import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.wizard.* ;
import net.sf.langproper.engine.project.*;
import net.sf.langproper.gui.filechooser.*;

public class Step1Panel extends WizardPanel implements ActionListener, KeyListener
{
  // detect a change name action
  private String standardName ;

  private JLabel welcomeTitle ;
  private JPanel contentPanel ;

  private JLabel iconLabel ;
  private ImageIcon icon ;

  private JLabel nameLabel = new JLabel( "Project name" ) ;
  private JTextField nameField = new JTextField( 25 ) ;

  private JCheckBox alreadyFiles = new JCheckBox("There are already existing files for this project", true) ;

  private JLabel pathLabel = new JLabel( "working directory" ) ;
  private JTextField pathField = new JTextField( 35 ) ;
  private JButton pathButton = new JButton( "..." ) ;

  private JLabel typeLabel = new JLabel( "project type" ) ;
  private JComboBox typeCombo ;

  private TFileChooser dirChooser ;

  public Step1Panel(WizardData wzd)
  {
    super(wzd) ;
    iconLabel = new JLabel() ;
    contentPanel = getContentPanel() ;
    contentPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    icon = new ImageIcon( GUIGlobals.wizardWallPaperName ) ;

    setLayout( new java.awt.BorderLayout() ) ;

    if ( icon != null )
    {
      iconLabel.setIcon( icon ) ;
    }

    iconLabel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) ) ;
    Dimension dim = new Dimension( icon.getIconWidth(), icon.getIconHeight()) ;
    iconLabel.setMaximumSize( dim );
    iconLabel.setPreferredSize( dim );

    add( iconLabel, BorderLayout.WEST ) ;

    JPanel secondaryPanel = new JPanel() ;
    secondaryPanel.add( contentPanel, BorderLayout.NORTH ) ;
    add( secondaryPanel, BorderLayout.CENTER ) ;

    // fill default values
    standardName = "project" ;
    File current = new File( TGlobal.runtime.config.getLastWorkDirectory()) ;
    pathField.setText( current.toString() );
    nameField.setText( standardName );
    nameField.selectAll();
    nameField.grabFocus();

    // the panel has a valid status
    this.setValidStatus(true);

    // generate the directory chooser
    dirChooser = GUIGlobals.PROJECT_LOAD_SAVE_DIALOG ;
    dirChooser.setCurrentDirectory( current ) ;
  }

  private JPanel getContentPanel()
  {

    JPanel contentPanel1 = new JPanel() ;

    welcomeTitle = new JLabel() ;

    contentPanel1.setLayout( new java.awt.BorderLayout() ) ;

    welcomeTitle.setFont( new java.awt.Font( "MS Sans Serif", Font.BOLD, 11 ) ) ;
    welcomeTitle.setText( "Select a name for a new " + TGlobal.APPLICATION_NAME +
                          " project!" ) ;
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
    infoText.append( TInfoText.runtime.getText("newwizard", "site1") );

    typeCombo = new JComboBox( TGlobal.runtime.projects.getProjectTypeList() ) ;
    typeCombo.setEditable( false ) ;

    nameField.addKeyListener(this);

    pathField.addKeyListener(this);
    pathButton.addActionListener(this);

    JPanel ops = getOptionPanel() ;

    //Create and add the components.
    mainPanel.add( infoText ) ;
    mainPanel.add( nameLabel ) ;
    mainPanel.add( nameField ) ;
    mainPanel.add( pathLabel ) ;
    mainPanel.add( pathField ) ;
    mainPanel.add( pathButton ) ;
    mainPanel.add( typeLabel ) ;
    mainPanel.add( typeCombo ) ;
//    mainPanel.add( ops ) ;

    // infoText
    layout.putConstraint( SpringLayout.WEST, infoText,
                          5,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          5,
                          SpringLayout.NORTH, mainPanel ) ;

    // namelabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, nameLabel,
                          5,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, nameLabel,
                          20,
                          SpringLayout.SOUTH, infoText ) ;

    // nameField
    layout.putConstraint( SpringLayout.WEST, nameField,
                          5,
                          SpringLayout.EAST, pathLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, nameField,
                          -2,
                          SpringLayout.NORTH, nameLabel ) ;

    // pathLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, pathLabel,
                          12,
                          SpringLayout.SOUTH, nameField ) ;

    layout.putConstraint( SpringLayout.WEST, pathLabel,
                          0,
                          SpringLayout.WEST, nameLabel ) ;

    // pathField
    layout.putConstraint( SpringLayout.WEST, pathField,
                          5,
                          SpringLayout.EAST, pathLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, pathField,
                          10,
                          SpringLayout.SOUTH, nameField ) ;

    // pathButton
    layout.putConstraint( SpringLayout.WEST, pathButton,
                          5,
                          SpringLayout.EAST, pathField ) ;
    layout.putConstraint( SpringLayout.NORTH, pathButton,
                          7,
                          SpringLayout.SOUTH, nameField ) ;

    // typeLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, typeLabel,
                          20,
                          SpringLayout.SOUTH, pathField ) ;

    layout.putConstraint( SpringLayout.WEST, typeLabel,
                          0,
                          SpringLayout.WEST, nameLabel ) ;

    // typeCombo
    layout.putConstraint( SpringLayout.WEST, typeCombo,
                          5,
                          SpringLayout.EAST, pathLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, typeCombo,
                          15,
                          SpringLayout.SOUTH, pathField ) ;
/*
    // Options ----------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, ops,
                          10,
                          SpringLayout.WEST, nameLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, ops,
                          15,
                          SpringLayout.SOUTH, typeCombo ) ;
*/
    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, mainPanel,
                          5,
                          SpringLayout.EAST, pathButton ) ;
    layout.putConstraint( SpringLayout.SOUTH, mainPanel,
                          5,
                          SpringLayout.SOUTH, typeCombo ) ; // ops ) ;

    contentPanel1.add( mainPanel, BorderLayout.CENTER ) ;

    return contentPanel1 ;
  }

  private JPanel getOptionPanel()
  {
    JPanel oPanel = new JPanel() ;

    BoxLayout box  = new BoxLayout( oPanel, BoxLayout.PAGE_AXIS ) ;
    oPanel.setLayout( box );
    oPanel.setBorder( new TitledBorder("Options"));

    oPanel.add( alreadyFiles ) ;
    return oPanel ;
  }

  private void checkState()
  {
    boolean state = false ;
    String str = nameField.getText() ;
    if (str != null)
    {
      if (str.length() > 0)  // nameField ok
      {
        str = pathField.getText() ;
        if (str != null)
        {
          if (str.length() > 0)  // pathField ok
          {
            state = true ;
          }
        }
      }
    }

    this.setValid( state );
  }

  // --------------------------------------------------------------------------
  // ActionListener -----------------------------------------------------------
  // --------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if (sender == this.pathButton)
    {
      dirChooser.setDialogTitle( "working directory" ) ;
      dirChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY ) ;
      if ( dirChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
      {
        pathField.setText( dirChooser.getSelectedFile().toString() ) ;
        checkState() ;
      }
      // disable the directories only option
      dirChooser.setFileSelectionMode( JFileChooser.FILES_ONLY) ;
    }
  }

  // --------------------------------------------------------------------------
  // KeyListener --------------------------------------------------------------
  // --------------------------------------------------------------------------

  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
    checkState() ;
  }

  public void keyTyped( KeyEvent e )
  {
  }

  // --------------------------------------------------------------------------
  // Panel I/O ----------------------------------------------------------------
  // --------------------------------------------------------------------------

  public String getProjectType()
  {
    return (String) typeCombo.getSelectedItem() ;
  }

  public void setProjectType( String typeName )
  {
  if (typeName != null)
    typeCombo.setSelectedItem( typeName ) ;
  }

  public String getProjectName()
  {
    return nameField.getText() ;
  }

  public String getWorkDir()
  {
    return pathField.getText() ;
  }

  public JFileChooser getDirChooser()
  {
    return dirChooser ;
  }
}
