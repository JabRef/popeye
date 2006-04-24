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
// function : basic option panel, all other panel must extend it
//
// todo     :
//
// modified :

package net.sf.langproper.gui.options ;

import javax.swing.* ;
import javax.swing.tree.*;

public abstract class TAbstractOptionPanel extends JPanel
{
  private DefaultMutableTreeNode treeNode ;
  private String myTitle ;
  private String myPanelID ;

  private boolean needRestart = false ;

  public TAbstractOptionPanel(String title, String panelID)
  {
    myTitle = title ;
    myPanelID = panelID ;
    treeNode = new DefaultMutableTreeNode( this ) ;
  }

  public String getID()
  {
    return myPanelID ;
  }

  public DefaultMutableTreeNode getTreeNode()
  {
    return treeNode ;
  }

  public void addSubPanel( TAbstractOptionPanel panel )
  {
    treeNode.add( panel.getTreeNode() ) ;
  }

  public abstract String getPanelBorderTitle(); // panel title

  /** update global Application properties,
   *  returns true, if the application must restart
   */
  public boolean applyChanges()
  {
    boolean dummy = needRestart ;
    needRestart = false ;

    return dummy ;
  }

  /** some properties has changed */
  public abstract boolean hasChanged();

  /** put's the actual config into the panel */
  public abstract void loadConfig() ;


  public TAbstractOptionPanel()
  {
    String borderTitle = getPanelBorderTitle();
    setBorder(BorderFactory.createTitledBorder(borderTitle));
  }

  public String toString()
  {
    return myTitle ;
  }

  public void setNeedRestart()
  {
    needRestart = true ;
  }
}
