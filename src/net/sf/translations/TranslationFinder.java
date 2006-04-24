package net.sf.translations ;

import java.io.* ;
import java.net.* ;

public class TranslationFinder
{

  public static void extract(String line)
  {
     if ( line.indexOf("textarea") > -1)
     {
          // closing >
         int i1 = line.indexOf(">") +1 ;
         if (i1 > 0)  // index must be > 0
         {
           int i2 = line.indexOf("textarea", i1) ;
           if (i2 > i1)
           {
             String tr = line.substring(i1, i2-2) ;
             System.out.println(tr );
           }
         }
     }
  }

  public static void translate()
  {
    try
    {
//      URL url = new URL("http://www.systranbox.com/systran/box?systran_text=no+input&systran_lp=en_de") ;
      URL url = new URL("http://www.systranbox.com") ;
      try
      {
        InputStream is = url.openStream() ;

        BufferedReader in = new BufferedReader( new InputStreamReader( is )) ;

        String line = new String() ;
        while (( line = in.readLine() ) != null)
        {
                extract(line) ;
        }
      }
      catch (IOException e) {}
    }
    catch (MalformedURLException me ) {}
  }




  public static void main(String args[])
  {
    translate() ;
  }
}
