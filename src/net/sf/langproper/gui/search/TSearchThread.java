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
*/

// created by : r.nagel 30.06.2005
//
// function : search entries (used by TSearchToolBar)
//
// todo     :
//
// modified :

package net.sf.langproper.gui.search;

import net.sf.langproper.*;
import net.sf.langproper.engine.TMultiLanguageEntry;
import net.sf.langproper.gui.*;
import javax.swing.*;
import net.sf.langproper.engine.project.*;

public class TSearchThread extends Thread
{
  private Object mutex = new Object();

  private String data = null ;

  private boolean running = true ;

  private int index ;  // current index
  private int len ;    // size of data
  private int rounds = 0 ;  // number of search rounds (start -> end -> start -> end...)

  private int foundCounter = 0 ; // number of found entries

  private int offset ;  // found pattern at index on offset....

  private boolean search = false ;

  private Runnable returnRoutine ;
  private Runnable notFoundMessanger ;

  public TSearchThread()
  {
    returnRoutine = new Runnable()
    {
      public void run()
      {
        GUIGlobals.oPanel.selectAndMark( index, offset, data.length()+offset ) ;
      }
    } ;

    notFoundMessanger = new Runnable()
    {
      public void run()
      {
        JOptionPane.showMessageDialog(null,
            "no entry found.",
            "search warning",
            JOptionPane.WARNING_MESSAGE);
      }
    } ;
  }

  public void run()
  {
    TProjectData current = TGlobal.projects.getCurrentData() ;
    while (running)
    {
      if (search)
      {
        TMultiLanguageEntry currentEntry = ( TMultiLanguageEntry ) current.getValueAt(index, 0 ) ;
        if (currentEntry != null)
        {
          String key = currentEntry.getKey() ;
          if (key != null)
          {
            String org = key.toLowerCase() ;
            offset = org.indexOf( data ) ;
            if ( offset > -1)
            {
              search = false ;
              foundCounter++ ;
//              System.out.println( "found at <" +index +">" +key) ;
              mark() ;
            }
          }
        }

        if (search)  // if found, do not set the next index
        {
          nextIndex() ;
        }
      }
      else  // nothing do to -> sleep
      {
        synchronized ( mutex )
        {
          try
          {
            mutex.wait() ;
            current = TGlobal.projects.getCurrentData() ; // refresh
          }
          catch ( InterruptedException e )
          {
            running = false ;
          }
        }
      }
    }
  }

  // select the entry
  private void mark()
  {
      try
      {
        SwingUtilities.invokeAndWait( returnRoutine ) ;
      }
      catch (InterruptedException e1)
      {
        running = false ;
      }
      catch (Exception e2)
      {

      }
  }

  // print a message dialog
  private void notFound()
  {
      try
      {
        SwingUtilities.invokeAndWait( notFoundMessanger ) ;
      }
      catch (InterruptedException e1)
      {
        running = false ;
      }
      catch (Exception e2)
      {
      }
  }

  // set the startpoint of search
  private void init()
  {
    index = 0 ;
    rounds = 0 ;
    foundCounter = 0 ;
    len = TGlobal.projects.getCurrentData().getRowCount() ;
  }

  // calculate next index
  private void nextIndex()
  {
    index++ ;
    if ( index > len )  // restart
    {
      // prevent a loop, if nothing could be found
      if ((rounds < 2) || (foundCounter > 0))
      {
        index = 0 ;
        rounds++ ;
//      System.out.println( "restart" ) ;
      }
      else // nothing found, end of search
      {
        search = false ;
        // message to user
        notFound() ;
      }
    }
  }

  // wakeup the thread
  private void wakeUp()
  {
    search = true ;
    synchronized(mutex)
    {
      mutex.notify();
    }
  }

  // start search from start of data
  public void setPattern( String pattern )
  {
    if (pattern == null)
      return ;

    data = pattern.toLowerCase() ;
    init() ;
    wakeUp() ;
  }


  // start new search, if the thread is inactive
  public void searchNext()
  {
    if (!search)
    {
      nextIndex() ;
      wakeUp() ;
    }
  }
}
