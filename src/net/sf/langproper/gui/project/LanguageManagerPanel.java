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


// created by : r.nagel 10.12.2005
//
// function : managing of all languages
//
// todo     :  remove a language in a live system
//
// modified :

package net.sf.langproper.gui.project ;

import java.util.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;

import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.model.* ;
import net.sf.quercus.* ;

public class LanguageManagerPanel extends JPanel implements ActionListener, LanguageUpdateListener, ListSelectionListener
{
  private TLangPropsModel model ;
  private JTable table ;

  private JButton newButton ;
  private JButton delButton ;
  private JButton editButton ;

  // Listener
  private Vector luListener = new Vector() ;

  private LanguageTypeDialog dialog = null ;

  private TProject project = null ;

  private boolean removeStat = false ;

  public LanguageManagerPanel(TProject pProject, JDialog parent, boolean canRemove)
  {
    removeStat = canRemove ;

    if (parent != null)
    {
      dialog = new LanguageTypeDialog(parent);
    }
    else
    {
      dialog = new LanguageTypeDialog();
    }

    setLayout( new BorderLayout() ) ;

    project = pProject ;
    if (pProject != null)
    {
      model = new TLangPropsModel( pProject.getProjectData().getAvailableLangs() ) ;
    }
    else
    {
      model = new TLangPropsModel( null ) ;
    }
    TableSorter sorter = new TableSorter( model ) ;

    table = new JTable( sorter ) ;
    sorter.setTableHeader( table.getTableHeader() ) ;
    table.setPreferredScrollableViewportSize( new Dimension( 250, 170 ) ) ;
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    table.getSelectionModel().addListSelectionListener( this );

    //Set up tool tips for column headers.
    table.getTableHeader().setToolTipText(
        "Click to specify sorting; Control-Click to specify secondary sorting" ) ;

    //Create the scroll pane and add the table to it.
    JScrollPane scroller = new JScrollPane( table ) ;
//    scroller.setMaximumSize( new Dimension(300, 300) );
//    scroller.setPreferredSize( new Dimension(300, 200) );

    // language manipulating buttons panel -----------------------------------
//    lmPanel.setBorder( BorderFactory.createEtchedBorder());

    SpringLayout layout = new SpringLayout() ;
    setLayout( layout ) ;


    // add a new language version
    newButton = new JButton("add") ;
    newButton.addActionListener( this );

    editButton = new JButton("edit") ;
    editButton.addActionListener(this);
    editButton.setEnabled(false);

    delButton = new JButton("remove") ;
    delButton.addActionListener( this );
    delButton.setEnabled(false);

    add( scroller ) ;
    add(newButton) ;
    add(editButton) ;
    add(delButton) ;

    //table
    layout.putConstraint( SpringLayout.WEST, scroller,
                          5,
                          SpringLayout.WEST, this ) ;
    layout.putConstraint( SpringLayout.NORTH, scroller,
                          5,
                          SpringLayout.NORTH, this) ;


    //addButton
    layout.putConstraint( SpringLayout.WEST, newButton,
                          5,
                          SpringLayout.EAST, scroller ) ;
    layout.putConstraint( SpringLayout.NORTH, newButton,
                          15,
                          SpringLayout.NORTH, scroller) ;

    //editButton
    layout.putConstraint( SpringLayout.WEST, editButton,
                          5,
                          SpringLayout.EAST, scroller ) ;
    layout.putConstraint( SpringLayout.NORTH, editButton,
                          5,
                          SpringLayout.SOUTH, newButton) ;

    //removeButton
    layout.putConstraint( SpringLayout.WEST, delButton,
                          5,
                          SpringLayout.EAST, scroller ) ;
    layout.putConstraint( SpringLayout.NORTH, delButton,
                          15,
                          SpringLayout.SOUTH, editButton) ;


    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, this,
                          5,
                          SpringLayout.EAST, delButton ) ;
    layout.putConstraint( SpringLayout.SOUTH, this,
                          5,
                          SpringLayout.SOUTH, scroller ) ;

    layout.layoutContainer( this ) ;
  }

  public void setProject( TProject pProject)
  {
    project = pProject ;
    if (pProject != null)
    {
      model.setModelData( pProject.getProjectData().getAvailableLangs() );
    }
  }
  // --------------------------------------------------------------------------
  public void startEdit(int langIndex)
  {
    if (project != null)
    {
      TProjectFileList files = project.getProjectData().getAvailableLangs() ;

      TLanguageFile langFile = files.getData( langIndex ) ;
      if ( dialog.showModal( langFile, project ) == TInternalDialog.MR_OKAY)
      {
        fireLanguageUpdate() ;
      }
    }
  }


  public void startAdd()
  {
     // only if any data available
     if (project.getProjectData() != null)
     {
       if ( dialog.showModal(null, project) == TInternalDialog.MR_OKAY )
       {
         fireLanguageUpdate() ;
         model.fireTableDataChanged();
       }
     }
  }


  // --------------------------------------------------------------------------
  // ActionListener
  // --------------------------------------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if (sender == editButton )
    {
      int index = table.getSelectedRow() ;
      if (index >= 0)
      {
        startEdit( index );
      }
    }
    else if (sender == newButton)
    {
      startAdd() ;
    }
    else if (sender == delButton)
    {
      // NOT WORKING --------------------------------
       int row = table.getSelectedRow() ;

       if ( (project != null) && (row > -1))
       {
         TProjectFileList list = project.getProjectData().getAvailableLangs() ;
         TLanguageFile file = list.getData(row) ;

         list.removeLanguageVersion( file ) ;

         if (list.getSize() < 1)
         {
           delButton.setEnabled(false);
         }
         model.fireTableDataChanged() ;
         fireLanguageUpdate() ;
//         project.getProjectData().fireTableDataChanged();
       }


    }
  }

  // --------------------------------------------------------------------------
  // LanguageUpdateListener
  // --------------------------------------------------------------------------
  public void updateStructure()
  {
    model.fireTableDataChanged();
  }

  /** insert a listener for the update action */
  public void addLanguageUpdateListener( LanguageUpdateListener listener )
  {
    if ( listener != null )
    {
      luListener.add( listener ) ;
    }
  }

  public void removeLanguageUpdateListener( LanguageUpdateListener listener )
  {
    if ( listener != null )
    {
      luListener.remove( listener ) ;
    }
  }

  public void fireLanguageUpdate()
  {
    for ( int t = 0, len = luListener.size() ; t < len ; t++ )
    {
      LanguageUpdateListener listener = ( LanguageUpdateListener ) luListener.get( t ) ;
      listener.updateStructure() ;
    }
  }


  // --------------------------------------------------------------------------
  // ListSelectionListener ----------------------------------------------------
  // --------------------------------------------------------------------------
  public void valueChanged( ListSelectionEvent e )
  {
    ListSelectionModel lsm = ( ListSelectionModel ) e.getSource() ;

    //Ignore extra messages.
    if ( e.getValueIsAdjusting() )
    {
      return ;
    }
    if ( lsm.isSelectionEmpty() )
    {
      //no rows are selected
      delButton.setEnabled(false);
      editButton.setEnabled(false);
    }
    else
    {
      delButton.setEnabled(removeStat);
      editButton.setEnabled(true);
    }

  }


}
