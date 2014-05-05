/**
 * 
 */
package seprini.controllers.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.controllers.AircraftController;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Airspace;
import seprini.models.Waypoint;

/**
 * @author Leslie
 * 
 */
public class FlightPlanComponentTest {

	FlightPlanComponent flightPlanComponent;
	WaypointComponent waypointComponent;

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
		Airspace airspace = new Airspace();
		AircraftController aircraftController = new AircraftController(
				GameDifficulty.MEDIUM, airspace);
		waypointComponent = new WaypointComponent(aircraftController,
				GameMode.SINGLE);

		flightPlanComponent = new FlightPlanComponent(waypointComponent);
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
		ArrayList<Waypoint> flightPlan = flightPlanComponent.generate();

		assertTrue(waypointComponent.getEntryList().contains(flightPlan.get(0)));
		assertTrue(waypointComponent.getExitList().contains(
				flightPlan.get(flightPlan.size() - 1)));
		assertTrue(flightPlan.size() > 2);

	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.FlightPlanComponent#generate(seprini.models.Waypoint)}
	 * .
	 */
	@Test
	public void testGenerateWaypoint() {
		ArrayList<Waypoint> flightPlan = flightPlanComponent
				.generate(waypointComponent.getEntryList().get(0));

		assertEquals(waypointComponent.getEntryList().get(0), flightPlan.get(0));
		assertTrue(waypointComponent.getExitList().contains(
				flightPlan.get(flightPlan.size() - 1)));
		assertTrue(flightPlan.size() > 2);
	}

}
