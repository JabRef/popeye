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


// created by : r.nagel 30.08.2005
//
// function : basic project data handling class
//
// todo     :
//
// modified :

package net.sf.langproper.engine.project ;

import java.io.* ;

import javax.swing.table.* ;

import net.sf.langproper.engine.* ;
import net.sf.langproper.gui.GUIGlobals;
import net.sf.langproper.charset.TEncodingList;

public abstract class TProjectData extends AbstractTableModel
{
  public static final int KEY_COLUMN = 2 ; // index of the key column
  public static final int COL_OFFSET = 3 ; // first language column


  /** contains all translations */
  protected TranslationList translations = new TranslationList();

  /** list of all available languages and some of it's properties  */
  private TProjectFileList langList ;

  /** data available */
  private boolean dataSetLoaded = false ;

  /** data were changed - every data manipulation operation must set this flag */
  private boolean dataChanged = false ;

  /** true if a comment was found */
  private boolean commentFound = false ;

  /** record for table column -> languageID */
  private int[] mapping ;

  /** settings reference */
  protected TProjectSettings settings = null ;

  // --------------------------------------------------------------------------
  // abstract methods ---------------------------------------------------------
  // --------------------------------------------------------------------------

  // load a single file and denote all entries with langID
  public abstract boolean loadSingleFile( TLanguageFile file, int langID ) ;

  /** save a single file */
  public abstract void saveSingleFile( TLanguageFile file, int langID ) ;
  // --------------------------------------------------------------------------

  public TProjectData(TProjectFileList list)
  {
    langList = list ;
  }

  public TranslationList getTranslations()
  {
    return translations;
  }
  // --------------------------------------------------------------------------
  /** set the reference for the global project settings */
  public void setProjectSettings( TProjectSettings projectSettings)
  {
    settings = projectSettings ;
  }

  // --------------------------------------------------------------------------
  /** collect all files, use <file> as pattern */
  public boolean collect( File file)
  {
    langList.clear();
    return langList.collect(file) ;
  }

  // --------------------------------------------------------------------------
  /** load all files into MM */
  public void reload()
  {
    commentFound = false ;

    // delete all data
    translations.clear() ;

    // load default file
    if ( langList.getDefaultLang() != null )
    {
      loadSingleFile( langList.getDefaultLang(), TLanguageList.DEFAULT_LANG ) ;
    }
    // load all other language files
    for ( int t = 0 ; t < langList.size() ; t++ )
    {
      loadSingleFile( langList.get( t ), t ) ;
    }

    this.fireUpdateDataStructure(false) ;

    GUIGlobals.sortDirection.setUnsorted() ;

    dataChanged = false ;
    dataSetLoaded = true ;
  }

  // --------------------------------------------------------------------------
  /** save all files */
  public void save()
  {
    if ( langList.getDefaultLang() != null )
    {
      saveSingleFile( langList.getDefaultLang(), TLanguageList.DEFAULT_LANG ) ;
    }
    // load all other language files
    for ( int t = 0 ; t < langList.size() ; t++ )
    {
      saveSingleFile( langList.get( t ), t ) ;
    }
    this.setDataChanged( false ) ;
  }

  // --------------------------------------------------------------------------

  /** returns the current encoding for the file */
  public String getUsedEncodingName(TLanguageFile file)
  {
    String back = "ISO-8859-1" ;
    if (file != null)
    {
      // not WYSIWYG mode or the encoding is java utf with escape sequences
      if ( !settings.getSaveWYSIWYGmode() ||
           ( TEncodingList.isJavaEncoding( file.getDefaultEncoding() ) ) )
      {
        back = "ISO-8859-1" ;
      }
      else // WYSIWYG mode
      {
        back = file.getDefaultEncoding() ;

        // valid ?
        if ( ( back == null ) || ( back.length() < 1 ) )
          back = "ISO-8859-1" ;
      }
    }
    return back ;
  }

  /** save with or without escape sequences */
  public boolean isEscapeMode( TLanguageFile file )
  {
    if (file != null)
    {
      // WYSIWYG mode and NOT java utf for the file encoding
      if ( settings.getSaveWYSIWYGmode() &&
           !TEncodingList.isJavaEncoding( file.getDefaultEncoding() ))
      {
        return file.isSaveWithEscapeSequence() ;
      }
    }

    return true ;
  }
  // --------------------------------------------------------------------------

