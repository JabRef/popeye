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
// function : contains a list of all keys, every key has several translations
//            -> list of TMultiLanguageEntry
//
// todo     :
//
// modified :

package net.sf.langproper.engine ;

import java.util.* ;

import net.sf.langproper.gui.* ;

public class TranslationList
{

  public static final int
      SORT_NOTHING = -1,
      SORT_UPWARD = 8,
      SORT_DOWNWARD = 11,
      SORT_TRANSLATIONSCOUNT_UPWARD = 13,
      SORT_TRANSLATIONSCOUNT_DOWNWARD = 14
      ;

  private Vector translations ;

  public TranslationList()
  {
    translations = new Vector() ;
  }


  /** returns the first entry with key <key> */
  public TMultiLanguageEntry get( String key )
  {
    if ( key == null)
      return null ;

    TMultiLanguageEntry back = null ;
    int keyHash = key.hashCode() ;

    Enumeration myEnum = translations.elements() ;
    while ( (myEnum.hasMoreElements()) && (back == null) )
    {
      TMultiLanguageEntry dummy = (TMultiLanguageEntry) myEnum.nextElement() ;
      if (dummy.getKey().hashCode() == keyHash)
      {
        back = dummy ;
      }
    }

    return back ;
  }

  /** returns the entry on index <index> */
  public TMultiLanguageEntry get( int index )
  {
    TMultiLanguageEntry back = null ;

    if ( (index >= 0) && (index < translations.size()) )
    {
      back = (TMultiLanguageEntry) translations.get(index) ;
    }

    return back ;
  }

  /** returns the entry on index <index> without any check*/
  public TMultiLanguageEntry getFast( int index )
  {
    return (TMultiLanguageEntry) translations.get(index) ;
  }


  /** returns the index of entry with key <key> or -1 if no entry in list */
  public int getIndex( String key )
  {
    int back = -1 ;

    if ( key != null)
    {
      if (key.length() > 0)
      {
        int keyHash = key.hashCode() ;
        int counter = 0 ;

        Enumeration myEnum = translations.elements() ;
        while ( ( myEnum.hasMoreElements() ) && ( back < 0 ) )
        {
          TMultiLanguageEntry dummy = ( TMultiLanguageEntry ) myEnum.
              nextElement() ;
          if ( dummy.getKey().hashCode() == keyHash )
          {
            back = counter ;  // found -> save index
          }
          counter++ ;
        }
      }
    }
    return back ;
  }


  /** Adds a new entry into the list.
   *  There is no check whether the element is already contained.
   */
  public void add( TMultiLanguageEntry entry )
  {
     translations.add( entry ) ;
  }

  // --------------------------------------------------------------------------

  /** removing without revoking */
  public boolean shred( TMultiLanguageEntry entry )
  {
    if (entry == null)
      return false ;

    // try to find entry in list and remove it
    return shred( getIndex( entry.getKey() )) ;
  }

  /** removing without revoking */
  public boolean shred( int index )
  {
    // valid entry index
    if ( (index > -1) && (index < translations.size()) )
    {
      translations.remove( index ) ;
      return true ;
    }

    return false ;
  }


  /** Denote the as erased. If swap flag is set to true, the status
   *  of the entry will be swap between changed and erased */
  public boolean erase( TMultiLanguageEntry entry, boolean swapStatus )
  {
    if (entry == null)
      return false ;

    // denote the entry, if swapMode swap between Modified and Erased
    if ( (entry.getModifyStatus() == TMultiLanguageEntry.STATE_ERASED) &&
         (swapStatus) )
    {
      entry.setModifyStatus( TMultiLanguageEntry.STATE_MODIFIED );
    }
    else
    {
      entry.setModifyStatus( TMultiLanguageEntry.STATE_ERASED ) ;
    }
    return true ;
  }

  /** Denote the as erased. If swap flag is set to true, the status
   *  of the entry will be swap between changed and erased */
  public boolean erase( int index, boolean swapStatus )
  {
    return erase( get(index), swapStatus ) ;
  }


  /** clear the list, remove all entries */
  public void clear()
  {
    translations.clear();
  }

  // --------------------------------------------------------------------------

  /** returns an Enumeration of all visible AND erased items ! */
  public Enumeration allElements()
  {
    return translations.elements() ;
  }

  /** returns an Enumeration all visible items (removed items are not not included) */
  public Enumeration elements()
  {
    return new EntryEnumeration( this ) ;
  }


  /** sorts all entries - now only the key sorting is implemented
   *  and the langID is ignored */
  public void sortElements(int langID)
  {
    Object eList[] = translations.toArray() ;
    java.util.Arrays.sort(eList, GUIGlobals.sortDirection.getActive() );

      translations.clear();
      for (int t = 0 ; t < eList.length ; t++)
      {
        translations.add(eList[t]) ;
      }
  }

  /** returns the size */
  public int size()
  {
    return translations.size() ;
  }

  /** increases the language capacity of all elements */
  public void increaseLanguageCapacity( int numb )
  {
    if ( numb > 0)
    {
      Enumeration myEnum = translations.elements() ;
      while ( myEnum.hasMoreElements() )
      {
        TMultiLanguageEntry dummy = ( TMultiLanguageEntry ) myEnum.nextElement() ;
        dummy.increaseCapacity(numb);
      }
    }
  }


  /** search from index <row> the next incomplete language entry <col>
   * keys not supported */
  public int getNextIncompleteIndex(int row, int col)
  {
     boolean notFound = true ;
     int len = translations.size() ;
     int t = row ;
     int back = -1 ;
     while ( (t < len) && notFound)
     {
       TMultiLanguageEntry dummy = (TMultiLanguageEntry) translations.get(t) ;
       String str = dummy.getTranslationOnly(col) ;

       if (str == null)
       {
         notFound = false ;
         back = t ;
       }
       else if (str.length() < 1)
       {
         notFound = false ;
         back = t ;
       }
       else
       {
         t++ ;
       }
     }
     return back ;
  }

  /** search from index <row> a previous incomplete language entry <col>
   * keys not supported */
  public int getPrevIncompleteIndex(int row, int col)
  {
     boolean notFound = true ;
     int len = translations.size() ;
     int t = row ;
     int back = -1 ;
     while ( (t >= 0) && notFound)
     {
       TMultiLanguageEntry dummy = (TMultiLanguageEntry) translations.get(t) ;
       String str = dummy.getTranslationOnly(col) ;

       if (str == null)
       {
         notFound = false ;
         back = t ;
       }
       else if (str.length() < 1)
       {
         notFound = false ;
         back = t ;
       }
       else
       {
         t-- ;
       }
     }
     return back ;
  }

}
