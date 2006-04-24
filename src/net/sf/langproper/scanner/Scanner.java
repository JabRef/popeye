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

// created by : r.nagel 12.09.2005
//
// function : extract string keys from source
//
// todo     : - make the scanner (parser) chooseable
//
// modified :

package net.sf.langproper.scanner ;

import java.util.* ;
import java.io.* ;
import net.sf.langproper.scanner.java.* ;
import net.sf.langproper.engine.project.*;

public class Scanner
{
  /** a list of all registered parser */
  private Vector registeredParser = new Vector() ;

  // the used scanner (parser)
  private static CodeScanner codeScanner ;

  static
  {
     // the constructur of CodeScanner requires a stream....
     codeScanner = new CodeScanner( new InputStream()
     {
       public int read()
       {
         return -1 ;
       }
     }) ;
  }

  private FileFilter filter = new JavaNameFilter() ;

  private TScannerData scannerData ;

  public Scanner()
  {
    registeredParser.add("default");
  }

  public TScannerData getScannerData()
  {
    return scannerData;
  }

  public void setScannerData(TScannerData pScannerData)
  {
    this.scannerData = pScannerData;

  }


  /** prepare file parsing, collect all files */
  public void collect()
  {
    if ( scannerData == null)
      return ;

    Vector fileList = scannerData.getFileList() ;
    fileList.clear() ;

    File file = new File( scannerData.getSourcePath() ) ;

    if ( file.exists() )
    {
      Stack stack = new Stack() ;
      stack.push( file ) ;

      while ( !stack.empty() )
      {
        file = ( File ) stack.pop() ;
        if ( file.isDirectory() )
        {
          // get all entries
          File files[] = file.listFiles( filter ) ;

          if ( files != null )
          {
            // sort all entries, files into list the vector, directories to the stack
            for ( int t = 0, len = files.length ; t < len ; t++ )
            {
              file = files[t] ;
              if ( file.isDirectory() )
              {
                stack.push( file ) ;
              }
              else // file
              {
                fileList.add( file ) ;
              }
            }
          }
        }
        else // special case => methode parameter root = file, not directory
        {
          fileList.add( file ) ;
        }
      }
    }
  }

  /** parse all collected files and store all string literals */
  public void scan()
  {
    if ( scannerData == null)
      return ;

    TKeyRefList keyList = scannerData.getKeyList() ;
    Vector fileList = scannerData.getFileList() ;

    codeScanner.setKeyList( keyList );

    System.out.println( "scanner filelist " +fileList.size()) ;

    keyList.clear() ;  // remove all old entries

    if ( fileList.size() > 0 )  // scan every file
    {
      File file = null ;
      for ( int t = 0, len = fileList.size() ; t < len ; t++ )
      {
        try
        {

          file = ( File ) fileList.get( t ) ;
          InputStream stream = new FileInputStream( file ) ;

          codeScanner.ReInit( stream ) ;

          codeScanner.setCurrentFileName( file.getName() );

//          System.out.println( "scan " + file ) ;
          codeScanner.JavaCompilationUnit() ;
        }
        catch ( Exception e )
        {
          System.out.println( "scan " + file ) ;
          System.out.println( "Ausnahme " ) ;
          e.printStackTrace() ;
        }
        catch ( Error e2 )
        {
          System.out.println( "scan " + file ) ;
          System.out.println( "FEHLER " ) ;
          e2.printStackTrace() ;
        }
      }
    }

    keyList.rebuild();  // rebuild the tablemodel and filter same keys
  }

  public Vector getRegisteredParser()
  {
    return this.registeredParser ;
  }

  // ---------------------------------------------------------------------------
  /** demo */
  public static void main( String args[] )
  {
    String file = "/home/nagel/develop/OpenTools/LangProper/src" ;
    if ( args.length > 0 )
    {
      file = args[0] ;
    }

    TScannerData data = new TScannerData(file, "", new TProjectSettings() ) ;
    Scanner scanner = new Scanner() ;
    scanner.setScannerData(data);
    scanner.collect() ;
    scanner.scan() ;

    System.out.println( "found " +  data.getFileList().size() + " files" ) ;
  }

  // ---------------------------------------------------------------------------
  private class JavaNameFilter implements FileFilter
  {
    public boolean accept( File file )
    {
      if ( file.isFile() )
      {
        String name = file.getName() ;
        int i = name.indexOf( ".java" ) ;  // use lastIndexOf() ???
        if ( ( i > 0 ) && ( i + 5 >= name.length() ) ) // a ".java" name is not allowed
        {
          return true ;
        }

        return false ;
      }

      return true ; // directory
    }

  }
}
