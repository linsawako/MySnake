package  com.example.linsawako.mysnake;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CanvasView extends View implements OnTouchListener{

	private String tagString = "CanvasView";
	
	private List<Point> snakeList = new ArrayList<Point>();
	private int viewHeight;//界面的高
	private int viewWidth;//界面的宽
	
	private Paint foodPaint;//画食物的画笔
	private Paint snakePaint;//画蛇的画笔
	
	private final int BOXSIZE = 12;//蛇每个圆圈的大小
	private final int INITSNAKELENGTH = 10;//蛇的初始化长度
	private Point foodPoint;//食物的点
	private boolean foodisEaten = true;//食物是否被吃了
	
	//定义方向的常量
	private final int UP = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;
	private int nowDre = UP;//现在的方向，默认向上
	
	private final int ADDSCORE = 2;//每吃一个食物增加的分数
	
	//构造函数
	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	

	public CanvasView(Context context) {
		super(context);
		init();
	}
	
	public void init(){
		//初始化蛇
		for (int i = 0; i < INITSNAKELENGTH; i++) {
			Point point = new Point(500, 500 + i * 2 * BOXSIZE);
			snakeList.add(point);
		}
		
		//初始化画笔
		foodPaint = new Paint();
		
		foodPaint.setARGB(255, Integer.valueOf("F7", 16), Integer.valueOf("CA", 16), Integer.valueOf("18", 16));
		snakePaint = new Paint();
		snakePaint.setARGB(255, Integer.valueOf("F2", 16), 78, Integer.valueOf("4B", 16));
		
		setOnTouchListener(this);//设置监听
		this.setFocusable(true);
	}
	
	@Override
    protected void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }
		

	@Override
	protected void onDraw(Canvas canvas) {
		//画的时候调用
		super.onDraw(canvas);
		
		drawBg(canvas);
		drawFood(canvas);
		drawSnake(canvas);
	}
	
	//画相应的
	public void drawBg(Canvas canvas){
		canvas.drawColor(Color.WHITE);
	}
	
	public void drawSnake(Canvas canvas){
		for (int i = 0; i < snakeList.size(); i++) {
			int x = snakeList.get(i).x;
			int y = snakeList.get(i).y;
			canvas.drawCircle(x, y, BOXSIZE, snakePaint);
		}
		moveSnake();//每此画完后在头部增加
		//后面的去掉一格，如果吃到食物就不删除
		if(isEated(foodPoint, snakeList.get(0))){
			foodisEaten = true;
			
			MainActivity.getMainActivity().addScore(ADDSCORE);
		}else {
			snakeList.remove(snakeList.size() - 1);
		}
	}
	
	public void drawFood(Canvas canvas){
		if(foodisEaten){
			//随机画食物
			int x = (int)(Math.random() * (viewWidth - 20));
			int y = (int)(Math.random() * (viewHeight - 20));
			foodPoint = new Point(x, y);
			foodisEaten = false;
		}
		canvas.drawCircle(foodPoint.x, foodPoint.y, BOXSIZE, foodPaint);
	}
	
	public void moveSnake(){
		Point newPoint = new Point();
		Point headPoint = new Point();
		headPoint = snakeList.get(0);
		
		//根据当前的方向，在头部进行增加
		switch (nowDre) {
		case UP:
			newPoint.x = headPoint.x;
			newPoint.y = headPoint.y - 2 * BOXSIZE;
			break;
		case DOWN:
			newPoint.x = headPoint.x;
			newPoint.y = headPoint.y + 2 * BOXSIZE;
			break;
		case LEFT:
			newPoint.x = headPoint.x - 2 * BOXSIZE;
			newPoint.y = headPoint.y;
			break;
		case RIGHT:
			newPoint.x = headPoint.x + 2 * BOXSIZE;
			newPoint.y = headPoint.y;
		default:
			break;
		}
		
		if (isOut(newPoint) || eatenItself(newPoint)) {
			loseGame();
		}else {
			
		}
		snakeList.add(0, newPoint);
	}
	
	public boolean eatenItself(Point headpoint){
		//判断食物是不是被吃了
		for (int i = 0; i < snakeList.size(); i++) {
			Point point = snakeList.get(i);
			if(isEated(headpoint, point)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isOut(Point point){
		//判断是否出界
		if(point.x < 0 || point.x > viewWidth)return true;
		if (point.y < 0 || point.y > viewHeight)return true;
		return false;
	}
	
	public void loseGame(){
		//输掉
		MainActivity.getMainActivity().setlost(true);
		Toast.makeText(getContext(), "you are losed", Toast.LENGTH_SHORT);
		Log.d(tagString, "you are losed");
	}
	
	
	public boolean isEated(Point point1, Point point2){
		//判断两个Rect是否有相交
		RectF rect1 = new RectF(point1.x - BOXSIZE, point1.y - BOXSIZE, point1.x + BOXSIZE, point1.y + BOXSIZE);
		RectF rect2 = new RectF(point2.x - BOXSIZE, point2.y - BOXSIZE, point2.x + BOXSIZE, point2.y + BOXSIZE);
		return rect1.intersect(rect2);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//打开时调用，得到height和width
		super.onSizeChanged(w, h, oldw, oldh);
		viewHeight = h;
		viewWidth = w;
	}
	
	float startX = 0, startY = 0, offsetX, offsetY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//触碰时处理
		int eventaction = event.getAction();   
        
        switch (eventaction ) {   
        case MotionEvent.ACTION_UP:   
        	//起来时记录手指方向
        	offsetX = event.getX() - startX;
			offsetY = event.getY() - startY;
			
			if(nowDre == UP || nowDre == DOWN){
				if(offsetX < -5){//避免误差，使用-5
					nowDre = LEFT;
				}else if(offsetX > 5){
					nowDre = RIGHT;
				}
			}else{
				if(offsetY < -5){
					nowDre = UP;
				}else if(offsetY > 5){
					nowDre = DOWN;
				}
			}
			break;   
        case MotionEvent.ACTION_DOWN:
        	//down时记下位置
        	startX = event.getX();
			startY = event.getY();
			break;
		default:
			break;
        } 
		return true;
	}
	
}
