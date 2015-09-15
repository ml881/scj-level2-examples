package flatbuffer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class Writer extends ManagedThread
{
	private final FlatBufferMission fbMission;
	private int i = 1;

	public Writer(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission)
	{
		super(priority, storage);

		this.fbMission = fbMission;
	}

	public void run()
	{
		Console.println("Writer!");

		while (!fbMission.terminationPending())
		{
		
				while (!fbMission.buffer.bufferEmpty("Writer"))
				{
					fbMission.buffer.waitOnBuffer("Writer");
				}

				fbMission.buffer.write(i);
				i++;
				
				if(i >= 5 )
				{
					fbMission.requestTermination();
				}

				
			
		}

	}
}
