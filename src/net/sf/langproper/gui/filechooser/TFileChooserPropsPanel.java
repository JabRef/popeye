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


// created by : r.nagel 16.12.2005
//
// function : provides an encoding selection panel for the TFileChooser dialog
//
// todo     :
//
// modified :

package net.sf.langproper.gui.filechooser ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.* ;

public class TFileChooserPropsPanel extends JPanel implements ActionListener
//    implements PropertyChangeListener
{
  private JRadioButton javaEncRB = new JRadioButton( "java utf" ) ;
//  private JRadioButton wysiwygRB = new JRadioButton("WYSIWYG mode") ;
  private JRadioButton fileEncRB = new JRadioButton( "file specific" ) ;

  public TFileChooserPropsPanel( TFileChooser fc )
  {
    super() ;

    javaEncRB.setToolTipText("save as a string with \\u escape sequences");
    fileEncRB.setToolTipText("save with file specific settings for each file");

    javaEncRB.addActionListener(this);
    fileEncRB.addActionListener(this);

    setBorder( BorderFactory.createTitledBorder( "Encoding" ) ) ;

    ButtonGroup group = new ButtonGroup() ;
    group.add( javaEncRB ) ;
    group.add( fileEncRB ) ;

    javaEncRB.setSelected( true );

    //Font font = GUIGlobals.theme.getDialogStandardFont() ;
    Font font = fc.getFont() ;
    javaEncRB.setFont( font );
    fileEncRB.setFont( font );

    SpringLayout layout = new SpringLayout() ;
    setLayout( layout ) ;

    JPanel empty = new JPanel() ;

    add( javaEncRB ) ;
    add( fileEncRB ) ;
    add( empty ) ;

    //java RadioButton
    layout.putConstraint( SpringLayout.WEST, javaEncRB,
                          5,
                          SpringLayout.WEST, this ) ;
    layout.putConstraint( SpringLayout.NORTH, javaEncRB,
                          5,
                          SpringLayout.NORTH, this ) ;

    // file RadioButton
    layout.putConstraint( SpringLayout.WEST, fileEncRB,
                          0,
                          SpringLayout.WEST, javaEncRB ) ;
    layout.putConstraint( SpringLayout.NORTH, fileEncRB,
                          5,
                          SpringLayout.SOUTH, javaEncRB) ;

    // some space
    layout.putConstraint( SpringLayout.WEST, empty,
                          0,
                          SpringLayout.WEST, javaEncRB ) ;
    layout.putConstraint( SpringLayout.NORTH, empty,
                          0,
                          SpringLayout.SOUTH, fileEncRB) ;

    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, this,
                          10,
                          SpringLayout.EAST, fileEncRB ) ;
    layout.putConstraint( SpringLayout.SOUTH, this,
                          5,
                          SpringLayout.SOUTH, empty ) ;

  }

  /** read the project settings and update the viewable components */
  public void updateStatus()
  {
    TProject project = TGlobal.projects.getCurrentProject();
    TProjectSettings projectSettings = project.getSettings();

    if (projectSettings.getSaveWYSIWYGmode())
    {
      javaEncRB.setSelected(false);
      fileEncRB.setSelected(true);
    }
    else
    {
      fileEncRB.setSelected(false);
      javaEncRB.setSelected(true);
    }

  }

  public void actionPerformed( ActionEvent e )
  {
    TProject project = TGlobal.projects.getCurrentProject();
    TProjectSettings projectSettings = project.getSettings();

    if (javaEncRB.isSelected())
    {
      projectSettings.setSaveWYSIWYGmode(false);
    }
    else if (fileEncRB.isSelected())
    {
      projectSettings.setSaveWYSIWYGmode(true);
    }
  }

  /*
    public void propertyChange( PropertyChangeEvent e )
    {
      String prop = e.getPropertyName() ;

      //If the directory changed, don't show an image.
      if ( JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals( prop ) )
      {
        update = true ;

        //If a file became selected, find out which one.
      }
      else if ( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals( prop ) )
      {
        File file = ( File ) e.getNewValue() ;
        update = true ;
      }

      //Update the preview accordingly.
      if ( update )
      {
        if ( isShowing() )
        {
        }
      }
    }
   */
}
