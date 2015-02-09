/**************************************************************************
 * File name  : PriorityScheduler.java
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
 *************************************************************************/
package javax.realtime;

import icecaptools.IcecapCompileMe;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.ManagedSchedulable;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.OneShotEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.scj.util.Const;
import javax.scj.util.Priorities;

/**
 * This class represents the priority-based scheduler for Level 1 and 2. <br>
 * The only access to the priority scheduler is for obtaining the minimum and maximum priorities.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 */
@SCJAllowed(Level.LEVEL_1)
public class PriorityScheduler extends Scheduler {
	protected PriorityFrame pFrame;
	PrioritySchedulerImpl prioritySchedulerImpl;
	Clock rtClock;
	RelativeTime timeGrain;
	protected static PriorityScheduler scheduler;
	protected Mission currMission;
	protected RtProcess outerMostSeqProcess = null;

	protected static PriorityScheduler instance() {
		if (scheduler == null) {
			scheduler = new PriorityScheduler();
		}
		return scheduler;
	}

	protected PriorityScheduler() {
		int[] schedulerStack = new int[Const.PRIORITY_SCHEDULER_STACK_SIZE];
		pFrame = new PriorityFrame(Const.DEFAULT_PRIORITY_QUEUE_SIZE);
		this.prioritySchedulerImpl = new PrioritySchedulerImpl(this);

		vm.ClockInterruptHandler.initialize(this.prioritySchedulerImpl, schedulerStack);

		this.rtClock = Clock.getRealtimeClock();
		this.timeGrain = new RelativeTime(0, 0, this.rtClock);
		rtClock.getResolution(this.timeGrain);
		scheduler = this;
	}

	private vm.Process mainProcess;

	private void processStart() {
		vm.ClockInterruptHandler clockHandler = vm.ClockInterruptHandler.instance;
		mainProcess = new vm.Process(null, null);

		clockHandler.register();
		clockHandler.enable();
		clockHandler.startClockHandler(mainProcess);
		clockHandler.yield();
	}

	@IcecapCompileMe
	protected void stop(vm.Process current) {
		current.transferTo(mainProcess);
	}

	// ----27 Dec 2013 end -----------------------------------

	protected void start() {
		// put idle process in readyQueue
		current = pFrame.queue.extractMin();

		processStart();
	}

