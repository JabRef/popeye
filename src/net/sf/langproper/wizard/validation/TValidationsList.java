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
// function : a list (tree) of all detected validation messages
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.validation ;

import javax.swing.tree.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;

public class TValidationsList extends DefaultTreeModel
{
  private DefaultMutableTreeNode myRoot ;

  private TValidationTypeHandler types ;

  public TValidationsList()
  {
    super( new DefaultMutableTreeNode("discrepancies") ) ;
    myRoot = (DefaultMutableTreeNode) this.getRoot() ;
    types = new TValidationTypeHandler() ;
  }

  public void update()
  {
    nodeStructureChanged(myRoot);
  }

  /** clear the list of all validations and delete every reference
   *  in MultiLanguageEntry (TranslationList)
   */
  public synchronized void clear()  // NOT threadsafe (if any other thread removes or inserts new elements !)
  {
    myRoot.removeAllChildren();

    // delete all informations
    types.clearAll();

    // clear all validations beetween the translations (TranslationList)
    TranslationList data = TGlobal.projects.getCurrentData().getTranslations() ;
    int len = data.size() ;
    for (int ref = 0 ; ref < len ; ref++)
    {
      TMultiLanguageEntry refEntry = data.getFast( ref ) ;
      refEntry.clearValidations();
    }
  }

  public synchronized void insert(TValidationType node)
  {
    myRoot.add( node );
  }

  public TValidationTypeHandler getTypes()
  {
    return this.types ;
  }

}
