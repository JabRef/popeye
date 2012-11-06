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
// function : central configuration instance of non-graphical application properties
//
// todo     :
//
// modified : 08.08.2005
//             load/save application settings
//            1.06.2005
//             handling of build number, date and version from extern file

package net.sf.langproper ;

import java.io.* ;
import java.util.logging.* ;

import net.sf.langproper.config.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.versions.* ;
import net.sf.langproper.scanner.* ;

public class TGlobal
{
  // a "under development" flag
  public static final boolean DEVEL = false ;

  public static final String RESOURCE_ROOT = "/resource" ;
  public static final String FLAG_ROOT = "/resource/images/flags/" ;
  public static final String HELP_ROOT = "/resource/doc/" ;
  public static final String LANGUAGES_FILENAME = "iso639.txt" ;
  public static final String COUNTRY_FILENAME = "iso3166.txt" ;
  public static final String HTML_ENC_FILENAME = "html.txt" ;
  public static final String LOGGER_PROPS_FILENAME = "logger.properties" ;

//  public static final String SERVER_LAST_VERSION_URL = "http://popeye.sourceforge.net/downloads/version.info" ;
//  public static final String SERVER_LAST_VERSION_URL = "http://popeye.sourceforge.net/version.php" ;
  public static final String SERVER_LAST_VERSION_URL = "http://koppor.github.com/popeye/version.info" ;

  public static final String DEFAULT_LOAD_PATH = "./" ;

  public static final String APPLICATION_NAME = "Popeye" ;
  public static String VERSION_STRING = "devel version" ; // DEVELopment VERSION

  // the text in language properties files
  public static final String INFO_COMMENT_TEXT = "#! created/edited by " ;

  // home of popeye
  public static final String PROJECT_MAIN_URL = "https://github.com/koppor/popeye" ;
  public static final String PROJECT_HOST = "github" ;

  public static final String PROJECT_FILE_EXTENSION = "ppf" ; // Popeye Project File
  public static final String PROJECT_FILE_EXTENSION_DESC = "project file" ; // Popeye Project File

  // project managment
  public static final TProjectManager projects = new TProjectManager() ;

  public static final TProjectTools tools = new TProjectTools() ;

  public static final TUserPreferences config = new TUserPreferences() ;

  // source code scannner, used by the codeinsight feature
  public static final Scanner codeScanner = new Scanner() ;

  // Runtime informations ---------------------------------------------------

  // information about version
  private TBuildInfo build ;

  // the runtime instance
  // after the static variables, because these things were need by the constructor
  public static final TGlobal runtime = new TGlobal() ;

  // --------------------------------------------------------------------------

  private TGlobal()
  {
    LogManager logMan = LogManager.getLogManager() ;

    try
    {
      InputStream is = TGlobal.class.getResourceAsStream( RESOURCE_ROOT + "/" +
          LOGGER_PROPS_FILENAME ) ;
      if ( is != null )
      {
        logMan.readConfiguration( is ) ;
      }
      else // no logging config => turn logging off
      {
        System.out.println( "logger config not readable" ) ;
        Logger.global.setLevel( Level.OFF ) ;
      }
    }
    catch ( Exception ex )
    {
      System.out.println( "logger config not readable" ) ;
      ex.printStackTrace() ;
      Logger.global.setLevel( Level.OFF ) ;
    }
    build = new TBuildInfo( RESOURCE_ROOT + "/version.info" ) ;

    if ( DEVEL )
    {
      VERSION_STRING = "devel version " + build.getBUILD_VERSION() ;
    }
    else
    {
      VERSION_STRING = "version " + build.getBUILD_VERSION() ;
    }
  }

  // --------------------------------------------------------------------------

  public TBuildInfo getCurrentBuildInfo()
  {
    return build ;
  }

  // --------------------------------------------------------------------------
  public boolean isJava5()
  {
    StringBuffer buffer = new StringBuffer( System.getProperty( "java.version" )) ;

    // buggy for all x.0 - x.4 version (whereby x >= 2)
    if (  (buffer.charAt(0) >= '1') && (buffer.charAt(2) >= '5') )
    {
      return true ;
    }

    return false ;
  }
}