	@IcecapCompileMe
	protected RtProcess move() {
		currMission = current.target.getMission();

		if (current == RtProcess.idleProcess) {
			// add a time grain to the idle process, so it is pushed back in the
			// queue
			rtClock.getTime(current.next);
			current.next.add(timeGrain, current.next);
			pFrame.queue.insert(current);
		}

		else if (current.target instanceof MissionSequencer<?>) {
			if (current.state == RtProcess.State.HANDLED) {
				// missionSequencer terminates
				if (current.index == -2) {
					current.target.cleanUp();
				} else {
					removeSO(currMission, current.target);
				}
				current.state = RtProcess.State.TERMINATED;
			} else {
				// handler was preempted
				current.state = RtProcess.State.READY;

				// add a time grain to the mission sequencer process, so it is
				// pushed back in the queue
				rtClock.getTime(current.next);
				current.next.add(timeGrain, current.next);
				pFrame.queue.insert(current);
			}

		}

		// periodic handlers
		else if (current.target instanceof PeriodicEventHandler) {
			if (current.state == RtProcess.State.HANDLED) {
				// finished executing handleAsyncEvent

				if (currMission.terminationPending()) {
					removeSO(currMission, current.target);
					current.state = RtProcess.State.TERMINATED;
				} else {
					// run it again
					current.state = RtProcess.State.SLEEPING;
					current.start();
					pFrame.queue.insert(current);
				}
			} else {
				// handler was preempted
				current.state = RtProcess.State.READY;
				pFrame.queue.insert(current);
			}
		}

		else if (current.target instanceof AperiodicEventHandler) {
			if (current.state == RtProcess.State.HANDLED) {
				// AperiodicEventHandler finished handling
				if (currMission.terminationPending()) {
					removeSO(currMission, current.target);
					removeAperiodicHandlers(currMission);
					current.state = RtProcess.State.TERMINATED;
				} else
					// block it and can be released again
					current.state = RtProcess.State.BLOCKED;
			} else {
				// handler was preempted or in state NEW (first release)
				current.state = RtProcess.State.READY;
				pFrame.queue.insert(current);
			}
		}

		else if (current.target instanceof OneShotEventHandler) {
			if (current.state == RtProcess.State.HANDLED) {
				// oneShotHandler finished
				removeSO(currMission, current.target);
				current.state = RtProcess.State.TERMINATED;
			} else {
				// handler was preempted
				current.state = RtProcess.State.READY;
				pFrame.queue.insert(current);
			}
		}

		else if (current.target instanceof ManagedThread) {
			if (current.state == RtProcess.State.HANDLED) {
				// thread finished and removed.
				removeSO(currMission, current.target);
				current.state = RtProcess.State.TERMINATED;
			} else {
				// handler was preempted
				current.state = RtProcess.State.READY;
				pFrame.queue.insert(current);
			}
		}

		else {
			devices.Console.println(" current name: " + current.target.getName() + " index: " + current.index);
			throw new IllegalArgumentException("PriorityScheduler.move: UPS: another handler??");
		}

		// get next process from queue
		RtProcess nextProcess = pFrame.queue.extractMin();
		nextProcess.state = RtProcess.State.EXECUTING;
		current = nextProcess;

		if ((current == RtProcess.idleProcess) && (pFrame.queue.heapSize == 0)) {
			current.target.cleanUp();
			return null;
		} else {
			return nextProcess;
		}
	}

	/**
	 * 
	 * @return The maximum software real-time priority supported by this scheduler.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public int getMaxPriority() {
		return Priorities.MAX_PRIORITY;
	}

	/**
	 * 
	 * @return The minimum software real-time priority supported by this scheduler.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public int getMinPriority() {
		return Priorities.MIN_PRIORITY;
	}

	protected void insertReleaseQueue(RtProcess process) {
		pFrame.queue.insert(process);
	}

	protected void removeReleaseQueue(RtProcess process) {
		pFrame.queue.remove(process);
	}

	protected void addProcess(RtProcess process) {
		pFrame.addProcess(process);
	}

	protected void removeSO(Mission m, ManagedSchedulable ms) {
	}

	protected void removeAperiodicHandlers(Mission m) {
	}

	protected RtProcess getProcess(int missionIndex, int scjProcessIndex) {
		return null;
	}

	protected void waitForMS(Monitor monitor) {
		prioritySchedulerImpl.waitForMS(monitor);
	}

	protected void notifyForMS(Monitor monitor) {
		prioritySchedulerImpl.notifyForMS(monitor);
	}

	protected void moveToNext() {
		RtProcess nextProcess = pFrame.queue.extractMin();
		nextProcess.state = RtProcess.State.EXECUTING;
		current = nextProcess;
	}

	protected void addProcessToLockQueue(Object target, RtProcess process) {
		pFrame.lockQueue.addProcess(target, process);
	}

	protected RtProcess getProcessFromLockQueue(Object target) {
		return pFrame.lockQueue.getNextProcess(target);
	}

	public void printQueues() {
		vm.ClockInterruptHandler.instance.disable();
		devices.Console.println("");
		devices.Console.println("PS current process: " + current.target.getName());
		devices.Console.println("----------- release queue ----------");
		pFrame.queue.print();
		devices.Console.println("----------- lock queue ----------");
		pFrame.lockQueue.print();
		devices.Console.println("----------- wait queue ----------");
		pFrame.waitQueue.print();
		devices.Console.println("");
		vm.ClockInterruptHandler.instance.enable();
	}
}
