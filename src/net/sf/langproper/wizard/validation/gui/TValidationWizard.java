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
// function : wizard window for validation check
//
// todo     : "fix the discrepancies" action
//
// modified :

package net.sf.langproper.wizard.validation.gui ;

import java.io.* ;
import java.net.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;

import net.sf.langproper.gui.* ;
import net.sf.quercus.*;

public class TValidationWizard
    extends TRichDialog
    implements ActionListener
{

  private JButton closeButton ;
  private JButton startButton ;
  private TValidationMessagePanel warnPanel ;

  public TValidationWizard()
  {
//    super( GUIGlobals.APPLICATION_MAINFRAME, "dialog", false ) ; // no modal
    super("validations", true) ;

    try
    {
      jbInit() ;
      pack() ;
    }
    catch ( Exception ex )
    {
      ex.printStackTrace() ;
    }
  }

  private void jbInit() {
//    this.setModal( true ) ;
    this.setResizable( false ) ;

    // messages
    this.setTitle( "validation messages - beta" ) ;
    warnPanel = new TValidationMessagePanel( ) ;

    // Buttons
    startButton = new JButton("Scan") ;
    startButton.addActionListener( this) ;
    closeButton = new JButton("Close");
    closeButton.addActionListener( this) ;

    addButton( startButton ) ;
    addButton( closeButton ) ;

    // content
    Container content = this.getContentPanel() ;

    content.setLayout( new BorderLayout());
    content.add(warnPanel, BorderLayout.CENTER) ;
    content.add( Box.createHorizontalBox(), BorderLayout.SOUTH) ;
  }


// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;

    if (sender == closeButton)
    {
      dispose() ;
    }
    else if (sender == startButton)
    {
      scan() ;
    }
  }


  // scan the data - separate thread
  public void scan()
  {
    startButton.setEnabled(false);
    Runnable scanWork = new Runnable()
    {
      public void run()
      {
        warnPanel.updateView();
        GUIGlobals.oPanel.updateView();
        startButton.setEnabled(true);
      }
    } ;

    SwingUtilities.invokeLater(scanWork);
  }



}
