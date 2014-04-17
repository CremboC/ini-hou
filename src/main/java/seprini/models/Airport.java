package seprini.models;

import java.util.ArrayList;
import java.util.Random;

import seprini.data.Art;
import seprini.data.Config;
import seprini.screens.AbstractScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Airport extends Waypoint {

	// Waypoints designating the end of the runway
	public Waypoint runwayStart;
	public Waypoint runwayEnd;
	public Waypoint runwayLeft;
	public Waypoint runwayRight;

	private final boolean visible;
	private Random rand = new Random();
	// Maximum number of aircraft that can be in the airport at once. If
	// exceeded, game ends.
	private final static int MAX_AIRCRAFT_NUMBER = 5;
	// Required altitude for an aircraft to land
	public final static int MIN_ALTITUDE = 5000;
	// Time remaining before an aircraft can take off.
	public static int timeTillFreeRunway = 5;
	public ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();
	public int boardingAircraft = 0;

	private boolean selected;
	private static final Color COLOR = new Color(1, 0, 0, 0);
	public static int[] countdown = {
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY, timeTillFreeRunway };
	public static int[] timeElapsed = { 0, 0, 0, 0, 0, 0 };

	public Airport(float x, float y, boolean visible) {
		super(x, y, visible);
		// Position takeoff and landing waypoints relative to airport position.
		runwayStart = new Waypoint(x - 77, y - 60, false);
		runwayEnd = new Waypoint(x + 77, y + 60, false);
		runwayLeft = new Waypoint(x - 157, y - 60, false);
		runwayRight = new Waypoint(x - 77, y - 140, false);
		this.visible = visible;
		this.texture = Art.getTextureRegion("airport");
		this.size = Config.AIRPORT_SIZE;

	}

	public void setTimeLeft(int timeTillFreeRunway) {
		// avoid the timer becoming negative.
		if (timeTillFreeRunway <= 0) {
			this.timeTillFreeRunway = 0;
		}

	}

	/**
	 * Inserts an aircraft into the airport, done by reference.
	 * 
	 * @param aircraft
	 *            to insert
	 * @throws IllegalStateException
	 *             if insertion will overflow airport
	 */
	public void insertAircraft(final Aircraft aircraft)
			throws IllegalStateException {
		if (aircraftList.size() + 1 > MAX_AIRCRAFT_NUMBER) {
			throw new IllegalStateException(
					"Tried landing an aircraft into a full airport.");
		}
		boardingAircraft += 1;

		Timer.schedule(new Task() {
			@Override
			public void run() {
				aircraftList.add(aircraft);
			}
		}, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);

		boardingCountdown(boardingAircraft);

	}

	/**
	 * Forces an aircraft to take off. Selects one from the list randomly.
	 * Removes it from the list afterwards.
	 * 
	 * @return Aircraft one of the aircraft that were held in the airport. This
	 *         aircraft can be used instead of generating a new one.
	 * @throws IllegalStateException
	 *             if there are no aircraft in the airport
	 */

	public Aircraft takeoff(int i) {
		if (aircraftList.size() == 0)
			return null;

		for (int j = 0; j < aircraftList.size(); j++) {
			countdown[j] = countdown[j + 1];
		}

		Aircraft aircraft = aircraftList.get(i);
		aircraftList.remove(i);
		boardingAircraft -= 1;

		countdown[5] = Airport.timeTillFreeRunway;

		Timer.schedule(new Task() {
			@Override
			public void run() {
				countdown[5] = Airport.timeTillFreeRunway - timeElapsed[5];
				timeElapsed[5] = timeElapsed[5] + 1;

			}
		}, 0, 1, Airport.timeTillFreeRunway);

		Timer.schedule(new Task() {
			@Override
			public void run() {
				timeElapsed[5] = 0;
			}
		}, Airport.timeTillFreeRunway);

		boardingCountdown(boardingAircraft);

		return aircraft;
	}

	public void setSelected(boolean value) {
		this.selected = value;
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {

		if (selected) {

			AbstractScreen.drawCircle(COLOR, getX(), getY(), 25, batch);

		}

		if (countdown[5] == 5) {
			AbstractScreen.drawString("Time remaining: 0", getX(), getY(),
					Color.BLACK, batch, true, 1);
		} else {
			AbstractScreen.drawString("Time remaining: " + countdown[5],
					getX(), getY(), Color.BLACK, batch, true, 1);
		}

	}

	private void boardingCountdown(int boardingAircraft) {
		switch (boardingAircraft) {
		case 1:
			Timer.schedule(new Task() {
				@Override
				public void run() {

					countdown[0] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY
							- timeElapsed[0];
					timeElapsed[0] = timeElapsed[0] + 1;

				}
			}, 0, 1, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);

			break;
		case 2:
			Timer.schedule(new Task() {
				@Override
				public void run() {
					countdown[1] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY
							- timeElapsed[1];
					timeElapsed[1] = timeElapsed[1] + 1;

				}
			}, 0, 1, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);
			break;
		case 3:
			Timer.schedule(new Task() {
				@Override
				public void run() {
					countdown[2] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY
							- timeElapsed[2];
					timeElapsed[2] = timeElapsed[2] + 1;

				}
			}, 0, 1, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);
			break;
		case 4:
			Timer.schedule(new Task() {
				@Override
				public void run() {

					countdown[3] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY
							- timeElapsed[3];
					timeElapsed[3] = timeElapsed[3] + 1;

				}
			}, 0, 1, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);
			break;
		case 5:
			Timer.schedule(new Task() {
				@Override
				public void run() {

					countdown[4] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY
							- timeElapsed[4];
					timeElapsed[4] = timeElapsed[4] + 1;

				}
			}, 0, 1, Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY);
			break;
		}
	}
}
