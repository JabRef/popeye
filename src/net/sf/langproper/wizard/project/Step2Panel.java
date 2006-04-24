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
// function :  collector panel for already existing project files
//
// todo     :
//
// modified :  11.03.2006 redesign

package net.sf.langproper.wizard.project ;

import java.io.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.project.* ;
import net.sf.langproper.wizard.* ;
import net.sf.quercus.helpsystem.* ;

public class Step2Panel extends WizardPanel implements ActionListener
{
  private javax.swing.JLabel welcomeTitle ;

  private JPanel contentPanel ;
  private JLabel iconLabel ;
  private JSeparator separator ;
  private JLabel textLabel ;
  private JPanel titlePanel ;

  private JLabel baseLabel = new JLabel( "reference file" ) ;
  private JTextField baseField = new JTextField( 25 ) ;
  private JButton baseButton = new JButton( "..." ) ;

  private JButton rescanButton = new JButton("rescan") ;

  private TProject project = null ;

  private JFileChooser dirChooser = new JFileChooser() ;
  private LanguageManagerPanel table ;

  private File baseFile = null ;

  public Step2Panel(WizardData wzd)
  {
    super(wzd) ;

    contentPanel = getContentPanel(wzd) ;
    contentPanel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;

    ImageIcon icon = getImageIcon() ;

    titlePanel = new javax.swing.JPanel() ;
    textLabel = new javax.swing.JLabel() ;
    iconLabel = new javax.swing.JLabel() ;
    separator = new javax.swing.JSeparator() ;

    setLayout( new java.awt.BorderLayout() ) ;

    titlePanel.setLayout( new java.awt.BorderLayout() ) ;
    titlePanel.setBackground( Color.gray ) ;

    textLabel.setBackground( Color.gray ) ;
    textLabel.setFont( new Font( "MS Sans Serif", Font.BOLD, 14 ) ) ;
    textLabel.setText( "Project Language Files" ) ;
    textLabel.setBorder( new EmptyBorder( new Insets( 10, 10, 10, 10 ) ) ) ;
    textLabel.setOpaque( true ) ;

    iconLabel.setBackground( Color.gray ) ;
    if ( icon != null )
    {
      iconLabel.setIcon( icon ) ;
    }

    titlePanel.add( textLabel, BorderLayout.CENTER ) ;
    titlePanel.add( iconLabel, BorderLayout.EAST ) ;
    titlePanel.add( separator, BorderLayout.SOUTH ) ;

    add( titlePanel, BorderLayout.NORTH ) ;
    JPanel secondaryPanel = new JPanel() ;
    secondaryPanel.add( contentPanel, BorderLayout.NORTH ) ;
    add( secondaryPanel, BorderLayout.WEST ) ;

  }

  private JPanel getContentPanel(WizardData wzd)
  {

    JPanel contentPanel1 = new JPanel() ;

    welcomeTitle = new JLabel() ;

    contentPanel1.setLayout( new java.awt.BorderLayout() ) ;

    welcomeTitle.setFont( new java.awt.Font( "MS Sans Serif", Font.BOLD, 11 ) ) ;
    welcomeTitle.setText( "Please insert already existing language files!" ) ;
    contentPanel1.add( welcomeTitle, BorderLayout.NORTH ) ;

    // ------------------------------------------------------------------------
    Container mainPanel = new JPanel() ;
    SpringLayout layout = new SpringLayout() ;
    mainPanel.setLayout( layout ) ;

    JTextArea infoText = new JTextArea() ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( mainPanel.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText("newwizard", "site2") );

    JDialog parent = null ;
    if (wzd.getWizard() != null)
    {
      parent = wzd.getWizard().getDialog() ;
    }
    table = new LanguageManagerPanel(TGlobal.projects.getCurrentProject(), parent, true) ;

    rescanButton.addActionListener(this);
    baseButton.addActionListener( this );

    //Create and add the components.
    mainPanel.add( infoText ) ;
    mainPanel.add(baseLabel ) ;
    mainPanel.add( baseField ) ;
    mainPanel.add( baseButton ) ;
    mainPanel.add( rescanButton ) ;
    mainPanel.add( table ) ;

    //infoText
    layout.putConstraint( SpringLayout.WEST, infoText,
                          5,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          5,
                          SpringLayout.NORTH, mainPanel ) ;

    // baseLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, baseLabel,
                          0,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, baseLabel,
                          12,
                          SpringLayout.SOUTH, infoText ) ;

    // nameField
    layout.putConstraint( SpringLayout.WEST, baseField,
                          5,
                          SpringLayout.EAST, baseLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, baseField,
                          10,
                          SpringLayout.SOUTH, infoText ) ;

