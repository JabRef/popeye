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
// function : a thread which can execute jobs
//
// todo     :
//
// modified :

package net.sf.quercus.task ;

// A Service Thread

public class TServiceThread extends Thread
{
  private boolean running ;

  private Object mutex = new Object() ;

  private JobQueue jobs ;

  private TServiceThreadManager manager ;


  public TServiceThread(JobQueue jobQueue, TServiceThreadManager thrManager)
  {
    manager = thrManager ;

    jobs = jobQueue ;
    start() ;
  }

  public void run()
  {
    running = true ; // Endlosschleife

    while (running)
    {
      JobContainer job = null ;

      // diesen Block komplett locken, damit zwischen get & if keine Unterbrechung
      // erfolgen kann
      synchronized (jobs)
      {
        job = jobs.getJob() ;

        // Kein Job vorhanden => schlafen legen
        if (job == null)
        {
          manager.sleep();
          try
          {
            jobs.wait() ;
          }
          catch (InterruptedException ie)
          {
            running = false ;
          }
        }
      }

      // diese Abfrage ausserhalb von synchronized
      if ((job != null) && (running))
      {
        manager.busy();
        job.runJob();
      }
    }
  }


  /** Thread "sauber" beenden */
  public void close()
  {
    running = false ;
    wakeUp() ;
  }


  /** schlafenden Thread aufwecken */
  public void wakeUp()
  {
    synchronized (mutex)
    {
      mutex.notifyAll() ;
    }
  }


  // --------------------------------------------------------------------------

}
