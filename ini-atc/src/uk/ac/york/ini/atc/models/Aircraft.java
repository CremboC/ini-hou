package uk.ac.york.ini.atc.models;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.york.ini.atc.controllers.AircraftType;
import uk.ac.york.ini.atc.data.Config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Aircraft extends Entity {

	private int altitude;
	protected Vector2 velocity = new Vector2(0, 0);

	protected float radius;
	protected float separationRadius;

	protected ArrayList<Waypoint> waypoints;

	protected float maxTurningRate;
	protected float maxClimbRate;
	protected float maxSpeed;

	protected int sepRulesBreachCounter;
	protected int lastTimeTurned;

	protected boolean isActive = true;
	protected boolean turningFlag; // May not be used

	public Aircraft(AircraftType aircraftType, ArrayList<Waypoint> flightPlan) {

		// initialise all of the aircraft values according to the passed
		// aircraft type
		radius = aircraftType.getRadius();
		separationRadius = aircraftType.getSeparationRadius();
		texture = aircraftType.getTexture();
		maxTurningRate = aircraftType.getMaxTurningSpeed();
		maxClimbRate = aircraftType.getMaxClimbRate();
		maxSpeed = aircraftType.getMaxSpeed();
		velocity = aircraftType.getVelocity();

		// set the flightplan to the generated by the controller
		waypoints = flightPlan;

		// set the size
		size = new Vector2(76, 63);

		// set the coords to the entry point, remove it from the flight plan
		Waypoint entryPoint = flightPlan.get(0);
		coords = new Vector2(entryPoint.getX(), entryPoint.getY());
		flightPlan.remove(0);

		// set origin to center of the aircraft, makes rotation more intuitive
		this.setOrigin(getWidth() / 2, getHeight() / 2);

		this.setScale(0.5f);

		// set bounds so the aircraft is clickable
		this.setBounds(getX(), getY(), getWidth() / 2, getHeight() / 2);

		// set rotation to fit next waypoint
		this.setRotation(angleToWaypoint());
	}

	@Override
	public void act() {
		float delta = Gdx.graphics.getDeltaTime();

		calculateVelocity();
		this.setBounds(getX() + getOriginX(), getY() + getOriginY(),
				getWidth(), getHeight());
		isActive();

	}

	private void calculateVelocity() {

		Vector2 nextWaypoint = vectorToWaypoint();

		float degrees = (float) ((Math.atan2(coords.x - nextWaypoint.x,
				-(coords.y - nextWaypoint.y)) * 180.0d / Math.PI) + 90.0f);

		this.setRotation(degrees);

		// Calculating velocity and making sure it is under the max and before
		// the next waypoint
		velocity = nextWaypoint.sub(coords);

		// checking whether aircraft is at the next waypoint (close enough =
		// 10px)
		if (velocity.len() < 10) {
			isActive();
			waypoints.remove(0);
		}

		// making sure speed is not too big
		velocity.clamp(0, this.maxSpeed);

		// finally updating coordinates
		coords.add(velocity);
	}

	/**
	 * Calculates the vector to the next waypoint
	 * 
	 * @return 3d vector to the next waypoint
	 */
	private Vector2 vectorToWaypoint() {
		// Creates a new vector to store the new velocity in temporarily
		Vector2 nextWaypoint = new Vector2();

		// converts waypoints coordinates into 3d vectors to enabled
		// subtraction.
		nextWaypoint.x = waypoints.get(0).getCoords().x
				+ (Config.WAYPOINT_SIZE.x / 2);
		nextWaypoint.y = waypoints.get(0).getCoords().y
				+ (Config.WAYPOINT_SIZE.y / 2);

		return nextWaypoint;
	}

	/**
	 * Calculate angle to the next waypoint
	 * 
	 * @return
	 */
	private float angleToWaypoint() {
		Vector2 nextWaypoint = vectorToWaypoint();

		return angleToWaypoint(nextWaypoint);
	}

	/**
	 * Calculate angle to the next waypoint, use this if you already have the 3d
	 * vector to the next waypoint
	 * 
	 * @return
	 */
	private static float angleToWaypoint(Vector2 nextWaypoint) {
		// setting angle using the waypoint's size so it heads towards
		// the centre of the waypoint
		nextWaypoint.x += (Config.WAYPOINT_SIZE.x / 2);
		nextWaypoint.y += (Config.WAYPOINT_SIZE.x / 2);

		return nextWaypoint.angle();
	}

	/**
	 * Checks whether the aircraft is within 10 pixels of the next waypoint
	 * 
	 * @param vectorToWaypoint
	 * @return whether aicraft is at the next waypoint
	 */
	public boolean isAtNextWaypoint(Vector3 vectorToWaypoint) {
		if (vectorToWaypoint.len() < 10) {
			isActive();
			waypoints.remove(0);
			return true;
		} else {
			return false;
		}

	}

	public void checkSpeed() {
		this.velocity.clamp(0, this.maxSpeed);
	}

	public void updateCoords() {
		coords.add(velocity);
	}

	/**
	 * Adding a new waypoint to the head of the arraylist
	 * 
	 * @param newWaypoint
	 */
	public void insertWaypoint(Waypoint newWaypoint) {
		waypoints.add(0, newWaypoint);
	}

	/**
	 * Turns right by 5 degrees if the user presses the right key for more than
	 * 2000ms
	 */
	public void turnRight() {
		Vector3 zAxis = new Vector3();
		zAxis.set(0, 0, 1);
		if (delay())
			;
		velocity.rotate(5);
	}

	/**
	 * Turns left by 5 degrees if the user presses the right key for more than
	 * 2000ms
	 */
	public void turnLeft() {
		Vector3 zAxis = new Vector3();
		zAxis.set(0, 0, 1);
		if (delay())
			;
		velocity.rotate(-5);
	}

	/**
	 * Calculates the time for which the buttons have been pressed.
	 * 
	 * @return
	 */
	public boolean delay() {
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		long previousTime = currentTime;
		if (currentTime - previousTime >= 2000)
			;
		return true;
	}

	/**
	 * Increases rate of altitude change
	 */
	public void increaseAltitude() {
		// this.altitude += 5;
		// if (this.altitude > maxClimbRate) {
		// this.velocity.z = maxClimbRate;
		// }
	}

	/**
	 * Decreasing rate of altitude change
	 */
	public void decreaseAltitude() {
		// this.velocity.add(0, 0, -5);
		// if (this.velocity.z > -maxClimbRate) {
		// this.velocity.z = -maxClimbRate;
		// }

	}

	/**
	 * Regular regular getter for radius
	 * 
	 * @return int radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * check if its only got the exit point left to go to.
	 * 
	 * @return whether is active
	 */
	public boolean isActive() {
		if (waypoints.size() == 1) {
			this.isActive = false;
		}
		return isActive;
	}

}
