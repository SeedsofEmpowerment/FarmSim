/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : CreateAccountActivity.java
* description : When the user press create a new account, it will create a new activity
*
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

//This Activity is called when the user create a new account
public class CreateAccountActivity extends Activity{	
	private Button okButton;
	private Button cancelButton;
	private EditText name;
	private EditText age;
	private RadioButton boy;
	private RadioButton girl;
	private boolean isComplete = true;
	private boolean isExist = false;
	public static Activity instance;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
   	 	//hidden the battery flag and any other parts
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hidden the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.createaccountlayout);
        name = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        boy = (RadioButton)findViewById(R.id.boybutton);
        girl = (RadioButton)findViewById(R.id.girlbutton);
        GameInfo.user = new User();
        okButton = (Button)findViewById(R.id.okbutton);
        cancelButton = (Button)findViewById(R.id.cancelbutton);

		okButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
 					if(!(name.getText().toString().trim().equals(""))){
 						GameInfo.user.setName(name.getText().toString());
 					}
 					else{
 						isComplete = false;
 						name.setText("");
 					}
 					GameInfo.user.setAge(0);
 					if(!(age.getText().toString().trim().equals(""))){
 						GameInfo.user.setAge(Integer.parseInt(age.getText().toString()));
 					}		
 					if(GameInfo.user.getAge() <= 0 || GameInfo.user.getAge() > 100){
 						isComplete = false;
 						age.setText("");
 					}
 					if(boy.isChecked()==true){
 						GameInfo.user.setGender(0);
 					}
 					else if(girl.isChecked()==true){
 						GameInfo.user.setGender(1);
 					}
 					else{
 						GameInfo.user.setGender(2);
 					}
 					if(isComplete == true){
 						//judge whether this account has already existed
 						boolean isHaveSDCard = false;
	    				if(Environment.getExternalStorageState() != null && !Environment.getExternalStorageState().equals("removed")){
	    					Log.v("wang","read from files");
	    					isHaveSDCard = true;
	    				}
	    				if(isHaveSDCard){
	    					File path = new File("/sdcard/FarmSim/save");
	    					if(path.exists()){
	    						final String nameSequence[];
	    						nameSequence = path.list();
	    						for(String temp: nameSequence){
	    							int j = temp.indexOf(".");
	    				        	if(j != -1){
	    				        		temp = temp.substring(0, j);
	    				        	} 
	    							if(temp.equals(GameInfo.user.getName())){
	    								isExist = true;
	    							}
	    						}
	    					}
	    				}
	    				else{
	    					File path = new File("/");
	    					if(path.exists()){
	    						final String nameSequence[];
	    						nameSequence = path.list();
	    						for(String temp: nameSequence){
	    							int j = temp.indexOf(".");
	    				        	if(j != -1){
	    				        		temp = temp.substring(0, j);
	    				        	} 
	    							if(temp.equals(GameInfo.user.getName())){
	    								isExist = true;
	    							}
	    						}
	    					}
	    				}
	    				if(isExist == false){
	 						GameInfo.isStartAllowed = true;
							GameInfo.isCreateSurfaceView = false;
							CreateAccountActivity.this.finish();
	    				}
	    				else{
	    					Intent intent = new Intent(CreateAccountActivity.this, AccountAlreadyExistActivity.class);
	 						CreateAccountActivity.this.startActivity(intent);
	    				}
 					}
 					else{
 						Intent intent = new Intent(CreateAccountActivity.this, LackInfoActivity.class);
 						CreateAccountActivity.this.startActivity(intent);
 					}
 					isComplete = true;
 					isExist = false;
 				}
				return false;
 			}
         });
		
		cancelButton = (Button) findViewById(R.id.cancelbutton);
		cancelButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
 					GameInfo.isCreateSurfaceView = false;
					CreateAccountActivity.this.finish();
 				}
				return false;
 			}
         });
	}
	
	public void onBackPressed() {
		GameInfo.isCreateSurfaceView = false;
	}
}