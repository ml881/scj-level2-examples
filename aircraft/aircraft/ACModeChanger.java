/** Aircraft - Mode Change Example
* 
* 	This is the mode changer for the Aircraft application,
* 	it controls which mode the application is in
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import modechange.Mode;
import modechange.ModeChanger;

public class ACModeChanger extends MissionSequencer<ModeMission> implements ModeChanger
{
	private MainMission controllingMission;
	
	public ACModeChanger(PriorityParameters priority, StorageParameters storage,MainMission controllingMission)
	{
		super(priority, storage);
		this.controllingMission = controllingMission;
	}
	
	/**
	 * This variable represents the number of modes this ModeChanger
	 * has to deal with
	 */
	private int modesLeft=3;
	/**
	 * A reference to a mode
	 */
	private Mode currentMode, launchMode, cruiseMode, landMode;

	/**
	 * Class constructor
	 * @param priority	the priority parameters for this mission sequencer	
	 * @param storage	the storage parameters for this mission sequencer
	 */
	public ACModeChanger(PriorityParameters priority,
			StorageParameters storage)
	{
		super(priority, storage);
		launchMode = (Mode) new TakeOffMission(controllingMission);
		cruiseMode = (Mode) new CruiseMission(controllingMission);		
		landMode   = (Mode) new LandMission(controllingMission);
	
	}

	/**
	 * Change the mode to given mode
	 */
	@Override
	public void changeTo(Mode newMode)
	{
		currentMode = newMode;		
	}

	/**
	 * Advance the mode to the next mode
	 */
	public synchronized void advanceMode()
	{
			System.out.println("Advance To Next Mode");
		//check the value of the modes variable and changeTo the associated mode
		//once all the missions have been run, changeTo null to terminate the sequencer
		if(modesLeft == 3)
		{
			modesLeft--;			
			changeTo(launchMode) ;
		}
		else if(modesLeft == 2)
		{
			modesLeft--;
			changeTo(cruiseMode);			
		}
		else if(modesLeft == 1)
		{
			modesLeft--;			
			changeTo(landMode);
		}
		else
		{
			changeTo(null);
		}
	}

	/**
	 * return the <code>currentMode</code> which has been set by either
	 * <code>advanceMode</code> or <code>changeTo</code>
	 */
	@Override
	protected ModeMission getNextMission()
	{	
		return (ModeMission)currentMode;
	}

		

}
