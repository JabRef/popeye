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
// function : try to detect a file encoding (charset)
//            use of IBM's icu and Mozilla's chardet algorithms
//
// todo     :
//
// modified :


package net.sf.langproper.charset ;

import java.io.* ;

import org.mozilla.intl.chardet.* ;
import com.ibm.icu.text.* ;

public class Detector
{
  public DetectorMatch check( String fileName )
  {
    DetectorMatch back = ibmCheck( fileName) ;
    if (back == null)
      back = mozillaCheck( fileName) ;

    return back ;
  }

  // --------------------------------------------------------------------------
  // run ibm's charset detection
  // --------------------------------------------------------------------------

  public DetectorMatch ibmCheck( String fileName )
  {
    DetectorMatch back = null ;

    try
    {
      File file = new File( fileName ) ;
      BufferedInputStream imp = new BufferedInputStream(
                                   new FileInputStream( file ) ) ;

      CharsetDetector detector = new CharsetDetector() ;
      detector.setText(imp) ;

      CharsetMatch match = detector.detect() ; // detectAll()

      if (match != null)
      {
        back = new DetectorMatch( match.getName(),
                                  match.getLanguage(),
                                  match.getConfidence()) ;
      }
    }
    catch (Exception e)
    {
    }

    return back ;
  }

  // --------------------------------------------------------------------------
  // run the mozilla charset detection
  // --------------------------------------------------------------------------

  private DetectorMatch mozillaCheck( String fileName )
  {
    DetectorMatch back = null ;
    nsDetector det = new nsDetector() ;

    // Set an observer...
    // The Notify() will be called when a matching charset is found.

    MyMozillaObserver observer = new MyMozillaObserver() ;

    det.Init( observer ) ;

    try
    {
      File file = new File( fileName ) ;
      BufferedInputStream imp = new BufferedInputStream(
                                    new FileInputStream( file ) ) ;

      byte[] buf = new byte[1024] ;
      int len ;
      boolean done = false ;
      boolean isAscii = true ;

      while ( (!done) && ((len = imp.read( buf, 0, buf.length)) != -1 ))
      {

        // Check if the stream is only ascii.
        if ( isAscii )
        {
          isAscii = det.isAscii( buf, len ) ;
        }

        // DoIt if non-ascii and not done yet.
        if ( !isAscii && !done )
        {
          done = det.DoIt( buf, len, false ) ;
        }
      }
      det.DataEnd() ;

      back = observer.getMatch() ;

      if (back == null) // nothing was found, try to present a probable result
      {
         String prob[] = det.getProbableCharsets() ;
         if (prob != null)
         {
           if (prob.length > 0)
           {
             back = new DetectorMatch( prob[0], "", 0) ;
           }
         }
      }

    }
    catch (Exception e)
    {
      System.out.println( "ERROR" ) ;
    }


    return back ;
  }

  // -----------------------------------------------------------------------
  /** helper class for charDet algorithm */
  private class MyMozillaObserver implements nsICharsetDetectionObserver
  {
    private DetectorMatch match ;

    /** called by nsDetector from mozillaCheck */
    public void Notify( String charset )
    {
      match = new DetectorMatch( charset, "", 0) ;
    }

    public DetectorMatch getMatch()
    {
      return match ;
    }
  }

}
