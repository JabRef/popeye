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
// function : handling of all available language versions for the project
//
// todo     :
//
// modified :

package net.sf.langproper.engine.project ;

import java.io.* ;

import org.w3c.dom.* ;
import net.sf.langproper.engine.* ;
import java.util.logging.*;

public abstract class TProjectFileList extends TLanguageList
{
  /** collect all project files, based on file */
  public abstract boolean collect( File file ) ;

  /** Generates an instance of project file with translations.
   *  It's only a handle, without any connection to the FileList. This
   *  must be do seperatly! */
  public abstract TLanguageFile createFileHandle( File file ) ;

  /** generates a TLanguageFile instance, for the language specification */
  public abstract TLanguageFile generateLanguageNameForIso( String isoLang,
      String isoCountry,
      String isoVariant,
      String path ) ;


  // --------------------------------------------------------------------------
  // load from / save to xml project file support -----------------------------
  // --------------------------------------------------------------------------
  public void exportToXML( Element node, Document doc )
  {
    // save the default language
    TLanguageFile lf = this.getDefaultLang() ;
    if (lf != null)
    {
      Element elem = doc.createElement("default") ;
      exportSingle( lf, elem, doc ) ;

      node.appendChild( elem ) ;
    }

    // save all other file settings
    for ( int t = 0, len = langVersions.size() ; t < len ; t++ )
    {
      TLanguageFile lFile = (TLanguageFile) langVersions.get(t) ;
      Element dummy = doc.createElement("file") ;
      exportSingle(lFile, dummy, doc);
      node.appendChild(dummy ) ;
    }
  }

  // export of the settings from a single file into the xml database structure
  private void exportSingle(TLanguageFile lfile, Element node, Document doc)
  {
    String str = lfile.getFileName() ;
    if (str != null)
      if (str.length() > 0)
      {
        node.setAttribute( "filename", lfile.getFileName() ) ;
      }

    Element elem = doc.createElement("properties") ;
    lfile.exportToXml(elem, doc);
    node.appendChild(elem) ;
  }

  // --------------------------------------------------------------------------
  /** read all file nodes and initialize the files */
  public void initFromXML( Element node, String workDir, String projectDir )
  {
    clear();

    NodeList nList = node.getElementsByTagName("default") ;
    TLanguageFile dummy ;

    // default entry found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        dummy = initSingle( (Element) nList.item(0), workDir, projectDir) ;
        if (dummy != null)
        {
          addDefaultVersion(dummy, false) ;
        }
      }
    }

    // all other files
    nList = node.getElementsByTagName("file") ;
    if (nList != null)
    {
      for(int t = 0, len = nList.getLength() ; t < len ; t++)
      {
        dummy = initSingle( (Element) nList.item(t), workDir, projectDir) ;
        if (dummy != null)
        {
          addLanguageVersion(dummy) ;
        }
      }
    }
  }

  /** initialize the filename */
  private TLanguageFile initSingle( Element node, String workDir, String projectDir)
  {
    TLanguageFile back = null ;

    String filename = node.getAttribute("filename") ;

    if (filename != null)
    {
      if (filename.length() > 0)
      {
        File workFile = new File( filename ) ;

        // file doesn't exists => look at a static path
        if ( !workFile.exists() )
        {
          workFile = new File( workDir, filename ) ;

          // try the xml project path
          if (!workFile.exists() )
          {
            workFile = new File(projectDir, filename) ;
          }
        }

        // last hope: found?
        if ( workFile.exists() )
        {
          back = createFileHandle( workFile ) ;

          // read some extra properties
          NodeList nList = node.getElementsByTagName("properties") ;
          if (nList != null)
          {
            if (nList.getLength() > 0)
            {
              back.initFromXml( (Element) nList.item(0) ) ;
            }
          }
        }
        else
        {
          Logger.global.warning( "file not found " +filename ) ;
        }
      }
    }
    return back ;
  }

}
