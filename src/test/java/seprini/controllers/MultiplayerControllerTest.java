/**
 * 
 */
package seprini.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seprini.controllers.components.FlightPlanComponent;
import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.Entrypoint;
import seprini.models.Waypoint;
import seprini.models.types.AircraftType;

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
	@Before
	public void setUp() throws Exception {
		Airspace airspace = new Airspace();
		multiplayerController = new MultiplayerController(
				GameDifficulty.MEDIUM, airspace);
		testAircraft = multiplayerController.generateAircraft();
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#generateAircraft()}.
	 */
	@Test
	public void testGenerateAircraft() {
		Aircraft aTestAircraft = multiplayerController.generateAircraft();

		assertTrue(aTestAircraft instanceof Aircraft);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#incrementScore(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testIncrementScore() {
		multiplayerController.incrementScore(testAircraft);
		assertEquals(multiplayerController.getPlayerScores()[0],
				testAircraft.getPoints(0) / 2, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#getPlayerScores()}.
	 */
	@Test
	public void testGetPlayerScores() {
		assertEquals(multiplayerController.getPlayerScores()[0], 0, 0);
		assertEquals(multiplayerController.getPlayerScores()[1], 0, 0);
		testAircraft.setPosition(0, 0);
		multiplayerController.incrementScore(testAircraft);
		assertEquals(multiplayerController.getPlayerScores()[0],
				testAircraft.getPoints(0) / 2, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#withinPlayerZone(seprini.models.Aircraft, int)}
	 * .
	 */
	@Test
	public void testWithinPlayerZone() {
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		ArrayList<Waypoint> exitpoints = new ArrayList<Waypoint>();
		ArrayList<Entrypoint> entrypoints = new ArrayList<Entrypoint>();

		GameDifficulty gameDifficulty = new GameDifficulty(10, 3, 100, 1, 500,
				5, 1);

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

		testAircraft = new Aircraft(aircraftType, flightplan, 0,
				GameMode.MULTI, gameDifficulty);

		assertTrue(MultiplayerController.withinPlayerZone(testAircraft, 0));
		assertFalse(MultiplayerController.withinPlayerZone(testAircraft, 1));

		entrypoints.clear();
		entrypoints.add(new Entrypoint(1200, 0));

		flightplan = new FlightPlanComponent(waypoints, exitpoints, entrypoints);

		testAircraft = new Aircraft(aircraftType, flightplan, 0,
				GameMode.MULTI, gameDifficulty);

		assertTrue(MultiplayerController.withinPlayerZone(testAircraft, 1));
		assertFalse(MultiplayerController.withinPlayerZone(testAircraft, 0));
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.MultiplayerController#withinNoMansLand(seprini.models.Aircraft)}
	 * .
	 */
	@Test
	public void testWithinNoMansLand() {
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		ArrayList<Waypoint> exitpoints = new ArrayList<Waypoint>();
		ArrayList<Entrypoint> entrypoints = new ArrayList<Entrypoint>();

		GameDifficulty gameDifficulty = new GameDifficulty(10, 3, 100, 1, 500,
				5, 1);

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

		testAircraft = new Aircraft(aircraftType, flightplan, 0,
				GameMode.MULTI, gameDifficulty);

		assertFalse(MultiplayerController.withinNoMansLand(testAircraft));

		entrypoints.clear();
		entrypoints.add(new Entrypoint(600, 0));

		flightplan = new FlightPlanComponent(waypoints, exitpoints, entrypoints);

		testAircraft = new Aircraft(aircraftType, flightplan, 0,
				GameMode.MULTI, gameDifficulty);

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
