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
// function : a task pool (queue)
//
// todo     : task dependencies, english javadoc
//
// modified :

package net.sf.quercus.task ;

import java.util.*;

// zentrale Schnittstelle zwischen Servicethread und den Konsumenten (erzeugen Arbeit)
public class JobQueue
{
  // --------------------------------------------------------------------------

  // Liste mit den registrierten Tasks
  private Vector tasks = new Vector() ;

  // high Priority Task, overrides other priority tasks
  private JobContainer priorJob = null ;

  // execution ready task
  private LinkedList jobQueue = new LinkedList() ;


  // --------------------------------------------------------------------------
  // registering of task ------------------------------------------------------
  // --------------------------------------------------------------------------

  /** liefert einen Task mit der Id <id> */
  public TStandardTask getTask(int id)
  {
    if (id > -1 && id < tasks.size())
    {
      return (TStandardTask) tasks.get(id) ;
    }

    return (TStandardTask) tasks.get(0) ;
  }

  /** Registriert einen Task und liefert dessen ID zurueck. Alle weiteren
   *  Referenzierungen auf diesen Task benutzen diese ID.
   */
  public int registerTask( TStandardTask task)
  {
    int back = -1 ;

    if (task != null)
    {
      back = tasks.size() ;

      tasks.add(task) ;
    }

    return back ;
  }

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------

  /** Fordert die Ausfuehrung eines Tasks durch einen ServiceThread an.
    * Sobald ein Thread alle anderen Aufgaben erfuellt hat wird er mit der
    * Abarbeitung beginnen. Es wird sichergestellt, das der Task zur
    * Ausfuehrung kommt.*/
  public synchronized int addJob(int taskID, Object jobData)
  {
     int back = -1 ;
     TStandardTask task = getTask(taskID) ;
     if ( (task != null) && (jobData != null) )
     {
       JobContainer job = new JobContainer(task, jobData) ;

       back = job.getJobID() ;

       jobQueue.addLast( job ) ; // hinten dran

       notifyAll() ;
     }

     return back ;
  }

  /** Fordert die Ausfuehrung eines Tasks mit sehr hoher Prioritaet durch einen
   * ServiceThread an. Ein (oder mehrere) noch nicht gestartete PriorityTasks
   * werden hierdurch ueberschrieben */
  public synchronized void addPriorityJob(int taskID, Object jobData)
  {
    TStandardTask task = getTask(taskID) ;
    if ( (task != null) && (jobData != null) )
    {
      JobContainer job = new JobContainer(task, jobData) ;

      priorJob = job ;

      notifyAll() ;
    }
  }

  /** returns the next open job */
  public synchronized JobContainer getJob()
  {
    JobContainer back = null ;
    if (priorJob != null)
    {
      back = priorJob ;
      priorJob = null ;
    }
    else if (jobQueue.size() > 0)  // in der Schlange sind noch Elemente
    {

      back = (JobContainer) jobQueue.removeFirst() ; //ersten Eintrag holen
    }

    return back ;
  }

  // --------------------------------------------------------------------------
}
