/** Spacecraft - Mode Change Example
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.safetycritical.LaunchLevel2;
import javax.scj.util.Const;

import devices.Console;

public class SPSafeletExecuter
{
	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
		Console.println("Launcher");
		Const.OVERALL_BACKING_STORE = 16000000;

		// new SPSafeletExecuter(new SPSafelet()).run();
		new LaunchLevel2(new SPSafelet());
	}
}
