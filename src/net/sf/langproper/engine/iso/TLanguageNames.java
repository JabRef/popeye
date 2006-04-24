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
// function : A "translation class" for expanding the 2-letter iso language code
//            to the full language name
//
// todo     : - country and flags support
//            - cache index of iso code after a getXXX() method call, because
//              every access requires O(N) ! If it necessary ?
//
// modified :  26.08.2005 r.nagel
//             load flags


package net.sf.langproper.engine.iso;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import javax.swing.* ;
import javax.swing.table.* ;

import net.sf.langproper.* ;


/**
 *  A "translation class" for expanding the 2-letter iso language code to
 *  full language name
 */

public class TLanguageNames extends AbstractTableModel
{

  /** the getValueAt(row, column) function returns the ISOData Object
   *  which is linked with this line, if the column parameter has this value
   */
  public static final int
      FULL_ENTRY = -1 ;

  public static TLanguageNames runtime = new TLanguageNames();

  /** contains all 2 letter iso codes and it's full name */
  private ArrayList liste = new ArrayList() ;

  /** language code file found and loaded */
  private boolean availStatus = false ;

  private TLanguageNames()
  {
    loadFullLanguageNames() ;
  }

  public boolean isAvailable()
  {
    return availStatus ;
  }

  public int size()
  {
    return liste.size() ;
  }

  private void loadFullLanguageNames()
  {
    // generate stream from jar file resource
    URL res = TGlobal.class.getResource(TGlobal.RESOURCE_ROOT + "/" +TGlobal.LANGUAGES_FILENAME) ;
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
      // inStream = this.getClass().getResourceAsStream(TGlobal.RESOURCE_ROOT + "/" +TGlobal.LANGUAGES_FILENAME);
    }

    if (inStream == null)  // stream not empty
    {
      availStatus = false ;

      if (TGlobal.DEVEL)
      {

        try  // JBuilder IDE hack ;-)
        {
          inStream = new FileInputStream(
              "src/resource/" + TGlobal.LANGUAGES_FILENAME ) ;
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
           if (fields.length > 1)
           {
              liste.add( new ISOLanguage(fields[0], fields[1]) );
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

  /**
   * getLanguageName
   *
   * @param id String, the 2 letter iso language code
   * @return If the 2 letter iso code was found it returns the full language
   *   name (else an empty String).
   */
  public String getLanguageName(String id)
  {
    String back = "" ;

    if (id != null)
    {
       boolean found = false ;
       int hash = id.hashCode() ;
       ListIterator it = liste.listIterator() ;
       while ( (it.hasNext()) && (!found))
       {
         ISOLanguage dummy = (ISOLanguage) it.next() ;
         if (dummy.getLanguageIso().hashCode() == hash)
         {
           found = true ;
           back = dummy.getLanguageName() ;
         }

       }
    }

    return back ;
  }

  /**
   * getLanguageNameExt
   *
   * @param id String, the 2 letter iso language code
   * @return full language name and iso code : "fullname (xx)"
   */
  public String getLanguageNameExt(String id)
  {
    String res = getLanguageName(id) ;
    String back = id ;

    if (res != null)
    {
      if (res.length() > 0)
      {
        back = res + " (" +id +")" ;
      }
    }

    return back ;
  }

  /**
   * getLanguageNameExt
   *
   * @param lID String, the 2 letter iso language code
   * @param cID String, the 2 letter iso country code
   * @return full language name and iso code : "fullname (xx_xx)"
   */
  public String getLanguageNameExt(String lID, String cID)
  {
    String res = getLanguageName(lID) ;
    String back = lID +"_" +cID ;

    if (res != null)
    {
      if (res.length() > 0)
      {
        back = res + " (" +back +")" ;
      }
    }

    return back ;
  }

  // --------------------------------------------------------------------------
  /**
   * getFlag
   *
   * @param lID String, the 2 letter iso language code
   * @param cID String, the 2 letter iso country code
   * @returns the flag
   */
  public Icon getFlag(String language, String country)
  {
    // try to load the country flag
    Icon back = loadFlag(country) ;

    // could not load a country flag, try out the language
    if (back == null)
    {
      if (language != null)
      {
        back = loadFlag( language ) ;

        // flag with language iso code not found, special case : english?
        if ( language.hashCode() == "en".hashCode() )
        {
          back = loadFlag( "gb" ) ;
        }
      }
    }


    return back ;
  }

  private Icon loadFlag( String strID)
  {
    Icon back = null ;

    if (strID != null)
    {
      URL iconName = TGlobal.class.getResource( TGlobal.FLAG_ROOT +
                                                strID.toLowerCase() + ".png" ) ;
      try
      {
        back = new ImageIcon( iconName ) ;
      }
      catch ( Exception e )
      {
        back = null ;
      }
    }
    return back ;
  }

  // --------------------------------------------------------------------------
  // AbstractTableModel ---------------
  // --------------------------------------------------------------------------

  private String[] columnNames =
      {
      "iso code",
      "language"
  } ;

  public int getColumnCount()
  {
    return columnNames.length ;
  }

  public int getRowCount()
  {
    return liste.size() ;
  }

  public String getColumnName( int col )
  {
    return columnNames[col] ;
  }

  public Object getValueAt( int row, int col )
  {
    Object dummy = liste.get( row ) ;
    if (dummy != null)
    {
      ISOLanguage data = (ISOLanguage) liste.get( row ) ;
      switch (col)
      {
        case FULL_ENTRY:
          return dummy ;
        case 0:
          return data.getLanguageIso() ;
        case 1:
          return data.getLanguageName() ;
      }
    }

    return "" ;
  }

  /*
   * JTable uses this method to determine the default renderer/
   * editor for each cell.  If we didn't implement this method,
   * then the last column would contain text ("true"/"false"),
   * rather than a check box.
   */
  public Class getColumnClass( int c )
  {
//    return getValueAt( 0, c ).getClass() ;
      return String.class ;
  }
}
