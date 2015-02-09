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

package javax.safetycritical;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RtProcess;
import javax.scj.util.Const;
import javax.scj.util.Priorities;

import vm.ProcessLogic;
import vm.RealtimeClock;

/**
 * Defines the VM process context for an executing Java program.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - implementation issue: infrastructure class; not part of the SCJ specification.
 */
final class ScjProcess extends javax.realtime.RtProcess {
	protected interface State extends javax.realtime.RtProcess.State {
	}
	/**
	 * The constructor initializes a new VM process object
	 * 
	 * @param target
	 *            is the handler containing the handleAsyncEvent method to be executed
	 * @param stack
	 *            is the run time stack
	 */
	ScjProcess(ManagedSchedulable ms, int[] stack) {
		this.next = new AbsoluteTime(rtClock);
		this.target = ms;
		this.state = State.NEW;
		this.process = new vm.Process(new ProcessLogic() {
			public void run() {
				try {
					getTarget().getManagedMemory().enter(getTarget());
				} catch (Exception e) {
					devices.Console.println("ScjProcess: exception -> " + e);
				} finally {
					if (getTarget() instanceof PeriodicEventHandler) {
						next.add(period, next); // next = next + period
					}
					state = State.HANDLED;
				}
			}

			public void catchError(Throwable t) {
				devices.Console.println("ScjProcess: exception -> " + t);
			}

		}, stack);

		this.process.initialize();

		rtClock.getTime(this.next);

		if (getTarget() instanceof PeriodicEventHandler) {
			this.start = ((PeriodicParameters) getTarget().getReleaseParameter()).getStart();
			this.period = ((PeriodicParameters) getTarget().getReleaseParameter()).getPeriod();
			next.add(start, next); // next = next + start
		} else if (getTarget() instanceof OneShotEventHandler) {
			if (((OneShotEventHandler) getTarget()).releaseTime instanceof RelativeTime) {
				RelativeTime releaseTime = (RelativeTime) ((OneShotEventHandler) getTarget()).releaseTime;
				next.add(releaseTime, next); // next = next + releaseTime
			} else {
				AbsoluteTime releaseTime = (AbsoluteTime) ((OneShotEventHandler) getTarget()).releaseTime;
				int compare = releaseTime.compareTo(Clock.getRealtimeClock().getTime(new AbsoluteTime(rtClock)));
				if (compare < 0)
					next.add(new RelativeTime(), next);
				else
					next.set(releaseTime);
			}
		}

		this.getTarget().setScjProcess(this);
	}

	/**
	 * Creates and returns the singleton idle process. If idle process is already created, no new process is created.
	 * 
	 * @return Returns the singleton idle process.
	 */
	static RtProcess createIdleProcess() {
		if (idleProcess == null) {
			PeriodicEventHandler peh = new PeriodicEventHandler(new PriorityParameters(Priorities.MIN_PRIORITY), new PeriodicParameters(
					new RelativeTime(Clock.getRealtimeClock()),// start
					// (0,0)
					Const.INFINITE_TIME), // period
					new StorageParameters(Const.PRIVATE_MEM_SIZE_DEFAULT, new long[] { Const.IDLE_PROCESS_STACK_SIZE },
							Const.PRIVATE_MEM_SIZE_DEFAULT, Const.IMMORTAL_MEM_SIZE_DEFAULT, Const.MISSION_MEM_SIZE_DEFAULT), "idle") {
				public void handleAsyncEvent() {
					devices.Console.println("Idle");
					yield();
				}

				private void yield() {
					while (true) {
						RealtimeClock.awaitNextTick();
					}
				}
			};
			
			ScjProcess process = new ScjProcess(peh, new int[Const.IDLE_PROCESS_STACK_SIZE]);
			process.setIndex(-1);
			idleProcess = process;
		}
		
		return idleProcess;
	}

	protected void start() {
		process.initialize();
	}

	protected ManagedSchedulable getTarget() {
		return target;
	}

	protected int getIndex() {
		return this.index;
	}

	protected void setIndex(int index) {
		this.index = index;
	}

	protected int getState() {
		return this.state;
	}

	protected void setState(int state) {
		this.state = state;
	}

	protected vm.Process getProcess() {
		return this.process;
	}

	protected AbsoluteTime getNext() {
		return this.next;
	}

	protected String print() {
		return ("name: " + this.target.getName() + " 	index: " + index);
	}
}
