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

// created by : r.nagel 4.4.2005
//
// function : contains all comments for an unique translation
//
// todo     :
//
//
// modified : 21.02.1006
//            - insert the handling of blank lines before/after a comment
//              -> blank lines in a comment block will be removed

package net.sf.langproper.engine ;

import java.util.*;
import java.util.logging.Logger;

public class TComments
{
  private int before = 0 ;
  private int after = 0 ;
  private Vector comment ;

  public TComments()
  {
    comment = new Vector() ;
  }

  /** returns the "raw" comment line at index <index> */
  public String getComment(int index)
  {
    if (index < comment.size() ) {
      return (String) comment.get(index) ;
    }

    return "" ;
  }

  /** returns the formated comment line at index <code>index</code>
   *  -> the line starts with a '#' sign,
   *  all leading and trailing whitespaces are deleted
   * */
  public String getFormatedComment(int index)
  {
    String back = getComment(index).trim() ;
    if (back.length() > 0)
    {
      if ( back.charAt( 0 ) != '#' )
        return ( "#" + back ) ;

      return back ;
    }

    return "#" ;
  }

  /** erase all comments. */
  public void clear()
  {
    comment.clear() ;
  }

  /** insert a comment line  */
  public void addComment(String value)
  {
    if (value != null)  // we have something
    {
      String trimmed = value.trim() ;
      if (trimmed.length() > 0)
      {
        if (trimmed.charAt(0) == '#' ) // comment sign
        {
          comment.add( value ) ;
          after = 0 ;
          return ;
        }
        else
        {
          Logger.global.warning("no comment! >" + value ) ;
        }
      }
    }

    // not a comment -> empty line, increment the counters
    if (comment.size() > 0)
    {
      after++ ;
    }
    else
    {
      before++ ;
    }
  }

  /** returns the number of comment lines. */
  public int getSize()
  {
    return comment.size() ;
  }

  public boolean hasComments()
  {
    if (comment.size() > 0)
      return true ;

    return false ;
  }

  // -------------------------------------------------------------------------
  public int getBlanksBefore()
  {
    return before ;
  }

  public int getBlanksAfter()
  {
    return after ;
  }

  // --------------------------------------------------------------------------

  /** find the same comment line */
  public int find(String data)
  {
    // compare the trimmed comments
    int back = 0 ;

    int dummy = data.trim().hashCode() ;
    for (Enumeration myEnum = comment.elements() ; myEnum.hasMoreElements() ;)
    {
      int hash = ((String) myEnum.nextElement()).trim().hashCode() ;
      if (hash == dummy)
      {
        return back ;
      }
      back++ ;
    }

    return -1 ;
  }


}
