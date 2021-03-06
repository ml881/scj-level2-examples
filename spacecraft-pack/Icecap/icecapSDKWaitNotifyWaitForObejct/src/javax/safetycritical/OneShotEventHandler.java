/**************************************************************************
 * File name: OneShotEventHandler.java
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

import javax.realtime.AbsoluteTime;
import javax.realtime.AperiodicParameters;
import javax.realtime.Clock;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

/**
 * This class permits the automatic execution of time-triggered code. The <code>handleAsyncEvent</code> method behaves as if the handler were attached
 * to a one-shot timer asynchronous event.
 * <p>
 * 
 * This class is abstract, non-abstract sub-classes must implement the method <code>handleAsyncEvent</code> and may override the default
 * <code>cleanUp</code> method.
 * 
 * Note that the values in parameters passed to the constructors are those that will be used by the infrastructure. Changing these values after
 * construction will have no impact on the created event handler.
 * 
 * @version 1.2; - December 2013
 * 
 * @author Anders P. Ravn, Aalborg University, <A HREF="mailto:apr@cs.aau.dk">apr@cs.aau.dk</A>, <br>
 *         Hans S&oslash;ndergaard, VIA University College, Denmark, <A HREF="mailto:hso@viauc.dk">hso@via.dk</A>
 * 
 * @scjComment - SCJ issue: One constructor only. <br>
 */

@SCJAllowed(Level.LEVEL_1)
public abstract class OneShotEventHandler extends ManagedEventHandler {
	HighResolutionTime releaseTime;
	PriorityScheduler sch;

	/**
	 * Constructs a one-shot event handler.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this event handler. Must not be <code>null</code>.
	 * @param releaseTime
	 *            specifies the time at which the handler should be released. A relative time is relative to the start of the associated mission. A
	 *            null parameter is equivalent to a relative time of 0.
	 * @param release
	 *            specifies the aperiodic release parameters, in particular the deadline miss handler. A <code>null</code> parameter indicates that
	 *            there is no deadline associated with this handler.
	 * @param storage
	 *            specifies the storage parameters. It must not be <code>null</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if priority, release (or storage ?) is null.
	 * 
	 * 
	 */
	/*
	 * @ public normal_behavior requires priority != null && release != null; ensures true; also public exceptional_behavior requires priority ==
	 * null; signals (IllegalArgumentException) true; also public exceptional_behavior requires release == null; signals (IllegalArgumentException)
	 * true;
	 * 
	 * @
	 */
	@SCJAllowed(Level.LEVEL_1)
	@SCJRestricted(Phase.INITIALIZE)
	public OneShotEventHandler(PriorityParameters priority, HighResolutionTime releaseTime, AperiodicParameters release, StorageParameters storage,
			String name) {
		super(priority, release, storage, name);

		if (releaseTime instanceof RelativeTime
				&& (releaseTime.getMilliseconds() < 0 || (releaseTime.getMilliseconds() == 0 && releaseTime.getNanoseconds() < 0)))
			throw new IllegalArgumentException("releaseTime is a negative time");
		else
			this.releaseTime = releaseTime;
	}

	public OneShotEventHandler(PriorityParameters priority, HighResolutionTime releaseTime, AperiodicParameters release, StorageParameters storage) {
		this(priority, releaseTime, release, storage, null);
	}

	public OneShotEventHandler(PriorityParameters priority, AperiodicParameters release, StorageParameters storage) {
		this(priority, null, release, storage, null);
	}

	/**
	 * Deschedules the release of the handler.
	 * 
	 * @return true if the handler was scheduled to be released, false otherwise.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public boolean deschedule() {
		ManagedSchedulableSet hs = mission.MSSetForMission;

		if (hs.contains(this)) {
			hs.removeMSO(this);
			return true;
		} else
			return false;
	}

	public final void cleanUp() {
		super.cleanUp();
	}

	@SCJAllowed(Level.INFRASTRUCTURE)
	@SCJRestricted(Phase.INITIALIZE)
	public final void register() {
		super.register();
		sch = PriorityScheduler.instance();
	}

	public AbsoluteTime getNextReleaseTime(AbsoluteTime dest) {
		if (dest != null) {
			dest.set(process.getNext());
		} else {
			dest = new AbsoluteTime(process.getNext());
		}
		return dest;
	}

	public void scheduleNextReleaseTime(HighResolutionTime time) {
		vm.ClockInterruptHandler.instance.disable();
		sch.removeReleaseQueue(process);
		if (time instanceof RelativeTime) {
			RelativeTime releaseTime = (RelativeTime) time;
			process.getNext().add(releaseTime, process.getNext()); // next = next + releaseTime
		} else {
			AbsoluteTime releaseTime = (AbsoluteTime) time;
			int compare = releaseTime.compareTo(Clock.getRealtimeClock().getTime(new AbsoluteTime(Clock.getRealtimeClock())));
			if (compare < 0)
				process.getNext().add(new RelativeTime(), process.getNext());
			else
				process.getNext().set((AbsoluteTime) releaseTime);
		}
		sch.insertReleaseQueue(process);
		vm.ClockInterruptHandler.instance.enable();
	}

}
