/*
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

// created by : r.nagel 1.6.2005
//
// function : check all key-entries
//
// todo     : run as background thread
//
//
// modified :

package net.sf.langproper.wizard.validation ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;

public class TValidationChecker
{
  private TValidationsList valList ;

  private boolean look4EqualKeys = true ;

  public TValidationChecker()
  {
    valList = new TValidationsList() ;
  }

  public TValidationsList getValidationsList()
  {
    return valList ;
  }

  // -------------------------------------------------------------------------

  public void check()
  {
    valList.clear() ;

    TValidationWorkingSet workSet = new TValidationWorkingSet( valList ) ;

    TranslationList data = TGlobal.projects.getCurrentData().getTranslations() ;

    TValidationTypeHandler types = valList.getTypes() ;
    int len = data.size() ;

    // Each entry must be checked by all others
    // 1. loop, take each entry as reference
    for ( int ref = 0 ; ref < len ; ref++ )
    {
      TMultiLanguageEntry refEntry = data.getFast( ref ) ;
      TNormString refKey = new TNormString( refEntry.getKey() ) ;

      // fill workset with reference data
      workSet.clear() ;
      workSet.setRefEntry( refEntry, ref ) ;
      workSet.setRefKey( refKey ) ;

      // 2. loop, check reference with all other entries
      // only succeeding entries of the reference entry will be used
      for ( int loop = ref + 1 ; loop < len ; loop++ )
      {
        TMultiLanguageEntry loopEntry = data.getFast( loop ) ;

        TNormString lKey = new TNormString( loopEntry.getKey() ) ;

        // fill the workset with current data
        workSet.clearLoop() ;
        workSet.setLoopEntry( loopEntry, loop ) ;
        workSet.setLoopKey( lKey ) ;

        // call all registered validators
        types.performAll( workSet );

      }
    } // for

    valList.update() ;

  }

}
