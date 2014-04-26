/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : InputNameActivity.java
* description : When the user load the game, it will pop up a new window
* 
* created by Wang Shiliang at 5/1/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

//This activity is called after the user load the game
public class InputNameActivity extends Activity{
	private Button okButton;
	private Button cancelButton;
	private Spinner name;
	private String stringName;
	
	public void onCreate(Bundle savedInstanceState) {
	   	 super.onCreate(savedInstanceState);
	   	 	//hidden the battery flag and any other parts
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        //hidden the title bar
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.inputnamelayout);
	        okButton = (Button)findViewById(R.id.ok_btn);
	        cancelButton = (Button)findViewById(R.id.cancel_btn);
	        name = (Spinner)findViewById(R.id.spinner);
	        boolean isHaveSDCard = false;
	        GameInfo.user = new User();
	        if(Environment.getExternalStorageState() != null && !Environment.getExternalStorageState().equals("removed")){
				Log.v("wang","read from files");
				isHaveSDCard = true;
			}
			if(isHaveSDCard){
				File path = new File("/sdcard/FarmSim/save");
				if(path.exists()){
					final String nameSequence[];
					nameSequence = path.list();
			        int i = 0;
			        for(String temp: nameSequence){
			        	int j = temp.indexOf(".");
			        	if(j != -1){
			        		nameSequence[i] = temp.substring(0, j);
			        	} 
			        	++i;
			        }        
			        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, nameSequence);
			        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        name.setAdapter(adapter);
			        name.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
			        {
			        	@Override
			            public void onItemSelected(AdapterView<?> arg0, View arg1,
			                    int arg2, long arg3) {
			                // TODO Auto-generated method stub
			        		stringName = nameSequence[arg2];
			            }

			            @Override
			            public void onNothingSelected(AdapterView<?> arg0) {
			                // TODO Auto-generated method stub
			                stringName = "";
			            }
			        });
				}
			}
			else{
				File path = new File("/");
				if(path.exists()){
					final String nameSequence[];
					nameSequence = path.list();
			        int i = 0;
			        for(String temp: nameSequence){
			        	int j = temp.indexOf(".");
			        	if(j != -1){
			        		nameSequence[i] = temp.substring(0, j);
			        	} 
			        	++i;
			        }
			        
			        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, nameSequence);
			        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        name.setAdapter(adapter);
			        name.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
			        {
			        	@Override
			            public void onItemSelected(AdapterView<?> arg0, View arg1,
			                    int arg2, long arg3) {
			                // TODO Auto-generated method stub
			        		stringName = nameSequence[arg2];
			            }

			            @Override
			            public void onNothingSelected(AdapterView<?> arg0) {
			                // TODO Auto-generated method stub
			                stringName = "";
			            }
			        });
				}
			}
        
	        okButton.setOnTouchListener(new OnTouchListener(){
	 			@Override
	 			public boolean onTouch(View v, MotionEvent event) {
	 				if(event.getAction() == MotionEvent.ACTION_DOWN){
	 					GameInfo.vibrate.playVibrate(-1);
	 					GameInfo.soundEffect[0].play((float) 0.3);
	 				}
	 				else if(event.getAction() == MotionEvent.ACTION_UP){
	 					if(!stringName.equals("")){
	 						GameInfo.user.setName(stringName);
	 						GameInfo.isCreateSurfaceView = false;
	 						InputNameActivity.this.finish();
	 						GameSurfaceView.myView.isLoad = true;
	 						GameSurfaceView.myView.releaseGameMenuResource();
	 						GameSurfaceView.gameState = GameSurfaceView.LOAD_GAME;
	 						GameSurfaceView.myView.initiateLoadGame();
	 						GameSurfaceView.gameState = GameSurfaceView.GAMMING;
	 						GameSurfaceView.myView.isLoad = false;	
 						}
						else{
							GameInfo.isCreateSurfaceView = false;
	 						InputNameActivity.this.finish();
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
						GameInfo.isCreateSurfaceView = false;
						InputNameActivity.this.finish();
	 				}
					return false;
	 			}
	         });
	}
	
	public void onBackPressed(){
		GameInfo.isCreateSurfaceView = false;
	}
}
