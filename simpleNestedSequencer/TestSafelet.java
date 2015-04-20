/** Simple Nested Sequencer
 * 
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.simpleNestedSequencer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

import devices.Console;

public class TestSafelet implements Safelet<Mission>
{

	// public static StorageParameters storageParametersSchedulable;
	public static StorageParameters storageParameters_topLevelSequencer;
	public static StorageParameters storageParameters_nestedSequencer;
	public static StorageParameters storageParameters_Schedulable;

	@Override
	public MissionSequencer<Mission> getSequencer()
	{
		Console.println("TestSafelet: getSequencer");

		storageParameters_topLevelSequencer = new StorageParameters(
				1000000,
				new long[] { Const.HANDLER_STACK_SIZE },
				100000, 
				10000 ,
				100000);

		return new MainMissionSequencer(new PriorityParameters(5),
				storageParameters_topLevelSequencer);
	}

	@Override
	public long immortalMemorySize()
	{
		return 100000;
	}

	@Override
	public void initializeApplication()
	{
		Console.println("TestSafelet: Init");
		// storageParameters = new StorageParameters(150 * 1000, new long[] {
		// Const.HANDLER_STACK_SIZE },
		// Const.PRIVATE_MEM_SIZE-25*1000, Const.IMMORTAL_MEM_SIZE-50*1000,
		// Const.MISSION_MEM_SIZE-100*1000);
		//
		// storageParameters_Schedulable = new
		// StorageParameters(Const.PRIVATE_MEM_SIZE-30*1000, new long[] {
		// Const.HANDLER_STACK_SIZE },
		// Const.PRIVATE_MEM_SIZE-30*1000, Const.IMMORTAL_MEM_SIZE-50*1000,
		// Const.MISSION_MEM_SIZE-100*1000);

//		storageParameters_nestedSequencer = new StorageParameters(
//				(Const.BACKING_STORE_SIZE_DEFAULT / 2) + 1000000,
//				new long[] { Const.HANDLER_STACK_SIZE },
//				Const.PRIVATE_MEM_SIZE_DEFAULT, 10000 * 2,
//				Const.MISSION_MEM_SIZE_DEFAULT);
		

		storageParameters_nestedSequencer = new StorageParameters(
				1000000,
		new long[] { Const.HANDLER_STACK_SIZE },
		100000, 
		10000 ,
		0);

		storageParameters_Schedulable = new StorageParameters(
				1000000,
		new long[] { Const.HANDLER_STACK_SIZE },
		10000, 
		10000 ,
		0);

		// storageParametersSchedulable = new StorageParameters(
		// Const.BACKING_STORE_SIZE ,
		// new long[] { Const.HANDLER_STACK_SIZE },
		// Const.PRIVATE_MEM_SIZE_DEFAULT,
		// Const.IMMORTAL_MEM_SIZE_DEFAULT,
		// Const.MISSION_MEM_SIZE_DEFAULT );

		Console.println("TestSafelet: Begin");
	}

}
