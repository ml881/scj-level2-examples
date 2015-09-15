package flatbufferFault;

import javax.safetycritical.Services;

import devices.Console;

public class Buffer {
	
	private volatile int buffer;
	
	public Buffer(){
		buffer = 0;
		Services.setCeiling(this, 20);
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

	public synchronized void waitOnBuffer(String name)
			
	{
		Console.println(name + " Waiting on Buffer");
		try
		{
			this.wait();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
