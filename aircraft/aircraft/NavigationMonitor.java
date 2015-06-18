/** Aircraft - Mode Change Example
* 
* 	Checks the navigation systems of the aircraft to ensure that the craft is on the correct course
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;



/**
 * Handler for monitoring the conditions which have to be true for the craft to begin cruising
 * @author Matt Luckcuck
 *
 */
public class NavigationMonitor extends PeriodicEventHandler
{
	private CruiseMission mission;
	
	public NavigationMonitor(PriorityParameters priority, PeriodicParameters periodic,
			StorageParameters storage, String name, CruiseMission mission)
	{
		super(priority, periodic, storage, name);	
		this.mission = mission;
	}

    /**
    *   Called when the handler is fired
    */
	@SuppressWarnings("unused")
	@Override
	public void handleAsyncEvent()
	{
		//read and check these variables
		double heading = mission.getControllingMission().getHeading();
		double airSpeed = mission.getControllingMission().getAirSpeed();
		double altitude = mission.getControllingMission().getAltitude();
		
		//Obviously this would then check the variables again expected values
		
	}
}
