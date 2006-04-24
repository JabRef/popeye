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
// function : handling of all available language versions (from properties file set)
//            basic functionality for a AbstractTableModel
//
// todo     :
//
// modified :  31.08.2005 r.nagel
//             AbstractTableModel from class source extracted, now the data
//             can be splitt into different tableModels (..gui.model.TLangPropsModel)

package net.sf.langproper.engine ;

import java.util.* ;
import java.util.logging.* ;

import javax.swing.* ;
import javax.swing.event.* ;
import net.sf.langproper.engine.project.*;

public class TLanguageList implements ListModel
{

  public static final int
      KEY_VALUE      = -3,
      UNDEFINED_LANG = -2,
      DEFAULT_LANG = -1 ;

  /** information about language versions (single properties file) */
  protected Vector langVersions ;

  /** placeholder for a default translation */
  private TLanguageFile defaultLang ;

  /** visibility of single data changed */
  private boolean dataVisibilityChanged ;

  private int visibleVersionsCount ;

  //----------------------------------------------------------------------------

  public TLanguageList()
  {
    langVersions = new Vector( 5 ) ;
    dataVisibilityChanged = false ;
    visibleVersionsCount = -1 ;
  }

  //----------------------------------------------------------------------------

  /** clear all data */
  public void clear()
  {
    langVersions.clear();
    defaultLang = null ;
    dataVisibilityChanged = false ;
    visibleVersionsCount = -1 ;
  }

  /** returns the number of available language versions - without default language */
  public int size()
  {
    return langVersions.size() ;
  }

  /** returns the number of ALL visible language versions - without default language */
  public int getVisibleSize()
  {
    if ( (visibleVersionsCount < 0) || dataVisibilityChanged)
    {
      int len = langVersions.size() ;
      int vLen = 0 ;

      // count all visible languages
      for ( int t = 0 ; t < len ; t++ )
      {
//        System.out.println( "language " +get(t).getFullLanguageName() ) ;
        if ( get( t ).isVisible() )
          vLen++ ;
      }
      visibleVersionsCount = vLen ;
    }

    return visibleVersionsCount ;
  }

  protected boolean isVisible(int langID)
  {
    return (( TLanguageFile ) langVersions.get( langID )).isVisible() ;
  }

  /** returns the language with ID <langID>, no ID checks  */
  public TLanguageFile get(int langID)
  {
    // langID < 0 (normally default language or wrong id
    if ( (langID == this.DEFAULT_LANG) && (this.hasDefaultLang()))
      return this.defaultLang ;

    // if no languages are available
    if (langID >= langVersions.size())
    {
      return this.getDefaultLang() ;
    }

    return ( TLanguageFile ) langVersions.get( langID ) ;
  }

  //----------------------------------------------------------------------------

  /** Put a languagefile into array.
   *  returns false, if the language is already defined */
  public boolean addLanguageVersion( TLanguageFile versionFile )
  {
    boolean back = false ;

      int index = getLanguageID( versionFile ) ;
      if ( index == UNDEFINED_LANG ) // not found
      {
        langVersions.add( versionFile ) ;
        this.dataVisibilityChanged = true ; // new language is visible
        back = true ;
      }
      else
      {
        System.out.println( "already!!!" ) ;
        Logger.global.fine( "language version already defined " +
                             versionFile.getFileHandle() ) ;
      }

    return back ;
  }

  /** Add a default version of translations. If overwrite is false and
   *  a default version is already defined, no update were performed.
   */
  public boolean addDefaultVersion( TLanguageFile versionFile, boolean overwrite)
  {
    boolean back = false ;

    if ( ( defaultLang == null ) | (overwrite))
    {
      defaultLang = versionFile ;
      back = true ;
    }
    else
    {
      Logger.global.fine( "default version already defined " +
                          versionFile.getFileHandle() ) ;
    }
    return back ;
  }
  //----------------------------------------------------------------------------

  /** removes a language definition from list,
   * !! there are some references from TProjectData at all entries of this list
   */
  public boolean removeLanguageVersion( TLanguageFile versionFile )
  {
    boolean back = false ;

    if (versionFile == null)
    {
      return false ;
    }

    // try to delete default
    if (defaultLang != null)
    {
       if (defaultLang.hashCode() == versionFile.hashCode() )
       {
         defaultLang = null ;
         back = true ;
       }
    }

    if ( !back)
    {
      back = langVersions.remove( versionFile ) ;
    }

    return back ;
  }

