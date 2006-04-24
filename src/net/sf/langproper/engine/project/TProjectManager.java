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
// function : handling of several projects
//
// todo     : test it
//
// modified :

package net.sf.langproper.engine.project ;

import java.io.* ;
import java.util.* ;
import javax.xml.parsers.* ;
import javax.xml.transform.* ;
import javax.xml.transform.dom.* ;
import javax.xml.transform.stream.* ;

import javax.swing.* ;

import org.w3c.dom.* ;
import org.xml.sax.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.java.* ;

public class TProjectManager
{
  /** all available empty projects (cache!) */
  private Vector emptyProjects = new Vector(5) ;

  /** all open projects */
  private Hashtable openProjects = new Hashtable(5) ;

  private TProjectData currentData ;
  private TProject currentProject ;

  private TProjectFactory factory ;

  public TProjectManager()
  {
    factory = new TProjectFactory() ;

    addProject(  new JavaBundleProject() ) ;
  }

  // --------------------------------------------------------------------------
  // private section ----------------------------------------------------------
  // --------------------------------------------------------------------------

  /** search for a project of type <typeName> in emptyProject list */
  private TProject findEmptyProject( String typeName )
  {
    TProject back = null ;

    if ( typeName != null )  // valid String
    {
      int t = 0 ;
      int len = emptyProjects.size() ;
      int hash = typeName.hashCode() ; // cache the hashcode of search string

      while ( t < len )  // check all registered items
      {
        // get the name of item t
        TProject dummy = ( TProject ) emptyProjects.get( t ) ;

        // found ?
        if ( hash == dummy.getTypeName().hashCode() )
        {
          back = dummy ; // remember the object instance
          t = len ; // cancel loop
        }
        else
        {
          t++ ;
        }
      }
    }
    return back ;
  }

  // --------------------------------------------------------------------------
  /** returns an empty project of type <type> */
  public TProject getEmptyProject(String type, boolean cacheIt)
  {

    TProject project = findEmptyProject(type) ;
    if (project == null)
    {
      project = factory.getProjectInstance( type ) ;

      if (cacheIt)  // insert into the list of empty project instances
      {
        emptyProjects.add(project) ;
      }
    }

    return project ;
  }

  /** insert a project into the list of opened projects */
  public void addProject( TProject project )
  {
    if (project != null)
    {
      // remove from empty list, if such project exists
      emptyProjects.remove( project ) ;

      TProject dummy = (TProject) openProjects.get( project.getProjectName() ) ;
      if (dummy == null)
      {
        openProjects.put( project.getProjectName(), project) ;
        if (currentProject == null)
        {
          setCurrentProject( project ) ;
        }
      }
    }
  }

  /** removes a project from the list of opened files */
  private void removeProject( TProject project)
  {
    if ( project != null)
    {
      openProjects.remove( project.getProjectName() ) ;
    }
  }

  /** set the current project */
  // testen ob Projekt bereits in der internen Verwaltungsliste? eventuell einfuegen ?
  // -> d.h. erst Verwaltung von mehreren Projekten einbauen
  public void setCurrentProject( TProject project )
  {
    if (project != null)
    {
      removeProject( currentProject ) ;
      currentProject = project ;
      currentData = currentProject.getProjectData() ;
    }
  }

  // --------------------------------------------------------------------------
  // service wrapper function for the current (active) project ----------------
  // --------------------------------------------------------------------------

  /** get the data (TProjectData) of the active project */
  public TProjectData getCurrentData()
  {
    return currentData ;
  }

  /** get the TProject instance of the active project */
  public TProject getCurrentProject()
  {
    return currentProject ;
  }

  /** returns true, if any open project contains changed data
   * @todo alle Projekte ueberpruefen!
   */
  public boolean isProjectChanged()
  {
    return ( currentData.isDataChanged() | currentProject.getUnSaved()) ;
  }

  /** get the TLanguageList (TProjectFileList) instance of active project */
  public TLanguageList getLanguages()
  {
    return currentData.getAvailableLangs() ;
  }

  /** perform a data update */
  public void updateCurrentData()
  {
    currentData.fireUpdateData();
  }

  /** perform a structure changed into the TProjectData object of the
   *  current TProject instance */
  public void updateCurrentStructure( boolean value )
  {
    currentData.fireUpdateDataStructure( value );
  }

  // --------------------------------------------------------------------------

