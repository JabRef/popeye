/*
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


// created by : r.nagel 27.06.2005
//
// function : simple undo mechanism
//
// todo     :
//
// modified :

package net.sf.langproper.gui.fields ;

import java.util.*;

public class TUndoHandler
{
  private class UndoData
  {
    private int id ;       // id of textfield
    private String text ;  // text

    public UndoData(int i, String s)
    {
      id = i ;
      text = s ;
    }
  }

  private LinkedList list = null ;

  private HashMap hashmap = new HashMap() ;

  // identifier for active list, only internal use
  private Object activeListId = null ;

  /** perform a undo operation for textfield <field> */
  public void undo(IDTextField field)
  {
    // no active list
    if (list == null)
      return ;

    int otherID = field.getMyID() ;
    int index = 0 ;
    boolean notFound = true ;
    ListIterator iterator = list.listIterator() ;

    // search for undo's
    while (iterator.hasNext() && notFound)
    {
      UndoData data = (UndoData) iterator.next() ;

      // take the first undo for the component with id <otherID>
      if (data.id == otherID)
      {
        field.setText( data.text );
        field.setDataChanged(true);
        notFound = false ;
        list.remove(index) ;

      }
      else index++ ;
    }

  }

  /** register an undo */
  public void insertUndo(String undoText, int id)
  {
    // if a list available ?
    if (list == null)
    {
      // an identifier for the new list must be exist
      if (activeListId != null)
      {
        list = new LinkedList() ;
        hashmap.put(activeListId, list) ;
      }
    }

    // 2nd test, nesessary!
    if (list != null)
      list.addFirst( new UndoData(id, undoText ) );
  }

  /** load the undo-list for object <obj> */
  public void changeList( Object obj )
  {
    // try to find an old undo-list
    Object dummy = hashmap.get( obj ) ;

    // set the active list
    if (dummy != null)
      list = (LinkedList) dummy ;
    else
      list = null ;

    // remember the identifier for insertUndo
    activeListId = obj ;
  }
}
