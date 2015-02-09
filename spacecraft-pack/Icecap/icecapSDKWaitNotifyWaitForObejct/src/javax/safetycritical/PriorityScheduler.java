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
package javax.safetycritical;

import javax.realtime.Monitor;
import javax.realtime.RtProcess;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.scj.util.Priorities;

/**
 * This class represents the priority-based scheduler for Level 1 and 2. <br>
 * The only access to the priority scheduler is for obtaining the software priorities.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 */
@SCJAllowed(Level.LEVEL_1)
public class PriorityScheduler extends javax.realtime.PriorityScheduler {

	/**
	 * 
	 * @return The priority scheduler.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public static PriorityScheduler instance() {
		if (scheduler == null) {
			scheduler = new PriorityScheduler();
		}
		return (PriorityScheduler) scheduler;
	}

	private PriorityScheduler() {
		super();
	}

	void addOuterMostSeq(ManagedEventHandler handler, int[] stack) {
		if (handler instanceof MissionSequencer<?>) {
			ScjProcess process = new ScjProcess(handler, stack);
			process.setIndex(-2);
			outerMostSeqProcess = handler.getScjProcess();
			addProcess(process);
		}
	}

	protected void start() {
		super.start();
	}

	protected ScjProcess getCurrentProcess() {
		return (ScjProcess) current;
	}

	protected void insertReleaseQueue(RtProcess process) {
		super.insertReleaseQueue(process);
	}

	protected void addProcess(ScjProcess process) {
		super.addProcess(process);
	}

	protected void removeReleaseQueue(ScjProcess process) {
		super.removeReleaseQueue(process);
	}

	@Override
	protected void removeSO(Mission m ,ManagedSchedulable ms) {
		m.MSSetForMission.removeMSO(ms);
	}

	@Override
	protected void removeAperiodicHandlers(Mission m) {
		m.MSSetForMission.removeAperiodicHandlers();
	}
	
	@Override
	protected RtProcess getProcess(int missionIndex, int scjProcessIndex){
		return Mission.missionSet[missionIndex].MSSetForMission.scjProcesses[scjProcessIndex];
	}
	
	protected Mission getCurrentMission(){
		return currMission;
	}
	
	protected void waitForMS(Monitor monitor){
		super.waitForMS(monitor);
	}
	
	protected void notifyForMS(Monitor monitor){
		super.notifyForMS(monitor);
	}
	
	protected void moveToNext(){
		super.moveToNext();
	}
	
	protected void addProcessToLockQueue(Object target, RtProcess process){
		super.addProcessToLockQueue(target, process);
	}
	
	protected ScjProcess getProcessFromLockQueue(Object target){
		return (ScjProcess) super.getProcessFromLockQueue(target);
	}
	
	/**
	 * 
	 * @return The maximum hardware real-time priority supported by this scheduler.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public int getMaxHardwarePriority() {
		return Priorities.MAX_HARDWARE_PRIORITY;
	}

	/**
	 * 
	 * @return The minimum hardware real-time priority supported by this scheduler.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public int getMinHardwarePriority() {
		return Priorities.MIN_HARDWARE_PRIORITY;
	}

}
