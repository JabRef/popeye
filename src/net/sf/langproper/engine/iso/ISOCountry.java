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

// created by : r.nagel 21.03.2006
//
// function : contains an iso country entry
//
// todo     :
//
// modified :

package net.sf.langproper.engine.iso ;

public class ISOCountry
{
  private String iso ;
  private String iso3 ;
  private String name ;
  private String extension ; // an extension like "Rebublic of"
  private String full ;

  private boolean old = false ;

  public ISOCountry( String id2, String id3, String shortName, String ext )
  {
    if ( id2 != null )
    {
      iso = id2.toUpperCase() ; // 2 letter iso
    }
    else
    {
      iso = "" ;
    }

    iso3 = id3 ; // 3 letter iso

    name = shortName ;

    if ( ext == null )
    {
      extension = "" ;
    }
    else
    {
      extension = ext.trim() ;
    }

    // some name validations
    if ( name == null )
    {
      name = "" ;
      full = "" ;
    }
    else
    {
      name = name.trim() ;
      full = name + " " + extension ;
      int len = name.length() ;
      if ( len > 1 )
      {
        if ( name.charAt( len - 1 ) == ',' )
        {
          name = name.substring( 0, len - 1 ) ;
          full = extension + " " + name ;
        }
      }
    }

  }

  /** returns the 2-letter Country iso code */
  public String getCountryIso2()
  {
    return iso ;
  }

  /** returns the full iso Country name */
  public String getCountryName()
  {
    return full ;
  }

  /** It's a withdrawn codec */
  public boolean isOld()
  {
    return old ;
  }

  /** It's a withdrawn codec */
  public void setOld( boolean pOld )
  {
    this.old = pOld ;
  }

  // --------------------------------------------------------------------------
  public String toString()
  {
    String back = full ;
    if ( old )
    {
      back += " (old version) " ;
    }
    back += " " + iso ;

    return back ;
  }

}
