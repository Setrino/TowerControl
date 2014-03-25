/*
 * Sergei Kostevitch
 * Apr 24, 2012
 */

package tower;

public class LandingLogic {
	

	private static LandingLogic landingLogicInstance = null;

	private LandingLogic(){
				
	}
	
	public static synchronized LandingLogic getLandingInstance() {

		if (landingLogicInstance == null) {

			landingLogicInstance = new LandingLogic();
		}

		return landingLogicInstance;
	}
}
