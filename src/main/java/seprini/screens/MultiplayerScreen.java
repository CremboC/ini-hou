package seprini.screens;

import seprini.ATC;
import seprini.controllers.MultiplayerController;
import seprini.controllers.OverlayController;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.models.Airspace;
import seprini.models.PauseOverlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MultiplayerScreen extends AbstractScreen {

	// position of the score texts for both player
	private final static Vector2[] SCORE_POS = { new Vector2(230, 715),
			new Vector2(1010, 715) };

	private final MultiplayerController controller;
	private final PauseOverlay overlay;

	public MultiplayerScreen(ATC game, GameDifficulty diff) {
		super(game);

		// create a table layout, main ui
		Stage root = getStage();
		Table ui = new Table();
		Table overlayWrapper = new Table();

		ui.top();

		if (Config.DEBUG_UI) {
			ui.debug();
			overlayWrapper.debug();
		}

		// create and add the Airspace group, contains aircraft and waypoints
		Airspace airspace = new Airspace();

		controller = new MultiplayerController(diff, airspace, this);

		airspace.addListener(controller);
		ui.add(airspace).width(Config.MULTIPLAYER_SIZE.x)
				.height(Config.MULTIPLAYER_SIZE.y);

		final OverlayController overlayController = new OverlayController(
				controller, ui);

		root.setKeyboardFocus(airspace);

		// set controller update as first actor
		ui.addActor(new Actor() {
			@Override
			public void act(float delta) {
				try {
					controller.update(delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				overlayController.update(delta);
			}
		});

		// make it fill the whole screen
		ui.setFillParent(true);
		root.addActor(ui);

		Art.getSound("ambience").playLooping(0.7f);

		overlay = new PauseOverlay();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// temporary drawing of no man's land

		getStage().getSpriteBatch().begin();

		int[] scores = controller.getPlayerScores();

		for (int i = 0; i < scores.length; i++) {

			AbstractScreen.drawString("Score: " + scores[i], SCORE_POS[i].x,
					SCORE_POS[i].y, Color.BLUE, getStage().getSpriteBatch(),
					true, 1);
		}

		AbstractScreen.drawLine(Color.RED, 540, 0, 540, 720, getStage()
				.getSpriteBatch());

		AbstractScreen.drawLine(Color.RED, 740, 0, 740, 720, getStage()
				.getSpriteBatch());

		getStage().getSpriteBatch().end();

		// end temporary drawing of no man's land

		if (Config.DEBUG_UI) {
			Table.drawDebug(getStage());
		}
	}

	@Override
	public void setPaused(boolean paused) {
		super.setPaused(paused);

		if (paused) {
			getStage().addActor(overlay);
		} else {
			overlay.remove();
		}
	}
}
