package scjlevel2examples.flatbuffer;

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
			try
			{
				while (!fbMission.bufferEmpty("Writer"))
				{
					fbMission.waitOnMission("Writer");
				}

				fbMission.write(i);
				i++;

			} catch (InterruptedException ie)
			{
				// Handle Interruption
			}
		}

	}
}
