package com.block.breaker;

import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BlocksView extends RelativeLayout implements OnRatingBarChangeListener, OnClickListener,OnKeyListener
{
	private Blocks[] _blocks;
	private Blocks _paddle;
	private Ball _ball;
	//boolean for checking if the ball dropped to ground wall
	private boolean _isDropped = true;
	private final int _paddleWidth = 200,_paddleHeight = 50;
	private int width = getDisplay().getWidth();
	private int height = getDisplay().getHeight();
	private int _ballRadius = 20;
	private Canvas _canvas;
	private final int STARTAGAIN = 100;
	private int _blockWidth,_blockHeight;
	private String _taptoStart = getResources().getString(R.string.taptostart);
	private int score = 0;
	private RatingBar _ratingBar ;
	private int _noOfChances = 3;
	private Button _cancel;
	private Button _start;
	private Dialog _dialog;
	private int _rating;
	// X and Y velocities of the ball
	private int _xVelocity;
	private int _yVelocity;
	//this constructor is called when we define the class in the layout xml wiyh style
	public BlocksView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	//this constructor is called when we define the class in the layout xml
	public BlocksView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BlocksView(Context context) {
		super(context);
		init();
	}
	//returns the display properties
	private Display getDisplay() {
		WindowManager display = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		return display.getDefaultDisplay();
	}
	
	/**
	 * At the starting this dialog appears
	 */
	private void showDialog()
	{
		_dialog = new Dialog(getContext());
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.level,null);
		_handler.post(new Runnable() {
			
			@Override
			public void run() 
			{
				_ratingBar  = (RatingBar) dialogLayout.findViewById(R.id.ratingBar);
				_cancel = (Button) dialogLayout.findViewById(R.id.cancel);
				_start = (Button) dialogLayout.findViewById(R.id.start);
				_ratingBar.setMax(5);
				_ratingBar.setNumStars(3);
				_ratingBar.setRating(0);
				_ratingBar.setOnRatingBarChangeListener(BlocksView.this);
				_cancel.setOnClickListener(BlocksView.this);
				_start.setOnClickListener(BlocksView.this);
				_dialog.setOnKeyListener(BlocksView.this);
				_dialog.setContentView(dialogLayout);
			}
		});
		_dialog.show();
	}
	//handle the messages what type of messages we can sent and performs the action
	private Handler _handler = new Handler() 
	{
		

		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case 100:
				_ball.setXVelocity(_xVelocity);
				_ball.setYVelocity(_yVelocity);
				_isDropped = false;
				invalidate();
				break;
			default:
				break;
			}
		}
	};
	//Defining and creating all the bricks, ball and paddle
	private void init() 
	{
		showDialog();
		setWillNotDraw(false);
		setLayoutParams(new LayoutParams(width, height));
		//this will call onDraw(canvas) method and update the view
		this.invalidate();
		setPaddle();
		setBricks();
		setBall();
	}
	

	/**
	 * sets the bottom paddle
	 */
	private void setPaddle() 
	{
		_paddle = new Blocks(width / 3, height - 60);
		_paddle.right = _paddle.left + _paddleWidth;
		_paddle.bottom = _paddle.top + _paddleHeight;
		Paint p = new Paint();
		p.setColor(Color.rgb(69, 91, 93));
		p.setAntiAlias(true);
		_paddle.setPaint(p);
		invalidate();
		
	}
	private void showToast()
	{
		Toast.makeText(getContext(), _taptoStart, Toast.LENGTH_SHORT).show();
	}
	/**
	 * set the all top blocks
	 */
	private void setBricks() 
	{
		int currentRow = 1;
		invalidate();
		_blocks = new Blocks[50];
		Blocks tempBlock = null;
		_blockWidth = width/10;
		_blockHeight = height/20;
		int pos = 0;
		for(int i = 0; i < _blocks.length;i++)
		{
				currentRow = i / 10;
				if(pos > 9)
				{
					pos = 0;
				}
				tempBlock = new Blocks(pos * (_blockWidth+10), (_blockHeight + 50) * (currentRow+1));
				tempBlock.right = tempBlock.left + _blockWidth;
				tempBlock.bottom = tempBlock.top + _blockHeight;
				tempBlock.setPaint(getRandomColor());
				_blocks[i] = tempBlock;
				pos++;
		}
	}
	private Paint getRandomColor()
	{
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Random randomGenerator = new Random();
		int red = randomGenerator.nextInt(255);
		int green = randomGenerator.nextInt(255);
		int blue = randomGenerator.nextInt(255);
		paint.setColor(Color.rgb(red, green, blue));
		return paint;
	}
	
	// this is call back method for a view when any changes occurs on that view
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		_canvas = canvas;
		drawPaddle();
		drawBricks();
		drawBall();
		drawScore();
		drawChances();
	}
	
	/**
	 * drawing the paddle within its boundaries and updating on canvas
	 */
	private void drawPaddle()
	{
		invalidate();
		_canvas.drawRoundRect(_paddle, 6, 6, _paddle.getPaint());
	}
	
	
	/**
	 * drawing the ball with its velocity and updating on canvas
	 */
	private void drawBall()
	{
		_ball.setX(_ball.getX() + _ball.getXVelocity());
		_ball.setY(_ball.getY() + _ball.getYVelocity());
		setBallBounds();
		RectF rect = new RectF(_ball.getX(), _ball.getY() - _ballRadius, _ball.getX() + _ballRadius, _ball.getY());
		_canvas.drawRoundRect(rect, _ballRadius/2, _ballRadius/2, _ball.getPaint());
		invalidate();
	}
	
	/**
	 * drawing the bricks and updating on canvas
	 */
	private void drawBricks()
	{
		for(int i= 0; i<_blocks.length;i++)
		{
			if(!_blocks[i].isHit())
			_canvas.drawRoundRect(_blocks[i], 5,5,_blocks[i].getPaint());
		}
	}
	
	/**
	 * drawing the score and updating the score when collission occurs
	 */
	private void drawScore()
	{
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setTextSize(30);
		_canvas.drawText("Score : " + score , 50, 50, paint);
	}
	
	/**
	 * setting the ball when starting and restarting the game
	 */
	private void setBall()
	{
		invalidate();
		_ball = new Ball((int)_paddle.left + _paddleWidth / 2, (int)_paddle.top , _ballRadius);
		_ball.setXVelocity(0);
		_ball.setYVelocity(0);
	}
	
	/**
	 * setting ball boundaries and drawing the bricks on the view
	 */
	private void setBallBounds()
	{
		
		 //sets boundaries for ball in the horizontal direction
        if(_ball.getX() + 5 >= width || _ball.getX() <= 0)
        {
        	_ball.setXVelocity(-1 * _ball.getXVelocity());
        }
        
        //sets boundary for ball in the vertical direction
        if(_ball.getY() < 0)
        {
        	_ball.setYVelocity(-1 * _ball.getYVelocity());
        }
        Rect circleRect = new Rect(_ball.getX(), _ball.getY() - _ballRadius, _ball.getX() + _ballRadius, _ball.getY());
        Rect paddleRect = new Rect((int) _paddle.left, (int) _paddle.top,
				(int) _paddle.right, (int) _paddle.bottom);
        //checking intesrsection of circle and paddle
        if(circleRect.intersect(paddleRect))
        {
        	_ball.setYVelocity(-1 * _ball.getYVelocity());
        }
        //if ball falls to the ground
        if(circleRect.bottom > height)
        {
        	_noOfChances--;
        	setBall();
        	_isDropped = true;
        	if(_noOfChances > 0)
        		showToast();
        	_ratingBar.setProgress(_noOfChances);
        }
        //drawing the blocks on the canvas
        for (int i = 0; i < _blocks.length; i++) 
        {
        	Rect blockRect = new Rect((int) _blocks[i].left, (int) _blocks[i].top,
    				(int) _blocks[i].right, (int) _blocks[i].bottom);
        	if(!_blocks[i].isHit())
        	if(circleRect.intersect(blockRect))
        	{
        		_blocks[i].setHit(true);
        		_ball.setYVelocity(-1 * _ball.getYVelocity());
        		score += 10;
        	}
		}
        if(_noOfChances <= 0)
        {
        	Paint p = new Paint();
        	p.setColor(Color.rgb(255, 0, 0));
        	p.setTextSize(20);
        	_canvas.drawText("Game Over to restart close the app and start again ", 5, height/3, p);
        	_isDropped = false;
        }
	}
	
	//setting the boundaries of the paddle
	private void checkpaddleIntersection(MotionEvent event) 
	{
		int x = (int) event.getX(), y = (int) event.getY();
		Rect rect = new Rect(x, y, x + _paddleWidth, y + _paddleHeight);
		Rect paddleRect = new Rect((int) _paddle.left, (int) _paddle.top,
				(int) _paddle.right, (int) _paddle.bottom);
		if (rect.intersect(paddleRect)) 
		{
			if(x >= 5)
			{
				_paddle.left = x;
				_paddle.right = _paddle.left + _paddleWidth;
				_paddle.bottom = _paddle.top + _paddleHeight;
				invalidate();
			}
			if(_paddle.right >= width)
			{
				_paddle.left = width - _paddleWidth;
				_paddle.right = _paddle.left + _paddleWidth;
				_paddle.bottom = _paddle.top + _paddleHeight;
				invalidate();
			}
			
		}

	}

	
