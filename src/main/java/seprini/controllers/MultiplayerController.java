package seprini.controllers;

import seprini.controllers.components.FlightPlanComponent;
import seprini.controllers.components.ScoreComponent;
import seprini.controllers.components.WaypointComponent;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.Map;
import seprini.models.types.Player;
import seprini.screens.ScreenBase;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MultiplayerController extends AircraftController {

	private final Aircraft[] selectedAircraft = { null, null };
	// One is the left player
	private final ScoreComponent[] playerScore = { new ScoreComponent(),
			new ScoreComponent() };

	public MultiplayerController(GameDifficulty diff, Airspace airspace,
			ScreenBase screen) {
		super(diff, airspace, screen);
	}

	@Override
	protected void init() {

		this.mode = GameMode.MULTI;

		// add the background
		airspace.addActor(new Map(mode));

		// manages the waypoints
		this.waypoints = new WaypointComponent(this, mode);

		// helper for creating the flight plan of an aircraft
		this.flightplan = new FlightPlanComponent(waypoints);
	}

	@Override
	public void update(float delta) throws InterruptedException {
		super.update(delta);

		// go over the aircraft list, deselect aircraft in no man's land, hand
		// over aircraft if they passed the midline
		for (Aircraft aircraft : aircraftList) {

			if (selectedAircraft[aircraft.getPlayer().getNumber()] != null) {
				// if the aircraft is in no man's land and it is selected,
				// deselect it
				if (aircraft.getCoords().x >= Config.NO_MAN_LAND[0]
						&& aircraft.getCoords().x <= Config.NO_MAN_LAND[2]
						&& selectedAircraft[aircraft.getPlayer().getNumber()]
								.equals(aircraft)) {
					deselectAircraft(aircraft);
				}
			}

			// Handing over control from player one to player two
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[1]) {
				aircraft.setPlayer(getPlayers()[Player.ONE]);
			} else {
				aircraft.setPlayer(getPlayers()[Player.TWO]);
			}

		}
	}

	@Override
	public void collisionHasOccured(Aircraft a, Aircraft b) {
		// stop the ambience sound and play the crash sound
		Art.getSound("ambience").stop();
		Art.getSound("crash").play(0.6f);

		// change the screen to the endScreen
		// TODO: hold the screen for n seconds while asplosion animation is
		// played, while ceasing all other updates.

		showGameOverMulti(a);
	}

	/**
	 * Selects an aircraft.
	 * 
	 * @param aircraft
	 */
	@Override
	protected void selectAircraft(Aircraft aircraft) {

		// Cannot select in the No Man's Land
		if (aircraft.getCoords().x >= Config.NO_MAN_LAND[0]
				&& aircraft.getCoords().x <= Config.NO_MAN_LAND[2]) {
			return;
		}

		// make sure old selected aircraft is no longer selected in its own
		// object
		Aircraft playerAircraft = selectedAircraft[aircraft.getPlayer()
				.getNumber()];

		if (playerAircraft != null) {

			playerAircraft.selected(false);

			// make sure the old aircraft stops turning after selecting a new
			// aircraft; prevents it from going in circles
			playerAircraft.turnLeft(false);
			playerAircraft.turnRight(false);

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

		Aircraft selected = selectedAircraft[aircraft.getPlayer().getNumber()];

		// only select if passed aircraft is the same as the currently selected
		// aircraft, otherwise it doesn't allow selecting an aircraft while
		// there is one in no man's land (another check is done in
		// Aircraft.java)
		if (!selected.equals(aircraft)) {
			return;
		}

		if (selected != null) {
			selected.selected(false);

			selected.turnLeft(false);
			selected.turnRight(false);

			selectedAircraft[aircraft.getPlayer().getNumber()] = null;
		}
	}

	/**
	 * Switch the currently selected aircraft
	 */
	@Override
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

		if (plane.getPlayer().getNumber() == playerNumber) {
			selectAircraft(plane);
		}

		lastAircraftIndex = index;
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts as alternatives to the on screen buttons
	 */
	public boolean keyDown(InputEvent event, int keycode) {
		if (!screen.isPaused()) {

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

					if (keycode == selectedAircraft[i].getPlayer().getTakeoff())
						takeoff(waypoints.getAirportList().get(i).takeoff(0));
				}

			}

		}

		if (keycode == players[Player.ONE].getSwitchPlane()) {
			switchAircraft(Player.ONE);
		} else if (keycode == players[Player.TWO].getSwitchPlane()) {
			switchAircraft(Player.TWO);
		}

		if (keycode == Keys.SPACE)
			screen.setPaused(!screen.isPaused());

		if (keycode == Keys.ESCAPE) {
			Art.getSound("ambience").stop();
			screen.getGame().showMenuScreen();
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
		// TODO overload gameover constructor to show scores

		if (withinPlayerZone(aircraft, Player.ONE)) {
			playerScore[Player.ONE].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		} else {
			playerScore[Player.TWO].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		}

		screen.getGame().showMultiEndScreen(timer,
				playerScore[Player.ONE].getScore(),
				playerScore[Player.TWO].getScore());
	}

	@Override
	protected void incrementScore(Aircraft aircraft) {
		if (withinPlayerZone(aircraft, Player.ONE)) {
			playerScore[Player.ONE].incrementScore(aircraft.getPoints()
					* difficulty.getScoreMultiplier());
		} else {
			playerScore[Player.TWO].incrementScore(aircraft.getPoints()
					* difficulty.getScoreMultiplier());
		}
	}

	/**
	 * Get the player scores in an array
	 * 
	 * @return
	 */
	public int[] getPlayerScores() {
		int[] scores = { playerScore[Player.ONE].getScore(),
				playerScore[Player.TWO].getScore() };

		return scores;
	}

	/**
	 * A static method to check the position of the aircraft - whether it's in
	 * P1's zone or P2's zone. Returns false if it's in NML.
	 * 
	 * @param aircraft
	 *            the aircraft which needs checking
	 * @param playerNumber
	 *            pass Player.ONE or Player.TWO
	 * @return
	 */
	public static boolean withinPlayerZone(Aircraft aircraft, int playerNumber) {
		if (playerNumber == Player.ONE) {
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[0]) {
				return true;
			} else {
				return false;
			}
		}

		if (playerNumber == Player.TWO) {
			if (aircraft.getCoords().x > Config.NO_MAN_LAND[2]) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
