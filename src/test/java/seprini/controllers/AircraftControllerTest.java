/**
 * 
 */
package seprini.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.Waypoint;
import seprini.models.types.Player;

/**
 * @author Leslie
 * 
 */
public class AircraftControllerTest {

	AircraftController aircraftController;
	Airspace airspace;

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
		airspace = new Airspace();
		aircraftController = new AircraftController(GameDifficulty.MEDIUM,
				airspace);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#AircraftController(seprini.data.GameDifficulty, seprini.models.Airspace, seprini.screens.ScreenBase)}
	 * .
	 */
	@Test
	public void testAircraftController() {
	}

	/**
	 * Test method for {@link seprini.controllers.AircraftController#init()}.
	 */
	@Test
	public void testInit() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#update(float)}.
	 */
	@Test
	public void testUpdate() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#collisionHasOccured(seprini.models.Aircraft, seprini.models.Aircraft)}
	 * .
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCollisionHasOccured() throws InterruptedException {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#generateAircraft()}.
	 */
	@Test
	public void testGenerateAircraft() {
		Aircraft testAircraft = aircraftController.generateAircraft();

		assertTrue(testAircraft instanceof Aircraft);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#removeAircraft(int)}.
	 */
	@Test
	public void testRemoveAircraft() {
		Aircraft aircraftA = aircraftController.generateAircraft();
		Aircraft aircraftB = aircraftController.generateAircraft();
		Aircraft aircraftC = aircraftController.generateAircraft();

		assertEquals(aircraftController.aircraftList.size(), 3, 0);
		assertTrue(aircraftController.aircraftList.get(1) == aircraftB);

		aircraftController.removeAircraft(1);

		assertEquals(aircraftController.aircraftList.size(), 2, 0);
		assertFalse(aircraftController.aircraftList.get(1) == aircraftB);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#selectAircraft(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testSelectAircraft() {
		Aircraft aircraftA = aircraftController.generateAircraft();
		Aircraft aircraftB = aircraftController.generateAircraft();

		assertEquals(aircraftController.getSelectedAircraft(), null);

		aircraftController.selectAircraft(aircraftA);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftA);
		assertFalse(aircraftController.getSelectedAircraft() == aircraftB);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#switchAircraft(int)}.
	 */
	@Test
	public void testSwitchAircraft() {
		Aircraft aircraftA = aircraftController.generateAircraft();
		Aircraft aircraftB = aircraftController.generateAircraft();
		Aircraft aircraftC = aircraftController.generateAircraft();

		assertEquals(aircraftController.getSelectedAircraft(), null);

		aircraftController.switchAircraft(0);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftB);

		aircraftController.switchAircraft(0);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftC);

		aircraftController.switchAircraft(0);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftA);

		aircraftController.switchAircraft(0);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftB);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#redirectAircraft(seprini.models.Waypoint)}
	 * .
	 */
	@Test
	public void testRedirectAircraft() {
		Aircraft aircraftA = aircraftController.generateAircraft();

		Waypoint previousWaypoint = aircraftA.getFlightPlan().get(0);

		aircraftController.selectAircraft(aircraftA);

		if (previousWaypoint != aircraftController.waypoints.getEntryList()
				.get(0)) {
			aircraftController.redirectAircraft(aircraftController.waypoints
					.getEntryList().get(0));
		} else {
			aircraftController.redirectAircraft(aircraftController.waypoints
					.getEntryList().get(1));
		}

		Waypoint newWaypoint = aircraftA.getFlightPlan().get(0);
		;

		assertFalse(previousWaypoint == newWaypoint);
	}

	/**
	 * Test method for {@link seprini.controllers.AircraftController#getTimer()}
	 * .
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testGetTimer() throws InterruptedException {
		assertEquals(aircraftController.getTimer(), 0f, 0);

		aircraftController.update(0.3f);

		assertEquals(aircraftController.getTimer(), 0.3f, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getPlayerScore()}.
	 */
	@Test
	public void testGetPlayerScore() {
		assertEquals(aircraftController.getPlayerScore(), 0, 0);

		Aircraft aircraftA = aircraftController.generateAircraft();

		aircraftController.incrementScore(aircraftA);

		assertEquals(aircraftController.getPlayerScore(), 20, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getSelectedAircraft()}.
	 */
	@Test
	public void testGetSelectedAircraft() {
		Aircraft aircraftA = aircraftController.generateAircraft();
		Aircraft aircraftB = aircraftController.generateAircraft();

		assertEquals(aircraftController.getSelectedAircraft(), null);

		aircraftController.selectAircraft(aircraftA);

		assertEquals(aircraftController.getSelectedAircraft(), aircraftA);
		assertFalse(aircraftController.getSelectedAircraft() == aircraftB);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getAircraftList()}.
	 */
	@Test
	public void testGetAircraftList() {
		ArrayList<Aircraft> testAircraftList = new ArrayList<Aircraft>();

		assertEquals(aircraftController.getAircraftList(), testAircraftList);

		Aircraft aircraftA = aircraftController.generateAircraft();
		Aircraft aircraftB = aircraftController.generateAircraft();

		testAircraftList.add(aircraftA);
		testAircraftList.add(aircraftB);

		assertEquals(aircraftController.getAircraftList(), testAircraftList);

	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getAirspace()}.
	 */
	@Test
	public void testGetAirspace() {
		assertEquals(aircraftController.getAirspace(), airspace);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#allowRedirection()}.
	 */
	@Test
	public void testAllowRedirection() {
		assertFalse(aircraftController.allowRedirection());

		aircraftController.setAllowRedirection(true);

		assertTrue(aircraftController.allowRedirection());

		aircraftController.setAllowRedirection(false);

		assertFalse(aircraftController.allowRedirection());
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#setAllowRedirection(boolean)}
	 * .
	 */
	@Test
	public void testSetAllowRedirection() {
		assertFalse(aircraftController.allowRedirection());

		aircraftController.setAllowRedirection(true);

		assertTrue(aircraftController.allowRedirection());

		aircraftController.setAllowRedirection(false);

		assertFalse(aircraftController.allowRedirection());
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getGameMode()}.
	 */
	@Test
	public void testGetGameMode() {
		assertEquals(aircraftController.getGameMode(), GameMode.SINGLE);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#getPlayers()}.
	 */
	@Test
	public void testGetPlayers() {
		assertTrue(aircraftController.getPlayers()[0] instanceof Player);
		assertTrue(aircraftController.getPlayers()[1] instanceof Player);
		assertFalse(aircraftController.getPlayers()[0] == aircraftController
				.getPlayers()[1]);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent, int)}
	 * .
	 */
	@Test
	public void testKeyDownInputEventInt() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#keyUp(com.badlogic.gdx.scenes.scene2d.InputEvent, int)}
	 * .
	 */
	@Test
	public void testKeyUpInputEventInt() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#takeoff(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testTakeoff() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#showGameOver()}.
	 */
	@Test
	public void testShowGameOver() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.AircraftController#incrementScore(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testIncrementScore() {
		assertEquals(aircraftController.getPlayerScore(), 0, 0);

		Aircraft aircraftA = aircraftController.generateAircraft();

		aircraftController.incrementScore(aircraftA);

		assertEquals(aircraftController.getPlayerScore(), 20, 0);
	}

}
