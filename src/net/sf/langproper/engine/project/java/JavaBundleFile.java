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

// created by : r.nagel 31.08.2005
//
// function : name handling of one properties language file for a Java Bundle
//
// todo     : not conform filenames cannot parsed
//
// modified : 31.08.2005 r.nagel
//            basic functionality comes from the old TLanguageVersionFile
//            (see TLanguageFile)
//            31.08.2005 r.nagel
//            insert handling of variants

package net.sf.langproper.engine.project.java ;

import java.io.* ;

import net.sf.langproper.engine.iso.* ;
import net.sf.langproper.engine.project.* ;

public class JavaBundleFile extends TLanguageFile
{
  private File fullName ;
  private String prefix ; // all characters before _ => filename
  private String suffix ; // extension ( all characters after . (incl ".") )
  private String lang ; // 2 letter language ID
  private String country ; // 2 letter country ID
  private String variant ; // 2 letter variant ID
  private String version ; // lang, country and variant ID (the language part of filename)

  public JavaBundleFile( File file )
  {
    init( file ) ;
  }

  public JavaBundleFile( JavaBundleFile other,
                         String isoLang,
                         String isoCountry,
                         String isoVariant,
                         String defaultPath )
  {
    String path ;

    if (other != null)
    {
      prefix = other.prefix ;
      suffix = other.suffix ;
      path = other.getFileHandle().getParent() + "/" ;
    }
    else
    {
      prefix = "file" ;
      suffix = ".properties" ;
      path = "./" ;
    }

    // use the defaultPath if available
    if (defaultPath != null)
    {
      if (defaultPath.length() > 0)
      {
        path = defaultPath +"/" ;
      }
    }

    lang = isoLang ;
    country = isoCountry ;
    variant = isoVariant ;

    // build the language part of filename
    version = "language" ;

    if (lang != null)
    {
      if (lang.length() > 0)
      {
        version = lang ;
        if ( country != null )
        {
          if (country.length() > 0)
          {
            version = version + "_" + country ;
            if ( variant != null )
            {
              if (variant.length() > 0)
              {
                version = version + "_" + variant ;
              }
            }
          }
        }
      }
    }

    fullName = new File(path + prefix +"_" +version + suffix) ;
  }

  private void init( File file )
  {
    fullName = file ;
    String fileName = fullName.getName() ;

    int i1 = fileName.indexOf( '_' ) ;
    int i2 = fileName.indexOf( '.', -1 ) ;

    prefix = "" ;
    suffix = "" ;

    if ( i1 > 0 ) // has _ => filename with language extension
    {
      prefix = fileName.substring( 0, i1 ) ;
      if ( i2 > 0 )
      {
        suffix = fileName.substring( i2 ) ;
        version = fileName.substring( i1 + 1, i2 ) ;
      }
      else // no valid suffix
      {
        version = fileName.substring( i1 + 1 ) ;
      }
    }
    else // no _ => no language extension
    {
      version = "" ;
      if ( i2 > 0 ) // only . in filename => no language extension
      {
        prefix = fileName.substring( 0, i2 ) ;
        suffix = fileName.substring( i2 ) ;
      }
      else // no _ and . => filename without fileextension like .properties
      {
        prefix = fileName ;
      }
    }

    if ( version.length() > 0 )
    {
      // full structure "_languageID_countryID" = version

      i1 = version.indexOf( '_' ) ;
      if ( i1 > 1 ) // ignore any leading _
      {
        country = version.substring( i1 + 1 ) ;
        lang = version.substring( 0, i1 ) ;
        i1 = country.indexOf("_") ;
        if ( i1 > 1)  // variant available
        {
          String dummy = country ;
          country = dummy.substring(0, i1) ;
          variant = dummy.substring(i1+1) ;
        }
      }
      else
      {
        lang = version ;
      }
    }
    //    System.out.println("filename #" +fileName +"# lang =" +lang +":") ;
  }

  // check's if the filename is equal
  public boolean isEqual( TLanguageFile other )
  {
    boolean back = false ;

    String str = other.getFileName() ;
    if ( str != null )
    {
      if ( str.hashCode() == this.getFileName().hashCode() )
      {
        back = true ;
      }
    }
    return back ;
  }

  // check's if the filename is a bundle filename (name and suffix equal)
  public boolean isBundleName( TLanguageFile other )
  {
    boolean back = false ;

    String dummy = other.getBaseName() ;
    if ( prefix.hashCode() == dummy.hashCode() )  // name equal
    {
      dummy = other.getFileExtension() ;
      if ( suffix.hashCode() == dummy.hashCode() ) // suffix (.properties)
      {
        back = true ;
      }
    }

    return back ;
  }

  /** check, if the File <other> has the same language definition
   * (language, country, variant) */
  public boolean isSameVersion( TLanguageFile other )
  {
    if (other == null)
      return false ;

    // language -------
    boolean back = check( lang, other.getLanguageCode() ) ;

    // country -------
    back = back & check( country, other.getCountryCode() ) ;

    // variant -------
    back = back & check( variant, other.getVariantCode() ) ;

    return back ;
  }


  private boolean check( String s1, String s2)
  {
    boolean b1 = true ;  // s1 = empty

    if (s1 != null)   // check, if s1 contains some data
    {
      if (s1.length() > 0)
      {
        b1 = false ;
      }
    }

    boolean b2 = true ;  // s2 = empty

    if (s2 != null)   // check, if s1 contains some data
    {
      if (s2.length() > 0)
      {
        b2 = false ;
      }
    }

    if (b1 || b2)  // one is empty
    {
      // if s1 & s2 are empty => definition is equal....
      if ( b1 && b2 )
      {
        return true ;
      }
      return false ;
    }

    // different ?
    if (s1.toLowerCase().hashCode() != s2.toLowerCase().hashCode())
    {
      return false ;
    }

    return true ;
  }

  /** returns the language (and country) iso code  */
  public String getLanguageExtension()
  {
    return version ;
  }

  /** returns the "english" language name and the available ids
   *  like "German (de_DE)".
   */
  public String getFullLanguageName()
  {
    if ( country != null )
    {
      if ( country.length() > 0 )
      {
        return TLanguageNames.runtime.getLanguageNameExt( lang, country ) ;
      }
    }

    return TLanguageNames.runtime.getLanguageNameExt( lang ) ;
  }

  /** returns the 2letter iso country code from file name */
  public String getCountryCode()
  {
    return country ;
  }

  /** returns the 2letter iso language code from file name */
  public String getLanguageCode()
  {
    return lang ;
  }

  public String getVariantCode()
  {
    return variant ;
  }


  public boolean hasLanguageExtension()
  {
    boolean back = false ;
    if ( lang != null )
    {
      if ( lang.length() > 0 )
      {
        back = true ;
      }
    }
    return back ;
  }

  public String getBaseName()
  {
    return prefix ;
  }

  /** file extension, e.g. ".properties" */
  public String getFileExtension()
  {
    return suffix ;
  }


  public String getFileName()
  {
    return fullName.getName() ;
  }

  public File getFileHandle()
  {
    return fullName ;
  }

}
