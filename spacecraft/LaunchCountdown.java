/** Spacecraft - Mode Change Example
 * 
 * 	This handler counts down from a given value to zero
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class LaunchCountdown extends PeriodicEventHandler
{
	/**
	 * The Aperiodic Event Handler to be released
	 */
	private final AperiodicEventHandler aperiodic;
	
	/**
	 * The starting value of the countdown
	 */
	private int countdown;

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
	 * @param countdown
	 *            starting value of countdown
	 * @param ae
	 *            the event to be fired
	 */
	public LaunchCountdown(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			int countdown, AperiodicEventHandler ae)
	{
		super(priority, periodic, storage);
		Console.println("LaunchCountdown: Construct");
		
		aperiodic = ae;
		this.countdown = countdown;
	}

	/**
	 * Called when this event is fired, if the countdown is 0 then fire the
	 * launch event
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("***LaunchCountdown***");
		// if the count has reached 0 then fire the launch event
		// else decrement the count
		if (countdown == 0)
		{
			Console.println("" + countdown);
			Console.println("Launching");
			aperiodic.release();
		}
		else
		{
			Console.println("" + countdown);
			countdown--;
		}
	}

}
