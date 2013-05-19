package com.mark.games.fallingblocks.framework.impl;

import com.mark.games.fallingblocks.framework.Game;
import com.mark.games.fallingblocks.framework.Screen;

public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
