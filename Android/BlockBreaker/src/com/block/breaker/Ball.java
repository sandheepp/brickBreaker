package com.block.breaker;

import android.graphics.Color;
import android.graphics.Paint;

public class Ball
{
    //coordinates
   private int x,y;
    //velocities
     private int dx, dy;
	private int _radius;
     
    public int get_radius() {
		return this._radius;
	}
    public Ball(int x, int y,int radius)
    {

        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
        _radius = radius;

    }


    //gets x velocity of the ball
    public int getXVelocity() {
        return dx;
    }

    //sets x velocity of ball
    public void setXVelocity(int dx) {
        this.dx = dx;
    }

    //gets y velocity of ball
    public int getYVelocity() {
        return dy;
    }

    //sets y velocity of ball
    public void setYVelocity(int dy) {
        this.dy = dy;
    }

    //gets x-coordinate of ball
    public int getX() {
        return x;
    }

    //sets x-coordinate of ball
    public void setX(int x) {
        this.x = x;
    }

    //gets y-coordinate of ball
    public int getY() {
        return y;
    }

    //sets y-coordinate of ball
    public void setY(int y) {
        this.y = y;
    }
    public Paint getPaint()
    {
    	Paint p = new Paint();
		p.setColor(Color.rgb(173,32,177));
		p.setAntiAlias(true);
		return p;
    }
}
