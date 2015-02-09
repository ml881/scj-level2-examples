/** Spacecraft - Mode Change Example
 * 
 * 	This is the mode changer for the Spacecraft application,
 * 	it controls which mode the application is in
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class SPModeChanger extends MissionSequencer<Mission> implements
		ModeChanger
{
	/**
	 * This variable represents the number of modes this ModeChanger has to deal
	 * with
	 */
	private int modesLeft = 3;
	/**
	 * A reference to a mode
	 */
	private Mode currentMode, launchMode, cruiseMode, landMode;

	/**
	 * The controlling mission
	 */
	private MainMission controllingMission;
	
	/**
	 * Class constructor
	 * 
	 * @param priority
	 *            the priority parameters for this mission sequencer
	 * @param storage
	 *            the storage parameters for this mission sequencer
	 */
	public SPModeChanger(PriorityParameters priority, StorageParameters storage, MainMission controllingMission)
	{
		super(priority, storage);
		Console.println("Mode Changer: Construct ");
		
		launchMode = new LaunchMission();
		cruiseMode = new CruiseMission();
		landMode = new LandMission();

		this.controllingMission = controllingMission;
	}

	/**
	 * Change the mode to given mode
	 */
	@Override
	public synchronized void changeTo(Mode newMode)
	{
		currentMode = newMode;

	}

	/**
	 * Advance the mode to the next mode
	 */
	@Override
	public synchronized void advanceMode()
	{
		Console.println("Mode Changer: Advance To Next Mode");
		// check the value of the modes variable and changeTo the associated
		// mode
		// once all the missions have been run, changeTo null to terminate the
		// sequencer
		if (modesLeft == 3)
		{
			modesLeft--;
			changeTo(launchMode);
		}
		else if (modesLeft == 2)
		{
			modesLeft--;
			changeTo(cruiseMode);
		}
		else if (modesLeft == 1)
		{
			modesLeft--;
			changeTo(landMode);
		}
		else
		{
			changeTo(null);
			Console.println("Mode Changer: FINISHED");
			controllingMission.requestTermination();
		}
	}

	/**
	 * return the <code>currentMode</code> which has been set by either
	 * <code>advanceMode</code> or <code>changeTo</code>
	 */
	@Override
	protected Mission getNextMission()
	{
		Console.println("Mode Changer: getNextMission ");
		advanceMode();
		return (Mission) currentMode;
	}

}
