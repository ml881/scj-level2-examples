/** Aircraft - LandingGearHandler
 *  
 * 	Marker interface for missions that use the LandingGearHandler 
 */
package aircraft;

public interface LandingGearUser {
	
	/**
	 * Is the landing gear deployed?
	 */
	boolean landingGearDeployed = false;
	
	public void deployLandingGear();
	
	public void stowLandingGear();
	
	public boolean isLandingGearDeployed();

}
