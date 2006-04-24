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
// function : contains all translations for one entry and allows to edit it
//
// todo     :
//
//
// modified :


package net.sf.langproper.gui.fields ;

import java.util.logging.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.fields.action.* ;
import java.util.* ;
import net.sf.langproper.gui.listener.* ;
import net.sf.langproper.engine.project.TProjectData ;
import net.sf.langproper.engine.project.*;

public class TTextFieldPanel extends JPanel implements ListSelectionListener,
    ActionListener,
    FocusListener
{
  // input textfields
  private TLangTextField langFields[] ;

  private TMultiLanguageEntry currentEntry ;

  private boolean showSource ;

  //private SpringLayout layout ;
  private BoxLayout layout ;

  private int activeEntry = -1 ;

  private FieldPopup popup = new FieldPopup() ;

  private TUndoHandler undoHandler = new TUndoHandler() ;

  // registered ViewUpdateListener
  private Vector vuListener = new Vector() ;

  public TTextFieldPanel()
  {
    super() ;

    showSource = TGlobal.config.isShowSource() ;

    try
    {
      layout = new BoxLayout( this, BoxLayout.Y_AXIS ) ;
      this.setLayout( layout ) ;

      // popup menu
      popup.add( new TUndoAction( undoHandler ) ) ;
      popup.addSeparator() ;
      popup.add( new TCutAction() ) ;
      popup.add( new TCopyAction() ) ;
      popup.add( new TPasteAction() ) ;

      // input text-fields
      generateFields() ;
    }
    catch ( Exception ex )
    {
      Logger.global.fine( "failure : create textfields" ) ;
      ex.printStackTrace() ;
    }
  }

  /** select the first textfield */
  public void requestFocus()
  {
    if ( langFields != null )
    {
      if ( langFields.length > 0 )
      {
        langFields[0].requestFocus() ;
        return ;
      }
    }

    super.requestFocus() ;
  }

  /** the panel structure has changed */
  public void updateStructure()
  {
    try
    {
      clear() ;
      generateFields() ;
      layout.invalidateLayout( this ) ;
      this.updateUI() ;
    }
    catch ( Exception ex )
    {
      System.out.println( "failure" ) ;
      ex.printStackTrace() ;
    }
  }

  public void markText( int start, int end )
  {
    langFields[0].markText( start, end ) ;
  }

  /** removes all components and listeners */
  private void clear()
  {
    int len = langFields.length ;
    for ( int t = 0 ; t < len ; t++ )
    {
      TLangTextField field = langFields[t] ;
      field.removeActionListener( this ) ;
      field.removeFocusListener( this ) ;
    }

    this.removeAll() ;
  }

  /** put all textfield components into the panel */
  private void generateFields()
  {
    TProjectData data = TGlobal.projects.getCurrentData() ;
    if ( data != null )
    {
      // get the number of language fields
      int len = data.getLangSize() ;

      // if default entry available counter +1
      if ( data.hasDefaultEntry() )
      {
        len++ ;
      }

      // key field
      len++ ;

      // make the array
      langFields = new TLangTextField[len] ;

      // insert key field
      TLangTextField keyfield = new TLangTextField( "key", 0, popup ) ;
      keyfield.addActionListener( this ) ;
      keyfield.addFocusListener( this ) ;
      langFields[0] = keyfield ;
//      keyfield.setMinimumSize( new Dimension( 200, 22 ) ) ;
//      keyfield.setPreferredSize( new Dimension( 200, 30 ) ) ;
      this.add( keyfield ) ;

      // generate all other language fields
      for ( int t = 1 ; t < len ; t++ )
      {
        TLangTextField field = new TLangTextField(
            data.getColumnName( t + TProjectData.KEY_COLUMN ),
            t, popup ) ;
        field.addActionListener( this ) ;
        field.addFocusListener( this ) ;
        langFields[t] = field ;
//        field.setMaximumSize( new Dimension( 1000, 22 ) ) ;
//        field.setMinimumSize( new Dimension( 200, 22 ) ) ;
//        field.setPreferredSize( new Dimension( 200, 30 ) ) ;

        langFields[t - 1].setNextFocusable( field ) ;

        this.add( field ) ;
      }

      langFields[len - 1].setNextFocusable( keyfield ) ;
    }
  }

  // update all fields with data from currentEntry
  public void repaintData()
  {
    if ( currentEntry != null )
    {
      TProjectData current = TGlobal.projects.getCurrentData() ;
      int len = langFields.length ;
      for ( int t = 0 ; t < len ; t++ )
      {
        String str = current.getTranslation( currentEntry, t ) ;
        TLangTextField field = langFields[t] ;

        if ( str == null )
        {
          str = "" ;
          field.setStatus( TLangTextField.REQUIRE_STATE ) ;
        }
        else if ( str.length() < 1 )
        {
          field.setStatus( TLangTextField.REQUIRE_STATE ) ;
        }
        else // something available
        {
          field.setStatus( TLangTextField.NOTHING_STATE ) ;
        }

        // with/without unicode
        if ( showSource )
        {
          field.setText( str ) ;
        }
        else
        {
          field.setText( Utils.get_WYSIWYG_String( str ) ) ;
        }
      }
    }
    else // no entry data available - clear all fields
    {
      int len = langFields.length ;
      for ( int t = 0 ; t < len ; t++ )
      {
        TLangTextField field = langFields[t] ;
        field.setText( "" ) ;
        field.setStatus( TLangTextField.REQUIRE_STATE ) ;
      }
    }
    GUIGlobals.oPanel.updateEntryComment( "" ) ;
  }

  public boolean isShowSource()
  {
    return showSource ;
  }

  public void setShowSource( boolean pShowSource )
  {
    this.showSource = pShowSource ;
    repaintData() ;
  }

  public Dimension getRealSize()
  {
//    System.out.println( "layout "+   layout.maximumLayoutSize(this)) ;
//    return layout.minimumLayoutSize(this) ;
    return layout.preferredLayoutSize( this ) ;
//    return layout.maximumLayoutSize(this) ;
  }

  public void setData( TMultiLanguageEntry entry )
  {
    currentEntry = entry ;
    repaintData() ;
  }

  /** put the data from TLangTextField with id <id> into the translation set */
  private void adoptData( int id )
  {
    if ( currentEntry != null )
    {
      // make undo entry
      String old = currentEntry.getTranslation( id - 1 ) ;
      undoHandler.insertUndo( old, id ) ;

      TLangTextField field = langFields[id] ;
      String str = field.getText() ;

      TProject project = TGlobal.projects.getCurrentProject() ;

      String valid = Utils.normalizeIT( str,
                                        project.getSettings().getReplaceWhitespace(),
                                        project.getSettings().getReplaceString() ) ;

      TGlobal.projects.getCurrentData().replaceTranslation(
                                                currentEntry, id, valid ) ;
      if ( showSource )
      {
        field.setText( valid ) ;
      }
      else
      {
        field.setText( Utils.get_WYSIWYG_String( str ) ) ;
      }

      currentEntry.setModifyStatus( TMultiLanguageEntry.STATE_MODIFIED ) ;
      field.setStatus( TLangTextField.NOTHING_STATE ) ;
    }
  }

  /** put the selected data from translation-set into the TLangTextFields */
  private void fetchData()
  {
    // activeEntry is set by valueChanged()
    currentEntry = ( TMultiLanguageEntry )
        TGlobal.projects.getCurrentData().getValueAt( activeEntry, 0 ) ; // column attribut has no effect
    undoHandler.changeList( currentEntry ) ;
    this.setData( currentEntry ) ;
  }

  // ViewUpdateListener -------------------------------------------------------
  // insert a listener for the "select next or prev item" action
  public void addViewUpdateListener( ViewUpdateListener listener )
  {
    if ( listener != null )
    {
      vuListener.add( listener ) ;
    }
  }

  public void removeViewUpdateListener( ViewUpdateListener listener )
  {
    if ( listener != null )
    {
      vuListener.remove( listener ) ;
    }
  }

  public void fireShowNextEntry()
  {
    for ( int t = 0, len = vuListener.size() ; t < len ; t++ )
    {
      ViewUpdateListener listener = ( ViewUpdateListener ) vuListener.get( t ) ;
      listener.showNextEntry() ;
    }
  }

  public void fireShowPrevEntry()
  {
    for ( int t = 0, len = vuListener.size() ; t < len ; t++ )
    {
      ViewUpdateListener listener = ( ViewUpdateListener ) vuListener.get( t ) ;
      listener.showPrevEntry() ;
    }
  }

  public void fireShowNextIncomplete( int id, boolean bottomUp )
  {
    for ( int t = 0, len = vuListener.size() ; t < len ; t++ )
    {
      ViewUpdateListener listener = ( ViewUpdateListener ) vuListener.get( t ) ;
      listener.showNextIncomplete( id, bottomUp ) ;
    }
  }

  // --------------------------------------------------------------------------
  // ActionListener
  public void actionPerformed( ActionEvent e )
  {
    int hash = e.getActionCommand().hashCode() ;
//      System.out.println("Action from " +e.getID() +" " +e.getActionCommand()) ;

    // enter comes only, if the data of the sender has been changed
    if ( hash == "enter".hashCode() )
    {
      adoptData( e.getID() ) ;
    }
    else if ( hash == "down".hashCode() )
    {
      this.fireShowNextEntry() ;
    }
    else if ( hash == "up".hashCode() )
    {
      this.fireShowPrevEntry() ;
    }
    else if ( hash == "next".hashCode() )
    {
      this.fireShowNextIncomplete( e.getID(), false ) ;
    }
    else if ( hash == "prev".hashCode() )
    {
      this.fireShowNextIncomplete( e.getID(), true ) ;
    }

  }

  // --------------------------------------------------------------------------
  // ListSelectionListener - JTable
  public void valueChanged( ListSelectionEvent e )
  {

    ListSelectionModel lsm = ( ListSelectionModel ) e.getSource() ;
    activeEntry = lsm.getMinSelectionIndex() ;

    //Ignore extra messages.
    if ( e.getValueIsAdjusting() )
    {
      return ;
    }
    if ( lsm.isSelectionEmpty() )
    {
      //no rows are selected
    }
    else
    {
      fetchData() ;
    }
  }

  // --------------------------------------------------------------------------
  // focus handling of a single TLangFields component
  public void focusGained( FocusEvent e )
  {
    int id = e.getID() ;
    String txt = "" ;

    if ( currentEntry != null )
    {
      TComments comments = TGlobal.projects.getCurrentData().getComments(
          currentEntry, id ) ;

      // get the first line of comments
      if ( comments != null )
      {
        txt = comments.getFormatedComment( 0 ) ;
      }
    }
    GUIGlobals.oPanel.updateEntryComment( txt ) ;

  }

  public void focusLost( FocusEvent e )
  {
    int id = e.getID() ;
    TLangTextField field = langFields[id] ;

    if ( field.isDataChanged() && ( !popup.isVisible() ) )
    {
      // ask
      if ( !TGlobal.config.confirmDataOnFocusLost() )
      {
        if ( JOptionPane.showConfirmDialog( null,
                                            "Data changed - apply ?",
                                            "question",
                                            JOptionPane.YES_NO_OPTION ) ==
             JOptionPane.YES_OPTION )
        {
          this.adoptData( id ) ;
        }
        // If the JOptionPane pops up, then no fireValueChanged() from
        // JTable arrives. Update the data manually.
        this.fetchData() ;
      }
      else // don't ask
      {
        this.adoptData( id ) ;
      }
    }
  }

}
