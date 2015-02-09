/** Spacecraft - Mode Change Example
 * 
 * This mission handles events when the craft is cruising -- not launching,
 * orbiting, or landing.
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.Clock;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.Services;
import javax.scj.util.Const;

import devices.Console;

public class CruiseMission extends Mission implements Mode
{
	/**
	 * Boolean representing if it is safe to burn the engines
	 */
	private boolean okToCruise = true;

	/**
	 * Desired duration of the burn
	 */
	private RelativeTime burnDuration;

	@Override
	protected void initialize()
	{
		
		Console.println("Cruise Mission: Init ");
		
		/**
		 * Then length of time to burn the engines for
		 */
		burnDuration = new RelativeTime();

		/**
		 * Handler for monitoring the cruising conditions and updating
		 * <code>okToCruise</code>
		 */
		CruiseConditionsMonitor crusieConditionsMonitor = new CruiseConditionsMonitor(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0), new RelativeTime(500, 0)),
				SPSafelet.storageParameters_Schedulable, this);
		crusieConditionsMonitor.register();

		/**
		 * Handler for responding to the burn being activated
		 */
//		BurnActivationHandler burnActivationHandler = new BurnActivationHandler(
//				new PriorityParameters(5), new AperiodicParameters(),
//				SPSafelet.storageParameters_Schedulable,	this);
//		burnActivationHandler.register();

		/**
		 * Handler for activating the engine burn when requested
		 */
//		BurnDurationHandler burnDurationHandler = new BurnDurationHandler(
//				new PriorityParameters(5), new AperiodicParameters(),
//				SPSafelet.storageParameters_Schedulable,	this);
//		burnDurationHandler.register();
		/**
		 * Handler simulating a button push to activate the burn
		 */
//		AperiodicSimulator cruiseSim = new AperiodicSimulator(
//				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
//						new RelativeTime(100, 0)),
//				SPSafelet.storageParameters_Schedulable, burnActivationHandler);
//		cruiseSim.register();
		
		Console.println("Cruise Mission: Begin ");
	}

	/**
	 * returns the mission's private memory size
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	/**
	 * returns <code> oKToCruise</code>
	 * 
	 * @return true if it is ok to activate the burn, false if it is not
	 */
	public boolean isOkToCruise()
	{
		return okToCruise;
	}

	/**
	 * Sets <code>okToCruise</code>
	 * 
	 * @param okToCruise
	 *            new boolean value for <code>okToCruise</code>
	 */
	public void setOkToCruise(boolean okToCruise)
	{
		this.okToCruise = okToCruise;
	}

	/**
	 * Sets the duration of the burn
	 * 
	 * @param millis
	 *            burn duration millisecond part
	 * @param nanos
	 *            burn duration nanosecond part
	 */
	public synchronized void setBurnDuration(long millis, int nanos)
	{
		burnDuration.set(millis, nanos);
	}

	/**
	 * activates the engine burn
	 */
	public void activateBurn()
	{
		Console.println("Burning Engines!");
		// actually activate the engines here
		Services.delay(Clock.getRealtimeClock().getTime().add(burnDuration));
	}

}
