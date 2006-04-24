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


// created by : r.nagel 15.09.2005
//
// function : handling of service threads
//
// todo     :
//
// modified :

package net.sf.quercus.task ;

import java.util.Vector;
import javax.swing.SwingUtilities;
import net.sf.quercus.task.service.*;

public class TServiceThreadManager implements Runnable
{
  /** list of all activity listeners */
  private Vector consumerQuene = new Vector() ;

  private int busyCounter = 0 ;

  private Object mutex = new Object();


  public TServiceThreadManager(JobQueue jobQueue)
  {
    // create a thread
    // !! no task dependendies implemented, please start only 1 thread !!
   TServiceThread thr1 = new TServiceThread( jobQueue, this ) ;
  }

  // thread activity managment ------------------------------------------------

  /** enter busy state */
  public void busy()
  {
    synchronized(mutex)
    {
      if (busyCounter < 0)
        busyCounter = 1 ;
      else
        busyCounter++ ;
    }
    updateServices() ;
  }

  /** enter sleep state */
  public void sleep()
  {
    synchronized(mutex)
    {
      if (busyCounter > 0)
        busyCounter-- ;
    }
    updateServices() ;
  }


  /** register a listener for thread activity events */
  public void register(ThreadStatusService consumer)
  {
    consumerQuene.add(consumer) ;
  }

  /** unregister a listener for thread activity events */
  public void unregister(ThreadStatusService consumer)
  {
    consumerQuene.remove(consumer) ;
  }

  /** signalize thread (in)activity */
  public void updateServices()
  {
    SwingUtilities.invokeLater( this ) ;
  }

  /** update the registered swing listeners */
  public final void run()
  {
    for (int t = 0, len = consumerQuene.size() ; t < len ; t++)
    {
      ThreadStatusService service = (ThreadStatusService) consumerQuene.get(t) ;
      synchronized(mutex)
      {
        if (busyCounter < 1)
        {
          service.setThreadSleeping();
        }
        else
        {
          service.setThreadBusy();
        }
      }
    }
  }


}
