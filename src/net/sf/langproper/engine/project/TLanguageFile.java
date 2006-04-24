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
// function : name handling of one properties language file
//            basic class for the project file class (e.g. JavaBundleFile)
//            some functionality like the java.util.Locale class
//
// todo     :  - use java.util.Local and replace some functions
//
//             - a filename can contain a langague, a country and a variant
//               -> put variant handling into the class functionality
//
//
// modified : 09.05.2005 r.nagel
//            insert a "visible" Boolean object, used by TPropTable (hide some languages)
//            31.08.2005 r.nagel
//            refactoring, old name : langproper.engine.TLanguageVersionFile
//            define project type specific methodes as abstract
//            split old methode set, some parts roll out (JavaBundleFile)

package net.sf.langproper.engine.project ;

import java.io.* ;

import javax.swing.* ;

import org.w3c.dom.* ;
import net.sf.langproper.engine.iso.* ;
import net.sf.langproper.charset.*;
import net.sf.langproper.engine.*;

public abstract class TLanguageFile
{
  private Boolean visible = new Boolean(true) ;

  private String defaultEncoding = TEncodingList.JAVA_ENC ;

  private boolean saveWithEscapeSequence = true ;

  private Icon flag = null ;
  private boolean flagLoaded = false ;

  /** check's if the files are equal (upper and lower cases are different) */
  public abstract boolean isEqual(TLanguageFile other) ;

  /** check's if the filename is a bundle filename (name and suffix equal) */
  public abstract boolean isBundleName( TLanguageFile other ) ;

  /** check, if the File <other> has the same language definition
   * (language, country, variant) */
  public abstract boolean isSameVersion( TLanguageFile other ) ;

  /** returns the "english" language name and the available ids
   *  like "German (de_DE)".
   */
  public abstract String getFullLanguageName() ;

  /** returns the 2letter iso country code from file name */
  public abstract String getCountryCode() ;

  /** returns the 2letter iso language code from file name */
  public abstract String getLanguageCode() ;

  /** returns the 2letter iso variant code from file name */
  public abstract String getVariantCode() ;

  /** returns true, if the filename contains a language extension or
   *  if it false the file is the default file (only one default file is permitted) */
  public abstract boolean hasLanguageExtension() ;

  /** returns the language (and country (and variant)) iso code  */
  public abstract String getLanguageExtension() ;

  /** returns the complete filename (without path, with language extensions) */
  public abstract String getFileName() ;

  /** returns only the filename (without any language, contry.. extensions */
  public abstract String getBaseName() ;

  /** file extension, e.g. ".properties" */
  public abstract String getFileExtension() ;

  /** returns a reference of the file object */
  public abstract File getFileHandle() ;

  // --------------------------------------------------------------------------

  /** returns the boolean Object */
  public Boolean getVisible()
  {
    return visible;
  }

  /** returns a boolean value */
  public boolean isVisible()
  {
    return visible.booleanValue() ;
  }

  public void setVisible(boolean pVisible)
  {
      visible = new Boolean(pVisible) ;
  }

  public Icon getFLag()
  {
    if (!flagLoaded)
    {
      flag = TLanguageNames.runtime.getFlag( getLanguageCode(), getCountryCode() ) ;
      flagLoaded = true ;
    }

    return flag ;
  }

  // --------------------------------------------------------------------------
  // encoding stuff
  // --------------------------------------------------------------------------

  public void setDefaultFileEncoding( String encoding )
  {
    if ( (encoding != null) && (encoding.length() > 0))
      defaultEncoding = encoding ;
    else
      defaultEncoding = TEncodingList.JAVA_ENC ;
  }

  public String getDefaultEncoding()
  {
    return defaultEncoding ;
  }

  public boolean isSaveWithEscapeSequence()
  {
    return saveWithEscapeSequence;
  }

  public void setSaveWithEscapeSequence(boolean pSaveWithEscapeSequence)
  {
    this.saveWithEscapeSequence = pSaveWithEscapeSequence;
  }

  // --------------------------------------------------------------------------
  // load from / save to xml project file support -----------------------------
  // --------------------------------------------------------------------------
  public void exportToXml( Element node, Document doc)
  {
    node.setAttribute("visible", Boolean.toString(isVisible() ) );
    node.setAttribute("encoding", defaultEncoding);
    node.setAttribute("withescape", Boolean.toString( isSaveWithEscapeSequence()));
  }

  public void initFromXml( Element node )
  {
    setVisible( XmlUtils.readBooleanAttribute( node, "visible", true) ) ;
    setDefaultFileEncoding( XmlUtils.readStringAttribute(node, "encoding", defaultEncoding) )  ;
    setSaveWithEscapeSequence( XmlUtils.readBooleanAttribute( node, "withescape", true) ) ;
  }


}
