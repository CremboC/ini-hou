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

import seprini.controllers.AircraftController;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Airspace;

/**
 * @author Leslie
 * 
 */
public class WaypointComponentTest {

	WaypointComponent singlePlayerWaypointComponent;
	WaypointComponent multiPlayerWaypointComponent;

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
		singlePlayerWaypointComponent = new WaypointComponent(
				aircraftController, GameMode.SINGLE);
		singlePlayerWaypointComponent = new WaypointComponent(
				aircraftController, GameMode.MULTI);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#WaypointComponent(seprini.controllers.AircraftController, seprini.data.GameMode)}
	 * .
	 */
	@Test
	public void testWaypointComponent() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#singleplayerWaypoints()}
	 * .
	 */
	@Test
	public void testSingleplayerWaypoints() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#multiplayerWaypoints()}
	 * .
	 */
	@Test
	public void testMultiplayerWaypoints() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#createWaypoint(float, float, boolean)}
	 * .
	 */
	@Test
	public void testCreateWaypoint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getPermanentList()}
	 * .
	 */
	@Test
	public void testGetPermanentList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getEntryList()}.
	 */
	@Test
	public void testGetEntryList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getExitList()}.
	 */
	@Test
	public void testGetExitList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getAirportList()}
	 * .
	 */
	@Test
	public void testGetAirportList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getSelectedAirport()}
	 * .
	 */
	@Test
	public void testGetSelectedAirport() {
		fail("Not yet implemented");
	}

}
