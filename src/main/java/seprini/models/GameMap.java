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

				texture = Art.getTextureRegion(SP_AIRSPACE);
				coords = new Vector2(Config.AIRSPACE_SIZE.x / 2,
						Config.AIRSPACE_SIZE.y / 2);
				size = Config.AIRSPACE_SIZE;

				break;

			case MULTI :

				texture = Art.getTextureRegion(MP_AIRSPACE);
				coords = new Vector2(Config.MULTIPLAYER_SIZE.x / 2,
						Config.MULTIPLAYER_SIZE.y / 2);
				size = Config.MULTIPLAYER_SIZE;

				break;

			default :
				break;
		}


	}
}
