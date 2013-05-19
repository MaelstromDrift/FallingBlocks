package com.mark.games.fallingblocks;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Graphics;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class LossScreen extends Screen {

	Graphics graphics;
	GLGraphics glGraphics;
	Camera2D camera;
	Vector2 touchPoint;

	Rectangle tryAgainButtonRect;
	Rectangle menuButtonRect;

	public LossScreen(Game game) {
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();
		camera = new Camera2D(glGraphics, 320, 480);

		touchPoint = new Vector2();
	}

	@Override
	public void update(float deltaTime) {
		tryAgainButtonRect = new Rectangle(100, 280, 120, 35);
		menuButtonRect = new Rectangle(100, 208, 120, 35);

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		try {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPoint.set(event.x, event.y);
				camera.touchToWorld(touchPoint);
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (OverlapTester.pointInRectangle(menuButtonRect,
							touchPoint))
						game.setScreen(new MainMenuScreen(game));
					if (OverlapTester.pointInRectangle(tryAgainButtonRect,
							touchPoint))
						game.setScreen(new GameScreen(game));
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClearColor(1, 1, 1, 1);
		camera.setViewportAndMatrices();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Assets.mainText.begin(0f, 0f, 0f, 1.0f);
		Assets.mainText.draw("Game Over", 100, 424);
		Assets.mainText.draw("Try Again", 100, 280);
		Assets.mainText.draw("Main Menu", 100, 208);
		Assets.mainText.end();

	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
