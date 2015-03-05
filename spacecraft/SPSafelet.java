/** Spacecraft - Mode Change Example
 * 
 * This safelet is the top level of the application and loads the main mission sequencer
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

import devices.Console;

public class SPSafelet implements Safelet<Mission>
{

//	public static StorageParameters storageParametersSchedulable;
	public static StorageParameters storageParameters_topLevelSequencer;
	public static StorageParameters storageParameters_nestedSequencer;
	public static StorageParameters storageParameters_Schedulable;

	
	@Override
	public MissionSequencer<Mission> getSequencer()
	{		
		//TODO FIx Memory Parameters
		 storageParameters_topLevelSequencer = 
				 new StorageParameters(
						 Const.OUTERMOST_SEQ_BACKING_STORE_DEFAULT , 
						 new long[] { Const.HANDLER_STACK_SIZE },
						 Const.PRIVATE_MEM , 
						 10000*2, 
						 Const.MISSION_MEM);
		 
		 
		 storageParameters_nestedSequencer = 
				 new StorageParameters( 
						 Const.OVERALL_BACKING_STORE- Const.OUTERMOST_SEQ_BACKING_STORE_DEFAULT , 
						 new long[] { Const.HANDLER_STACK_SIZE },
						 Const.PRIVATE_MEM,
						 10000*2, 
						 Const.MISSION_MEM);
		
		 storageParameters_Schedulable = 
				 new StorageParameters(
						 Const.PRIVATE_BACKING_STORE_DEFAULT, 
						 new long[] { Const.HANDLER_STACK_SIZE },
						 Const.PRIVATE_MEM ,
						 10000, 	
						 Const.MISSION_MEM);
		
		return new MainMissionSequencer(new PriorityParameters(5),
				storageParameters_topLevelSequencer);	
	}

	@Override
	public long immortalMemorySize()
	{          
		return Const.IMMORTAL_MEM_DEFAULT;
	}

	@Override
	public void initializeApplication()
	{
		Console.println("SPSafelet: Init");

		//Apparently this is never called.	
		 
		 Console.println("SPSafelet: Begin");
	}

}
