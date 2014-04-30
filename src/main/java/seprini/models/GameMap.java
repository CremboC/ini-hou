package seprini.models;

import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameMode;

import com.badlogic.gdx.math.Vector2;

public final class GameMap extends Entity {

	// names of the textures for the multiplayer and single player maps
	public final static String MP_AIRSPACE = "mp-airspace";
	public final static String SP_AIRSPACE = "airspace";

	public GameMap(GameMode mode) {

		// switches map texture depending on the game mode
		switch (mode) {
			case SINGLE :

				this.texture = Art.getTextureRegion(SP_AIRSPACE);
				this.coords = new Vector2(Config.AIRSPACE_SIZE.x / 2,
						Config.AIRSPACE_SIZE.y / 2);
				this.size = Config.AIRSPACE_SIZE;

				break;

			case MULTI :

				this.texture = Art.getTextureRegion(MP_AIRSPACE);
				this.coords = new Vector2(Config.MULTIPLAYER_SIZE.x / 2,
						Config.MULTIPLAYER_SIZE.y / 2);
				this.size = Config.MULTIPLAYER_SIZE;

				break;

			default :
				break;
		}


	}
}
