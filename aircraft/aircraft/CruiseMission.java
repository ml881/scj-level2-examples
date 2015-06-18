/** Aircraft - Mode Change Example
* 
 * This mission handles events when the Aircraft is cruising 
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;


public class CruiseMission extends ModeMission
{
	private final MainMission controllingMission;
	
	public CruiseMission(MainMission controllingMission)
	{
		this.controllingMission = controllingMission;
	}
		
	
	@Override
	protected void initialize()
	{
		StorageParameters storageParametersSchedulable= new StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
		
		
/*  *** Start the Schedulable Objects *** */
 
		BeginLandingHandler beginLandingHandler = new BeginLandingHandler(new PriorityParameters(5),
				new AperiodicParameters(),
				storageParametersSchedulable,
				"Begin Landing Handler",
				this);
		beginLandingHandler.register();
				
 
		/**
		 * Handler for monitoring the cruising conditions and updating <code>okToCruise</code> 
		 */
		NavigationMonitor navigationMonitor = 
				new NavigationMonitor(
					new PriorityParameters(PriorityScheduler.instance().getMaxPriority()) ,
					new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(10, 0)),
					storageParametersSchedulable,
					"Cruise Controller", this);
		navigationMonitor.register();
		
	
		/**
		 * Handler simulating a button push to begin landing
		 */
		AperiodicSimulator landSim = new AperiodicSimulator(
				new PriorityParameters(5),
				new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(10, 0)),
				storageParametersSchedulable,
				beginLandingHandler);
		landSim.register();

	}

	/**
	 * returns the mission's private memory size
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}



	
	public MainMission getControllingMission() {
		return controllingMission;
	}

	

}
