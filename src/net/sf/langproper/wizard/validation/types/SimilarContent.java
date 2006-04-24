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


// created by : r.nagel 15.09.2005
//
// function : same content check,
//            2 different keys have many identical translations
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.validation.types ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.wizard.validation.* ;

public class SimilarContent
    extends TValidationType
{
  /**
   *
   * @param wSet TValidationWorkingSet
   * @todo Implement this net.sf.langproper.wizard.validation.TValidationType
   *   method
   */
  public void check( TValidationWorkingSet wSet )
  {
    TMultiLanguageEntry ref = wSet.getRefEntry() ;
    TMultiLanguageEntry dummy = wSet.getLoopEntry() ;

    TLanguageList langList = TGlobal.projects.getLanguages() ;

    int len = langList.getSize() ;
    int sim = 0 ;

    for(int t = 0 ; t < len ; t++)
    {
      int h1 = getHash( ref.getTranslationForIndex(t) ) ;
      int h2 = getHash( dummy.getTranslationForIndex(t) ) ;

      if ( (h1 == h2) && (h1 > 0))
        sim++ ;
    }

    // > len/2 (half) equal translations, but more then one!!!!!!!
    if ( (sim > len/2) && (sim > 1))
      insert( wSet );
  }

  private int getHash(String data)
  {
    if (data != null)
      return data.hashCode() ;

    return 0 ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.wizard.validation.TValidationType
   *   method
   */
  public String getDescription()
  {
    return "checks if some keys have similar content" ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.wizard.validation.TValidationType
   *   method
   */
  public String getMessage()
  {
    return "the entries (different keys) have similar translations" ;
  }

  /**
   *
   * @return String
   * @todo Implement this net.sf.langproper.wizard.validation.TValidationType
   *   method
   */
  public String getName()
  {
    return "similar content" ;
  }
}
