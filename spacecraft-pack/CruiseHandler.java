/** Spacecraft - Mode Change Example
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraftpack;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

//Handler for responding to the pilot starting an engine burn
public class CruiseHandler extends AperiodicEventHandler
{
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
	 */
	public CruiseHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage)
	{
		super(priority, release, storage, "Cruise Handler");

	}

	/**
	 * Called when the handler is fired
	 */
	@Override
	public void handleAsyncEvent()
	{
		System.out.println("Handling Cruise");

	}

}
