package seprini.models.types;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

/**
 * Sets all of the controls for the different players. Passed to aircraft to
 * later get the correct controls
 * 
 * @author Crembo
 * 
 */
public class Player {

	public final static int ONE = 0;
	public final static int TWO = 1;

	private final int LEFT, RIGHT, ALT_INC, ALT_DEC, SPEED_INC, SPEED_DEC,
			RETURN_TO_PATH, SWITCH_PLANE, TAKEOFF;

	protected int number;

	protected Color circleColor;

	public Player(int playerNumber) {
		switch (playerNumber) {
			default :
			case ONE :
				LEFT = Keys.A;
				RIGHT = Keys.D;

				ALT_INC = Keys.W;
				ALT_DEC = Keys.S;

				SPEED_INC = Keys.E;
				SPEED_DEC = Keys.Q;

				RETURN_TO_PATH = Keys.R;
				SWITCH_PLANE = Keys.TAB;

				TAKEOFF = Keys.F;

				number = Player.ONE;
				circleColor = Color.RED;
				break;

			case TWO :
				LEFT = Keys.NUMPAD_4;
				RIGHT = Keys.NUMPAD_6;

				ALT_INC = Keys.NUMPAD_8;
				ALT_DEC = Keys.NUMPAD_2;

				SPEED_INC = Keys.NUMPAD_7;
				SPEED_DEC = Keys.NUMPAD_9;

				RETURN_TO_PATH = Keys.NUMPAD_0;
				SWITCH_PLANE = Keys.ENTER;

				TAKEOFF = Keys.NUMPAD_1;

				number = Player.TWO;
				circleColor = Color.BLUE;
				break;
		}
	}

	/**
	 * Get key for turning left
	 * 
	 * @return
	 */
	public int getLeft() {
		return LEFT;
	}

	/**
	 * Get key for turning right
	 * 
	 * @return
	 */
	public int getRight() {
		return RIGHT;
	}

	/**
	 * Get key to increase altitude
	 * 
	 * @return
	 */
	public int getAltIncrease() {
		return ALT_INC;
	}

	/**
	 * Get key to decrease altitude
	 * 
	 * @return
	 */
	public int getAltDecrease() {
		return ALT_DEC;
	}

	/**
	 * Get key to increase speed
	 * 
	 * @return
	 */
	public int getSpeedIncrease() {
		return SPEED_INC;
	}

	/**
	 * Gey key to decrease speed
	 * 
	 * @return
	 */
	public int getSpeedDecrease() {
		return SPEED_DEC;
	}

	/**
	 * Get key to return to path (restore aircraft's route after taking direct
	 * control)
	 * 
	 * @return
	 */
	public int getReturnToPath() {
		return RETURN_TO_PATH;
	}

	/**
	 * Get key to switch between aircraft
	 * 
	 * @return
	 */
	public int getSwitchPlane() {
		return SWITCH_PLANE;
	}

	/**
	 * Get the key to make an aircraft takeoff from the appropriate airport in
	 * MP
	 * 
	 * @return
	 */
	public int getTakeoff() {
		return TAKEOFF;
	}

	/**
	 * Gets which player this is - one or two
	 * 
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * The colour for the circle when an aircraft it selected. Depends on which
	 * player selects.
	 * 
	 * @return
	 */
	public Color getColor() {
		return circleColor;
	}
}