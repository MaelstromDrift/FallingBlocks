package com.mark.games.fallingblocks;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Graphics;
import com.mark.games.fallingblocks.framework.Input;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class LossScreen extends Screen {

	GLGraphics glGraphics;
    SpriteBatcher batcher;
	Camera2D camera;
	Vector2 touchPoint;

    boolean buttonPressed = false;
    float buttonX, buttonY;
    float buttonWidth, buttonHeight;

    List<Rectangle> buttonList;

    Rectangle playAgain;
    Rectangle mainMenu;

    public LossScreen(Game game) {
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();
        batcher = new SpriteBatcher(glGraphics, 10);
		camera = new Camera2D(glGraphics, 320, 480);
		touchPoint = new Vector2();

        buttonList = new ArrayList<Rectangle>();
        // Play button
        buttonList.add(new Rectangle(0, 280, 290, 50));
        playAgain = buttonList.get(0);
        // High score button
        buttonList.add(new Rectangle(0, 205, 290, 50));
        mainMenu = buttonList.get(1);
	}

	@Override
	public void update(float deltaTime) {

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		try {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPoint.set(event.x, event.y);
				camera.touchToWorld(touchPoint);
                if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED) {
                    for (int j = 0; j < buttonList.size(); j++) {
                        if (OverlapTester.pointInRectangle(buttonList.get(j),
                                touchPoint)) {
                            buttonPressed = true;
                            buttonX = buttonList.get(j).lowerLeft.x
                                    + buttonList.get(j).width / 2;
                            buttonY = buttonList.get(j).lowerLeft.y
                                    + buttonList.get(j).height / 2;
                            buttonWidth = buttonList.get(j).width;
                            buttonHeight = buttonList.get(j).height;
                            break;
                        } else {
                            buttonPressed = false;
                        }
                    }
                }
                if (event.type == Input.TouchEvent.TOUCH_UP) {

                    if (buttonPressed == true) {
                        if (OverlapTester.pointInRectangle(playAgain, touchPoint)) {
                            game.setScreen(new GameScreen(game));
                        }
                        if (OverlapTester.pointInRectangle(mainMenu, touchPoint)) {
                            game.setScreen(new MainMenuScreen(game));
                        }
                    }
                    buttonPressed = false;
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
        batcher.beginBatch(Assets.lossScreen);
        batcher.drawSprite(160, 240, 320, 480, Assets.lossBack);
        batcher.endBatch();

        if(buttonPressed) {
            batcher.beginBatch(Assets.backLavaHigh);
            batcher.drawSprite(buttonX, buttonY, buttonWidth, buttonHeight, Assets.highlight);
            batcher.endBatch();
        }
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
