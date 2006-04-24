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
// function : contains all rendering flags for the table of translations
//
// todo     :
//
// modified :


package net.sf.langproper.gui.table ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;

public class TableParameter
{
  private boolean showSource ;
  private boolean markIncomplete ;

  private boolean checkAllEntries = true ;
  private boolean markSingleColumns = false ;

  private int markedCellRow = -1 ;
  private int markedCellCol = -1 ;

  private TProjectData myModel ;

  public TableParameter()
  {
    updateParameter() ;
  }

  public void updateParameter()
  {
    showSource = TGlobal.config.isShowSource() ;
    markIncomplete = TGlobal.config.isMarkIncomplete() ;

    if ( TGlobal.config.getMarkEntriesMode().hashCode() ==
         TGlobal.config.MARK_ALL_ENTRIES.hashCode() )
    {
      checkAllEntries = true ;
      markSingleColumns = false ;
    }
    else
    {
      checkAllEntries = false ;
      if ( TGlobal.config.getMarkEntriesMode().hashCode() ==
           TGlobal.config.MARK_COLUMNS_ONLY.hashCode() )
      {
        markSingleColumns = true ;
      }
      else
      {
        markSingleColumns = false ;
      }
    }
  }

  // --------------------------------------------------------------------------
  public void setShowSource( boolean pShowSource )
  {
    showSource = pShowSource ;
  }

  public boolean isShowSource()
  {
    return showSource;
  }

  // --------------------------------------------------------------------------
  public void setMarkIncomplete( boolean pMarkIncomplete )
  {
    markIncomplete = pMarkIncomplete ;
  }

  public boolean isMarkIncomplete()
  {
    return markIncomplete;
  }

  // --------------------------------------------------------------------------
  public TProjectData getModel()
  {
    return myModel;
  }

  public void setModel(TProjectData pMyModel)
  {
    this.myModel = pMyModel;
  }

  // --------------------------------------------------------------------------
  /** check all entries for marking ... */
  public boolean getCheckAllEntries()
  {
    return checkAllEntries;
  }

  public void setCheckAllEntries(boolean pCheckAllEntries)
  {
    this.checkAllEntries = pCheckAllEntries;
  }

  // --------------------------------------------------------------------------

  /** only visible and incomplete columns should be marked */
  public boolean getMarkColumnsOnly()
  {
    return this.markSingleColumns ;
  }

  // --------------------------------------------------------------------------

  /** mark a cell, only one cell at the same time is supported. To unmark a
   *  cell row = -1 & col = -1
   */
  public void setMarkedCell( int row, int column)
  {
    // save the old values
    int r = markedCellRow ;
    int c = markedCellCol ;

    markedCellRow = row ;
    markedCellCol = column ;

    // workaround for java < 1.5
    // sometimes the invisble command arrives after the popup is showing again
    // -> make an older selection invisble per default
    if (r != -1)
    {
      myModel.fireTableCellUpdated(r, c);
    }

    if ( (row != -1) && (column != -1))
    {
      myModel.fireTableCellUpdated( row, column ) ;
    }

  }

  /** is it the marked cell ? */
  public boolean isExtraMarkedCell( int row, int column)
  {
    if ( (markedCellRow == row) && (markedCellCol == column))
      return true ;

    return false ;
  }

}
