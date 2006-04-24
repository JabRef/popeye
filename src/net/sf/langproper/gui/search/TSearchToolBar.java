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

// created by : r.nagel 30.06.2005
//
// function : Toolbar with find/search function
//
// todo     : background search
//
// modified :

package net.sf.langproper.gui.search;

import javax.swing.* ;
import java.awt.event.*;
import net.sf.langproper.gui.*;

public class TSearchToolBar
    extends JToolBar implements KeyListener
{
  private JTextField field = new JTextField(10); // minimum size ?
  private TSearchThread thread = new TSearchThread();

  private boolean changed = false ;

  public TSearchToolBar()
  {
    field.addKeyListener( this );
    field.setBackground( GUIGlobals.SearchFieldColor );

    JPanel sPanel = new JPanel() ;
    sPanel.setLayout( new BoxLayout(sPanel, BoxLayout.LINE_AXIS) );

    sPanel.add( new JLabel("find key :") ) ;
    sPanel.add( field) ;

    this.add( sPanel ) ;
    this.setVisible( false );

    thread.start();
  }

  public void keyPressed( KeyEvent e )
  {
  }

  public void keyReleased( KeyEvent e )
  {
  }

  public void keyTyped( KeyEvent e )
  {
    char code = e.getKeyChar() ;

    if ( code == e.VK_ENTER )  // start search
    {
       if (changed)
       {
         thread.setPattern( field.getText());
       }
       else
       {
         thread.searchNext();
       }
       changed = false ;
    }
    else  // input changed
    {
      changed = true ;
    }
  }
}
