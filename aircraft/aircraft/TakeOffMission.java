/** Aircraft - Mode Change Example
* 
*   This mission deals with the aircraft's take off phase
*   
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;
import javax.realtime.PeriodicParameters;


public class TakeOffMission extends ModeMission implements LandingGearUser
{
	final double SAFE_AIRSPEED_THRESHOLD = 10.00;
	final double TAKEOFF_ALTITUDE = 10.00;
	
	private MainMission controllingMission;
	
	private boolean abort = false;
	
	/**
	 * Is the landing gear deployed?
	 */
	private boolean landingGearDeployed;

	
	public TakeOffMission(MainMission controllingMission)
	{
		this.controllingMission = controllingMission;
	}
	
	

	/**
	 * initialises the mission
	 */
	@Override
	protected void initialize()
	{
		StorageParameters storageParametersSchedulable= new StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);

		
		//Load the schedulables for this mission
		
		LandingGearHandler landingGearHandler = new LandingGearHandler(new PriorityParameters(5),
				new AperiodicParameters(), storageParametersSchedulable, "Landing Gear Handler", this);

		landingGearHandler.register();
		
		TakeOffMonitor takeOffMonitor =
				new TakeOffMonitor
					(new PriorityParameters(PriorityScheduler.instance().getMaxPriority()),
					new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(500, 0)),
					storageParametersSchedulable,
					this,
					TAKEOFF_ALTITUDE,
					landingGearHandler);
		
		takeOffMonitor.register();

		TakeOffFailureHandler takeOffFailureHandler = 
			new TakeOffFailureHandler(new PriorityParameters(5),
            				new AperiodicParameters(), 
            				storageParametersSchedulable, "Take Off Handler", this, SAFE_AIRSPEED_THRESHOLD);

		takeOffFailureHandler.register();

		
	}

	/**
	 * Returns the size of the mission's memory
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	public void abort() 
	{
		abort = true;		
	}



	public MainMission getControllingMission() {
		return controllingMission;
	}


	public void setControllingMission(MainMission controllingMission) {
		this.controllingMission = controllingMission;
	}


	@Override
	public synchronized void deployLandingGear()
	{
		landingGearDeployed = true;		
	}
	
	@Override
	public boolean cleanUp()
	{
		System.out.println("Take Off Mission Cleanup, Returning " + abort);
		return !abort;		
	}

	@Override
	public void stowLandingGear() 
	{
		landingGearDeployed = false;		
	}

	@Override
	public boolean isLandingGearDeployed() 
	{		
		return landingGearDeployed;
	} 
}
