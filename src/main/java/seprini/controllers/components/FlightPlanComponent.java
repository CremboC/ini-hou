package seprini.controllers.components;

import java.util.ArrayList;
import java.util.Random;

import seprini.data.Config;
import seprini.models.Airport;
import seprini.models.Entrypoint;
import seprini.models.Waypoint;

import com.badlogic.gdx.math.Vector2;

public class FlightPlanComponent {

	static Random rand = new Random();
	private final static double airportGradient = 1.3;

	private final ArrayList<Waypoint> permanentWaypoints, exitPointList;
	private final ArrayList<Entrypoint> entryPointList;

	public FlightPlanComponent(WaypointComponent waypoints) {

		permanentWaypoints = waypoints.getPermanentList();
		exitPointList = waypoints.getExitList();
		entryPointList = waypoints.getEntryList();
	}

	public FlightPlanComponent(ArrayList<Waypoint> permanentWaypoints,
			ArrayList<Waypoint> exitPointList,
			ArrayList<Entrypoint> entryPointList) {

		this.permanentWaypoints = permanentWaypoints;
		this.exitPointList = exitPointList;
		this.entryPointList = entryPointList;
	}

	/**
	 * Generates a flight plan - a list of waypoints - for aircraft. Aircraft
	 * with the same entry and exit points will always follow the same route.
	 * 
	 * @return completeFlightPlan
	 */
	public ArrayList<Waypoint> generate() {

		// Initialisation of parameters required by flightPlanWaypointGenerator.
		ArrayList<Waypoint> flightPlan = new ArrayList<Waypoint>();
		Waypoint entryWaypoint = setStartpoint();
		Waypoint lastWaypoint = setEndpoint(entryWaypoint,
				Config.MIN_DIST_BETWEEN_ENTRY_EXIT_WAYPOINTS);
		// entryWaypoint immediately added to aircrafts flightPlan.
		flightPlan.add(entryWaypoint);
		return flightPlanWaypointGenerator(flightPlan, entryWaypoint,
				lastWaypoint);
	}

	/**
	 * Overload to use when aircraft is taking off from an airport, allowing
	 * entry waypoint to be specified.
	 * 
	 * @param entryWaypoint
	 * @return completeFlightPlan
	 */
	public ArrayList<Waypoint> generate(Waypoint entryWaypoint) {

		// Initialisation of parameters required by flightPlanWaypointGenerator.
		ArrayList<Waypoint> flightPlan = new ArrayList<Waypoint>();
		Waypoint lastWaypoint = setEndpoint(entryWaypoint,
				Config.MIN_DIST_BETWEEN_ENTRY_EXIT_WAYPOINTS);
		// entryWaypoint immediately added to aircrafts flightPlan.
		flightPlan.add(entryWaypoint);
		if (entryWaypoint instanceof Airport) {
			flightPlan.add(((Airport) entryWaypoint).runwayEnd);
		}
		return flightPlanWaypointGenerator(flightPlan, entryWaypoint,
				lastWaypoint);
	}

	/**
	 * Adds a selection of waypoints + lastWaypoint to flighPlan.
	 * 
	 * @param flightPlan
	 * @param currentWaypoint
	 * @param lastWaypoint
	 * @return completeFlightPlan
	 */
	private ArrayList<Waypoint> flightPlanWaypointGenerator(
			ArrayList<Waypoint> flightPlan, Waypoint currentWaypoint,
			Waypoint lastWaypoint) {

		// Base Case; self explanatory.
		if (currentWaypoint.equals(lastWaypoint)) {
			// create an exception here for if lastWaypoint is an airport.
			if (lastWaypoint instanceof Airport) {
				// Check which airport it is and insert the start of the runway
				// to flightplan.
				Waypoint previousWaypoint = flightPlan
						.get(flightPlan.size() - 2);
				flightPlan.add(flightPlan.size() - 1,
						((Airport) lastWaypoint).runwayStart);
				// Now decide which landing waypoint aircraft is to use,
				// dependent on the previous waypoint's position.
				if ((previousWaypoint.getX() > airportGradient
						* previousWaypoint.getY() - lastWaypoint.getY()
						+ lastWaypoint.getX())) {
					flightPlan.add(flightPlan.size() - 2,
							((Airport) lastWaypoint).runwayRight);
				} else {
					flightPlan.add(flightPlan.size() - 2,
							((Airport) lastWaypoint).runwayLeft);
				}
			}
			return flightPlan;
		}

		else {
			// Find normal vector from currentWaypoint to lastWaypoint and
			// normalise.
			Vector2 normalVectorFromCurrentToLast = (lastWaypoint.getCoords()
					.cpy().sub(currentWaypoint.getCoords())).nor();

			// Create the list of waypoints for the generator to choose from,
			// including the final waypoint so that the base case can be
			// satisfied;
			ArrayList<Waypoint> waypointSelectionList = new ArrayList<Waypoint>(
					permanentWaypoints);
			waypointSelectionList.add(lastWaypoint);

			// Call selectNextWaypoint.
			Waypoint nextWaypoint = selectNextWaypoint(currentWaypoint,
					lastWaypoint, flightPlan, normalVectorFromCurrentToLast,
					waypointSelectionList, 30, 100);

			waypointSelectionList.clear();

			// Recurse with updated flightPlan and nextWaypoint.
			return flightPlanWaypointGenerator(flightPlan, nextWaypoint,
					lastWaypoint);
		}
	}

