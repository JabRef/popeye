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


// created by : r.nagel 4.4.2005
//
// function : JTextField and Label
//
// todo     : - mark the field if some data has changed (?)
//            - mark the label (bold font?) if the textfield has the focus
//            - a textfield could be not visible (scrollpane!) if it becomes
//              the focus ->  insert mechanism to ensure visibility
//
// modified : r.nagel 27.06.2005
//            extract popup menu and actions to package fields.action
//            r.nagel 30.08.2005
//            fix some image visibility bugs, new state (checked)


package net.sf.langproper.gui.fields ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;
import javax.swing.text.DefaultHighlighter.* ;

import net.sf.langproper.gui.* ;

public class TLangTextField
    extends JPanel implements KeyListener, FocusListener
{
  private static int counter = 0 ;

  public static final int
      NOTHING_STATE = 1,
      ERROR_STATE = 2,
      REQUIRE_STATE = 3,
      WARNING_STATE = 4,
      CHECKED_STATE = 5,
      USER_STATE = 6 // user definied status
      ;

  private static final ImageIcon
      warningIcon = new ImageIcon( GUIGlobals.warningIconName ),
      errorIcon = new ImageIcon( GUIGlobals.errorIconName ),
      checkedIcon = new ImageIcon( GUIGlobals.checkedIconName ),
      requireIcon = new ImageIcon( GUIGlobals.requireIconName ) ;

  private ImageIcon userIcon = null ; // user icon
  private int fieldState ;
  private JLabel imageLabel ;

  private JPanel bigPanel ;
  private IDTextField textField ;
  private JLabel label ;

  private SpringLayout layout ;

  private EventListenerList listenerList ;

  // auf diese Komponente wird der Focus nach einem Return/Enter gesetzt
  private JComponent nextFocusable ;

  private Image currentImage = null ;

  // charAt positions
  private Insets leerInsets ;
  private Insets bildInsets = new Insets(0, 9, 0, 0);

  /** creates a JTextfield with JLabel (contains labelText) and an identifier id */
  public TLangTextField( String labelText, int id, FieldPopup popup )
  {
    super() ;

//    this.setOpaque( false ) ;
    this.setLayout( new OverlayLayout( this ) ) ;
//    this.setBorder( BorderFactory.createEtchedBorder());

    listenerList = new EventListenerList() ;

    // build the labeled textfield --------------------------------------------
    textField = new IDTextField( id, popup)
    {
      public void paint( Graphics g )
      {
          super.paint( g ) ;

          if (currentImage != null)
          {
            Graphics2D g2 = (Graphics2D) g ;
            g2.drawImage(currentImage, -8, -2, this) ;
          }

//        System.out.println( "paint " +(counter++) ) ;
      }
    } ;

    textField.addKeyListener( this ) ;
    textField.addFocusListener( this ) ;
    textField.setPopupName( labelText );
    textField.setMaximumSize( new Dimension( 2000, 30 ) ) ;
    textField.setFont( GUIGlobals.defaultLanguageFont) ;


    // charat position
    leerInsets = textField.getMargin() ;

    label = new JLabel( labelText, JLabel.TRAILING ) ;
    label.setLabelFor( textField ) ;
    label.addFocusListener( this ) ;

    // insert label and textfield
    layout = new SpringLayout() ;
    bigPanel = new JPanel( layout ) ;
//    bigPanel.setOpaque( false ) ;

    bigPanel.add( label ) ;
    bigPanel.add( textField ) ;

    //Adjust constraints for the label so it's at (5,5).
    layout.putConstraint( SpringLayout.WEST, label,
                          10,
                          SpringLayout.WEST, bigPanel ) ;
    layout.putConstraint( SpringLayout.NORTH, label,
                          6,
                          SpringLayout.NORTH, bigPanel ) ;

    //Adjust constraints for the text field so it's at
    //(<label's right edge> + 5, 5).
    layout.putConstraint( SpringLayout.WEST, textField,
                          10,
                          SpringLayout.EAST, label ) ;
    layout.putConstraint( SpringLayout.NORTH, textField,
                          -2,
                          SpringLayout.NORTH, label ) ;

    //Adjust constraints for the content pane: Its right
    //edge should be 5 pixels beyond the text field's right
    //edge, and its bottom edge should be 5 pixels beyond
    //the bottom edge of the tallest component (which we'll
    //assume is textField).
    layout.putConstraint( SpringLayout.EAST, bigPanel,
                          5,
                          SpringLayout.EAST, textField ) ;
    layout.putConstraint( SpringLayout.SOUTH, bigPanel,
//                          8,
//                          SpringLayout.SOUTH, label ) ;
                          5, SpringLayout.SOUTH, textField) ;


    // build image section ----------------------------------------------------
    imageLabel = new JLabel() ;
    imageLabel.setOpaque(false);
    fieldState = REQUIRE_STATE ;
    setImage() ;
    textField.other = imageLabel ;

    // insert image and labeled-field -----------------------------------------
    this.add( imageLabel ) ;
    this.add( bigPanel ) ;

    // pipe all focus events to local class methods
    super.addFocusListener( this ) ;
  }

  // --------------------------------------------------------------------------
  public String getText()
  {
    return textField.getText() ;
  }

  public void setText( String newText )
  {
    textField.setText( newText ) ;
  }

  // --------------------------------------------------------------------------
  public void setLabelText( String labelText )
  {
    label.setText( labelText ) ;
  }

  public String getLabelText()
  {
    return label.getText() ;
  }

  // --------------------------------------------------------------------------
  public int getID()
  {
    return textField.getMyID() ;
  }

  public void setID( int pMyID )
  {
    textField.setMyID( pMyID ) ;
  }

  // --------------------------------------------------------------------------

  public void paint( Graphics g )
  {
    imageLabel.setLocation( textField.getX() - 8, 2 ) ;
    super.paint( g ) ;
//    System.out.println( "panel paint" ) ;
  }

  // icon handling ------------------------------------------------------------
  // setzt anhand des Status (fieldState) das entsprechende Icon
  private final void setImage()
  {
    switch ( fieldState )
    {
      case ERROR_STATE:
        currentImage = errorIcon.getImage() ;
        imageLabel.setIcon( errorIcon ) ;
        imageLabel.setToolTipText( "failure" ) ;
        textField.setMargin( bildInsets );
        break ;
      case REQUIRE_STATE:
        currentImage = requireIcon.getImage() ;
        imageLabel.setIcon( requireIcon ) ;
        imageLabel.setToolTipText( "required" ) ;
        textField.setMargin( bildInsets );
        break ;
      case WARNING_STATE:
        currentImage = warningIcon.getImage() ;
        imageLabel.setIcon( warningIcon ) ;
        imageLabel.setToolTipText( "warning" ) ;
        textField.setMargin( bildInsets );
        break ;
      case CHECKED_STATE:
        currentImage = checkedIcon.getImage() ;
        imageLabel.setIcon( checkedIcon ) ;
        imageLabel.setToolTipText( "ok" ) ;
        textField.setMargin( bildInsets );
        break ;
      case USER_STATE:
        currentImage = userIcon.getImage() ;
        imageLabel.setIcon( userIcon ) ;
        imageLabel.setToolTipText( "user message" ) ;
        textField.setMargin( bildInsets );
        break ;
      default: //         case NOTHING_STATE :
        currentImage = null ;
        imageLabel.setIcon( null ) ;
        imageLabel.setToolTipText( "" ) ;
        textField.setMargin( leerInsets );
    }
    repaint() ;
  }

  // user defined icon
  public void setUserIcon( ImageIcon icon )
  {
    userIcon = icon ;
  }

  public ImageIcon getUserIcon()
  {
    return userIcon ;
  }

  /** input status */
  public final void setStatus( int status )
  {
    fieldState = status ;
    setImage() ;
  }

  public final int getStatus()
  {
    return fieldState ;
  }

  /** returns the status of data */
  public boolean isDataChanged()
  {
    return textField.isDataChanged() ;
  }

  public void markText(int start, int end)
  {
    try
    {
      textField.getHighlighter().addHighlight( start, end,
                                     new DefaultHighlightPainter( Color.RED ) ) ;
    }
    catch (Exception e) {}
  }


  // --------------------------------------------------------------------------
  // Action handling
  // taken from Java 2 Platform Std.Ed.v1.4.2 API - EventListenerList

  public void addActionListener( ActionListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.add( ActionListener.class, listener ) ;
    }
  }

  public void removeActionListener( ActionListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.remove( ActionListener.class, listener ) ;
    }
  }

  protected void fireActionPerformed( String command )
  {
    ActionEvent foo = new ActionEvent( this, this.getID(), command ) ;

    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList() ;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2 ; i >= 0 ; i -= 2 )
    {
      if ( listeners[i] == ActionListener.class )
      {
        ( ( ActionListener ) listeners[i + 1] ).actionPerformed( foo ) ;
      }
    }
  }

  // --------------------------------------------------------------------------
  // Key handling
  public void addKeyListener( KeyListener listener )
  {
    textField.addKeyListener( listener ) ;
  }

  // KeyListener interface ---------------
  public void keyPressed( KeyEvent e )
  {
    int code = e.getKeyCode() ;
    switch (code)
    {
      case KeyEvent.VK_DOWN :
        fireActionPerformed( "down" ) ;
        e.consume();
        break ;
      case KeyEvent.VK_UP :
        fireActionPerformed("up") ;
        e.consume();
        break ;
      case KeyEvent.VK_PAGE_DOWN :
        fireActionPerformed("next") ;
        e.consume();
        break ;
      case KeyEvent.VK_PAGE_UP :
        fireActionPerformed("prev") ;
        e.consume();
        break ;
    }
  }

  public void keyReleased( KeyEvent e )
  {
  }

  public void keyTyped( KeyEvent e )
  {
    char code = e.getKeyChar() ;
    if ( code == e.VK_ENTER )
    {
//      if (dataField != null)
      {
        e.setKeyChar( e.CHAR_UNDEFINED ) ;
        e.setKeyCode( -1 ) ;

        // only fire this event, if the data has been changed
        if ( textField.isDataChanged() )
        {
          fireActionPerformed( "enter" ) ;
        }

        // not shift + enter => focus to next
        if ( ( e.getModifiers() & e.SHIFT_MASK ) != e.SHIFT_MASK )
        {
          this.changeFocusToNext() ;
        }
      }
    }
    else
    {
      if (!TKeyLock.keys.isLocked())
      {
        textField.setDataChanged( true ) ;
      }
    }
  }

  // --------------------------------------------------------------------------
  // focus handling -----------------------------------------------------------
  // --------------------------------------------------------------------------

  // msg from JTextfield
  public void focusGained( FocusEvent e )
  {
    if ( e.getSource() == textField )
    {
      this.fireFocusGained( e.isTemporary() ) ;
    }
    else // if the label get's the focus then delegate it to textfield
    {
      textField.grabFocus() ;
      // !!! field could be not visible (scrollpane!)
      // insert mechanism to ensure visibility
    }
    textField.setBackground( GUIGlobals.theme.ActiveTextField);
  }

  // msg from JTextfield
  public void focusLost( FocusEvent e )
  {
    if ( e.getSource() == textField )
    {
      textField.setBackground( GUIGlobals.theme.InactiveTextField);
      this.fireFocusLost( e.isTemporary() ) ;
      this.repaint();
    }
  }

  public void addFocusListener( FocusListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.add( FocusListener.class, listener ) ;
    }
  }

  public void removeFocusListener( FocusListener listener )
  {
    if ( listenerList != null )
    {
      listenerList.remove( FocusListener.class, listener ) ;
    }
  }

  protected void fireFocusLost( boolean temporary )
  {
    FocusEvent foo = new FocusEvent( this, this.getID(), temporary ) ;
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList() ;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2 ; i >= 0 ; i -= 2 )
    {
      if ( listeners[i] == FocusListener.class )
      {
        ( ( FocusListener ) listeners[i + 1] ).focusLost( foo ) ;
      }
    }
  }

  protected void fireFocusGained( boolean temporary )
  {
    FocusEvent foo = new FocusEvent( this, this.getID(), temporary ) ;
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList() ;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2 ; i >= 0 ; i -= 2 )
    {
      if ( listeners[i] == FocusListener.class )
      {
        ( ( FocusListener ) listeners[i + 1] ).focusGained( foo ) ;
      }
    }
  }

  public void setNextFocusable( JComponent nextfocus )
  {
    nextFocusable = nextfocus ;
  }

  public JComponent getNextFocusable()
  {
    return nextFocusable ;
  }

  public void changeFocusToNext()
  {
    if ( nextFocusable != null )
    {
      nextFocusable.grabFocus() ;
    }
  }

  public String toString()
  {
    return ( "TLangTextField <" + this.label.getText() + "><" + this.getID() +
             ">" ) ;
  }

  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  // Example Action
  /*
    private class TExtendedAction extends AbstractAction
    {
      public TExtendedAction()
      {
        super( "example" ) ;
      }

      public void actionPerformed( ActionEvent e )
      {
        if (e.getSource() instanceof FieldPopup)
          {
            FieldPopup popup = (FieldPopup) e.getSource() ;
            System.out.println( "ID " + popup.getSenderID() ) ;
            System.out.println( "extended Action" +e.getID()) ;
          }
      }
    }
   */

  // --------------------------------------------------------------------------

  /*
     Quick & Dirty Example:

     mark textparts in jtextfield

     import java.awt.*;
     import javax.swing.*;
     import javax.swing.text.*;

     public class Test {
         public static final void main( String[] args ) {
             try {
                 JFrame f = new JFrame("Test");
                 f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 JPanel p = new JPanel();
                 JTextField tf = new JTextField("TEST");
                 tf.getHighlighter().addHighlight( 0, 2,
                     new DefaultHighlighter.DefaultHighlightPainter(
                         Color.RED) );
                 p.add( tf );
                 f.getContentPane().add( p );
                 f.pack();
                 f.setVisible( true );
             }
             catch ( Exception e ) {
                 e.printStackTrace();
             }
         }
     }
   */
}
