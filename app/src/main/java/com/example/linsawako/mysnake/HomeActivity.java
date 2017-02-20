package com.example.linsawako.mysnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener{

	private Button infiniteButton;
	private Button timelimitButton;
	private Button setupButton;
	private AlertDialog setupDialog;
	private final int LEVEL1 = 0;
	private final int LEVEL2 = 1;
	private final int LEVEL3 = 2;
	private int level = LEVEL2;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_layout);
		
		if(getLevel() == -1){
			saveLevel();
			Log.d("HomeActivity", "do getLevel()");
		}
		
		infiniteButton = (Button) findViewById(R.id.start);
		setupButton = (Button) findViewById(R.id.setup);
		
		setupButton.setOnClickListener(this);
		infiniteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setup:
			showDialog();
			break;
		case R.id.start:
			Intent intent = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(intent);
		default:
			break;
		}
	}


	public void showDialog(){
		
		setupDialog = new AlertDialog.Builder(HomeActivity.this).create();  
		setupDialog.show();  
		setupDialog.getWindow().setContentView(R.layout.setupdialog_layout);  
		setupDialog.getWindow()  
            .findViewById(R.id.level1)  
            .setOnClickListener(leveListener);
		setupDialog.getWindow()  
        .findViewById(R.id.level2)  
        .setOnClickListener(leveListener);
		setupDialog.getWindow()  
        .findViewById(R.id.level3)  
        .setOnClickListener(leveListener);
	}
	
	public void saveLevel(){
		editor = getSharedPreferences("data", MODE_PRIVATE).edit();
		editor.putInt("level", level);
		editor.commit();
		
	}
	
	public int getLevel(){
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		level = pref.getInt("level", -1);
		return level;
	}
	
	OnClickListener leveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//editor.clear();
			switch (v.getId()) {
			case R.id.level1:
				level = LEVEL1;
				break;
			case R.id.level2:
				level = LEVEL2;
				break;
			case R.id.level3:
				level = LEVEL3;
				break;

			default:
				break;
			}
			saveLevel();
			setupDialog.dismiss();
		}
	};
	
}
