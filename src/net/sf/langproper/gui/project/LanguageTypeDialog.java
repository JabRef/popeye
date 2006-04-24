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

// created by : r.nagel 27.09.2005
//
// function : edit the properties of a language versions
//
// todo     : - after adding a filename, fill the lang, country, vari fields
//            - edit of all properties (filename) in edit mode
//
// modified :

package net.sf.langproper.gui.project ;

import java.io.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.engine.iso.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.quercus.* ;
import net.sf.quercus.helpsystem.* ;

public class LanguageTypeDialog extends TInternalDialog implements
    ActionListener, KeyListener, ItemListener
{
  public static final int
      ADD_MODE = 1,
      EDIT_MODE = 2 ;

  private int mode = ADD_MODE ;

  public boolean needReload = false ;

  private JTextField filenameField = new JTextField( 20 ) ;
  private JComboBox encCombo = new JComboBox( net.sf.langproper.charset.TEncodingList.ENCODINGS ) ;
  private JCheckBox escapeCB = new JCheckBox("use the java escape sequences for this file") ;

  // language iso id and language name
  private JTextField langField = new JTextField( 5 ) ;
  private JTextField fullLangField = new JTextField( 20 ) ;

  // country iso id and country name
  private JTextField countryField = new JTextField( 5 ) ;
  private JTextField fullCountryField = new JTextField( 20 ) ;

  private JTextField variantField = new JTextField( 5 ) ;

  private JButton langButton = new JButton( ">" ) ;
  private JButton countryButton = new JButton( ">" ) ;
  private JButton filenameButton = new JButton( ".." ) ;

  private JButton closeButton = new JButton( "apply" ) ;
  private JButton cancelButton = new JButton( "cancel" ) ;

  private LanguageChooserDialog langDialog ;
  private JFileChooser dirChooser = new JFileChooser() ;

  private TLanguageFile languageFile = null ;

  private TProject project = null ;

  private JTextArea warnText = null ;

  /** parent window is the application main window */
  public LanguageTypeDialog()
  {
    super( "Language Properties" ) ;
    initGUI() ;
  }

  public LanguageTypeDialog( JDialog parent )
  {
    super( parent, "Language Properties" ) ;
    initGUI() ;
  }

  private final void initGUI()
  {
    langDialog = new LanguageChooserDialog(this) ;

    JPanel contentPanel = new JPanel( new BorderLayout() ) ;
    this.setContentPane( contentPanel ) ;

    contentPanel.add( generateFilePanel(), BorderLayout.NORTH ) ;
    contentPanel.add( generateTypePanel(), BorderLayout.CENTER ) ;
    contentPanel.add( generateButtons(), BorderLayout.SOUTH ) ;
    this.pack() ;

    this.setResizable( false ) ;
  }

  public JPanel generateTypePanel()
  {
    JPanel mainPanel = new JPanel() ;
    SpringLayout layout = new SpringLayout() ;

    mainPanel.setLayout( layout ) ;

    // set up the components -------------------------------------------
    JTextArea infoText = new JTextArea() ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( mainPanel.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText( "newlanguage", "site1" ) ) ;

    JLabel langLabel = new JLabel( "language iso code" ) ;
    JLabel fullLangLabel = new JLabel( "language" ) ;

    langField.addKeyListener( this ) ;
    // the full language name field is read only
    fullLangField.setEditable( false ) ;
    fullLangField.setFocusable( false ) ;

    langButton.addActionListener( this ) ;
    langButton.setFocusable( false ) ;

    JLabel countryLabel = new JLabel( "country iso code" ) ;
    JLabel fullCountryLabel = new JLabel( "country" ) ;

    fullCountryField.setEditable( false ) ;
    fullCountryField.setFocusable( false ) ;

    countryButton.addActionListener( this ) ;
    countryButton.setEnabled( false ) ;
    countryButton.setFocusable( false ) ;

    JLabel variantLabel = new JLabel( "variant" ) ;

    // insert all components into the panel ----------------------
    mainPanel.add( infoText ) ;

    mainPanel.add( langLabel ) ;
    mainPanel.add( fullLangLabel ) ;
    mainPanel.add( langField ) ;
    mainPanel.add( langButton ) ;
    mainPanel.add( fullLangField ) ;

    mainPanel.add( countryLabel ) ;
    mainPanel.add( fullCountryLabel ) ;
    mainPanel.add( countryField ) ;
    mainPanel.add( countryButton ) ;
    mainPanel.add( fullCountryField ) ;

    mainPanel.add( variantLabel ) ;
    mainPanel.add( variantField ) ;

    // infoText -------------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, infoText,
                          10,
                          SpringLayout.WEST, mainPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          10,
                          SpringLayout.NORTH, mainPanel ) ;

    // langLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, langLabel,
                          17,
                          SpringLayout.SOUTH, infoText ) ;

    layout.putConstraint( SpringLayout.WEST, langLabel,
                          0,
                          SpringLayout.WEST, infoText ) ;

    // langField
    layout.putConstraint( SpringLayout.NORTH, langField,
                          5,
                          SpringLayout.SOUTH, langLabel ) ;

    layout.putConstraint( SpringLayout.WEST, langField,
                          5,
                          SpringLayout.WEST, langLabel ) ;

    // langButton
    layout.putConstraint( SpringLayout.NORTH, langButton,
                          2,
                          SpringLayout.SOUTH, langLabel ) ;

    layout.putConstraint( SpringLayout.WEST, langButton,
                          5,
                          SpringLayout.EAST, langField ) ;

    // fullLangLabel
    layout.putConstraint( SpringLayout.NORTH, fullLangLabel,
                          0,
                          SpringLayout.NORTH, langLabel ) ;

    layout.putConstraint( SpringLayout.WEST, fullLangLabel,
                          15,
                          SpringLayout.EAST, langButton ) ;

    // fullLangField
    layout.putConstraint( SpringLayout.NORTH, fullLangField,
                          0,
                          SpringLayout.NORTH, langField ) ;

    layout.putConstraint( SpringLayout.WEST, fullLangField,
                          5,
                          SpringLayout.WEST, fullLangLabel ) ;

    // countryLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, countryLabel,
                          10,
                          SpringLayout.SOUTH, langField ) ;

    layout.putConstraint( SpringLayout.WEST, countryLabel,
                          0,
                          SpringLayout.WEST, langLabel ) ;

    // countryField
    layout.putConstraint( SpringLayout.NORTH, countryField,
                          5,
                          SpringLayout.SOUTH, countryLabel ) ;

    layout.putConstraint( SpringLayout.WEST, countryField,
                          5,
                          SpringLayout.WEST, countryLabel ) ;

    // countryButton
    layout.putConstraint( SpringLayout.NORTH, countryButton,
                          2,
                          SpringLayout.SOUTH, countryLabel ) ;

    layout.putConstraint( SpringLayout.WEST, countryButton,
                          5,
                          SpringLayout.EAST, countryField ) ;

    // fullCountryLabel
    layout.putConstraint( SpringLayout.NORTH, fullCountryLabel,
                          0,
                          SpringLayout.NORTH, countryLabel ) ;

    layout.putConstraint( SpringLayout.WEST, fullCountryLabel,
                          0,
                          SpringLayout.WEST, fullLangLabel ) ;

    // fullCountryField
    layout.putConstraint( SpringLayout.NORTH, fullCountryField,
                          0,
                          SpringLayout.NORTH, countryField ) ;

    layout.putConstraint( SpringLayout.WEST, fullCountryField,
                          5,
                          SpringLayout.WEST, fullCountryLabel ) ;

    // variantLabel -------------------------------------------------------------
    layout.putConstraint( SpringLayout.NORTH, variantLabel,
                          10,
                          SpringLayout.SOUTH, langField ) ; // countryField ) ;

    layout.putConstraint( SpringLayout.WEST, variantLabel,
                          15,
                          SpringLayout.EAST, fullLangField ) ; // .WEST, langLabel ) ;

    // variantField
    layout.putConstraint( SpringLayout.NORTH, variantField,
                          5,
                          SpringLayout.SOUTH, variantLabel ) ;

    layout.putConstraint( SpringLayout.WEST, variantField,
                          5,
                          SpringLayout.WEST, variantLabel ) ;

    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, mainPanel,
                          10,
                          SpringLayout.EAST, infoText ) ;
    layout.putConstraint( SpringLayout.SOUTH, mainPanel,
                          20,
                          SpringLayout.SOUTH, variantField ) ;

    return mainPanel ;
  }

  private JPanel generateFilePanel()
  {
    JPanel panel = new JPanel() ;
    SpringLayout layout = new SpringLayout() ;

    panel.setLayout( layout ) ;

    // set up the components -------------------------------------------
    JTextArea infoText = new JTextArea() ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( panel.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText( "newlanguage", "site2" ) ) ;

    warnText = new JTextArea() ;
    warnText.setLineWrap( false ) ;
    warnText.setEditable( false ) ;
    warnText.setFocusable( false ) ;
    warnText.setBackground( panel.getBackground() ) ;
    warnText.append( TInfoText.runtime.getText( "newlanguage", "warning1" ) ) ;

    JLabel filenameLabel = new JLabel( "filename" ) ;
    JLabel encLabel = new JLabel( "use the following encoding" ) ;
    JLabel defLabel = new JLabel( "host encoding is :"
                                  +
                                  net.sf.langproper.engine.Utils.
                                  getDefaultHostEncoding() ) ;
    defLabel.setEnabled( false ) ;

    encCombo.addItemListener( this );

    filenameButton.addActionListener( this ) ;

    // insert all components into the panel ----------------------
    panel.add( infoText ) ;

    panel.add( filenameLabel ) ;
    panel.add( filenameField ) ;
    panel.add( filenameButton ) ;
    panel.add( encLabel ) ;
    panel.add( encCombo ) ;
    panel.add( defLabel ) ;
    panel.add( escapeCB ) ;
    panel.add( warnText ) ;

    // infoText -------------------------------------------------------------
    layout.putConstraint( SpringLayout.WEST, infoText,
                          10,
                          SpringLayout.WEST, panel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // filename
    layout.putConstraint( SpringLayout.NORTH, filenameLabel,
                          17,
                          SpringLayout.SOUTH, infoText ) ;

    layout.putConstraint( SpringLayout.WEST, filenameLabel,
                          0,
                          SpringLayout.WEST, infoText ) ;

    // field
    layout.putConstraint( SpringLayout.NORTH, filenameField,
                          5,
                          SpringLayout.SOUTH, filenameLabel ) ;

    layout.putConstraint( SpringLayout.WEST, filenameField,
                          5,
                          SpringLayout.WEST, filenameLabel ) ;

    // button
    layout.putConstraint( SpringLayout.NORTH, filenameButton,
                          2,
                          SpringLayout.SOUTH, filenameLabel ) ;

    layout.putConstraint( SpringLayout.WEST, filenameButton,
                          5,
                          SpringLayout.EAST, filenameField ) ;

    // encoding ----
    layout.putConstraint( SpringLayout.NORTH, encLabel,
                          0,
                          SpringLayout.NORTH, filenameLabel ) ;

    layout.putConstraint( SpringLayout.WEST, encLabel,
                          25,
                          SpringLayout.EAST, filenameButton ) ;

    // encoding combo
    layout.putConstraint( SpringLayout.NORTH, encCombo,
                          2,
                          SpringLayout.SOUTH, encLabel ) ;

    layout.putConstraint( SpringLayout.WEST, encCombo,
                          5,
                          SpringLayout.WEST, encLabel ) ;

    // host encoding label
    layout.putConstraint( SpringLayout.NORTH, defLabel,
                          5,
                          SpringLayout.SOUTH, encCombo ) ;

    layout.putConstraint( SpringLayout.WEST, defLabel,
                          0,
                          SpringLayout.WEST, encCombo ) ;

    // host encoding label
    layout.putConstraint( SpringLayout.NORTH, escapeCB,
                          5,
                          SpringLayout.SOUTH, defLabel ) ;

    layout.putConstraint( SpringLayout.WEST, escapeCB,
                          0,
                          SpringLayout.WEST, filenameField ) ;


    // warning encoding label
    layout.putConstraint( SpringLayout.NORTH, warnText,
                          5,
                          SpringLayout.SOUTH, escapeCB ) ;

    layout.putConstraint( SpringLayout.WEST, warnText,
                          0,
                          SpringLayout.WEST,  filenameField) ;


    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, panel,
                          10,
                          SpringLayout.EAST, infoText ) ;
    layout.putConstraint( SpringLayout.SOUTH, panel,
                          5,
                          SpringLayout.SOUTH, warnText ) ;

    return panel ;
  }

  private JPanel generateButtons()
  {
    // buttons ------------------------------------------------------
    JPanel buttonPanel = new JPanel() ;
    buttonPanel.setLayout( new BorderLayout() ) ;
    buttonPanel.setBorder( new EmptyBorder( new Insets( 5, 5, 5, 5 ) ) ) ;

    JSeparator separator = new JSeparator() ;

    Box buttonBox = new Box( BoxLayout.X_AXIS ) ;
    buttonBox.setBorder( new EmptyBorder( new Insets( 5, 5, 0, 0 ) ) ) ;
    buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    buttonBox.add( closeButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 20 ) ) ;
    buttonBox.add( cancelButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 5 ) ) ;

    buttonPanel.add( separator, BorderLayout.NORTH ) ;
    buttonPanel.add( buttonBox, BorderLayout.EAST ) ;

    closeButton.addActionListener( this ) ;
    cancelButton.addActionListener( this ) ;

    return buttonPanel ;

  }

  /** open a dialog for selecting iso code <=> real name language */
  private void showLanguageDialog()
  {
    if ( langDialog.showModal() == TInternalDialog.MR_OKAY )
    {
      ISOLanguage iso = langDialog.getLanguage() ;
      if ( iso != null )
      {
        langField.setText( iso.getLanguageIso() ) ;
        fullLangField.setText( iso.getLanguageName() ) ;
      }
    }
    langField.requestFocus() ;
  }

  /** fill the dialog components */
  private void setData( TLanguageFile langFile, TProject pProject )
  {
    project = pProject ;

    if ( langFile != null )
    {
      mode = EDIT_MODE ;
      filenameField.setText( langFile.getFileName() ) ;
      String dummy = langFile.getLanguageCode() ;
      if ( ( dummy != null ) && ( dummy.length() > 0 ) )
      {
        fullLangField.setText( TLanguageNames.runtime.getLanguageName( dummy ) ) ;
      }
      else
      {
        fullLangField.setText( "" ) ;
      }

      encCombo.setSelectedItem( langFile.getDefaultEncoding() );

      langField.setText( dummy ) ;
      dummy = langFile.getCountryCode() ;
      if ( dummy != null )
      {
        fullCountryField.setText( TCountryNames.runtime.getCountryName( dummy ) ) ;
      }
      else
      {
        fullCountryField.setText( "" ) ;
      }
      countryField.setText( langFile.getCountryCode() ) ;
      variantField.setText( langFile.getVariantCode() ) ;

      escapeCB.setSelected( langFile.isSaveWithEscapeSequence() );
    }
    else
    {
      mode = ADD_MODE ;
      filenameField.setText( "" ) ;
      langField.setText( "" ) ;
      fullLangField.setText( "" ) ;
      countryField.setText( "" ) ;
      fullCountryField.setText( "" ) ;
      variantField.setText( "" ) ;
      encCombo.setSelectedIndex(0);

      escapeCB.setSelected(true);
    }

    if ( project != null )
    {
      dirChooser.setSelectedFile( new File( project.getWorkingDirectory() ) ) ;
      checkEncoding();
    }

  }

  // --------------------------------------------------------------------------
  //  I/O
  // --------------------------------------------------------------------------

  /** Make the dialog visible and modal */
  public int showModal( TLanguageFile langFile, TProject pProject )
  {
    languageFile = langFile ;
    setData( langFile, pProject ) ;
    updateMode() ;
    return super.showModal() ;
  }

  public String getLanguage()
  {
    return langField.getText() ;
  }

  public String getCountry()
  {
    return countryField.getText() ;
  }

  public String getVariant()
  {
    return variantField.getText() ;
  }

  // --------------------------------------------------------------------------
  //  ActionListener
  // --------------------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if ( sender == langButton )
    {
      showLanguageDialog() ;
    }
    else if ( sender == closeButton )
    {
      makeResult() ;
      this.setModalResult( TInternalDialog.MR_OKAY ) ;
    }
    else if ( sender == cancelButton )
    {
      this.setModalResult( TInternalDialog.MR_CANCEL ) ;
    }

    else if ( sender == filenameButton )
    {
      if ( project != null )
      {
        if ( dirChooser != null )
        {
          dirChooser.setDialogTitle( "add a language file" ) ;
          dirChooser.setFileSelectionMode( JFileChooser.FILES_ONLY ) ;
          if ( dirChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
          {
            File file = dirChooser.getSelectedFile() ;

            if ( checkFileAllowed( file ) )
            {
              // filenameField.setText( file.getName() );
              //languageFile = project.getProjectData().getAvailableLangs().createFileHandle( file ) ;
              filenameField.setText(file.getName());
            }
            else
            {
              filenameField.setText( "" ) ;
            }
          }
        }
        else
        {
          System.out.println( "no chooser" ) ;
        }
      }
      else
      {
        System.out.println( "no project" ) ;
      }
    }

  }

  // --------------------------------------------------------------------------
  //  KeyListener
  // --------------------------------------------------------------------------
  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
  }

  public void keyTyped( KeyEvent e )
  {
    if ( e.getKeyChar() == '?' )
    {
      e.consume() ;
      e.setKeyChar( KeyEvent.CHAR_UNDEFINED ) ;
      e.setKeyCode( KeyEvent.VK_UNDEFINED ) ;
      showLanguageDialog() ;
    }
  }

  // --------------------------------------------------------------------------
  //  ItemListener
  // --------------------------------------------------------------------------
  public void itemStateChanged( ItemEvent e )
  {
    if (e.getStateChange() == ItemEvent.SELECTED)
    {
      // System.out.println( "ITemListener" ) ;
      checkEncoding() ;
    }
  }

  // --------------------------------------------------------------------------
  //  Logic
  // --------------------------------------------------------------------------

  private boolean checkFileAllowed( File file )
  {
    boolean back = true ;

    if ( !project.allowDifferentBundleName() )
    {
      TProjectData data = project.getProjectData() ;
      TProjectFileList list = data.getAvailableLangs() ;

      TLanguageFile dummy = list.createFileHandle( file ) ;

//      System.out.println( file.getName() ) ;

      // test, if the new file has the same bundle name
      // we need some already existing files -> check with the first file (at index 0)
      if ( list.getSize() > 0 )
      {
        // not the same bundle name
        if ( !dummy.isBundleName( list.getData( 0 ) ) )
        {
          JOptionPane.showMessageDialog( null, "Sorry, for this project only a <"
                                         + dummy.getBaseName()
                                         + "_x_y> style filename is allowed!",
                                         "error adding language file",
                                         JOptionPane.ERROR_MESSAGE ) ;
          back = false ;
        }
      }
    }
    return back ;
  }

  private void updateMode()
  {
    if ( mode == EDIT_MODE )
    {
      filenameField.setEnabled( false ) ;
      langField.setEditable( false ) ;
      countryField.setEditable( false ) ;
      variantField.setEditable( false ) ;
      langButton.setEnabled( false ) ;
      filenameButton.setEnabled(false);
    }
    else
    {
      filenameField.setEnabled( true ) ;
      langField.setEditable( true ) ;
      countryField.setEditable( true ) ;
      variantField.setEditable( true ) ;
      langButton.setEnabled( true ) ;
      filenameButton.setEnabled(true);
    }

    checkEncoding() ;
  }

  private void makeResult()
  {
    if (mode == ADD_MODE)
    {
      TProjectData pData = project.getProjectData() ;
      TProjectFileList liste = pData.getAvailableLangs() ;

      int keysBefore = pData.getRowCount() ;

      // insert a file, if available
      String str = filenameField.getText() ;
      if ((str != null) && (str.length() > 0))
      {
        File file = new File(project.getWorkingDirectory(), str) ;
        languageFile = liste.createFileHandle(file) ;
        if (languageFile != null)
        {
          pData.addNewLanguage( languageFile ) ;
        }
      }
      else  // try to insert with language, country, variant settings
      {
        languageFile =
            project.getProjectData().addNewLanguage(
                langField.getText(),
                countryField.getText(),
                variantField.getText(),
                project.getWorkingDirectory() ) ;
      }

      // try to load already available data
      if (languageFile.getFileHandle().exists())
      {
        pData.loadSingleFile( languageFile, liste.getSize() - 1 ) ;
      }

      int keysAfter = pData.getRowCount() ;
      int newKeys = keysAfter - keysBefore ;
      if (newKeys > 0)
      {
         JOptionPane.showMessageDialog(this, "<" +newKeys +"> new keys found!",
                                       "new keys", JOptionPane.CLOSED_OPTION);

      }
    }
    else   // EDIT
    {
      // no special things to do
    }

    // insert the encoding - for both edit and add mode
    if (languageFile != null)
    {
      languageFile.setDefaultFileEncoding( encCombo.getSelectedItem().toString() ) ;
      languageFile.setSaveWithEscapeSequence( escapeCB.isSelected() );
    }
  }


  private void checkEncoding()
  {
    // get the elected encoding type
    String str = encCombo.getSelectedItem().toString() ;
    boolean javaUTF = net.sf.langproper.charset.TEncodingList.isJavaEncoding( str ) ;


    // other encoding
    if ( !javaUTF )
    {
      escapeCB.setEnabled(true);

      // in java mode
      if (! project.getSettings().getSaveWYSIWYGmode())
      {
        warnText.setForeground( Color.red );
        return ;
      }
    }
    else
    {
      escapeCB.setSelected(true);
      escapeCB.setEnabled(false);
    }

    warnText.setForeground( warnText.getBackground() );
  }

}
