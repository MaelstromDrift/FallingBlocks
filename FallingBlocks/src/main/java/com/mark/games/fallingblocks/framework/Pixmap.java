package com.mark.games.fallingblocks.framework;

import com.mark.games.fallingblocks.framework.Graphics.PixmapFormat;

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
