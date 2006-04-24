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
// function : panel with font properties
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options.fonts ;

import javax.swing.* ;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FontLanguagePropsPanel extends JPanel
{
  private JCheckBox boxes[] ;

  public FontLanguagePropsPanel()
  {
    super() ;
    setBorder( new TitledBorder( "supported languages" ) ) ;
    setLayout( new GridLayout(2, 3) ) ;

    boxes = new JCheckBox[FontListItem.LANGS] ;
    for( int t = 0 ; t < FontListItem.LANGS ; t++)
    {
      boxes[t] = new JCheckBox( FontListItem.getPropertyName(t)) ;
      boxes[t].setEnabled(false);

      add( boxes[t] ) ;
    }
  }

  public void update( FontListItem font )
  {
    for ( int t = 0 ; t < FontListItem.LANGS ; t++)
    {
      boxes[t].setSelected( font.isProperty(t));
    }
  }
}
