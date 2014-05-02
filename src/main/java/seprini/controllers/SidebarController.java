package seprini.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import seprini.data.Art;
import seprini.data.Config;
import seprini.models.Aircraft;
import seprini.models.Airport;
import seprini.models.types.Player;
import seprini.screens.ScreenBase;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;

/**
 * Controls the sidebar in the GameScreen
 */
public final class SidebarController extends ChangeListener {

	private final AircraftController controller;

	private Aircraft selectedAircraft;
	private ArrayList<Airport> airportList;

	private final HashMap<String, TextButton> buttons = new HashMap<String, TextButton>();
	private final HashMap<String, Label> labels = new HashMap<String, Label>();

	private final ScreenBase screen;

	// UI wrappers for the controls and the buttons at the bottom
	private Table sidebar, aircraftControls, bottomButtons, landedAircraft;

	// stores state of the turn left/right buttons
	private boolean turningLeft, turningRight;

	/**
	 * 
	 * 
	 * @param sidebar
	 *            the sidebar layout, so the controller can add all of the
	 *            buttons
	 * @param controller
	 *            AircraftController instance to get the selected aircraft
	 * @param screen
	 *            for changing screens once Menu or Pause have been clicked
	 */
	public SidebarController(Table sidebar, AircraftController controller,
			ScreenBase screen) {
		this.sidebar = sidebar;
		this.controller = controller;
		this.screen = screen;
		this.init();
		this.airportList = controller.waypoints.getAirportList();
	}

