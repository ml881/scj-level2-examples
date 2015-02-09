/** Spacecraft - Mode Change Example
 * 
 * 	This class monitors the craft's environment -- Oxygen levels, internal pressure, fuel levels etc.
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class EnvironmentMonitor extends PeriodicEventHandler
{

	private final MainMission mainMission;

	/**
	 * Class Constructor
	 * 
	 * @param priority
	 *            priority parameters
	 * @param periodic
	 *            periodic parameters
	 * @param storage
	 *            storage parameters
	 * @param size
	 *            private memory size
	 */
	public EnvironmentMonitor(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			MainMission mainMission)
	{
		super(priority, periodic, storage);
		this.mainMission = mainMission;
	}

	/**
	 * Called when the handler is fired
	 */
	@SuppressWarnings("unused")
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Checking Environment");

		// **Obviously these is for testing purposes only toggle the true and
		// false values to test behaviour

		// if environment conditions are bad
		// if(true)

		// if environment conditions are fine
		if (false)
		{
			// To get here the environment conditions should be below safe
			// levels

			mainMission.environmentBad();
		}
	}

}
