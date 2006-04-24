/*
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

// created by : r.nagel 25.08.2005
//
// function : simple "theme"
//
// todo     :
//
// modified :


package net.sf.langproper.gui.theme ;

import java.awt.Font;
import javax.swing.plaf.metal.DefaultMetalTheme;
import java.awt.Color;
import net.sf.langproper.gui.*;

public class DefaultTheme extends DefaultMetalTheme
//public class DefaultTheme extends javax.swing.plaf.metal.OceanTheme // since 1.5
{
  protected static String preferredFont = "Lucida" ;

  private static Font tableHeaderFont = new Font(preferredFont, Font.PLAIN, 12)  ;
  private static Font dialogStandardFont = new Font(preferredFont, Font.PLAIN, 12) ;

  public static Color
      SearchFieldColor = new Color( 0xf5f6a5 ),
      ActiveTextField = SearchFieldColor,
      InactiveTextField = Color.WHITE
      ;


  public String getName()
  {
    return "Default" ;
  }

  /**
   * This override is provided such that Theme objects can
   * be directly passed to JComboBox, instead of Strings. (This would
   * not be necessary if getName had been named toString instead).
   */
  public final String toString()
  {
    return getName() ;
  }

  public Font getTableHeaderFont()
  {
    return tableHeaderFont ;
  }

  public Font getDialogStandardFont()
  {
    return dialogStandardFont ;
  }
}
