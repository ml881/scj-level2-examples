/** Aircraft - Mode Change Example
* 
* 	The main mission sequencer for the application
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

public class MainMissionSequencer extends MissionSequencer<Mission>
{	
	/**
	 * Has this single mission been returned?
	 */
	private boolean returnedMission;

	/**
	 * Class Constructor 
	 * @param priority the PriorityParameters for the sequencer
	 * @param storage the StorageParameters for the sequencer 
	 */
	public MainMissionSequencer(PriorityParameters priority,
			StorageParameters storage)
	{
		super(priority, storage);		
		returnedMission = false;
	}

	/**
	 * Returns the new mission
	 */
	@Override
	protected Mission getNextMission()
	{
		//This returns the main mission once only
		if(!returnedMission)
		{
			returnedMission = true;
			return new MainMission();
		}
		else
		{
			return null;
		}
	}

}
