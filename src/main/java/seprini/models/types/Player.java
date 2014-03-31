package seprini.models.types;

import com.badlogic.gdx.Input.Keys;

public class Player {

	public final static int ONE = 0;
	public final static int TWO = 1;

	public int LEFT, RIGHT, ALT_INC, ALT_DEC, SPEED_INC, SPEED_DEC,
			RETURN_TO_PATH, SWITCH_PLANE;

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
			break;
		}
	}

	public int getLeft() {
		return LEFT;
	}

	public int getRight() {
		return RIGHT;
	}

	public int getAltIncrease() {
		return ALT_INC;
	}

	public int getAltDecrease() {
		return ALT_DEC;
	}

	public int getSpeedIncrease() {
		return SPEED_INC;
	}

	public int getSpeedDecrease() {
		return SPEED_DEC;
	}

	public int getReturnToPath() {
		return RETURN_TO_PATH;
	}

	public int getSwitchPlane() {
		return SWITCH_PLANE;
	}
}