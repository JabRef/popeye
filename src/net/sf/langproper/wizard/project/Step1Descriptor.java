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
import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.wizard.* ;

public class Step1Descriptor extends WizardPanelDescriptor implements
    StatusChangedListener
{

  public static final String IDENTIFIER = "INTRODUCTION_PANEL" ;

  private Step1Panel panel ;
  private WizardData data ;

  public Step1Descriptor( WizardData wd, String projectType )
  {
    data = wd ;

    panel = new Step1Panel( wd ) ;
    panel.addStatusChangedListener( this ) ;
    panel.setProjectType( projectType );

    setPanelDescriptorIdentifier( IDENTIFIER ) ;
    setPanelComponent( panel ) ;
  }

  public Object getNextPanelDescriptor()
  {
    return Step2Descriptor.IDENTIFIER ;
  }

  public Object getBackPanelDescriptor()
  {
    return null ;
  }

  /**
   * Override this method to perform functionality just before the panel is to be
   * hidden.
   */
  public void aboutToHidePanel()
  {
    if (data != null)
    {
      // generate an empty project or get an "old" but empty instance
      TProject dummy = TGlobal.projects.getEmptyProject( panel.getProjectType(), true ) ;

      // fill with wizard data
      dummy.getSettings().readDefaults();
      dummy.setProjectName( panel.getProjectName() ) ;
      dummy.setWorkingDirectory( panel.getWorkDir()) ;
      data.setProperty( "dir", panel.getDirChooser() );

      data.setData( dummy );
    }
  }

  // --------------------------------------------------------------------------
  // StatusChangedListener ----------------------------------------------------
  // --------------------------------------------------------------------------

  public void performStatusCheck( StatusChangedEvent event )
  {
    getWizard().setNextFinishButtonEnabled( event.isValid() ) ;
  }

}
