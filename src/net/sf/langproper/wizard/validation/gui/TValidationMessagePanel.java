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

// created by : r.nagel 27.6.2005
//
// function : message panel for showing the validation check messages
//
// todo     :
//
//
// modified :


package net.sf.langproper.wizard.validation.gui ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.tree.* ;

import net.sf.langproper.wizard.validation.* ;
import javax.swing.event.*;
import net.sf.langproper.gui.*;

public class TValidationMessagePanel
    extends JPanel implements KeyListener, ActionListener, TreeSelectionListener

{
  private JTree warnings ;
  private TreeModel warningData ;

  private TValidationChecker validChecker ;

  private JTextField content ;
  private JButton applyButton ;
  private JButton fixButton ;

  public TValidationMessagePanel()
  {
    validChecker = new TValidationChecker() ; // errors, warnings, hints

    // JList --------------------------------------------------------------
    warningData = validChecker.getValidationsList() ;
    warnings = new JTree( warningData ) ;
    warnings.addTreeSelectionListener( this ) ;
//    warnings.setCellRenderer( new IntegrityListRenderer() ) ;

    JScrollPane paneScrollPane = new JScrollPane( warnings ) ;
    paneScrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ) ;
    paneScrollPane.setPreferredSize( new Dimension( 540, 255 ) ) ;
    paneScrollPane.setMinimumSize( new Dimension( 10, 10 ) ) ;

    // Fix Panel ---------------------------------------------------------
    JPanel fixPanel = new JPanel() ;
    BoxLayout box = new BoxLayout( fixPanel, BoxLayout.LINE_AXIS ) ;

    JLabel label1 = new JLabel( "Field_content" ) ;

    content = new JTextField( 40 ) ;
    content.addKeyListener( this ) ;
    applyButton = new JButton( "Apply" ) ;
    applyButton.addActionListener( this ) ;
    applyButton.setEnabled( false ) ;
    fixButton = new JButton( "Suggest" ) ;
    fixButton.setEnabled( false ) ;

    fixPanel.add( label1 ) ;
    fixPanel.add( content ) ;
    fixPanel.add( applyButton ) ;
    fixPanel.add( fixButton ) ;

    // Main Panel --------------------------------------------------------
    this.setLayout( new BorderLayout() ) ;
    this.add( paneScrollPane, BorderLayout.CENTER ) ;
    this.add( fixPanel, BorderLayout.SOUTH ) ;

  }

  // ------------------------------------------------------------------------

  public void updateView( )
  {
    validChecker.check();
  }

// --------------------------------------------------------------------------
// This methods are required by KeyListener
  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
    applyButton.setEnabled( true ) ;
    if ( e.getKeyCode() == e.VK_ENTER )
    {
      applyButton.doClick() ;
    }
  }

  public void keyTyped( KeyEvent e )
  {
  }

  public void actionPerformed( ActionEvent e )
  {
/*
    Object obj = e.getSource() ;
    if ( obj == applyButton )
    {
      Object data = warnings.getSelectedValue() ;
      if ( data != null )
      {
//        warningData.valueUpdated( warnings.getSelectedIndex() ) ;
      }
    }

    applyButton.setEnabled( false ) ;
*/
  }

  // ---------------------------------------------------------------------------
  // TreeSelectionListener
  // ---------------------------------------------------------------------------

  public void valueChanged( TreeSelectionEvent e )
  {
    TreePath path = warnings.getSelectionPath() ;
    if (path != null)
    {
      Object source = path.getLastPathComponent() ;
      if ( source != null )
      {
        if ( source instanceof TValueNode )
        {
          TValueNode vDummy = ( TValueNode ) source ;
//          System.out.println( vDummy ) ;

          GUIGlobals.oPanel.setSelectedEntry( vDummy.getReference() ) ;
        }
        else if ( source instanceof TValidationType )
        {
//          System.out.println( ( ( TValidationType ) source ).getDescription() ) ;
        }
      }
    }
//    System.out.println( e.getSource() ) ;
  }

}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------


