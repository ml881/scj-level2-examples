/** Spacecraft - Mode Change Example
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.safetycritical.Launcher;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;
import javax.scj.util.Const;

import devices.Console;

public class SPSafeletExecuter extends Launcher
{
	/**
	 * Class Constructor
	 * 
	 * @param arg0
	 *            The Safelet to execute
	 */
	public SPSafeletExecuter(Safelet<Mission> safelet)
	{
		super(safelet, 2);
	}

	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
		Console.println("Launcher");
		Const.OVERALL_BACKING_STORE= 16000000;
		
		new SPSafeletExecuter(new SPSafelet()).run();
	}
}
