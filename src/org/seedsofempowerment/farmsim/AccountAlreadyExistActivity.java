/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : AccountAlreadyExistActivity.java
* description : If the user create a new account, and the user account has already existed. 
* 				It will pop up a new window
* 
* created by Wang Shiliang at 5/2/2012 21:19:50
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

public class AccountAlreadyExistActivity extends Activity{
	private Button okButton;
	private Button cancelButton;
	private TextView tv;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hidden the battery flag and any other parts
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//hidden the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.accountalreadyexistlayout);
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
 					GameInfo.isStartAllowed = true;
					GameInfo.isCreateSurfaceView = false;
					Intent intent = new Intent();
					intent.setClass(AccountAlreadyExistActivity.this, GameEngineActivity.class);
					AccountAlreadyExistActivity.this.finish();
					CreateAccountActivity.instance.finish();
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
					Intent intent = new Intent();
					intent.setClass(AccountAlreadyExistActivity.this, GameEngineActivity.class);
					AccountAlreadyExistActivity.this.finish();
 				}
				return false;
 			}
         });
	}
}
