/** Spacecraft - Mode Change Example
 * 
 * 	This mission handles events when the craft is landing 
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.scj.util.Const;

import devices.Console;

public class LandMission extends Mission implements Mode
{

	/**
	 * The craft's distance form the ground in Metres
	 */
	private double distanceFromGround;
	/**
	 * The craft's airSpeed in Knots
	 */
	@SuppressWarnings("unused")
	private double airSpeed;
	/**
	 * Is the parachute deployed?
	 */
	@SuppressWarnings("unused")
	private boolean parachuteDeployed;
	/**
	 * Is the landing gear deployed?
	 */
	@SuppressWarnings("unused")
	private boolean LandingGearDeployed;

	/**
	 * Initialise the mission
	 */
	@Override
	protected void initialize()
	{
		Console.println("Land Mission: Init ");
		
		/* ***Start this mission's handlers */
		GroundDistanceMonitor groundDistanceMonitor = new GroundDistanceMonitor(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
						new RelativeTime(500, 0)),
			 SPSafelet.storageParameters_Schedulable, this);
		groundDistanceMonitor.register();

		AirSpeedMonitor airSpeedMonitor = new AirSpeedMonitor(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
						new RelativeTime(500, 0)),
				SPSafelet.storageParameters_Schedulable, this);
		airSpeedMonitor.register();

//		LandingGearHandler landingHandler = new LandingGearHandler(
//				new PriorityParameters(5), new AperiodicParameters(),
//				SPSafelet.storageParameters_Schedulable, this);
//
//		landingHandler.register();

//		ParachuteHandler parachuteHandler = new ParachuteHandler(
//				new PriorityParameters(5), new AperiodicParameters(),
//				SPSafelet.storageParameters_Schedulable, this);
//
//		parachuteHandler.register();
		
		Console.println("Land Mission: Begin ");
	}

	/**
	 * Returns the size of this mission's memory
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	/**
	 * called when landing gear is deployed, sets
	 * <code>LandingGearDeployed</code> to <code>true</code>
	 */
	public synchronized void deployLandingGear()
	{
		LandingGearDeployed = true;

	}

	/**
	 * sets <code>distanceFromGround<code> to the supplied <code>double</code>
	 * 
	 * @param distanceFromGround
	 *            the distance the craft is from the ground in metres
	 */
	public synchronized void setGroundDistance(double distanceFromGround)
	{
		this.distanceFromGround = distanceFromGround;
	}

	/**
	 * Sets the <code>airSpeed</code> to the supplied <code>double</code>
	 * 
	 * @param airSpeed
	 *            the craft's air speed in knots
	 */
	public void setAirSpeed(double airSpeed)
	{
		this.airSpeed = airSpeed;
	}

	/**
	 * Deploys the parachute
	 */
	public void deployParachute()
	{
		// Assuming that 0.0 IS wheels on the run way
		// This would likely be a helper method
		if (distanceFromGround == 0.0)
		{
			// actually deploy parachute then set this variable
			parachuteDeployed = true;
		}
	}

}
