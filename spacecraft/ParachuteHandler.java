/** Spacecraft - Mode Change Example
* 
*  Handler that deploys a parachute to slow the craft on landing
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package scjlevel2examples.spacecraft;


import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

public class ParachuteHandler extends AperiodicEventHandler
{
	/**
	 * The controlling mission
	 */
	private final LandMission mission;
	
	/**
	 * Class Constructor
	 *
	 *@param	priorityParameters	the priority parameters for this handler
	 * @param	periodicParameters	the periodic parameters for this handler
	 * @param 	storageConfigurationParameters	the storage parameters for this handler
	 * @param	size	the size of the private memory for this handler
	 * @param landMission the controlling mission
	 */
	public ParachuteHandler(PriorityParameters priorityParameters,
			AperiodicParameters aperiodicParameters,
			StorageParameters storageParameters,
			 LandMission landMission)
	{
		super(priorityParameters, aperiodicParameters, storageParameters, "Parachute Handler");
		
		mission = landMission; 
	}

	/**
	 * Called when the handler fires, deploys the parachute
	 */
	@Override
	public void handleAsyncEvent()
	{
		mission.deployParachute();	
	}

	

}
