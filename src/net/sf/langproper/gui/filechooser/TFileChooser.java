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
// function : a filechooser for the application files
//
// todo     :
//
// modified :   16.12.2005 r.nagel
//              encoding chooser in save dialog

package net.sf.langproper.gui.filechooser ;

import java.util.* ;

import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import javax.swing.filechooser.*;
import java.io.File;

public class TFileChooser extends JFileChooser
{
  private ProjectFileFilter projectFilter ;
  private ProjectFileFilter allFilter ;

  private TFileChooserPropsPanel settings ;

  private Hashtable singleFilterSet ;

  public TFileChooser( String path, TProjectManager projects)
  {
    super(path) ;

    settings =  new TFileChooserPropsPanel( this ) ;

    projectFilter = new ProjectFileFilter() ;
    projectFilter.addExtension( TGlobal.PROJECT_FILE_EXTENSION ) ;
    projectFilter.setDescription( TGlobal.PROJECT_FILE_EXTENSION_DESC ) ;
//    projectFilter.setProjectTypeName(null);

    this.setFileFilter( projectFilter );
    init( projects) ;
  }

  /** display the open dialog */
  public int showOpenDialog()
  {
      setAccessory( null ) ;

      return super.showOpenDialog( null ) ;
  }

  /** display the save dialog and put a special settings option into */
  public int showSaveDialog()
  {
    settings.updateStatus();
//    setAccessory( settings ) ;

    return super.showSaveDialog( null ) ;
  }


  /** create the filters */
  private void init(TProjectManager projects)
  {
    allFilter = new ProjectFileFilter() ;
//    allFilter.setProjectTypeName(null);
    singleFilterSet = new Hashtable( ) ;

    for (Enumeration myEnum = projects.getRegisteredProjectTypes() ;
         myEnum.hasMoreElements() ; )
    {
      ProjectFileFilter filter = new ProjectFileFilter() ;
      TProjectMaker dummy = (TProjectMaker) myEnum.nextElement() ;

      dummy.insertFileFilter( filter ) ;
      dummy.insertFileFilter( allFilter );
      filter.setProjectTypeName( dummy.getTypeName() );  // store the project type (java, worker etc)

      singleFilterSet.put( dummy.getTypeName(), filter) ;

      this.setFileFilter(filter);
    }

    allFilter.setDescription("all registered language files");
    this.setFileFilter(allFilter);
  }

  /** project files */
  public void setProjectFiler()
  {
    this.setFileFilter( projectFilter ) ;
  }

  /** set a filter of all registered files */
  public void setAllFilesFilter()
  {
    this.setFileFilter( allFilter );
  }

  /** set the project specific filefilter, or if it could not be found, set
   *  the "allfiles" filter */
  public void setProjectFileFilter(String projectTypeName)
  {
    FileFilter filter = getFilter( projectTypeName) ;
    setFileFilter( filter );
  }

  /** returns the project specific filefilter, or if it could not be found,
   *  returns the "allfiles" filter */
  private FileFilter getFilter( String projectTypeName)
  {
    ProjectFileFilter filter = ( ProjectFileFilter )
                                   singleFilterSet.get( projectTypeName ) ;
    if ( filter != null )
    {
      return filter ;
    }

    return this.getAcceptAllFileFilter() ;
  }


  // --------------------------------------------------------------------------
  /** returns the project type for this file(extension) */
  public String getProjectType( File file )
  {
    String back = null ;

    Enumeration myEnum = singleFilterSet.elements() ;
    boolean found = false ;

    while ( (!found) && (myEnum.hasMoreElements() ))
    {
      ProjectFileFilter filter = ( ProjectFileFilter ) myEnum.nextElement() ;
      if (filter.accept( file ))
      {
        found = true ;
        back = filter.getProjectTypeName() ;
      }
    }

    return back ;
  }

}
