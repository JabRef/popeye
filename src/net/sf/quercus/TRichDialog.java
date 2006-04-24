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

// created by : r.nagel 4.04.2006
//
// function : a "rich" dialog
//
//
// todo     :
//
// modified :
package net.sf.quercus ;

import java.awt.* ;
import javax.swing.* ;

import net.sf.quercus.helpsystem.TInfoText;
import javax.swing.border.*;

public class TRichDialog
    extends TInternalDialog
{
    private TitlePanel descriptionPanel ;
    private JPanel contentPanel = new JPanel() ;

    private JPanel buttonPanel ;
    private JComponent buttonBox ;

    public TRichDialog(String id)
    {
      this( id, false ) ;
    }

    public TRichDialog(String id, boolean centerButtons)
    {
      super(id) ;

      this.setTitle( TInfoText.runtime.getTitle(id, "frame") ) ;

      Container main = this.getContentPane() ;

      main.setLayout( new BorderLayout() );

      descriptionPanel = new TitlePanel(id) ;

      initButtons(centerButtons) ;

      main.add( descriptionPanel, BorderLayout.NORTH) ;
      main.add( contentPanel, BorderLayout.CENTER) ;
      main.add( buttonPanel, BorderLayout.SOUTH) ;

      contentPanel.setLayout( new BorderLayout() );

      pack() ;
    }


    public JPanel getContentPanel()
    {
      return contentPanel ;
    }

    public JPanel getButtonPanel()
    {
      return buttonPanel ;
    }

    // ---------------------------------------------------------------------
    // Button handling

    private void initButtons(boolean center)
    {
      buttonPanel = new JPanel( new BorderLayout() ) ;
      buttonPanel.add( new JSeparator(), BorderLayout.NORTH ) ;

      if (!center)
      {
        buttonBox = new Box( BoxLayout.X_AXIS ) ;
        buttonPanel.add( buttonBox, java.awt.BorderLayout.EAST ) ;
      }
      else
      {
        buttonBox = new JPanel() ;
        buttonPanel.add(buttonBox, BorderLayout.CENTER) ;
      }
      buttonBox.setBorder( new EmptyBorder( new Insets( 5, 10, 5, 10 ) ) ) ;
    }

    public void addButton( JButton button )
    {
      buttonBox.add( button ) ;
      buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    }
}
