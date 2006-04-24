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

// created by : r.nagel 01.06.2005
//
// function : read build informations from version.info file
//
// todo     : compare 2 TBuildInfo objects and compute some infos like
//            "there is a newer/older version on server"
//
// modified :

package net.sf.versions;

import java.io.* ;
import java.util.logging.* ;
import java.net.*;

public class TBuildInfo
{
  private String BUILD_DATE = "" ;
  private String BUILD_VERSION = "" ;
  private String BUILD_NUMBER = "" ;
  private String BUILD_META = "" ;  // some description information

  // number of information which could be read from stream
  // < 0 => number of infos but error
  // > 0 => number of infos without error
  // = 0 => no infos, no error
  private int dataAvailable = 0 ;

  // numerical build number
  private int bNum = 0 ;

  public TBuildInfo(String path)
  {
    readFromFile(path) ;
  }

  public TBuildInfo(URL url)
  {
    if (url != null)
      readFromURL(url) ;
  }

  // --------------------------------------------------------------------------

  // some informations from extern build file
  private void readFromFile(String path)
  {
    BufferedReader input = null ;

    try
    {
      input = new BufferedReader(
          new InputStreamReader( getClass().getResourceAsStream( path) ), 100 ) ;
    }
    catch ( Exception e1 )
    {
//      System.err.println(e1.getMessage());
      Logger.global.info( e1.getMessage() ) ;
    }

    if (input != null)
      readFromStream( input ) ;
  }

// --------------------------------------------------------------------------

  public void readFromURL(URL url)
  {
    BufferedReader input = null ;
    try
    {
      InputStream is = url.openStream() ;
      input = new BufferedReader( new InputStreamReader( is )) ;
    }
    catch (IOException e) {}

    if (input != null)
    {
      readFromStream(input) ;
    }
  }

// --------------------------------------------------------------------------

  private void readFromStream(BufferedReader input)
  {
    dataAvailable = 0 ;

    String buf = null ;
    int sep = 0 ;
    String Key, Value ;
    boolean error = false ;

    try
    {
      BUILD_META = "" ;
      while ( ( buf = input.readLine() ) != null )
      {
        if ( buf.length() > 0 )   // no empty lines
        {
//          System.out.println( "<" +buf +">") ;
          if ( buf.charAt( 0 ) != '#' )
          { // data line, comments - first char = #
            sep = buf.indexOf( '=' ) ;
            if ( sep > 0 )
            { // = found
              Key = buf.substring( 0, sep ) ;
              Value = buf.substring( sep + 1 ) ;
              if ( Key.equals( "builddate" ) )
              {
                BUILD_DATE = Value ;
                dataAvailable++ ;
              }
              else if ( Key.equals( "build" ) )
              {
                BUILD_NUMBER = Value ;
                dataAvailable++ ;
              }
              else if ( Key.equals( "version" ) )
              {
                BUILD_VERSION = Value ;
                dataAvailable++ ;
              }
              else if ( Key.equals( "meta" ) )
              {
                BUILD_META += Value ;
                dataAvailable++ ;
              }
            }
          } // data line
        }
      } // while
    }
    catch ( IOException iex )
    {
//      System.err.println(iex.getMessage());
      Logger.global.info( iex.getMessage() ) ;
      error = true ;
    }

    try
    {
      input.close() ;
    }
    catch ( Exception e )
    {
//      System.out.println(e.getMessage());
      Logger.global.info( e.getMessage() ) ;
      error = true ;
    }

    if (error)
    {
      if (dataAvailable == 0)
        dataAvailable = -1 ;
      else
      {
          dataAvailable = dataAvailable * (-1) ;
      }
    }


    // convert build number
    try
    {
      bNum = Integer.parseInt( BUILD_NUMBER ) ;
    }
    catch (Exception ne)
    {
      bNum = 0 ;
    }
  }


  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------


  public String getBUILD_DATE()
  {
    return BUILD_DATE;
  }

  public String getBUILD_VERSION()
  {
    return BUILD_VERSION;
  }

  public String getBUILD_NUMBER()
  {
    return BUILD_NUMBER;
  }

  public String getBUILD_META()
  {
    return BUILD_META ;
  }

// --------------------------------------------------------------------------

  public int getDataStatus()
  {
    return dataAvailable ;
  }
// --------------------------------------------------------------------------

  // returns > 0, if this.buildNumber is higher then other.buildNumber
  // return < 0, this < other
  // return  0, equal
  public int isNewer(TBuildInfo other)
  {
    return this.bNum - other.bNum ;

//    return 0 ;
  }

}
