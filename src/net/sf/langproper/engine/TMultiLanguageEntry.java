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
// function : contains a key and all appendant translations (and comments)
//
//            some words about comments handling :
//            every value (key specific translation) can have comments
//            -> for every translation there is a comments object
//
// todo     :
//
// modified : 21.02.2006 kiar
//            - handling of leading space before the keys


package net.sf.langproper.engine ;

import java.util.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.wizard.validation.* ;
import net.sf.langproper.scanner.* ;

public class TMultiLanguageEntry
{

  // used for a translation
  private class TValueEntry
  {
    private String value ;
    private TComments comment ; // every key can have some comments

    // it's a leading string into the file before key=value pair
    // used for the original file saver
    private String leading ;

    public TValueEntry( String pValue, TComments pComment )
    {
      value = pValue ;
      comment = pComment ;
    }

    public void set( String pValue, TComments pComment )
    {
      value = pValue ;
      // !!!!!!!!!!! add or replace the comment ?
      if ( pComment != null )
      {
        comment = pComment ;
      }
    }
  }

  // --------------------------------------------------------------------------

  public static final int
      STATE_UNCHANGED = 2,
  STATE_MODIFIED = 5,
  STATE_ERASED = 7
      ;

  private String keyData ;

  private TValueEntry defValue ;
  private TValueEntry values[] ;

  private transient int modStatus = STATE_UNCHANGED ; // changed or modified

  private int numberOfTranslations ; // size of translation array

  private transient boolean hasAnyComment = false ; // has any translation a comment ?

  private transient Vector validations ; // all validationinfo objects for this entry

  private TKeyRef keyRefs = null ;

  // some necessary informations for the table renderer
  private TCachedVisibilityData visCache = new TCachedVisibilityData() ;

  /** creates a new Object with <size> empty translations */
  public TMultiLanguageEntry( int size )
  {
    values = new TValueEntry[size] ;
    numberOfTranslations = size ;

    validations = new Vector() ;
  }

  /** increases the length of available translations newSize = oldSize + incNumb */
  public void increaseCapacity( int incNumb )
  {
    if ( incNumb > 0 )
    {
      int newLen = numberOfTranslations + incNumb ;
      TValueEntry newArray[] = new TValueEntry[newLen] ;
      System.arraycopy( values, 0, newArray, 0, numberOfTranslations ) ;
      numberOfTranslations = newLen ;
      values = newArray ;
      visCache.reset() ; // set all cached data to false
    }
  }

  // --------------------------------------------------------------------------

  private TValueEntry getValueEntry( int langID )
  {
    TValueEntry back ;
    if ( ( langID >= 0 ) && ( langID < numberOfTranslations ) )
    {
      back = values[langID] ;
    }
    else
    {
      back = defValue ;
    }

    return back ;
  }

  // --------------------------------------------------------------------------

  public void setTranslation( String value, int langID )
  {
    setTranslation( value, null, langID ) ;
  }

  /** Set a new value for translation with ID <langID>
   *  If the param <comment> is null, the old comment isn't affected. Otherwise
   *  the old comment is overwritten.
   */
  public void setTranslation( String value, TComments comment, int langID )
  {
    if ( langID < 0 )
    {
      if ( defValue == null )
      {
        defValue = new TValueEntry( value, comment ) ;
      }
      else
      {
        defValue.set( value, comment ) ;
      }
    }
    else if ( langID < numberOfTranslations )
    {
      TValueEntry dummy = values[langID] ;
      if ( dummy == null )
      {
        values[langID] = new TValueEntry( value, comment ) ;
      }
      else
      {
        dummy.set( value, comment ) ;
      }
    }

    // is there any comment
    if ( comment != null )
    {
      hasAnyComment = comment.hasComments() ;
    }
    else
    {
      hasAnyComment = false ;
    }
  }

  /** returns the language dependend translation or if it is not available the
   *  default translation or the key value
   */
  public String getTranslation( int langID )
  {
    if ( ( langID >= 0 ) && ( langID < numberOfTranslations ) )
    {
      TValueEntry dummy = values[langID] ;
      if ( dummy != null )
      {
        return dummy.value ;
      }
    }

    // no translation but a default value
    if ( defValue != null )
    {
      return defValue.value ;
    }

    // no default and translation found
    return this.getKey() ;
  }

