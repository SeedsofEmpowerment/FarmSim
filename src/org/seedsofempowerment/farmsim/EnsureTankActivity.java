/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : EnsureTankActivity.java
* description : This activity is to make sure whether the user wnat to buy a water tank
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

//This activity is called when the user want to buy a water tank and a sensor 
public class EnsureTankActivity extends Activity{
	private Button okButton, cancelButton;
	public static Activity instance;
	private TextView tv;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//hidden the battery flag and any other parts
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//hidden the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ensuretanklayout);
		tv = (TextView)findViewById(R.id.tv);
		tv.setTextColor(Color.BLACK);
		okButton = (Button) findViewById(R.id.okbutton);
		cancelButton = (Button) findViewById(R.id.cancelbutton);
		okButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){	
 					if(GameInfo.money >= 2000 ){
 						GameInfo.money -= 2000;
 						GameInfo.isWaterTankSelected = true;
 						EnsureTankActivity.this.finish();
 					}
 					else{
 						Intent intent = new Intent();
 						intent.setClass(EnsureTankActivity.this, EnsureTankCancelActivity.class);
 						EnsureTankActivity.instance.startActivity(intent);
 					}
 				}
 				return false;
 			}
         });
		
		cancelButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
 					EnsureTankActivity.instance.finish();
 				}
				return false;
 			}
         });
	}
}
