/**
 * 
 */
package com.block.breaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author sandeep.penchala
 * 
 */
public class BrickBreakerPanel extends JPanel implements KeyListener,
		ActionListener {
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	private Ball _ball;
	private Blocks[] _blocks;
	private Blocks _paddle;
	private int _blockWidth, _blockHeight;
	private int _ballWeightNHeight = 30;
	private Dimension _dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int _borderLineWidth = 10;
	private int _score;
	private int _chances = 5;
	private JButton _help, _exit, _restart;
	private boolean _isRunning = false;
	private ArrayList<Effects> _effects = new ArrayList<Effects>();
	private Effects effect;

	public BrickBreakerPanel() {
		_help = new JButton("HELP");
		_exit = new JButton("EXIT");
		_restart = new JButton("RESTART");
		add(_restart);
		add(_help);
		add(_exit);
		_paddle = new Blocks((int) (_dimension.width / 2.3),
				_dimension.height - 80, 200, 50);
		_ball = new Ball(_dimension.width / 2, _dimension.height - 113,
				_ballWeightNHeight, _ballWeightNHeight);
		setBackground(new Color(100, 100, 80));
		setinitialBallValues();
		setBricks();
		showHelpAlert();
		addListeners();
	}

	/**
	 * adds the the listeners for the components
	 */
	private void addListeners() {
		addKeyListener(this);
		_exit.addActionListener(this);
		_help.addActionListener(this);
		_restart.addActionListener(this);
	}

	/**
	 * shows the help for the
	 */
	private void showHelpAlert() {
		JFrame frame = new JFrame();
		String helpText = "Press Space to Strart the Game \n Use Left and Right Arrows to move the Paddle";
		URL imageurl	= getClass().getClassLoader().getResource("help.png");
		ImageIcon imageIcon = new ImageIcon(imageurl);
		JOptionPane.showMessageDialog(frame, helpText, "HELP", 1, imageIcon);
	}

	/**
	 * sets the ball velocity to 0
	 */
	private void setinitialBallValues() {
		_ball.setXVelocity(0);
		_ball.setYVelocity(0);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.setPaintMode();
		graphics.setColor(Color.RED);
		requestFocusInWindow();
		graphics.fillOval((int) _ball.getX(), (int) _ball.getY(), _ball.width,
				_ball.height);
		graphics.setColor(Color.GREEN);
		graphics.fillRoundRect(_paddle.x, _paddle.y, _paddle.width,
				_paddle.height, 10, 10);
		drawBall(graphics);
		drawBricks(graphics);
		drawScore(graphics);
		drawChances(graphics);
		drawEffects(graphics);
		checkEffecthitstoPaddle();
		if (_chances == 0) {
			Font big = new Font("SansSerif", Font.ITALIC, 48);
			graphics.setFont(big);
			graphics.setColor(Color.GREEN);
			graphics.drawString("Click Restart button to Restart the Game",
					(int) (_dimension.width / 4), _dimension.height / 2);
			_score = 0;
			_isRunning = false;
		}
	}

	/**
	 * resets the when touches to ground
	 */
	private void resetBall() {
		setinitialBallValues();
		_isRunning = false;
		_ball = new Ball(_paddle.x + _paddle.width / 2, _paddle.y
				- _ballWeightNHeight, _ballWeightNHeight, _ballWeightNHeight);
		updateBoard();
	}

	/**
	 * updates the ball on the view
	 * 
	 * @param graphics
	 */
	private void drawBall(Graphics graphics) {
		_ball.x = _ball.x + _ball.getXVelocity();
		_ball.y = _ball.y + _ball.getYVelocity();
		setBallBounds();
		updateBoard();
	}

	/**
	 * set the bricks for the graphics
	 */
	private void setBricks() {
		int currentRow = 1;
		invalidate();
		_blocks = new Blocks[40];
		Blocks tempBlock = null;
		_blockWidth = _dimension.width / 10;
		_blockHeight = _dimension.height / 20;

		int pos = 0;
		for (int i = 0; i < _blocks.length; i++) {
			currentRow = i / 8;
			if (pos == 7) {
				RandomGenerator rand = new RandomGenerator();
				try {
					_blocks[Math.abs(rand.nextInt((currentRow * 8),
							((currentRow + 1) * 8) - 1))].set_type(Type.BOMB);
					_blocks[Math.abs(rand.nextInt((currentRow * 8),
							((currentRow + 1) * 8) - 1))]
							.set_type(Type.SMALL_PADDLE);
					_blocks[Math.abs(rand.nextInt((currentRow * 8),
							((currentRow + 1) * 8) - 1))]
							.set_type(Type.BIG_PADDLE);
				} catch (Exception e) {
				}
			}
			if (pos > 7) {
				pos = 0;
			}
			if (currentRow % 2 == 0)
				tempBlock = new Blocks(pos * (_blockWidth + 2) + _blockWidth,
						(_blockHeight + 6) * (currentRow + 1), _blockWidth,
						_blockHeight);
			else
				tempBlock = new Blocks(pos * (_blockWidth + 2) + _blockWidth
						/ 2, (_blockHeight + 6) * (currentRow + 1),
						_blockWidth, _blockHeight);
			tempBlock.set_color(getRandomColor());
			_blocks[i] = tempBlock;
			pos++;
		}
	}

	/**
	 * draws and updates the bricks on graphics
	 * 
	 * @param Graphics
	 */
	private void drawBricks(Graphics g) {
		for (int i = 0; i < _blocks.length; i++) {
			if (!_blocks[i]._isHit()) {
				g.setColor(_blocks[i].get_color());
				g.fillRoundRect(_blocks[i].x, _blocks[i].y, _blocks[i].width,
						_blocks[i].height, 20, 20);
//				Font font;
//				switch (_blocks[i].get_type()) {
//				case BOMB:
//					font = new Font(Font.SANS_SERIF, Font.BOLD, 15);
//					g.setFont(font);
//					g.setColor(Color.black);
//					g.drawString(_blocks[i].get_type().name().toLowerCase(),
//							_blocks[i].x + (int) (_blocks[i].width / 3.5),
//							_blocks[i].y + (_blocks[i].height / 2));
//					break;
//				case BIG_PADDLE:
//					font = new Font(Font.MONOSPACED, Font.BOLD, 15);
//					g.setFont(font);
//					g.setColor(Color.black);
//					g.drawString(_blocks[i].get_type().name().toLowerCase(),
//							_blocks[i].x + (int) (_blocks[i].width / 3.5),
//							_blocks[i].y + (_blocks[i].height / 2));
//					break;
//				case SMALL_PADDLE:
//					font = new Font(Font.SERIF, Font.BOLD, 15);
//					g.setFont(font);
//					g.setColor(Color.black);
//					g.drawString(_blocks[i].get_type().name().toLowerCase(),
//							_blocks[i].x + (int) (_blocks[i].width / 3.5),
//							_blocks[i].y + (_blocks[i].height / 2));
//					break;
//				case DEFAULT:
//				default:
//					break;
//				}
			}
			else 
			{
				effect = new Effects(_blocks[i].toString());
				ImageIcon image;
				effect.set_Yvelocity(3);
				effect.set_X(_blocks[i].x + (int) (_blocks[i].width / 3.5));
				effect.set_Y(_blocks[i].y + (_blocks[i].height / 2));
				effect.set_type(_blocks[i].get_type());
				URL imageurl;
				switch (_blocks[i].get_type()) {
				case BOMB:
					imageurl	= getClass().getClassLoader().getResource("bomb.png");
					image = new ImageIcon(imageurl);
					effect.set_image(image);
					if (!checkiFEffectExists(_blocks[i]))
						_effects.add(effect);
					break;
				case BIG_PADDLE:
					imageurl	= getClass().getClassLoader().getResource("bigpaddle.png");
					image = new ImageIcon(imageurl);
					effect.set_image(image);
					if (!checkiFEffectExists(_blocks[i]))
						_effects.add(effect);
					break;
				case SMALL_PADDLE:
					imageurl	= getClass().getClassLoader().getResource("smallpaddle.png");
					image = new ImageIcon(imageurl);
					effect.set_image(image);
						if (!checkiFEffectExists(_blocks[i]))
							_effects.add(effect);
					break;
				case DEFAULT:
				default:
					break;
				}

			}
			updateBoard();
		}
	}
	/**
	 * checks if effects are exists in array if not return true
	 * @param block
	 * @return
	 */
	private boolean checkiFEffectExists(Blocks block)
	{
		boolean checkiFexists = false;
		if (_effects.size() <= 0) 
		{
			_effects.add(effect);
		}
		else
		{
			for (int j = 0; j < _effects.size(); j++) 
			{
				if (_effects.get(j).toString().equalsIgnoreCase(block.toString())) 
				{
					checkiFexists = true;
				}
			}
	}
		return checkiFexists;
	}
	/**
	 * 
	 * @param Graphics
	 */
	private void drawEffects(Graphics g) {
		for (int i = 0; i < _effects.size(); i++) {
			Effects effect = _effects.get(i);
			ImageIcon image = effect.get_image();
			int x = effect.get_X();
			int y = effect.get_Y();
			int width = effect.get_width();
			int height = effect.get_height();
			int yVelocity = effect.get_Yvelocity();
			switch (effect.get_type()) {
			case BOMB:
				g.drawImage(image.getImage(), x, y + yVelocity, width, height, this);
				effect.set_Y(y + yVelocity);
				break;
			case SMALL_PADDLE:
				g.drawImage(image.getImage(), x, y + yVelocity, width, height, this);
				effect.set_Y(y + yVelocity);
				break;
			case BIG_PADDLE:
				g.drawImage(image.getImage(), x, y + yVelocity, width, height, this);
				effect.set_Y(y + yVelocity);
				break;
			case DEFAULT:
			default:
				break;

			}
		}
	}
/**
 * checks intersection of effect and paddle
 */
	private void checkEffecthitstoPaddle() {
		for (int i = 0; i < _effects.size(); i++) {
			Effects effect = _effects.get(i);
			int x = effect.get_X();
			int y = effect.get_Y();
			int width = effect.get_width();
			int height = effect.get_height();
			Rectangle effectRect = new Rectangle(x, y, width, height);
			Timer timer;
			if (_paddle.intersects(effectRect)) {
				switch (effect.get_type()) {
				case BOMB:
					if(_chances > 0)
					{
					_chances = _chances - 1;
					}
					if(_chances == 0)
					{
						resetBall();
					}
					effect.set_X(0);
					effect.set_Y(0);
					effect.set_Yvelocity(-10);
					break;
				case BIG_PADDLE:
					_paddle = new Blocks((int) (_paddle.x),
							_paddle.y, 250, 50);
					timer = new Timer();
				    timer.schedule(new TimeUpEffect(), 7 * 1000);
				    effect.set_X(0);
					effect.set_Y(0);
					effect.set_Yvelocity(-10);
					break;
				case SMALL_PADDLE:
					_paddle = new Blocks((int) (_paddle.x),
							_paddle.y, 150, 50);
					timer = new Timer();
				    timer.schedule(new TimeUpEffect(), 7 * 1000);
				    effect.set_X(0);
					effect.set_Y(0);
					effect.set_Yvelocity(-10);
					break;
				case DEFAULT:
				default:
					break;
				}
			}
		}

	}
	
	/**
	 * Draws and Updates the number of chances
	 * 
	 * @param g
	 */
	private void drawChances(Graphics g) {
		Font font = new Font("SansSerif", Font.BOLD, 48);
		g.setFont(font);
		g.setColor(Color.GREEN);
		g.drawString("Chances  :  " + _chances, _dimension.width - 400, 40);
	}

	/**
	 * Draws and Updates the Score
	 * 
	 * @param Graphics
	 */
	private void drawScore(Graphics g) {
		Font font = new Font("SansSerif", Font.BOLD, 48);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Score  :  " + _score, 50, 40);
	}

	/**
	 * sets the boundaries for ball and Paddle
	 */
	private void setBallBounds() {
		// sets boundaries for ball in the horizontal direction
		if (_ball.x + 10 >= _dimension.width || _ball.getX() <= 0) {
			_ball.setXVelocity(-1 * _ball.getXVelocity());
		}

		// sets boundary for ball in the vertical direction
		if (_ball.getY() < 10) {
			_ball.setYVelocity(-1 * _ball.getYVelocity());
		}
		Rectangle circleRect = new Rectangle(_ball.x, _ball.y, _ball.width,
				_ball.height);
		Rectangle paddleRect = new Rectangle(_paddle.x, _paddle.y,
				_paddle.width, _paddle.height);
		if (paddleRect.intersects(circleRect)) {
			_ball.setYVelocity(-1 * _ball.getYVelocity());
		}

		// if ball hits the ground we are resetting ball
		if (_ball.y >= _dimension.height - _borderLineWidth) {
			resetBall();
			_chances--;
		}

		// updating the bricks on graphics
		for (int i = 0; i < _blocks.length; i++) {
			Rectangle blockRect = new Rectangle((int) _blocks[i].x,
					(int) _blocks[i].y, (int) _blocks[i].width,
					(int) _blocks[i].height);
			if (!_blocks[i]._isHit())
				if (circleRect.intersects(blockRect)) {
					_blocks[i].set_isHit(true);
					_ball.setYVelocity(-1 * _ball.getYVelocity());
					_score += 10;
				}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			if (_chances > 0 && !_isRunning) {
				_ball.setXVelocity(-5);
				_ball.setYVelocity(-5);
				_isRunning = !_isRunning;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (_paddle.x + _paddle.width >= _dimension.width
					- _borderLineWidth) {
				_paddle.x = _paddle.x - _borderLineWidth;
				return;
			}
			_paddle.x = _paddle.x + 120;
			break;
		case KeyEvent.VK_LEFT:
			if (_paddle.x <= 10) {
				_paddle.x = _paddle.x + _borderLineWidth;
				return;
			}
			_paddle.x = _paddle.x - 120;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	private void updateBoard() {
		repaint();
	}

	/**
	 * gives the random Color
	 * 
	 * @return Color
	 */
	private Color getRandomColor() {
		Random randomGenerator = new Random();
		int red = randomGenerator.nextInt(255);
		int green = randomGenerator.nextInt(255);
		int blue = randomGenerator.nextInt(255);
		return new Color(red, green, blue, 200);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _help) {
			showHelpAlert();
		} else if (e.getSource() == _exit) {
			System.exit(0);
		} else if (e.getSource() == _restart) {
			Restartgame();
		}

	}

	/**
	 * Restarts the game
	 */
	private void Restartgame() {
		resetBall();
		setBricks();
		updateBoard();
		_effects.clear();
		_isRunning = false;
		_chances = 5;
		_score = 0;
		for (int i = 0; i < _blocks.length; i++) {
			_blocks[i].set_isHit(false);
		}
	}

	private class RandomGenerator extends Random {
		public int nextInt(int Lower_limit, int Upper_limit) {
			return nextInt((Upper_limit - Lower_limit + 1)) + Lower_limit;
		}
	}
	
	private class TimeUpEffect extends TimerTask {
	    public void run()
	    {
	      _paddle = new Blocks((int) (_paddle.x),
					_paddle.y, 200, 50);
	    }
	  }
}
