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
// function :
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.validation ;

import net.sf.langproper.engine.*;

public class TValidationWorkingSet
{
  private TValidationsList valList ;

  private TMultiLanguageEntry refEntry ;
  private TMultiLanguageEntry loopEntry ;

  private int refIndex ;
  private int loopIndex ;

  private TNormString refKey ;
  private TNormString loopKey ;

  /** counter mechanism */
  private int counter = 0 ;

  public TValidationWorkingSet(TValidationsList pValList)
  {
    this.valList = pValList;
  }

  public void clear()
  {
    refEntry = null ;
    refIndex = -1 ;
    refKey = null ;

    clearLoop() ;
  }

  public void clearLoop()
  {
    loopEntry = null ;
    loopIndex = -1 ;
    loopKey = null ;
  }

  // -------------------------------------------------------------------------

  public TMultiLanguageEntry getRefEntry()
  {
    return refEntry;
  }

  public void setRefEntry(TMultiLanguageEntry pRefEntry, int index)
  {
    this.refEntry = pRefEntry;
    this.refIndex = index ;
  }

  public int getRefIndex()
  {
    return refIndex;
  }

  // -------------------------------------------------------------------------

  public TMultiLanguageEntry getLoopEntry()
  {
    return loopEntry;
  }

  public void setLoopEntry(TMultiLanguageEntry pLoopEntry, int index)
  {
    this.loopEntry = pLoopEntry;
    this.loopIndex = index ;
  }

  public int getLoopIndex()
  {
    return loopIndex;
  }

  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public TNormString getRefKey()
  {
    return refKey;
  }

  public void setRefKey(TNormString pRefKey)
  {
    this.refKey = pRefKey;
  }

  // -------------------------------------------------------------------------

  public TNormString getLoopKey()
  {
    return loopKey;
  }

  public void setLoopKey(TNormString pLoopKey)
  {
    this.loopKey = pLoopKey;
  }

  // -------------------------------------------------------------------------

  public TValidationsList getValList()
  {
    return valList;
  }

  public void setValList(TValidationsList pValList)
  {
    this.valList = pValList;
  }

  // -------------------------------------------------------------------------

  /** internal counter */
  public int getNextCounter()
  {
    return counter++ ;
  }

}
