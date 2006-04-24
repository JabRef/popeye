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
// function : start the codeinsight wizard
//
// todo     :
//
// modified :
package net.sf.langproper.gui.actions ;

import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.scanner.* ;
import net.sf.langproper.gui.task.* ;
import net.sf.langproper.wizard.code.* ;
import net.sf.quercus.* ;
import net.sf.quercus.report.* ;

public class TCodeWizardAction extends TAbstractGUIAction
{
  public TCodeWizardAction()
  {
    super( "Codeinsight",
           "start the code wizard",
           new ImageIcon( GUIGlobals.develIconName ) ) ;
//    putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( "ctrl O" ) ) ;
  }

  public void actionPerformed( ActionEvent e )
  {

    ErrorReportDialog eDiag = new ErrorReportDialog() ;
    eDiag.report() ;

    CodeWizard wzFrame = new CodeWizard() ;

    wzFrame.show();

    if (wzFrame.getResultCode() == TInternalDialog.MR_OKAY)
    {
       TaskManager.runtime.addPriorityJob( TaskManager.runtime.scanTask,
                                            wzFrame.getProjectData() ) ;
//        TaskManager.runtime.addJob( TaskManager.runtime.updateRefsTask,
//                                             wzFrame.getProjectData() ) ;

//         CodeInsightDialog diag = new CodeInsightDialog();
//         diag.showModal() ;
//         diag.dispose();
    }

  }

}
