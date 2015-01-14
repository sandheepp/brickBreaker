/**
 * 
 */
package com.block.breaker;

import java.awt.Rectangle;


/**
 * @author sandeep.penchala
 * 
 */
public class Ball extends Rectangle
{

	// velocities
	private int dx, dy;

	public Ball(int x, int y,int width,int height) 
	{
		this.x = x;
		this.y = y;
		dx = 0;
		dy = 0;
		this.width = width;
		this.height = height;
	}

	// gets x velocity of the ball
	public int getXVelocity() {
		return dx;
	}

	// sets x velocity of ball
	public void setXVelocity(int dx) {
		this.dx = dx;
	}

	// gets y velocity of ball
	public int getYVelocity() {
		return dy;
	}

	// sets y velocity of ball
	public void setYVelocity(int dy) {
		this.dy = dy;
	}

}