  /** insert a new language and resize all internal data structures */
  public TLanguageFile addNewLanguage(String isoLang,
                                      String isoCountry,
                                      String isoVari,
                                      String defaultPath)
  {
    // generate a new File object
    TLanguageFile dummy = langList.generateLanguageNameForIso(isoLang,
                                                              isoCountry,
                                                              isoVari,
                                                              defaultPath) ;
    if ( addNewLanguage( dummy ) )
    {
      return dummy ;
    }

    return null ;
  }

  /** insert a new language and resize all internal data structures */
  public boolean addNewLanguage( TLanguageFile file )
  {
    // if true, the language could be inserted into the list
    boolean back = langList.addLanguageVersion( file ) ;
    if (back)
    {
      translations.increaseLanguageCapacity( 1 ) ;

      dataSetLoaded = true ;
      dataChanged = true ;

      this.fireUpdateDataStructure( true ) ;
    }

    return back ;
  }

  // --------------------------------------------------------------------------

  public TMultiLanguageEntry addTranslation( String key, String value,
                                             int langID )
  {
    return addTranslation( key, value, null, langID ) ;
  }

  public TMultiLanguageEntry addTranslation( String key, String value,
                                             TComments comment,
                                             int langID )
  {
    key = Utils.get_Unicode_I18n_String( key ) ;
    value = Utils.get_Unicode_I18n_String( value ) ;

    TMultiLanguageEntry entry = translations.get( key ) ;

    if ( entry == null )
    {
      entry = new TMultiLanguageEntry( langList.size() ) ;
      entry.setKey( key ) ;
      translations.add( entry ) ;

//      System.out.println( "key " +key) ;
    }

    entry.setTranslation( value, comment, langID ) ;
    entry.resetCachedData();
    dataChanged = true ;

    return entry ;
  }

  // returns a translation
  // langID = 0    => key
  //        = 1    => default (if available) or first available language translation
  //        = 2..x => other translations
  public String getTranslation( TMultiLanguageEntry entry, int langID )
  {
    String back = null ;
    if ( langID > -1 )
    {
      if ( langID == 0 )
      {
        back = entry.getKey() ;
      }
      else
      {
        int id = this.getMappedIndex( langID - 1 ) ;
        back = entry.getTranslationOnly( id ) ;
        if ( back == null )
        {
          back = "" ;
        }
      }
    }
    else
    {
      back = "" ;
    }

    return back ;
  }

  // returns the comments (if available)
  // langID = 0    => key
  //        = 1    => default (if available) or first available language comment
  //        = 2..x => other comments
  public TComments getComments( TMultiLanguageEntry entry, int langID )
  {
    TComments back = null ;
    if ( langID > -1 )
    {
      if ( langID != 0 ) // key = 0 => no comments
      {
        int id = this.getMappedIndex( langID - 1 ) ;
        back = entry.getComments( id ) ;
      }
    }
    return back ;
  }

  // replace a translation
  // langID = 0    => key
  //        = 1    => default (if available) or first available language translation
  //        = 2..x => other translations
  public void replaceTranslation( TMultiLanguageEntry entry, int langID,
                                  String value )
  {
    if ( entry == null )
    {
      return ;
    }

    if ( langID == 0 )
    {
      entry.setKey( value ) ;
    }
    else
    {
      int id = this.getMappedIndex( langID - 1 ) ;
      entry.setTranslation( value, id ) ;
    }
    dataChanged = true ;
    entry.resetCachedData();
    this.fireTableRowsUpdated( 0, this.getSize() - 1 ) ;
  }

  /** remove a translation entry <entry> from list */
  public void shredTranslation( TMultiLanguageEntry entry )
  {
    if ( translations.shred( entry ) )
    {
      dataChanged = true ;
      this.fireTableRowsDeleted( 0, this.getSize() - 1 ) ;
    }
  }

  /** remove the translation entry on index <index> from list */
  public void shredTranslation( int index )
  {
    if ( translations.shred( index ) )
    {
      dataChanged = true ;
      this.fireTableRowsDeleted( index, index ) ;
    }
  }

  /** denote the item <entry> as erased from list */
  public void removeTranslation( TMultiLanguageEntry entry )
  {
    if ( translations.erase( entry, true ) )
    {
      dataChanged = true ;
    }
  }

