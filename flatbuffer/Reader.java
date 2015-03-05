package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

public class Reader extends ManagedThread
{
	private final Writer writer;
	private final FlatBufferMission fbMission;

	public Reader(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission, Writer writer)
	{
		super(priority, storage);

		this.fbMission = fbMission;
		this.writer = writer;
	}

	public synchronized void notifyReader()
	{
		notify();
	}

	public synchronized void run()
	{
		System.out.println("Reader!");

		while (!fbMission.terminationPending())
		{
			try
			{
				while (fbMission.bufferEmpty())
				{
					fbMission.waitOnMission();
				}

				System.out.println("I Read: " + fbMission.read());

				fbMission.notifyOnMission();
			}
			catch (InterruptedException ie)
			{
				//Handle Interruption
			}

		}
	}
}
