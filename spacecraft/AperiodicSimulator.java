/** Spacecraft - Mode Change Example
 * 
 * This class is not a component of the pattern or the example application,
 * it is plumbing only.
 * 
 * This class simulates the aperiodic firing of an 
 * external event (e.g. a button press) by simply firing the event periodically
 * 
 * @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class AperiodicSimulator extends PeriodicEventHandler
{

	AperiodicEventHandler aperiodic;

	/**
	 * Class constructor
	 * 
	 * @param priority
	 *            the priority of the handler
	 * @param periodic
	 *            the periodic parameters of the handler
	 * @param storage
	 *            the storage parameters of the handler
	 * @param size
	 *            the size of the private memory of the handler
	 * @param aperiodicEvent
	 *            the aperiodic even to be fires each period
	 */
	public AperiodicSimulator(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			AperiodicEventHandler aperiodicEvent)
	{
		super(priority, periodic, storage);
		aperiodic = aperiodicEvent;
	}

	/**
	 * The method the infrastructure calls when it is fired
	 * 
	 * This method fires the <code>event</code>
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Simulating AperiodicEvent");
		aperiodic.release();
	}

}
