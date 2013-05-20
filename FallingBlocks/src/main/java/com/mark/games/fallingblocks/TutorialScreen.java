package com.mark.games.fallingblocks;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.collision.SpatialHashGrid;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.FPSCounter;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class TutorialScreen extends Screen {

	GLGraphics glGraphics;
    Camera2D camera;
	FPSCounter fpsCounter;
	int fps;
	Vector2 touchPosition = new Vector2();
	SpriteBatcher batcher;
	TutorialWorld world;
	TutorialRenderer renderer;

    List<Rectangle> buttonList;
    Rectangle pauseResume;
    Rectangle pauseQuit;

	Rectangle pauseButton;

	public TutorialScreen(Game game) {
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();

		batcher = new SpriteBatcher(glGraphics, 100);
		world = new TutorialWorld();
		renderer = new TutorialRenderer(glGraphics, batcher, world);
		camera = new Camera2D(glGraphics, renderer.camera.position.x,
				renderer.camera.position.y);
		fpsCounter = new FPSCounter();

        buttonList = new ArrayList<Rectangle>();

        buttonList.add(new Rectangle(camera.position.x - 120 + 3.5f, camera.position.y - renderer.FRUSTUM_HEIGHT/2 + 32.5f, 233.5f, 50));
        pauseResume = buttonList.get(0);
        buttonList.add(new Rectangle(camera.position.x - 120 + 3.5f, camera.position.y - renderer.FRUSTUM_HEIGHT/2 -35, 233.5f, 50));
        pauseQuit = buttonList.get(1);
    }

	@Override
	public void update(float deltaTime) {
		updateState(deltaTime);
	}

	public void updateState(float deltaTime) {
		switch (world.state) {
		case GameWorld.GAME_GAMEOVER:
			updateGameOver();
			break;
		case GameWorld.GAME_PAUSED:
			updatePaused();
			break;
		case GameWorld.GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		}
	}

	public void updateGameOver() {
		game.setScreen(new MainMenuScreen(game));
	}

	public void updatePaused() {
		pauseButton = new Rectangle(renderer.FRUSTUM_WIDTH - 60,
				camera.position.y + renderer.FRUSTUM_HEIGHT / 2 - 40, 60, 40);
        pauseResume = new Rectangle(camera.position.x - 120 + 3.5f, camera.position.y + 32.5f, 233.5f, 50);
        pauseQuit = new Rectangle(camera.position.x - 120 + 3.5f, camera.position.y - 35, 233.5f, 50);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		try {
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                touchPosition.set(event.x, event.y);
                renderer.camera.touchToWorld(touchPosition);
                if (event.type == TouchEvent.TOUCH_UP) {
                    if (OverlapTester.pointInRectangle(pauseResume, touchPosition)) {
                        world.state = GameWorld.GAME_RUNNING;
                    }
                    if (OverlapTester.pointInRectangle(pauseQuit, touchPosition)) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
            }

		} catch (IndexOutOfBoundsException e) {

		}
		fps = fpsCounter.getFps();

	}

	public void updateRunning(float deltaTime) {
		world.update(deltaTime, game.getInput().getAccelX());
		world.checkCollisions(deltaTime);

		pauseButton = new Rectangle(renderer.FRUSTUM_WIDTH - 60,
				camera.position.y + renderer.FRUSTUM_HEIGHT / 2 - 40, 60, 40);

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		try {
		int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPosition.set(event.x, event.y);
				renderer.camera.touchToWorld(touchPosition);
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (OverlapTester.pointInRectangle(pauseButton,	touchPosition)) {
						if (world.state == GameWorld.GAME_PAUSED) {
							world.state = GameWorld.GAME_RUNNING;
						} else {
							world.state = GameWorld.GAME_PAUSED;
						}
					}
					// game.setScreen(new MainMenuScreen(game));
					world.character.jump();
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		fps = fpsCounter.getFps();
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1, 1, 1, 1);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderer.render();
		camera.position.y = renderer.camera.position.y;

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		drawGUI();

		Assets.gameScreenText.begin(0f, 0f, 0f, 1.0f);

		if (Settings.showFps) {
			Assets.gameScreenText.draw("FPS: " + Integer.toString(fps), 10, camera.position.y + 215);
		}
		Assets.gameScreenText.end();
	}

	public void drawGUI() {
		batcher.beginBatch(Assets.icons);
		batcher.drawSprite(300, camera.position.y + renderer.FRUSTUM_HEIGHT / 2
				- 15, 10, 15, Assets.pauseButton);
		batcher.endBatch();
		// replace the gltext with an image maybe?
		if (world.state == GameWorld.GAME_PAUSED) {
            //darkening the game screen
            batcher.beginBatch(Assets.backLavaHigh);
            batcher.drawSprite(renderer.camera.position.x, camera.position.y, renderer.FRUSTUM_WIDTH, renderer.FRUSTUM_HEIGHT, Assets.highlight);
            batcher.endBatch();
            //drawing the pause menu
            batcher.beginBatch(Assets.pauseMenu);
            batcher.drawSprite(renderer.camera.position.x, camera.position.y, 240, 360, Assets.pauseRegion);
            batcher.endBatch();

		}
	}

	@Override
	public void resume() {
		Settings.load(game.getFileIO(), ".falling");
	}

	@Override
	public void pause() {

	}

	@Override
	public void dispose() {

	}
}