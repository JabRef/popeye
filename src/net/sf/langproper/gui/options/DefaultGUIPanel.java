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
// function : a option panel for gui stuff
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.TitledBorder;
import net.sf.langproper.TGlobal;
import net.sf.langproper.config.TUserPreferences;
import net.sf.langproper.gui.*;

public class DefaultGUIPanel
    extends TAbstractOptionPanel implements ActionListener
{
  private JRadioButton visibleOnlyRB = new JRadioButton("mark incomplete entries for visible languages only") ;
  private JRadioButton allEntriesRB = new JRadioButton("mark all incomplete entries") ;
  private JRadioButton onlyColumnsRB = new JRadioButton("mark only the column of an incomplete entry") ;

  private JCheckBox loadLastFileJB = new JCheckBox("load the last working files after a restart of application") ;
  private JCheckBox flagTableJB = new JCheckBox("show flags in table") ;

  private JCheckBox blindConfirmJB = new JCheckBox( "automatic confirmation of all inputs" ) ;
  private JCheckBox askShredderingJB = new JCheckBox("ask before shred a entry")  ;

  private boolean isChanged = false ;

  public DefaultGUIPanel()
  {
    super("GUI", "basegui") ;

    // ----------------------------------------------------
    visibleOnlyRB.addActionListener(this);
    allEntriesRB.addActionListener(this);
    onlyColumnsRB.addActionListener(this);

    ButtonGroup group = new ButtonGroup();
    group.add( visibleOnlyRB );
    group.add( onlyColumnsRB ) ;
    group.add( allEntriesRB ) ;

    JPanel groupPanel = new JPanel( new GridLayout(5, 1) ) ;
    groupPanel.setBorder( new TitledBorder( "table" ) ) ;

    groupPanel.add( new JLabel("standard marking strategy for table items :") ) ;
    groupPanel.add( visibleOnlyRB );
    groupPanel.add( onlyColumnsRB ) ;
    groupPanel.add( allEntriesRB ) ;

    // --------------------------------------------
    JPanel generalPanel = new JPanel( new GridLayout(4, 1)) ;
    generalPanel.setBorder( new TitledBorder("general options") );
//    generalPanel.setLayout( new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS) );

    loadLastFileJB.addActionListener(this);
    flagTableJB.addActionListener(this);

    blindConfirmJB.setToolTipText(
        "replace/overwrite all data, without pressing the enter key" ) ;
    blindConfirmJB.addActionListener(this);

    askShredderingJB.setToolTipText("confirm a shred command") ;
    askShredderingJB.addActionListener(this);

    generalPanel.add( loadLastFileJB) ;
    generalPanel.add( flagTableJB ) ;
    generalPanel.add( blindConfirmJB ) ;
    generalPanel.add( askShredderingJB ) ;


    // ----------------------------------------------------
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS) ;
    this.setLayout( box );

    this.add( generalPanel ) ;
    this.add( groupPanel ) ;
  }

  // --------------------------------------------------------------------------
  public boolean applyChanges()
  {
    boolean back = super.applyChanges() ;

    isChanged = false ;
    if (visibleOnlyRB.isSelected() )
    {
      TGlobal.config.setMarkEntriesMode( TGlobal.config.MARK_VISIBLE_ONLY) ;
    }
    else if (allEntriesRB.isSelected())
    {
      TGlobal.config.setMarkEntriesMode( TGlobal.config.MARK_ALL_ENTRIES) ;
    }
    else if (onlyColumnsRB.isSelected())
    {
      TGlobal.config.setMarkEntriesMode( TGlobal.config.MARK_COLUMNS_ONLY) ;
    }

    GUIGlobals.oPanel.updateTabelParameter();

    TGlobal.config.setAutomaticLastLoad( loadLastFileJB.isSelected() );
    TGlobal.config.setShowFlagsInTable( flagTableJB.isSelected() );
    TGlobal.config.setConfirmDataOnFocusLost( blindConfirmJB.isSelected() );
    TGlobal.config.setConfirmShred( this.askShredderingJB.isSelected());

    return back ;
  }

  public String getPanelBorderTitle()
  {
    return "" ;
  }

  public boolean hasChanged()
  {
    return isChanged ;
  }

  public void loadConfig()
  {
    isChanged = false ;

    String rbMode = TGlobal.config.getMarkEntriesMode() ;
    if (rbMode.hashCode() == TUserPreferences.MARK_VISIBLE_ONLY.hashCode())
    {
      visibleOnlyRB.setSelected(true);
    }
    else if (rbMode.hashCode() == TUserPreferences.MARK_ALL_ENTRIES.hashCode())
    {
      allEntriesRB.setSelected(true);
    }
    else if (rbMode.hashCode() == TUserPreferences.MARK_COLUMNS_ONLY.hashCode())
    {
      onlyColumnsRB.setSelected(true);
    }

    this.loadLastFileJB.setSelected( TGlobal.config.getAutomaticLastLoad() );
    this.flagTableJB.setSelected( TGlobal.config.getShowFlagsInTable() );
    blindConfirmJB.setSelected( TGlobal.config.confirmDataOnFocusLost() ) ;
    this.askShredderingJB.setSelected( TGlobal.config.getConfirmShred() );
  }

  public void actionPerformed( ActionEvent e )
  {
    isChanged = true ;
    if (e.getSource() == flagTableJB)
    {
      setNeedRestart() ;
    }
  }

}
