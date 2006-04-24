/*
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


// created by : r.nagel 4.4.2005
//
// function : a simple popup menu for the IDTextField
//
// todo     :
//
// modified :


package net.sf.langproper.gui.fields ;

import javax.swing.* ;
import java.awt.event.*;

public class FieldPopup extends JPopupMenu implements ActionListener
{
  private int senderID ;
  private IDTextField field ;
  private boolean active = false ;

  private JMenuItem headItem ;

  public FieldPopup()
  {
    super() ;
    senderID = -1 ;

    headItem = new JMenuItem("Menu") ;
    headItem.setEnabled( false );
    this.add(headItem) ;

    this.addSeparator();
  }

  public void setMenuTitle(String title)
  {
    if (title != null)
    {
      headItem.setText(title + " Menu");
    }
    else
      headItem.setText("Menu") ;
  }

  public int getSenderID()
  {
    return senderID;
  }

  public void setSenderID(int pSenderID)
  {
    this.senderID = pSenderID;
  }

  public void actionPerformed( ActionEvent e )
  {
    // workaround : set the sender
    e.setSource(this);
  }

  public JMenuItem add(Action a)
  {
    JMenuItem back = super.add(a) ;
    if (back != null)
      back.addActionListener( this );

    return back ;
  }

  public String toString()
  {
    return "popup <" +senderID +">" ;
  }

  public IDTextField getField()
  {
    return field;
  }

  public void setField(IDTextField pField)
  {
    this.field = pField;
  }



}
