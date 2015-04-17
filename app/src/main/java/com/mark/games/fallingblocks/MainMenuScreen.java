package com.mark.games.fallingblocks;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Graphics;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class MainMenuScreen extends Screen {

	GLGraphics glGraphics;
	Camera2D camera;
	Vector2 touchPoint;
	ArrayList<Rectangle> buttonList;
	List<TouchEvent> touchEvents;

	Rectangle settingsButtonRect;
	Rectangle startButtonRect;
	Rectangle highscoreButtonRect;

	SpriteBatcher batcher;

	boolean buttonPressed;
	float buttonX;
	float buttonY;

	final float FRUSTUM_WIDTH = 320.0f;
	final float FRUSTUM_HEIGHT = 480.0f;

	public MainMenuScreen(Game game) {
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();
		batcher = new SpriteBatcher(glGraphics, 20);
		camera = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		touchPoint = new Vector2();

		buttonList = new ArrayList<Rectangle>();
		// Play button
		buttonList.add(new Rectangle(0, 280, 290, 50));
		startButtonRect = buttonList.get(0);
		// High score button
		buttonList.add(new Rectangle(0, 205, 290, 50));
		highscoreButtonRect = buttonList.get(1);
		// Settings button
		buttonList.add(new Rectangle(0, 130, 290, 50));
		settingsButtonRect = buttonList.get(2);
	}

	@Override
	public void update(float deltaTime) {

		touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		try {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPoint.set(event.x, event.y);
				camera.touchToWorld(touchPoint);
				getTouchEvents(event, touchPoint);
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void getTouchEvents(TouchEvent event, Vector2 touchPoint) {

		if (event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED) {
			for (int i = 0; i < buttonList.size(); i++) {
				if (OverlapTester.pointInRectangle(buttonList.get(i),
						touchPoint)) {
                    buttonPressed = true;
					buttonX = buttonList.get(i).lowerLeft.x
							+ buttonList.get(i).width / 2;
					buttonY = buttonList.get(i).lowerLeft.y
							+ buttonList.get(i).height / 2;
					break;
				} else {
					buttonPressed = false;
				}
			}
		}
		if (event.type == TouchEvent.TOUCH_UP) {

			if (buttonPressed) {
				if (OverlapTester.pointInRectangle(startButtonRect, touchPoint)) {
					game.setScreen(new ModeSelectScreen(game));
				}
				if (OverlapTester.pointInRectangle(settingsButtonRect,
						touchPoint)) {
					game.setScreen(new SettingsScreen(game));
				}
				if (OverlapTester.pointInRectangle(highscoreButtonRect,
						touchPoint)) {
					game.setScreen(new HighscoreScreen(game));
				}
			}
			buttonPressed = false;
		}

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.setViewportAndMatrices();

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		glGraphics = ((GLGame) game).getGLGraphics();

		batcher.beginBatch(Assets.menu);
		batcher.drawSprite(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 320, 480,
				Assets.mainBack);
		batcher.drawSprite(90, 420, 125, 50, Assets.menuFalling);
		batcher.drawSprite(230, 420, 125, 50, Assets.menuBlocks);
		batcher.endBatch();

		if (buttonPressed) {
			batcher.beginBatch(Assets.backLavaHigh);
			batcher.drawSprite(buttonX, buttonY, 290, 50, Assets.highlight);
			batcher.endBatch();
		}

		gl.glLoadIdentity();
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);

	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void dispose() {

	}
}