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

// created by : r.nagel 08.08.2005
//
// function : option panel for some basic options
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.* ;
import net.sf.quercus.helpsystem.TInfoText;

public class TBaseOptionsPanel
    extends TAbstractOptionPanel implements ActionListener, KeyListener
{
  private JCheckBox replaceBlanksJB ;
  private JCheckBox makeBackupJB ;
  private JCheckBox saveKeysJB ;

  private JRadioButton wysiwygRB ;
  private JRadioButton sequenceRB ;

  private JTextField replaceTextField ;
  private JLabel replaceTextLabel ;

  private boolean changed = false ;

  public TBaseOptionsPanel()
  {
    super( "basic", "base" ) ;

    setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) ) ;

    add( makeDefaultProjectPanel() ) ;
    add( makeBasePanel() ) ;
    add( new JPanel() ) ;
  }

  // --------------------------------------------------------------------------
  private JPanel makeDefaultProjectPanel()
  {
    JPanel panel = new JPanel() ;
    panel.setBorder( new TitledBorder( "default project settings" ) ) ;

    SpringLayout layout = new SpringLayout() ;
    panel.setLayout( layout ) ;

    // short help text
    JTextArea infoText = new JTextArea() ;
    infoText.setRows(2);
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( panel.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText("options", "defaultProject") );

    // setting the components
    replaceBlanksJB = new JCheckBox( "replace all blanks" ) ;
    replaceBlanksJB.setToolTipText( "replace all blanks, by a user defined string" ) ;
    replaceBlanksJB.addActionListener( this ) ;

    replaceTextLabel = new JLabel( "replace whitespaces by " ) ;

    replaceTextField = new JTextField( "_", 5 ) ;
    replaceTextField.addKeyListener(this);

    saveKeysJB = new JCheckBox( "save empty keys" ) ;
    saveKeysJB.setToolTipText(
        "if a key contains only empty values, it will be saved " ) ;
    saveKeysJB.addActionListener(this);

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
    panel.add( infoText ) ;
    panel.add( replaceBlanksJB) ;
    panel.add( replaceTextLabel ) ;
    panel.add( replaceTextField ) ;
    panel.add( saveKeysJB ) ;
    panel.add( radioPanel );

    // short info (help) text -------------
    layout.putConstraint( SpringLayout.WEST, infoText,
                          5,
                          SpringLayout.WEST, panel ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // checkbox - replace blanks -------------
    layout.putConstraint( SpringLayout.WEST, replaceBlanksJB,
                          0,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, replaceBlanksJB,
                          1,
                          SpringLayout.SOUTH, infoText ) ;

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

    // saving wyswig mode -------------------
    layout.putConstraint( SpringLayout.NORTH, radioPanel,
                          10,
                          SpringLayout.SOUTH, saveKeysJB) ;

    layout.putConstraint( SpringLayout.WEST, radioPanel,
                          0,
                          SpringLayout.WEST, saveKeysJB) ;


    // panel edges -----------
    layout.putConstraint( SpringLayout.EAST, panel,
                          5,
                          SpringLayout.EAST, replaceTextField ) ;
    layout.putConstraint( SpringLayout.SOUTH, panel,
                          10,
                          SpringLayout.SOUTH, radioPanel ) ;

    return panel ;
  }

  // --------------------------------------------------------------------------
  private JPanel makeBasePanel()
  {
    JPanel panel = new JPanel() ;
    panel.setBorder( new TitledBorder( "misc" ) ) ;

    SpringLayout layout = new SpringLayout() ;
    panel.setLayout( layout ) ;

    // setting the components
    makeBackupJB = new JCheckBox( "make backup (bak files)" ) ;
    makeBackupJB.addActionListener(this);

    JPanel spacePanel = new JPanel() ;

    // insert components
    panel.add( makeBackupJB) ;
    panel.add( spacePanel ) ;


    // checkbox - replace blanks -------------
    layout.putConstraint( SpringLayout.WEST, makeBackupJB,
                          5,
                          SpringLayout.WEST, panel ) ;
    layout.putConstraint( SpringLayout.NORTH, makeBackupJB,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // insert some space
    layout.putConstraint( SpringLayout.WEST, spacePanel,
                          5,
                          SpringLayout.EAST, makeBackupJB ) ;
    layout.putConstraint( SpringLayout.NORTH, spacePanel,
                          10,
                          SpringLayout.NORTH, panel ) ;

    // panel edges -----------
    layout.putConstraint( SpringLayout.EAST, panel,
                          5,
                          SpringLayout.EAST, spacePanel ) ;
    layout.putConstraint( SpringLayout.SOUTH, panel,
                          10,
                          SpringLayout.SOUTH, makeBackupJB ) ;

    return panel ;
  }
  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  public boolean applyChanges()
  {
    changed = false ;

    TGlobal.config.setReplaceSpaceInput( replaceBlanksJB.isSelected() );
    TGlobal.config.setMakeBackupFiles( makeBackupJB.isSelected());
    TGlobal.config.setSaveEmptyKeys( saveKeysJB.isSelected());

    if (this.sequenceRB.isSelected())
    {
      TGlobal.config.setSaveWYSIWYGmode( false );
    }
    else
    {
      TGlobal.config.setSaveWYSIWYGmode( true ) ;
    }

    TGlobal.config.setReplaceString( replaceTextField.getText());

    return true ;
  }


  public String getPanelBorderTitle()
  {
    return "" ;
  }

  public boolean hasChanged()
  {
    return changed ;
  }

  public void loadConfig()
  {
    replaceBlanksJB.setSelected( TGlobal.config.replaceSpaceInInput() ) ;
    makeBackupJB.setSelected( TGlobal.config.makeBackupFiles() ) ;
    saveKeysJB.setSelected( TGlobal.config.saveEmptyKeys() ) ;

    if (TGlobal.config.saveWYSIWYGmode())
    {
      this.wysiwygRB.setSelected(true);
    }
    else
    {
      this.sequenceRB.setSelected(true);
    }

    replaceTextField.setText( TGlobal.config.getReplaceString());

    changed = false ;
  }

  // ActionListener --------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if (sender == this.replaceBlanksJB)
    {
      replaceTextField.setEnabled( replaceBlanksJB.isSelected() );
      replaceTextLabel.setEnabled( replaceBlanksJB.isSelected() );
    }
    changed = true ;
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
    changed=true ;
  }
}
