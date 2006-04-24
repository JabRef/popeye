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

$Id: TStatusLine.java,v 1.1.1.1 2005/12/13 13:02:48 kiar Exp $
*/

package net.sf.langproper.gui;

import java.awt.*;
import java.awt.event.* ;
import javax.swing.*;
import net.sf.langproper.engine.sort.*;

public class TStatusLine extends JPanel
                         implements ComponentListener
{
  private static final long serialVersionUID = 3258408439360204854L;

  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JLabel sortDirectionLabel = new JLabel();
  JTextField entryNumberField = new JTextField();
  JTextField commentsField = new JTextField();
  FlowLayout flowLayout2 = new FlowLayout();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  FlowLayout flowLayout1 = new FlowLayout();

  public TStatusLine()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception
  {
    this.setLayout(flowLayout1);
    jPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel1.setMaximumSize(new Dimension(32767, 20));
    jPanel1.setMinimumSize(new Dimension(120, 20));
    jPanel1.setPreferredSize(new Dimension(120, 20));
    jPanel1.setLayout(flowLayout2);
    jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setMaximumSize(new Dimension(32767, 20));
    jPanel2.setMinimumSize(new Dimension(14, 20));
    jPanel2.setLayout(flowLayout4);
    jPanel3.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel3.setMaximumSize(new Dimension(32767, 20));
    jPanel3.setLayout(flowLayout3);
    sortDirectionLabel.setFont(new java.awt.Font("Dialog", 0, 11));
    sortDirectionLabel.setMaximumSize(new Dimension(32767, 16));
    sortDirectionLabel.setMinimumSize(new Dimension(190, 16));
    sortDirectionLabel.setPreferredSize(new Dimension(190, 16));
    sortDirectionLabel.setText("sorting");
    entryNumberField.setBackground(new Color(212, 208, 200));
    entryNumberField.setFont(new java.awt.Font("Dialog", 0, 11));
    entryNumberField.setMaximumSize(new Dimension(2147483647, 16));
    entryNumberField.setMinimumSize(new Dimension(40, 16));
    entryNumberField.setPreferredSize(new Dimension(80, 16));
    entryNumberField.setRequestFocusEnabled(false);
    entryNumberField.setEditable(false);
    entryNumberField.setText("");

    commentsField.setMaximumSize(new Dimension(32767, 16));
    commentsField.setMinimumSize(new Dimension(10, 16));
    commentsField.setPreferredSize(new Dimension(210, 16));
    entryNumberField.setBackground(new Color(212, 208, 200));
    commentsField.setFont(new java.awt.Font("Dialog", 0, 11));
    commentsField.setRequestFocusEnabled(false);
    commentsField.setEditable(false);
    commentsField.setText("");


    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(0);
    flowLayout2.setVgap(0);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setHgap(0);
    flowLayout4.setVgap(0);
    this.setMaximumSize(new Dimension(32767, 20));
    this.setMinimumSize(new Dimension(181, 20));
    this.setPreferredSize(new Dimension(262, 20));
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    this.add(jPanel1, null);
    jPanel1.add(sortDirectionLabel, null);
    this.add(jPanel3, null);
    jPanel3.add(entryNumberField, null);
    this.add(jPanel2, null);
    jPanel2.add(commentsField, null);

    this.addComponentListener(this); // move, show, hide, resize
  }


    // -----------------------------------------------------------------------
    // Component Listener - move, resize, show, hide events

    public void componentResized(ComponentEvent e)
    {
      adjustLine() ;
    }

    public void componentMoved(ComponentEvent e)
    {
    }

    public void componentShown(ComponentEvent e)
    {
    }

    public void componentHidden(ComponentEvent e)
    {
    }
/* -------------------------------------------------------------------------- */

    private final void adjustLine()
    {
      /* length of jPanel2 = total length - Status Label and Text */
      int len = this.getWidth()
                 - this.jPanel1.getWidth() - this.jPanel3.getWidth() ;

      int hi1 = this.jPanel2.getHeight() ;
      int hi2 = this.commentsField.getHeight() ;

      int len1 = sortDirectionLabel.getWidth() ;

      jPanel1.setPreferredSize( new Dimension(len1, jPanel1.getHeight()) );

      if (len < 5) len = this.jPanel2.getMinimumSize().width ;

      this.jPanel2.setPreferredSize(new Dimension(len, hi1));
      this.commentsField.setPreferredSize(new Dimension(len - 4, hi2));
      this.jPanel2.revalidate() ;
    }

    public void updateSortingDirectionLabel()
    {
      TSorter sort = GUIGlobals.sortDirection.getActive() ;
      sortDirectionLabel.setText( sort.getDescription());
      sortDirectionLabel.setToolTipText( sort.getToolTip() );
    }

    public void setNumberOfEntries( int size )
    {
      this.entryNumberField.setText( Integer.toString( size ) +" items");
    }

    public void setComment( String comment)
    {
      if (comment != null)
      {
        this.commentsField.setText( comment );
      }
      else
        this.commentsField.setText( "" );
    }


}
