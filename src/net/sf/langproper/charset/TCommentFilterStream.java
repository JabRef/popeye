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

// created by : r.nagel 01.09.2005
//
// function : stream filter, hide all comments (#)
//
// todo     :
//
// modified :

package net.sf.langproper.charset ;

import java.io.* ;

public class TCommentFilterStream extends InputStream
{
  private InputStream inStream ;
  private boolean comment = false ;

  public TCommentFilterStream( InputStream stream )
  {
    super() ;
    inStream = stream ;
  }

  public int read() throws IOException
  {
    boolean weiter = false ;
    int back = 0 ;

    while ( !weiter )
    {
      back = inStream.read() ;

      if (back > -1)
      {

        if ( back != '#') // no comment sign
        {
          if (comment)  // comment found in previous loop
          {
            if (back < 32)  // newline ?
            {
              comment = false ;
              weiter = true ;
            }
          }
          else  // no comment and valid sign
          {
            weiter = true ;
          }
        }
        else  // comment sign found
        {
          comment = true ;
          weiter = false ;
        }
      }
      else weiter = true ;  // end of stream
    }
//    System.out.print( (char) back ) ;
    return back ;
  }
}
