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

package net.sf.langproper.wizard.project ;

import com.nexes.wizard.* ;
import net.sf.langproper.wizard.* ;

public class Step2Descriptor extends WizardPanelDescriptor
{

  public static final String IDENTIFIER = "FILE_CHOOSE_PANEL" ;

  private Step2Panel panel2 ;
  private WizardData data ;

  public Step2Descriptor(WizardData wd)
  {
    data = wd ;
    panel2 = new Step2Panel(wd) ;

    setPanelDescriptorIdentifier( IDENTIFIER ) ;
    setPanelComponent( panel2 ) ;
  }

  public boolean canFinished()
  {
    return true ;
  }

  public Object getNextPanelDescriptor()
  {
//    return Step3Descriptor.IDENTIFIER ;
     return FINISH ;
  }

  public Object getBackPanelDescriptor()
  {
    return Step1Descriptor.IDENTIFIER ;
  }

  /**
   * Override this method to provide functionality that will be performed just before
   * the panel is to be displayed.
   */
  public void aboutToDisplayPanel()
  {
    // come from Step1Panel
    if (getWizard().getLastButtonClicked() == Wizard.LAST_BUTTON_NEXT)
    {
//      panel2.rescan();
//      data.getData() ;
        panel2.updateProjectData();
    }
  }

}
