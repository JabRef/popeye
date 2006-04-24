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

// created by : r.nagel 31.08.2005
//
// function : get info- and helptext from external files
//            this is a first experimental version
//
// todo     : - make it usable,
//
// modified :

package net.sf.quercus.helpsystem ;

import java.io.* ;
import javax.xml.parsers.* ;

import org.w3c.dom.* ;
import org.xml.sax.* ;
import net.sf.langproper.* ;

public class TInfoText
{
  private static Document config ; // XML Daten
  private DocumentBuilderFactory factory ;
  private DocumentBuilder builder ;

  public static TInfoText runtime = new TInfoText() ;

  private TInfoText()
  {
    readConfig() ;
  }

  public String getText( String parent, String key )
  {
    String back = "infotext" ;

    // Tag suchen
    NodeList mainNodes = config.getElementsByTagName( parent ) ;

    int tagsCount = mainNodes.getLength() ;
    if ( tagsCount > 0 )
    {
      Node mainEntry = mainNodes.item( 0 ) ;
      back = getSimpleElementText( ( Element ) mainEntry, key ) ;
    }
    return back ;
  }

  public String getTitle( String parent, String key )
  {
    String back = "infotext" ;

    // Tag suchen
    NodeList nodes = config.getElementsByTagName( parent ) ;

    // ersten gefundenen Knoten durchgehen
    if ( nodes != null )
    {
      if ( nodes.getLength() > 0 )
      {
        Element single = ( Element ) nodes.item( 0 ) ;
        back = this.readStringAttribute(single, "title", "Titel") ;
      }
    }
    return back ;
  }

  // --------------------------------------------------------------------------

  private void readConfig() // liest die Konfigurationsdatei
  {
    factory = DocumentBuilderFactory.newInstance() ;
    try
    {
      builder = factory.newDocumentBuilder() ;

      if ( TGlobal.DEVEL )
      {
        File f = new File( "src/resource/info/new.xml" ) ;
        config = builder.parse( f ) ;
      }
      else
      {
        InputStream stream = TGlobal.class.getResourceAsStream(
            "/resource/info/new.xml" ) ;
        config = builder.parse( stream ) ;
      }
    }

    catch ( SAXException sxe )
    {
      sxe.printStackTrace() ;
    }
    catch ( ParserConfigurationException pce )
    {
      pce.printStackTrace() ;
    }
    catch ( IOException ioe )
    {
      ioe.printStackTrace() ;
    }
  }

  // ---------------------------------------------------------------------------

  public static Element getFirstElement( Element element, String name )
  {
    NodeList nl = element.getElementsByTagName( name ) ;
    if ( nl.getLength() < 1 )
    {
      throw new RuntimeException(
          "Element: " + element + " does not contain: " + name ) ;
    }
    return ( Element ) nl.item( 0 ) ;
  }

  public static String getSimpleElementText( Element node, String name )
  {
    Element namedElement = getFirstElement( node, name ) ;
    return getSimpleElementText( namedElement ) ;
  }

  public static String getSimpleElementText( Element node )
  {
    StringBuffer sb = new StringBuffer() ;
    NodeList children = node.getChildNodes() ;
    for ( int i = 0 ; i < children.getLength() ; i++ )
    {
      Node child = children.item( i ) ;
      if ( child instanceof Text )
      {
        sb.append( child.getNodeValue().trim() ) ;
      }
    }
    return sb.toString() ;
  }

// ---------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  public static String readStringAttribute( Element node, String attrName,
                                            String defaultValue )
  {
    if ( node != null )
    {
      String data = node.getAttribute( attrName ) ;
      if ( data != null )
      {
        if ( data.length() > 0 )
        {
          return data ;
        }
      }
    }
    return defaultValue ;
  }

  // --------------------------------------------------------------------------
}
