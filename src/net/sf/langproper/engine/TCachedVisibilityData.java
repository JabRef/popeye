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

// created by : r.nagel 11.07.2005
//
// function : some (cached) informations about the entry
//            used by the JTable-Renderer (TPropTable)
//
// todo     :
//
// modified :

package net.sf.langproper.engine ;

public class TCachedVisibilityData
{
  /** data are valid/invalid */
  private boolean valid = false ;

  /** all entries are complete */
  private boolean allComplete = false ;

  /** all visible entries are complete */
  private boolean visibleComplete = false ;

  private StringBuffer comments = new StringBuffer() ;

  // some counter
  private int visibleAndComplete = 0 ;
  private int complete = 0 ;

  /** delete all data */
  public void reset()
  {
    allComplete = false ;
    visibleComplete = false ;
    valid = false ;
    visibleAndComplete = 0 ;
    complete = 0 ;
    comments.delete(0, comments.length()) ;
  }

  // -------------------------------------------------------------------------

  public boolean getAllComplete()
  {
    return allComplete;
  }

  public void setAllComplete(boolean pAllComplete)
  {
    this.allComplete = pAllComplete;
  }

  // --------------------------------------------------------------------------

  public boolean getVisibleComplete()
  {
    return visibleComplete;
  }

  public void setVisibleComplete(boolean pVisibleComplete)
  {
    this.visibleComplete = pVisibleComplete;
  }

  // --------------------------------------------------------------------------

  public boolean isValid()
  {
    return valid;
  }

  public void setValid(boolean pValid)
  {
    this.valid = pValid;
  }

  // --------------------------------------------------------------------------

  /** number of visible translations */
  public int getVisibleAndComplete()
  {
    return visibleAndComplete;
  }

  public void setVisibleAndComplete(int pVisibleAndComplete)
  {
    this.visibleAndComplete = pVisibleAndComplete;
  }

  // --------------------------------------------------------------------------

  /** number of available translations */
  public int getComplete()
  {
    return complete;
  }

  public void setComplete(int pComplete)
  {
    this.complete = pComplete;
  }


  public StringBuffer getComments()
  {
    return comments;
  }
  public void setComments(StringBuffer pComments)
  {
    this.comments = pComments;
  }
}
