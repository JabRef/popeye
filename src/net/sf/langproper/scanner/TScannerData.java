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

// created by : r.nagel 12.09.2005
//
// function : a scanner project
//
// todo     :
//
// modified :

package net.sf.langproper.scanner ;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import net.sf.langproper.engine.project.*;

public class TScannerData
{
  private String sourcePath ;

  private String parserType ;

  private TKeyRefList keyList ;

  private Vector fileList = new Vector() ;

  private boolean enable = false ;

  public TScannerData()
  {
    keyList = new TKeyRefList() ;
  }

  public TScannerData(String sPath, String pType, TProjectSettings settings)
  {
    sourcePath = sPath ;
    parserType = pType ;
    keyList = new TKeyRefList( settings ) ;
  }

  public String getSourcePath()
  {
    String back = "./" ;
    if (sourcePath != null)
      if (sourcePath.length() > 0)
        return sourcePath ;

    return back ;
  }

  public void setSourcePath(String pSourcePath)
  {
    this.sourcePath = pSourcePath;
  }

  public String getParserType()
  {
    return parserType;
  }

  public void setParserType(String pParserType)
  {
    this.parserType = pParserType;
  }

  public TKeyRefList getKeyList()
  {
    return keyList;
  }

  public Vector getFileList()
  {
    return fileList;
  }

  // -------------------------------------------------------------------------
  public boolean getEnable()
  {
    return enable;
  }
  public void setEnable(boolean pEnable)
  {
    this.enable = pEnable;
  }


  // --------------------------------------------------------------------------
  // load from / save to xml project file support -----------------------------
  // --------------------------------------------------------------------------

  public void exportToXML( Element node, Document doc )
  {
    Element props = doc.createElement("files") ;
    props.setAttribute("enable", Boolean.toString( getEnable()) );
    props.setAttribute("rootdir", this.getSourcePath() ) ;
    node.appendChild( props ) ;
  }

  public void initFromXML( Element node )
  {
    NodeList nList = node.getElementsByTagName("files") ;
    Element elem = null ;

    // a settings entry was found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        Boolean bool = new Boolean( node.getAttribute("enable") ) ;
        setEnable( bool.booleanValue() ) ;
        this.setSourcePath( elem.getAttribute("rootdir"));
      }
    }
  }

}
