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


// created by : r.nagel 02.09.2005
//
// function : a project instance
//
// todo     :
//
// modified :


package net.sf.langproper.engine.project ;

import java.io.* ;

import org.w3c.dom.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.scanner.* ;

public abstract class TProject
{
  private static int projectCounter = 0 ;

  /** short name of the project */
  private String projectName = null ;

  /** the projects working place */
  private String workingDirectory = "./" ;

  /** reference of the xml project file */
  private File projectFile = null ;

  /** flag for saving mode */
  private boolean unSaved = false ;

  /** scannerdata and key references */
  private TScannerData scannerData = new TScannerData() ;

  /** other project settings */
  private TProjectSettings settings = new TProjectSettings() ;

  /** indicates that some project properties or settings have been changed */
  private boolean propertiesChanged = false ;

  // --------------------------------------------------------------------------
  // abstract methods ---------------------------------------------------------
  // --------------------------------------------------------------------------

  /** returns the type of project : e.g. java bundle */
  public abstract String getTypeName() ;

  public abstract TProjectData getProjectData() ;

  /** alternative contructor methode, called by TProjectFactory */
  public void init()
  {
    scannerData.getKeyList().setProjectSettings( settings );
    TProjectData data = this.getProjectData() ;
    if (data != null)
    {
      data.setProjectSettings( settings );
    }
    else System.out.println( "DATA NULL" ) ;
  }

  // --------------------------------------------------------------------------
  // utility methods ----------------------------------------------------------
  // --------------------------------------------------------------------------

  public static final String generateNewName()
  {
    return ( "empty" + (projectCounter++)) ;
  }
  // --------------------------------------------------------------------------

  public TProjectSettings getSettings()
  {
    return settings ;
  }

  // --------------------------------------------------------------------------
  public String getProjectName()
  {
    if (projectName == null)
      projectName = generateNewName() ;

    return projectName;
  }

  /** set the project name,
   *  if the new name <pProjectName> null or empty (length = 0) the project
   *  will not be set
   */
  public void setProjectName(String pProjectName)
  {
    if (pProjectName != null)
    {
      if (pProjectName.length() > 0)
      {
        propertiesChanged = true ;
        this.projectName = pProjectName ;
      }
    }
  }

  // --------------------------------------------------------------------------
  public String getWorkingDirectory()
  {
    return workingDirectory;
  }

  public void setWorkingDirectory(String pWorkingDirectory)
  {
    if (pWorkingDirectory != null)
    {
      propertiesChanged = true ;
      this.workingDirectory = pWorkingDirectory ;
    }
  }

  // --------------------------------------------------------------------------

  /** reference of xml project file */
  public File getProjectFile()
  {
    return projectFile;
  }

  /** reference of xml project file */
  public void setProjectFile(File pProjectFile)
  {
    this.projectFile = pProjectFile;
  }

  // --------------------------------------------------------------------------
  public boolean getUnSaved()
  {
    return unSaved;
  }

  public void setUnSaved(boolean pUnSaved)
  {
    this.unSaved = pUnSaved;
  }

  // --------------------------------------------------------------------------
  public TScannerData getScannerData()
  {
    return this.scannerData ;
  }

  // --------------------------------------------------------------------------
  public boolean getPropertiesChanged()
  {
    return propertiesChanged;
  }

  public void setPropertiesChanged(boolean pPropertiesChanged)
  {
    this.propertiesChanged = pPropertiesChanged;
  }

  public void setPropertiesHasChanged()
  {
    this.propertiesChanged = true ;
  }

  // --------------------------------------------------------------------------
  // key references -----------------------------------------------------------
  // --------------------------------------------------------------------------

  /** combine the scanner and project data */
  public void updateKeyReferences()
  {
    int found = 0 ;
    int notFound = 0 ;
    TProjectData data = getProjectData() ;
    if ( data != null )
    {
       TranslationList list = data.getTranslations() ;
       for(int t = 0, len = list.size() ; t < len ; t++)
       {
         TMultiLanguageEntry entry = list.getFast(t) ;
         TKeyRef keyRef = scannerData.getKeyList().getKeyRef( entry.getKey() ) ;
         if (keyRef != null)
           found ++ ;
         else
           notFound++ ;

         entry.setKeyRef( keyRef );

//         entry.setKeyRef( scannerData.getKeyList().getKeyRef( entry.getKey() ) );
       }
    }

    System.out.println( "zugeordnet " +found +"<" +notFound +">") ;
  }

  /** checks, if there is a key reference in scanner data for this entry */
  public void updateSingleKeyReference(TMultiLanguageEntry entry)
  {
    if (entry != null)
    {
      entry.setKeyRef( scannerData.getKeyList().getKeyRef( entry.getKey() ) );
    }
  }


  // --------------------------------------------------------------------------
  // load from / save to xml project file support -----------------------------
  // --------------------------------------------------------------------------

  public void exportToXML( Element node, Document doc )
  {
    Element props = doc.createElement("settings") ;
    props.setAttribute("name", getProjectName()) ;
    // other settings
    settings.exportToXML(props, doc);
    node.appendChild( props ) ;

    // generate the file-node
    Element files = doc.createElement("files") ;

    // insert the working directory into the files section
    String str = getWorkingDirectory() ;
    if (str != null)
      if (str.length() > 0)
      {
        files.setAttribute( "workdirectory", str ) ;
      }

    // export all file settings
    getProjectData().getAvailableLangs().exportToXML(files, doc);

    node.appendChild(files) ;

    // scanner properties
    if (scannerData != null)
    {
      Element scanner = doc.createElement( "scanner" ) ;
      scannerData.exportToXML( scanner, doc ) ;
      node.appendChild( scanner ) ;
    }
  }

  public void initFromXML( Element node, String projectDir )
  {
    NodeList nList = node.getElementsByTagName("settings") ;
    Element elem = null ;

    // a settings entry was found
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        setProjectName( elem.getAttribute("name"));

        // other settings
        settings.initFromXML(elem);
      }
    }

    nList = node.getElementsByTagName("files") ;

    // project files
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;
        String wd = elem.getAttribute("workdirectory") ;
        setWorkingDirectory( wd );

        // import all file settings
        getProjectData().getAvailableLangs().initFromXML(elem, wd, projectDir);
      }
    }

    // scanner
    nList = node.getElementsByTagName("scanner") ;

    // read only the first element
    if (nList != null)
    {
      if (nList.getLength() > 0)
      {
        elem = (Element) nList.item(0) ;

        // import the settings
        scannerData.initFromXML(elem);
      }
    }
  }

  // --------------------------------------------------------------------------
  // some static project properties
  // --------------------------------------------------------------------------

  /** a project cannot contain files with different "bundle" names
   *  e.g. a project with "MessageBundle_xx_yy.properties" files cannot
   *  contain files like "other_xx_yy.properties"
   */
  public static boolean allowDifferentBundleName()
  {
    return false ;
  }

  /** There is a direct relationship between the filename and the
   *  language/country/variant setting.
   *  E.g. a java bundle filename like xyz_de.properties is a filename for
   *  the german language.
   *  Used by the LanguageProperties Settings Dialog
   */
  public boolean filenameContainsInfo()
  {
    return false ;
  }

}
