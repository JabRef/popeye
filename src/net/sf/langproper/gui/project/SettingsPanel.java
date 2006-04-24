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
// function : a panel with all project settings
//
// todo     :
//
// modified :

package net.sf.langproper.gui.project ;

import javax.swing.* ;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import net.sf.langproper.engine.project.*;
import net.sf.langproper.scanner.*;
import net.sf.langproper.*;
import java.awt.*;

public class SettingsPanel extends JPanel implements ActionListener, KeyListener
{
  private JCheckBox replaceBlanksJB ;
  private JCheckBox saveKeysJB ;
  private JCheckBox enableParserJB ;

  private JCheckBox unusedBox;

  private JRadioButton wysiwygRB ;
  private JRadioButton sequenceRB ;

  private JTextField replaceTextField ;
  private JTextField sourceCodePath ;

  private JLabel sourcePathLabel ;

  private boolean somethingChanged = false ;

  public SettingsPanel()
  {
    setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) ) ;
    add( makeDefaultProjectPanel() ) ;
    add( makeParserPanel() ) ;
//    add( new JPanel() ) ;

    if (!TGlobal.DEVEL)
    {
      enableParserJB.setEnabled(false);
    }
  }

  // --------------------------------------------------------------------------
  private JPanel makeDefaultProjectPanel()
  {
    JPanel panel = new JPanel() ;
    panel.setBorder( new TitledBorder( "entries" ) ) ;

    SpringLayout layout = new SpringLayout() ;
    panel.setLayout( layout ) ;

    // setting the components
    replaceBlanksJB = new JCheckBox( "replace all blanks" ) ;
    replaceBlanksJB.setToolTipText( "replace all blanks, by a user defined string" ) ;
    replaceBlanksJB.addActionListener( this ) ;

    JLabel replaceTextLabel = new JLabel( "replace whitespaces by " ) ;

    replaceTextField = new JTextField("_", 10) ;
    replaceTextField.addKeyListener(this);
    replaceTextField.setToolTipText("the replacement string");

    saveKeysJB = new JCheckBox( "save empty keys" ) ;
    saveKeysJB.setToolTipText(
        "if a key contains only empty values, it will be saved " ) ;
    saveKeysJB.addActionListener(this);

    unusedBox = new JCheckBox( "save WYSIWYG mode (use host encoding)" );
    unusedBox.setToolTipText(
       "If set save as a WYSISYG chars (native host encoding), otherwise with a \\u escape sequence");
    unusedBox.addActionListener(this);

    wysiwygRB = new JRadioButton("WYSIWYG mode (use the encoding from the language settings)") ;
    wysiwygRB.addActionListener(this);
    sequenceRB = new JRadioButton("java utf mode (\\u escape sequence, ISO 8859-1)") ;
    sequenceRB.addActionListener(this);

    ButtonGroup group = new ButtonGroup();
    group.add(wysiwygRB) ;
    group.add(sequenceRB) ;

    JPanel radioPanel = new JPanel( new GridLayout(0, 1)) ;
    radioPanel.setBorder( new TitledBorder("character encoding mode"));
    radioPanel.add( wysiwygRB ) ;
    radioPanel.add( sequenceRB ) ;

    // insert components
    panel.add( replaceBlanksJB) ;
    panel.add( replaceTextLabel ) ;
    panel.add( replaceTextField ) ;
    panel.add( saveKeysJB ) ;
//    panel.add( unusedBox );
    panel.add( radioPanel ) ;

    // checkbox - replace blanks -------------
    layout.putConstraint( SpringLayout.WEST, replaceBlanksJB,
                          5,
                          SpringLayout.WEST, panel ) ;
    layout.putConstraint( SpringLayout.NORTH, replaceBlanksJB,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // label
    layout.putConstraint( SpringLayout.NORTH, replaceTextLabel,
                          5,
                          SpringLayout.SOUTH, replaceBlanksJB ) ;

    layout.putConstraint( SpringLayout.WEST, replaceTextLabel,
                          10,
                          SpringLayout.WEST, replaceBlanksJB ) ;

    // textfield
    layout.putConstraint( SpringLayout.NORTH, replaceTextField,
                          3,
                          SpringLayout.SOUTH, replaceBlanksJB ) ;

    layout.putConstraint( SpringLayout.WEST, replaceTextField,
                          5,
                          SpringLayout.EAST, replaceTextLabel ) ;



    // combobox empty keys -------------------
    layout.putConstraint( SpringLayout.NORTH, saveKeysJB,
                          10,
                          SpringLayout.SOUTH, replaceTextLabel ) ;

    layout.putConstraint( SpringLayout.WEST, saveKeysJB,
                          0,
                          SpringLayout.WEST, replaceBlanksJB ) ;

/*
    layout.putConstraint( SpringLayout.NORTH, unusedBox,
                          10,
                          SpringLayout.SOUTH, saveKeysJB ) ;

    layout.putConstraint( SpringLayout.WEST, unusedBox,
                          0,
                          SpringLayout.WEST, saveKeysJB ) ;
*/
    // saving wyswig mode -------------------

    layout.putConstraint( SpringLayout.NORTH, radioPanel,
                          10,
                          SpringLayout.SOUTH, saveKeysJB) ; // unusedBox ) ;

    layout.putConstraint( SpringLayout.WEST, radioPanel,
                          0,
                          SpringLayout.WEST, saveKeysJB) ; // unusedBox ) ;


    // panel edges -----------
    layout.putConstraint( SpringLayout.EAST, panel,
                          5,
                          SpringLayout.EAST, radioPanel ) ; // replaceTextField ) ;
    layout.putConstraint( SpringLayout.SOUTH, panel,
                          10,
                          SpringLayout.SOUTH, radioPanel ) ;

    return panel ;
  }

  // --------------------------------------------------------------------------
  private JPanel makeParserPanel()
  {
    JPanel panel = new JPanel() ;
    panel.setBorder( new TitledBorder( "CodeInsight" ) ) ;

    SpringLayout layout = new SpringLayout() ;
    panel.setLayout( layout ) ;

    // setting the components
    enableParserJB = new JCheckBox( "parse source code" ) ;
    enableParserJB.setToolTipText( "enable the CodeInsight feature for the keys" ) ;
    enableParserJB.addActionListener( this ) ;

    sourcePathLabel = new JLabel( "source path" ) ;

    sourceCodePath = new JTextField() ;
    sourceCodePath.addKeyListener(this);
    sourceCodePath.setToolTipText("the path to the sources");

    // insert components
    panel.add( enableParserJB) ;
    panel.add( sourcePathLabel ) ;
    panel.add( sourceCodePath ) ;

    // checkbox - replace blanks -------------
    layout.putConstraint( SpringLayout.WEST, enableParserJB,
                          5,
                          SpringLayout.WEST, panel ) ;
    layout.putConstraint( SpringLayout.NORTH, enableParserJB,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // label
    layout.putConstraint( SpringLayout.NORTH, sourcePathLabel,
                          5,
                          SpringLayout.SOUTH, enableParserJB ) ;

    layout.putConstraint( SpringLayout.WEST, sourcePathLabel,
                          10,
                          SpringLayout.WEST, enableParserJB ) ;

    // textfield
    layout.putConstraint( SpringLayout.NORTH, sourceCodePath,
                          3,
                          SpringLayout.SOUTH, enableParserJB ) ;

    layout.putConstraint( SpringLayout.WEST, sourceCodePath,
                          5,
                          SpringLayout.EAST, sourcePathLabel ) ;

    // panel edges -----------
    // link the south edge at the label,
    // if link it with the textfield, then the textfield can change it's size
    layout.putConstraint( SpringLayout.EAST, panel,
                          5,
                          SpringLayout.EAST, sourceCodePath ) ;
    layout.putConstraint( SpringLayout.SOUTH, panel,
                          10,
                          SpringLayout.SOUTH, sourcePathLabel ) ;

    return panel ;
  }


  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  public void loadSettings( TProject project )
  {
    somethingChanged = false ;

    TProjectSettings settings = project.getSettings() ;

    this.replaceBlanksJB.setSelected( settings.getReplaceWhitespace() );
    this.replaceTextField.setText( settings.getReplaceString() );
    this.saveKeysJB.setSelected( settings.getSaveEmptyKeys() );

    if (settings.getSaveWYSIWYGmode())
    {
      this.wysiwygRB.setSelected(true);
    }
    else
    {
      this.sequenceRB.setSelected(true);
    }
//    this.unusedBox.setSelected( settings.getSaveWYSIWYGmode() );

    TScannerData sDat = project.getScannerData() ;
    this.enableParserJB.setSelected( sDat.getEnable());
    this.sourceCodePath.setText( sDat.getSourcePath() );

    checkPathSection() ;
  }

  public void saveSettings( TProject project )
  {
    if (somethingChanged)
    {
      project.setPropertiesHasChanged();
      TProjectSettings settings = project.getSettings() ;

      settings.setReplaceWhitespace( replaceBlanksJB.isSelected() ) ;
      settings.setReplaceString( replaceTextField.getText() ) ;
      settings.setSaveEmptyKeys( saveKeysJB.isSelected() ) ;
//      settings.setSaveWYSIWYGmode( unusedBox.isSelected() );

      if (this.sequenceRB.isSelected())
      {
        settings.setSaveWYSIWYGmode( false );
      }
      else
      {
        settings.setSaveWYSIWYGmode( true ) ;
      }
      TScannerData sDat = project.getScannerData() ;
      sDat.setEnable( enableParserJB.isSelected() ) ;
      sDat.setSourcePath( sourceCodePath.getText() ) ;
    }
  }


  // ActionListener --------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    somethingChanged = true ;
    Object sender = e.getSource() ;

    // enable or disable the path input section
    if (sender == enableParserJB)
    {
      checkPathSection() ;
    }
  }

  /** enables or disables the source code path section, depend on the checkbox */
  private void checkPathSection()
  {
    boolean status = enableParserJB.isSelected() ;
    sourceCodePath.setEnabled( status );
    sourcePathLabel.setEnabled( status );
  }

  // KeyListener --------------------------------------------------------------
  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
  }

  public void keyTyped( KeyEvent e )
  {
    somethingChanged = true ;
  }

}
