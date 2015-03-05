package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

public class FlatBufferMission extends Mission
{
	private volatile int[] buffer;
	private Writer writer;
	private Reader reader;

	public FlatBufferMission()
	{
		buffer = new int[1];
		buffer[0] = 0;

		System.out.println("FlatBufferMission");
	}

	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(1048576,
				new long[] { Const.HANDLER_STACK_SIZE }, 1048576, 1048576,
				Const.MISSION_MEM_DEFAULT - 100 * 1000);

		reader = new Reader(new PriorityParameters(5), storageParameters, this,
				writer);

		writer = new Writer(new PriorityParameters(5), storageParameters, this,
				reader);

		System.out.println("FlatBufferMission init");
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
