package com.mark.games.fallingblocks;

import java.io.InputStream;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.mark.games.fallingblocks.framework.FileIO;
import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class HighscoreScreen extends Screen {

	FileIO file;
	GLGraphics glGraphics;
	Camera2D camera;
	InputStream in;
	Rectangle backButtonRect;
	Vector2 touchPoint = new Vector2();
	SpriteBatcher batcher;
	boolean buttonPressed = false;

	public HighscoreScreen(Game game) {
		super(game);

		glGraphics = ((GLGame) game).getGLGraphics();
		camera = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 20);

		file = ((GLGame) game).getFileIO();
	}

	@Override
	public void update(float deltaTime) {
		backButtonRect = new Rectangle(0, 25, 121, 50);

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			camera.touchToWorld(touchPoint);

			if (OverlapTester.pointInRectangle(backButtonRect, touchPoint)) {
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (OverlapTester.pointInRectangle(backButtonRect,
							touchPoint)) {
						buttonPressed = true;
					} else {
						buttonPressed = false;
					}
				}
				if (event.type == TouchEvent.TOUCH_UP) {
					game.setScreen(new MainMenuScreen(game));
				}

			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 1.0f);
		camera.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		batcher.beginBatch(Assets.highscoreScreen);
		batcher.drawSprite(160, 240, 320, 480, Assets.highscoreBack);
		batcher.endBatch();

		if (buttonPressed) {
			batcher.beginBatch(Assets.backLavaHigh);
			batcher.drawSprite(60, 50, 121, 50, Assets.highlight);
			batcher.endBatch();
		}

		Assets.mainText.begin(0f, 0f, 0f, 1f);
		Assets.mainText
				.draw(Integer.toString(Settings.scoresArray[0]), 25, 315);
		Assets.mainText
				.draw(Integer.toString(Settings.scoresArray[1]), 25, 260);
		Assets.mainText
				.draw(Integer.toString(Settings.scoresArray[2]), 25, 205);
		Assets.mainText
				.draw(Integer.toString(Settings.scoresArray[3]), 25, 150);
		Assets.mainText.draw(Integer.toString(Settings.scoresArray[4]), 25, 95);
		Assets.mainText.end();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		Settings.load(file, ".falling");

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}