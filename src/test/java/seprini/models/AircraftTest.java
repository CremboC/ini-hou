/**
 * 
 */
package seprini.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.controllers.components.FlightPlanComponent;
import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.types.AircraftType;
import seprini.models.types.Player;

/**
 * @author Leslie
 * 
 */
public class AircraftTest {

	Aircraft aircraft;

	ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

	ArrayList<Waypoint> exitpoints = new ArrayList<Waypoint>();

	ArrayList<Entrypoint> entrypoints = new ArrayList<Entrypoint>();

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
		GameDifficulty gameDifficulty = new GameDifficulty(10, 3, 100, 1, 500,
				5);

		waypoints.add(new Waypoint(200, 200, true));

		exitpoints.add(new Waypoint(500, 500, true));

		entrypoints.add(new Entrypoint(0, 0));

		// helper for creating the flight plan of an aircraft
		FlightPlanComponent flightplan = new FlightPlanComponent(waypoints,
				exitpoints, entrypoints);

		AircraftType aircraftType = new AircraftType().setMaxClimbRate(600)
				.setMinSpeed(30f).setMaxSpeed(90f).setMaxTurningSpeed(48f)
				.setRadius(15)
				.setSeparationRadius(gameDifficulty.getSeparationRadius())
				.setTexture(Art.getTextureRegion("aircraft"))
				.setInitialSpeed(60f);

