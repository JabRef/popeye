package net.sf.langproper.gui.fields.single ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.event.* ;

import net.sf.langproper.* ;
import net.sf.langproper.engine.* ;
import net.sf.langproper.engine.project.* ;
import net.sf.langproper.gui.* ;
import net.sf.quercus.* ;

public class TSingleEntryPanel extends JPanel implements ListSelectionListener
{
  private int activeEntry = -1 ;
  private TMultiLanguageEntry currentEntry ;

  public TSingleEntryPanel()
  {
    setLayout( new BorderLayout(5, 5) ) ;

    add( getMainPanel(), BorderLayout.CENTER ) ;
  }

  private JPanel getMainPanel()
  {
    JPanel mainPanel = new JPanel() ;

    TFramePanel valuePanel = makeLangFrame() ;
    TFramePanel refPanel = makeRefFrame() ;
    TFramePanel commentPanel = makeCommentFrame() ;

    mainPanel.setLayout( new BorderLayout( 5, 5 ) ) ;

    JPanel center = new JPanel( new BorderLayout( 5, 5 ) ) ;
    center.add( valuePanel, BorderLayout.NORTH ) ;
    center.add( commentPanel, BorderLayout.CENTER ) ;

//    mainPanel.add( new JPanel(), BorderLayout.NORTH) ; // dummy
    mainPanel.add( center, BorderLayout.CENTER ) ;
    mainPanel.add( refPanel, BorderLayout.EAST ) ;

    return mainPanel ;
  }

  /** generates the FramePanel for the key/language data */
  private TFramePanel makeLangFrame()
  {
    TFramePanel back = new TFramePanel( "language" ) ;

    JLabel label1 = new JLabel( "key" ) ;
    JTextField keyField = new JTextField( 10 ) ;

    JLabel label2 = new JLabel( "active language" ) ;
    JComboBox languageCombo = new JComboBox() ;

    JLabel label3 = new JLabel( "translation" ) ;
    JTextArea translationArea = new JTextArea( 3, 1) ;
    JScrollPane scroller = new JScrollPane( translationArea,
                                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) ;

    JPanel content = back.getContentPanel() ;
    SpringLayout layout = new SpringLayout() ;
    content.setLayout( layout ) ;

    content.add( label1 ) ;
    content.add( keyField ) ;
    content.add( label2 ) ;
    content.add( languageCombo ) ;
    content.add( label3 ) ;
    content.add( scroller ) ;

    // key
    layout.putConstraint( SpringLayout.WEST, label1,
                          5,
                          SpringLayout.WEST, content ) ;
    layout.putConstraint( SpringLayout.NORTH, label1,
                          5,
                          SpringLayout.NORTH, content ) ;

    layout.putConstraint( SpringLayout.WEST, keyField,
                          5,
                          SpringLayout.EAST, label1 ) ;
    layout.putConstraint( SpringLayout.NORTH, keyField,
                          3,
                          SpringLayout.NORTH, content ) ;

    // language
    layout.putConstraint( SpringLayout.WEST, label2,
                          5,
                          SpringLayout.EAST, keyField ) ;
    layout.putConstraint( SpringLayout.NORTH, label2,
                          0,
                          SpringLayout.NORTH, label1 ) ;

    layout.putConstraint( SpringLayout.WEST, languageCombo,
                          5,
                          SpringLayout.EAST, label2 ) ;
    layout.putConstraint( SpringLayout.NORTH, languageCombo,
                          0,
                          SpringLayout.NORTH, keyField ) ;

    // translation entry
    layout.putConstraint( SpringLayout.WEST, label3,
                          0,
                          SpringLayout.WEST, label1 ) ;
    layout.putConstraint( SpringLayout.NORTH, label3,
                          15,
                          SpringLayout.SOUTH, label1 ) ;

    layout.putConstraint( SpringLayout.WEST, scroller,
                          5,
                          SpringLayout.EAST, label3 ) ;
    layout.putConstraint( SpringLayout.NORTH, scroller,
                          12,
                          SpringLayout.SOUTH, label1 ) ;

// panel edges ------------------------------------------------------
    layout.putConstraint( SpringLayout.EAST, content,
                          5,
                          SpringLayout.EAST, scroller ) ;
    layout.putConstraint( SpringLayout.SOUTH, content,
                          5,
                          SpringLayout.SOUTH, scroller ) ;

    return back ;
  }

  private TFramePanel makeRefFrame()
  {
    TFramePanel back = new TFramePanel( "found references" ) ;
    JPanel content = back.getContentPanel() ;
    content.setLayout( new BorderLayout() ) ;

    JScrollPane scroller = new JScrollPane( new JTable() ) ;
    content.add( scroller, BorderLayout.CENTER ) ;

    return back ;
  }

  private TFramePanel makeCommentFrame()
  {
    TFramePanel back = new TFramePanel( "comments" ) ;
    JPanel content = back.getContentPanel() ;
    content.setLayout( new BorderLayout(5, 5) );

    JScrollPane scroller = new JScrollPane( new JTextArea( 3, 1 ) ) ;
    content.add( scroller, BorderLayout.CENTER) ;

    return back ;
  }

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  // update all fields with data from currentEntry
  public void repaintData()
  {
    if ( currentEntry != null )
    {
      TProjectData current = TGlobal.projects.getCurrentData() ;
    }
    else // no entry data available - clear all fields
    {
    }
    GUIGlobals.oPanel.updateEntryComment( "" ) ;
  }

  public void setData( TMultiLanguageEntry entry )
  {
    currentEntry = entry ;
    repaintData() ;
  }

  /** put the selected data from translation-set into the TLangTextFields */
  private void fetchData()
  {
    // activeEntry is set by valueChanged()
    currentEntry = ( TMultiLanguageEntry )
        TGlobal.projects.getCurrentData().getValueAt( activeEntry, 0 ) ; // column attribut has no effect
    repaintData() ;
  }

  // --------------------------------------------------------------------------
  // ListSelectionListener - JTable
  public void valueChanged( ListSelectionEvent e )
  {

    ListSelectionModel lsm = ( ListSelectionModel ) e.getSource() ;
    activeEntry = lsm.getMinSelectionIndex() ;

    //Ignore extra messages.
    if ( e.getValueIsAdjusting() )
    {
      return ;
    }
    if ( lsm.isSelectionEmpty() )
    {
      //no rows are selected
    }
    else
    {
      fetchData() ;
    }
  }

}
