package com.mark.games.fallingblocks;

import javax.microedition.khronos.opengles.GL10;

import com.mark.games.fallingblocks.framework.gl.Camera2D;
import com.mark.games.fallingblocks.framework.gl.SpriteBatcher;
import com.mark.games.fallingblocks.framework.impl.GLGraphics;

public class TutorialRenderer {
	final static float FRUSTUM_WIDTH = 320.0f;
	final static float FRUSTUM_HEIGHT = 480.0f;
	GLGraphics glGraphics;
	TutorialWorld world;
	Camera2D camera;
	SpriteBatcher batcher;
	
	public TutorialRenderer(GLGraphics glGraphics, SpriteBatcher batcher, TutorialWorld world) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.camera = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batcher = batcher;
	}
	
	public void render() {
	//	Log.d("char y", Float.toString(world.character.position.y));
		
		camera.setViewportAndMatrices();
		renderBackground();
		//renderFloor();
		renderObjects();
	}
	
	public void renderObjects() {
		GL10 gl = glGraphics.getGL();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	     
		renderBlocks();
		renderCharacter();
		renderLava();

		gl.glDisable(GL10.GL_BLEND);
	}
	
	public void renderCharacter() {
		//Assets.character.reload();
		batcher.beginBatch(Assets.character);
		batcher.drawSprite(world.character.position.x, world.character.position.y, world.character.bounds.width, world.character.bounds.height, Assets.mainChar);
		batcher.endBatch();
	}
	
	public void renderLava() {
		batcher.beginBatch(Assets.backLavaHigh);
		batcher.drawSprite(world.lava.position.x, world.lava.position.y, world.lava.width, world.lava.height, Assets.lavaRegion);
		batcher.endBatch();
	}
	
	public void renderBlocks() {
		batcher.beginBatch(Assets.blocks);
		for(int i = 0; i < world.blocks.size(); i++) {
			batcher.drawSprite(world.blocks.get(i).position.x, world.blocks.get(i).position.y, world.blocks.get(i).bounds.width, world.blocks.get(i).bounds.height, Assets.largeBlue);
		}
		batcher.endBatch();
	}
	
	public void renderFloor() {
		batcher.beginBatch(Assets.floor);
		batcher.drawSprite(160, -150, 330, 300, Assets.floorRegion);
		batcher.endBatch();
	}
	
	public void renderBackground() {
		batcher.beginBatch(Assets.tutorial);
		batcher.drawSprite(camera.position.x, camera.position.y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.tutBackground);
		batcher.endBatch();
	}
}