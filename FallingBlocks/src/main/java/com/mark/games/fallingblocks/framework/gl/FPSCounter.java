package com.mark.games.fallingblocks.framework.gl;

import android.util.Log;

public class FPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	int fps;

	public void logFrame() {
		frames ++;
		if(System.nanoTime() - startTime >= 1000000000) {
			Log.d("FPSCounter", "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
	}
	public int getFps() {
		frames ++;
		if(System.nanoTime() - startTime >= 1000000000) {
			fps = frames;
			frames = 0;
			startTime = System.nanoTime();
		}
		return fps;
	}
}