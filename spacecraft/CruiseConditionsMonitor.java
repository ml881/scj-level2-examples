/** Spacecraft - Mode Change Example
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

/**
 * Handler for monitoring the conditions which have to be true for the craft to
 * begin cruising
 * 
 * @author Matt Luckcuck
 * 
 */
public class CruiseConditionsMonitor extends PeriodicEventHandler
{
	/**
	 * The controlling mission
	 */
	private final CruiseMission mission;

	/**
	 * The count of times this handler will be released before terminating the
	 * controlling mission
	 */
	private int count = 5;

	public CruiseConditionsMonitor(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			CruiseMission mission)
	{
		super(priority, periodic, storage);
		this.mission = mission;
	}

	/**
	 * Called when the handler is fired
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Checking Cruise Conditions");
		// Check sensors to make sure an engine burn is safe

		if (count == 0)
		{
			Console.println("CruiseConditionsMonitor: Terminating");
			mission.requestTermination();
		} else
		{
			Console.println("CruiseConditionsMonitor: " + count);
			mission.setOkToCruise(true);
			count--;
		}
	}
}