  /** returns a combobox model with all registered project types */
  public ComboBoxModel getProjectTypeList()
  {
    return factory ;
  }

  /** returns an Enumeration of all registered project types */
  public Enumeration getRegisteredProjectTypes()
  {
    return factory.getRegisteredProjectTypes() ;
  }


  // --------------------------------------------------------------------------
  // XML project files --------------------------------------------------------
  // --------------------------------------------------------------------------

  /** load a new project and make it active, it returns 0 if all data could
   *  be loaded */
  public int loadProject( File file)
  {
    int back = -1 ;

    TProject dummy = null ;
    try
    {
      dummy = this.readXmlFile( file ) ;
    }
    catch ( SAXException sxe )
    {
//      sxe.printStackTrace() ;
      back = 1 ;
    }
    catch ( ParserConfigurationException pce )
    {
//      pce.printStackTrace() ;
      back = 2 ;
    }
    catch ( IOException ioe )
    {
//      ioe.printStackTrace() ;
      back = 3 ;
    }
    catch (Exception e)
    {
      back = 4 ;
    }
    catch (Error er)
    {
      back = 5 ;
    }

    if (dummy != null)
    {
      dummy.setProjectFile(file);
      // all data are up to date
      dummy.setUnSaved(false);

      this.addProject( dummy ) ;
      this.setCurrentProject( dummy ) ;
      back = 0 ;
    }

    return back ;
  }

  /** save all files of the current project */
  public void saveProject( File file )
  {
     TProject project = this.getCurrentProject() ;

     // write the configuration file
     this.writeXmlFile( project, file);

     // write all language files
     project.getProjectData().save();

     // all data are up to date
     project.setUnSaved(false);
     project.setProjectFile( file );
  }

  /** This method generates and writes a DOM document to a file */
  public void writeXmlFile( TProject project, File file )
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;

      DocumentBuilder builder = factory.newDocumentBuilder() ;
      org.w3c.dom.Document document = builder.newDocument() ; // Create from whole cloth

      // main node
      Element root = ( Element ) document.createElement( "project" ) ;

      root.setAttribute( "type", project.getTypeName() ) ;

      // call the TProject export methode
      project.exportToXML( root, document ) ;

      document.appendChild( root ) ;

//      root.appendChild( document.createTextNode( "Some text" ) ) ;

      document.getDocumentElement().normalize() ;

      // Prepare the DOM document for writing
      Source source = new DOMSource( document ) ;

      // Prepare the output file
//      File file = new File( filename ) ;
      Result result = new StreamResult( file ) ;

      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer() ;
      xformer.setOutputProperty( OutputKeys.METHOD, "xml" ) ;
      xformer.setOutputProperty( OutputKeys.INDENT, "yes" ) ;

      // this could be buggy because the default xalan transformer can't handle
      // empty strings (like "") correctly
      xformer.transform( source, result ) ;
    }
    catch ( ParserConfigurationException pce )
    {
      // Parser with specified options can't be built
      pce.printStackTrace() ;
    }
    catch ( TransformerConfigurationException e )
    {
    }
    catch ( TransformerException e )
    {
    }
    catch ( NullPointerException npe )
    {
      // @see xformer.transform( source, result)
      // @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4981389
      // ..Xalan fails to use xmlns="" to unset the default namespace for a
      // nested element;..
      System.out.println( "could not write the project-file correctly" ) ;
      System.out.println( "Please try Java >= 1.5" ) ;
    }
  }

  /** load a project from xml file */
  public TProject readXmlFile( File file ) throws Exception, Error
  {
    org.w3c.dom.Document document = null ;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;

    TProject back = null ;

      DocumentBuilder builder = factory.newDocumentBuilder() ;

//      File f = new File( filename ) ;
      document = builder.parse( file ) ;

      NodeList projectNodes = document.getElementsByTagName( "project" ) ;

      if ( projectNodes != null )
      {
        if ( projectNodes.getLength() > 0 )
        {
          Element node = ( Element ) projectNodes.item( 0 ) ;

          back = getEmptyProject( node.getAttribute("type"), false) ;
          if (back != null)
          {
            // init the filenodes, the second parameter gives the project file path
            // it is neccessary to find the files (if the path is broken)
            back.initFromXML( node, file.getParentFile().getAbsolutePath() );
          }
        }
      }
    return back ;
  }

}
