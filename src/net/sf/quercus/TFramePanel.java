package net.sf.quercus;

import javax.swing.* ;
import java.awt.*;

public class TFramePanel extends JPanel
{
  private JPanel header = new JPanel(false) ;
  private JPanel content = new JPanel() ;
  private JLabel titleLabel = new JLabel() ;

  public TFramePanel()
  {
    super( new BorderLayout() ) ;

//    header.setBackground( Color.darkGray );
    header.setBackground( new Color(146, 153, 153) );
    titleLabel.setForeground( Color.white );
    header.add(titleLabel) ;
    header.setBorder( BorderFactory.createEtchedBorder());

//    content.setBorder( BorderFactory.createLineBorder( Color.black));
    content.setBorder( BorderFactory.createEtchedBorder());

    super.add( header, BorderLayout.PAGE_START );
    super.add( content, BorderLayout.CENTER) ;
  }

  public TFramePanel(String title)
  {
    this() ;
    setTitle( title ) ;
  }

  public JPanel getContentPanel()
  {
    return content ;
  }

  public void setTitle( String title )
  {
    titleLabel.setText( title );
  }

/*
  public void setLayout( LayoutManager manager )
  {
    content.setLayout( manager ) ;
  }
*/
}