/**
 * When clicking start button setting the velocity of ball
 * @param rating
 */
	private void startGame(int rating) 
	{
		if(rating == 0 || rating == 1)
		{
			_xVelocity = -5;
			_yVelocity = -5;
			_ratingBar.setRating(1);
			invalidate();
		}
		if(rating == 2)
		{
			_xVelocity = -10;
			_yVelocity = -10;
			invalidate();
			_ratingBar.setRating(2);
		}
		if(rating == 3)
		{
			_xVelocity = -18;
			_yVelocity = -18;
			invalidate();
			_ratingBar.setRating(3);
		}
	}
//Actions for cancel and start buttons
	@Override
	public void onClick(View v) 
	{
		if(v.getId() == _cancel.getId())
		{
			System.exit(0);
		}
		if(v.getId() == _start.getId())
		{
			_dialog.dismiss();
			startGame(_rating);
			showToast();
			invalidate();
		}
	}

	/**
	 * when on the level selection screen we are disabling back,home and search buttons
	 */
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.KEYCODE_4 :
			case KeyEvent.KEYCODE_SEARCH :
			case KeyEvent.KEYCODE_BACK :
			default :
				break;
		}
		return true;
	}
	//when touching the paddle on the screen we are moving the paddle to the touched direction with in its boundaries
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		checkpaddleIntersection(event);
		if(_isDropped)
		{
			_handler.sendEmptyMessage(STARTAGAIN);
		}
		return true;
	}
//This is called when you selecting the level
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rate,
			boolean fromUser) 
	{
		if(fromUser)
		{
			_rating = (int) rate;
		}
		
	}
	
	/**
	 * Draw the chances for the game
	 */
	private void drawChances()
	{
		invalidate();
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setTextSize(20);
		p.setColor(Color.rgb(255, 255, 255));
		_canvas.drawText("Chances :  "+_noOfChances, (int)(width/1.5), 50, p);
	}
}
