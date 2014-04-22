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

public class MultiplayerEndScreen extends AbstractScreen {

	public MultiplayerEndScreen(ATC game, float time, float scoreOne,
			float scoreTwo, float totalScore) {

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

		Art.getSkin().getFont("default").setScale(1f);

		String winningPlayer;
		String losingPlayer;
		float winningScore;
		float losingScore;
		Label text;
		if (scoreOne != scoreTwo) {
			if (scoreOne > scoreTwo) {
				winningPlayer = "player One";
				losingPlayer = "player Two";
				winningScore = scoreOne;
				losingScore = scoreTwo;
			} else {
				losingPlayer = "player One";
				winningPlayer = "player Two";
				winningScore = scoreTwo;
				losingScore = scoreOne;
			}

			text = new Label("Game Over!\n\n" + winningPlayer
					+ " won the game with " + winningScore + " points. \n"
					+ losingPlayer + " followed with " + losingScore
					+ " points. \n" + "Total score was " + totalScore
					+ " points.\n", Art.getSkin(), "textStyle");
		} else {
			text = new Label("Game Over!\n\n"
					+ "You drew with one another, both gaining a score of "
					+ scoreTwo + " points.\n" + "Total score was " + totalScore
					+ " points.\n", Art.getSkin(), "textStyle");
		}

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
