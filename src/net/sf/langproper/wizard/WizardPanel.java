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
// function : a wizard panel with some usefull features
//
// todo     :
//
// modified :

package net.sf.langproper.wizard ;

import javax.swing.* ;
import java.util.*;

public class WizardPanel extends JPanel
{
  private boolean valid = false ;
  private Vector listenerList = new Vector() ;

  /** the panels can share some data over this object */
  private WizardData wizardData = null ;

  public WizardPanel( WizardData shared)
  {
    wizardData = shared ;
  }

  /** set the valid flag, without any listener interaction */
  protected void setValidStatus( boolean newValue)
  {
    valid = newValue ;
  }

  /** get the status of valid flag */
  public boolean isValid()
  {
    return valid ;
  }

  /** set the valid status. If it has changed, fire an event */
  public void setValid( boolean newValue)
  {
    if (valid != newValue)
    {
      valid = newValue ;
      fireStatusChanged() ;
    }
  }

  // --------------------------------------------------------------------------
  /** add a StatusChangedListener */
  public void addStatusChangedListener(StatusChangedListener listener)
  {
    listenerList.add( listener ) ;
  }

  /** removes a StatusChangedListener */
  public void removeStatusChangedListener(StatusChangedListener listener)
  {
    listenerList.remove( listener ) ;
  }

  /** inform all registered StatusChangedListener about new data */
  public final void fireStatusChanged()
  {
    System.out.println( "checked?" ) ;

    StatusChangedEvent event = new StatusChangedEvent(this, valid) ;

    for (int t = 0, len = listenerList.size() ; t < len ; t++)
    {
      StatusChangedListener dummy = (StatusChangedListener) listenerList.get(t) ;
      dummy.performStatusCheck( event );
    }
  }

  /** get the shared wizard data */
  public WizardData getWizardData()
  {
    return wizardData ;
  }

  /** get the shared wizard data from a WizardData instance*/
  public Object getWizardSharedData()
  {
    return wizardData.getData() ;
  }


  /** set the shared wizard data into a WizardData instance */
  public void setWizardSharedData(WizardData pWizardData)
  {
    this.wizardData = pWizardData ;
  }


}
