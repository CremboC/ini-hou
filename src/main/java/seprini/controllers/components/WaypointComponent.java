package seprini.controllers.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import seprini.controllers.AircraftController;
import seprini.data.Debug;
import seprini.data.GameMode;
import seprini.models.Airport;
import seprini.models.Entrypoint;
import seprini.models.Exitpoint;
import seprini.models.Waypoint;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WaypointComponent {

	private ArrayList<Waypoint> permanentList = new ArrayList<Waypoint>();
	private ArrayList<Entrypoint> entryList = new ArrayList<Entrypoint>();
	private ArrayList<Waypoint> exitList = new ArrayList<Waypoint>();
	private ArrayList<Airport> airportList = new ArrayList<Airport>();

	private final AircraftController controller;

	private Airport selectedAirport;

	public WaypointComponent(AircraftController controller, GameMode mode) {

		this.controller = controller;

		switch (mode) {
		case SINGLE:
			singleplayerWaypoints();
			break;
		case MULTI:
			multiplayerWaypoints();
			break;
		}

		Collections.shuffle(permanentList, new Random());
	}

	/**
	 * Creates all the predefined waypoints for singleplayer
	 */
	public void singleplayerWaypoints() {
		// add entry waypoints to entryList
		createEntrypoint(0, 0);
		createEntrypoint(0, 720);
		createEntrypoint(1080, 360);
		createEntrypoint(540, 0);

		// add exit waypoints to exitList
		createExitpoint(1080, 720);
		createExitpoint(1080, 0);
		createExitpoint(0, 420);
		createExitpoint(540, 720);

		// add visible waypoints
		createWaypoint(150, 360, true);
		createWaypoint(250, 600, true);
		createWaypoint(600, 650, true);
		createWaypoint(700, 100, true);
		createWaypoint(550, 360, true);
		createWaypoint(700, 500, true);
		createWaypoint(450, 100, true);
		createWaypoint(850, 300, true);

		// add airports;
		createAirport(250, 250);
		createAirport(830, 470);
	}

	/**
	 * Creates all the predefined waypoints for multiplayer
	 */
	public void multiplayerWaypoints() {
		// add entry waypoints to entryList
		createEntrypoint(0, 0); // bottom left
		createEntrypoint(1280, 720); // top right

		createEntrypoint(340, 720); // no man's land left top
		createEntrypoint(940, 0); // no man's land right bottom

		// add exit waypoints to exitList
		createExitpoint(0, 720); // top left
		createExitpoint(1280, 0); // bottom right

		createExitpoint(440, 0); // no man's land left bottom
		createExitpoint(840, 720); // no man's land top right

		// add visible waypoints
		createWaypoint(640, 240, true);
		createWaypoint(640, 480, true);
		createWaypoint(960, 240, true);
		createWaypoint(960, 480, true);
		createWaypoint(320, 240, true);
		createWaypoint(320, 480, true);

		// add airports;
		createAirport(387, 335);
		createAirport(900, 350);
	}

	/**
	 * Creates a new waypoint.
	 * 
	 * <p>
	 * Creates a new user waypoint when the user left-clicks within the airspace
	 * window.
	 * 
	 * Also is convinience method for generated permanent waypoints
	 * 
	 * @param x
	 * @param y
	 */
	public boolean createWaypoint(float x, float y, boolean visible) {
		Debug.msg("Creating waypoint at: " + x + ":" + y);

		final Waypoint waypoint = new Waypoint(x, y, visible);

		// add it to the correct list according to whether it is user created or
		// not
		getPermanentList().add(waypoint);

		// add it to the airspace so it is automatically drawn using root.draw()
		controller.getAirspace().addActor(waypoint);

		// add a listener is a user can remove it or redirect aircraft to it
		waypoint.addListener(new ClickListener() {

			/**
			 * Removes a user waypoint if a user waypoint is right-clicked.
			 * Alternatively, should call redirection method.
			 */
			@Override
			public boolean touchDown(InputEvent event, float tX, float tY,
					int pointer, int button) {

				if (button == Buttons.LEFT && controller.allowRedirection()) {
					controller.redirectAircraft(waypoint);
					return true;
				}
				return true;
			}
		});

		return true;
	}

	/**
	 * Creates an exitpoint, adds it to the list of exitpoints and adds it to
	 * the airspace
	 * 
	 * @param x
	 * @param y
	 */
	private void createExitpoint(float x, float y) {
		Exitpoint point = new Exitpoint(new Vector2(x, y));
		getExitList().add(point);
		controller.getAirspace().addActor(point);
	}

	/**
	 * Creates an entry point, adds it to the list of exitpoints and adds it to
	 * the airspace
	 * 
	 * @param x
	 * @param y
	 */
	private void createEntrypoint(float x, float y) {
		Entrypoint point = new Entrypoint(new Vector2(x, y));
		getEntryList().add(point);
		controller.getAirspace().addActor(point);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	private void createAirport(float x, float y) {
		final Airport airport = new Airport(x, y, true);

		exitList.add(airport);
		airportList.add(airport);

		controller.getAirspace().addActor(airport);

		airport.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float tX, float tY,
					int pointer, int button) {

				for (Airport airportI : airportList) {
					airportI.setSelected(false);
				}

				airport.setSelected(true);
				selectedAirport = airport;

				return true;
			}
		});
	}

	public ArrayList<Waypoint> getPermanentList() {
		return permanentList;
	}

	public ArrayList<Entrypoint> getEntryList() {
		return entryList;
	}

	public ArrayList<Waypoint> getExitList() {
		return exitList;
	}

	public ArrayList<Airport> getAirportList() {
		return airportList;
	}

	public Airport getSelectedAirport() {
		return selectedAirport;
	}
}
