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
// function : A "translation class" for expanding the 2-letter iso country code
//            to the full name
//
// todo     :
//
// modified :

/*
   At the end of the iso file, there is a "withdraw" section with old and
   unused iso codecs.
 */

package net.sf.langproper.engine.iso ;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import net.sf.langproper.* ;

public class TCountryNames
{
  public static TCountryNames runtime = new TCountryNames();

  /** contains all 2 letter iso codes and it's full name */
  private ArrayList liste = new ArrayList() ;

  /** language code file found and loaded */
  private boolean availStatus = false ;

  public TCountryNames()
  {
    loadFullCountryNames() ;
  }

  public boolean isAvailable()
  {
    return availStatus ;
  }

  public int size()
  {
    return liste.size() ;
  }

  private void loadFullCountryNames()
  {
    // generate stream from jar file resource
    URL res = TGlobal.class.getResource(TGlobal.RESOURCE_ROOT + "/" +TGlobal.COUNTRY_FILENAME) ;
    InputStream inStream = null ;

    if (res != null)
    {
      JarURLConnection langRes = null ;
      try
      {
        langRes = ( JarURLConnection ) res.openConnection() ;
      }
      catch ( Exception e )
      {
        System.out.println( e ) ;
        return ;
      }
      try
      {
        inStream = langRes.getInputStream() ;
      }
      catch ( Exception e )
      {
        System.out.println( e ) ;
        return ;
      }
    }
    else
    {
      // inStream = this.getClass().getResourceAsStream(TGlobal.RESOURCE_ROOT + "/" +TGlobal.COUNTRY_FILENAME);
    }

    if (inStream == null)  // stream not empty
    {
      availStatus = false ;

      if (TGlobal.DEVEL)
      {

        try  // JBuilder IDE hack ;-)
        {
          inStream = new FileInputStream(
              "src/resource/" + TGlobal.COUNTRY_FILENAME ) ;
        }
        catch ( Exception e2 )
        {
//          e2.printStackTrace();
        }
      }
      else return ;
    }

    String line ;
    try
    {
      BufferedReader input = new BufferedReader(
             new InputStreamReader( inStream), 1000) ;
//              new FileInputStream( fileName ) ), 1000 ) ;
      while ( input.ready() )
      {
        line = input.readLine().trim() ;

        // no comment line
        if ( (line.length() > 0) && (line.charAt(0) != '#'))
        {
           String fields[] = line.split("\\s++") ;
           if (fields.length > 3)
           {
             String iso2 = fields[0] ;

             // if any ISO code available
             if ((iso2 != null) && (iso2.length() > 0))
             {
               // create and insert the entry
               ISOCountry country = new ISOCountry( iso2,
                   fields[1],
                   fields[3],
                   parse( fields[3], line ) ) ;

               // is a previous entry is found, mark the current entry as withdrawn (old)
               String f = getCountryName( iso2 ) ;
               if (f != "")
               {
                 country.setOld(true);
               }
               liste.add( country ) ;
             }
           }
        }
      }
      availStatus = true ;
    }
    catch (Exception e)
    {
      availStatus = false ;

      System.out.println(e) ;
      e.printStackTrace();
    }
  }

  // --------------------------------------------------------------------------

  // extract the additional country informations
  private String parse( String name, String full)
  {
    String back = "" ;
    int offset = full.indexOf(name) ;
     // found
    if (offset > -1)
    {
      offset += name.length() ;
      if (offset < full.length())
      {
        back = full.substring(offset) ;
      }
    }

    return back ;
  }
  // --------------------------------------------------------------------------

  /**
   * getCountryName
   *
   * @param id String, the 2 letter iso country code
   * @return If the 2 letter iso code was found, it returns the full country
   *   name (else an empty String).
   */
  public String getCountryName(String iso2)
  {
    String back = "" ;

    if (iso2 != null)
    {
       boolean found = false ;

       // normalize to capital letters
       int hash = iso2.toUpperCase().hashCode() ;
       ListIterator it = liste.listIterator() ;
       while ( (it.hasNext()) && (!found))
       {
         ISOCountry dummy = (ISOCountry) it.next() ;
         if ( dummy.getCountryIso2().hashCode() == hash )
         {
           // try the find a current code
           found = !dummy.isOld() ;
           back = dummy.getCountryName() ;
         }
       }
    }

    return back ;
  }



}
