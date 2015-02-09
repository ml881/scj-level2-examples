/** Spacecraft - Mode Change Example
 * 
 * 	 handler monitors the conditions which must be true for the craft to launch
//Obviously this is simulated here
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class LaunchConditionsMonitor extends PeriodicEventHandler
{
	/**
	 * Controlling mission
	 */
	private final LaunchMission launchMission;
	/**
	 * This variable is for testing only and indicates the amount of releases
	 * the handler will have before the launch conditions are 'good enough' to
	 * be true
	 */
		

	/**
	 * Class Constructor
	 * 
	 * @param priorityParameters
	 *            the priority parameters for this handler
	 * @param periodicParameters
	 *            the periodic parameters for this handler
	 * @param storageConfigurationParameters
	 *            the storage parameters for this handler
	 * @param size
	 *            the size of the private memory for this handler
	 * @param launchMission
	 *            controlling mission
	 */
	public LaunchConditionsMonitor(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			LaunchMission launchMission)
	{
		super(priority, periodic, storage);
		
		Console.println("LaunchConditionsMonitor: Construct");
		this.launchMission = launchMission;
	}

	/**
	 * Called when the handler is fired, sets <code>goodToLaunch</code>
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Checking Launch Conditions");
		//This would check the launch conditions from sensors 
		
		launchMission.goodToLaunch();
		

	}

}
