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
// function : option panel
//
// todo     :
//
// modified :


package net.sf.langproper.gui.options ;

import javax.swing.*;
import java.awt.*;
import net.sf.langproper.*;
import net.sf.versions.*;
import java.awt.event.*;

public class TRootPanel
    extends TAbstractOptionPanel implements ActionListener
{
  private JButton resetButton = new JButton("reset") ;
  private JButton loadButton = new JButton("load") ;
  private JButton saveButton = new JButton("save") ;

  private JFileChooser fileDialog = new JFileChooser() ;

  public TRootPanel()
  {
    super("Options", "root") ;

    JPanel jPanel2 = new JPanel() ;
    jPanel2.setLayout(new java.awt.GridBagLayout());

     jPanel2.setBorder(new javax.swing.border.TitledBorder("global informations"));

     JTextArea jTextArea1 = new JTextArea() ;
     jTextArea1.setBackground(getBackground());
//     jTextArea1.setColumns();
     jTextArea1.setEditable(false);
     jTextArea1.setRows(5);

     TBuildInfo binf = TGlobal.runtime.getCurrentBuildInfo() ;
     jTextArea1.setText("This is the " +TGlobal.APPLICATION_NAME +" options dialog.\n");
     jTextArea1.append("\nversion\t:" +binf.getBUILD_VERSION());
     jTextArea1.append("\nbuild\t:" +binf.getBUILD_NUMBER());
     jTextArea1.append("\ndate\t:" +binf.getBUILD_DATE());
     jTextArea1.append("\ninfo\t:" +binf.getBUILD_META() +"\n\n");
     GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
     gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 0);
     jPanel2.add(jTextArea1, gridBagConstraints);

     JPanel jPanel4 = new JPanel() ;
     jPanel4.setLayout(new java.awt.GridBagLayout());
//     jPanel4.setBorder(new javax.swing.border.TitledBorder("option center"));

     JTextArea jTextArea2 = new JTextArea() ;
     jTextArea2.setBackground(getBackground());
     jTextArea2.setEditable(false);
     jTextArea2.setText("These buttons provide some functions for easy handling of application settings.");
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.gridwidth = 2;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
     gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
     jPanel4.add(jTextArea2, gridBagConstraints);

     resetButton.addActionListener(this);
     resetButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
     resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
     gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
     jPanel4.add(resetButton, gridBagConstraints);

     JLabel jLabel1 = new JLabel() ;
     jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
     jLabel1.setText("restore all options and delete the user specific settings");
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
     gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
     jPanel4.add(jLabel1, gridBagConstraints);

     loadButton.addActionListener(this);
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
     gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 0);
     jPanel4.add(loadButton, gridBagConstraints);

     saveButton.addActionListener(this);
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 3;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
     gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
     jPanel4.add(saveButton, gridBagConstraints);

     JLabel jLabel2 = new JLabel() ;
     jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
     jLabel2.setText("load settings from external file");
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
     gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
     jPanel4.add(jLabel2, gridBagConstraints);

     JLabel jLabel3 = new JLabel() ;
     jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
     jLabel3.setText("save settings into external file");
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 3;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
     gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
     jPanel4.add(jLabel3, gridBagConstraints);

     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.gridwidth = 2;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     jPanel2.add(jPanel4, gridBagConstraints);

     add(jPanel2);

  }

  /**
   *
   * @return boolean
   * @todo Implement this net.sf.langproper.gui.options.TAbstractOptionPanel
   *   method
   */
  public boolean applyChanges()
  {
    return false ;
  }

  /**
   * getPanelBorderTitle
   *
   * @return String
   * @todo Implement this net.sf.langproper.gui.options.TAbstractOptionPanel
   *   method
   */
  public String getPanelBorderTitle()
  {
    return "" ;
  }

  /**
   *
   * @return boolean
   * @todo Implement this net.sf.langproper.gui.options.TAbstractOptionPanel
   *   method
   */
  public boolean hasChanged()
  {
    return false ;
  }

  /**
   *
   * @todo Implement this net.sf.langproper.gui.options.TAbstractOptionPanel
   *   method
   */
  public void loadConfig()
  {
  }


  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;

    if ( sender == saveButton)
    {
      if ( fileDialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
      {
        TGlobal.config.saveToFile(fileDialog.getSelectedFile());
      }
    }
    else if (sender == loadButton)
    {
      if (fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
      {
        // load from file
        if ( TGlobal.config.loadFromFile( fileDialog.getSelectedFile()) )
        {
          this.firePropertyChange( "reload", 0, 1 ) ;  // no error -> update view
        }
        else
        {
          this.firePropertyChange( "save", 0, 1 ) ;  // error -> put current settings into database
        }
      }
    }
    else if (sender == resetButton)
    {
      this.firePropertyChange("reset", 0, 1);
    }

  }
}
