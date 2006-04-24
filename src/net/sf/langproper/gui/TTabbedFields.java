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


// created by : r.nagel 15.09.2005
//
// function : a tabbed field panel
//           located at the south area of the main window, it contains a
//           "all translations" and a "single translation" view)
//
// todo     :
//
// modified :

package net.sf.langproper.gui ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.event.* ;
import javax.swing.plaf.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.fields.* ;
import net.sf.langproper.gui.listener.* ;
import net.sf.langproper.gui.fields.single.*;

public class TTabbedFields extends JTabbedPane implements ListSelectionListener
{
  private JScrollPane scroller ;
  private TTextFieldPanel textfields = new TTextFieldPanel();
  private TProjectInfoPanel infos = new TProjectInfoPanel();

  private int tabHeigh ;

  public TTabbedFields()
  {
    super( JTabbedPane.TOP ) ;
    scroller = new JScrollPane(textfields) ;
    scroller.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    add(scroller) ;
    setIconAt(0, new ImageIcon(GUIGlobals.viewAllEntriesIconName) );
    setToolTipTextAt(0, "all visible languages for a single key") ;

    if (TGlobal.DEVEL)
    {
      add( new TSingleEntryPanel() ) ;
      setIconAt( 1, new ImageIcon( GUIGlobals.viewSingleEntryIconName ) ) ;
      setToolTipTextAt( 1, "edit and show all properties of a single language" ) ;

      add( infos ) ;
      setIconAt( 2, new ImageIcon( GUIGlobals.viewInfoStatsName ) ) ;
      setToolTipTextAt( 2, "all informations about the project" ) ;

    } else
    {

      add( infos ) ;
      setIconAt( 1, new ImageIcon( GUIGlobals.viewInfoStatsName ) ) ;
      setToolTipTextAt( 1, "all informations about the project" ) ;
    }

    // resolve the heigh of the tabs section
    TabbedPaneUI ui = this.getUI() ;
    Rectangle rec = ui.getTabBounds( this, 0) ;
    tabHeigh = rec.height ;
  }

  /** update the table view, some columns could be changed */
  public void updateStructure()
  {
    infos.updateStatus();
    textfields.updateStructure();
  }

  // insert a listener for the "select next or prev item" action
  public void addViewUpdateListener( ViewUpdateListener listener )
  {
    textfields.addViewUpdateListener( listener ) ;
  }

  public void requestFocus()
  {
    super.requestFocus();
  }

  /** mark some text into the active panel */
  public void selectKeyText( int offset, int end)
  {
    textfields.markText( offset, end ) ;
  }

  /** Show entry source or real text.
   *  If true, the table shows the "native" data. Otherwise the utf encoding is
   *  enabled */
  public void setShowSource( boolean pShowSource )
  {
    textfields.setShowSource( pShowSource);
  }

  /** the minimal heigh for painting the complete component */
  public int getRealHeigh()
  {
    Dimension dim = scroller.getLayout().preferredLayoutSize( scroller ) ;
    return dim.height + tabHeigh ;
  }


  // -------------- ListSelectionListener -----------------------------------
  public void valueChanged( ListSelectionEvent e )
  {
    textfields.valueChanged( e ) ;
  }

}
