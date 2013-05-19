package com.mark.games.fallingblocks;

import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Input;
import com.mark.games.fallingblocks.framework.Screen;
import com.mark.games.fallingblocks.framework.collision.OverlapTester;
import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGame;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;
import com.mark.games.fallingblocks.framework.math.Rectangle;
import com.mark.games.fallingblocks.framework.math.Vector2;
import com.mark.games.fallingblocks.framework.Input.TouchEvent;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 5/19/13.
 */
public class ModeSelectScreen extends Screen {

    SpriteBatcher batcher;
    GLGraphics glGraphics;
    Camera2D camera;
    Vector2 touchPoint;
    List<TouchEvent> touchEvents;
    List<Rectangle> buttonList;

    Rectangle mainGame;
    Rectangle tutorial;
    Rectangle back;

    float buttonX;
    float buttonY;
    float buttonWidth;
    float buttonHeight;

    boolean buttonPressed;

    public ModeSelectScreen(Game game) {
        super(game);
        glGraphics = ((GLGame)game).getGLGraphics();
        camera = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 10);
        touchPoint = new Vector2();

        back = new Rectangle(0, 25, 121, 50);
        buttonList = new ArrayList<Rectangle>();
        // Play button
        buttonList.add(new Rectangle(0, 280, 290, 50));
        mainGame = buttonList.get(0);
        // High score button
        buttonList.add(new Rectangle(0, 205, 290, 50));
        tutorial = buttonList.get(1);

        buttonList.add(back);
    }


    @Override
    public void update(float deltaTime) {
        touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        try {
            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);
                touchPoint.set(event.x, event.y);
                camera.touchToWorld(touchPoint);
                getTouchEvents(event, touchPoint);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void getTouchEvents(Input.TouchEvent event, Vector2 touchPoint) {
        if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED) {
            for (int i = 0; i < buttonList.size(); i++) {
                if (OverlapTester.pointInRectangle(buttonList.get(i),
                        touchPoint)) {
                    buttonPressed = true;
                    buttonX = buttonList.get(i).lowerLeft.x
                            + buttonList.get(i).width / 2;
                    buttonY = buttonList.get(i).lowerLeft.y
                            + buttonList.get(i).height / 2;
                    buttonWidth = buttonList.get(i).width;
                    buttonHeight = buttonList.get(i).height;
                    break;
                } else {
                    buttonPressed = false;
                }
            }
        }
        if (event.type == Input.TouchEvent.TOUCH_UP) {

            if (buttonPressed == true) {
                if (OverlapTester.pointInRectangle(mainGame, touchPoint)) {
                    game.setScreen(new GameScreen(game));
                }
                if (OverlapTester.pointInRectangle(tutorial,
                        touchPoint)) {
                    game.setScreen(new TutorialScreen(game));
                }
                if (OverlapTester.pointInRectangle(back,
                        touchPoint)) {
                    game.setScreen(new MainMenuScreen(game));
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
        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.selectScreen);
        batcher.drawSprite(160, 240, 320, 480, Assets.selectScreenBack);
        batcher.endBatch();

        if(buttonPressed) {
            batcher.beginBatch(Assets.backLavaHigh);
            batcher.drawSprite(buttonX, buttonY, buttonWidth, buttonHeight, Assets.highlight);
            batcher.endBatch();
        }
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
