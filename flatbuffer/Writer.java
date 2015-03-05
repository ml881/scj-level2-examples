package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

public class Writer extends ManagedThread
{
	private final FlatBufferMission fbMission;
	private final Reader reader;
	private int i=1;

	public Writer(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission, Reader reader)
	{
		super(priority, storage);

		this.fbMission = fbMission;
		this.reader = reader;
	}



	public synchronized void run()
	{
		System.out.println("Writer!");

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
			}
			catch (InterruptedException ie)
			{
				//Handle Interruption	
			}
		}

	}
}
