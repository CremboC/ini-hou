package seprini.models;

import seprini.data.Art;

import com.badlogic.gdx.math.Vector2;

public class PauseOverlay extends Entity {

	/**
	 * The pause overlay in MP which displays the controls for the game
	 */
	public PauseOverlay() {
		texture = Art.getTextureRegion("pauseOverlay");
		coords = new Vector2(1280 / 2, 720 / 2);
		size = new Vector2(720, 1280);

		setOrigin(size.x / 2, size.y / 2);
		setRotation(270);

	}
}
