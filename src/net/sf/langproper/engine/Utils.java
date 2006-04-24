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

import java.io.* ;

public class Utils
{
  // placeholder for the default host encoding, used by getDefaultHostEncoding()
  private static String encoding = "" ;

  /** the default encoding used by the local host */
  public static String getDefaultHostEncoding()
  {
    if ( (encoding == null) || (encoding.length() < 1))
    {
      // An application can determine the default encoding by calling the
      // Charset.defaultCharset method, available since J2SE 5. In older versions
      // of the Java platform, you can use the expression :
      encoding = ( new OutputStreamWriter(
                     new ByteArrayOutputStream() ) ).getEncoding() ;
    }

    return encoding ;

  }

  // replace all unicode \\uxxxx sequences
  public static String get_WYSIWYG_String( String data )
  {
    if ( data == null )
    {
      return "" ;
    }

    StringBuffer buffer = new StringBuffer( data ) ;
    int len = buffer.length() ;
    int index = 0 ;
    while ( index < len )
    {
      char zei = buffer.charAt( index ) ;
      if ( zei == '\\' ) // start of special sequence
      {
        if ( index <= len - 6 ) // such sequence has 6 characters
        {
          char next = buffer.charAt( index + 1 ) ;
          if ( ( next == 'u' ) || ( next == 'U' ) ) // unicode sequence
          {
            // get the decoded character
            String newChar = convertHexToUni( buffer.substring( index + 2,
                index + 6 ) ) ;

            // replace the sequence
            if ( newChar != null )
            {
              buffer.replace( index, index + 6, newChar ) ;

              len = buffer.length() ; // string has a new length
            }
          }
        }
      }
      index++ ; // next character
    }
    return buffer.toString() ;
  }


  public static String get_Unicode_I18n_String(String data)
  {
    if (data == null)
    {
      return null ;
    }

    StringBuffer buffer = new StringBuffer() ;

    int len = data.length() ;
    int index = 0 ;
    int escapeCounter = 0 ;

    while ( index < len )
    {
      char c1 = data.charAt(index) ;
      if (c1 == '\\')  //escape sequence ?
      {
        escapeCounter++ ;
      }
      else
      {
        if ((c1 == 'u') && (escapeCounter > 0))  // "\\..\\u" found
        {
          if ( (index +4) < len) // hex code available
          {
            buffer.append( data.substring( index-1, index+4)) ;
            index += 3 ;  // skip hex code
          }
        }
        else  // no escape sequence
        {
          if (c1 > 127)  // mask all not ascii characters
          {
            buffer.append("\\u") ;
            buffer.append( convertUniToHex( c1 ) ) ;
          }
          else
          {
            // don't forget the \\ signs
            for (int etc = 0 ; etc < escapeCounter ; etc++)
            {
              buffer.append( '\\' ) ;
            }
            buffer.append( c1 ) ;
          }
        }
        escapeCounter = 0 ;
      }

      index++ ;
    }
    return buffer.toString() ;
  }

  // Convert a hexadecimal string ("7fd1") to a single unicode letter.
  public static String convertHexToUni( String theNumber )
  {
    char[] the_unicode_char = new char[1] ;
    try
    {
      the_unicode_char[0] = ( char ) Integer.parseInt( theNumber, 16 ) ;
    }
    catch ( NumberFormatException nfe )
    {
      return null ;
    }
    return new String( the_unicode_char ) ;
  }

  // returns a 4 digit hex string
  public static String convertUniToHex(char theUniCode)
  {
    StringBuffer back = new StringBuffer("000") ;
    back.append( Integer.toHexString( theUniCode )) ;

    int len = back.length() ;
    return back.substring(len-4) ;
  }


  /** convert the String into a valid internal representation */
  public static String normalizeIT(String data,
                                   boolean replaceSpaces, String rString)
  {
    // convert all whitespaces?
   if ( replaceSpaces )
     data = data.replaceAll(" ", rString ) ;


   return Utils.get_Unicode_I18n_String( data ) ;
  }

}
