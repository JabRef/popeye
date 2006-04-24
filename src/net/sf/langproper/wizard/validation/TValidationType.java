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
// function : a validation
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.validation ;

import javax.swing.tree.*;
import net.sf.langproper.engine.*;

public abstract class TValidationType extends DefaultMutableTreeNode
{
   /** a unique id */
   private int typeID ;

   /** node is registered */
   private transient boolean isRegistered = false ;

   /** internal node */
   private transient TValidationInfo node = null ;

   /** name of validator */
   public abstract String getName() ;

   /** description of Validator */
   public abstract String getDescription() ;

   /** message for a validation of this type */
   public abstract String getMessage() ;

   /** check the data in workingSet */
   public abstract void check(TValidationWorkingSet wSet) ;


   /** override, if the type can fix the problem */
   public boolean canFix()
   {
     return false ;
   }

   public void fixIt()
   {
     // empty methode
   }

   // get the internal registration and reference id
   public final void setID(int id)
   {
     typeID = id ;
   }

   // returns the internal registration and reference id
   public final int getID()
   {
     return typeID ;
   }

   /** name of the node */
   public String toString()
   {
     return getName() ;
   }

   /** remove all informations */
   public void clear()
   {
     isRegistered = false ;
     node = null ;
     removeAllChildren();
   }

   // register data on root
   private void register( TValidationWorkingSet wSet )
   {
     if (!isRegistered)
     {
       wSet.getValList().insert( this ) ;
       isRegistered = true ;
     }
   }

   /** insert a new validation message and make links to every entry */
   public void insert( TValidationWorkingSet wSet )
   {
     // insert info into key node
//    e1.addValidation(data);
//    e2.addValidation(data);

     TMultiLanguageEntry refKey = wSet.getRefEntry() ;
     TMultiLanguageEntry loopKey = wSet.getLoopEntry() ;

     // create a validation and insert the data
     TValidationInfo data = new TValidationInfo(this, wSet.getNextCounter() ) ;
     data.add( new TValueNode( refKey ) );
     if (loopKey != null)
       data.add( new TValueNode( loopKey ) );
     add(data);

     // link to the key entries from Translationtable
     refKey.addValidation( data ) ;
     if (loopKey != null)
       loopKey.addValidation( data ) ;

     register(wSet) ;
   }

   /** append the data onto the last node or if no node exists, create one */
   public void append( TValidationWorkingSet wSet)
   {

   }

}
