package com.mark.games.fallingblocks;

import java.util.Random;
import com.mark.games.fallingblocks.framework.DynamicGameObject;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class Block extends DynamicGameObject {

	Vector2 position;
	Vector2 oldPosition;
	Vector2 temp;
	Vector2 velocity;
	int constant;
	Random ran = new Random();
	float width, height;
	
	public static final int STATE_FALLING = 0;
	public static final int STATE_STOPPED = 1;
	
	public int state;
	
	float tempDelta;
	float deltaOld;
	float deltaMix = 0;

	public Block(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2(0, -150);
		position = new Vector2();	
		
		state = STATE_FALLING;	
		
		position.set(x, y);
		oldPosition = new Vector2(x, y + 300);
		temp = new Vector2();

		this.width = width;
		this.height = height;
		tempDelta = .2f;
		deltaOld = 0.0f;
	}

	public void update(float deltaTime) {
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);

		deltaOld = tempDelta;
		tempDelta = deltaTime;

		deltaMix = deltaTime / deltaOld;

		if (deltaMix >= Float.POSITIVE_INFINITY) {
			deltaMix = 1.0f;
		}
		temp.set(position);
		
		if (state == STATE_FALLING)
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		// oldPosition.set(temp.x, temp.y-constant);

		// velocity.y = -(position.y - oldPosition.y);
		if (position.y - height / 2 <= 0) {
			velocity.y = 0;
			position.y = 50;
		}
	}
	
	public void hitBlock(float y) {
		position.set(position.x, y);
		state = STATE_STOPPED;
	}
}