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

// created by : r.nagel 21.02.2006
//
// function : Listmodel, contains all available fonts
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options.fonts ;

import javax.swing.* ;
import javax.swing.event.* ;
import java.awt.*;
import java.util.*;

public class FontList implements ListModel
{
  private Vector liste = new Vector() ;

  public FontList()
  {
    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment() ;

    String fonts[] = gEnv.getAvailableFontFamilyNames() ;
    for(int t = 0 ; t < fonts.length ; t++)
    {
      liste.add( new FontListItem( fonts[t] ) ) ;
    }
  }

  /**
   * Adds a listener to the list that's notified each time a change to the data
   * model occurs.
   *
   * @param l the <code>ListDataListener</code> to be added
   * @todo Implement this javax.swing.ListModel method
   */
  public void addListDataListener( ListDataListener l )
  {
  }

  /**
   * Returns the value at the specified index.
   *
   * @param index the requested index
   * @return the value at <code>index</code>
   * @todo Implement this javax.swing.ListModel method
   */
  public Object getElementAt( int index )
  {
    return liste.get( index ) ;
  }

  /**
   * Returns the length of the list.
   *
   * @return the length of the list
   * @todo Implement this javax.swing.ListModel method
   */
  public int getSize()
  {
    return liste.size() ;
  }

  /**
   * Removes a listener from the list that's notified each time a change to the
   * data model occurs.
   *
   * @param l the <code>ListDataListener</code> to be removed
   * @todo Implement this javax.swing.ListModel method
   */
  public void removeListDataListener( ListDataListener l )
  {
  }
}
