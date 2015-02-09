/**************************************************************************
 * File name  : ScjProcess.java
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

import javax.safetycritical.ManagedSchedulable;

/**
 * Defines the VM process context for an executing Java program.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A
 *         HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A
 *         HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - implementation issue: infrastructure class; not part of the SCJ
 *             specification.
 */
public class RtProcess implements Comparable<RtProcess> {
	protected vm.Process process;
	protected ManagedSchedulable target;
	protected int state;
	protected AbsoluteTime next; // next activation time
	protected RelativeTime start;
	protected RelativeTime period;
	protected Object lockRequired = null;
	protected AbsoluteTime next_temp = null;
	protected boolean isNotified = false;
	protected int index = -9999; // The index of the ScjProcesses; used by
	// PriorityScheduler; -9999 is 'no index set'
	protected static Clock rtClock = Clock.getRealtimeClock();
	protected static AbsoluteTime now =  new AbsoluteTime(rtClock);

	/**
	 * Idle process is created and put in readyQueue, so that readyQueue will
	 * never be empty. Idle process has lowest priority. <br>
	 * 
	 * Idle process is a periodic handler with "infinite" period.
	 */

	protected static RtProcess idleProcess;

	protected interface State {
		public final static byte NEW = 0;
		public final static byte READY = 1;
		public final static byte EXECUTING = 2;
		public final static byte BLOCKED = 3;
		public final static byte SLEEPING = 4;
		public final static byte HANDLED = 5;
		public final static byte TERMINATED = 6;
	}

	/**
	 * Compares this process with the parameter process. The ordering of the
	 * processes are done after next release and priority.
	 */
	public int compareTo(RtProcess process) {
		rtClock.getTime(now);

		if(process.next.compareTo(now)<=0){
			if(this.next.compareTo(now)>0)
				return 1;
			else
				return (process.target.getPriorityParameter().getPriority() - this.target
					.getPriorityParameter().getPriority());
		}
		else{
			int result = this.next.compareTo(process.next);
			if (result == 0) {
				result = (process.target.getPriorityParameter().getPriority() - this.target
						.getPriorityParameter().getPriority());
			}
			return result;
		}
		
		
//		int thisCompareNow = this.next.compareTo(now);
//		int processCompareNow = process.next.compareTo(now);
//
//		if (thisCompareNow <= 0 && processCompareNow <= 0)
//			return (process.target.getPriorityParameter().getPriority() - this.target
//					.getPriorityParameter().getPriority());
//		else if (thisCompareNow > 0 && processCompareNow <= 0)
//			return 1;
//		else {
//			int result = this.next.compareTo(process.next);
//			if (result == 0) {
//				result = (process.target.getPriorityParameter().getPriority() - this.target
//						.getPriorityParameter().getPriority());
//			}
//			return result;
//		}
	}

	protected void start() {
		process.initialize();
	}

	protected String print() {
		return ("name: " + this.target.getName() + " 	index: " + index);
	}

}












