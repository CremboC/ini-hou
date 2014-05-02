package seprini.models;

import java.util.ArrayList;

import seprini.data.Art;
import seprini.data.Config;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Airport extends Waypoint {

	// Waypoints designating the end of the runway
	public Waypoint runwayStart, runwayEnd, runwayLeft, runwayRight;

	// Maximum number of aircraft that can be in the airport at once. If
	// exceeded, game ends.
	private final static int MAX_AIRCRAFT_NUMBER = 5;

	// Required altitude for an aircraft to land
	public final static int MIN_ALTITUDE = 5000;

	// Time remaining before an aircraft can take off.
	public float timeTillFreeRunway = 5;
	public ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();
	public int boardingAircraft = 0;

	public float[] countdown = { Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY,
			Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY, timeTillFreeRunway };

	public int[] timeElapsed = { 0, 0, 0, 0, 0, 0 };
	public boolean takeoffReady = true;

	public ArrayList<Aircraft> waitingAircraft = new ArrayList<Aircraft>();

	public Airport(float x, float y, boolean visible) {
		super(x, y, visible);
		// Position takeoff and landing waypoints relative to airport position.
		runwayStart = new Waypoint(x - 77, y - 60, false);
		runwayEnd = new Waypoint(x + 77, y + 60, false);
		runwayLeft = new Waypoint(x - 157, y - 60, false);
		runwayRight = new Waypoint(x - 77, y - 140, false);
		this.texture = Art.getTextureRegion("airport");
		this.size = Config.AIRPORT_SIZE;

	}

	public void act(float delta) {
		for (int i = 0; i < waitingAircraft.size(); i++) {
			countdown[i] -= delta;
			if (countdown[i] <= 0) {
				countdown[i] = Config.AIRCRAFT_TAKEOFF_AND_LANDING_DELAY;

				aircraftList.add(waitingAircraft.get(0));
				waitingAircraft.remove(0);
			}
		}

		if (takeoffReady == false) {
			countdown[5] -= delta;
			if (countdown[5] <= 0) {
				countdown[5] = timeTillFreeRunway;
				takeoffReady = true;
			}
		}

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

		waitingAircraft.add(aircraft);
	}

	/**
	 * Forces an aircraft to take off. Removes it from the list afterwards.
	 * 
	 * @return Aircraft one of the aircraft that were held in the airport. This
	 *         aircraft can be used instead of generating a new one.
	 * @throws IllegalStateException
	 *             if there are no aircraft in the airport
	 */
	public Aircraft takeoff(int i) {
		if (aircraftList.size() == 0)
			return null;

		for (int j = 0; j < aircraftList.size() + waitingAircraft.size(); j++) {
			countdown[j] = countdown[j + 1];
		}

		takeoffReady = false;

		Aircraft aircraft = aircraftList.get(i);
		aircraftList.remove(i);
		boardingAircraft -= 1;

		return aircraft;
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {
	}
}