		ArrayList<Waypoint> excludedWaypoints = new ArrayList<Waypoint>();
		aircraft = new Aircraft(aircraftType, flightplan, 0, GameMode.SINGLE);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#act(float)}.
	 */
	@Test
	public void testAct() {
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#additionalDraw(com.badlogic.gdx.graphics.g2d.SpriteBatch)}
	 * .
	 */
	@Test
	public void testAdditionalDraw() {
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#Aircraft(seprini.models.types.AircraftType, java.util.ArrayList, int, seprini.controllers.AircraftController)}
	 * .
	 */
	@Test
	public void testAircraft() {
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getPoints()}.
	 */
	@Test
	public void testGetPoints() {
		assertEquals(aircraft.getPoints(), 20, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#insertWaypoint(seprini.models.Waypoint)}.
	 */
	@Test
	public void testInsertWaypoint() {
		Waypoint testWaypoint = new Waypoint(0, 0, false);

		aircraft.insertWaypoint(testWaypoint);

		assertEquals(aircraft.waypoints.get(0), testWaypoint);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getNextWaypoint()}.
	 */
	@Test
	public void testGetNextWaypoint() {
		assertEquals(aircraft.getNextWaypoint(), waypoints.get(0));
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#increaseSpeed()}.
	 */
	@Test
	public void testIncreaseSpeed() {
		assertEquals(aircraft.getSpeed(), 60f, 0);
		aircraft.increaseSpeed();
		assertEquals(aircraft.getSpeed(), 66f, 0);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#decreaseSpeed()}.
	 */
	@Test
	public void testDecreaseSpeed() {
		assertEquals(aircraft.getSpeed(), 60f, 0);
		aircraft.decreaseSpeed();
		assertEquals(aircraft.getSpeed(), 54f, 0);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#decreaseAltitude()}.
	 */
	@Test
	public void testDecreaseAltitude() {
		int previousAlt = aircraft.getAltitude();

		if (previousAlt == 5000) {
			aircraft.increaseAltitude();
			aircraft.act(5f);
			previousAlt = aircraft.getAltitude();
		}

		aircraft.decreaseAltitude();

		aircraft.act(5f);

		assertTrue(aircraft.getAltitude() < previousAlt);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#increaseAltitude()}.
	 */
	@Test
	public void testIncreaseAltitude() {
		int previousAlt = aircraft.getAltitude();

		if (previousAlt == 15000) {
			aircraft.decreaseAltitude();
			aircraft.act(5f);
			previousAlt = aircraft.getAltitude();
		}

		aircraft.increaseAltitude();

		aircraft.act(5f);

		assertTrue(aircraft.getAltitude() > previousAlt);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#turnRight(boolean)}.
	 */
	@Test
	public void testTurnRight() {
		aircraft.turnRight(true);

		assertTrue(aircraft.isTurningRight());
		assertFalse(aircraft.isTurningLeft());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#turnLeft(boolean)}.
	 */
	@Test
	public void testTurnLeft() {
		aircraft.turnLeft(true);

		assertTrue(aircraft.isTurningLeft());
		assertFalse(aircraft.isTurningRight());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#returnToPath()}.
	 */
	@Test
	public void testReturnToPath() {
		aircraft.returnToPath();

		assertFalse(aircraft.isTurningRight());
		assertFalse(aircraft.isTurningLeft());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#selected(boolean)}.
	 */
	@Test
	public void testSelected() {
		assertTrue(aircraft.selected(true));
		assertFalse(aircraft.selected(false));
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#checkBreaching()}.
	 */
	@Test
	public void testCheckBreaching() {
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getPlayer()}.
	 */
	@Test
	public void testGetPlayer() {
		assertEquals(aircraft.getPlayer(), null);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#takingOff()}.
	 */
	@Test
	public void testTakingOff() {
		aircraft.takingOff();

		assertEquals(aircraft.getAltitude(), 0);
		assertEquals(aircraft.getSpeed(), 60f, 0);

	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isTurningRight()}.
	 */
	@Test
	public void testIsTurningRight() {
		assertFalse(aircraft.isTurningRight());
		assertFalse(aircraft.isTurningLeft());

		aircraft.turnRight(true);

		assertTrue(aircraft.isTurningRight());
		assertFalse(aircraft.isTurningLeft());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isTurningLeft()}.
	 */
	@Test
	public void testIsTurningLeft() {
		assertFalse(aircraft.isTurningRight());
		assertFalse(aircraft.isTurningLeft());

		aircraft.turnLeft(true);

		assertTrue(aircraft.isTurningLeft());
		assertFalse(aircraft.isTurningRight());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getFlightPlan()}.
	 */
	@Test
	public void testGetFlightPlan() {
		assertEquals(aircraft.getFlightPlan().get(0), waypoints.get(0));
		assertEquals(aircraft.getFlightPlan().get(1), exitpoints.get(0));
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getRadius()}.
	 */
	@Test
	public void testGetRadius() {
		assertEquals(aircraft.getRadius(), 15f, 0);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getSeparationRadius()}.
	 */
	@Test
	public void testGetSeparationRadius() {
		assertEquals(aircraft.getSeparationRadius(), 100f, 0);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isBreaching()}.
	 */
	@Test
	public void testIsBreaching() {
		assertFalse(aircraft.isBreaching());

		aircraft.setBreaching(true);

		assertTrue(aircraft.isBreaching());

		aircraft.setBreaching(false);

		assertFalse(aircraft.isBreaching());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#setBreaching(boolean)}.
	 */
	@Test
	public void testSetBreaching() {
		assertFalse(aircraft.isBreaching());

		aircraft.setBreaching(true);

		assertTrue(aircraft.isBreaching());

		aircraft.setBreaching(false);

		assertFalse(aircraft.isBreaching());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getAltitude()}.
	 */
	@Test
	public void testGetAltitude() {
		if (aircraft.getAltitude() == 5000) {
			assertEquals(aircraft.getAltitude(), 5000);
		}
		if (aircraft.getAltitude() == 10000) {
			assertEquals(aircraft.getAltitude(), 10000);
		}
		if (aircraft.getAltitude() == 15000) {
			assertEquals(aircraft.getAltitude(), 15000);
		}
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getSpeed()}.
	 */
	@Test
	public void testGetSpeed() {
		assertEquals(aircraft.getSpeed(), 60f, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#setPlayer(seprini.models.types.Player)}.
	 */
	@Test
	public void testSetPlayer() {
		Player testPlayer = new Player(0);
		aircraft.setPlayer(testPlayer);

		assertEquals(aircraft.getPlayer(), testPlayer);
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isActive()}.
	 */
	@Test
	public void testIsActive() {
		assertTrue(aircraft.isActive());
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {

	}

	/**
	 * Test method for {@link seprini.models.Aircraft#toString()}.
	 */
	@Test
	public void testToString() {

	}

}
