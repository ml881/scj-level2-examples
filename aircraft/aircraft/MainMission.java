/** Aircraft - Mode Change Example
*
* This is the main mission, it represents the Aircraft.
* It loads the persistent schedulables and the sequencer for the modes.
*
*   @author Matt Luckcuck <ml881@york.ac.uk> 
*/
package aircraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

public class MainMission extends Mission
{
	/**
	 * The read that the sensors will get when the aircraft is on the ground
	 */
	final double ALTITUDE_READING_ON_GROUND = 0.0;
	
	//Evn Mon Variables
	private double cabinPressure;
	private double emergencyOxygen;
	private double fuelRemaining; 
	
	
	private double altitude;
	private double airSpeed;
	private double heading;
	
    /**
    *   Initialises the Mission, loading the ModeChanger and the persistent schedulables
    */
	@Override
	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-25*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
				
		StorageParameters storageParametersSchedulable= new StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);

		//Load the submission sequencer and persistent handlers
		ACModeChanger aCModeChanger= new ACModeChanger(new PriorityParameters(5), storageParameters, this );
		
		aCModeChanger.register();

		EnvironmentMonitor environmentMonitor = new EnvironmentMonitor(new PriorityParameters(5), 
				new PeriodicParameters(new RelativeTime(10, 0), null),
				storageParametersSchedulable,"Environment Monitor", this);

		environmentMonitor.register();
		
		ControlHandler controlHandler = new ControlHandler(new PriorityParameters(5),
            					new AperiodicParameters(), 
            					storageParametersSchedulable,"Control Handler");
		
		controlHandler.register();
		
		FlightSensorsMonitor flightSensMon = new FlightSensorsMonitor(new PriorityParameters(5), 
				new PeriodicParameters(new RelativeTime(10, 0), null),
				storageParametersSchedulable,"Flight Sensors Monitor", this);
		
		flightSensMon.register();
		
		CommunicationsHandler commsHandler = new CommunicationsHandler(new PriorityParameters(5),
				new AperiodicParameters(), 
				storageParametersSchedulable,"Communications Handler");
		
		commsHandler.register();
		
		AperiodicSimulator controlSim = new AperiodicSimulator(new PriorityParameters(5), 
				new PeriodicParameters(new RelativeTime(10, 0), null),
				storageParametersSchedulable,controlHandler);
		
		controlSim.register();
		
		AperiodicSimulator commsSim = new AperiodicSimulator(new PriorityParameters(5), 
				new PeriodicParameters(new RelativeTime(10, 0), null),
				storageParametersSchedulable,commsHandler);
		
		commsSim.register();
	}
    
    /**
    * Returns the required size of this Mission's private memory
    */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	public double getCabinPressure() {
		return cabinPressure;
	}

	public void setCabinPressure(double cabinPressure) {
		this.cabinPressure = cabinPressure;
	}

	public double getFuelRemaining() {
		return fuelRemaining;
	}

	public void setFuelRemaining(double fuelRemaining) {
		this.fuelRemaining = fuelRemaining;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getAirSpeed() {
		return airSpeed;
	}

	public void setAirSpeed(double airSpeed) {
		this.airSpeed = airSpeed;
	}

	public double getEmergencyOxygen() {
		return emergencyOxygen;
	}

	public void setEmergencyOxygen(double emergencyOxygen) {
		this.emergencyOxygen = emergencyOxygen;
	}

	public double getHeading() 
	{
		return heading;		
	}

	public void setHeading(double heading) 
	{
		this.heading = heading;		
	}
 
}
