package seprini.models.types;

import com.badlogic.gdx.Input.Keys;

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

	public int LEFT, RIGHT, ALT_INC, ALT_DEC, SPEED_INC, SPEED_DEC,
			RETURN_TO_PATH, SWITCH_PLANE;

	public int number;

	public Player(int playerNumber) {
		switch (playerNumber) {
		case ONE:
			LEFT = Keys.A;
			RIGHT = Keys.D;

			ALT_INC = Keys.W;
			ALT_DEC = Keys.A;

			SPEED_INC = Keys.E;
			SPEED_DEC = Keys.Q;

			RETURN_TO_PATH = Keys.R;
			SWITCH_PLANE = Keys.TAB;

			number = Player.ONE;
			break;

		case TWO:
			LEFT = Keys.LEFT;
			RIGHT = Keys.RIGHT;

			ALT_INC = Keys.UP;
			ALT_DEC = Keys.DOWN;

			SPEED_INC = Keys.NUM_8;
			SPEED_DEC = Keys.NUM_2;

			RETURN_TO_PATH = Keys.NUM_0;
			SWITCH_PLANE = Keys.NUM_5;

			number = Player.TWO;
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
	 * Gets which player this is - one or two
	 * 
	 * @return
	 */
	public int getNumber() {
		return number;
	}
}