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


// created by : r.nagel 12.07.2005
//
// function : basic class for a table data sorter
//
// todo     :
//
//
// modified :

package net.sf.langproper.engine.sort ;

import net.sf.langproper.engine.TMultiLanguageEntry;

public abstract class TSorter implements java.util.Comparator

{
  // a short description
  public abstract String getDescription() ;

  public abstract String getToolTip() ;

  // compares 2 entries
  public abstract int perform( TMultiLanguageEntry p1, TMultiLanguageEntry p2) ;

  // comparator interface
  public int compare( Object o1, Object o2 )
  {
    TMultiLanguageEntry p1 = null ;
    TMultiLanguageEntry p2 = null ;

    try
    {
      p1 = ( TMultiLanguageEntry ) o1 ;
      p2 = ( TMultiLanguageEntry ) o2 ;
    }
    catch ( Exception e )
    {}

    int back = 0 ;

    if ( ( p1 != null ) && ( p2 != null ) )
    {
      back = perform(p1, p2) ;
    }

    return back ;

  }

  // from Comparator Interface - not used
  public final boolean equals( Object obj )
  {
    return false ;
  }

}
