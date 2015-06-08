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

	public synchronized void run()
	{
		Console.println("Writer!");

		while (!fbMission.terminationPending())
		{
			try
			{
				while (!fbMission.bufferEmpty())
				{
					fbMission.waitOnMission();
				}

				fbMission.write(i);
				i++;

				fbMission.notifyOnMission();
			} catch (InterruptedException ie)
			{
				// Handle Interruption
			}
		}

	}
}
