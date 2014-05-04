/**
 * 
 */
package seprini.controllers.components;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.ATC;
import seprini.data.GameDifficulty;

/**
 * @author Leslie
 * 
 */
public class FlightPlanComponentTest {

	FlightPlanComponent flightPlanComponent;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ATC atc = new ATC();
		atc.showGameScreen(GameDifficulty.MEDIUM);
		// WaypointComponent waypointComponent = new WaypointComponent();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.FlightPlanComponent#FlightPlanComponent(seprini.controllers.components.WaypointComponent)}
	 * .
	 */
	@Test
	public void testFlightPlanComponent() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.FlightPlanComponent#generate()}.
	 */
	@Test
	public void testGenerate() {

	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.FlightPlanComponent#generate(seprini.models.Waypoint)}
	 * .
	 */
	@Test
	public void testGenerateWaypoint() {
		fail("Not yet implemented");
	}

}
