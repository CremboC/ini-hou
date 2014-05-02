package seprini.screens;

import seprini.ATC;
import seprini.data.Art;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class EndScreen extends AbstractScreen {
	public EndScreen(ATC game, float time, float score) {

		super(game);

		Stage root = getStage();
		Table ui = new Table();

		ui.setFillParent(true);
		root.addActor(ui);

		ui.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ESCAPE)
					getGame().showMenuScreen();

				return false;
			}
		});

		root.setKeyboardFocus(ui);

		Label scoreLabel = new Label("You have earned: " + Math.round(score)
				+ " Dolla", Art.getSkin(), "bigTextStyle");

		ui.add(scoreLabel).center();

		ui.row();

		Label text = new Label(
				"You have failed.\n"
						+ "You have allowed two aircraft to collided, resulting in death and distruction of a sort not seen for a millenia.\n"
						+ "You spent "
						+ Math.round(time)
						+ " seconds in employment and would have earnt "
						+ Math.round(score)
						+ " Dolla had you not been knocked up in some Cuban prison for your actions. \n",
				Art.getSkin(), "textStyle");

		ui.add(text).center();

		ui.row();

		TextButton button = new TextButton("Menu", Art.getSkin());

		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getGame().showMenuScreen();
			}
		});

		ui.add(button).width(150);
	}
}
