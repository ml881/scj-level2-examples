/**************************************************************************
 * File name  : PrioritySchedulerImpl.java
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

import vm.Process;

final class PrioritySchedulerImpl implements vm.Scheduler {
	PriorityScheduler sch;

	protected PrioritySchedulerImpl(PriorityScheduler sch) {
		this.sch = sch;
	}

	public Process getNextProcess() {
		RtProcess scjProcess = sch.move();
		if (scjProcess != null) {
			return scjProcess.process;
		}
		sch.stop(sch.current.process);
		return null;
	}
	
	protected void waitForMS(Monitor monitor) {
		vm.ClockInterruptHandler.instance.disable();
		monitor.unlockWithOutEnable();
		sch.pFrame.waitQueue.addProcess(monitor, sch.current);

		// get the next process and set appropriate state.
		RtProcess next = sch.pFrame.queue.extractMin();
		next.state = RtProcess.State.EXECUTING;
		sch.current = next;

		vm.ClockInterruptHandler.instance.enable();
		vm.ClockInterruptHandler.instance.handle();
	}

	protected void notifyForMS(Monitor monitor) {
		RtProcess process = sch.pFrame.waitQueue.getNextProcess(monitor);
		if (process != null) {
			sch.pFrame.lockQueue.addProcess(monitor, process);
		}
	}

	@Override
	public void wait(Object target) {
		Monitor monitor = Monitor.getMonitor(target);
		
		monitor.unlockWithOutEnable();
		sch.pFrame.waitQueue.addProcess(monitor, sch.current);

		// get the next process and set appropriate state.
		RtProcess next = sch.pFrame.queue.extractMin();
		next.state = RtProcess.State.EXECUTING;
		sch.current = next;
	}

	@Override
	public void notify(Object target) {
		Monitor monitor = Monitor.getMonitor(target);

		RtProcess process = sch.pFrame.waitQueue.getNextProcess(monitor);

		if (process != null) {
			if (process.next_temp != null) {
				sch.pFrame.queue.remove(process);
				process.next.set(process.next_temp);
				process.next_temp = null;
				process.isNotified = true;
			}
			
			sch.pFrame.lockQueue.addProcess(monitor, process);
		}
	}

	@Override
	public void notifyAll(Object target) {
		Monitor monitor = Monitor.getMonitor(target);

		RtProcess process = sch.pFrame.waitQueue.getNextProcess(monitor);
		while (process != null) {
			
			if (process != null) {
				if (process.next_temp != null) {
					sch.pFrame.queue.remove(process);
					process.next.set(process.next_temp);
					process.next_temp = null;
					process.isNotified = true;
				}
			}
			
			sch.pFrame.lockQueue.addProcess(monitor, process);
			process = sch.pFrame.waitQueue.getNextProcess(monitor);
		}
	}
	
}