  /** denote the item with index <index> as erased from list */
  public void removeTranslation( int index )
  {
    if ( translations.erase( index, true ) )
    {
      dataChanged = true ;
    }
  }


  /** search the entry with key <key> and returns the TMultiLanguageEntry object */
  public TMultiLanguageEntry getLanguageEntry( String key )
  {
    return translations.get( key ) ;
  }

  /** returns the index of entry with key <key> or -1 if no entry in translationlist */
  public int getLanguageEntryIndex( String key )
  {
    return translations.getIndex( key ) ;
  }

  /** put a new TMultiLanguageEntry with key <key> into the translation list,
   *  the method doesn't check, if the key is already definied
   */
  public TMultiLanguageEntry addLanguageEntry( String key )
  {
    TMultiLanguageEntry entry = new TMultiLanguageEntry( langList.size() ) ;
    entry.setKey( key ) ;
    translations.add( entry ) ;

    dataChanged = true ;
    this.fireTableRowsInserted( translations.size(), translations.size() ) ;

    return entry ;
  }
// ----------------------------------------------------------------------------

  /** returns the key for the entry at index <index> */
  public String getKey( int index )
  {
    TMultiLanguageEntry entry = translations.get(index) ;
    if (entry != null)
    {
      return entry.getKey() ;
    }

    return "" ;
  }

// ----------------------------------------------------------------------------

  /** changes the sortDirection of data list */
  public void resortEntries()
  {
    translations.sortElements( 0 ) ;
  }

  // ----------------------------------------------------------------------------

  /** returns the size */
  public int getSize()
  {
    return translations.size() ;
  }

  /** returns the number of available and visible languages */
  public int getLangSize()
  {
//    return langList.size() ;
    return langList.getVisibleSize() ;
  }

  /** returns true if a default translation is available */
  public boolean hasDefaultEntry()
  {
    if ( langList.getDefaultLang() == null )
    {
      return false ;
    }

    return true ;
  }

  public TProjectFileList getAvailableLangs()
  {
    return langList ;
  }

  /** returns the status of internal data */
  public boolean hasData()
  {
    return dataSetLoaded ;
  }

  /** change the internal <dataSetLoaded> flag */
  protected void setDataLoaded(boolean value)
  {
    dataSetLoaded = value ;
  }

  public boolean isDataChanged()
  {
    return dataChanged ;
  }

  /** change the internal <dataChanged> flag */
  protected void setDataChanged(boolean value)
  {
    dataChanged = value ;
  }

  public boolean isCommentFound()
  {
    return commentFound ;
  }

  protected void setCommentFound(boolean value)
  {
    commentFound = value ;
  }

  /** checks if any translation entry on index <index> has a comment */
  public boolean hasComment( int index )
  {
    TMultiLanguageEntry dummy = translations.get( index ) ;
    if ( dummy != null )
    {
      return dummy.hasComment() ; // has any translation a comment
    }

    return false ;
  }

  /** returns true, if a validation entry is detected */
  public boolean hasValidations( int index )
  {
    TMultiLanguageEntry dummy = translations.get( index ) ;
    if ( dummy != null )
    {
      return dummy.hasValidations() ; // has any validation comment
    }

    return false ;
  }

  /** returns the number of validation comments, found for this entry */
  public int getNumberOfValidations( int index )
  {
    TMultiLanguageEntry dummy = translations.get( index ) ;
    if ( dummy != null )
    {
      return dummy.sizeOfValidations() ; // # of validation comments
    }

    return 0 ;
  }

  /** repaint table data and updates the structure (+/- columns) */
  public void fireUpdateDataStructure(boolean refreshCachedData)
  {
    this.updateMapping() ;

     // delete all cached visibility/rendering data for every entry
    if (refreshCachedData)
    {
      resetCachedData() ;
    }
    this.fireTableStructureChanged() ;
  }

  /** some visibility options has been changed -> repaint the data */
  public void fireUpdateData()
  {
    // delete all cached visibility/rendering data for every entry
    resetCachedData() ;

    this.fireTableDataChanged();
  }

  // delete all cached visibility/rendering data for every entry
  private final void resetCachedData()
  {
    for (int t = 0, len = translations.size() ; t < len ; t++)
    {
      TMultiLanguageEntry dummy = translations.getFast( t ) ;
      dummy.resetCachedData();
    }
  }


// ----------------------------------------------------------------------------
//     mapping of table coulmns -> langID
// ----------------------------------------------------------------------------