  //----------------------------------------------------------------------------

  /** Returns the internal ID of the language which is defined into the
   *  file description.  Only the (language, country and variant) definitions
   *  are checked. */
  public int getLanguageID( TLanguageFile checkFile )
  {
    int back = UNDEFINED_LANG ;

    if ( checkFile != null )
    {
      if (checkFile.hasLanguageExtension() )
      {
        boolean found = false ;
        int t = 0 ;
        int len = langVersions.size() ;
        while ( ( !found ) && ( t < len ) )
        {
          if ( checkFile.isSameVersion( ( TLanguageFile ) langVersions.get( t ) ) )
          {
            found = true ;
            back = t ;
          }
          else
          {
            t++ ;
          }
        }
      }
      else  // no extension => default language
      {
        back = DEFAULT_LANG ;
      }
    }
    return back ;
  }


  //----------------------------------------------------------------------------
  // AbstractTableModel
  // ---------------------------------------------------------------------------

  public int getRowCount()
  {
    int back = langVersions.size() ;
    if ( defaultLang != null)
      back++ ;

    return back ;
  }

  // returns the LanguageVersionFile Object for row <row>
  public TLanguageFile getData(int row)
  {
    TLanguageFile dummy = null ;
    if ((row == 0) && (defaultLang != null))
    {
      dummy = defaultLang ;
    } else if (defaultLang != null)
    {
      dummy = (TLanguageFile) langVersions.get(row -1) ;
    }
    else
    {
      dummy = (TLanguageFile) langVersions.get(row) ;
    }

    return dummy ;
  }

  /** set the data visibility flag to true */
  public void reportDataVisibilityChanged()
  {
    dataVisibilityChanged = true ;
  }

  /** the visibility of some language versions was changed */
  public boolean hasDataVisibiltyChanged()
  {
    return dataVisibilityChanged ;
  }

  /** resets all flags for data (dataVisibiltyChanged) */
  public void resetDataFlags()
  {
    dataVisibilityChanged = false ;
  }


  //----------------------------------------------------------------------------
  // handling of optional default language version
  // ---------------------------------------------------------------------------
  public boolean hasDefaultLang()
  {
    if (defaultLang == null)
      return false ;

    return true ;
  }
  public TLanguageFile getDefaultLang()
  {
    return defaultLang;
  }

  public void setDefaultLang(TLanguageFile pDefaultLang)
  {
    this.defaultLang = pDefaultLang;
  }
  //----------------------------------------------------------------------------


  /** sorts all entries - now only the key sorting is implemented
   *  and the langID is ignored */
  public void sortElements()
  {
    Object eList[] = langVersions.toArray() ;
    java.util.Arrays.sort(eList, new TLanguageNameComparator() );

    langVersions.clear();
    for (int t = 0 ; t < eList.length ; t++)
    {
       langVersions.add(eList[t]) ;
    }
  }

  // ---------------------------------------------------------------------------
  // ListModel -----------------------------------------------------------------
  // ---------------------------------------------------------------------------

  // with or without default language
  public int getSize()
  {
    return this.getRowCount() ;
  }

  public Object getElementAt( int index )
  {
    return this.getData(index) ;
  }

  public void addListDataListener( ListDataListener l )
  {
  }

  public void removeListDataListener( ListDataListener l )
  {
  }

  // -------------------------------------------------------------------------
  class TLanguageNameComparator implements java.util.Comparator
  {
    public final int compare(Object o1, Object o2)
    {
      TLanguageFile p1 = null ;
      TLanguageFile p2 = null ;

      try
      {
        p1 = (TLanguageFile) o1 ;
        p2 = (TLanguageFile) o2 ;
      }
      catch (Exception e) {}

      int back = 0 ;

      if ((p1 != null) && (p2 != null))
      {
        back = compareTo(p1.getLanguageCode(), p2.getLanguageCode()) ;
        if (back == 0)
          back = compareTo(p1.getCountryCode(), p2.getCountryCode()) ;
      }

      return back ;
    }

    public final boolean equals(Object obj)
    {
      return false ;
    }

    private int compareTo(String s1, String s2)
    {
      if (s1 == null)
        s1 = "" ;

      if (s2 == null)
        s2 = "" ;
      return s1.compareTo(s2) ;
    }

  }

}
