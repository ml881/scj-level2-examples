/**************************************************************************
 * File name  : ManagedSchedulableSet.java
 * 
 * This file is part a SCJ Level 0 and Level 1 implementation, 
 * based on SCJ Draft, Version 0.94 25 June 2013.
 *
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as  
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This SCJ Level 0 and Level 1 implementation is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the  
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this SCJ Level 0 and Level 1 implementation.  
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2012 
 * @authors  Anders P. Ravn, Aalborg University, DK
 *           Stephan E. Korsholm and Hans S&oslash;ndergaard, 
 *             VIA University College, DK
 *    
 * Description: 
 * 
 * Revision history:
 *   date   init  comment
 *
 *************************************************************************/

package javax.safetycritical;

import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.scj.util.Const;

/**
 * This collection class of handlers is created in mission memory and used by the mission.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - implementation issue: infrastructure class; not part of the SCJ specification.
 */
@SCJAllowed(Level.INFRASTRUCTURE)
class ManagedSchedulableSet {
	ManagedSchedulable[] MSOs = new ManagedSchedulable[Const.DEFAULT_HANDLER_NUMBER];
	int noOfRegistered = 0;
	ScjProcess[] scjProcesses = new ScjProcess[Const.DEFAULT_HANDLER_NUMBER];

	/**
	 * Handler count for the mission. Only one mission at a time; no sub-mission
	 * 
	 * The handlerCount is incremented by addHandler, and is decremented by PriorityScheduler when handler is terminated.
	 * 
	 * Mission.runCleanup is waiting until handlerCount == 0
	 */
	int noOfActived = 0;

	ManagedSchedulableSet() {
	}

	/*
	 * @ behavior requires eh != null; ensures this.contains(eh);
	 * 
	 * @
	 */
	void addMS(ManagedSchedulable ms) {
		if (!contains(ms)) {
			MSOs[noOfRegistered] = ms;
			noOfRegistered++;
			noOfActived++;
		}
	}

	boolean contains(ManagedSchedulable ms) {
		for (int i = 0; i < noOfRegistered; i++) {
			if (MSOs[i] == ms)
				return true;
		}
		return false;
	}

	void terminateMSOs() // stop all managed schedule objects;
	// called in
	// CyclicExecutive.runCleanup
	{
		for (int i = noOfRegistered; i > 0; i--) {
			MSOs[i - 1].cleanUp();
			MSOs[i - 1] = null;
			noOfActived--;
		}
	}

	void removeMSO(ManagedSchedulable ms) // called in
	// PriorityScheduler.move
	{

		for (int i = 0; i < noOfRegistered; i++) {
			if (MSOs[i] == ms) {
				MSOs[i].cleanUp();
				MSOs[i] = null;
				PriorityScheduler.instance().removeReleaseQueue(scjProcesses[i]);
				scjProcesses[i] = null;
				noOfActived--;
			}
		}
		
		if (noOfActived == 0) {
			Monitor lock = ms.getMission().getSequencer().getLock();
			lock.lockWithOutEnable();
			PriorityScheduler.instance().notifyForMS(lock);
			lock.unlockWithOutEnable();
		}
		
	}

	void removeAperiodicHandlers() // remove all aperiodic
	// handlers;
	// called in PriorityScheduler.move()
	{
		for (int i = 0; i < noOfRegistered; i++) {
			if (MSOs[i] instanceof AperiodicEventHandler) {
				MSOs[i].cleanUp();
				PriorityScheduler.instance().removeReleaseQueue(scjProcesses[i]);
				noOfActived--;
			}
			
			if (noOfActived == 0) {
				Monitor lock = MSOs[i].getMission().getSequencer().getLock();
				lock.lockWithOutEnable();
				PriorityScheduler.instance().notifyForMS(lock);
				lock.unlockWithOutEnable();
			}
		}
	}

	int indexOf(ManagedSchedulable ms) {
		for (int i = 0; i < noOfRegistered; i++) {
			if (MSOs[i] == ms)
				return i;
		}
		return -1;
	}

	public String toString() {
		return "Mission: " + noOfRegistered + " managed schedule objects.";
	}
}