package com.block.breaker;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Blocks extends RectF 
{
    private boolean _hit;
    private Paint _paint;
    //constructor takes in x and y coordinates
    public Blocks(int x, int y)
    {
        this.left = x;
        this.top = y;
        _hit = false;
    }

    //if hits returns true otherwise returns false
    public boolean isHit() 
    {
        return _hit;
    }

    //set if the brick is hit or not
    public void setHit(boolean hit) {
        this._hit = hit;
    }
    
    public Paint getPaint()
    {
		return _paint;
    }
    public void setPaint(Paint p)
    {
    	_paint = p;
    }
}