	/**
	 * Initialise all the buttons and labels
	 */
	private void init() {

		// wrapper for aicraft controls
		aircraftControls = new Table();
		aircraftControls.setFillParent(true);

		if (Config.DEBUG_UI)
			aircraftControls.debug();

		aircraftControls.top();
		sidebar.addActor(aircraftControls);

		// wrapper for airport listings
		landedAircraft = new Table();
		landedAircraft.setFillParent(true);

		if (Config.DEBUG_UI)
			landedAircraft.debug();

		landedAircraft.center();

		// offset the airport listings so the don't overlap the controls
		landedAircraft.padTop(200);
		sidebar.addActor(landedAircraft);

		// wrapper for bottom buttons
		bottomButtons = new Table();

		bottomButtons.setFillParent(true);

		if (Config.DEBUG_UI)
			aircraftControls.debug();

		bottomButtons.bottom();
		sidebar.addActor(bottomButtons);

		// adding labels to aircraft controls
		createLabel("speed", " Speed: ", aircraftControls).width(200)
				.colspan(2);

		aircraftControls.row();

		createLabel("altitude", " Altitude: ", aircraftControls).width(200)
				.colspan(2);

		aircraftControls.row();

		// adding buttons to aircraft controls

		createButton("assignWaypoint", " Assign Waypoint", aircraftControls,
				true).width(200).colspan(2);

		aircraftControls.row();

		createButton("returnToPath", " Return to Path (R)", aircraftControls,
				false).width(200).colspan(2);

		aircraftControls.row();

		createButton("accelerate", " Accelerate (E)", aircraftControls, false)
				.width(200).colspan(2);

		aircraftControls.row().colspan(2);

		createButton("decelerate", " Decelerate (Q)", aircraftControls, false)
				.width(200);

		aircraftControls.row().spaceTop(20);

		createButton("up", " Up (W)", aircraftControls, false).width(100)
				.colspan(2);

		aircraftControls.row();

		createButton("left", " Left (A)", aircraftControls, true).width(100);
		createButton("right", "Right (D)", aircraftControls, true).width(100);

		aircraftControls.row();

		createButton("down", "Down (S)", aircraftControls, false).width(100)
				.colspan(2);

		aircraftControls.row();

		createLabel("", " Lives:", bottomButtons).width(100);
		createLabel("lives", "..", bottomButtons).width(100);

		bottomButtons.row();

		createLabel("", " Time:", bottomButtons).width(100);
		createLabel("timer", "..", bottomButtons).width(100);

		bottomButtons.row();

		createLabel("", " Score:", bottomButtons).width(100);
		createLabel("score", "..", bottomButtons).width(100);

		bottomButtons.row();

		// adding buttons for airport listings

		createLabel("leftTakeoffTimer", "Left Airport 0 seconds",
				landedAircraft).width(200);
		landedAircraft.row();

		createButton("leftAircraft0", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("leftAircraft1", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("leftAircraft2", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("leftAircraft3", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("leftAircraft4", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createLabel("rightTakeoffTimer", "Right Airport 0 seconds",
				landedAircraft).width(200);
		landedAircraft.row();

		createButton("rightAircraft0", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("rightAircraft1", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("rightAircraft2", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("rightAircraft3", " ", landedAircraft, false).width(200);
		landedAircraft.row();

		createButton("rightAircraft4", " ", landedAircraft, false).width(200);

		// adding buttons to bottom
		createButton("menu", " Menu", bottomButtons, false).width(100);
		createButton("pause", " Pause", bottomButtons, false).width(100);
	}

	/**
	 * Update the sidebar according to changes in the AircraftController
	 */
	public void update() {
		String altitudeText;
		String speedText;

		// update timer
		labels.get("timer").setText(
				"" + (int) Math.round(controller.getTimer()));

		// update score
		labels.get("score").setText(
				"" + (int) Math.round(controller.getPlayerScore()));

		// update lives
		labels.get("lives").setText(
				"" + controller.getPlayerLives()[Player.ONE]);

		// if there is no selected aircraft, return immediately to avoid errors
		// otherwise set it to the local selectedAircraft variable and update
		// the text
		if ((selectedAircraft = controller.getSelectedAircraft()) == null) {
			altitudeText = " Altitude: ";
			speedText = " Speed: ";
		} else {
			altitudeText = " Altitude: " + selectedAircraft.getAltitude() + "m";
			speedText = " Speed: "
					+ Math.round(selectedAircraft.getSpeed()
							* Config.AIRCRAFT_SPEED_MULTIPLIER) + "km/h";
		}

		// force left + right buttons to be checked correctly
		buttons.get("left").setChecked(
				selectedAircraft != null && selectedAircraft.isTurningLeft());
		buttons.get("right").setChecked(
				selectedAircraft != null && selectedAircraft.isTurningRight());

		// update aircraft altitude text
		labels.get("altitude").setText(altitudeText);

		// update aircraft speed text
		labels.get("speed").setText(speedText);

		// resets countdown for boarding times
		for (Airport airport : controller.waypoints.getAirportList()) {
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

		// if there is no selected airport, return immediately to avoid errors
		// otherwise set it to the local selectedAirport variable and update
		// the text

		for (int i = 0; i < 5; i++) {
			buttons.get("leftAircraft" + Integer.toString(i)).setText(" ");
		}

		for (int i = 0; i < airportList.get(0).waitingAircraft.size(); i++) {
			buttons.get(
					"leftAircraft"
							+ Integer.toString(airportList.get(0).aircraftList
									.size() + i)).setText(
					"Aircraft boarding: "
							+ (int) airportList.get(0).countdown[i]);
		}

		for (int i = 0; i < airportList.get(0).aircraftList.size(); i++) {
			buttons.get("leftAircraft" + Integer.toString(i)).setText(
					"Ready for take off");
		}

		if (airportList.get(0).countdown[5] == 5) {
			labels.get("leftTakeoffTimer").setText(" Left Airport: 0 seconds");
		} else {
			labels.get("leftTakeoffTimer").setText(
					" Left Airport: " + (int) airportList.get(0).countdown[5]
							+ " seconds");
		}

		for (int i = 0; i < 5; i++) {
			buttons.get("rightAircraft" + Integer.toString(i)).setText(" ");
		}

		for (int i = 0; i < airportList.get(1).waitingAircraft.size(); i++) {
			buttons.get(
					"rightAircraft"
							+ Integer.toString(airportList.get(1).aircraftList
									.size() + i)).setText(
					"Aircraft boarding: "
							+ (int) airportList.get(1).countdown[i]);
		}

		for (int i = 0; i < airportList.get(1).aircraftList.size(); i++) {
			buttons.get("rightAircraft" + Integer.toString(i)).setText(
					"Ready for take off");
		}

		if (airportList.get(1).countdown[5] == 5) {
			labels.get("rightTakeoffTimer")
					.setText(" Right Airport: 0 seconds");
		} else {
			labels.get("rightTakeoffTimer").setText(
					" Right Aiport: " + (int) airportList.get(1).countdown[5]
							+ " seconds");
		}

	}

	/**
	 * Convinience method to create buttons and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	private Cell<?> createButton(String name, String text, Table parent,
			boolean toggle) {
		TextButton button = new TextButton(text, Art.getSkin(),
				(toggle) ? "toggle" : "default");
		button.pad(3);
		button.addListener(this);

		buttons.put(name, button);

		return parent.add(button);
	}

	/**
	 * Convinience method to create labels and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unused")
	private Cell<?> createLabel(String name, String text) {
		Label label = new Label(text, Art.getSkin());
		labels.put(name, label);

		return sidebar.add(label);
	}

	/**
	 * Convinience method to create labels and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	private Cell<?> createLabel(String name, String text, Table parent) {
		Label label = new Label(text, Art.getSkin());

		labels.put(name, label);

		return parent.add(label);
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (!screen.isPaused()) {

			if (actor.equals(buttons.get("assignWaypoint")))
				controller.setAllowRedirection(!controller.allowRedirection());

			if (selectedAircraft != null) {
				if (actor.equals(buttons.get("returnToPath")))
					selectedAircraft.returnToPath();

				if (actor.equals(buttons.get("left")))
					selectedAircraft.turnLeft(turningLeft = (!turningLeft));

				if (actor.equals(buttons.get("right")))
					selectedAircraft.turnRight(turningRight = (!turningRight));

				if (actor.equals(buttons.get("up")))
					selectedAircraft.increaseAltitude();

				if (actor.equals(buttons.get("down")))
					selectedAircraft.decreaseAltitude();

				if (actor.equals(buttons.get("accelerate")))
					selectedAircraft.increaseSpeed();

				if (actor.equals(buttons.get("decelerate")))
					selectedAircraft.decreaseSpeed();

			}

			if ((airportList.get(0).aircraftList.size() != 0)
					&& (airportList.get(0).takeoffReady)) {

				for (int i = 0; i < airportList.get(0).aircraftList.size(); i++) {
					if (actor.equals(buttons.get("leftAircraft" + i)))
						controller.takeoff(airportList.get(0).takeoff(i));

				}

			}

			if ((airportList.get(1).aircraftList.size() != 0)
					&& (airportList.get(1).takeoffReady)) {

				for (int i = 0; i < airportList.get(1).aircraftList.size(); i++) {
					if (actor.equals(buttons.get("rightAircraft" + i)))
						controller.takeoff(airportList.get(1).takeoff(i));

				}

			}
		}

		if (actor.equals(buttons.get("menu"))) {
			Art.getSound("ambience").stop();
			screen.getGame().showMenuScreen();
		}

		if (actor.equals(buttons.get("pause"))) {
			screen.setPaused(!screen.isPaused());

		}

	}
}
