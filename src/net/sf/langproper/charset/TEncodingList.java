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

// created by : r.nagel 12.03.2006
//
// function : contains a list of all available encodings (Combobox Model)
//
// todo     :
//
// modified :

package net.sf.langproper.charset ;

import javax.swing.*;
import javax.swing.event.ListDataListener ;
import java.util.*;
import java.nio.charset.*;
import net.sf.langproper.*;

public class TEncodingList implements ComboBoxModel
{
  public static final TEncodingList ENCODINGS = new TEncodingList() ;

  public static final String JAVA_ENC = "java utf" ;
  public static final String HTML_ENC = "html entities" ;

  private int selectedIndex = 0 ;
  private Vector liste = new Vector() ;

  private TEncodingList()
  {
    liste.add(JAVA_ENC) ;
    if (TGlobal.DEVEL)
    {
      liste.add( HTML_ENC ) ;
    }
    liste.add(net.sf.langproper.engine.Utils.getDefaultHostEncoding()) ;

    SortedMap map = Charset.availableCharsets() ;
    if (map != null)
    {
      Collection col = map.values() ;
      for (Iterator it = col.iterator() ; it.hasNext() ;)
      {
        Charset charSet = (Charset) it.next() ;
        liste.add( charSet.displayName() ) ;
      }
    }
  }

  /** returns true, if the encodingName the "java utf escape sequence encoding" ;-) */
  public static boolean isJavaEncoding( String encodingName )
  {
    if ((encodingName != null) &&
        (encodingName.hashCode() == JAVA_ENC.hashCode()) )
    {
      return true ;
    }
    return false ;
  }

  /** returns true, if the encodingName "html entities"  */
  public static boolean isHtmlEncoding( String encodingName )
  {
    if ((encodingName != null) &&
        (encodingName.hashCode() == HTML_ENC.hashCode()) )
    {
      return true ;
    }
    return false ;
  }


  // -------------------------------------------------------------------------

  public Object getSelectedItem()
  {
    return  liste.get( selectedIndex );
  }

  public void setSelectedItem( Object anItem )
  {
    if (anItem != null)
    {
      selectedIndex = liste.indexOf( anItem ) ;
    }
  }

  public int getSize()
  {
    return liste.size() ;
  }

  public Object getElementAt( int index )
  {
    if ((index >= 0) && (index < liste.size()))
    {
      return liste.get( index ) ;
    }

    return null ;
  }

  public void addListDataListener( ListDataListener l )
  {
  }

  public void removeListDataListener( ListDataListener l )
  {
  }
}
