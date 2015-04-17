package com.mark.games.fallingblocks;

import com.mark.games.fallingblocks.framework.DynamicGameObject;


public class Lava extends DynamicGameObject {
	
	float x;
	float y;
	float width;
	float height;

	public Lava(float x, float y, float width, float height) {
		super(x, y, width, height);
		position.set(x, y);
		velocity.set(0, 50);
		
		this.width = width;
		this.height = height;
	}
	
	public void update(float deltaTime) {
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		position.add(0, velocity.y * deltaTime);
	}
}