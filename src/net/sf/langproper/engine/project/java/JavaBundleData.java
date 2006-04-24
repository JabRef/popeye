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
// function : data handling of a java bundles project
//
// todo     :
//
// modified : 21.02.2006 kiar
//            - insert buf.trim in loadSingleFile()
//            - handling of leading space before the keys
//            20.02.2006 kiar
//            - encoding

package net.sf.langproper.engine.project.java ;

import java.io.* ;
import java.util.* ;
import java.util.logging.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;

public class JavaBundleData extends TProjectData
{

  public JavaBundleData()
  {
    super( new JavaBundleFileList() ) ;
  }

  public void saveSingleFile( TLanguageFile file, int langID )
  {
    if ( file == null )
    {
      return ;
    }

    try
    {
      String fileName = file.getFileHandle().getCanonicalPath() ;

      if ( TGlobal.config.makeBackupFiles() )
      {
        File bakFile = new File( fileName ) ;
        if ( !bakFile.renameTo( new File( fileName + ".bak" ) ) )
        {
          Logger.global.warning( fileName + ".bak could not be created !" ) ;
        }
      }

     String encodingName = this.getUsedEncodingName( file );

     PrintWriter out = new PrintWriter( new OutputStreamWriter(
                              new FileOutputStream(fileName),
                              java.nio.charset.Charset.forName(encodingName))) ;
     // since 1.5
     // out = new PrintWriter( fileName, file.getDefaultEncoding() ) ;

      // read the escape mode
      boolean escapeMode = this.isEscapeMode(file) ;

      // save the info text (applicationname and version)
      out.println("#!");
      out.println( TGlobal.INFO_COMMENT_TEXT
                   + TGlobal.APPLICATION_NAME
                   + " " + TGlobal.VERSION_STRING
                   + " (" + TGlobal.PROJECT_MAIN_URL + ")" ) ;
      out.println("#! encoding:" +encodingName);

      // loop over all translations
      for ( Enumeration myEnum = translations.elements() ;
            myEnum.hasMoreElements() ; )
      {
        TMultiLanguageEntry entry = ( TMultiLanguageEntry ) myEnum.nextElement() ;

        boolean save = false ;

        // get translation for this language or NULL
        String trans = entry.getTranslationOnly( langID ) ;

        // save keys with empty values ?
        if ( TGlobal.config.saveEmptyKeys() )
        {
          save = true ;
        }
        else // key with value ?
        {
          if ( trans != null )
          {
            if ( trans.length() > 0 )
            {
              save = true ;
            }
          }
        }

        if ( save )
        {
          // handling of comments
          TComments comment = entry.getComments( langID ) ;
          if ( comment != null )
          {
            int cSize = comment.getBlanksBefore() ;

            // write blank lines before the comment
            for (int t = 0 ; t < cSize ; t++)
            {
              out.println() ;
            }

            // write the comment(s)
            cSize = comment.getSize() ;
            for ( int t = 0 ; t < cSize ; t++ )
            {
              out.println( comment.getComment( t ) ) ;
            }

            // write blank lines after the comment
            cSize = comment.getBlanksAfter() ;
            for (int t = 0 ; t < cSize ; t++)
            {
              out.println() ;
            }
          } // all available comments saved

          // save a leading string sequence (spaces)
          String leading = entry.getLeading(langID) ;
          if (leading != null)
          {
            if (leading.length() > 0)
            {
              out.print(leading);
            }
          }

          // save key=value pairs
          out.print( entry.getKey() ) ;
          out.print( "=" ) ;

          // is there any translation?
          if (trans != null)
          {
            if ( !escapeMode )
            {
              trans = Utils.get_WYSIWYG_String( trans ) ;
            }
//            Logger.global.finest( "saving (" + langID + ") " + entry.getKey() + "=" +
//                                  trans ) ;
          }

          // prevent a key=null line in saved file
          // this second test is necessary, because the Utils.get_WYSIWYG()
          // methode could be null
          if (trans != null)
          {
            out.println( trans ) ;
          }
          else
          {
            out.println() ; // newline for empty lines
          }
        }

        entry.setModifyStatus( TMultiLanguageEntry.STATE_UNCHANGED ) ;
      }

      out.flush() ;
      out.close() ;

    }
    catch ( Exception e )
    {
      Logger.global.warning( "could not write file " + file.getFileName() ) ;
    }
  }

  // load a single key=value property file
  public boolean loadSingleFile( TLanguageFile file, int langID )
  {
    String buf = null ;

    int trenner = 0 ;
    int zeile = 1 ;
    boolean back = false ;
    try
    {
      String key, value ;

      TComments comment = null ;

      String encodingName = this.getUsedEncodingName( file );

      BufferedReader input = new BufferedReader(
          //new FileReader( file.getFileHandle()), 1000) ;
                    new InputStreamReader(
                              new FileInputStream(file.getFileHandle()),
                              java.nio.charset.Charset.forName(encodingName))) ;

//      System.out.println( "load " +file + " -> " +encodingName ) ;

      while ( input.ready() )
      {
        String org  = input.readLine() ;

        if ( org != null )
        {
          // we NEED unique keys, that's why all leading and trailing whitespace
          // must be removed
          buf = org.trim() ; // returns a copy of the string, with leading and trailing whitespace omitted.
          /*
             leave the lines just as they were. Maybe it was the intetion of for example two leading spaces left.
             If you think it should be trimmed let's do a configuration option.
          */
          if ( buf.length() > 0 && buf.charAt(0) != '#' ) // a REAL line, not a comment
          {
            trenner = buf.indexOf( '=' ) ;

            if ( ( trenner == -1 ) || ( trenner == 0 ) ) // no =
            {
              Logger.global.warning( file.getFileHandle() +
                                  " --> \"=\" not found at line : " + zeile ) ;
              Logger.global.warning( "line : " + buf ) ;
            }
            else
            {
              key = buf.substring( 0, trenner ) ;
              value = buf.substring( trenner + 1 ) ;

              TMultiLanguageEntry entry = addTranslation( key, value, comment, langID ) ;

              // check if some space before the key exists
              int t = org.indexOf(key) ;
              if (t > 0)
              {
                entry.setLeading( org.substring(0, t+1), langID) ;
              }

              comment = null ;
            }
          }
          else // comment line (or just an empty line)
          {
            boolean insert = true ;

            // a "#!...." line isn't a real comment => ignore it
            if ( buf.length() > 1 )
            {
              if ( buf.startsWith("#!") )
              {
                insert = false ;
              }
            }

            if ( insert ) // no "#!" comment
            {
              if ( comment == null )
              {
                comment = new TComments() ;
              }
              comment.addComment( org ) ;
              this.setCommentFound(true);
            }
          }
        }

        zeile++ ;
      }
      input.close() ;
      back = true ;
    }
    catch ( Exception e )
    {
//      System.out.println( e ) ;
//      e.printStackTrace() ;
      Logger.global.warning( " error loading file " + file.getFileName() ) ;
    }

    return back ;
  }
  // --------------------------------------------------------------------------

}
