package seprini.controllers;

import java.util.ArrayList;

import seprini.controllers.components.FlightPlanComponent;
import seprini.controllers.components.ScoreComponent;
import seprini.controllers.components.WaypointComponent;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airport;
import seprini.models.Airspace;
import seprini.models.GameMap;
import seprini.models.types.Player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MultiplayerController extends AircraftController {

	// One is the left player
	private final Aircraft[] selectedAircraft = {null, null};

	private final ScoreComponent[] playerScore = {new ScoreComponent(),
			new ScoreComponent()};

	private final ScoreComponent totalScore = new ScoreComponent();

	// lists to store the aircraft which belong to each player. This makes it
	// easier to switch between the aircraft of a player
	private ArrayList<Aircraft> playerOneAircraft = new ArrayList<Aircraft>(),
			playerTwoAircraft = new ArrayList<Aircraft>();

	/**
	 * remember the last index of which aircraft was selected, used with
	 * {@link #switchAircraft(int playerNumber)}
	 */
	private int[] lastIndex = {0, 0};

	private float scoreTimer;

	public MultiplayerController(GameDifficulty diff, Airspace airspace) {
		super(diff, airspace);
	}

	@Override
	protected void init() {
		this.mode = GameMode.MULTI;

		// add the background
		airspace.addActor(new GameMap(mode));

		// manages the waypoints
		this.waypoints = new WaypointComponent(this, mode);

		// helper for creating the flight plan of an aircraft
		this.flightPlanComponent = new FlightPlanComponent(waypoints);

	}

	@Override
	public void update(float delta) throws InterruptedException {
		super.update(delta);

		for (Airport airport : waypoints.getAirportList()) {
			// resets countdown for boarding times
			for (int i = 0; i <= 4; i++) {
				if (airport.countdown[i] <= 0)
					airport.countdown[i] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY;
				if (airport.timeElapsed[i] >= Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY)
					airport.timeElapsed[i] = 0;
			}

			// resets countdown for time between takeoffs
			if (airport.countdown[5] == 0)
				airport.countdown[5] = airport.timeTillFreeRunway;
		}

		// go over the aircraft list, hand over aircraft if they passed the
		// midline
		for (Aircraft aircraft : aircraftList) {

			// Handing over control from player one to player two
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[1]) {
				aircraft.setPlayer(getPlayers()[Player.ONE]);
			} else {
				aircraft.setPlayer(getPlayers()[Player.TWO]);
			}

			// if the player has changed, do the handover procedure
			if (aircraft.getPreviousPlayer().getNumber() != aircraft
					.getPlayer().getNumber()) {

				// remove it from the previous player's list
				removeFromListByPlayer(aircraft, aircraft.getPreviousPlayer()
						.getNumber());

				// deselect it for the previous player
				deselectAircraft(aircraft, aircraft.getPreviousPlayer()
						.getNumber());

				// change the previous player so the handover procedure is not
				// done again
				aircraft.changePreviousPlayer(getPlayers());

				aircraft.returnToPath();

				// add it to the new player list
				addToListByPlayer(aircraft);
			}

		}

		scoreTimer += delta;

		if (scoreTimer >= 5f) {
			scoreTimer = 0f;
			decrementScores();
		}
	}

	@Override
	protected boolean collisionHasOccured(Aircraft a, Aircraft b)
			throws InterruptedException {
		// prevents game from ending if collision occurs in no-mans land.
		if (withinNoMansLand(a) && withinNoMansLand(b)) {
			return false;
		}

		// if both aircraft have collided it (probably) means they both have
		// collided last few frames, so do nothing.
		if (a.hasCollided() && b.hasCollided()) {
			return false;
		}

		// if both are yet to collide, the regular procedure
		if (!a.hasCollided() || !b.hasCollided()) {

			a.setHasCollided(true);
			b.setHasCollided(true);

			if (withinPlayerZone(a, Player.ONE) && lives[Player.ONE] - 1 != 0) {
				lives[Player.ONE]--;
				return false;
			} else if (withinPlayerZone(a, Player.TWO)
					&& lives[Player.TWO] - 1 != 0) {
				lives[Player.TWO]--;
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

		showGameOverMulti(a);

		return true;
	}

	/**
	 * Selects an aircraft.
	 * 
	 * @param aircraft
	 */
	@Override
	protected void selectAircraft(Aircraft aircraft) {

		Aircraft playerAircraft = selectedAircraft[aircraft.getPlayer()
				.getNumber()];

		// deselect old aircraft so it knows it's no longer selected and stop it
		// from turning if they user holds the turning key and chooses another
		// aircraft
		if (playerAircraft != null) {
			deselectAircraft(playerAircraft);
		}

		// set new selected aircraft in this controller
		selectedAircraft[aircraft.getPlayer().getNumber()] = aircraft;

		// make new aircraft know it's selected
		selectedAircraft[aircraft.getPlayer().getNumber()].selected(true);
	}

	/**
	 * Allows the deselection of an aircraft, used when an aircraft goes into no
	 * man's land
	 * 
	 * @param aircraft
	 */
	protected void deselectAircraft(Aircraft aircraft) {
		deselectAircraft(aircraft, aircraft.getPlayer().getNumber());
	}

	/**
	 * 
	 * @param aircraft
	 * @param playerNumber
	 */
	protected void deselectAircraft(Aircraft aircraft, int playerNumber) {
		Aircraft selected = selectedAircraft[playerNumber];

		if (selected == null)
			return;

		// only deselect if passed aircraft is the same as the currently
		// selected aircraft, otherwise it doesn't allow selecting an aircraft
		// while there is one in no man's land
		if (!selected.equals(aircraft))
			return;

		// make sure old selected aircraft is no longer selected in its own
		// object
		selected.selected(false);

		// stop it from spinning if the aircraft is deselected while a
		// turning key is pressed
		selected.turnLeft(false);
		selected.turnRight(false);

		// actually deselect the aircraft in this object
		selectedAircraft[playerNumber] = null;
	}

	@Override
	protected Aircraft generateAircraft() {
		Aircraft aircraft = super.generateAircraft();

		if (aircraft == null)
			return null;

		if (aircraft.getEntryPoint().getCoords().x < Config.NO_MAN_LAND[0]) {
			aircraft.setPlayer(players[Player.ONE]);
			aircraft.setPreviousPlayer(players[Player.ONE]);
			aircraft.setLineColor(Color.RED);
		} else {
			aircraft.setPlayer(players[Player.TWO]);
			aircraft.setPreviousPlayer(players[Player.TWO]);
			aircraft.setLineColor(Color.BLUE);
		}

		aircraft.setScreenBoundaries(-10, -10, 10, 10);

		addToListByPlayer(aircraft);

		return aircraft;
	}

	@Override
	protected Aircraft removeAircraft(int i) {
		Aircraft aircraft = super.removeAircraft(i);

		removeFromListByPlayer(aircraft);

		return aircraft;
	}

	/**
	 * Switch the currently selected aircraft.
	 * 
	 * In this case this switches depending on the user and uses an 'lastIndex'
	 * to remember which aircraft was last selected.
	 */
	@Override
	protected void switchAircraft(int playerNumber) {

		// depending on which player is switching, select the appropriate
		// aircraftList
		ArrayList<Aircraft> aircraftList;

		switch (playerNumber) {
			default :
			case Player.ONE :
				aircraftList = playerOneAircraft;
				break;

			case Player.TWO :
				aircraftList = playerTwoAircraft;
				break;

		}

		// empty - do nothing
		if (aircraftList.size() == 0) {
			return;
		}

		int index;
		Aircraft aircraft = null;

		// after incrementing the last index by one -
		index = lastIndex[playerNumber] + 1;

		// try to get the aircraft
		// with that index from the list
		try {
			aircraft = aircraftList.get(index);
		} catch (IndexOutOfBoundsException e) {
			// if that index doesn't exist, it means we reached the end of the
			// list, reset the index to 0
			index = 0;
			lastIndex[playerNumber] = 0;
		}

		// since we may reset the index to 0, we need to get the aircraft again
		// from the list (as the 'try/catch' failed)
		if (index == 0) {
			aircraft = aircraftList.get(index);
		}

		if (aircraft == null)
			throw new IllegalStateException("Something went very wrong");

		// lastly select the aircraft and update the lastIndex so we know which
		// aircraft was selected previously.
		selectAircraft(aircraft);
		lastIndex[playerNumber] = index;
	}

	/**
	 * General case of adding an aircraft to a specific player's list - uses the
	 * aircraft's player number
	 * 
	 * @param aircraft
	 */
	private void addToListByPlayer(Aircraft aircraft) {
		addToListByPlayer(aircraft, aircraft.getPlayer().getNumber());
	}

	/**
	 * Add an aircraft to a specific aircraft list
	 * 
	 * @param aircraft
	 *            aircraft to add
	 * @param playerNumber
	 *            to which player's list to add
	 */
	private void addToListByPlayer(Aircraft aircraft, int playerNumber) {
		switch (playerNumber) {
			case Player.ONE :
				playerOneAircraft.add(aircraft);
				break;

			case Player.TWO :
				playerTwoAircraft.add(aircraft);
				break;

			default :
				break;
		}
	}

	/**
	 * General case of removing an aircraft to a specific player's list - uses
	 * the aircraft's player number
	 * 
	 * @param aircraft
	 */
	private void removeFromListByPlayer(Aircraft aircraft) {
		removeFromListByPlayer(aircraft, aircraft.getPlayer().getNumber());
	}

	/**
	 * Remove an aircraft to a specific aircraft list
	 * 
	 * @param aircraft
	 *            aircraft to add
	 * @param playerNumber
	 *            to which player's list to add
	 */
	private void removeFromListByPlayer(Aircraft aircraft, int playerNumber) {
		switch (playerNumber) {
			case Player.ONE :
				playerOneAircraft.remove(aircraft);
				break;

			case Player.TWO :
				playerTwoAircraft.remove(aircraft);
				break;

			default :
				break;
		}
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts as alternatives to the on screen buttons
	 */
	public boolean keyDown(InputEvent event, int keycode) {
		if (!paused) {

			for (int i = 0; i < selectedAircraft.length; i++) {

				if (selectedAircraft[i] != null) {
					if (keycode == selectedAircraft[i].getPlayer().getLeft())
						selectedAircraft[i].turnLeft(true);

					if (keycode == selectedAircraft[i].getPlayer().getRight())
						selectedAircraft[i].turnRight(true);

					if (keycode == selectedAircraft[i].getPlayer()
							.getAltIncrease())
						selectedAircraft[i].increaseAltitude();

					if (keycode == selectedAircraft[i].getPlayer()
							.getAltDecrease())
						selectedAircraft[i].decreaseAltitude();

					if (keycode == selectedAircraft[i].getPlayer()
							.getSpeedIncrease())
						selectedAircraft[i].increaseSpeed();

					if (keycode == selectedAircraft[i].getPlayer()
							.getSpeedDecrease())
						selectedAircraft[i].decreaseSpeed();

					if (keycode == selectedAircraft[i].getPlayer()
							.getReturnToPath())
						selectedAircraft[i].returnToPath();
				}

			}

			if (keycode == players[Player.ONE].getTakeoff()) {
				takeoff(waypoints.getAirportList().get(Player.ONE).takeoff(0));
			} else if (keycode == players[Player.TWO].getTakeoff()) {
				takeoff(waypoints.getAirportList().get(Player.TWO).takeoff(0));
			}

			if (keycode == players[Player.ONE].getSwitchPlane()) {
				switchAircraft(Player.ONE);
			} else if (keycode == players[Player.TWO].getSwitchPlane()) {
				switchAircraft(Player.TWO);
			}

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

		for (int i = 0; i < selectedAircraft.length; i++) {

			if (selectedAircraft[i] != null) {

				if (keycode == selectedAircraft[i].getPlayer().getLeft())
					selectedAircraft[i].turnLeft(false);

				if (keycode == selectedAircraft[i].getPlayer().getRight())
					selectedAircraft[i].turnRight(false);

			}

		}

		return false;
	}

	/**
	 * Game over - display the end screen
	 * 
	 * @param aircraft
	 */
	protected void showGameOverMulti(Aircraft aircraft) {

		if (withinPlayerZone(aircraft, Player.ONE)) {
			playerScore[Player.TWO].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		} else {
			playerScore[Player.ONE].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		}

		gameHasEnded = true;
	}

	@Override
	protected void incrementScore(Aircraft aircraft) {
		playerScore[Player.ONE].incrementScore((aircraft.getPoints() / 2)
				* difficulty.getScoreMultiplier());
		playerScore[Player.TWO].incrementScore((aircraft.getPoints() / 2)
				* difficulty.getScoreMultiplier());

		totalScore.incrementScore(aircraft.getPoints()
				* difficulty.getScoreMultiplier());
	}

	/**
	 * A static method to check the position of the aircraft - whether it's in
	 * P1's zone or P2's zone. Returns false if it's in NML.
	 * 
	 * @param aircraft
	 *            the aircraft which needs checking
	 * @param playerNumber
	 *            pass Player.ONE or Player.TWO
	 * @return <b>true</b> if aircraft is in provided player zone <br>
	 *         <b>false</b> otherwise
	 */
	public static boolean withinPlayerZone(Aircraft aircraft, int playerNumber) {
		if (playerNumber == Player.ONE) {
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[1]) {
				return true;
			}
		}

		if (playerNumber == Player.TWO) {
			if (aircraft.getCoords().x > Config.NO_MAN_LAND[1]) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check whether an aircraft is in no man's land
	 * 
	 * @param aircraft
	 * @return <b>true</b> if the aircraft is in NML <br>
	 *         <b>false</b> otherwise
	 */
	public static boolean withinNoMansLand(Aircraft aircraft) {
		return aircraft.getCoords().x >= Config.NO_MAN_LAND[0]
				&& aircraft.getCoords().x <= Config.NO_MAN_LAND[2];
	}

	private void decrementScores() {

		playerScore[Player.ONE].incrementScore(playerOneAircraft.size());
		playerScore[Player.TWO].incrementScore(playerTwoAircraft.size());
		totalScore.incrementScore(playerOneAircraft.size()
				+ playerTwoAircraft.size());
	}

	/**
	 * Get the player scores in an array
	 * 
	 * @return player scores
	 */
	public int[] getPlayerScores() {
		int[] scores = {playerScore[Player.ONE].getScore(),
				playerScore[Player.TWO].getScore()};

		return scores;
	}

	public int getTotalScore() {
		return totalScore.getScore();
	}

}