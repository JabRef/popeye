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


// created by : r.nagel 28.04.2005
//
// function : handling of all available language versions for java bundle
//            basic functionality for a AbstractTableModel
//
// todo     :
//
// modified :  31.08.2005 r.nagel
//             AbstractTableModel from class source extracted, now the data
//             can be splitt into different tableModels (..gui.model.TLangPropsModel)

package net.sf.langproper.engine.project.java ;

import java.io.* ;

import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;
import java.util.*;
import java.util.logging.* ;

public class JavaBundleFileList extends TProjectFileList
{
  // collect all .properties files from java message bundle
  public boolean collect( File file )
  {
    boolean back = false ;  // return value -> default : not loaded

    // splitt the filename
    JavaBundleFile firstLoadFile = new JavaBundleFile( file ) ;

    String suffix = firstLoadFile.getFileExtension() ;

    if ( suffix.toLowerCase().hashCode() == ".properties".hashCode() )
    {
      File dir = new File( file.getParent() ) ;

      File files[] = dir.listFiles() ;
      if ( files != null )
      {
        // collect all filenames
        for ( int t = 0 ; t < files.length ; t++ )
        {
          JavaBundleFile workName = new JavaBundleFile( files[t] ) ;

          // load only files with an equal filename (but different language/country extension)
          if ( workName.isBundleName( firstLoadFile ) )
          {
            // append language
            if (workName.hasLanguageExtension())
            {
              addLanguageVersion( workName ) ;
            }
            else
            {
              addDefaultVersion( workName, false) ;
            }
          }

        }

        // arrange the languages files in an alphabetical order
        sortElements() ;

        back = true ;
      }
      else
      {
        Logger.global.info( " could not found any language bundle file " ) ;
      }
    }
    return back ;
  }

  /** generates a new TLanguageFile instance, for the language specification */
  public TLanguageFile generateLanguageNameForIso(String isoLang,
                                                  String isoCountry,
                                                  String isoVariant,
                                                  String path)
  {
    JavaBundleFile dummy = (JavaBundleFile) this.get(0) ;

    return new JavaBundleFile( dummy, isoLang, isoCountry, isoVariant, path ) ;
  }

  /** generates a project file with translations */
  public TLanguageFile createFileHandle( File file )
  {
    return new JavaBundleFile( file ) ;
  }

}
