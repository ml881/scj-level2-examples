/** Aircraft - Mode Change Example
* 
* 	The TakeOffMonitor reads the current height from the ground, from the main mission, 
* 	and when the aircraft has actually taken off it retracts the landing gear and terminates 
* 	the TakeOff mission.
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


public class TakeOffMonitor extends PeriodicEventHandler
{
	/**
	 * Controlling mission
	 */
	private final TakeOffMission takeoffMission;
	
	private double takeOffAltitude;
	private AperiodicEventHandler landingGearHandler;
	
	/**
	 * Class Constructor
	 * @param	priorityParameters	the priority parameters for this handler
	 * @param	periodicParameters	the periodic parameters for this handler
	 * @param 	storageConfigurationParameters	the storage parameters for this handler
	 * @param	string	the size of the private memory for this handler
	 * @param takeoffMission controlling mission
	 */
	public TakeOffMonitor(PriorityParameters priority, PeriodicParameters periodic,
			StorageParameters storage,  TakeOffMission takeoffMission, double takeOffAltitude, 
			AperiodicEventHandler landingGearHandler)
	{
		super(priority, periodic, storage);	
		this.takeoffMission = takeoffMission;	
		this.takeOffAltitude = takeOffAltitude;
		this.landingGearHandler = landingGearHandler;
	}

	/**
	 * Called when the handler is fired, sets <code>goodToLaunch</code>
	 */
	@Override
	public void handleAsyncEvent()
	{
		System.out.println("Reading Altitude");
		
		double altitude = takeoffMission.getControllingMission().getAltitude();
		
		if (altitude > takeOffAltitude)
		{
			System.out.println("Take Off Complete");
			landingGearHandler.release();
			takeoffMission.requestTermination();
		}
		
	}
}
