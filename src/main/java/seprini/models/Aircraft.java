package seprini.models;

import java.util.ArrayList;
import java.util.Random;

import seprini.controllers.AircraftController;
import seprini.data.Config;
import seprini.data.Debug;
import seprini.models.types.AircraftType;
import seprini.screens.AbstractScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class Aircraft extends Entity {

	private final int id;

	private static final float SPEED_CHANGE = 6f;
	private static final int ALTITUDE_CHANGE = 5000;

	private int desiredAltitude;

	private int altitude;
	private Vector2 velocity = new Vector2(0, 0);

	private final ArrayList<Waypoint> waypoints;

	private final float radius, separationRadius, maxTurningRate, maxClimbRate,
			maxSpeed;
	private float minSpeed;

	private boolean breaching;

	private boolean isActive = true;
	private boolean ignorePath = false; // When user has taken control of the
	// aircraft

	// whether the aircraft is selected by the player
	private boolean selected;

	private boolean turnRight, turnLeft;

	// used for smooth turning
	// rememeber last angle to check if it's increasing or not
	private float previousAngle = 0;
	// if is increasing, switch rotation sides so it uses the 'smaller' angle
	private boolean rotateRight = false;

	public Aircraft(AircraftType aircraftType, ArrayList<Waypoint> flightPlan,
			int id) {

		// allows drawing debug shape of this entity
		debugShape = true;

		this.id = id;

		// initialise all of the aircraft values according to the passed
		// aircraft type
		radius = aircraftType.getRadius();
		separationRadius = aircraftType.getSeparationRadius();
		texture = aircraftType.getTexture();
		maxTurningRate = aircraftType.getMaxTurningSpeed();
		maxClimbRate = aircraftType.getMaxClimbRate();
		maxSpeed = aircraftType.getMaxSpeed();
		minSpeed = aircraftType.getMinSpeed();
		velocity = new Vector2(aircraftType.getInitialSpeed(), 0);

		Random rand = new Random();
		altitude = Config.ALTITUDES[rand.nextInt(Config.ALTITUDES.length)];
		desiredAltitude = altitude;

		// set the flightplan to the generated by the controller
		waypoints = flightPlan;

		// set the size
		size = new Vector2(76, 63);

		// set the coords to the entry point, remove it from the flight plan
		Waypoint entryPoint = waypoints.get(0);
		coords = new Vector2(entryPoint.getX(), entryPoint.getY());
		waypoints.remove(0);

		// set origin to center of the aircraft, makes rotation more intuitive
		this.setOrigin(size.x / 2, size.y / 2);

		this.setScale(0.5f);

		// set bounds so the aircraft is clickable
		this.setBounds(getX() - getWidth() / 2, getY() - getWidth() / 2,
				getWidth(), getHeight());

		// set rotation & velocity angle to fit next waypoint
		float relativeAngle = relativeAngleToWaypoint();

		this.velocity.setAngle(relativeAngle);
		this.setRotation(relativeAngle);

		Debug.msg("||\nGenerated aircraft id " + id + "\nEntry point: "
				+ coords + "\nRelative angle to first waypoint: "
				+ relativeAngle + "\nVelocity" + velocity + "\nWaypoints: "
				+ waypoints + "\n||");
	}

	/**
	 * Additional drawing for if the aircraft is breaching
	 * 
	 * @param batch
	 */
	@Override
	protected void additionalDraw(SpriteBatch batch) {

		ShapeRenderer drawer = AbstractScreen.shapeRenderer;

		// if the user takes control of the aircraft, 
		// show full flight plan.
		if (selected) {
			//Initialises current to plane's current position.
			Vector2 current = new Vector2(this.getX(), this.getY());
			//Loops through waypoints in flight plan drawing a line between them
			for (int i = 0; i < waypoints.size(); i++)
			{
				Vector2 nextWaypoint = new Vector2(waypoints.get(i).getX(), waypoints.get(i).getY());
				
				
				batch.end();

				drawer.begin(ShapeType.Line);
				drawer.setColor(1, 0, 0, 0);
				drawer.line(current.x, current.y, nextWaypoint.x, nextWaypoint.y);
				drawer.end();

				batch.begin();
				
				current = nextWaypoint;
			}
		}

		// if the aircraft is either selected or is breaching, draw a circle
		// around it
		if (selected || breaching) {
			batch.end();

			drawer.begin(ShapeType.Line);
			drawer.setColor(1, 0, 0, 0);
			drawer.circle(getX(), getY(), getSeparationRadius() * 0.5f);
			drawer.end();

			batch.begin();
		}

		// draw the altitude for each aircraft
		Color color;

		if (getAltitude() <= 7500) {
			color = Color.GREEN;
		} else if (getAltitude() <= 12500) {
			color = Color.ORANGE;
		} else if (getAltitude() > 12500) {
			color = Color.RED;
		} else {
			color = Color.BLACK;
		}

		AbstractScreen.drawString("alt: " + getAltitude(), getX() - 30, getY() - 20,
				color, batch, true, 1);


		// debug line from aircraft centre to waypoint centre
		if (Config.DEBUG_UI && waypoints.size() > 0) {
			Vector2 nextWaypoint = vectorToWaypoint();

			batch.end();

			drawer.begin(ShapeType.Line);
			drawer.setColor(1, 0, 0, 0);
			drawer.line(getX(), getY(), nextWaypoint.x, nextWaypoint.y);
			drawer.end();

			batch.begin();
		}

	}

	/**
	 * Update the aircraft rotation & position
	 * @param  
	 */
	public void act(float delta) {

		if (!isActive)
			return;

		// handle aircraft rotation
		rotateAircraft(delta);

		// finally updating coordinates
		coords.add(velocity.cpy().scl(delta));

		// allows for smooth decent/ascent
		if (altitude > desiredAltitude) {
			this.altitude -= this.maxClimbRate * delta;
		} else if (altitude < desiredAltitude) {
			this.altitude += this.maxClimbRate * delta;
		}

		// updating bounds to make sure the aircraft is clickable
		this.setBounds(getX() - getWidth() / 2, getY() - getWidth() / 2,
				getWidth(), getHeight());

		//Landing speed and altitude changes
		Waypoint approach1 = new Waypoint(230, 275, true, false);
		Waypoint approach2 = new Waypoint(310, 195, true, false);
		if (this.getNextWaypoint().getCoords().equals(approach1.getCoords()) || this.getNextWaypoint().getCoords().equals(approach2.getCoords())){
			this.setSpeed(400 / Config.AIRCRAFT_SPEED_MULTIPLIER);
		}
		
		Waypoint runwayStart = new Waypoint(310, 275, true, false);
		if (this.getNextWaypoint().getCoords().equals(runwayStart.getCoords())){
			this.setSpeed(400 / Config.AIRCRAFT_SPEED_MULTIPLIER);
			this.altitude = 5000;
		}
		
		Waypoint runwayMid = new Waypoint(387, 335, true, false);
		if (this.getNextWaypoint().getCoords().equals(runwayMid.getCoords())){
			this.setSpeed(200 / Config.AIRCRAFT_SPEED_MULTIPLIER);
			this.altitude = 2500;
		}
		
		Waypoint runwayEnd = new Waypoint(464, 395, true, false);
		if (this.getNextWaypoint().getCoords().equals(runwayEnd.getCoords())){
			this.minSpeed = 0.00000000001f;
			this.setSpeed(minSpeed);
			this.altitude = 0;
			// Plane has landed! - "Another Happy Landing!" - Obi Wan Kenobi, Star Wars Episode III "Revenge of the Sith."
			AircraftController.setLanding(false);
		}
		
		// finally, test waypoint collisions using new coordinates
		testWaypointCollisions();

		// test screen boundary
		if (isActive)
		{
			if (getX() < -10 ||
				getY() < -10 ||
				getX() > Config.SCREEN_WIDTH - 190 ||
				getY() > Config.SCREEN_HEIGHT + 105) {

				isActive = false;
				Debug.msg("Aircraft id " + id + ": Out of bounds, last coordinates: " + coords);
			}
		}
	}

	/**
	 * Calculate the angle between the aircraft's coordinates and the vector the
	 * next waypoint
	 * 
	 * @param waypoint
	 * @return angle IN DEGREES, NOT RADIANS
	 */
	private float angleCoordsToWaypoint(Vector2 waypoint) {
		Vector2 way = new Vector2(waypoint.x - coords.x, waypoint.y - coords.y)
				.nor();
		Vector2 coord = velocity.cpy().nor();

		float angle = (float) Math.acos(way.dot(coord) / way.len()
				* coord.len())
				* MathUtils.radiansToDegrees;
		
		return angle;
	}

	/**
	 * Calculates the vector to the next waypoint
	 * 
	 * @return 3d vector to the next waypoint
	 */
	private Vector2 vectorToWaypoint() {
		// Creates a new vector to store the new velocity in temporarily
		Vector2 nextWaypoint = new Vector2();

		// round it to 2 points after decimal, makes it more manageable later
		nextWaypoint.x = (float) (Math
				.round(waypoints.get(0).getCoords().x * 100.0) / 100.0);
		nextWaypoint.y = (float) (Math
				.round(waypoints.get(0).getCoords().y * 100.0) / 100.0);

		return nextWaypoint;
	}

	/**
	 * Calculate relative angle of the aircraft to the next waypoint
	 * 
	 * @return relative angle in degrees, rounded to 2 points after decimal
	 */
	private float relativeAngleToWaypoint() {
		return relativeAngleToWaypoint(vectorToWaypoint());
	}

	/**
	 * Calculate relative angle of the aircraft to a waypoint
	 * 
	 * @param waypoint
	 * @return angle in degrees, rounded to 2 points after decimal
	 */
	private float relativeAngleToWaypoint(Vector2 waypoint) {
		return new Vector2(waypoint.x - getX(), waypoint.y - getY()).angle();
	}

	/**
	 * Handles aircraft rotation during the act method call
	 * @param delta time step
	 */
	private void rotateAircraft(float delta)
	{
		float baseRate = maxTurningRate * delta;
		float rate = 0;

		// Calculate turning rate
		if (turnRight)
		{
			ignorePath = true;
			rate = -baseRate;
		}
		else if (turnLeft)
		{
			ignorePath = true;
			rate = baseRate;
		}
		else if (!ignorePath && waypoints.size() > 0)
		{
			// Vector to next waypoint
			Vector2 nextWaypoint = vectorToWaypoint();

			// relative angle from the aircraft coordinates to the next waypoint
			float relativeAngle = angleCoordsToWaypoint(nextWaypoint);

			// smoothly rotate aircraft
			// sets a threshold due to float imprecision, should be generally
			// relativeAngle != 0
			if (relativeAngle > 1) {

				// if the current angle is bigger than the previous, it means we
				// are rotating towards the wrong side
				if (previousAngle < relativeAngle) {
					// switch to rotate to the other side
					rotateRight = (!rotateRight);
				}

				// instead of using two rotation variables, it is enough to
				// store one and just switch that one
				if (rotateRight) {
					rate = -baseRate;
				} else {
					rate = baseRate;
				}

				// save the current angle as the previous angle for the next
				// iteration
				previousAngle = relativeAngle;
			}
		}

		// Do the turning (while handling wraparound)
		if (rate != 0)
		{
			float newRotation = getRotation() + rate;
			if (newRotation < 0)
			{
				newRotation += 360;
			}
			else if (newRotation > 360)
			{
				newRotation -= 360;
			}

			setRotation(newRotation);
			velocity.setAngle(getRotation());
		}
	}

	/**
	 * Tests whether this aircraft has collided with any waypoints
	 */
	private void testWaypointCollisions()
	{
		int numWaypoints = waypoints.size();

		if (numWaypoints > 0)
		{
			float distanceToWaypoint = coords.cpy().sub(waypoints.get(0).getCoords()).len();

			if (numWaypoints == 1)
			{
				if (distanceToWaypoint < Config.EXIT_WAYPOINT_SIZE.x / 2)
				{
					// Collided with exit point
					AircraftController.score += 77;
					Debug.msg("Aircraft id " + id + ": Reached exit WP");

					waypoints.clear();
					isActive = false;
				}
			}
			else
			{
				if (distanceToWaypoint < Config.WAYPOINT_SIZE.x / 2)
				{
					// Collided with normal waypoint
					AircraftController.score += 111;
					Debug.msg("Aircraft id " + id + ": Hit waypoint");

					waypoints.remove(0);
				}
			}
		}
	}

	/**
	 * Adding a new waypoint to the head of the arraylist
	 * 
	 * @param newWaypoint
	 */
	public void insertWaypoint(Waypoint newWaypoint) {
		waypoints.add(0, newWaypoint);
	}
	
	public Waypoint getNextWaypoint() {
		return waypoints.get(0);
	}
	
	/**
	 * Increase speed of the aircraft <br>
	 * Actually changes a scalar which is later multiplied by the velocity
	 * vector
	 */
	public void increaseSpeed() {

		float prevSpeed = getSpeed();
		float newSpeed = prevSpeed + SPEED_CHANGE;

		if (newSpeed > maxSpeed)
			newSpeed = maxSpeed;

		setSpeed(newSpeed);
		Debug.msg("Increasing speed; New Speed: " + newSpeed);
	}

	/**
	 * Decrease speed of the aircraft <br>
	 * Actually changes a scalar which is later multiplied by the velocity
	 * vector
	 */
	public void decreaseSpeed() {


		float prevSpeed = getSpeed();
		float newSpeed = prevSpeed - SPEED_CHANGE;

		if (newSpeed < minSpeed)
			newSpeed = minSpeed;

		setSpeed(newSpeed);
		Debug.msg("Decreasing speed; New Speed: " + newSpeed);
	}

	/**
	 * Increases rate of altitude change
	 */
	public void increaseAltitude() {
		if (desiredAltitude + ALTITUDE_CHANGE > 15000)
			return;

		this.desiredAltitude += ALTITUDE_CHANGE;
	}

	/**
	 * Decreasing rate of altitude change
	 */
	public void decreaseAltitude() {
		if (desiredAltitude - ALTITUDE_CHANGE < 5000)
			return;

		this.desiredAltitude -= ALTITUDE_CHANGE;
	}

	public void turnRight(boolean set) {
		turnRight = set;
	}

	public void turnLeft(boolean set) {
		turnLeft = set;
	}

	/**
	 * Causes the aircraft to return to its flightplan after being manually controlled
	 */
	public void returnToPath()
	{
		turnLeft = false;
		turnRight = false;
		ignorePath = false;
	}

	/**
	 * Creates and inserts landing flightplan
	 * 
	 * - Creates waypoints at start and end of waypoints invisible to user
	 * - Calculates position of aircraft relative to runway and selects appropriate approach waypoint position
	 * - Creates appropriate invisible approach waypoint
	 * - Adds route to start of flightplan
	 * 
	 * - Changes in altitude and speed are handled in act.
	 */
	public void landAircraft(){
		if (!selected || AircraftController.getLanding())
			return;
		AircraftController.setLanding(true);
		Waypoint runwayEnd = new Waypoint(464, 395, true, false);
		Waypoint runwayMid = new Waypoint(387, 335, true, false);
		Waypoint runwayStart = new Waypoint(310, 275, true, false);
		Waypoint approach = null;
		int choice = 0;
		//Calculates if aircraft is in Pos A or B to decide which approach waypoint to use.
		//
		//--------------
		//|          _/|
		//| A      _/  |
		//|      _/    |
		//|    _/      |
		//|  _/     B  |
		//|_/          |
		//--------------
		//
		//Adds 1 to avoid 0 error
		if (((this.getX() + 1) / (this.getY() + 1)) > 1.8){
			choice = 1;
		}
		if (choice == 0){
			approach = new Waypoint(230, 275, true, false);
		} else {
			approach = new Waypoint(310, 195, true, false);
		}
		
		this.insertWaypoint(runwayEnd);
		this.insertWaypoint(runwayMid);
		this.insertWaypoint(runwayStart);
		this.insertWaypoint(approach);
	}
	
	/**
	 * Get the whole flightplan for this aircraft
	 * 
	 * @return flightplan
	 */
	public ArrayList<Waypoint> getFlightPlan() {
		return waypoints;
	}

	/**
	 * Regular regular getter for radius
	 * 
	 * @return int radius
	 */
	public float getRadius() {
		return radius;
	}

	public float getSeparationRadius() {
		return separationRadius;
	}

	public void isBreaching(boolean is) {
		this.breaching = is;
	}

	public int getAltitude() {
		return altitude;
	}


	/**
	 * Sets the speed of the aircraft (ignoring minimum and maximum speeds)
	 *
	 * @param speed new speed
	 */
	private void setSpeed(float speed)
	{
		if (speed == 0)
			throw new IllegalArgumentException("speed cannot be 0");

		velocity.clamp(speed, speed);
	}

	/**
	 * Returns aircraft speed (pixels per second)
	 * 
	 * @return the velocity scalar
	 */
	public float getSpeed() {
		return velocity.len();
	}

	/**
	 * Returns false if aircraft has hit the exit point or if it is off the screen
	 * 
	 * @return whether is active
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Setter for selected
	 * 
	 * @param newSelected
	 * @return whether is selected
	 */
	public boolean selected(boolean newSelected) {
		return this.selected = newSelected;
	}

	@Override
	public String toString() {
		return "Aircraft - x: " + getX() + " y: " + getY()
				+ "\n\r flight plan: " + waypoints.toString();
	}
	

}
