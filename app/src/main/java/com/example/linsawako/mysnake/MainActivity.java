package com.example.linsawako.mysnake;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int LEVEL1 = 0;
	private final int LEVEL2 = 1;
	private final int LEVEL3 = 2;
	
	private CanvasView canvasView;
	private final int FRESHTIME = 60;
	private final int FRESH = 1;
	private boolean stop = false;
	private boolean lose = false;
	private static MainActivity mainActivity;
	private int score = 0;
	private int highestScore = 0;
	private TextView scoreTextView;
	private TextView highestTextView;
	private int level;
	private int freshtime = 60;
	
	public MainActivity() {
		mainActivity = this;
	}
	
	public void clearScore(){
		score = 0;
		showScore();
	}
	
	public void showScore(){
		scoreTextView.setText(score + "");
	}
	
	public void addScore(int addscore){
		score += addscore;
		showScore();
	}
	
	public void setStop(boolean stop){
		this.stop = stop;
	}
	
	
	public void setlost(boolean lose){
		this.lose = lose;
		
		AlertDialog loseDialog = new AlertDialog.Builder(MainActivity.this).create();
		loseDialog.show();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("result");
        if(score > highestScore){
        	builder.setMessage("congratulations~your score " + score + " is the highest!");
        	
        	 SharedPreferences.Editor  editor = getSharedPreferences("data", MODE_PRIVATE).edit();
             editor.putInt("infiniteScore", score);
             editor.commit();
        }else {
        	builder.setMessage("you are losed, you score is " + score);
		}
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("重来", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(MainActivity.this, MainActivity.class);
            	startActivity(intent);
            	finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
	}
	
	public static MainActivity getMainActivity(){
		return mainActivity;
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.arg1 == FRESH){
				canvasView.invalidate();
				//Log.d("MainActivity", "" + 1);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		canvasView = new CanvasView(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.snake_layout);
		
		scoreTextView = (TextView) findViewById(R.id.score);
		canvasView = (CanvasView) findViewById(R.id.snakeView);
		highestTextView = (TextView)findViewById(R.id.highest);
		
		getData();
		
		highestTextView.setText(highestScore + "");
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!stop && !lose){
					Message message = handler.obtainMessage();
					message.arg1 = FRESH;
					handler.sendMessage(message);
					
					try {
						Thread.sleep(freshtime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.start();
	}
	
	public void getData(){
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		int level = pref.getInt("level", 0);
		highestScore = pref.getInt("infiniteScore", 0);
		switch (level) {
		case LEVEL1:
			freshtime = 50;
			break;
		case LEVEL2:
			freshtime = 100;
			break;
		case LEVEL3:
			freshtime = 150;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
