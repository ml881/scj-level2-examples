package flatbufferFault;

import javax.safetycritical.LaunchLevel2;

import devices.Console;

// This is with the new version, linked source, from the hvm-scj git...slightly different, should probably migrate to.


//Application entry point, runs the Safelet
public class FlatBufferSafeletLauncher
{
	public static void main(String[] args)
	{
		Console.println("FlatBufferSafeletExecuter ");
		
		// Run the safelet which starts the whole application
//		new FlatBufferSafeletLauncher(new FlatBuffer()).run();
		
		new LaunchLevel2(new FlatBuffer());
		
		
		Console.println("FlatBufferSafeletExecuter Finished");
	}
}
