package seprini.data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator implements ApplicationListener {
	// Reference to numbers found here:
	// https://github.com/libgdx/libgdx/wiki/2D-Animation
	private static final int FRAME_COLS = 8; // #1
	private static final int FRAME_ROWS = 8; // #2

	Animation explosionAnimation; // #3
	Texture explosionSheet; // #4
	TextureRegion[] explosionFrames; // #5
	SpriteBatch spriteBatch; // #6
	TextureRegion currentFrame; // #7

	float stateTime; // #8

	@Override
	public void create() {
		explosionSheet = Art.getTextureRegion("explosion").getTexture(); // #9
		TextureRegion[][] tmp = TextureRegion.split(explosionSheet,
				explosionSheet.getWidth() / FRAME_COLS,
				explosionSheet.getHeight() / FRAME_ROWS); // #10
		explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				explosionFrames[index++] = tmp[i][j];
			}
		}
		explosionAnimation = new Animation(0.125f, explosionFrames); // #11
		explosionAnimation.setPlayMode(3);
		spriteBatch = new SpriteBatch(); // #12
		stateTime = 0f; // #13
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = explosionAnimation.getKeyFrame(stateTime, true); // #16
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, 50, 50); // #17
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
