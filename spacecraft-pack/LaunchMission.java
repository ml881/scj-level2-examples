/** Spacecraft - Mode Change Example
 * 
 *   This mission deals with launching the craft 
 *   
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraftpack;

import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.scj.util.Const;

import devices.Console;

public class LaunchMission extends Mission implements Mode
{
	/**
	 * This variable represents if the craft is able to launch, when the
	 * countdown reaches 0
	 */
	private volatile boolean launch;

	/**
	 * Called when the craft is ok to launch sets <code>launch</code> to
	 * <code>true</code>
	 */
	public void goodToLaunch()
	{
		launch = true;
	}

	/**
	 * Returns the <code>launch</code> variable
	 * 
	 * @return <code>launch</code>
	 */
	public boolean canLaunch()
	{
		return launch;
	}

	/**
	 * initialises the mission
	 */
	@Override
	protected void initialize()
	{
		Console.println("Launch Mission: Init ");
		
		// Initially false because the conditions haven't been checked yet
		launch = false;

		// Load the handlers for this mission
		// Note these handlers are passed a reference to this mission so they
		// can update the
		// ready to launch variable with the two methods above
		LaunchConditionsMonitor launchConditionsMonitor = new LaunchConditionsMonitor(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
						new RelativeTime(500, 0)),
				SPSafelet.storageParameters_Schedulable, this);
		launchConditionsMonitor.register();

		LaunchHandler launchHandler = new LaunchHandler(new PriorityParameters(
				5), new AperiodicParameters(),
				SPSafelet.storageParameters_Schedulable, this);
		launchHandler.register();

		LaunchCountdown launchCountdown = new LaunchCountdown(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
						new RelativeTime(1000, 0)),
				SPSafelet.storageParameters_Schedulable, 5, launchHandler);
		launchCountdown.register();

		
		Console.println("Launch Mission: Begin ");	
	}

	/**
	 * Returns the size of the mission's memory
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

}
