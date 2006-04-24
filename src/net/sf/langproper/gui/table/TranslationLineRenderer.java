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
// function : a renderer for the translation column of translation-table
//            handling of even/odd lines (colors) and printing the right content
//
// todo     :
//
// modified :


package net.sf.langproper.gui.table ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.table.* ;

import net.sf.langproper.engine.* ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.scanner.* ;
import net.sf.langproper.engine.project.* ;

public class TranslationLineRenderer extends JLabel implements
    TableCellRenderer
{
  private Color selectedColor = new Color( 26, 70, 159 ) ; // 0x1A469F
  private Color oddColor = new Color( 0xdfdfdf ) ;

  private Color oddMarkedColor = new Color( 0xf1c7c0 ) ;
  private Color evenMarkedColor = new Color( 0xffe1e1 ) ;

  private TableParameter param ;

  public TranslationLineRenderer( TableParameter tableParam )
  {
    param = tableParam ;
    setOpaque( true ) ; //MUST do this for background to show up.
    this.setFont( GUIGlobals.defaultLanguageFont ) ;

    // disable the html view of JLabel (works since 1.5)
    this.putClientProperty( "html.disable", Boolean.TRUE ) ;
  }

  /** override because of html handling */
  public void setText( String newValue )
  {
    super.setText( newValue ) ;

    // workaround for 1.4
    // after each setText, the html rendering must be deleted
    putClientProperty( "html", null ) ;
  }

  // --------------------------------------------------------------------------
  public Component getTableCellRendererComponent(
      JTable table, Object value,
      boolean isSelected, boolean hasFocus,
      int row, int column )
  {
    boolean marked = false ;

    if ( value != null )
    {
      TMultiLanguageEntry entry = ( TMultiLanguageEntry ) value ;

      String str = param.getModel().getTranslation( entry,
          column - TProjectData.KEY_COLUMN ) ; // first column = status, second = reference

      // set text
      if ( param.isShowSource() )
      {
        this.setText( str ) ;
      }
      else
      {
        this.setText( Utils.get_WYSIWYG_String( str ) ) ;
      }

      // generate all neccessary rendering data
      if ( !entry.isCachedDataValid() )
      {
        entry.updateCachedData( param.getModel().getAvailableLangs() ) ;
      }

      // complete/incomplete
      if ( param.getCheckAllEntries() )
      {
        // check all language versions for incomplete entries
        marked = !entry.isComplete() ;
      }
      else if ( ( param.getMarkColumnsOnly() ) && (column != 0) )
      {
        // color only the data columns
        if ( column > TProjectData.KEY_COLUMN)
        {
          marked = true ;
          if ( str != null )
          {
            if ( str.length() > 0 )
            {
              marked = false ;
            }
          }
        } else marked = false ;  // don't color the info lines
      }
      else  // color the complete line, if a visible column is empty
      {
        marked = !entry.isVisibleComplete() ; // check only the visible languages
      }

      // has this entry any comment -> paint comments icon in status column
      if ( column == 0 )
      {
        if ( entry.isErased() )
        {
          this.setIcon( GUIGlobals.trashIcon ) ;
        }
        else if ( entry.hasComment() )
        {
          this.setIcon( GUIGlobals.tableEntryCommentIcon ) ;
        }
        else if ( entry.sizeOfValidations() > 0 )
        {
          this.setIcon( GUIGlobals.tableEntryValidationInfoIcon ) ;
        }
        else
        {
          this.setIcon( null ) ;
        }
      }
      else if ( column == 1 )
      {
        this.setIcon( null ) ;
        TKeyRef keyRef = entry.getKeyRef() ;
        if ( keyRef != null )
        {
          this.setText( Integer.toString( keyRef.getReferenceCount() ) ) ;
        }
        else
        {
          this.setText( "" ) ;
        }
      }
      else
      {
        this.setIcon( null ) ;
      }
    }
    else // value empty
    {
      this.setText( "" ) ;
    }

    Color background ;

    if ( param.isExtraMarkedCell(row, column))  // mark the cell, e.g. table menu
    {
      background = Color.orange ;
    }
    else if ( !isSelected )
    {
      // mark incomplete lines
      marked = marked & param.isMarkIncomplete() ;

      if ( ( row % 2 ) == 1 ) // odd line
      {
        if ( marked )
        {
          background = oddMarkedColor ;
        }
        else
        {
          background = oddColor ;
        }
      }
      else // even line
      {
        if ( marked )
        {
          background = evenMarkedColor ;
        }
        else
        {
          background = Color.white ;
        }
      }
      this.setForeground( Color.black ) ;
    }
    else // selected line
    {
      background = selectedColor ;
      this.setForeground( Color.lightGray ) ;
    }

    this.setBackground( background ) ;

    return this ;
  }
}
