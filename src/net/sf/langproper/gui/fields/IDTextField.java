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
// function : JTextField with uniquie ID
//
// todo     :
//
// modified :


package net.sf.langproper.gui.fields ;

import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.text.* ;

public class IDTextField
    extends JTextField
{
  // some special key bindings
  static final JTextComponent.KeyBinding[] myDefaultBindings =
  {
      new JTextComponent.KeyBinding(
          KeyStroke.getKeyStroke( KeyEvent.VK_INSERT, InputEvent.CTRL_MASK ),
          DefaultEditorKit.copyAction ),
      new JTextComponent.KeyBinding(
          KeyStroke.getKeyStroke( KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK ),
          DefaultEditorKit.pasteAction ),
  } ;

  private int myID ;

  private PopupListener listener ;

  private FieldPopup fieldPopupMenu = null ;

  private String popupName = null ;

  private IDTextField me = null ;

  public JComponent other ;

  /** data were changed - every data manipulation operation must set this flag */
  private boolean dataChanged = false ;

  public IDTextField( int ID, FieldPopup popup )
  {
    myID = ID ;

    if ( popup != null )
    {
      fieldPopupMenu = popup ;
      popupName = "" ;

      me = this ;

      listener = new PopupListener( fieldPopupMenu ) ;

      this.addMouseListener( listener ) ;
    }
    else
    {
      fieldPopupMenu = null ;
    }

    JTextComponent.loadKeymap(this.getKeymap(), myDefaultBindings, this.getActions());
  }

  public int getMyID()
  {
    return myID ;
  }

  public void setMyID( int pMyID )
  {
    this.myID = pMyID ;
  }

  public void setText( String txt )
  {
    super.setText( txt ) ;
    setDataChanged( false ) ;
  }

  public void cut()
  {
    // if something selected
    String str = this.getSelectedText() ;
    if ( str != null )
    {
      if ( str.length() > 0 )
      {
        setDataChanged( true ) ;
      }
    }

    super.cut() ;
  }

  public void paste()
  {
    super.paste() ;
    setDataChanged( true ) ;
  }

  /** returns the status of data */
  public boolean isDataChanged()
  {
    return dataChanged ;
  }

  protected void setDataChanged( boolean value )
  {
    dataChanged = value ;
  }

  // ---------------------- Popup Menu ----------------------------------------
  public FieldPopup getPopupMenu()
  {
    return fieldPopupMenu ;
  }

  public String getPopupName()
  {
    return popupName ;
  }

  public void setPopupName( String pPopupName )
  {
    this.popupName = pPopupName ;
  }

  // table right click popup menu
  class PopupListener
      extends MouseAdapter
  {
    private FieldPopup popMenu ;

    public PopupListener( FieldPopup menu )
    {
      popMenu = menu ;
    }

    public void mousePressed( MouseEvent e )
    {
      maybeShowPopup( e ) ;
    }

    public void mouseReleased( MouseEvent e )
    {
      maybeShowPopup( e ) ;
    }

    private void maybeShowPopup( MouseEvent e )
    {
      if ( e.isPopupTrigger() )
      {

        popMenu.setSenderID( myID ) ;
        popMenu.setField( me ) ;
        popMenu.setMenuTitle( popupName ) ;
        popMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
      }
    }
  }

}
