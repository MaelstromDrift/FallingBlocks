package com.mark.games.fallingblocks;

import android.util.Log;

import com.mark.games.fallingblocks.framework.DynamicGameObject;
import com.mark.games.fallingblocks.framework.math.Vector2;

public class SquareMan extends DynamicGameObject {

	Vector2 position;
	Vector2 oldPosition;
	Vector2 tempPos;
	Vector2 acceleration;
	Vector2 velocity;
	int jumps;
	
	public static final int STATE_MOVING = 0;
	public static final int STATE_STILL = 1;
	public static final int STATE_DEAD = 2;
	
	int state;

	float tempDelta;
	float deltaOld;
	float deltaMix = 0;
	float deltaT;
	
	public SquareMan(float x, float y, float width, float height) {
		super(x, y, width, height);
		state = STATE_STILL;
		position = new Vector2(x, y);
		acceleration = new Vector2(0, -650.0f);
		velocity = new Vector2(0.0f, 0.0f);
		oldPosition = new Vector2(x, y);
		tempPos = new Vector2();
		tempDelta = .2f;
		deltaOld = 0.0f;
	}
	
	public void update(float deltaTime) {
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		//Verlet integration because eulers is shit when using continuous collision detection
		//Only problem is Verlet assumes that we are using fixed time steps so if fps is lower than 60 than it will behave wrong
		//trying to implement the following equation
		//yi+1 = yi + (yi - yi-1) * (dti / dti-1) + a * dti * dti
		//Log.d("velocity.x", Float.toString(velocity.x));
		deltaT = deltaTime;

		deltaOld = tempDelta;
		tempDelta = deltaTime;
		
		tempPos.set(position);
		
		deltaMix = deltaTime / deltaOld;
		
		if( deltaMix >= Float.POSITIVE_INFINITY || deltaMix <= 0.0f){
			deltaMix = 1.0f;
		}
		
		if(state == STATE_MOVING)
			position.add(0, velocity.y * deltaMix + acceleration.y * deltaTime * deltaTime);
		
		position.add(velocity.x * deltaTime,0);
		
		oldPosition.set(tempPos);
		velocity.y = (position.y - oldPosition.y);
		
		if (position.y - bounds.height / 2 <= 0) {
		//	velocity.y = 0.0f;
			position.y = bounds.height / 2;
			jumps = 0;
			state = STATE_STILL;
		}
		
		if (position.x < 0) {
			position.x = 320;
		}
		
		if (position.x > 320) { 
			position.x = 0;
		}
		Log.d("state", Integer.toString(state));
	}

	public void hitTop(float y, float width, float blockVel) {
		state = STATE_STILL;
		position.y = y + width/2 + bounds.height / 2;
		jumps = 0;
	}

	public void hitBottomPlatform(float y, float blockVel) {
		position.y = y - bounds.height / 2;
		velocity.y = blockVel;
	}
	
	public void hitLeft(float x, float width) {
		velocity.x = 0.0f;
		position.x = x - width - bounds.width / 2;
		jumps = 0;

	}
	
	public void hitRight(float x, float width) {
		velocity.x = 0.0f;
		position.x = x + width + bounds.width / 2;
		jumps = 0;

	}

	public void jump() {
		if (jumps < 1) {
			state = STATE_MOVING;
			velocity.y = 380.0f * deltaT;
			jumps++;
		}
	}
}