/** Spacecraft - Mode Change Example
 *
 * This is the main mission, it represents the Spacecraft.
 * It loads the persistent handlers and the sequencer for the modes.
 *
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.scj.util.Const;

import devices.Console;

public class MainMission extends Mission
{
	/**
	 * Initilises the Mission, loading the ModeChanger and the persistent
	 * handlers
	 */
	@Override
	protected void initialize()
	{
		Console.println("Main Mission: Init ");

		

		// Load the submission sequencer and persistent handlers
		SPModeChanger sPModeChanger = new SPModeChanger(new PriorityParameters(5),
				SPSafelet.storageParameters_nestedSequencer, this);

		sPModeChanger.register();

		

		EnvironmentMonitor environmentMonitor = new EnvironmentMonitor(
				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
						new RelativeTime(1000, 0)),
				SPSafelet.storageParameters_Schedulable, this);
		environmentMonitor.register();

		

		ControlHandler controlHandler = new ControlHandler(
				new PriorityParameters(5), new AperiodicParameters(),
				SPSafelet.storageParameters_Schedulable);
		controlHandler.register();

		

//		AperiodicSimulator controlSim = new AperiodicSimulator(
//				new PriorityParameters(5), new PeriodicParameters(new RelativeTime(0,0),
//						new RelativeTime(100, 0)),
//				SPSafelet.storageParameters_Schedulable, controlHandler);
//		controlSim.register();
		
		Console.println("Main Mission: Begin ");
	}

	/**
	 * Returns the required size of this Mission's private memory
	 */
	@Override
	public long missionMemorySize()
	{
		return Const.MISSION_MEM_SIZE_DEFAULT;
	}

	public void environmentBad()
	{
		// This would cause the system to check and attempt to remedy the bad
		// internal environment
		Console.println("Envrionment Bad");

	}

}
