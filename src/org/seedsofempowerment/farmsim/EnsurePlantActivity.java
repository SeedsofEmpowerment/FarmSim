/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : EnsurePlantActivity.java
* description : This activity is to make sure whether the user want to plant the seed
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

//This activity is called after the user select a plant
public class EnsurePlantActivity extends Activity{
	private Button okButton, backButton, cancelButton;
	public static Activity instance;
	private TextView tv;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//hidden the battery flag and any other parts
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//hidden the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ensureplantlayout);
		tv = (TextView)findViewById(R.id.tv);
		tv.setTextColor(Color.BLACK);
		okButton = (Button) findViewById(R.id.okbutton);
		backButton  = (Button) findViewById(R.id.backbutton);
		cancelButton = (Button) findViewById(R.id.cancelbutton);
		okButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
 					if(GameInfo.money >= FarmGamming.CropInfo[GameInfo.selectCropNumber].cost){
 						long currentTime = System.currentTimeMillis();
						long time = (currentTime - GameInfo.startTime) / 1000;
						GameInfo.timeCropPlanted[GameInfo.timeCropNumber].time = time;
						if(GameInfo.selectCropNumber == 0){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Capsicum";
						}
						else if(GameInfo.selectCropNumber == 1){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Watermelon";
						}
						else if(GameInfo.selectCropNumber == 2){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Beans";
						}
						else if(GameInfo.selectCropNumber == 3){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Peach";
						}
						else if(GameInfo.selectCropNumber == 4){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Rice";
						}
						else if(GameInfo.selectCropNumber == 5){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Wheat";
						}
						else if(GameInfo.selectCropNumber == 6){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Guava";
						}
						else if(GameInfo.selectCropNumber == 7){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Apple";
						}
						else if(GameInfo.selectCropNumber == 8){
							GameInfo.timeCropPlanted[GameInfo.timeCropNumber].crop = "Rose";
						}
						++(GameInfo.timeCropNumber);
						EnsurePlantActivity.this.finish();
						SelectPlantActivity.instance.finish();
 					}
 					else{
 						GameInfo.selectCropNumber = 0;
 						GameInfo.choose = 0;
 						GameSurfaceView.myView.myDraw();
 						Intent intent = new Intent();
						intent.setClass(EnsurePlantActivity.this, EnsurePlantCancelActivity.class);
						EnsurePlantActivity.this.startActivity(intent);
 					}
 				}
				return false;
 			}
         });
		
		backButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
					Intent intent = new Intent();
					intent.setClass(EnsurePlantActivity.this, SelectPlantActivity.class);
					EnsurePlantActivity.this.finish();
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
					intent.setClass(EnsurePlantActivity.this, GameEngineActivity.class);
					GameInfo.choose = 0;
					GameSurfaceView.myView.myDraw();
					EnsurePlantActivity.this.finish();
					SelectPlantActivity.instance.finish();
 				}
				return false;
 			}
         });
	}
}
