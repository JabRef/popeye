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

package net.sf.langproper.engine ;

import org.w3c.dom.Element;

public class XmlUtils
{

  public static int readIntegerAttribute(Element elem, String name, int defaultValue)
  {
    int back = defaultValue ;

    String str = elem.getAttribute(name) ;
    if (str != null)
    {
      if (str.length() > 0)
      {
        back = Integer.parseInt(str) ;
      }
    }

    return back ;
  }

  public static boolean readBooleanAttribute(Element elem, String name,
                                             boolean defaultValue)
  {
    boolean back = defaultValue ;

    String str = elem.getAttribute(name) ;
    if (str != null)
    {
      if (str.length() > 0)
      {
        int hash = str.hashCode() ;
        if ( (hash == "yes".hashCode()) || (hash == "true".hashCode()))
        {
          back = true ;
        }
        else
        {
          back = false ;
        }
      }
    }

    return back ;
  }

  public static String readStringAttribute(Element elem, String name, String defaultValue)
  {
    String back = defaultValue ;

    String str = elem.getAttribute(name) ;
    if (str != null)
      if (str.length() > 0)
      {
        back = str ;
      }

    return back ;
  }

}
