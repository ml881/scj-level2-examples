/** Spacecraft - Mode Change Example
 *    
 *    This Handler monitors the air speed of the Spacecraft
 *    
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraftpack;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class AirSpeedMonitor extends PeriodicEventHandler
{
	/**
	 * A reference to this handler's controlling mission
	 */
	private final LandMission mission;

	/**
	 * Class constructor
	 * 
	 * @param priorityParameters
	 *            the priority parameters for this handler
	 * @param periodicParameters
	 *            the periodic parameters for this handler
	 * @param storageConfigurationParameters
	 *            the storage parameters for this handler
	 * @param size
	 *            the size of the private memory for this handler
	 * @param landMission
	 *            the controlling mission of this handler
	 *
	 */
	public AirSpeedMonitor(PriorityParameters priorityParameters,
			PeriodicParameters periodicParameters,
			StorageParameters storageParameters, LandMission landMission)
	{
		super(priorityParameters, periodicParameters, storageParameters);

		mission = landMission;
	}

	/**
	 * The method the infrastructure calls when the handler is released
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("AirSpeedMonitor: check Air Speed");
		mission.setAirSpeed(0.0);
	}

}
