/**
 Popeye - Java (Language) Properties File Editor

 Copyright (C) 2006 Raik Nagel <kiar@users.sourceforge.net>
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


// created by : r.nagel 20.04.2006
//
// function : a simple service class for the handling of (x,y) coordinates
//            and its corresponding table entries/data
//
// todo     :
//
// modified :


package net.sf.langproper.gui.table ;

import java.awt.* ;

import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.*;

public class TEntryHandle
{
  private TablePanel table ;

  private int row ;
  private int col ;

  // the x,y coodinates of the table cell
  private Point point ;

  private TMultiLanguageEntry tableEntry = null ;

  public TEntryHandle(TablePanel pTable)
  {
    table = pTable ;
  }

  /** calculate the row/column for the given x,y coordinates */
  public void update( int x, int y)
  {
    point = new Point(x, y) ;
    row = table.rowAtPoint(point) ;
    col = table.columnAtPoint(point) ;

    tableEntry = table.getEntryAtRow(row) ;
  }

  public int getRow()
  {
    return row;
  }

  public int getColumn()
  {
    return col;
  }

  /** the row data */
  public TMultiLanguageEntry getRowEntry()
  {
    return tableEntry ;
  }

  /** cell data as string */
  public String getCellContent()
  {
    if (tableEntry != null)
    {
      // col_offset of the first language column into the table
      if (col >= TProjectData.COL_OFFSET)
      {
        return tableEntry.getTranslation( col-TProjectData.COL_OFFSET ) ;
      }
      else if (col == TProjectData.KEY_COLUMN)
      {
        return tableEntry.getKey() ;
      }
    }
    return "" ;
  }
}
