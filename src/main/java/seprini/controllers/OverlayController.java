package seprini.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import seprini.data.Art;
import seprini.data.Config;
import seprini.models.Airport;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;

public class OverlayController extends ChangeListener {

	private final static int LEFT = 0;
	private final static int RIGHT = 1;

	private final MultiplayerController controller;

	private final HashMap<String, TextButton> buttons = new HashMap<String, TextButton>();
	private final HashMap<String, Label> labels = new HashMap<String, Label>();

	// UI wrappers for the controls and the buttons at the bottom
	private final Table[] landedAircraft = { new Table(), new Table() };
	private final Table ui;

	private ArrayList<Airport> airportList = new ArrayList<Airport>();

	/**
	 * Handler for the overlay present in the multiplayer controller
	 * 
	 * @param controller
	 * @param ui
	 */
	public OverlayController(MultiplayerController controller, Table ui) {
		this.controller = controller;
		this.ui = ui;

		this.init();
	}

	/**
	 * Initialise all of the wrappers, buttons and general layout elements
	 */
	public void init() {

		// initialise buttons for all aircraft that can land
		for (int i = 0; i < landedAircraft.length; i++) {
			landedAircraft[i].setFillParent(true);

			if (Config.DEBUG_UI)
				landedAircraft[i].debug();

			ui.addActor(landedAircraft[i]);

			for (int j = 0; j <= 4; j++) {
				createButton("aircraft" + i + "_" + j, "A", landedAircraft[i],
						false).width(40);
				landedAircraft[i].row();
			}
		}

		// set the position of the wrapper for the landed aircraft buttons
		landedAircraft[LEFT].center().left();
		landedAircraft[LEFT].toFront();

		landedAircraft[RIGHT].center().right();
		landedAircraft[RIGHT].toFront();

		ui.add(landedAircraft[LEFT]);
		ui.add(landedAircraft[RIGHT]);

		createLabel("leftTakeoffCountdown", " 0", landedAircraft[0]).width(40);
		createLabel("rightTakeoffCountdown", " 0", landedAircraft[1]).width(40);

		airportList = controller.waypoints.getAirportList();
	}

	/**
	 * Update the tag texts
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		// updates timers and text for all of the landed aircraft
		for (int i = 0; i < controller.waypoints.getAirportList().size(); i++) {
			Airport airport = controller.waypoints.getAirportList().get(i);

			for (int j = 0; j < 5; j++) {
				buttons.get("aircraft" + i + "_" + j).setText(" ");
				buttons.get("aircraft" + i + "_" + j).setVisible(false);
			}

			for (int j = 0; j < airport.waitingAircraft.size(); j++) {
				buttons.get(
						"aircraft" + i + "_"
								+ (j + airport.aircraftList.size()))
						.setVisible(true);
				buttons.get(
						"aircraft" + i + "_"
								+ (j + airport.aircraftList.size())).setText(
						"B: " + (int) airport.countdown[j]);
			}

			for (int j = 0; j < airport.aircraftList.size(); j++) {
				buttons.get("aircraft" + i + "_" + j).setVisible(true);
				buttons.get("aircraft" + i + "_" + j).setText("R!");
			}
		}

		// update timer for the left takeoff countdown
		if (airportList.get(0).countdown[5] == 5) {
			labels.get("leftTakeoffCountdown").setText(" 0");
		} else {
			labels.get("leftTakeoffCountdown").setText(
					" " + (int) airportList.get(0).countdown[5]);
		}

		// update timer for the right takeoff countdown
		if (airportList.get(1).countdown[5] == 5) {
			labels.get("rightTakeoffCountdown").setText(" 0");
		} else {
			labels.get("rightTakeoffCountdown").setText(
					" " + (int) airportList.get(1).countdown[5]);
		}

	}

	/**
	 * Convinience method to create labels
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

	@Override
	public void changed(ChangeEvent event, Actor actor) {

		// go through all airports in the screen
		for (int i = 0; i < controller.waypoints.getAirportList().size(); i++) {
			Airport airport = controller.waypoints.getAirportList().get(i);

			// go through all buttons, 5 for each airport
			for (int j = 0; j < 5; j++) {
				if (actor.equals(buttons.get("aircraft" + i + "_" + j)))
					controller.takeoff(airport.takeoff(j));
			}
		}

	}

}
