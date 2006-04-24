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
// function : container for the wizard data
//
// todo     :
//
// modified :

package net.sf.langproper.wizard ;

import java.util.*;
import com.nexes.wizard.*;

public class WizardData
{
  private Wizard wizardInst = null ;
  private Object data = null ;

  /** extra data */
  private Hashtable props = new Hashtable(5) ;

  public WizardData(Wizard wizard)
  {
    wizardInst = wizard ;
  }

  /** returns the wizard instance for this data */
  public Wizard getWizard()
  {
    return wizardInst ;
  }

  public Object getData()
  {
    return data ;
  }

  public void setData( Object pData)
  {
    data = pData ;
  }

  /** read an extra wizard property */
  public Object getProperty( String key )
  {
    if (key != null)
      return props.get( key ) ;

    return null ;
  }

  /** put into (value != null) or removes from (value == null) property list */
  public void setProperty( String key, Object value )
  {
    if ( (key != null))
    {
      if (value != null) // put data into the list
      {
        props.put( key, value ) ;
      }
      else  // value == null => remove the data from list
      {
        props.remove(key) ;
      }
    }
  }
}
