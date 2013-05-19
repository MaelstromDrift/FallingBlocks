package com.mark.games.fallingblocks.framework.collision;

public class ContinuousCollision {

	public static boolean checkYCollision(float y, float radius, float yv, float acceleration , float y2, float radius2) {
		float speed;
		float distance;
		float time;
		
		if(yv > 0)
			distance = (y2 - radius2)-(y+radius);
		else
			distance = (y-radius) - (y2+radius2);
		
		speed = yv + acceleration;
		
		time = distance / speed;
		//Log.d("time", Float.toString(time));
		if(time > 0.0f && time < 1.0f){
			return true;
		}
		else
			return false;
		
	}
	
	public static boolean checkXCollision(float x, float radius, float xv, float x2, float radius2) {
		float speed;
		float distance;
		float time;
		
		if(xv > 0)
			distance = (x2 - radius2)-(x+radius);
		else
			distance = (x-radius) - (x2+radius2);
		speed = xv;
		
		time = distance / speed;
		if(time > -1.0f && time < 1.0f)
			return true;
		else
			return false;
	}
}