/** Aircraft - Mode Change Example
* 
* 	SafeLandingMonitor which checks the distance from the ground and 
* 	if there is a problem during landing will ensure that the aircraft pulls up 
* 	(if the aircraft is still far enough from the ground that it can pull up) 
* 	or alerts the pilot and deploys any crash mitigation systems 
*   (if the aircraft is too close to the ground to safely pull up).
*   
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;


public class SafeLandingHandler extends AperiodicEventHandler
{
	/**
	 * The controlling mission 
	 */
	private final LandMission landMission;
	
	private double threshold;
	
	/**
	 * Class Constructor
	 * @param	priorityParameters	the priority parameters for this handler
	 * @param	periodicParameters	the periodic parameters for this handler
	 * @param 	storageConfigurationParameters	the storage parameters for this handler
	 * @param	size	the size of the private memory for this handler
	 * @param launchMission the controlling mission
	 */
	public SafeLandingHandler(PriorityParameters priority, AperiodicParameters release,
			StorageParameters storage, String name, LandMission landMission, Double threshold)
	{
		super(priority, release, storage, name);
		this.landMission = landMission;
		this.threshold = threshold;
	}
	
	/**
	 * Called when the handler is fired
	 * Launches the craft
	 */
	@Override
	public void handleAsyncEvent()
	{
		
		double altitude = landMission.getControllingMission().getAltitude();
		
		//in both cases this failure should be flagged somewhere
		if(altitude < threshold)
		{
			System.out.println("Failure: Pull Up");
			// Also perform some recovery action here, maybe a new mode
		}
		else
		{
			System.out.println("Failure: Continue With Landing");
		}
	}
	
}
