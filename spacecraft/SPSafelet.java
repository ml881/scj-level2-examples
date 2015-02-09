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
		Console.println("SPSafelet: getSequencer");
		
		return new MainMissionSequencer(new PriorityParameters(5),
				storageParameters_topLevelSequencer);	}

	@Override
	public long immortalMemorySize()
	{          
		return Const.IMMORTAL_MEM_SIZE_DEFAULT;
	}

	@Override
	public void initializeApplication()
	{
		Console.println("SPSafelet: Init");
//		 storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
//				 Const.PRIVATE_MEM_SIZE-25*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
//				
//		storageParameters_Schedulable = new StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
//				 Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000, Const.MISSION_MEM_SIZE-100*1000);
		
		 storageParameters_topLevelSequencer = new StorageParameters(Const.BACKING_STORE_SIZE-1000000 , new long[] { Const.HANDLER_STACK_SIZE },
				Const.PRIVATE_MEM_SIZE_DEFAULT , 10000*2, Const.MISSION_MEM_SIZE_DEFAULT);
		 
		 storageParameters_nestedSequencer = new StorageParameters( (Const.BACKING_STORE_SIZE/2)+1000000, new long[] { Const.HANDLER_STACK_SIZE },
					Const.PRIVATE_MEM_SIZE_DEFAULT , 10000*2, Const.MISSION_MEM_SIZE_DEFAULT);
		
		 storageParameters_Schedulable = new StorageParameters(Const.BACKING_STORE_SIZE/10, new long[] { Const.HANDLER_STACK_SIZE },
					Const.PRIVATE_MEM_SIZE_DEFAULT , 10000, Const.MISSION_MEM_SIZE_DEFAULT);
		
		
		
//		storageParametersSchedulable = new StorageParameters(
//				Const.BACKING_STORE_SIZE ,
//				new long[] { Const.HANDLER_STACK_SIZE },
//				Const.PRIVATE_MEM_SIZE_DEFAULT,
//				Const.IMMORTAL_MEM_SIZE_DEFAULT,
//				Const.MISSION_MEM_SIZE_DEFAULT  );
		 
		 Console.println("SPSafelet: Begin");
	}

}