	/**
	 * Selects a waypoint to insert into flightPlan, under certain constraints.
	 * 
	 * @param currentWaypoint
	 * @param flightPlan
	 * @param normalVectorFromCurrentToLast
	 * @param waypointSelectionList
	 * @param maxAngle
	 *            - minimum angle from currentWaypoint to nextWaypoint, where 0
	 *            degrees is the angle from currentWaypoint to lastWaypoint
	 * @param minDistance
	 *            - minimum distance from currentWaypoint to nextWaypoint. Ideal
	 *            value = diameter of the turning circle of the aircraft.
	 * @return nextWaypoint
	 */
	private static Waypoint selectNextWaypoint(Waypoint currentWaypoint,
			Waypoint lastWaypoint, ArrayList<Waypoint> flightPlan,
			Vector2 normalVectorFromCurrentToLast,
			ArrayList<Waypoint> waypointSelectionList, int maxAngle,
			int minDistance) {
		Waypoint nextWaypoint = null;

		for (Waypoint waypoint : waypointSelectionList) {
			// Find normal vector from current item in waypointSelectionList to
			// lastWaypoint.
			Vector2 normalVectorFromCurrentToPotential = new Vector2(waypoint
					.getCoords().cpy().sub(currentWaypoint.getCoords())).nor();
			// Check that waypoint in waypointSelectoinList:
			// 1. Is not already in flighPlan
			// 2. Angle between normalVectorFromCurrentToPotential and
			// normalVectorFromCurrentToLast is less than specified maxAngle.
			// 3. Is minDistance away from currentWaypoint
			if (!flightPlan.contains(waypoint)
					// the acos returns a value of radians, which is then
					// converted to degrees.
					&& (Math.acos(normalVectorFromCurrentToPotential
							.dot(normalVectorFromCurrentToLast)) * 180 / Math.PI) < maxAngle
					&& waypoint.getCoords().dst(currentWaypoint.getCoords()) > minDistance
					&& waypoint.getCoords().dst(lastWaypoint.getCoords()) > minDistance
					&& waypoint.getCoords().dst(
							flightPlan.get(flightPlan.size() - 1).getCoords()) <= lastWaypoint
							.getCoords().dst(
									flightPlan.get(flightPlan.size() - 1)
											.getCoords())) {
				// If all conditions are met, choose this waypoint as the
				// nextWaypoint.
				nextWaypoint = waypoint;
				break;
			}
		}
		if (nextWaypoint == null) {
			nextWaypoint = lastWaypoint;
		}

		// add nextWaypoint to flightPlan.
		flightPlan.add(nextWaypoint);
		return nextWaypoint;
	}

	/**
	 * Selects random waypoint from entrypointList.
	 * 
	 * @return Waypoint
	 */
	private Waypoint setStartpoint() {
		return entryPointList.get(rand.nextInt(entryPointList.size()));
	}

	/**
	 * Selects random exitpoint from exitpointList, that is at least minDistance
	 * away from the aircrafts entryWaypoint and doesn't lie on either the same
	 * x or y coord.
	 * 
	 * @param entryWaypoint
	 *            - where this aircraft entered the game
	 * @param minDistance
	 *            - desired minimum distance between aircraft's entryWaypoint
	 *            and its exitWaypoint.
	 * @return Waypoint
	 */
	private Waypoint setEndpoint(Waypoint entryWaypoint, int minDistance) {
		Waypoint chosenExitPoint = exitPointList.get(rand.nextInt(exitPointList
				.size()));
		if (chosenExitPoint.getCoords().dst(entryWaypoint.getCoords()) < minDistance
				|| chosenExitPoint.getCoords().x == entryWaypoint.getCoords().x
				|| chosenExitPoint.getCoords().y == entryWaypoint.getCoords().y) {
			chosenExitPoint = setEndpoint(entryWaypoint, minDistance);
		}

		return chosenExitPoint;
	}

}
