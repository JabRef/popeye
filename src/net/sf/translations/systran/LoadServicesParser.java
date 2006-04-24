/*
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

// created by : r.nagel 07.07.2005
//
// function : parse a file and extract the "language to language" informations
//
// todo     :
//
//
// modified :
package net.sf.translations.systran;

import java.util.* ;

import javax.swing.text.* ;
import javax.swing.text.html.* ;
import net.sf.translations.*;

public class LoadServicesParser extends HTMLEditorKit.ParserCallback
{
  private int selection = 0 ;
//  private String optionText = "" ;
//  private boolean logText = false ;

  private TServiceList services ;

  public LoadServicesParser( TServiceList sList)
  {
    services = sList ;
  }

  /**
   * Invoked when text in the html document is encountered. Based on
   * the current state, this will either: do nothing
   * (state == NO_ENTRY),
   * create a new BookmarkEntry (state == BOOKMARK_ENTRY) or
   * create a new
   * BookmarkDirectory (state == DIRECTORY_ENTRY). If state is
   * != NO_ENTRY, it is reset to NO_ENTRY after this is
   * invoked.
   */
  public void handleText( char[] data, int pos )
  {
//    System.out.println( "TEXT " ) ;
/*
     if (logText)
     {
       String dummy = new String(data) ;
       System.out.println( "#" +dummy) ;
     }
*/
  }

  /**
   * Invoked when a start tag is encountered. Based on the tag
   * this may update the BookmarkEntry and state, or update the
   * parentDate.
   */
  public void handleStartTag( HTML.Tag t, MutableAttributeSet a,
                              int pos )
  {
    if ( t == HTML.Tag.SELECT)
    {
      Object value = getValueOfAttribute(a, "name") ;
      if (value != null)
      {
        if (value.toString().hashCode() == "systran_lp".hashCode())
        {
          selection++ ;
//          System.out.println( "START " + t.toString() ) ;
        }
      }
    }
    else if (selection > 0)
    {
      if (t == HTML.Tag.OPTION)
      {
        String str = (String) getValueOfAttribute(a, "value") ;
        insert(str) ;
//        System.out.println( "option <" +str +">") ;
//        logText = true ;
      }
    }
  }

  /**
   * Invoked when the end of a tag is encountered. If the tag is
   * a DL, this will set the node that parents are added to the current
   * nodes parent.
   */
  public void handleEndTag( HTML.Tag t, int pos )
  {
    if ( t == HTML.Tag.SELECT )
    {
      selection-- ;
    }
    else if (selection > 0)
    {
      if (t == HTML.Tag.OPTION)
      {
//        logText = false ;
      }
    }
  }


  // find a attribute with name <name> and returns its value
  private Object getValueOfAttribute( MutableAttributeSet a, String name)
  {
    boolean notFound = true ;
    Object back = null ;

    Enumeration myEnum = a.getAttributeNames() ;
    while( myEnum.hasMoreElements() && notFound )
    {
      Object dummy = myEnum.nextElement() ;
      if (dummy.toString().hashCode() == name.hashCode())
      {
        notFound = false ;
        back = a.getAttribute(dummy) ; // value of
      }
    }

    return back ;
  }


  private void insert(String str)
  {
    if (str != null)
    {
      int l = str.indexOf("_") ;
      String s1 = str.substring(0, l) ;
      String s2 = str.substring(l+1) ;
      TService ser = new TService(s1, s2) ;
      services.add(ser);
    }
  }

}
