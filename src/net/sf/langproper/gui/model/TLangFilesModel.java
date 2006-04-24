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
// function : a table model for handling the language files into a table
//
// todo     :
//
// modified :


package net.sf.langproper.gui.model ;

import javax.swing.table.* ;
import net.sf.langproper.engine.*;
import net.sf.langproper.engine.project.TLanguageFile;

public class TLangFilesModel extends AbstractTableModel
{
  private TLanguageList data ;

  public TLangFilesModel( TLanguageList modelData )
  {
    data = modelData ;
  }

  public void setModelData( TLanguageList modelData )
  {
    data = modelData ;
    this.fireTableDataChanged();
  }

  /**
   * Returns the number of columns in the model.
   *
   * @return the number of columns in the model
   * @todo Implement this javax.swing.table.TableModel method
   */
  public int getColumnCount()
  {
    return 3 ;
  }

  /**
   * Returns the number of rows in the model.
   *
   * @return the number of rows in the model
   * @todo Implement this javax.swing.table.TableModel method
   */
  public int getRowCount()
  {
    if (data != null)
      return data.getRowCount() ;

    return 0 ;
  }

  public Object getValueAt( int rowIndex, int columnIndex )
  {
    Object back = "" ;
    if ( data != null)
    {
      TLanguageFile dummy = data.getData( rowIndex ) ;

      if ( dummy != null )
      {
        switch ( columnIndex )
        {
          case -1:
            if ( ( data.getDefaultLang() != null ) && ( rowIndex == 0 ) )
              back = "default" ;
            else
              back = dummy.getFullLanguageName() ;
            break ;
          case 2: // filename
            back = dummy.getFileName() ;
            break ;
          case 1: // language name
            back = dummy.getFullLanguageName() ;
            break ;
          default: // language iso code
            if ( ( data.getDefaultLang() != null ) && ( rowIndex == 0 ) )
              back = "default" ;
            else
              back = dummy.getLanguageCode() ;
        }
      }
    }
    return  back ;
  }

  public void setValueAt( Object aValue, int rowIndex, int columnIndex )
  {
//    System.out.println( "set " + rowIndex + ", " + columnIndex ) ;
    if ( data != null)
    {
      TLanguageFile dummy = data.getData( rowIndex ) ;
      if ( dummy != null )
      {
        if ( aValue != null )
        {
          Boolean value = ( Boolean ) aValue ;
          dummy.setVisible( value.booleanValue() ) ;
          data.reportDataVisibilityChanged() ;
        }
      }
    }
  }


  public String getColumnName( int col )
  {
    switch (col)
    {
      case 0 : return "iso code";
      case 1 : return "language" ;
      case 2 : return "file" ;
    }
    return "-" ;
  }

  public Class getColumnClass( int c )
  {
    return String.class ;
  }

  public boolean isCellEditable( int rowIndex, int columnIndex)
  {
    return false ;
  }

}

/*
  public Object getValueAt( int rowIndex, int columnIndex )
  {
    Object back = "" ;
    TLanguageVersionFile dummy = getData(rowIndex) ;

    if (dummy != null)
    {
       switch (columnIndex)
       {
         case -2 :  // ListModel Anfrage
           back = dummy ;
           break ;
         case -1 :
           if ( (defaultLang != null) && (rowIndex == 0))
             back = "default" ;
           else back = dummy.getFullLanguageName() ;
           break ;
         case 2 :  // visibility
           back = dummy.getVisible() ;
           break ;
         case 1 : // language name
           back = dummy.getFullLanguageName() ;
           break ;
         default :  // language iso code
           if ( (defaultLang != null) && (rowIndex == 0))
             back = "default" ;
           else back = dummy.getLanguageCode() ;
       }
    }

    return  back ;
  }

*/
/*
public void setValueAt( Object aValue, int rowIndex, int columnIndex )
{
//    System.out.println( "set " + rowIndex + ", " + columnIndex ) ;
  TLanguageVersionFile dummy = getData(rowIndex) ;
  if (dummy != null)
  {
    if (aValue != null)
    {
      Boolean data = (Boolean) aValue ;
      dummy.setVisible( data.booleanValue());
      dataVisibilityChanged = true ;
    }
  }
}
*/






