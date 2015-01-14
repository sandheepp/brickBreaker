/**
 * 
 */
package com.block.breaker;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * @author sandeep.penchala
 * 
 */
public class Blocks extends Rectangle 
{
	private boolean _isHit = false;
	private Type _type  = Type.DEFAULT;
	private Color _color;
	private int _YVelocity;
	public Blocks(int x, int y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the _isHit
	 */
	public boolean _isHit() 
	{
		return _isHit;
	}

	/**
	 * @param _isHit the _isHit to set
	 */
	public void set_isHit(boolean _isHit)
	{
		this._isHit = _isHit;
	}

	public Color get_color() {
		return _color;
	}

	public void set_color(Color _color) {
		this._color = _color;
	}

	public Type get_type() {
		return _type;
	}

	public void set_type(Type _type)
	{
		this._type = _type;
	}

	public int get_YVelocity() {
		return _YVelocity;
	}

	public void set_YVelocity(int _YVelocity) {
		this._YVelocity = _YVelocity;
	}
}
