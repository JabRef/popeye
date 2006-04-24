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


// created by : r.nagel 15.09.2005, extracted from TablePanel (old TPropTable)
//
// function : table with translations
//
// todo     :
//
// modified :

package net.sf.langproper.gui.table ;

import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.table.* ;

import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;

public class TDataTable extends JTable
{
  private TableParameter param ;

  // renderer instance
  private TranslationLineRenderer renderer ;

  public TDataTable( TableParameter tableParam)
  {
    super( tableParam.getModel() ) ;

    param = tableParam ;

    renderer = new TranslationLineRenderer(param) ;
  }

  public void setModel( TProjectData model )
  {
    super.setModel( model );
    param.setModel( model );
  }

  /** mark a cell, only one cell at the same time is supported. To unmark a
   *  cell row = -1 & col = -1
   */
  public void setMarkedCell( int row, int column)
  {
    param.setMarkedCell(row, column);
  }


  // --------------------------------------------------------------------------


  public TableCellRenderer getCellRenderer( int row, int column )
  {
//        if ( ( row == 0 ) && ( column == 0 ) )
    {
      return renderer ;
    }
//        return super.getCellRenderer( row, column ) ;
  }

  //Implement table cell tool tips.
  public String getToolTipText( MouseEvent e )
  {
    String tip = null ;
    java.awt.Point p = e.getPoint() ;
    int rowIndex = rowAtPoint( p ) ;
    int colIndex = columnAtPoint( p ) ;
    int realColumnIndex = convertColumnIndexToModel( colIndex ) ;

    if ( realColumnIndex == 0 ) // comments
    {
      TMultiLanguageEntry entry = ( TMultiLanguageEntry )
                                 param.getModel().getValueAt( rowIndex, 0 ) ;
      int t = entry.sizeOfValidations() ;
      if ( t < 1 )
      {
        tip = null ;
      }
      else if ( t > 1 )
      {
        tip = Integer.toString( t ) + "validation hints" ;
      }
      else
      {
        tip = "1 validation hint" ;
      }

      if ( entry.hasComment() )
      {
        if ( tip == null )
        {
          // tip = "comment available" ;
          tip = "comment available [" + entry.getCommentsInfo() + "]" ;
        }
        else
        {
          tip += "and a comment available" ;
        }
      }
    }
    else if (realColumnIndex == 1)
    {
      TMultiLanguageEntry entry = ( TMultiLanguageEntry )
                                 param.getModel().getValueAt( rowIndex, 0 ) ;
      if (entry != null)
      {
//        if (entry.getKeyRef().getReferenceCount() > 0)
        {
          tip = "number of references found in source code" ;
        }
      }
      else tip = null ;
    }
    else
    {
      //You can omit this part if you know you don't
      //have any renderers that supply their own tool
      //tips.
      tip = super.getToolTipText( e ) ;
    }
    return tip ;
  }


}
