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


// created by : r.nagel 02.05.2005
//
// function : base dialog class, some helpfull methods (modal handling)
//
// todo     :
//
// modified :

package net.sf.quercus;

import javax.swing.* ;
import net.sf.langproper.gui.*;

public class TInternalDialog extends JDialog
{

  public static final int
      MR_NOTHING  = -1,
      MR_OKAY     = 0,
      MR_CANCEL   = 1 ;

  private int modalResult ;

  private boolean firstStart = true ;

  public TInternalDialog(String dialogTitle)
  {
    super(GUIGlobals.APPLICATION_MAINFRAME, dialogTitle, true) ;
    modalResult = MR_NOTHING ;
  }

  public TInternalDialog(JDialog parent, String dialogTitle)
  {
    super(parent, dialogTitle, true) ;
    modalResult = MR_NOTHING ;
  }


  /** set the status info and close the dialog (make it invisble) */
  protected void setModalResult(int mdResult)
  {
    modalResult = mdResult ;
    this.setVisible(false);
  }

  /** returns the dialog's "modal result status info" */
  public int getModalResult()
  {
    return modalResult ;
  }

  /** returns false, if the dialog showModal() method was called a 2nd time */
  public boolean isFirstStart()
  {
    return firstStart ;
  }

  /** resets some necessary data flags - used by showModal, reuse of the dialog
   *  called from showModel just before the dialog became visible
   * */
  protected void init()
  {
    modalResult = MR_NOTHING ;

    this.setLocationRelativeTo(GUIGlobals.APPLICATION_MAINFRAME);

    this.pack();
  }

  /** after the dialog became invisble */
  protected void close()
  {
  }

  /** shows the dialog as "modal" and returns a status info */
  public int showModal()
  {
    init() ;

    this.setVisible(true);

    close() ;

    firstStart = false ;

    return modalResult ;
  }


}
