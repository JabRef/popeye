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
// function : the New Project Wizard
//
// todo     :
//
// modified :

package net.sf.langproper.wizard.project ;

import com.nexes.wizard.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.wizard.* ;
import net.sf.quercus.* ;

public class NewProjectWizard
{
  private int result ;
  private TProject project ;
  private Wizard wizard ;

  public void show(String projectType)
  {
    result = TInternalDialog.MR_NOTHING ;
    project = null ;

    wizard = new Wizard(true) ;
    wizard.getDialog().setTitle("New Project");

    // shared panel data, normaly a TProject instance
    WizardData wzData = new WizardData(wizard) ;

    WizardPanelDescriptor descriptor1 = new Step1Descriptor(wzData, projectType);
    wizard.registerWizardPanel(Step1Descriptor.IDENTIFIER, descriptor1);

    WizardPanelDescriptor descriptor2 = new Step2Descriptor(wzData);
    wizard.registerWizardPanel(Step2Descriptor.IDENTIFIER, descriptor2);

    wizard.setCurrentPanel(Step1Descriptor.IDENTIFIER);

    int res = wizard.showModalDialog();

    if (res == Wizard.FINISH_RETURN_CODE)
    {
      result = TInternalDialog.MR_OKAY ;
      project = (TProject) wzData.getData() ;
    }
  }

  public int getResultCode()
  {
    return result ;
  }

  public TProject getProject()
  {
    return project ;
  }

}
