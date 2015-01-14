/**
 * 
 */
package com.block.breaker;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author sandeep.penchala
 *
 */
public class Effects 
{
	private Type _type;
	private int _Xvelocity,_Yvelocity,_X,_Y;
	private ImageIcon _image;
	private String checkString;
	private int _width,_height;
	public Effects(String string) 
	{
		checkString = string;
	}
	public int get_Xvelocity() {
		return _Xvelocity;
	}
	public void set_Xvelocity(int _Xvelocity) {
		this._Xvelocity = _Xvelocity;
	}
	public int get_Yvelocity() {
		return _Yvelocity;
	}
	public void set_Yvelocity(int _Yvelocity) {
		this._Yvelocity = _Yvelocity;
	}
	public int get_X() {
		return _X;
	}
	public void set_X(int _X) {
		this._X = _X;
	}
	public int get_Y() {
		return _Y;
	}
	public void set_Y(int _Y) {
		this._Y = _Y;
	}
	public ImageIcon get_image() {
		return _image;
	}
	public void set_image(ImageIcon _image) {
		this._image = _image;
	}
	public void set_type(Type type) 
	{
		this._type = type;
		switch (type) {
		case BOMB:
			_height = _width = 50;
			break;
		case BIG_PADDLE:
			_width = 80;
			_height = 60;
			break;
		case SMALL_PADDLE:
			_width = 80;
			_height = 60;
			break;
		default:
			break;
		}
	}
	public Type get_type() {
		return _type;
	}
	public int get_width() {
		return _width;
	}
	public int get_height() {
		return _height;
	}

	@Override
	public String toString() 
	{
		return checkString;
	}
}
