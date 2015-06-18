/** Aircraft - Mode Change Example
* 
* 	Handler for monitoring the Instrument Landing System
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


public class InstrumentLandingSystemMonitor extends PeriodicEventHandler
{
	LandMission mission;
	
	/**
	 * Class Constructor
	 * @param priority the priority parameters of this handler
	 * @param periodic the periodic parameters of this handler
	 * @param storage the storage parameters of this handler
	 * @param size the private memory size for this handler
	 */
	public InstrumentLandingSystemMonitor(PriorityParameters priority, PeriodicParameters periodic,
			StorageParameters storage, String name, LandMission mission)
	{
		super(priority, periodic, storage, name);
		
		this.mission = mission;
	}

	/**
	 * Called when this handler is fired
	 */
	@Override
	public void handleAsyncEvent()
	{
		System.out.println("Checking ILS");
		
	}

}
