/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : EnsureTankCancelActivity.java
* description : If the user doesn't have enough money to buy a tank, it will pop up a new window
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

//This activity is called when the user don't have enougth money to buy the water tank and the sensor
public class EnsureTankCancelActivity extends Activity{
	private Button okButton;
	private TextView tv;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hidden the battery flag and any other parts
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//hidden the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ensuretankcancelayout);
		tv = (TextView)findViewById(R.id.tv);
		tv.setTextColor(Color.BLACK);
		okButton = (Button) findViewById(R.id.okbutton);
		okButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
					EnsureTankCancelActivity.this.finish();
					EnsureTankActivity.instance.finish();
 				}
				return false;
 			}
         });
	}
}
