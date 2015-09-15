package flatbufferFault;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

import devices.Console;

public class FlatBufferMission extends Mission
{
	Buffer buffer;

	public FlatBufferMission()
	{
		Console.println("FlatBufferMission");
	}

	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000,
				new long[] { Const.HANDLER_STACK_SIZE },
				Const.PRIVATE_MEM_DEFAULT, Const.IMMORTAL_MEM_DEFAULT,
				Const.MISSION_MEM_DEFAULT - 100 * 1000);

		new Reader(new PriorityParameters(10), storageParameters, this).register();

		new Writer(new PriorityParameters(10), storageParameters, this).register();
		
		buffer = new Buffer();

		Console.println("FlatBufferMission init");
	}
	
	public boolean cleanUp()
	{
		Console.print("FlatBufferMission Cleanup");
		return false;
	}

	public long missionMemorySize()
	{
		return 1048576;
	}
}
