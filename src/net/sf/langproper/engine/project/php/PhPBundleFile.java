package net.sf.langproper.engine.project.php ;

import java.io.* ;

import net.sf.langproper.engine.project.* ;
import net.sf.langproper.engine.iso.*;

public class PhPBundleFile extends TLanguageFile
{
  private File fileRef ;
  private String prefix ; // all characters before _ => filename
  private String suffix ; // extension ( all characters after . (incl ".") )
  private String lang ; // 2 letter language ID
  private String country ; // 2 letter country ID
  private String variant ; // 2 letter variant ID
  private String version ; // lang, country and variant ID (the language part of filename)

  public PhPBundleFile(File file)
  {
    init( file ) ;
  }

  public PhPBundleFile( PhPBundleFile other,
                        String isoLang,
                        String isoCountry,
                        String isoVariant,
                        String defaultPath )
  {
      lang = isoLang ;
      country = isoCountry ;
      variant = isoVariant ;
      version = "lang" ;

      suffix = ".php" ;
      prefix = "X" ;

      fileRef = new File(version + suffix) ;
  }

  private void init( File file )
  {
    fileRef = file ;

    String fileName = fileRef.getName() ;

    int i1 = fileName.indexOf( '_' ) ;
    int i2 = fileName.indexOf( '.', -1 ) ;

    prefix = "" ;
    suffix = "" ;

    // check the java style filename -> "name_language_country_variant.php"
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

        // prefix is iso code => file like "language.php"
        if (prefix.length() == 2)
        {
          // is it a iso code ?
          String dummy = TLanguageNames.runtime.getLanguageName(prefix) ;
          if (dummy.length() > 0)
          {
            lang = prefix ;
            prefix = "" ;
          }
        }
      }
      else // no _ and . => filename without fileextension like .php
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

  }



  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getCountryCode()
  {
    return country ;
  }

  /**
   * file extension, e.g.
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getFileExtension()
  {
    return suffix ;
  }

  /**
   *
   * @return File
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public File getFileHandle()
  {
    return fileRef ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getFileName()
  {
    return fileRef.getName() ;
  }

  /**
   * returns the "english" language name and the available ids like "German
   * (de_DE)".
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
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

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getLanguageCode()
  {
    return lang ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getLanguageExtension()
  {
    return version ;
  }

  /**
   * returns only the filename (without any language, contry..
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getBaseName()
  {
    return prefix ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public String getVariantCode()
  {
    return variant ;
  }

  /**
   *
   * @return boolean
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public boolean hasLanguageExtension()
  {
    return true ;
  }


  public boolean isBundleName( TLanguageFile other )
  {
    return true ;
  }

  /**
   *
   * @param other TLanguageFile
   * @return boolean
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public boolean isEqual( TLanguageFile other )
  {
    return false ;
  }

  /**
   *
   * @param other TLanguageFile
   * @return boolean
   * @todo Implement this net.sf.langproper.engine.project.TLanguageFile method
   */
  public boolean isSameVersion( TLanguageFile other )
  {
    return false ;
  }
}
