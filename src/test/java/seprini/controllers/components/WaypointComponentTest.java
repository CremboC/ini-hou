/**
 * 
 */
package seprini.controllers.components;

import static org.junit.Assert.assertEquals;

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
		multiPlayerWaypointComponent = new WaypointComponent(
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
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#multiplayerWaypoints()}
	 * .
	 */
	@Test
	public void testMultiplayerWaypoints() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#createWaypoint(float, float, boolean)}
	 * .
	 */
	@Test
	public void testCreateWaypoint() {
		assertEquals(singlePlayerWaypointComponent.getPermanentList().size(), 8);

		singlePlayerWaypointComponent.createWaypoint(300, 300, true);

		assertEquals(singlePlayerWaypointComponent.getPermanentList().size(), 9);

		assertEquals(multiPlayerWaypointComponent.getPermanentList().size(), 8);

		multiPlayerWaypointComponent.createWaypoint(300, 300, true);

		assertEquals(multiPlayerWaypointComponent.getPermanentList().size(), 9);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getPermanentList()}
	 * .
	 */
	@Test
	public void testGetPermanentList() {
		assertEquals(singlePlayerWaypointComponent.getPermanentList().size(), 8);
		assertEquals(multiPlayerWaypointComponent.getPermanentList().size(), 8);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getEntryList()}.
	 */
	@Test
	public void testGetEntryList() {
		assertEquals(singlePlayerWaypointComponent.getEntryList().size(), 4);
		assertEquals(multiPlayerWaypointComponent.getEntryList().size(), 4);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getExitList()}.
	 */
	@Test
	public void testGetExitList() {
		assertEquals(singlePlayerWaypointComponent.getExitList().size(), 6);
		assertEquals(multiPlayerWaypointComponent.getExitList().size(), 6);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getAirportList()}
	 * .
	 */
	@Test
	public void testGetAirportList() {
		assertEquals(singlePlayerWaypointComponent.getAirportList().size(), 2);
		assertEquals(multiPlayerWaypointComponent.getAirportList().size(), 2);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.WaypointComponent#getSelectedAirport()}
	 * .
	 */
	@Test
	public void testGetSelectedAirport() {
		assertEquals(singlePlayerWaypointComponent.getSelectedAirport(), null);
		assertEquals(multiPlayerWaypointComponent.getSelectedAirport(), null);
	}

}
