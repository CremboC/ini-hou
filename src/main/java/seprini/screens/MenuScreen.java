package seprini.screens;

import seprini.ATC;
import seprini.controllers.MenuController;
import seprini.data.Art;
import seprini.data.Config;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Menu Screen, displays the menu with buttons to start and exit the game
 */
public class MenuScreen extends AbstractScreen {
	private final Table ui;

	/**
	 * Initialises the input handler, stage and create the layout with buttons
	 * for the menu screen
	 */
	public MenuScreen(ATC game) {

		super(game);

		// Create table and add to stage
		ui = new Table();
		getStage().addActor(ui);

		// creater the controller for this screen, this will handle basically
		// everything for this screen, including input and creation of buttons
		new MenuController(this, ui);

		// make it fill the whole screen
		ui.setFillParent(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Stage root = getStage();

		drawString(Config.COPYRIGHT_NOTICE, 10, 10, Color.BLACK,
				root.getSpriteBatch(), false, 0.5f);

		draw(Art.getTextureRegion("menuAircraft"), 300, 400,
				root.getSpriteBatch());

		draw(Art.getTextureRegion("libgdx"), 1228, 0, root.getSpriteBatch());
	}

	@Override
	public void show() {
		super.show();
		Art.getSound("comeflywithme").play(1f);
	}
}
