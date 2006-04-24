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
// function : a jtable with codeinsight results
//
// todo     :
//
// modified :

package net.sf.langproper.gui.scanner ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.table.* ;

import net.sf.langproper.scanner.* ;
import net.sf.langproper.gui.*;

public class TKeyRefTable extends JTable
{
  private TKeyRefList model ;

  private KeyRefRenderer renderer = new KeyRefRenderer();

  public TKeyRefTable(TKeyRefList dataModel)
  {
    super( dataModel ) ;
    model = dataModel ;
  }

  public TKeyRefTable()
  {
    super() ;
    model = null ;
  }

  public TableCellRenderer getCellRenderer( int row, int column )
  {
//        if ( ( row == 0 ) && ( column == 0 ) )
    {
      return renderer ;
    }
//        return super.getCellRenderer( row, column ) ;
  }

  public void setModel(TKeyRefList dataModel)
  {
    super.setModel( dataModel );
    model = dataModel ;
  }


  // --------------------------------------------------------------------------
  // Table Renderer
  // handling of even/odd lines (colors) and printing the right content
  // --------------------------------------------------------------------------

  public class KeyRefRenderer
      extends JLabel implements TableCellRenderer
  {
    private Color selectedColor = new Color( 26, 70, 159 ) ; // 0x1A469F
    private Color oddColor = new Color( 0xdfdfdf ) ;

    public KeyRefRenderer()
    {
      setOpaque( true ) ; //MUST do this for background to show up.
      this.setFont( GUIGlobals.defaultLanguageFont ) ;

      // disable the html view of JLabel (works since 1.5)
      this.putClientProperty( "html.disable", Boolean.TRUE ) ;
    }

    public void setText( String newValue )
    {
      super.setText( newValue ) ;

      // workaround for 1.4
      // after each setText, the html rendering must be deleted
      putClientProperty( "html", null ) ;
    }

    public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus,
        int row, int column )
    {
      if ( value != null )
      {
        String str = (String) value ;

        this.setText( str ) ;
      }
      else // value empty
      {
        this.setText( "" ) ;
      }

      Color background ;

      if ( !isSelected )
      {
        if ( ( row % 2 ) == 1 ) // odd line
        {
            background = oddColor ;
        }
        else // even line
        {
            background = Color.white ;
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

}
