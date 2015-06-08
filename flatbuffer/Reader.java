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

	public synchronized void run()
	{
		Console.println("Reader!");

		while (!fbMission.terminationPending())
		{
			try
			{
				while (fbMission.bufferEmpty())
				{
					fbMission.waitOnMission();
				}

				Console.println("I Read: " + fbMission.read());

				fbMission.notifyOnMission();
			}
			catch (InterruptedException ie)
			{
				//Handle Interruption
			}

		}
	}
}
