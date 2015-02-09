/** Spacecraft - Mode Change Example
 * 
 * 	Handler for launching the craft
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class LaunchHandler extends AperiodicEventHandler
{
	/**
	 * The controlling mission
	 */
	private final LaunchMission launchMission;

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
	 *            the controlling mission
	 */
	public LaunchHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			LaunchMission launchMission)
	{
//		super(priority, release, storage);
		super(priority, release, storage, "Laucnh Handler");
		
		Console.println("LaunchHandler: Construct");
		this.launchMission = launchMission;
	}

	/**
	 * Called when the handler is fired Launches the craft
	 */
	@Override
	public void handleAsyncEvent()
	{ // if the launch mission says that the launch can go ahead then the craft
		// launches
		// for testing, here the launch mission is just terminated
		// else the handler waits for it's next release and checks again
		Console.println("LaunchHandler: LaunchHandler");
		
		if (launchMission.canLaunch())
		{
			Console.println("LaunchHandler: Launching!");
			
			launchMission.requestTermination();
		}
		else
		{
			Console.println("LaunchHandler: Launch Blocked!");
		}
	}

}
