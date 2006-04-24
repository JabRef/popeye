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

// created by : r.nagel 12.07.2005
//
// function : contains all sorting methods for the table
//
// todo     :
//
//
// modified :

package net.sf.langproper.engine.sort ;

import java.util.*;

public class SortEngine
{
  private Vector sorter ;

  private final int max = 5 ;
  private int index = 0 ;

  public SortEngine()
  {
    sorter = new Vector(max) ;
    sorter.add( new NoSort() ) ;
    sorter.add( new TKeyDownComparator() );
    sorter.add( new TKeyUpComparator() ) ;
    sorter.add( new TransCountDown() ) ;
    sorter.add( new TransCountUp() ) ;
  }

  /** returns the active sort module */
  public TSorter getActive()
  {
    return (TSorter) sorter.get(index) ;
  }

  /** returns the internal ID of the active sorting module */
  public int getActiveID()
  {
    return index ;
  }

  /** set the active sorting module */
  public void setActiveID(int numb)
  {
    if ((numb > -1) && (numb < max))
      index = numb ;
  }


  /** returns the next sorting module */
  public TSorter getNext()
  {
    index++ ;
    if (index >= max)
    {
      index = 1 ;
    }

    return (TSorter) sorter.get(index) ;
  }

  /** set "unsorted" as the active modul */
  public void setUnsorted()
  {
    index = 0 ;
  }

  /** returns the number of sorting modules */
  public int getSize()
  {
    return max ;
  }

  /** returns the sorting module with index <numb> */
  public TSorter get(int numb)
  {
    if ((numb > -1) && (numb < max))
      return (TSorter) sorter.get(numb) ;

    return (TSorter) sorter.get(0) ;
  }
}
