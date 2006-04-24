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
// function : some settings of the project
//
// todo     :
//
// modified :

package net.sf.langproper.engine.project ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import net.sf.langproper.*;

public class TProjectSettings
{
  // some other settings
  private boolean replaceWhitespace = false ;
  private String replaceString = "_" ;

  private boolean saveEmptyKeys = true ;
  
  /** 
   * <code>false</code> if saved entries should be written in using \\u escape sequence
   * <code>true</code> if they should be saved using WYSIWYG mode
  */
  private boolean saveWYSIWYGmode = false;

  public boolean getReplaceWhitespace()
  {
    return replaceWhitespace;
  }

  public void setReplaceWhitespace(boolean pReplaceWhitespace)
  {
    this.replaceWhitespace = pReplaceWhitespace;
  }

  public String getReplaceString()
  {
    return replaceString;
  }

  public void setReplaceString(String pReplaceString)
  {
    this.replaceString = pReplaceString;
  }

  public boolean getSaveEmptyKeys()
  {
    return saveEmptyKeys;
  }

  public void setSaveEmptyKeys(boolean pSaveEmptyKeys)
  {
    this.saveEmptyKeys = pSaveEmptyKeys;
  }

  
  public boolean getSaveWYSIWYGmode() {
    return saveWYSIWYGmode;
  }
  
  public void setSaveWYSIWYGmode(boolean saveWYSIWYGmode) {
    this.saveWYSIWYGmode = saveWYSIWYGmode;
  }

  public void readDefaults()
  {
    replaceWhitespace = TGlobal.config.replaceSpaceInInput() ;
    replaceString = TGlobal.config.getReplaceString() ;

    saveEmptyKeys = TGlobal.config.saveEmptyKeys() ;
    
    saveWYSIWYGmode = TGlobal.config.saveWYSIWYGmode();
  }

  // --------------------------------------------------------------------------
  // load from / save to xml project file support -----------------------------
  // --------------------------------------------------------------------------

  public void exportToXML( Element node, Document doc )
  {
    Element props = doc.createElement("replacespace") ;
    props.setAttribute("enable", Boolean.toString( getReplaceWhitespace() )) ;
    String str = getReplaceString() ;

     // don't save empty strings
    if (str != null)
      if (str.length() > 0)
      {
        props.setAttribute( "string", str ) ;
      }
    node.appendChild( props ) ;

    props = doc.createElement("emptykeys") ;
    props.setAttribute("enable", Boolean.toString( getSaveEmptyKeys() )) ;
    node.appendChild( props ) ;

    props = doc.createElement("saveWYSIWYGmode") ;
    props.setAttribute("enable", Boolean.toString( getSaveWYSIWYGmode() )) ;
    node.appendChild( props ) ;
  }

  public void initFromXML( Element node )
  {
    NodeList nList = node.getElementsByTagName("replacespace") ;
    Element elem = null ;

    // a settings entry was found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        Boolean bool = new Boolean( elem.getAttribute("enable") ) ;
        this.setReplaceWhitespace( bool.booleanValue() ) ;
        this.setReplaceString( elem.getAttribute("string"));
      }
    }

    nList = node.getElementsByTagName("emptykeys") ;
    // a settings entry was found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        Boolean bool = new Boolean( elem.getAttribute("enable") ) ;
        this.setSaveEmptyKeys( bool.booleanValue() ) ;
      }
    }

    nList = node.getElementsByTagName("saveWYSIWYGmode") ;
    // a settings entry was found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        Boolean bool = new Boolean( elem.getAttribute("enable") ) ;
        this.setSaveWYSIWYGmode( bool.booleanValue() ) ;
      }
    }
  }

}
