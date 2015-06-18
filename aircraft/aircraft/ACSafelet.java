/** Aircraft - Mode Change Example
* 
* This safelet is the top level of the application and loads the main mission sequencer
* 
*   @author Matt Luckcuck <ml881@york.ac.uk>
*/
package aircraft;


import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

public class ACSafelet implements Safelet<Mission>
{
	@Override
	public MissionSequencer<Mission> getSequencer()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_SIZE-25*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
		
		return new MainMissionSequencer(new PriorityParameters(5),storageParameters);
	}

	@Override
	public long immortalMemorySize()
	{
		return Const.IMMORTAL_MEM_SIZE;
	}

	@Override
	public void initializeApplication()
	{			
	} 
}