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

// created by : r.nagel 21.06.2005
//
// function : a dialog for version informations
//
// todo     :
//
// modified : 18.04.2006 r.nagel
//            update the view (new infotext area, TRichDialog)

package net.sf.versions ;

import javax.swing.* ;
import java.awt.* ;
import javax.swing.border.* ;
import java.net.* ;
import java.awt.event.* ;
import net.sf.langproper.* ;
import net.sf.quercus.*;

public class TVersionCheckDialog
    extends TRichDialog implements ActionListener
{

  // Variables declaration - do not modify
  private JButton startButton = new JButton( "start" ) ;
  private JButton closeButton = new JButton( "close" ) ;
  private JTextArea messageTextArea ;

  // End of variables declaration

//  private JPanel mainPanel = new JPanel() ;

  private TBuildInfo serverVersion = null ;

  private TBuildInfo localVersion = null ;

  public TVersionCheckDialog( JFrame parent, String dialogTitle,
                              TBuildInfo local )
  {
//    super( parent, dialogTitle, true ) ;
    super("checkversion") ;

    localVersion = local ;

    initComponents() ;

//    this.setSize( new Dimension( 300, 300 ) ) ;

    this.setLocationRelativeTo( parent ) ;
    this.setResizable(false);

    this.pack() ;
  }

  private void initComponents()
  {
    startButton.addActionListener( this ) ;
    closeButton.addActionListener( this ) ;

    addButton( startButton ) ;
    addButton( closeButton ) ;

    messageTextArea = new JTextArea() ;
    messageTextArea.setColumns( 50 ) ;
    messageTextArea.setRows( 12 ) ;
    messageTextArea.setEditable(false);
    messageTextArea.setFocusable(false);

    JPanel content = this.getContentPanel() ;
    content.setLayout( new BorderLayout() ) ;

    JScrollPane messageScroller = new JScrollPane( messageTextArea) ;
    messageScroller.setBorder(new TitledBorder(new EtchedBorder(), "results/messages"));
    messageScroller.setMinimumSize( new Dimension( 100, 250 ) ) ;

//    messageScroller.setViewportView( messageTextArea ) ;

    content.add( messageScroller, BorderLayout.CENTER ) ;
  }

  private void print()
  {
    Runnable updateText = new Runnable()
    {
      public void run()
      {
        if ( serverVersion != null )
        {
          int stat = serverVersion.getDataStatus() ;
          if ( stat == 0 )
          {
            messageTextArea.append( "ok\n\nno infos available or undetected erros" ) ;
          }
          else
          {
            messageTextArea.append( "ok\n" ) ;
            // compare local & server version
            if ( localVersion != null )
            {
              if ( serverVersion.isNewer( localVersion ) > 0 )
              {
                messageTextArea.setForeground( Color.blue ) ;
                messageTextArea.append( "\nThere is a newer version available!\n" ) ;
                messageTextArea.setForeground( Color.black ) ;
              }
            }

            // print the information about server version
            if ( stat > 0 ) // no errors
            {
              messageTextArea.append( "\nlatest version on server is:\n" ) ;
            }
            else // some errors
            {
              messageTextArea.append(
                  "failure\n\nthe version information could be incomplete\n" ) ;
            }
            messageTextArea.append( " version\t" + serverVersion.getBUILD_VERSION() +
                               "\n" ) ;
            messageTextArea.append( " build\t" + serverVersion.getBUILD_NUMBER() +
                               "\n" ) ;
            messageTextArea.append( " date\t" + serverVersion.getBUILD_DATE() + "\n" ) ;
            String meta = serverVersion.getBUILD_META() ;
            if ( meta.length() > 0 )
            {
              messageTextArea.append( " info\t" + serverVersion.getBUILD_META() +
                                 "\n" ) ;
            }
          }
        }
        startButton.setEnabled( true ) ;
        closeButton.setEnabled( true ) ;
      }
    } ;
    SwingUtilities.invokeLater( updateText ) ;
  }

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;

    if ( sender == startButton ) // start "get version" process
    {
      startButton.setEnabled( false ) ;
      closeButton.setEnabled( false ) ;
      ConnectionThread thr = new ConnectionThread() ;
      messageTextArea.setText( "" ) ;
      messageTextArea.append( "try to connect " + TGlobal.PROJECT_HOST + "..." ) ;
      thr.start() ;
    }
    else if ( sender == closeButton ) // close dialog
    {
      this.dispose() ;
    }
  }

  private class ConnectionThread
      extends Thread
  {
    public void run()
    {
      URL url = null ;
      try
      {
        url = new URL( TGlobal.SERVER_LAST_VERSION_URL ) ;
      }
      catch ( MalformedURLException me )
      {}

      serverVersion = new TBuildInfo( url ) ;

      print() ;
    }
  }

}
