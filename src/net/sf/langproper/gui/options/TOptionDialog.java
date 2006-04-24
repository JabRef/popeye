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

// created by : r.nagel 08.08.2005
//
// function : option dialog
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options ;

import java.beans.* ;
import java.util.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;
import javax.swing.event.* ;
import javax.swing.tree.* ;

import net.sf.langproper.* ;
import net.sf.langproper.gui.* ;
import net.sf.quercus.* ;

public class TOptionDialog extends TInternalDialog implements ActionListener,
    TreeSelectionListener,
    PropertyChangeListener
{
  // some functions can only work correctly, after a restart of application
  // was performed
  private boolean restartRequired = false ;

  private JButton closeButton = new JButton( "close" ) ;
  private JButton cancelButton = new JButton( "cancel" ) ;

  private CardLayout optionLayout = new CardLayout( 5, 5 ) ;
  private JPanel optionPanel = new JPanel( optionLayout ) ;

  private JTree tree ;
  private TAbstractOptionPanel topPanel = new TRootPanel() ; // root node/panel

  private boolean needReload = true ;

  public TOptionDialog()
  {
    super( "Options" ) ;

    this.setResizable( false ) ;

    // tree ----------------------------------------------------------

    topPanel.addPropertyChangeListener( this ) ;

    tree = new JTree( topPanel.getTreeNode() ) ;
    tree.setPreferredSize( new Dimension( 150, 100 ) ) ;
    tree.setBorder( new EtchedBorder() ) ;
    tree.addTreeSelectionListener( this ) ;
    //tree.setCellRenderer(new MyRenderer());
    tree.setVisibleRowCount( 1 ) ;
    tree.setShowsRootHandles( true ) ;
    //tree.setRootVisible( false ) ;

    TAbstractOptionPanel gui = new DefaultGUIPanel() ;

    insertOption( topPanel, null ) ;
    insertOption( new TBaseOptionsPanel(), topPanel ) ;
    insertOption( gui, topPanel ) ;
    insertOption( new FontPropsPanel(), gui ) ;

    // buttons ------------------------------------------------------
    JPanel buttonPanel = new JPanel() ;
    buttonPanel.setLayout( new BorderLayout() ) ;
    buttonPanel.setBorder( new EmptyBorder( new Insets( 5, 5, 5, 5 ) ) ) ;

    JSeparator separator = new JSeparator() ;

    Box buttonBox = new Box( BoxLayout.X_AXIS ) ;
    buttonBox.setBorder( new EmptyBorder( new Insets( 5, 5, 0, 0 ) ) ) ;
    buttonBox.add( Box.createHorizontalStrut( 10 ) ) ;
    buttonBox.add( closeButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 20 ) ) ;
    buttonBox.add( cancelButton ) ;
    buttonBox.add( Box.createHorizontalStrut( 5 ) ) ;

    buttonPanel.add( separator, BorderLayout.NORTH ) ;
    buttonPanel.add( buttonBox, BorderLayout.EAST ) ;

    closeButton.addActionListener( this ) ;
    cancelButton.addActionListener( this ) ;

    // put all gui elements into the main layout ----------------------------
    Container content = this.getContentPane() ;
    content.setLayout( new GridBagLayout() ) ;

    JScrollPane treeScroller = new JScrollPane( tree ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints() ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.gridheight = 2 ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.anchor = GridBagConstraints.LINE_START ;
    gridBagConstraints.insets = new Insets( 5, 0, 0, 0 ) ;
    content.add( treeScroller, gridBagConstraints ) ;

    gridBagConstraints = new GridBagConstraints() ;
    gridBagConstraints.gridx = 1 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.anchor = GridBagConstraints.WEST ;
    content.add( optionPanel, gridBagConstraints ) ;

    gridBagConstraints = new GridBagConstraints() ;
    gridBagConstraints.gridx = 1 ;
    gridBagConstraints.gridy = 1 ;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL ;
    gridBagConstraints.anchor = GridBagConstraints.CENTER ;
    content.add( buttonPanel, gridBagConstraints ) ;

    expandAllNodes() ;
  }

  private void insertOption( TAbstractOptionPanel panel,
                             TAbstractOptionPanel parent )
  {
    if ( parent != null )
    {
      DefaultMutableTreeNode parentNode = parent.getTreeNode() ;
      parentNode.add( panel.getTreeNode() ) ;
    }
    optionPanel.add( panel, panel.getID() ) ;
  }

  // --------------------------------------------------------------------------
  private void expandAllNodes()
  {
    // don't cache the getRowCount value, because its variable during the
    // expand process
    for (int row = 0 ; row < tree.getRowCount(); row++)
    {
        tree.expandRow(row);
    }
  }

  private void collapseAllNodes()
  {
    int row = tree.getRowCount() - 1 ;
    while ( row >= 0 )
    {
      tree.collapseRow( row ) ;
      row-- ;
    }
  }

  // --------------------------------------------------------------------------
  /** before the options dialog is shown the next time, a reload of the
   *  configuration data must be performed */
  public void setNeedReload()
  {
    needReload = true ;
  }

  /** resets some necessary data flags - used by showModal, reuse of the dialog */
  protected void init()
  {
    super.init() ;

    if ( needReload ) // data not valid -> reload
    {
      loadOptions() ;
    }
  }

  /** shows the dialog as "modal" and returns a status info */
  public int showModal()
  {

    int myResult = super.showModal() ;

    if ( myResult != TInternalDialog.MR_OKAY ) // dialog was canceled
    {
      needReload = true ;
    }
    else // ok button -> save "the changed" settings
    {
      restartRequired = saveOptions( false ) ;
    }

    if ( restartRequired )
    {
      JOptionPane.showMessageDialog( ( JFrame )null,
         "Some functions can only work correctly, after a restart of application was performed !"
          ) ;
    }

    return myResult ;
  }

  // --------------  load / save options into the database ---------------------

  /** load all options from database */
  private void loadOptions()
  {
    DefaultMutableTreeNode root = topPanel.getTreeNode() ;

    for ( Enumeration myEnum = root.breadthFirstEnumeration() ;
          myEnum.hasMoreElements() ; )
    {
      // get the node
      DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) myEnum.
          nextElement() ;

      // extract the panel from node
      TAbstractOptionPanel panel = ( TAbstractOptionPanel ) node.getUserObject() ;

      // load the current config
      panel.loadConfig() ;
    }

    // all data reloaded, reset the flag
    needReload = false ;
  }

  /** save all configured options
   *   returns true, if the application must be restartet
   * */
  private boolean saveOptions( boolean saveAlways )
  {
    boolean back = false ;

    DefaultMutableTreeNode root = topPanel.getTreeNode() ;

    for ( Enumeration myEnum = root.breadthFirstEnumeration() ;
          myEnum.hasMoreElements() ; )
    {
      // get the node
      DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) myEnum.
          nextElement() ;

      // extract the panel from node
      TAbstractOptionPanel panel = ( TAbstractOptionPanel ) node.getUserObject() ;

      // save the current configuration, if some data has been changed
      // returns the status of "needReload of application"
      if ( panel.hasChanged() || saveAlways )
      {
        back = back | panel.applyChanges() ;
      }
    }

    return back ;
  }

  // ------------ ActionListener -----------------------------------------------
  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;
    if ( sender == closeButton )
    {
      this.setModalResult( TInternalDialog.MR_OKAY ) ;
    }
    else if ( sender == cancelButton )
    {
      this.setModalResult( TInternalDialog.MR_CANCEL ) ;
    }
  }

  // --------------- TreeSelectionListener -------------------------------------
  public void valueChanged( TreeSelectionEvent e )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode )
        tree.getLastSelectedPathComponent() ;

    if ( node == null )
    {
      return ;
    }

    // extract the panel
    TAbstractOptionPanel panel = ( TAbstractOptionPanel ) node.getUserObject() ;

    // show the panel
    optionLayout.show( optionPanel, panel.getID() ) ;
  }

  // ----------------- PropertyChangeListener ---------------------------------
  public void propertyChange( PropertyChangeEvent evt )
  {
    if ( evt.getSource() == topPanel )
    {
      int hash = evt.getPropertyName().hashCode() ;
      if ( hash == "reload".hashCode() )
      {
        loadOptions() ;
      }
      else if ( hash == "save".hashCode() )
      {
        saveOptions( true ) ; // save all settings from dialog
      }
      else if ( hash == "reset".hashCode() )
      {
        TGlobal.config.reset() ;
        loadOptions() ;
      }
    }
  }
  /*
    class MyRenderer extends DefaultTreeCellRenderer
    {
      private static final long serialVersionUID = 1L;
          TImageIconProvider iprovider ;

      public MyRenderer()
      {
        iprovider = TImageIconProvider.getImageIconProvider();
      }

      public Component getTreeCellRendererComponent(
   JTree tree, Object value, boolean sel,  boolean expanded, boolean leaf,
          int row, boolean hasFocus)
      {

        super.getTreeCellRendererComponent(
            tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value ;
        NodeInfo nodeInfo = (NodeInfo)(node.getUserObject());

        if (nodeInfo.iconNumb != -1)
        {
          setIcon(iprovider.getImageIcon(nodeInfo.iconNumb) ) ;
          setToolTipText("ToolTip");
        }
        else
          setToolTipText(null); //no tool tip

        return this;
      }
    }
   */

}
