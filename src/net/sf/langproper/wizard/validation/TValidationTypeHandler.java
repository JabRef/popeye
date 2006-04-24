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

// created by : r.nagel 3.6.2005
//
// function : contains all types of validation errors/messages
//
// todo     :  methode for translation of a numerical type to a string description
//
// modified :

package net.sf.langproper.wizard.validation ;

import java.util.*;
import net.sf.langproper.wizard.validation.types.*;

public class TValidationTypeHandler
{
//  public static final TValidationTypeHandler current = new TValidationTypeHandler();


  private Vector validations ;

  public TValidationTypeHandler()
  {
     validations = new Vector() ;
     add( new EqualKeys() ) ;
     add( new LowerKeys() ) ;
     add( new SimilarContent() ) ;
  }

  // insert a new validator
  private void add(TValidationType type)
  {
    type.setID( validations.size() );
    validations.add( type );
  }

  public int getSize()
  {
    return validations.size() ;
  }

  public TValidationType get(int index)
  {
    if ( (index > 0) && (index < validations.size()) )
      return (TValidationType) validations.get(index) ;

    return null ;  // !!!! return unknown type
  }


  // perform all validators
  public void performAll(TValidationWorkingSet wSet)
  {
    for(int t = 0, len = validations.size() ; t < len ; t++)
    {
      TValidationType validator = (TValidationType) validations.get(t) ;

      validator.check( wSet );
    }
  }

  // remove all collected informations
  public void clearAll()
  {
    for(int t = 0, len = validations.size() ; t < len ; t++)
    {
      TValidationType validator = (TValidationType) validations.get(t) ;

      validator.clear() ;
    }

  }
}
