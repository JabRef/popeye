/*
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


// created by : r.nagel 4.4.2005
//
// function : Application frame of "standalone" version
//
// todo     :
//
//
// modified :

package net.sf.langproper.gui ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.actions.* ;
import net.sf.langproper.gui.search.* ;

public class TMainFrame extends JFrame implements ActionListener
{
  BorderLayout borderLayout1 = new BorderLayout() ;
  TOverviewPanel oPanel = new TOverviewPanel() ;
  JMenuBar jMenuBar1 = new JMenuBar() ;
  JMenu jMenu1 = new JMenu( "File" ) ;
  JMenuItem newProjectMenuItem = new JMenuItem() ;
  JMenuItem loadMenuItem = new JMenuItem() ;
  JMenuItem saveMenuItem = new JMenuItem() ;
  JMenuItem saveAsMenuItem = new JMenuItem() ;
  JMenuItem singleSaveMenuItem = new JMenuItem() ;
  JMenuItem exitMenuItem = new JMenuItem() ;
  JMenuItem newEntryMenuItem = new JMenuItem() ;
  JMenu jMenu2 = new JMenu( "View" ) ;
  JCheckBoxMenuItem showSourceMenuItem = new JCheckBoxMenuItem() ;
  JCheckBoxMenuItem incompleteMenuItem = new JCheckBoxMenuItem() ;
  MyToolBar toolBar = new MyToolBar() ;
  JToggleButton viewButton = new JToggleButton() ;
  JToggleButton markButton = new JToggleButton() ;
  JToggleButton searchButton = new JToggleButton() ;

  public TLoadProjectAction loadAction = new TLoadProjectAction() ; // used by TStarter
  public TNewProjectAction newProjectAction = new TNewProjectAction();
  public TReloadProjectAction reloadAction =  new TReloadProjectAction() ;

  TCodeWizardAction codeWizardAction = new TCodeWizardAction();
  TSaveProjectAsAction saveAsAction = new TSaveProjectAsAction() ;
  TSaveProjectAction saveAction = new TSaveProjectAction() ;
  TSaveSingleFileAction singleSaveAction = new TSaveSingleFileAction() ;
  TChangeDataViewAction changeViewAction = new TChangeDataViewAction() ;
  TMarkIncompleteViewAction markViewAction = new TMarkIncompleteViewAction() ;
  TNewEntryAction newEntryAction = new TNewEntryAction() ;
  TExitAction exitAction = new TExitAction() ;
  TProjectPropertiesAction projectPropsAction = new TProjectPropertiesAction() ;
  JMenuItem projectPropsMenuItem = new JMenuItem( projectPropsAction ) ;
  JMenu jMenu3 = new JMenu( "Project" ) ;

  TOptionsDialogAction optionDialogAction = new TOptionsDialogAction() ;
  JMenuItem optionsDialogMenuItem = new JMenuItem() ;

  JMenu jMenu4 = new JMenu( "Tools" ) ;
  JMenuItem checkVersionMenuItem = new JMenuItem() ;

  JMenu helpMenu = new JMenu("Help") ;

  public TVCheckAction validCheckAction = new TVCheckAction() ;

  TSearchToolBar searchBar = new TSearchToolBar() ;

  public TMainFrame()
  {
    super() ;
    try
    {
      jbInit() ;
    }
    catch ( Exception exception )
    {
      System.out.println( "error creating main window" ) ;
      exception.printStackTrace() ;
    }
  }

  /* JBuilder GUI Designer Style */
  private void jbInit() throws Exception
  {
    this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE ) ;
    this.addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        exitRoutine() ;
      }
    } ) ;

    getContentPane().setLayout( borderLayout1 ) ;
    this.setJMenuBar( jMenuBar1 ) ;

    newEntryMenuItem.setAction( newEntryAction ) ;
    newProjectMenuItem.setAction( newProjectAction ) ;
    loadMenuItem.setAction( loadAction ) ;
    saveMenuItem.setAction( saveAction ) ;
    saveAsMenuItem.setAction( saveAsAction);
    singleSaveMenuItem.setAction( singleSaveAction ) ;

    exitMenuItem.setText( "Exit" ) ;

    showSourceMenuItem.setText( "source view" ) ;
    showSourceMenuItem.addActionListener( changeViewAction ) ;
    incompleteMenuItem.setText( "mark incomplete entries" ) ;
    incompleteMenuItem.addActionListener( markViewAction ) ;
    incompleteMenuItem.setSelected(
        TGlobal.config.getDirectBool( "markIncomplete", true ) ) ;

     optionsDialogMenuItem.setAction( optionDialogAction ) ;
     checkVersionMenuItem.setAction( new TCheckVersionAction() ) ;

    //    selLangMenuItem.setText( "Language" ) ;
