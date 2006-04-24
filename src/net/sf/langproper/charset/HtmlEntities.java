package net.sf.langproper.charset ;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import net.sf.langproper.* ;

public class HtmlEntities
{
  /** contains the encoding informations */
  private Vector liste = new Vector() ;

  /** language code file found and loaded */
  private boolean availStatus = false ;

  public HtmlEntities()
  {
  }

  public void load()
  {
    // generate stream from jar file resource
     URL res = TGlobal.class.getResource(TGlobal.RESOURCE_ROOT + "/" +TGlobal.HTML_ENC_FILENAME) ;
     InputStream inStream = null ;

     if (res != null)
     {
       JarURLConnection langRes = null ;
       try
       {
         langRes = ( JarURLConnection ) res.openConnection() ;
       }
       catch ( Exception e )
       {
         System.out.println( e ) ;
         return ;
       }
       try
       {
         inStream = langRes.getInputStream() ;
       }
       catch ( Exception e )
       {
         System.out.println( e ) ;
         return ;
       }
     }
     else
     {
       // inStream = this.getClass().getResourceAsStream(TGlobal.RESOURCE_ROOT + "/" +TGlobal.COUNTRY_FILENAME);
     }

     if (inStream == null)  // stream not empty
     {
       availStatus = false ;

       if (TGlobal.DEVEL)
       {

         try  // JBuilder IDE hack ;-)
         {
           inStream = new FileInputStream(
               "src/resource/" + TGlobal.HTML_ENC_FILENAME ) ;
         }
         catch ( Exception e2 )
         {
//          e2.printStackTrace();
         }
       }
       else return ;
     }

     String line ;
     try
     {
       BufferedReader input = new BufferedReader(  new InputStreamReader( inStream), 1000) ;

       while ( input.ready() )
       {
         line = input.readLine().trim() ;

         if ( (line.length() > 0) && (line.charAt(0) == '<'))
         {
           int id1 = line.indexOf("<!ENTITY") ;
           int id2 = line.indexOf("CDATA ") ;

           // data line found
           // now use static informations about the file structure!!!
           if ( (id1 > -1) && (id2 > -1))
           {
             int id3 = line.indexOf(";\"", id2+4) ;
             String enti = line.substring(9, id2).trim() ;
             String code = line.substring(id2 +5, id3).trim() ;

             liste.add( new EntContainer(Integer.parseInt(code.substring(3)),
                                         enti)) ;
           }
         }
       }
       availStatus = true ;
     }
     catch (Exception e)
     {
       availStatus = false ;

       System.out.println(e) ;
       e.printStackTrace();
     }
  }


  // -------------------------------------------------------------------------
  private class EntContainer
  {
    private String name ;
    private int code ;

    public EntContainer( int i, String n)
    {
      name = n ;
      code = i ;
    }
  }
}
