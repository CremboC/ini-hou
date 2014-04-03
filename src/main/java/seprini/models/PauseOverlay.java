package seprini.models;

import seprini.data.Art;

import com.badlogic.gdx.math.Vector2;

public class PauseOverlay extends Entity {

	public PauseOverlay() {
		this.texture = Art.getTextureRegion("pauseOverlay");
		this.coords = new Vector2(1280 / 2, 720 / 2);
		this.size = new Vector2(720, 1280);

		this.setOrigin(size.x / 2, size.y / 2);
		this.setRotation(270);

	}
}
