/**************************************************************************
 * File name  : PriorityFrame.java
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

package javax.realtime;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.OneShotEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

/**
 * This frame class holds a ready queue and a sleeping queue for the priority scheduler. <br>
 * The class is package protected because it is not part of the SCJ specification.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - implementation issue: infrastructure class; not part of the SCJ specification.
 */
@SCJAllowed(Level.INFRASTRUCTURE)
class PriorityFrame {
	// a priority queue ordered by ScjProcess.next activation time and priority;
	ReleaseQueue queue;
	PriorityQueue waitQueue;
	PriorityQueue lockQueue;

	PriorityFrame(int queueSize) {
		queue = new ReleaseQueue(queueSize);
		waitQueue = new PriorityQueue(queueSize);
		lockQueue = new PriorityQueue(queueSize);
	}

	void addProcess(RtProcess process) {
		// devices.Console.println("PrFrame.addProcess, index " +
		// process.index);
		if (process.target instanceof MissionSequencer) {
			process.state = RtProcess.State.READY;
			queue.insert(process);
		}

		else if (process.target instanceof PeriodicEventHandler) {
			PeriodicEventHandler pevh = (PeriodicEventHandler) process.target;
			RelativeTime start = ((PeriodicParameters) pevh.getReleaseParameter()).getStart();

			if (start.getMilliseconds() == 0 && start.getNanoseconds() == 0)
				process.state = RtProcess.State.READY;
			else
				process.state = RtProcess.State.SLEEPING;

			queue.insert(process);
		}

		else if (process.target instanceof AperiodicEventHandler) {
			process.state = RtProcess.State.BLOCKED;
		}

		else if (process.target instanceof OneShotEventHandler) {
			process.state = RtProcess.State.SLEEPING;
			queue.insert(process);
		}

		// if a thread is added, then it is made blocked and waiting for the
		// call to start.
		else if (process.target instanceof ManagedThread) {
			// devices.Console.println("---------------------------------- enter into addProcess method : the  thread is made blocked ---------------------------------- ");
			if (((ManagedThread) process.target).isAutoStart()) {
				process.state = RtProcess.State.READY;
				queue.insert(process);
			} else {
				process.state = RtProcess.State.BLOCKED;
			}
		}

		else {
			throw new IllegalArgumentException("PriorityFrame.addHandler: UPS: another schedulable objects??");
		}
	}

}
