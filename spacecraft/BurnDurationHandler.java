/** Spacecraft - Mode Change Example
 * 
 * Handler that sets the required burn duration based on user input (simulated in this example)
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class BurnDurationHandler extends AperiodicEventHandler
{
	/**
	 * This handler's controlling mission
	 */
	private final CruiseMission mission;
	/**
	 * The burn duration's nanosecond component
	 */
	private int nanos;
	/**
	 * the burn duration's millisecond component
	 */
	private long millis;

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
	 * @param mission
	 *            this handler's controlling mission
	 */
	public BurnDurationHandler(PriorityParameters priority,
			AperiodicParameters aperiodic, StorageParameters storage,
			CruiseMission mission)
	{
		super(priority, aperiodic, storage, "Burn Duration Handler");
		this.mission = mission;
		nanos = 0;
		millis = 0;
	}

	/**
	 * Sets the desired duration of the burn
	 * 
	 * @param millis
	 *            burn duration, millisecond part
	 * @param nanos
	 *            burn duration, nanosecond part
	 */
	public void setBurnDuration(int millis, int nanos)
	{
		this.millis = millis;
		this.nanos = nanos;
	}

	/**
	 * This method is called when the handler is fired.
	 * 
	 * sets the <code>burnDuration</code> in the controlling mission to the
	 * values stored in this handler
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Burn Duration Handler");
		mission.setBurnDuration(millis, nanos);
	}
}