    // baseButton
    layout.putConstraint( SpringLayout.WEST, baseButton,
                          5,
                          SpringLayout.EAST, baseField ) ;
    layout.putConstraint( SpringLayout.NORTH, baseButton,
                          7,
                          SpringLayout.SOUTH, infoText ) ;

    //rescanButton
    layout.putConstraint( SpringLayout.WEST, rescanButton,
                          5,
                          SpringLayout.EAST, baseButton ) ;
    layout.putConstraint( SpringLayout.NORTH, rescanButton,
                          7,
                          SpringLayout.SOUTH, infoText) ;

    //table
    layout.putConstraint( SpringLayout.WEST, table,
                          5,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, table,
                          15,
                          SpringLayout.SOUTH, baseField) ;



    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, mainPanel,
                          5,
                          SpringLayout.EAST, infoText ) ;
    layout.putConstraint( SpringLayout.SOUTH, mainPanel,
                          5,
                          SpringLayout.SOUTH, table ) ;


    layout.layoutContainer( mainPanel ) ;
    contentPanel1.add( mainPanel, BorderLayout.CENTER ) ;

    return contentPanel1 ;
  }

  private ImageIcon getImageIcon()
  {
    //  Icon to be placed in the upper right corner.
    return null ;
  }

  // --------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
/*
    if (sender == addButton)
    {
       if (project != null)
       {
         if (dirChooser != null)
         {
           dirChooser.setDialogTitle( "add a file" ) ;
           dirChooser.setFileSelectionMode( JFileChooser.FILES_ONLY) ;
           if ( dirChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
           {
             addFile( dirChooser.getSelectedFile() ) ;
           }
         }
         else System.out.println( "no chooser" ) ;
       }
       else System.out.println( "no project" ) ;
    }
    else if (sender == removeButton)
    {
      int row = table.getSelectedRow() ;

      if (project != null)
      {
        TProjectData data = project.getProjectData() ;
        TProjectFileList list = data.getAvailableLangs() ;

        TLanguageFile file = list.getData(row) ;

        list.removeLanguageVersion(file) ;

        if (list.getSize() < 1)
        {
          removeButton.setEnabled(false);
        }

        model.fireTableDataChanged();
      }

    }
    else if (sender == rescanButton)
    {

    }
    else
 */
    if (sender == this.baseButton)
    {
//      dirChooser.setProjectFileFilter( (String) typeCombo.getSelectedItem() );
      dirChooser.setDialogTitle( "select a base file" ) ;
      dirChooser.setFileSelectionMode( JFileChooser.FILES_ONLY) ;
      if ( dirChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
      {
        setBaseFile( dirChooser.getSelectedFile() ) ;
        rescan() ;
      }
    }
  }


  // --------------------------------------------------------------------------
  // Panel I/O ----------------------------------------------------------------
  // --------------------------------------------------------------------------

  public void updateProjectData()
  {
    project = (TProject) this.getWizardSharedData() ;
    if (project != null)
    {
//      model.setModelData( project.getProjectData().getAvailableLangs() );
      table.setProject( project );
    }
    WizardData data = (WizardData) this.getWizardData() ;
    dirChooser = (JFileChooser) data.getProperty("dir") ;
  }

  public void rescan()
  {
    rescanButton.setEnabled(false);

    // get the shared data structure
    WizardData data = this.getWizardData() ;

    if ( data != null )
    {
      // get the Project
      TProject dummy = ( TProject ) data.getData() ;

      // project data
      TProjectData pdat = dummy.getProjectData() ;

      // base file specified ?
      String base = getBaseFileName() ;
      if ( base != null )
      {
        if (base.length() > 0)
        {
          File workFile ;

          // !!! working directory and directory of basefile could be not equal !!!!
          if (baseFile != null)
          {
            workFile = baseFile ;
          }
          else
          {
            workFile = new File( dummy.getWorkingDirectory(), base ) ;
          }

          pdat.collect( workFile ) ;
          rescanButton.setEnabled( true ) ;
        }
      }
/*
      else  // no base file -> delete all files in table
      {
        TLanguageList lList = pdat.getAvailableLangs() ;
        lList.clear();
      }
*/
      // update the visible components
      updateProjectData();
    }
  }

  public void setBaseFile( File file )
  {
    if (file != null)
    {
      baseField.setText( file.getName() ) ;
      baseFile = file ;
    }
  }

  public String getBaseFileName()
  {
    return baseField.getText() ;
  }

}
