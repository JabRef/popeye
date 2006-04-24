package net.sf.langproper.engine.project.php ;

import java.io.* ;

import net.sf.langproper.engine.project.* ;

public class PhPBundleFileList extends TProjectFileList
{

  /**
   *
   * @param file File
   * @return boolean
   * @todo Implement this net.sf.langproper.engine.project.TProjectFileList
   *   method
   */
  public boolean collect( File file )
  {
    return false ;
  }

  /**
   * Generates an instance of project file with translations.
   *
   * @param file File
   * @return TLanguageFile
   * @todo Implement this net.sf.langproper.engine.project.TProjectFileList
   *   method
   */
  public TLanguageFile createFileHandle( File file )
  {
    System.out.println( "create handle" ) ;
    return new PhPBundleFile( file ) ;
  }

  /**
   *
   * @param isoLang String
   * @param isoCountry String
   * @param isoVariant String
   * @param path String
   * @return TLanguageFile
   * @todo Implement this net.sf.langproper.engine.project.TProjectFileList
   *   method
   */
  public TLanguageFile generateLanguageNameForIso( String isoLang,
      String isoCountry, String isoVariant, String path )
  {
    PhPBundleFile dummy = (PhPBundleFile) this.get(0) ;

    return new PhPBundleFile( dummy, isoLang, isoCountry, isoVariant, path ) ;
  }
}
