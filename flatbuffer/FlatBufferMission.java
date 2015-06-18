package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

import devices.Console;

public class FlatBufferMission extends Mission
{

	private volatile int buffer;

	public FlatBufferMission()
	{
		buffer = 0;
		Console.println("FlatBufferMission");
	}

	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000,
				new long[] { Const.HANDLER_STACK_SIZE },
				Const.PRIVATE_MEM_DEFAULT, Const.IMMORTAL_MEM_DEFAULT,
				Const.MISSION_MEM_DEFAULT - 100 * 1000);

		new Reader(new PriorityParameters(10), storageParameters, this).register();

		new Writer(new PriorityParameters(10), storageParameters, this).register();

		Console.println("FlatBufferMission init");
	}

	public boolean bufferEmpty(String name)
	{
		Console.println(name + " Checking Buffer Empty");
		return buffer == 0;
	}

	public synchronized void write(int update)
	{
		Console.println("writing " + update + " to Buffer");
		buffer = update;
		this.notify();
	}

	public synchronized int read()
	{
		int out = buffer;
		Console.println("Reading " + out + " from Buffer");
		buffer = 0;
		this.notify();
		
		return out;
	}

	public synchronized void waitOnMission(String name)
			throws InterruptedException
	{
		Console.println(name + " Waiting on Mission");
		this.wait();
	}

	public long missionMemorySize()
	{
		return 1048576;
	}
}
