package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.*;
import javax.safetycritical.annotate.Level;
import javax.scj.util.Const;

public class FlatBuffer implements Safelet<Mission>
{
	
			public Level getLevel()
	{
		return Level.LEVEL_2;
	}

	public MissionSequencer<Mission> getSequencer()
	{
		System.out.println("FlatBuffer");
		//Create and return the main mission sequencer
		StorageParameters storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_DEFAULT-25*1000, Const.IMMORTAL_MEM_DEFAULT-50*1000, Const.MISSION_MEM_DEFAULT-100*1000);
		
		return new FlatBufferMissionSequencer(new PriorityParameters(5), storageParameters);
	}


	@Override
	public long immortalMemorySize()
	{		
		return Const.IMMORTAL_MEM_DEFAULT;
	}

	@Override
	public void initializeApplication()
	{		
		
	} 
}
