package seprini.controllers;

import java.util.ArrayList;
import java.util.Random;

import seprini.controllers.components.FlightPlanComponent;
import seprini.controllers.components.ScoreComponent;
import seprini.controllers.components.WaypointComponent;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.Debug;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.GameMap;
import seprini.models.Waypoint;
import seprini.models.types.AircraftType;
import seprini.models.types.Player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AircraftController extends InputListener {

	private Random rand = new Random();

	// aircraft and aircraft type lists
	private final ArrayList<AircraftType> aircraftTypeList = new ArrayList<AircraftType>();
	protected final ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();

	private float lastGenerated, lastWarned;
	private boolean breachingSound, breachingIsPlaying, allowRedirection;

	protected Aircraft selectedAircraft;

	protected final GameDifficulty difficulty;

	// helpers for this class
	public WaypointComponent waypoints;
	public FlightPlanComponent flightPlanComponent;

	// ui related
	protected final Airspace airspace;

	private int aircraftId = 0, lastAircraftIndex;

	// game timer
	protected float timer = 0;

	// game score
	private ScoreComponent playerScore = new ScoreComponent();

	protected final Player[] players = { new Player(Player.ONE),
			new Player(Player.TWO) };

	protected GameMode mode;

	// public variables to keep track of game state
	public boolean paused, exitToMenu, gameHasEnded;

	// lives until the game ends
	protected int[] lives = { 2, 2 };

	/**
	 * 
	 * @param diff
	 *            game difficulty, changes number of aircraft and time between
	 *            them
	 * @param airspace
	 *            the group where all of the waypoints and aircraft will be
	 *            added
	 */
	public AircraftController(GameDifficulty diff, Airspace airspace) {
		this.difficulty = diff;
		this.airspace = airspace;

		// initialise aircraft types.
		aircraftTypeList.add(new AircraftType().setMaxClimbRate(600)
				.setMinSpeed(30f).setMaxSpeed(90f).setMaxTurningSpeed(48f)
				.setRadius(15).setSeparationRadius(diff.getSeparationRadius())
				.setTexture(Art.getTextureRegion("aircraft"))
				.setInitialSpeed(60f));

		this.init();
	}

	/**
	 * Initialise helpers and main components
	 */
	protected void init() {
		this.mode = GameMode.SINGLE;

		// add the background
		airspace.addActor(new GameMap(GameMode.SINGLE));

		// manages the waypoints
		this.waypoints = new WaypointComponent(this, GameMode.SINGLE);

		// helper for creating the flight plan of an aircraft
		this.flightPlanComponent = new FlightPlanComponent(waypoints);
	}

	/**
	 * Updates the aircraft positions. Generates a new aircraft and adds it to
	 * the stage. Collision Detection. Removes aircraft if inactive.
	 * 
	 * @throws InterruptedException
	 */
	public void update(float delta) throws InterruptedException {
		// Update timer
		timer += delta;
		// Update score
		// score += difficulty.getScoreMultiplier() * delta;

		breachingSound = false;

		// wait at least 2 seconds before allowing to warn again
		breachingIsPlaying = (timer - lastWarned < 2);

		// Checks for collisions or separation rule breaches and
		// removes aircraft which are no longer active from aircraftList.
		updateCollision();

		// make sure the breaching sound plays only when a separation breach
		// occurs. Also makes sure it start playing it only one time so there
		// aren't multiple warning sounds at the same time
		if (breachingSound && !breachingIsPlaying) {
			breachingIsPlaying = true;
			lastWarned = timer;
			Art.getSound("warning").play(1.0f);
		}

		// If the number of aircraft is below the maximum permitted and the time
		// elapsed since the last generation is greater than the preset
		// time difference between aircraft generated, create a new aircraft
		if (aircraftList.size() < difficulty.getMaxAircraft()
				&& timer - lastGenerated > difficulty
						.getTimeBetweenGenerations() + rand.nextInt(100)) {
			final Aircraft generatedAircraft = generateAircraft();

			// makes the aircraft clickable. Once clicked it is set as the
			// selected aircraft.
			generatedAircraft.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					selectAircraft(generatedAircraft);
				}

			});

			// push the aircraft to the top so it's infront of the user created
			// waypoints
			generatedAircraft.toFront();

			// add it to the airspace (stage group) so its automatically drawn
			// upon calling root.draw()
			airspace.addActor(generatedAircraft);

			// play a sound to audibly inform the player that an aircraft has
			// spawned
			Art.getSound("ding").play(0.5f);
		}

		// sort aircraft so they appear in the right order
		airspace.sortAircraft();

	}

	/**
	 * Handles what happens after a collision
	 * 
	 * @param a
	 * @param b
	 * @return <b>true</b> if player is out of lives, need to end the game <br>
	 *         <b>false</b> player still has lives, do nothing beside reduce
	 *         count of lives
	 * @throws InterruptedException
	 */
	protected boolean collisionHasOccured(Aircraft a, Aircraft b)
			throws InterruptedException {

		// if both aircraft have collided it (probably) means they both have
		// collided last few frames, so do nothing.
		if (a.hasCollided() && b.hasCollided()) {
			return false;
		}

		// if both are yet to collide, the regular procedure
		if (!a.hasCollided() || !b.hasCollided()) {

			a.setHasCollided(true);
			b.setHasCollided(true);

			if (lives[Player.ONE] - 1 > 0) {
				lives[Player.ONE]--;
				Art.getSound("lifedown").play(1f);
				return false;
			}
		}

		// stop the ambience sound and play the crash sound
		Art.getSound("ambience").stop();
		Art.getSound("crash").play(0.6f);

		// change the screen to the endScreen
		// TODO: hold the screen for n seconds while asplosion animation is
		// played, while ceasing all other updates.

		Thread.sleep(3000);

		gameHasEnded = true;

		return true;
	}

	/**
	 * Handles what happens after the separation rules have been breached
	 * 
	 * @param a
	 *            first aircraft that breached
	 * @param b
	 *            second aircraft that breached
	 */
	private void separationRulesBreached(Aircraft a, Aircraft b) {
		// for scoring mechanisms, if applicable
		a.setBreaching(true);
		b.setBreaching(true);

		breachingSound = true;
	}

	/**
	 * Selects random aircraft type from aircraftTypeList.
	 * 
	 * @return AircraftType
	 */
	private AircraftType randomAircraftType() {
		return aircraftTypeList.get(rand.nextInt(aircraftTypeList.size()));
	}

	/**
	 * Generates aircraft of random type with 'random' flight plan.
	 * <p>
	 * Checks if maximum number of aircraft is not exceeded. If it isn't, a new
	 * aircraft is generated with the arguments randomAircraftType() and
	 * generateFlightPlan().
	 * 
	 * @return an <b>Aircraft</b> if the following conditions have been met: <br>
	 *         a) there are no more aircraft on screen than allowed <br>
	 *         b) enough time has passed since the last aircraft has been
	 *         generated <br>
	 *         otherwise <b>null</b>
	 * 
	 */
	protected Aircraft generateAircraft() {

		Aircraft newAircraft = new Aircraft(randomAircraftType(),
				flightPlanComponent, aircraftId++, getGameMode(), difficulty);

		newAircraft.setPlayer(players[Player.ONE]);
		newAircraft.setScreenBoundaries(-10, -10, -190, 10);
		newAircraft.setLineColor(Color.RED);

		aircraftList.add(newAircraft);

		// store the time when an aircraft was last generated to know when to
		// generate the next aircraft
		lastGenerated = timer;

		return newAircraft;
	}

	/**
	 * Removes aircraft from aircraftList at index i.
	 * 
	 * @param i
	 * @return the removed aircraft
	 */
	protected Aircraft removeAircraft(int i) {
		Aircraft aircraft = aircraftList.get(i);

		if (aircraft.equals(selectedAircraft))
			selectedAircraft = null;

		// removes the aircraft from the list of aircrafts on screen
		aircraftList.remove(i);

		// adds removed aircrafts' points to player score
		incrementScore(aircraft);
		// removes the aircraft from the stage
		aircraft.remove();

		return aircraft;
	}

	/**
	 * Selects an aircraft.
	 * 
	 * @param aircraft
	 */
	protected void selectAircraft(Aircraft aircraft) {
		// make sure old selected aircraft is no longer selected in its own
		// object
		if (selectedAircraft != null) {
			selectedAircraft.selected(false);

			// make sure the old aircraft stops turning after selecting a new
			// aircraft; prevents it from going in circles
			selectedAircraft.turnLeft(false);
			selectedAircraft.turnRight(false);
		}

		// set new selected aircraft
		selectedAircraft = aircraft;

		// make new aircraft know it's selected
		selectedAircraft.selected(true);
	}

	/**
	 * Switch the currently selected aircraft
	 */
	protected void switchAircraft(int playerNumber) {
		int index = lastAircraftIndex + 1;
		Aircraft plane;

		// to prevent out of bounds exception
		if (aircraftList.size() == 0) {
			return;
		}

		// if we increase the index by too much, it will give an an exception -
		// restore index to the first one and loop again
		try {
			plane = aircraftList.get(index);
		} catch (IndexOutOfBoundsException e) {
			index = 0;
			lastAircraftIndex = 0;
		}

		plane = aircraftList.get(index);

		selectAircraft(plane);

		lastAircraftIndex = index;
	}

	/**
	 * Redirects aircraft to another waypoint.
	 * 
	 * @param waypoint
	 *            Waypoint to redirect to
	 */
	public void redirectAircraft(Waypoint waypoint) {
		Debug.msg("Redirecting aircraft " + 0 + " to " + waypoint);

		if (getSelectedAircraft() == null)
			return;

		getSelectedAircraft().insertWaypoint(waypoint);
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts as alternatives to the on screen buttons
	 */
	public boolean keyDown(InputEvent event, int keycode) {
		if (!paused) {

			if (selectedAircraft != null) {
				if (keycode == selectedAircraft.getPlayer().getLeft())
					selectedAircraft.turnLeft(true);

				if (keycode == selectedAircraft.getPlayer().getRight())
					selectedAircraft.turnRight(true);

				if (keycode == selectedAircraft.getPlayer().getAltIncrease())
					selectedAircraft.increaseAltitude();

				if (keycode == selectedAircraft.getPlayer().getAltDecrease())
					selectedAircraft.decreaseAltitude();

				if (keycode == selectedAircraft.getPlayer().getSpeedIncrease())
					selectedAircraft.increaseSpeed();

				if (keycode == selectedAircraft.getPlayer().getSpeedDecrease())
					selectedAircraft.decreaseSpeed();

				if (keycode == selectedAircraft.getPlayer().getReturnToPath())
					selectedAircraft.returnToPath();
			}
		}

		if (keycode == players[Player.ONE].getSwitchPlane()) {
			switchAircraft(Player.ONE);
		}

		if (keycode == Keys.SPACE)
			paused = !paused;

		if (keycode == Keys.ESCAPE) {
			Art.getSound("ambience").stop();
			exitToMenu = true;
		}

		return false;
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts to disable the turn left and turn right buttons on screen
	 */
	public boolean keyUp(InputEvent event, int keycode) {

		if (selectedAircraft != null) {

			if (keycode == selectedAircraft.getPlayer().getLeft())
				selectedAircraft.turnLeft(false);

			if (keycode == selectedAircraft.getPlayer().getRight())
				selectedAircraft.turnRight(false);

		}

		return false;
	}

	/**
	 * Makes an aircraft takeoff from the airport. <br>
	 * Basically adds to airspace, adds the click listener and starts the
	 * (graphical) takeoff procedure
	 * 
	 * @param aircraft
	 *            aircraft that will takeoff
	 */
	public void takeoff(final Aircraft aircraft) {

		if (aircraft == null)
			return;

		aircraft.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				selectAircraft(aircraft);
			}

		});

		// push the aircraft to the top so it's infront of the user created
		// waypoints
		aircraft.toFront();

		// add it to the airspace (stage group) so its automatically drawn
		// upon calling root.draw()
		airspace.addActor(aircraft);

		// play a sound to inform the player that an aircraft has
		// spawned
		Art.getSound("ding").play(0.5f);

		aircraftList.add(aircraft);
		aircraft.takingOff();
	}

	protected void incrementScore(Aircraft aircraft) {
		playerScore.incrementScore(aircraft.getPoints(Player.ONE)
				* difficulty.getScoreMultiplier());
	}

	/**
	 * Collision detection for both collisions and breaches of separation rules.
	 * 
	 * @throws InterruptedException
	 */
	private void updateCollision() throws InterruptedException {
		// Manages collision detection.
		for (int i = 0; i < aircraftList.size(); i++) {
			Aircraft planeI = aircraftList.get(i);

			planeI.setBreaching(false);

			// upon entering a full airport the game should end (unless player
			// still has 2 lives)
			if (planeI.hasEnteredFullAirport()) {
				if (collisionHasOccured(planeI, planeI))
					return;
			}

			// Collision Detection + Separation breach detection.
			for (Aircraft planeJ : aircraftList) {

				// Quite simply checks if distance between the centres of both
				// the aircraft <= the radius of aircraft i + radius of aircraft
				// j

				if (!planeI.equals(planeJ)
						// Check difference in altitude.
						&& Math.abs(planeI.getAltitude() - planeJ.getAltitude()) < Config.MIN_ALTITUDE_DIFFERENCE
						// Check difference in horizontal 2d plane.
						&& planeI.getCoords().dst(planeJ.getCoords()) < planeI
								.getRadius() + planeJ.getRadius()) {
					if (collisionHasOccured(planeI, planeJ))
						return;
				}

				// Checking for breach of separation.
				if (!planeI.equals(planeJ)
						// Check difference in altitude.
						&& Math.abs(planeI.getAltitude() - planeJ.getAltitude()) < difficulty
								.getVerticalSeparationRadius()
						// Check difference in horizontal 2d plane.
						&& planeI.getCoords().dst(planeJ.getCoords()) < planeI
								.getSeparationRadius()) {

					separationRulesBreached(planeI, planeJ);
				}
			}

			// Remove inactive aircraft.
			if (!planeI.isActive()) {
				removeAircraft(i);
			}

			// This should never happen but...
			if (planeI.getAltitude() < 0) {
				gameHasEnded = true;
			}
		}
	}

	public float getTimer() {
		return timer;
	}

	public float getPlayerScore() {
		return this.playerScore.getScore();
	}

	public Aircraft getSelectedAircraft() {
		return selectedAircraft;
	}

	public ArrayList<Aircraft> getAircraftList() {
		return aircraftList;
	}

	public Airspace getAirspace() {
		return airspace;
	}

	public boolean allowRedirection() {
		return allowRedirection;
	}

	public void setAllowRedirection(boolean value) {
		allowRedirection = value;
	}

	public GameMode getGameMode() {
		return mode;
	}

	public Player[] getPlayers() {
		return players;
	}

	public int[] getPlayerLives() {
		return lives;
	}
}