//    selLangMenuItem.addActionListener( langSelAction );

    this.getContentPane().add( oPanel, java.awt.BorderLayout.CENTER ) ;
    jMenuBar1.add( jMenu1 ) ;
    jMenuBar1.add( jMenu2 ) ;
    jMenuBar1.add( jMenu3 ) ;
    jMenuBar1.add( jMenu4 ) ;
    jMenuBar1.add( Box.createHorizontalGlue() ) ;
    jMenuBar1.add( helpMenu ) ;

/*
    JMenu subMenu = new JMenu("TEST") ;
    subMenu.add( loadMenuItem) ;
    subMenu.add( new JMenuItem("sub-menu-item") ) ;
*/
    // File --------------------------------------------------------------------
    jMenu1.add( newProjectMenuItem ) ;
    jMenu1.addSeparator() ;
    jMenu1.add( loadMenuItem ) ;
//    jMenu1.add( subMenu ) ;
    jMenu1.add( saveMenuItem ) ;
    jMenu1.add( saveAsMenuItem ) ;
    jMenu1.add( singleSaveMenuItem ) ;
    jMenu1.addSeparator() ;
    jMenu1.add( exitMenuItem ) ;
    exitMenuItem.addActionListener( exitAction ) ;

    // View --------------------------------------------------------------------
    jMenu2.add( showSourceMenuItem ) ;
    jMenu2.add( incompleteMenuItem ) ;

    // Project
    jMenu3.add( newEntryMenuItem ) ;
    jMenu3.addSeparator() ;
    jMenu3.add( reloadAction ) ;
    jMenu3.add( projectPropsMenuItem ) ;


    // Tools -------------------------------------------------------------------
    jMenu4.add( optionsDialogMenuItem ) ;
    jMenu4.addSeparator() ;
    jMenu4.add( validCheckAction ) ;
    jMenu4.add( checkVersionMenuItem ) ;

    // Help -------------------------------------------------------------------
    helpMenu.add( new THelpAction( "Help",
                                   "some words about", "welcome") );

    helpMenu.add( new THelpAction( "Key bindings",
                                   "table of available key bindings", "keys") );

    // Toolbars ----------------------------------------------------------------
    viewButton.setAction( changeViewAction ) ;
    markButton.setAction( markViewAction ) ;
    searchButton.setAction( new TSearchViewAction() ) ;
    searchButton.setEnabled( false ) ;

    toolBar.add( reloadAction ) ;
    toolBar.addSeparator();
    toolBar.add( loadAction ) ;
    toolBar.add( saveAction ) ;

    toolBar.addSeparator() ;
    toolBar.add( searchButton ) ;

    toolBar.addSeparator() ;
    toolBar.add( viewButton ) ;
    toolBar.add( markButton ) ;

    if (TGlobal.DEVEL)
    {
      toolBar.addSeparator() ;
      toolBar.add( validCheckAction ) ;
      toolBar.add( codeWizardAction ) ;
    }

    JPanel toolBarArea = new JPanel() ;
    BoxLayout box2 = new BoxLayout( toolBarArea, BoxLayout.PAGE_AXIS ) ;
    toolBarArea.setLayout( box2 ) ;

    toolBarArea.add( toolBar ) ;
    toolBarArea.add( searchBar ) ;

    this.getContentPane().add( toolBarArea, BorderLayout.PAGE_START ) ;

    saveAction.setEnabled( false ) ;
    saveAsAction.setEnabled( false ) ;
    newEntryMenuItem.setEnabled( false ) ;

    this.disableButtons() ;

    if ( TGlobal.DEVEL )
    {
      this.setTitle( "DEVEL " + TGlobal.APPLICATION_NAME + " " +
                     TGlobal.VERSION_STRING ) ;
    }
    else
    {
      this.setTitle( TGlobal.APPLICATION_NAME + " " + TGlobal.VERSION_STRING ) ;
    }
    GUIGlobals.oPanel = oPanel ;
  }

  // --------------------------------------------------------------------------
  public void exitRoutine()
  {
    boolean exit = true ;

    TGlobal.config.setDirectInt( "mainWidth", this.getWidth() ) ;
    TGlobal.config.setDirectInt( "mainHeight", this.getHeight() ) ;

    // ask - save data ?
    if ( TGlobal.projects.isProjectChanged() )
    {
      int result = JOptionPane.showConfirmDialog( null,
                                                  "Project has changed - exit without saving files ?",
                                                  "Exit Question/Warning",
                                                  JOptionPane.YES_NO_OPTION ) ;

      if ( result != JOptionPane.YES_OPTION )
      {
        exit = false ;
      }
    }

    if ( exit )
    {
      // save the configuration
      try
      {
        // save the current project
        TGlobal.config.setLastProject( TGlobal.projects.getCurrentProject().getProjectFile()) ;
        TGlobal.config.save() ;
      }
      catch ( Exception e )
      {
        JOptionPane.showMessageDialog( null,
                                       "could not save the current configuration",
                                       "save configuration",
                                       JOptionPane.ERROR_MESSAGE
            ) ;
      }

      System.exit( 0 ) ;
    }
  }

  // --------------------------------------------------------------------------
  /** disables all buttons (save, save single....) */
  public void disableButtons()
  {
//    saveMenuItem.setEnabled( false ) ;
    saveAction.setEnabled( false ) ;
    saveAsAction.setEnabled( false ) ;

//    singleSaveMenuItem.setEnabled( false ) ;
    singleSaveAction.setEnabled( false ) ;

    newEntryMenuItem.setEnabled( false ) ;

    searchButton.setEnabled( false ) ;

    oPanel.setEnabled( false ) ;
  }

  /** enables all buttons (save, save single....) */
  public void enableButtons()
  {
    saveMenuItem.setEnabled( true ) ;
    saveAction.setEnabled( true ) ;
    saveAsAction.setEnabled( true ) ;

    singleSaveMenuItem.setEnabled( true ) ;
    singleSaveAction.setEnabled( true ) ;

    searchButton.setEnabled( true ) ;

    newEntryMenuItem.setEnabled( true ) ;

    oPanel.setEnabled( true ) ;
  }

  /** action from menu - radiobuttons (options -> mark entries -> ) */
  public void actionPerformed( ActionEvent e )
  {
    String com = e.getActionCommand() ;
    if ( TGlobal.config.setMarkEntriesMode( com ) ) // mode changed ?
    {
//        TGlobal.current.updateData(); // delete all cached rendering data
      GUIGlobals.oPanel.updateView() ;
    }
  }

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  // --------------------------------------------------------------------------
  private class TChangeDataViewAction extends AbstractAction
  {
    public TChangeDataViewAction()
    {
      super() ;
      putValue( Action.SHORT_DESCRIPTION, "enable/disable UTF view" ) ;

      boolean state = TGlobal.config.isShowSource() ;
      showSourceMenuItem.setSelected( state ) ;
      viewButton.setSelected( state ) ;
      setSelection( state ) ;

      putValue( Action.SMALL_ICON, viewButton.getIcon()) ;
    }

    private void setSelection( boolean state )
    {
      if ( state )
      {
        viewButton.setIcon( GUIGlobals.viewIcon1 ) ;
      }
      else
      {
        viewButton.setIcon( GUIGlobals.viewIcon2 ) ;
      }
    }

    public void actionPerformed( ActionEvent e )
    {
      boolean state ;
      if ( e.getSource() == showSourceMenuItem )
      {
        state = showSourceMenuItem.isSelected() ;
        viewButton.setSelected( state ) ;
      }
      else
      {
        state = viewButton.isSelected() ;
        showSourceMenuItem.setSelected( state ) ;
      }
      setSelection( state ) ;
      TGlobal.config.setShowSource( state ) ;
      oPanel.setShowSource( state ) ;
    }
  }

  // --------------------------------------------------------------------------
  private class TMarkIncompleteViewAction extends AbstractAction
  {
    public TMarkIncompleteViewAction()
    {
      super() ;
      putValue( Action.SHORT_DESCRIPTION, "mark/unmark incomplete entries" ) ;

      boolean state = TGlobal.config.isMarkIncomplete() ;
      incompleteMenuItem.setSelected( state ) ;
      markButton.setSelected( state ) ;
      setSelection( state ) ;

      putValue( Action.SMALL_ICON, markButton.getIcon()) ;
    }

    private void setSelection( boolean state )
    {
      if ( state )
      {
        markButton.setIcon( GUIGlobals.markIcon2 ) ;
      }
      else
      {
        markButton.setIcon( GUIGlobals.markIcon1 ) ;
      }
    }

    public void actionPerformed( ActionEvent e )
    {
      boolean state ;
      if ( e.getSource() == incompleteMenuItem )
      {
        state = incompleteMenuItem.isSelected() ;
        markButton.setSelected( state ) ;
      }
      else
      {
        state = markButton.isSelected() ;
        incompleteMenuItem.setSelected( state ) ;
      }

      setSelection( state ) ;
      TGlobal.config.setMarkIncomplete( state ) ;
      oPanel.setMarkIncomplete( state ) ;
    }
  }

  // --------------------------------------------------------------------------
  private class TExitAction extends AbstractAction
  {
    public TExitAction()
    {
      super( "Exit", GUIGlobals.viewIcon1 ) ;
    }

    public void actionPerformed( ActionEvent e )
    {
      exitRoutine() ;
    }
  }

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  private class TSearchViewAction extends AbstractAction
  {
    public TSearchViewAction()
    {
      super( "", GUIGlobals.getIcon( GUIGlobals.searchIconName ) ) ;
      putValue( Action.SHORT_DESCRIPTION, "find keys" ) ;
    }

    public void actionPerformed( ActionEvent e )
    {
      searchBar.setVisible( searchButton.isSelected() ) ;
    }
  }

  // --------------------------------------------------------------------------

}
