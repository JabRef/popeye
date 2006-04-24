package net.sf.langproper.gui.options ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.border.* ;
import net.sf.quercus.helpsystem.TInfoText ;
import net.sf.langproper.gui.* ;
import net.sf.langproper.gui.options.fonts.* ;
import javax.swing.event.* ;
import net.sf.langproper.TGlobal;

public class FontPropsPanel extends TAbstractOptionPanel implements
    ActionListener, ListSelectionListener
{

  private JTextField fontField = new JTextField() ;
  private JButton fontButton = new JButton( "use the selected font, instead" ) ;

  private JTextArea sampleArea = new JTextArea( 3, 20 ) ;
  private FontLanguagePropsPanel fontProps = new FontLanguagePropsPanel() ;
  private JList fonts ;

  private boolean changed = false ;

  public FontPropsPanel()
  {
    super( "Font", "basefont" ) ;
    this.setLayout( new BorderLayout() ) ;

    this.add( getFontPanel(), BorderLayout.CENTER ) ;
  }

  private JPanel getFontPanel()
  {
    JPanel back = new JPanel() ;
    back.setBorder( new TitledBorder( "font settings" ) ) ;

    SpringLayout layout = new SpringLayout() ;
    back.setLayout( layout ) ;

    // short help text
    JTextArea infoText = new JTextArea() ;
    infoText.setRows( 2 ) ;
    infoText.setLineWrap( false ) ;
    infoText.setEditable( false ) ;
    infoText.setFocusable( false ) ;
    infoText.setBackground( back.getBackground() ) ;
    infoText.append( TInfoText.runtime.getText( "options", "font" ) ) ;

    JLabel label = new JLabel( "Your current font is" ) ;
    JLabel previewLabel = new JLabel( "preview" ) ;

    fontField.setEditable( false ) ;
    fontField.setFocusable( false ) ;
    fontButton.addActionListener( this ) ;
    fontButton.setEnabled(false);

    // box with fonts
    fonts = new JList( new FontList() ) ;
    fonts.addListSelectionListener( this ) ;
    fonts.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION ) ;
    JScrollPane scroller = new JScrollPane( fonts ) ;

    // sample box
    sampleArea.setText( "Sample text!" ) ;
    JScrollPane sampleScroller = new JScrollPane( sampleArea ) ;

    back.add( infoText ) ;
    back.add( label ) ;
    back.add( fontField ) ;
    back.add( fontButton ) ;
    back.add( scroller ) ;
    back.add( previewLabel ) ;
    back.add( sampleScroller ) ;
    back.add( fontProps ) ;

    // short info (help) text -------------
    layout.putConstraint( SpringLayout.WEST, infoText,
                          5,
                          SpringLayout.WEST, back ) ;
    layout.putConstraint( SpringLayout.NORTH, infoText,
                          10,
                          SpringLayout.NORTH, back ) ;

    // label -------------
    layout.putConstraint( SpringLayout.WEST, label,
                          0,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, label,
                          5,
                          SpringLayout.SOUTH, infoText ) ;

    // textfield -------------
    layout.putConstraint( SpringLayout.WEST, fontField,
                          5,
                          SpringLayout.EAST, label ) ;
    layout.putConstraint( SpringLayout.NORTH, fontField,
                          4,
                          SpringLayout.SOUTH, infoText ) ;

    // button -------------
    layout.putConstraint( SpringLayout.WEST, fontButton,
                          10,
                          SpringLayout.EAST, fontField ) ;
    layout.putConstraint( SpringLayout.NORTH, fontButton,
                          1,
                          SpringLayout.SOUTH, infoText ) ;

    // Listbox with fonts -------------
    layout.putConstraint( SpringLayout.WEST, scroller,
                          0,
                          SpringLayout.WEST, infoText ) ;
    layout.putConstraint( SpringLayout.NORTH, scroller,
                          12,
                          SpringLayout.SOUTH, label ) ;

    // sampleArea -------------

    layout.putConstraint( SpringLayout.WEST, previewLabel,
                          10,
                          SpringLayout.EAST, scroller ) ;
    layout.putConstraint( SpringLayout.NORTH, previewLabel,
                          12,
                          SpringLayout.SOUTH, label ) ;

    layout.putConstraint( SpringLayout.WEST, sampleScroller,
                          5,
                          SpringLayout.WEST, previewLabel ) ;
    layout.putConstraint( SpringLayout.NORTH, sampleScroller,
                          2,
                          SpringLayout.SOUTH, previewLabel ) ;

    // some Font properties -------------
    layout.putConstraint( SpringLayout.WEST, fontProps,
                          0,
                          SpringLayout.WEST, sampleScroller ) ;
    layout.putConstraint( SpringLayout.NORTH, fontProps,
                          5,
                          SpringLayout.SOUTH, sampleScroller ) ;

    // panel edges -----------
    layout.putConstraint( SpringLayout.EAST, back,
                          5,
                          SpringLayout.EAST, fontButton ) ;
    layout.putConstraint( SpringLayout.SOUTH, back,
                          5,
                          SpringLayout.SOUTH, scroller ) ;

    return back ;
  }

  public String getPanelBorderTitle()
  {
    return "" ;
  }

  public boolean hasChanged()
  {
    return changed ;
  }

  public void loadConfig()
  {
    changed = false ;

    fontField.setText( GUIGlobals.defaultLanguageFontName ) ;
  }


  // --------------------------------------------------------------------------
  public boolean applyChanges()
  {
    boolean back = super.applyChanges() ;

    changed = false ;
    String str = fontField.getText() ;
    if (str != null)
    {
      if (str.length() > 0)
      {
        TGlobal.config.setDirectString( "Font", str);
      }
    }

    return back ;
  }


  // ------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    // extended font selection
//    FontChooser dialog = new FontChooser( null ) ;
//    dialog.show();

    // apply changes
    Object o = fonts.getSelectedValue() ;
    if ( o != null )
    {
      FontListItem item = ( FontListItem ) o ;
      changed = true ;
      fontField.setText( item.getName() );
      this.setNeedRestart();
    }
  }

  // Listbox clicked
  public void valueChanged( ListSelectionEvent e )
  {
    if ( e.getValueIsAdjusting() == false )
    {
      fontButton.setEnabled(true);

      Object o = fonts.getSelectedValue() ;
      if ( o != null )
      {
        FontListItem item = ( FontListItem ) o ;
        //System.out.println( item ) ;

        sampleArea.setFont( item.getFont() ) ;
        fontProps.update( item );
      }
    }
  }
}
