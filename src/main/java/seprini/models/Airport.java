package seprini.models;




import java.util.ArrayList;
import java.util.Random;




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

	private Random rand = new Random();
	// Maximum number of aircraft that can be in the airport at once. If
	// exceeded, game ends.
	private final static int MAX_AIRCRAFT_NUMBER = 5;
	// Required altitude for an aircraft to land
	public final static int MIN_ALTITUDE = 5000;
	// Time remaining before an aircraft can take off.
	private int timeLeft = 15;
	public ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();

	private boolean selected;
	private static final Color COLOR = new Color(1, 0, 0, 0);

	public Airport(float x, float y, boolean visible) {
		super(x, y, visible);
		// Position waypoints relative to airport position.
		runwayStart = new Waypoint(x - 77, y - 60, true);
		runwayEnd = new Waypoint(x + 77, y + 60, true);
		runwayLeft = new Waypoint(x - 157, y - 60, true);
		runwayRight = new Waypoint(x - 77, y - 140, true);

	}

	public void setTimeLeft(int timeLeft) {
		// avoid the timer becoming lower than 0 i.e. negative
		if (timeLeft >= 0) {
			this.timeLeft = 0;
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
	public void insertAircraft(final Aircraft aircraft) throws IllegalStateException {
		if (aircraftList.size() + 1 > MAX_AIRCRAFT_NUMBER) {
			throw new IllegalStateException(
					"Tried landing an aircraft into a full airport.");
		}
		float delay = 15; // seconds
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	aircraftList.add(aircraft);
		    }
		    }, delay);
		    	
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
	
	public Aircraft takeoff(int i) throws IllegalStateException {
		if (aircraftList.size() == 0)
			throw new IllegalStateException("No aircraft in airport");

		Aircraft aircraft = aircraftList.get(i);
		aircraftList.remove(i);

		return aircraft;
	}

	public void setSelected(boolean value) {
		this.selected = value;
	}

	protected void additionalDraw(SpriteBatch batch) {

		if (selected) {

			AbstractScreen.drawCircle(COLOR, getX(), getY(), 25, batch);

		}

	}
}
