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
// function : a job
//
// todo     :
//
// modified :

package net.sf.quercus.task ;

import java.util.Vector ;

/**
 * Standardklasse fuer alle Task. Die meisten Task sollen rechenintensive
*  Hintergrundarbeiten ausfuehren, deren Ergebnis in der GUI angezeigt
*  wird. Allerdings ist die (Swing-)GUI nicht threadsicher. Deshalb
*  stellt diese Basiklasse Funktionen bereit, um die angesprochenen
*  Hintergrundarbeiten durchzufuehren und die Ergebnisse anschliessend an
*  mehrere GUI Elemente (consumer) zu verteilen.
 */
public abstract class TStandardTask implements Runnable
{
  /** enthaelt alle Konsumenten */
  protected Vector consumerQuene = new Vector() ;

  private Object resultData ;

  /** Ruft alle registrierten consumer im Kontext des ServiceThreads auf.
   *  Da es moeglich ist, das mehrere Task eines Types nebenlaeufig ausgefuehrt
   *  werden, sollten die berechneten Daten nicht durch eine "globale"
   *  Objektvariable verteilt werden. Deshalb wird diese Methode bereit gestellt  */
  public final void setConsumerData(Object data)
  {
    resultData = data ;

    for (int t = 0, len = consumerQuene.size() ; t < len ; t++)
    {
      consumerCall(consumerQuene.get(t), data) ;
    }
  }

  /** liefert die aktuell zur Verfuegung stehenden Daten */
  public Object getResult()
  {
    return resultData ;
  }

  // --------------------------------------------------------------------------
  /** registriert einen Konsumenten (consumer) */
  public void register(Object consumer)
  {
    consumerQuene.add(consumer) ;
  }

  /** loescht einen consumer aus der Liste */
  public void unregister(Object consumer)
  {
    consumerQuene.remove(consumer) ;
  }

  public boolean hasServiceConsumer()
  {
    if (consumerQuene.size() > 0)
      return true ;

    return false ;
  }

  // --------------------------------------------------------------------------

  /** alle registrierten consumer werden im Kontext des SwingThreads durchlaufen */
  public final void run()
  {
    for (int t = 0, len = consumerQuene.size() ; t < len ; t++)
    {
      consumerUpdate(consumerQuene.get(t)) ;
    }
  }

  // --------------------------------------------------------------------------

  // --------------------------------------------------------------------------

  /** uebergeben wird ein consumer, den Typ dieses Objektes legt die Implementierung
   *  des Task fest bzw. diese kennt den Typ.
   *  performGUI ist dafuer gedacht alle Arbeiten fuer die Grafische Anzeige im
   *  SwingUtilities.invokeLater zu uebernehmen
   */
  public abstract void consumerUpdate(Object consumer) ;

  /** wird vom ServiceThread zur Vorbereitung und Datenuebergabe aufgerufen
  *   (z.B. Skalierung eines Bildes) */
  public abstract void consumerCall(Object consumer, Object result) ;

  /** diese Methode wird vom ServiceThread mit den aktuellen Jobdaten aufgerufen */
  public abstract void work( Object jobData ) ;

}
