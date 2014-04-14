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

	private ArrayList<Aircraft> playerOneAircraft = new ArrayList<Aircraft>();
	private ArrayList<Aircraft> playerTwoAircraft = new ArrayList<Aircraft>();

	private int[] lastIndex = { 0, 0 };

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

		// resets countdown for boarding times
		for (int i = 0; i <= 4; i++) {
			if (Airport.countdown[i] <= 0)
				Airport.countdown[i] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY;
			if (Airport.timeElapsed[i] >= Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY)
				Airport.timeElapsed[i] = 0;
		}

		// resets countdown for time between takeoffs
		if (Airport.countdown[5] == 0)
			Airport.countdown[5] = Airport.timeTillFreeRunway;

		// go over the aircraft list, deselect aircraft in no man's land, hand
		// over aircraft if they passed the midline
		for (Aircraft aircraft : aircraftList) {
			if (withinNoMansLand(aircraft)) {

				Aircraft selected = selectedAircraft[aircraft.getPlayer()
						.getNumber()];

				if (selected != null && selected.equals(aircraft)) {
					// if the aircraft is in no man's land and it is selected,
					// deselect it
					deselectAircraft(aircraft);
				}

				aircraft.returnToPath();
			}

			// Handing over control from player one to player two
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[1]) {
				// remove it from player two's list
				removeFromListByPlayer(aircraft);

				aircraft.setPlayer(getPlayers()[Player.ONE]);

				// add it to player one's list
				addToListByPlayer(aircraft);
			} else {
				// remove it from player one's list and
				removeFromListByPlayer(aircraft);

				aircraft.setPlayer(getPlayers()[Player.TWO]);

				// add it to player two's list
				addToListByPlayer(aircraft);
			}

		}
	}

	@Override
	public void collisionHasOccured(Aircraft a, Aircraft b) {
		// prevents game from ending if collision occurs in no-mans land.
		if (withinNoMansLand(a) && withinNoMansLand(b)) {
			return;
		}

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
		if (withinNoMansLand(aircraft) || aircraft == null) {
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

	@Override
	protected Aircraft generateAircraft() {
		Aircraft aircraft = super.generateAircraft();

		if (aircraft == null)
			return null;

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
	 * Switch the currently selected aircraft
	 */
	@Override
	protected void switchAircraft(int playerNumber) {

		ArrayList<Aircraft> aircraftList;

		switch (playerNumber) {
		default:
		case Player.ONE:
			aircraftList = playerOneAircraft;
			break;

		case Player.TWO:
			aircraftList = playerTwoAircraft;
			break;

		}

		if (aircraftList.size() == 0) {
			return;
		}

		int index;
		Aircraft aircraft = null;

		index = lastIndex[playerNumber] + 1;

		try {
			aircraft = aircraftList.get(index);
		} catch (IndexOutOfBoundsException e) {
			index = 0;
			lastIndex[playerNumber] = 0;
		}

		if (index == 0) {
			aircraft = aircraftList.get(index);
		}

		if (aircraft == null)
			System.out.println("wtf");

		selectAircraft(aircraft);

		lastIndex[playerNumber] = index;
	}

	/**
	 * 
	 * @param aircraft
	 */
	private void addToListByPlayer(Aircraft aircraft) {
		switch (aircraft.getPlayer().getNumber()) {
		case Player.ONE:
			playerOneAircraft.add(aircraft);
			break;

		case Player.TWO:
			playerTwoAircraft.add(aircraft);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @param aircraft
	 */
	private void removeFromListByPlayer(Aircraft aircraft) {
		switch (aircraft.getPlayer().getNumber()) {
		case Player.ONE:
			playerOneAircraft.remove(aircraft);
			break;

		case Player.TWO:
			playerTwoAircraft.remove(aircraft);
			break;

		default:
			break;
		}
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
			playerScore[Player.TWO].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		} else {
			playerScore[Player.ONE].incrementScore(difficulty
					.getScoreMultiplier() * Config.MULTIPLAYER_CRASH_BONUS);
		}

		screen.getGame().showMultiEndScreen(timer,
				playerScore[Player.ONE].getScore(),
				playerScore[Player.TWO].getScore());
	}

	@Override
	protected void incrementScore(Aircraft aircraft) {
		if (withinPlayerZone(aircraft, Player.ONE)) {

			// Creates a bonus. Fewer planes in your airspace = bigger
			// bonus
			int aircraftsInSpaceBonus = 10 - playerOneAircraft.size();
			// ensures the bonus can never go below 0
			if (aircraftsInSpaceBonus < 0) {
				aircraftsInSpaceBonus = 0;
			}
			playerScore[Player.ONE]
					.incrementScore((aircraft.getPoints() + aircraftsInSpaceBonus)
							* difficulty.getScoreMultiplier());
		} else {
			// Creates a bonus. Fewer planes in your airspace = bigger
			// bonus
			int aircraftsInSpaceBonus = 10 - playerOneAircraft.size();
			// ensures the bonus can never go below 0
			if (aircraftsInSpaceBonus < 0) {
				aircraftsInSpaceBonus = 0;
			}
			playerScore[Player.TWO]
					.incrementScore((aircraft.getPoints() + aircraftsInSpaceBonus)
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
			if (aircraft.getCoords().x < Config.NO_MAN_LAND[1]) {
				return true;
			} else {
				return false;
			}
		}

		if (playerNumber == Player.TWO) {
			if (aircraft.getCoords().x > Config.NO_MAN_LAND[1]) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	/**
	 * Check whether an aircraft is in no man's land
	 * 
	 * @param aircraft
	 * @return
	 */
	public static boolean withinNoMansLand(Aircraft aircraft) {
		return aircraft.getCoords().x >= Config.NO_MAN_LAND[0]
				&& aircraft.getCoords().x <= Config.NO_MAN_LAND[2];
	}
}
