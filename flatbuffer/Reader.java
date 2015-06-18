package scjlevel2examples.flatbuffer;

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

	public synchronized void notifyReader()
	{
		notify();
	}

	public void run()
	{
		Console.println("Reader!");

		while (!fbMission.terminationPending())
		{
			try
			{
				while (fbMission.bufferEmpty("Reader"))
				{
					fbMission.waitOnMission("Reader");
				}

				Console.println("Reader Read " + fbMission.read()
						+ " from Buffer");	

			} catch (InterruptedException ie)
			{
				// Handle Interruption
			}

		}
	}
}
