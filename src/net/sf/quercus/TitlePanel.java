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

// created by : r.nagel 4.04.2006
//
// function : a title area for a dialog
//
//
// todo     :
//
// modified :

package net.sf.quercus ;

import java.awt.* ;
import javax.swing.* ;
import net.sf.quercus.helpsystem.TInfoTextArea;
import net.sf.quercus.helpsystem.TInfoText;

public class TitlePanel extends JPanel
{
  private Color gradiantColor ;
  private float gradiantOffsetRatio ;


  private JLabel title = new JLabel() ;
  private TInfoTextArea infoText = new TInfoTextArea() ;

  public TitlePanel(String panelID)
  {
    this.setBackground( Color.white );

    gradiantColor = new Color(100,100,200,50) ;
    gradiantOffsetRatio = 0.5f ;

    title.setFont( new java.awt.Font( "MS Sans Serif", Font.BOLD, 12 ) ) ;
    title.setText( TInfoText.runtime.getTitle(panelID, "title") ) ;

    infoText.setFont( new java.awt.Font( "MS Sans Serif", Font.PLAIN, 11 ) ) ;
    infoText.setTextID(panelID, "summary");
    infoText.setBackground( Color.white );
    infoText.setOpaque(true);

    SpringLayout layout = new SpringLayout() ;
    setLayout( layout ) ;

    add( title ) ;
    add( infoText ) ;


    // title
    layout.putConstraint( SpringLayout.WEST, title,
                          5,
                          SpringLayout.WEST, this ) ;
    layout.putConstraint( SpringLayout.NORTH, title,
                          5,
                          SpringLayout.NORTH, this ) ;

    // Beschreibung
    layout.putConstraint( SpringLayout.WEST, infoText,
                          10,
                          SpringLayout.WEST, title ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          15,
                          SpringLayout.NORTH, title ) ;

    // panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, this,
                          20,
                          SpringLayout.EAST, infoText ) ;
    layout.putConstraint( SpringLayout.SOUTH, this,
                          5,
                          SpringLayout.SOUTH, infoText ) ;


//    this.setPreferredSize( new Dimension(200, 100) );


  }

  public void paint( Graphics g )
  {

    Graphics2D g2D = ( Graphics2D ) g ;
//    g2D.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON ));

    super.paint( g ) ;

    super.paintChildren( g ) ;
    //Paint Gradiant
    if ( gradiantColor != null )
    {
      Color c1 = new Color( 255, 255, 255, 0 ) ;
      int offset = ( int ) ( getWidth() * gradiantOffsetRatio ) ;
      GradientPaint gp = new GradientPaint( getWidth() - offset, 0, c1,
                                            getWidth(), 0, gradiantColor ) ;
      g2D.setPaint( gp ) ;
      g2D.fillRect( 0, 0, getWidth(), getHeight() ) ;
    }

  }

}
