package seprini.controllers;

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

	public OverlayController(MultiplayerController controller, Table ui) {
		this.controller = controller;
		this.ui = ui;

		this.init();
	}

	/**
	 * Initialise all of the wrappers, buttons and general layout elements
	 */
	public void init() {

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

		landedAircraft[LEFT].center().left();
		landedAircraft[LEFT].toFront();

		landedAircraft[RIGHT].center().right();
		landedAircraft[RIGHT].toFront();

		ui.add(landedAircraft[LEFT]);
		ui.add(landedAircraft[RIGHT]);
	}

	/**
	 * Update the tag texts
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		for (int i = 0; i < controller.waypoints.getAirportList().size(); i++) {
			Airport airport = controller.waypoints.getAirportList().get(i);

			for (int j = 0; j < 5; j++) {
				buttons.get("aircraft" + i + "_" + j).setText(" ");
				buttons.get("aircraft" + i + "_" + j).setVisible(false);
			}

			for (int j = 0; j < airport.boardingAircraft; j++) {
				buttons.get("aircraft" + i + "_" + j).setVisible(true);
				buttons.get("aircraft" + i + "_" + j).setText(
						"B: " + Airport.countdown[j]);
			}

			for (int j = 0; j < airport.aircraftList.size(); j++) {
				buttons.get("aircraft" + i + "_" + j).setVisible(true);
				buttons.get("aircraft" + i + "_" + j).setText("R!");
			}
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
