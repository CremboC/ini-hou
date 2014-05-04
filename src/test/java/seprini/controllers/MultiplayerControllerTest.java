/**
 * 
 */
package seprini.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.models.Aircraft;
import seprini.models.Airspace;

/**
 * @author Leslie
 * 
 */
public class MultiplayerControllerTest {

	MultiplayerController multiplayerController;
	Aircraft testAircraft;

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
		multiplayerController = new MultiplayerController(
				GameDifficulty.MEDIUM, airspace);
		testAircraft = multiplayerController.generateAircraft();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link seprini.controllers.MultiplayerController#init()}.
	 */
	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#update(float)}.
	 */
	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#collisionHasOccured(seprini.models.Aircraft, seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testCollisionHasOccured() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#generateAircraft()}.
	 */
	@Test
	public void testGenerateAircraft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#removeAircraft(int)}.
	 */
	@Test
	public void testRemoveAircraft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#selectAircraft(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testSelectAircraft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#switchAircraft(int)}.
	 */
	@Test
	public void testSwitchAircraft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent, int)}
	 * .
	 */
	@Test
	public void testKeyDownInputEventInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#keyUp(com.badlogic.gdx.scenes.scene2d.InputEvent, int)}
	 * .
	 */
	@Test
	public void testKeyUpInputEventInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#incrementScore(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testIncrementScore() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#MultiplayerController(seprini.data.GameDifficulty, seprini.models.Airspace, seprini.screens.ScreenBase)}
	 * .
	 */
	@Test
	public void testMultiplayerController() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#deselectAircraft(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testDeselectAircraft() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#showGameOverMulti(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testShowGameOverMulti() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#getPlayerScores()}.
	 */
	@Test
	public void testGetPlayerScores() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#withinPlayerZone(seprini.models.Aircraft, int)}
	 * .
	 */
	@Test
	public void testWithinPlayerZone() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#withinNoMansLand(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testWithinNoMansLand() {
		testAircraft.setOriginX(Config.NO_MAN_LAND[0]);
		System.out.println(Config.NO_MAN_LAND[0]);
		System.out.println(testAircraft.getCoords().x);
		assertTrue(MultiplayerController.withinNoMansLand(testAircraft));
		testAircraft.setPosition(Config.NO_MAN_LAND[2], 0);
		assertTrue(MultiplayerController.withinNoMansLand(testAircraft));
		testAircraft.setPosition(Config.NO_MAN_LAND[1], 0);
		assertTrue(MultiplayerController.withinNoMansLand(testAircraft));
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#getTotalScore()}.
	 */
	@Test
	public void testGetTotalScore() {
		assertEquals(multiplayerController.getTotalScore(), 0, 0);
	}

}
