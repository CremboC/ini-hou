/**
 * 
 */
package seprini.models;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.ATC;
import seprini.controllers.AircraftController;
import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.models.types.AircraftType;
import seprini.screens.GameScreen;

/**
 * @author Leslie
 * 
 */
public class AircraftTest {

	Aircraft aircraft;

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
		ATC atc = new ATC();
		GameScreen gameScreen = new GameScreen(atc, gameDifficulty);
		Airspace airspace = new Airspace();
		AircraftController aircraftController = new AircraftController(
				gameDifficulty, airspace, gameScreen);
		AircraftType aircraftType = new AircraftType().setMaxClimbRate(600)
				.setMinSpeed(30f).setMaxSpeed(90f).setMaxTurningSpeed(48f)
				.setRadius(15)
				.setSeparationRadius(gameDifficulty.getSeparationRadius())
				.setTexture(Art.getTextureRegion("aircraft"))
				.setInitialSpeed(60f);

		aircraft = new Aircraft(aircraftType,
				aircraftController.flightplan.generate(), 0, aircraftController);

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
		System.out.println(aircraft.getPoints());
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#insertWaypoint(seprini.models.Waypoint)}.
	 */
	@Test
	public void testInsertWaypoint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getNextWaypoint()}.
	 */
	@Test
	public void testGetNextWaypoint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#increaseSpeed()}.
	 */
	@Test
	public void testIncreaseSpeed() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#decreaseSpeed()}.
	 */
	@Test
	public void testDecreaseSpeed() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#decreaseAltitude()}.
	 */
	@Test
	public void testDecreaseAltitude() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#increaseAltitude()}.
	 */
	@Test
	public void testIncreaseAltitude() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#turnRight(boolean)}.
	 */
	@Test
	public void testTurnRight() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#turnLeft(boolean)}.
	 */
	@Test
	public void testTurnLeft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#returnToPath()}.
	 */
	@Test
	public void testReturnToPath() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#selected(boolean)}.
	 */
	@Test
	public void testSelected() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#checkBreaching()}.
	 */
	@Test
	public void testCheckBreaching() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getPlayer()}.
	 */
	@Test
	public void testGetPlayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#takingOff()}.
	 */
	@Test
	public void testTakingOff() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isTurningRight()}.
	 */
	@Test
	public void testIsTurningRight() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isTurningLeft()}.
	 */
	@Test
	public void testIsTurningLeft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getFlightPlan()}.
	 */
	@Test
	public void testGetFlightPlan() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getRadius()}.
	 */
	@Test
	public void testGetRadius() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getSeparationRadius()}.
	 */
	@Test
	public void testGetSeparationRadius() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isBreaching()}.
	 */
	@Test
	public void testIsBreaching() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#setBreaching(boolean)}.
	 */
	@Test
	public void testSetBreaching() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getAltitude()}.
	 */
	@Test
	public void testGetAltitude() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#getSpeed()}.
	 */
	@Test
	public void testGetSpeed() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.models.Aircraft#setPlayer(seprini.models.types.Player)}.
	 */
	@Test
	public void testSetPlayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#isActive()}.
	 */
	@Test
	public void testIsActive() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link seprini.models.Aircraft#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
