/*
 Popeye - Java (Language) Properties File Editor
 */

package net.sf.langproper.gui.theme ;

import java.awt.* ;
import javax.swing.plaf.* ;

/**
 * Defines all themes which can allow the user to customize the Java Look and Feel.
 *
 * <P>This application uses only the cross-platform Java Look-and-Feel, and never
 * attempts to adapt to the native look-and-feel (Windows, Metal, Mac).
 *
 * @used.By {@link GeneralLookPreferencesEditor}, {@link StocksMonitorMainWindow}.
 * @author <a href="http://www.javapractices.com/">javapractices.com</a>
 */
public final class SimpleTheme
{

   public static DefaultTheme getTheme(String themeName)
   {
     // new DefaultTheme() ;
     if (themeName != null)
     {
       if (themeName.hashCode() == "Moon".hashCode())
       {
         return new Moon();
       }
     }

     return new DefaultTheme();
   }

// ---------------------------------------------------------------------------
  /*
   * All items below are private nested classes which define the various
   * themes.
   */
// ---------------------------------------------------------------------------

  private static class Moon extends DefaultTheme
  {
    public String getName()
    {
      return "Moon" ;
    }

    protected ColorUIResource getPrimary1()
    {
      return fPrimary1 ;
    }

    protected ColorUIResource getPrimary2()
    {
      return fPrimary2 ;
    }

    protected ColorUIResource getPrimary3()
    {
      return fPrimary3 ;
    }

//    private final ColorUIResource fPrimary1 = new ColorUIResource(102, 153, 153);
//    private final ColorUIResource fPrimary1 = new ColorUIResource(0x98ACD4);
    private final ColorUIResource fPrimary1 = new ColorUIResource( 0x0D3079 ) ;

//    private final ColorUIResource fPrimary2 = new ColorUIResource(128, 192, 192); // 0x80C0C0
    private final ColorUIResource fPrimary2 = new ColorUIResource( 0x1A469F ) ;

//    private final ColorUIResource fPrimary3 = new ColorUIResource(0xC41C30);
    private final ColorUIResource fPrimary3 = new ColorUIResource( 0xB0C4ED ) ; // 0x9FEBEB
//    private final ColorUIResource fPrimary3 = new ColorUIResource(159, 235, 235); // 0x9FEBEB

    //fonts are larger than defaults
    public FontUIResource getControlTextFont()
    {
      return fControlFont ;
    }

    public FontUIResource getSystemTextFont()
    {
      return fSystemFont ;
    }

    public FontUIResource getUserTextFont()
    {
      return fUserFont ;
    }

    public FontUIResource getMenuTextFont()
    {
      return fControlFont ;
    }

    public FontUIResource getWindowTitleFont()
    {
      return fWindowTitleFont ;
    }

    public FontUIResource getSubTextFont()
    {
      return fSmallFont ;
    }

    private final FontUIResource fControlFont = new FontUIResource( preferredFont,
        Font.PLAIN, 12 ) ;
    private final FontUIResource fSystemFont = new FontUIResource( preferredFont,
        Font.PLAIN, 12 ) ;
    private final FontUIResource fWindowTitleFont = new FontUIResource( preferredFont,
        Font.PLAIN, 12 ) ;
    private final FontUIResource fUserFont = new FontUIResource( preferredFont,
        Font.PLAIN, 12 ) ;
    private final FontUIResource fSmallFont = new FontUIResource( preferredFont,
        Font.PLAIN, 10 ) ;
  }

// ---------------------------------------------------------------------------
}
