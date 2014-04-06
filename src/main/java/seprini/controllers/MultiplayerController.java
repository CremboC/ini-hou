package seprini.controllers;

import seprini.controllers.components.FlightPlanComponent;
import seprini.controllers.components.WaypointComponent;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.Debug;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.Map;
import seprini.models.Waypoint;
import seprini.models.types.Player;
import seprini.screens.ScreenBase;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MultiplayerController extends AircraftController {

	protected Aircraft[] selectedAircraft = { null, null };

	public MultiplayerController(GameDifficulty diff, Airspace airspace,
			ScreenBase screen) {
		super(diff, airspace, screen);
	}

	@Override
	protected void init() {

		this.mode = GameMode.MULTI;

		// add the background
		airspace.addActor(new Map(mode));

		// manages the waypoints
		this.waypoints = new WaypointComponent(this, mode);

		// helper for creating the flight plan of an aircraft
		this.flightplan = new FlightPlanComponent(waypoints);
	}

	/**
	 * Selects an aircraft.
	 * 
	 * @param aircraft
	 */
	@Override
	protected void selectAircraft(Aircraft aircraft) {
		
		// Cannot select in the No Man's Land
		if (aircraft.getCoords().x >= 540  && aircraft.getCoords().x <= 740){
			return;
		}
		// make sure old selected aircraft is no longer selected in its own
		// object
		Aircraft playerAircraft = selectedAircraft[aircraft.getPlayer()
				.getNumber()];

		if (playerAircraft != null) {

			playerAircraft.selected(false);

			// make sure the old aircraft stops turning after selecting a new
			// aircraft; prevents it from going in circles
			playerAircraft.turnLeft(false);
			playerAircraft.turnRight(false);
			
		}

		// set new selected aircraft
		selectedAircraft[aircraft.getPlayer().getNumber()] = aircraft;

		// make new aircraft know it's selected
		selectedAircraft[aircraft.getPlayer().getNumber()].selected(true);
	}

    public void deselectAircraft(Aircraft aircraft) {
    	 
        Aircraft selected = selectedAircraft[aircraft.getPlayer().getNumber()];
       
        if (selected != null) {
                selected.selected(false);

                selected.turnLeft(false);
                selected.turnRight(false);

                selected = null;
        }
}

	/**
	 * Switch the currently selected aircraft
	 */
	@Override
	protected void switchAircraft(int playerNumber) {
		int index = lastAircraftIndex + 1;
		Aircraft plane;

		// to prevent out of bounds exception
		if (aircraftList.size() == 0) {
			return;
		}

		// if we increase the index by too much, it will give an an exception -
		// restore index to the first one and loop again
		try {
			plane = aircraftList.get(index);
		} catch (IndexOutOfBoundsException e) {
			index = 0;
			lastAircraftIndex = 0;
		}

		plane = aircraftList.get(index);

		if (plane.getPlayer().getNumber() == playerNumber) {
			selectAircraft(plane);
		}

		lastAircraftIndex = index;
	}

	public Aircraft[] getSelectedAircrafts() {
		return selectedAircraft;
	}

	/**
	 * Redirects aircraft to another waypoint.
	 * 
	 * @param waypoint
	 *            Waypoint to redirect to
	 */
	@Override
	public void redirectAircraft(Waypoint waypoint) {
		Debug.msg("Redirecting aircraft " + 0 + " to " + waypoint);

		if (getSelectedAircraft() == null)
			return;

		getSelectedAircrafts()[Player.ONE].insertWaypoint(waypoint);
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts as alternatives to the on screen buttons
	 */
	public boolean keyDown(InputEvent event, int keycode) {
		if (!screen.isPaused()) {

			for (int i = 0; i < selectedAircraft.length; i++) {

				if (selectedAircraft[i] != null) {
					if (keycode == selectedAircraft[i].getPlayer().getLeft())
						selectedAircraft[i].turnLeft(true);

					if (keycode == selectedAircraft[i].getPlayer().getRight())
						selectedAircraft[i].turnRight(true);

					if (keycode == selectedAircraft[i].getPlayer()
							.getAltIncrease())
						selectedAircraft[i].increaseAltitude();

					if (keycode == selectedAircraft[i].getPlayer()
							.getAltDecrease())
						selectedAircraft[i].decreaseAltitude();

					if (keycode == selectedAircraft[i].getPlayer()
							.getSpeedIncrease())
						selectedAircraft[i].increaseSpeed();

					if (keycode == selectedAircraft[i].getPlayer()
							.getSpeedDecrease())
						selectedAircraft[i].decreaseSpeed();

					if (keycode == selectedAircraft[i].getPlayer()
							.getReturnToPath())
						selectedAircraft[i].returnToPath();
				}

			}

		}

		if (keycode == players[Player.ONE].getSwitchPlane()) {
			switchAircraft(Player.ONE);
		} else if (keycode == players[Player.TWO].getSwitchPlane()) {
			switchAircraft(Player.TWO);
		}

		if (keycode == Keys.SPACE)
			screen.setPaused(!screen.isPaused());

		if (keycode == Keys.ESCAPE) {
			Art.getSound("ambience").stop();
			screen.getGame().showMenuScreen();
		}

		return false;
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts to disable the turn left and turn right buttons on screen
	 */
	public boolean keyUp(InputEvent event, int keycode) {

		for (int i = 0; i < selectedAircraft.length; i++) {

			if (selectedAircraft[i] != null) {

				if (keycode == selectedAircraft[i].getPlayer().getLeft())
					selectedAircraft[i].turnLeft(false);

				if (keycode == selectedAircraft[i].getPlayer().getRight())
					selectedAircraft[i].turnRight(false);

			}

		}

		return false;
	}
	
}
	
	

	    
			
	
			
		