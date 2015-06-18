/** Aircraft - Mode Change Example
* 
* 	This mission handles events when the Aircraft is landing 
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

public class LandMission extends ModeMission implements LandingGearUser
{
	private final MainMission controllingMission;
	
	final double SAFE_LANDING_ALTITUDE = 10.00;
	
	private boolean abort = false;
	
	public LandMission(MainMission controllingMission)
	{
		this.controllingMission = controllingMission;
	}	

	/**
	 * Is the landing gear deployed?
	 */
	private boolean landingGearDeployed;

	/**
	 * Initialise the mission
	 */
	@Override
	protected void initialize()
	{
		
		StorageParameters storageParametersSchedulable= new StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
		
/* ***Start this mission's handlers */
		GroundDistanceMonitor groundDistanceMonitor = new GroundDistanceMonitor(
				new PriorityParameters(5),
				new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(10, 0)),
				storageParametersSchedulable,
				 this);
		groundDistanceMonitor.register();
		
		
		LandingGearHandler landingHandler = new LandingGearHandler(new PriorityParameters(5),
				new AperiodicParameters(), storageParametersSchedulable, "Landing Handler", this);

		landingHandler.register();
		

		InstrumentLandingSystemMonitor ilsMonitor = new InstrumentLandingSystemMonitor(
				new PriorityParameters(5),
				new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(10, 0)),
				storageParametersSchedulable,
				"ILS Monitor",
				 this);
		ilsMonitor.register();
		
		SafeLandingHandler safeLandingHandler = new SafeLandingHandler(
		new PriorityParameters(5),
		new AperiodicParameters(),
		storageParametersSchedulable, 
		"Safe Landing Handler",
		this,
		SAFE_LANDING_ALTITUDE);
		
		safeLandingHandler.register();
	}

	/**
	 * Returns the size of this mission's memory 
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	/**
	 * called when landing gear is deployed,
	 * sets <code>LandingGearDeployed</code> to <code>true</code>
	 */
	public synchronized void deployLandingGear()
	{
		landingGearDeployed = true;
		
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

	public MainMission getControllingMission() {
		return controllingMission;
	}

	public void abort() 
	{
		abort = true;		
	}
	
	@Override
	public boolean cleanUp()
	{
		System.out.println("Land Mission Cleanup, Returning " + abort);
		return !abort;
	}

}
