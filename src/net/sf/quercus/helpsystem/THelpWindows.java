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


// created by : r.nagel 31.08.2005
//
// function : a simple help window
//
// todo     :
//
// modified :

package net.sf.quercus.helpsystem ;

import java.net.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.gui.* ;
import javax.swing.event.*;
import net.sf.quercus.*;


//  !!!!!  JEditorPane has HTML 3.2 !

public class THelpWindows extends JFrame implements HyperlinkListener
{
  private String helpPath ;
  private String startSite ;

  private JEditorPane view ;
  private THelpListener listener ;

  /** Create a HelpWindow instance which loads the help sites from path.
   *  The startsite can be defined by the start parameter
   */
  public THelpWindows(String path, String start)
  {
    super("help window") ;

    helpPath = path ;
    startSite = start ;

    this.setIconImage( new ImageIcon(GUIGlobals.littleHelpIconName).getImage() );

    JToolBar toolBar = new JToolBar() ;
    toolBar.add( new HomeAction() ) ;
    toolBar.addSeparator();
    toolBar.add( new PrevAction() ) ;
    toolBar.add( new NextAction() ) ;

    view = new JEditorPane() ;
    view.setContentType("text/html");
    view.setEditable(false);
    view.addHyperlinkListener( this );

    JScrollPane viewScroller = new JScrollPane(view) ;

    Container contentPane = getContentPane() ;
    contentPane.add( toolBar, BorderLayout.PAGE_START) ;
    contentPane.add( viewScroller, BorderLayout.CENTER) ;

    if (startSite != null)
    {
      loadHelp( startSite ) ;
    }

    setSize( new Dimension( 400, 500 ) ) ;

    listener = null ;
  }


  /** Reaktion auf Fenster schliessen */
  protected void processWindowEvent(WindowEvent e)
  {
    super.processWindowEvent(e) ;
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      if (listener != null)
        listener.onHelpWindowClosed();
    }
  }

  /**
   * set the listener for window actions
   * @param hwl THelpListener
   */
  public void setHelpWindowListener(THelpListener hwl)
  {
    listener = hwl ;
  }

  /**
   * load the help text and open the window
   *
   * @param name : filename of html help text, without ".html" and path
   */
  public void loadHelp(String name)
  {
    if ((name != null) && (name.length() > 0))
    {
      URL datei = THelpWindows.class.getResource( helpPath + name + ".html") ;
      try
      {
        view.setPage( datei ) ;
      }
      catch ( Exception e )
      {
        view.setText( "loading helpfile failed !\n"
            + e.getMessage()
            ) ;
      }
    }
  }

  public void loadHelp(URL name)
  {
    try
    {
      view.setPage( name ) ;
    }
    catch ( Exception e )
    {
      view.setText( "loading helpfile failed !\n"
          + e.getMessage()
          ) ;
    }
  }

  // ----------------------------------------------------------------------------
  public void hyperlinkUpdate( HyperlinkEvent e )
  {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      loadHelp( e.getURL() );
    }
  }

  // ----------------------------------------------------------------------------
    class HomeAction
        extends AbstractAction
    {
      public HomeAction()
      {
        super( "overview", new ImageIcon( GUIGlobals.homeIconName ) ) ;
      }

      public void actionPerformed( ActionEvent e )
      {
        loadHelp( startSite ) ;
      }
    }

// ----------------------------------------------------------------------------
  class NextAction
      extends AbstractAction
  {
    public NextAction()
    {
      super( "next", new ImageIcon( GUIGlobals.nextHelpIconName ) ) ;
      this.setEnabled(false);
    }

    public void actionPerformed( ActionEvent e )
    {
    }
  }

  // ----------------------------------------------------------------------------
  class PrevAction
      extends AbstractAction
  {
    public PrevAction()
    {
      super( "prev", new ImageIcon( GUIGlobals.prevHelpIconName ) ) ;
      this.setEnabled(false);
    }

    public void actionPerformed( ActionEvent e )
    {
    }
  }

}
