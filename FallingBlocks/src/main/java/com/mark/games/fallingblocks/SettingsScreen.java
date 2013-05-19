package com.mark.games.fallingblocks;

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

public class SettingsScreen extends Screen {

	Graphics graphics;
	GLGraphics glGraphics;
	Camera2D camera;
	Vector2 touchPoint;
	SpriteBatcher batcher;

	static boolean showFps;
	static boolean showGrid;
	static boolean playMusic;
	boolean buttonPressed;
	Rectangle backButtonRect;
	Rectangle fpsOnOffButtonRect;
	Rectangle gridOnOffButtonRect;
	Rectangle logRect;
	Rectangle deleteLogRect;
	Rectangle musicRect;

	public SettingsScreen(Game game) {
		super(game);
		touchPoint = new Vector2();
		glGraphics = ((GLGame) game).getGLGraphics();
		Settings.load(((GLGame) game).getFileIO(), ".falling");
		batcher = new SpriteBatcher(glGraphics, 10);
		buttonPressed = false;
	}

	@Override
	public void update(float deltaTime) {

		backButtonRect = new Rectangle(0, 25, 121, 50);
		
		musicRect = new Rectangle(20, 275, 290, 50);
		fpsOnOffButtonRect = new Rectangle(0, 200, 290, 50);
		

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			camera.touchToWorld(touchPoint);
			if (event.type == TouchEvent.TOUCH_DRAGGED
					|| event.type == TouchEvent.TOUCH_DOWN) {
				if (OverlapTester.pointInRectangle(backButtonRect, touchPoint)) {
					buttonPressed = true;
				} else {
					buttonPressed = false;
				}
			}
			if (event.type == TouchEvent.TOUCH_UP) {
				buttonPressed = false;
				if (OverlapTester.pointInRectangle(backButtonRect, touchPoint)) {
					game.setScreen(new MainMenuScreen(game));
				}
				if (OverlapTester.pointInRectangle(fpsOnOffButtonRect,
						touchPoint)) {
					showFps = !showFps;
				}
				if (OverlapTester.pointInRectangle(musicRect, touchPoint)) {
					playMusic = !playMusic;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClearColor(1, 1, 1, 1);
		camera.setViewportAndMatrices();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batcher.beginBatch(Assets.settings);
		batcher.drawSprite(160, 240, 320, 480, Assets.settingsBack);
		batcher.endBatch();

		if(buttonPressed) {
			batcher.beginBatch(Assets.backLavaHigh);
			batcher.drawSprite(60, 50, 121, 50, Assets.highlight);
			batcher.endBatch();
		}
		
		Assets.mainText.begin(0f, 0f, 0f, 1.0f);

		if (showFps) {
			Assets.mainText.draw("On", 123, 215);
		} else {
			Assets.mainText.draw("Off", 123, 215);
		}
		if (playMusic) {
			Assets.mainText.draw("On", 123, 287);
		} else {
			Assets.mainText.draw("Off", 123, 287);
		}
		Assets.mainText.end();
	}

	@Override
	public void resume() {
		Settings.load(((GLGame) game).getFileIO(), ".falling");
		showFps = Settings.showFps;
		glGraphics = ((GLGame) game).getGLGraphics();
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL10.GL_TEXTURE_2D);

		camera = new Camera2D(glGraphics, 320, 480);
	}

	@Override
	public void pause() {
		Settings.save(((GLGame) game).getFileIO());
	}

	@Override
	public void dispose() {

	}
}