  /** returns a translation or null if nothing was found */
  public String getTranslationOnly( int langID )
  {
    if ( ( langID >= 0 ) && ( langID < numberOfTranslations ) )
    {
      TValueEntry dummy = values[langID] ;
      if ( dummy != null )
      {
        return dummy.value ;
      }
    }
    else if ( TLanguageList.DEFAULT_LANG == langID )
    {
      // no translation but a default value
      if ( defValue != null )
      {
        return defValue.value ;
      }
    }

    return null ;
  }

  /** returns a translation without any check */
  public String getTranslationFast( int langID )
  {
    TValueEntry dummy = values[langID] ;
    if ( dummy != null )
    {
      return dummy.value ;
    }

    return null ;
  }

  /** returns a translation without any array bounds check,
   *  the langID is identical to the TLanguageList ids
   *  -> if default translation is available => id = 0
   *  */
  public String getTranslationForIndex( int langID )
  {
    TValueEntry dummy = null ;

    if (defValue != null)
    {
      if (langID > 0)
        dummy = values[langID-1] ;
      else
        dummy = defValue ;
    }
    else
    {
      dummy = values[langID] ;
    }

    if (dummy != null)
      return dummy.value ;

    return "" ;
  }

  /** returns true, if a translation for <langID> exists */
  public boolean hasTranslation( int langID )
  {
    String dummy = getTranslationOnly( langID ) ;
    if ( dummy != null )
    {
      if ( dummy.length() > 0 )
      {
        return true ;
      }
    }
    return false ;
  }

  /** Fill all empty translations with non-empty translation of the <other>
   * object and return true if something could be imported.*/
  public boolean importTranslations( TMultiLanguageEntry other)
  {
    boolean imported = false ;

    if (other != null)
    {
      // check the default value
      if ( (defValue == null) || (defValue.value == null)
           || (defValue.value.length() < 1))
      {
        if (other.defValue != null)
        {
          if (other.defValue.value != null)
          {
            // create a new defValue without a comment
            defValue = new TValueEntry( other.defValue.value, null ) ;
            imported = true ;
          }
        }
      }

      // check all other translations
      TValueEntry oValues[] = other.values ;
      int len = values.length ;
      // other has the same size -> it seems to be compatible
      if ( (oValues != null) && (oValues.length == values.length))
      {
        for( int t = 0 ; t < len ; t++)
        {
          boolean fillIt = false ;

          TValueEntry dummy = values[t] ;

          // check, if the value at the current index t is empty
          if (dummy != null)
          {
            if (dummy.value == null)
              fillIt = true ;
            else if (dummy.value.length() < 1)
              fillIt = true ;
          }
          else
          {
            fillIt = true ;
          }

          // try to get the translation from <other>
          if (fillIt)
          {
            TValueEntry oDummy = oValues[t] ;
            if (oDummy != null)
            {
              if (oDummy.value != null)
              {
                dummy.value = oDummy.value ;
                imported = true ;
              }
            }
          }
        }
      }
    }

    if (imported)
    {
      resetCachedData() ;
    }

    return imported ;
  }
  // --------------------------------------------------------------------------

  /** set the comments for translation */
  public void setComments( TComments comment, int langID )
  {
    TValueEntry entry = getValueEntry( langID ) ;
    if ( entry != null )
    {
      entry.comment = comment ;
    }
  }

  /** get all comments for translation */
  public TComments getComments( int langID )
  {
    TValueEntry entry = getValueEntry( langID ) ;
    if ( entry != null )
    {
      return entry.comment ;
    }

    return null ;
  }

  /** returns true, if the translation (langID) contains a comment */
  public boolean hasComment( int langID )
  {
    TValueEntry entry = getValueEntry( langID ) ;
    if ( entry != null )
    {
      if ( entry.comment != null )
      {
        return true ;
      }
    }

    return false ;
  }

  /** returns true, if any translation contains a comment */
  public boolean hasComment()
  {
    return hasAnyComment ;
  }

  public String getCommentsInfo()
  {
    StringBuffer sBuf = visCache.getComments() ;
    if ( sBuf != null )
    {
      return sBuf.toString() ;
    }

    return "" ;
  }

  // --------------------------------------------------------------------------
  public String getKey()
  {
    return keyData ;
  }

  public void setKey( String pKey )
  {
    this.keyData = pKey ;
  }

  // --------------------------------------------------------------------------

  public int getModifyStatus()
  {
    return modStatus ;
  }

  public void setModifyStatus( int pStatus )
  {
    this.modStatus = pStatus ;
  }