  // every visible language get its own table column
  private void updateMapping()
  {
    int len = langList.size() ;
    int vLen = langList.getVisibleSize() ; // number of visible language versions
    int off = 0 ;

    if ( langList.getDefaultLang() != null )
    {
      mapping = new int[vLen + 1] ;
      mapping[0] = TLanguageList.DEFAULT_LANG ;
      off = 1 ;
    }
    else
    {
      mapping = new int[vLen] ;
    }

    // generate the mapping
    for ( int t = 0 ; t < len ; t++ )
    {
      if ( langList.get( t ).isVisible() )
      {
        mapping[off] = t ;
        off++ ;
      }
    }

  }

  // returns the language-version-object for column index
  private TLanguageFile getMapped_LVF_Object( int index )
  {
    TLanguageFile back = null ;

    if ( index > -1 )
    {
      int col = getMappedIndex( index ) ;
      if ( col != langList.UNDEFINED_LANG )
      {
        back = langList.get( col ) ;
      }
    }

    return back ;
  }

  // returns the language-version-ID (TLanguageVersionFile) for column index
  // 0 = default lang (if available)
  // 1 = first visible lang
  // 2 = ...
  private int getMappedIndex( int index )
  {
    int back = TLanguageList.UNDEFINED_LANG ;

    if ( ( index >= 0 ) && ( index < mapping.length ) )
    {
      back = mapping[index] ;
    }
    return back ;
  }

// ----------------------------------------------------------------------------
//     AbstractDataModel
// ----------------------------------------------------------------------------

  /**
   * getColumnCount
   *
   * @return int
   */
  public int getColumnCount()
  {
    int back = getLangSize() + this.COL_OFFSET ; // status columns and key !
    if ( langList.getDefaultLang() != null )
    {
      back++ ;
    }
    return back ;
  }

  /*
   * JTable uses this method to determine the default renderer/
   * editor for each cell.  If we didn't implement this method,
   * then the last column would contain text ("true"/"false"),
   * rather than a check box.
   */
  public Class getColumnClass( int c )
  {
    return String.class ;
  }

  public String getColumnName( int col )
  {
    String back = "-" ;
    if ( col == 0 ) // status column
    {
      back = "" ;
    }
    else if (col == 1)
    {
      back = "ref" ;
    }
    else if ( col == KEY_COLUMN ) // key
    {
      back = "key" ;
    }
    else // language column
    {
      // is a default entry available ?
      TLanguageFile dummy = this.getMapped_LVF_Object( col - COL_OFFSET ) ;
      if ( dummy != null ) // entry available
      {
        back = dummy.getFullLanguageName() ;

        if ( col == COL_OFFSET ) // default entry position ?
        {
          if ( langList.getDefaultLang() != null ) // default available
          {
            back = "default" ;
            if ( langList.size() < 1 ) // but no other languages
            {
              back = "value" ;
            }
          }
        }
      }
    }
    return back ;
  }

  /** returns the language object for this column or null if the column
   *  contains other data
   */
  public TLanguageFile getColumnLanguage(int column)
  {
     return (getMapped_LVF_Object( column - COL_OFFSET )) ;
  }

  // --------------------------------------------------------------------------
  /**
   * getRowCount
   *
   * @return int
   */
  public int getRowCount()
  {
    return getSize() ;
  }

  /**
   * getValueAt
   *
   * @param rowIndex int
   * @param columnIndex int (not used)
   * @return Object of type TSingleLanguageEntry
   */
  public Object getValueAt( int rowIndex, int columnIndex )
  {
    return translations.get( rowIndex ) ;
  }

  // --------------------------------------------------------------------------
  /** search from index <row> the next incomplete entry (inclomplete language <langID>)*/
  // langID  = 1    => default (if available) or first available language translation
  //         = 2..x => other translations
  // if searchUP then perform a bottom up search
  public int getNextIncompleteIndex(int row, int langID, boolean bottomUp)
  {
     if (langID > 0)
     {
       int id = this.getMappedIndex( langID - 1 ) ;
       if (bottomUp) // prev entry
         return translations.getPrevIncompleteIndex( row-1, id ) ;
       else // next entry
         return translations.getNextIncompleteIndex( row+1, id ) ;
     }

     return -1 ;
  }





}
