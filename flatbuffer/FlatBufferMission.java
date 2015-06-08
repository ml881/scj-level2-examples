package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

import devices.Console;

public class FlatBufferMission extends Mission
{
	private volatile int[] buffer;

	public FlatBufferMission()
	{
		buffer = new int[1];
		buffer[0] = 0;

		Console.println("FlatBufferMission");
	}

	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
				 Const.PRIVATE_MEM_DEFAULT, Const.IMMORTAL_MEM_DEFAULT, Const.MISSION_MEM_DEFAULT-100*1000);

		new Reader(new PriorityParameters(5), storageParameters, this).register();
		

		new Writer(new PriorityParameters(5), storageParameters, this).register();

		Console.println("FlatBufferMission init");
	}

	public boolean bufferEmpty()
	{
		return buffer[0] == 0;
	}

	public synchronized void write(int update)
	{
		buffer[0] = update;
	}

	public synchronized int read()
	{
		int out = buffer[0];
		this.buffer[0] = 0;

		return out;
	}
	
	public synchronized void waitOnMission() throws InterruptedException
	{
		this.wait();
	}
	
	public synchronized void notifyOnMission() throws InterruptedException
	{
		this.notify();
	}

	public long missionMemorySize()
	{
		return 1048576;
	}
}
