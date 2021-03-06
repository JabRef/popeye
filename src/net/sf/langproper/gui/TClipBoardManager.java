/*
  based on "Clipboard copy and paste" demo from http://www.javapractices.com/Topic82.cjp
*/

// created by : r.nagel 14.09.2004
//
// function : handle all clipboard action
//
// modified :
//
// !!!!!!!!!!!!!!!!!!!! NOT USED since version 0.23 !!!!!!!!!!!!!!!!!!!!!!!!!!
//

package net.sf.langproper.gui ;

import java.awt.datatransfer.*;
import java.awt.*;
import java.io.*;


public class TClipBoardManager implements ClipboardOwner
{
  public static TClipBoardManager clipBoard = new TClipBoardManager() ;

  /**
   * Empty implementation of the ClipboardOwner interface.
   */
  public void lostOwnership( Clipboard aClipboard, Transferable aContents )
  {
    //do nothing
  }

  /**
   * Place a String on the clipboard, and make this class the
   * owner of the Clipboard's contents.
   */
  public void setClipboardContents( String aString )
  {
    StringSelection stringSelection = new StringSelection( aString ) ;
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
    clipboard.setContents( stringSelection, this ) ;
  }

  /**
   * Get the String residing on the clipboard.
   *
   * @return any text found on the Clipboard; if none found, return an
   * empty String.
   */
  public String getClipboardContents()
  {
    String result = "" ;
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents( null ) ;
    boolean hasTransferableText = ( contents != null ) &&
        contents.isDataFlavorSupported( DataFlavor.stringFlavor ) ;
    if ( hasTransferableText )
    {
      try
      {
        result = ( String ) contents.getTransferData( DataFlavor.stringFlavor ) ;
      }
      catch ( UnsupportedFlavorException ex )
      {
        //highly unlikely since we are using a standard DataFlavor
        System.out.println( ex ) ;
      }
      catch ( IOException ex )
      {
        System.out.println( ex ) ;
      }
    }
    return result ;
  }
}
