/**
 * 
 */
package com.block.breaker;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author sandeep.penchala
 * 
 */
public class BrickBreakerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		BrickBreakerFrame frame = new BrickBreakerFrame();
		BrickBreakerPanel panel = new BrickBreakerPanel();
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolKit.getScreenSize();
		panel.setSize(dimension.width,dimension.height);
		frame.setUndecorated(true);
		frame.setName("Brick Breaker");
		frame.setPreferredSize(dimension);
		frame.setResizable(false);
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
	}

}
