package seprini.screens;

import seprini.ATC;
import seprini.controllers.AircraftController;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.data.GameMode;
import seprini.models.Airspace;
import seprini.models.PauseOverlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MultiplayerScreen extends AbstractScreen {

	private final AircraftController controller;
	private final PauseOverlay overlay;

	private boolean added = false;

	public MultiplayerScreen(ATC game, GameDifficulty diff) {
		super(game);

		// create a table layout, main ui
		Stage root = getStage();
		Table ui = new Table();

		if (Config.DEBUG_UI)
			ui.debug();

		// create and add the Airspace group, contains aircraft and waypoints
		Airspace airspace = new Airspace();

		controller = new AircraftController(diff, airspace, this,
				GameMode.MULTI);

		root.setKeyboardFocus(airspace);

		// set controller update as first actor
		ui.addActor(new Actor() {
			@Override
			public void act(float delta) {
				controller.update(delta);
			}
		});

		// make it fill the whole screen
		ui.setFillParent(true);
		root.addActor(ui);

		airspace.addListener(controller);
		ui.add(airspace).width(Config.MULTIPLAYER_SIZE.x)
				.height(Config.MULTIPLAYER_SIZE.y);

		Art.getSound("ambience").playLooping(0.7f);

		overlay = new PauseOverlay();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// temporary drawing of no man's land

		getStage().getSpriteBatch().begin();

		AbstractScreen.drawLine(Color.RED, 540, 0, 540, 720, getStage()
				.getSpriteBatch());

		AbstractScreen.drawLine(Color.RED, 740, 0, 740, 720, getStage()
				.getSpriteBatch());

		getStage().getSpriteBatch().end();

		// end temporary drawing of no man's land


		if (Config.DEBUG_UI) {
			Stage root = getStage();

			Table.drawDebug(root);
		}
	}

	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;

		if (paused) {
			getStage().addActor(overlay);
		} else {
			overlay.remove();
		}
	}

}
