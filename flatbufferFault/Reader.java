package flatbufferFault;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class Reader extends ManagedThread
{
	private final FlatBufferMission fbMission;

	public Reader(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission)
	{
		super(priority, storage);

		this.fbMission = fbMission;
	}

	public void run()
	{
		Console.println("Reader!");

		while (!fbMission.terminationPending())
		{
			
				while (fbMission.buffer.bufferEmpty("Reader"))
				{
					fbMission.buffer.waitOnBuffer("Reader");
				}

				Console.println("Reader Read " + fbMission.buffer.read()
						+ " from Buffer");			

		}
	}
}
