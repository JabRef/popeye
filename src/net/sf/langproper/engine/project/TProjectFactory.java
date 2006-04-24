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


// created by : r.nagel 30.08.2005
//
// function : contains a list of all available project types
//
// todo     :
//
// modified :


package net.sf.langproper.engine.project ;

import java.util.* ;
import javax.swing.* ;
import javax.swing.event.ListDataListener ;
import net.sf.langproper.engine.project.java.*;
import net.sf.langproper.engine.project.worker.*;
import net.sf.langproper.*;
import net.sf.langproper.engine.project.php.*;

public class TProjectFactory implements ComboBoxModel
{
  private Vector liste = new Vector( 2 ) ;

  // used by ComboBoxModel, remember the index of the selected item in combobox
  private int selectedIndex = 0 ;

  protected TProjectFactory()
  {
    liste.add( new JBPMaker() ) ;
    if (TGlobal.DEVEL)
    {
      liste.add( new WPMaker() ) ;
      liste.add( new PhPBundleMaker() ) ;
    }
  }

  /** search the project type name into the list of registered types */
  private int foundType( String typeName )
  {
    int back = -1 ;

    if ( typeName != null )  // valid String
    {
      int t = 0 ;
      int len = liste.size() ;
      int hash = typeName.hashCode() ; // cache the hashcode of search string

      while ( t < len )  // check all registered items
      {
        // get the name of item t
        TProjectMaker dummy = ( TProjectMaker ) liste.get( t ) ;

        // found ?
        if ( hash == dummy.getTypeName().hashCode() )
        {
          back = t ; // remember the index
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

  /** returns a registered ProjectMaker at index <index> */
  private TProjectMaker getProjectMaker(int index)
  {
    TProjectMaker dummy = null ;
    if ( ( index > -1 ) && ( index < liste.size() ) )
    {
      dummy = ( TProjectMaker ) liste.get( index ) ;
    }

    return dummy ;
  }

  /** returns a ProjectMaker for a default Project (selectable with comboBoxModel) */
  private TProjectMaker getSelectedProjectMaker()
  {
    return getProjectMaker( selectedIndex ) ;
  }

  public TProject getProjectInstance( String type )
  {
    TProject back = null ;

    TProjectMaker maker = getProjectMaker( foundType( type ) ) ;
    if (maker != null)
    {
      back = maker.getInstance() ;
      back.init();
    }

    return back ;
  }

  // --------------------------------------------------------------------------
  public Enumeration getRegisteredProjectTypes()
  {
    return liste.elements() ;
  }

  // --------------------------------------------------------------------------
  // ComboBoxModel ------------------------------------------------------------
  // --------------------------------------------------------------------------
  public Object getSelectedItem()
  {
    return getElementAt( selectedIndex ) ;
  }

  public void setSelectedItem( Object anItem )
  {
    selectedIndex = foundType( ( String ) anItem ) ;
  }

  public int getSize()
  {
    return liste.size() ;
  }

  public Object getElementAt( int index )
  {
    String back = null ;
    if ( ( index > -1 ) && ( index < liste.size() ) )
    {
      TProjectMaker dummy = ( TProjectMaker ) liste.get( index ) ;
      back = dummy.getTypeName() ;
    }

    return back ;
  }

  public void addListDataListener( ListDataListener l )
  {
  }

  public void removeListDataListener( ListDataListener l )
  {
  }
}
