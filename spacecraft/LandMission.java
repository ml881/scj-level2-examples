/** Spacecraft - Mode Change Example
 * 
 * 	This mission handles events when the craft is landing 
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.scj.util.Const;

import devices.Console;

public class LandMission extends Mission implements Mode
{


	/**
	 * Initialise the mission
	 */
	@Override
	protected void initialize()
	{
		Console.println("Land Mission: Init ");

		/* ***Start this mission's handlers */
	
		AirSpeedMonitor airSpeedMonitor = new AirSpeedMonitor(
				new PriorityParameters(5), new PeriodicParameters(
						new RelativeTime(0, 0), new RelativeTime(500, 0)),
				SPSafelet.storageParameters_Schedulable, this);
		airSpeedMonitor.register();

		 LandingGearHandler landingHandler = new LandingGearHandler(
		 new PriorityParameters(5), new AperiodicParameters(new RelativeTime(0,0), null),
		 SPSafelet.storageParameters_Schedulable, this);
		
		 landingHandler.register();

		 ParachuteHandler parachuteHandler = new ParachuteHandler(
		 new PriorityParameters(5), new AperiodicParameters(new RelativeTime(0,0), null),
		 SPSafelet.storageParameters_Schedulable, this);
		
		 parachuteHandler.register();

		GroundDistanceMonitor groundDistanceMonitor = new GroundDistanceMonitor(
					new PriorityParameters(5), new PeriodicParameters(
							new RelativeTime(0, 0), new RelativeTime(500, 0)),
					SPSafelet.storageParameters_Schedulable, this, landingHandler, parachuteHandler);
		groundDistanceMonitor.register();

		 
		Console.println("Land Mission: Begin ");
	}

	/**
	 * Returns the size of this mission's memory
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_DEFAULT;
	}

	

}
