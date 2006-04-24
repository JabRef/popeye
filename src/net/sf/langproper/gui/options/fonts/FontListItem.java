/**
Popeye - Java (Language) Properties File Editor

Copyright (C) 2005,2006 Raik Nagel <kiar@users.sourceforge.net>
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
// function : single font entry from FontList
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options.fonts ;

import java.awt.*;

public class FontListItem
{

  // number of provided language checks / properties
  public static final int
      LANGS = 4 ;


  // each language/property
  public static final int
      RUS = 0,
      CHI = 1,
      JAP = 2,
      ARA = 3 ;

  private String name ;
  private Font font ;

  private boolean langs[] = new boolean[LANGS] ;

  public FontListItem(String fontName)
  {
    name = fontName ;
    font = new Font(fontName, Font.PLAIN, 12) ;

    // check if some characters of the languages are available for displaying
    langs[0] = font.canDisplay('\u041a') ;
    langs[1] = font.canDisplay('\u4e00') ;
    langs[2] = font.canDisplay('\u30d5') ;
    langs[3] = font.canDisplay('\u0625') ;
  }

  public String toString()
  {
    return name ;
  }

  public String getName()
  {
    return name;
  }

  public Font getFont()
  {
    return font;
  }

  public boolean isProperty( int index )
  {
    return langs[ index ] ;
  }

  // ---------------------------------------------------------------------

  /** returns the name of the property */
  public static String getPropertyName( int index )
  {
    switch(index)
    {
      case 0 : return "russian" ;
      case 1 : return "chinese" ;
      case 2 : return "japanese" ;
      case 3 : return "arabic" ;
    }
    return "?" ;
  }


}