  // --------------------------------------------------------------------------

  /** returns true if all language translation entries are available */
  public boolean isComplete()
  {
    return visCache.getAllComplete() ;
  }

  /** returns true if all visible language translation entries are available */
  public boolean isVisibleComplete()
  {
    return visCache.getVisibleComplete() ;
  }

  /** return true, if the internal status = erased */
  public boolean isErased()
  {
    if ( modStatus == STATE_ERASED )
    {
      return true ;
    }

    return false ;
  }

  // --------------------------------------------------------------------------
  // --- handling of leading string sequences before a key=value pair ---------
  // --------------------------------------------------------------------------

  public void setLeading( String tmp, int langID )
  {
    TValueEntry entry = getValueEntry( langID ) ;
    if ( entry != null )
    {
      entry.leading = tmp ;
    }
  }

  public String getLeading( int langID )
  {
    TValueEntry entry = getValueEntry( langID ) ;
    if ( entry != null )
    {
      return entry.leading ;
    }
    return null ;
  }

  // --------------------------------------------------------------------------


  /** returns the number of available and visible translations */
  public int getEmptyTranslationCount()
  {
    if ( !visCache.isValid() )
    {
      updateCachedData( TGlobal.projects.getLanguages() ) ;
    }

    return visCache.getVisibleAndComplete() ;
  }

  // --------------------------------------------------------------------------
  // ------------- handling of code references --------------------------------
  // --------------------------------------------------------------------------

  public void setKeyRef( TKeyRef ref )
  {
    keyRefs = ref ;
  }

  public TKeyRef getKeyRef()
  {
    return keyRefs ;
  }

  // --------------------------------------------------------------------------
  // ------------- handling of validations ------------------------------------
  // --------------------------------------------------------------------------

  /** delete all validations */
  public void clearValidations()
  {
    validations.clear() ;
  }

  /** returns the number of validations */
  public int sizeOfValidations()
  {
    return validations.size() ;
  }

  public boolean hasValidations()
  {
    if ( validations.size() > 0 )
    {
      return true ;
    }

    return false ;
  }

  /** put a new validation into */
  public void addValidation( TValidationInfo data )
  {
    if ( data != null )
    {
      validations.add( data ) ;
    }
  }

  // --------------------------------------------------------------------------
  // ------------- handling of renderer infos ---------------------------------
  // ---------------  cachedVisibilityInfos -----------------------------------
  // --------------------------------------------------------------------------

  /** check all entries and update the internal informations in the
   *  TCachedVisibilityInfos data object
   */

  public void updateCachedData( TLanguageList langList )
  {
    boolean all = true ;
    boolean part = true ;
    int cVisible = 0 ;
    int cComplete = 0 ;
    StringBuffer sBuf = visCache.getComments() ;
    sBuf.delete( 0, sBuf.length() ) ;

    int t = 0 ;
    while ( t < numberOfTranslations )
    {
      boolean vis = langList.isVisible( t ) ;

      // no entry
      if ( values[t] == null )
      {
        all = false ;
        if ( vis )
        {
          part = false ;
        }
      }
      else // entry is valid
      {
        String str = values[t].value ;
        if ( str == null )
        {
          all = false ;
          if ( vis )
          {
            part = false ;
          }
        }
        else if ( str.length() < 1 )
        {
          all = false ;
          if ( vis )
          {
            part = false ;
          }
        }
        else // entry has some data
        {
          cComplete++ ;
          if ( vis )
          {
            cVisible++ ;
          }
        }

        // check for comments
        TComments com = values[t].comment ;
        if ( com != null )
        {
          // insert the language iso code into the toolTip string
          if ( sBuf.length() > 0 )
          {
            sBuf.append( ", " ) ;
          }
          TLanguageFile lvf = langList.getData( t ) ;
          if ( lvf != null )
          {
            sBuf.append( lvf.getLanguageCode() ) ;
          }
        }
      }
      t++ ;
    }

    visCache.setAllComplete( all ) ;
    visCache.setVisibleComplete( part ) ;
    visCache.setComplete( cComplete ) ;
    visCache.setVisibleAndComplete( cVisible ) ;
//    visCache.setComments(sBuf);
    visCache.setValid( true ) ;
  }

  /** reset all cached infos and mark as invalid */
  public void resetCachedData()
  {
    visCache.reset() ;
  }

  public boolean isCachedDataValid()
  {
    return visCache.isValid() ;
  }
}
