/** Spacecraft - Mode Change Example
* 
*   Handler for dealing with the craft's landing gear 
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package scjlevel2examples.spacecraftpack;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;


public class LandingGearHandler extends AperiodicEventHandler
{
	/**
	 * The controlling mission of this handler
	 */
	private final LandMission mission;
	
	/**
	 * Class Constructor 
	 * @param priority the priority parameters for this handler
	 * @param release the release parameters for this handler
	 * @param storage the storage parameters for this handler
	 * @param size the private memory size of this handler
	 * @param landMission the controlling mission of this handler
	 */
	public LandingGearHandler(PriorityParameters priority, AperiodicParameters release,
			StorageParameters storage, LandMission landMission)
	{
		super(priority, release, storage, "Landing Gear Handler");
		
		mission = landMission; 
	}
	
	/**
	 * Called when the handler is fired, deploys the landing gear
	 */
	@Override
	public void handleAsyncEvent()
	{
		System.out.println("Deploying Landing Gear");
		
		mission.deployLandingGear();
	}	
}
