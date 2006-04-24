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

// created by : r.nagel 12.09.2005
//
// function : contains a list of key references from sources
//
// todo     :
//
// modified :

package net.sf.langproper.scanner ;

import java.util.*;
import javax.swing.table.*;
import net.sf.langproper.engine.Utils;
import net.sf.langproper.TGlobal;
import net.sf.langproper.engine.project.*;

public class TKeyRefList extends AbstractTableModel
{
  private Hashtable refs = new Hashtable() ;

  private Vector list = new Vector() ;

  private boolean listInvalid = false ;

  private TProjectSettings settings ;


  public TKeyRefList( TProjectSettings projectSettings)
  {
    settings = projectSettings ;
  }

  public TKeyRefList()
  {
    settings = new TProjectSettings() ;
  }

  /** insert a valid project settings object */
  public void setProjectSettings( TProjectSettings projectSettings)
  {
    settings = projectSettings ;
  }

  /** insert a new key and its reference */
  public void addKey( String key, String filename, int line, int column)
  {
    if (key != null)
    {
      String str = Utils.normalizeIT( key,
                                      settings.getReplaceWhitespace(),
                                      settings.getReplaceString() ) ;

//      System.out.println( "#" +str +"#" ) ;

      TKeyRef kRef = ( TKeyRef ) refs.get( str ) ;
      if (kRef == null)
      {
        kRef = new TKeyRef(str);
        refs.put(str, kRef) ;
      }

      kRef.addReference(filename, line, column);
      listInvalid = true ;
    }
  }

  /** rebuild the tablemodel data (filter) */
  public void rebuild()
  {
    list.clear();
    for( Enumeration myEnum = refs.elements(); myEnum.hasMoreElements() ;)
    {
      list.add( myEnum.nextElement() );
    }
    listInvalid = false ;
  }

  /** return the number of different key references */
  public int getSize()
  {
    return refs.size() ;
  }

  /** remove all references and other data */
  public void clear()
  {
    refs.clear();
    list.clear();
    listInvalid = false ;
  }

  /** return a key reference or null for the parameter <key> */
  public TKeyRef getKeyRef( String key )
  {
    return ( TKeyRef ) refs.get( key ) ;
  }

  // --------------------------------------------------------------------------
  // AbstractTableModel -------------------------------------------------------
  // --------------------------------------------------------------------------

  public int getColumnCount()
  {
    return 2 ;
  }

  public String getColumnName( int col )
  {
    if (col == 1)
    {
      return "references" ;
    }

    return "key" ;
  }


  // same function like getSize()
  public int getRowCount()
  {
    return list.size() ;
  }

  public Object getValueAt( int rowIndex, int columnIndex )
  {
    String back = null ;

    if (listInvalid)
    {
      rebuild() ;
    }

    TKeyRef keyRef = (TKeyRef) list.get(rowIndex) ;

    switch (columnIndex)
    {
      case 1:
        back = Integer.toString( keyRef.getReferenceCount() ) + "("
               + Integer.toString( keyRef.getReferenceFileCount() ) +")" ;
        break ;
      default:
        back = keyRef.getKey() ;
    }


    return back ;
  }

}
