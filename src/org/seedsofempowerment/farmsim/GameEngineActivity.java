/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : GameEngineActivity.java
* description : This class is the main activity of the game
* 
* created by Wang Shiliang at 4/10/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

//This Activity is where the whole game will be initialed
//Wrote by Shiliang Wang 2012/3/1

public class GameEngineActivity extends Activity {
    /** Called when the activity is first created. */
	public static Activity instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //hidden the battery flag and any other parts
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hidden the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.gamelayout);
        //get the resolution of the phone
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = 0;
        int height = 0;
        if(metric.widthPixels >= metric.heightPixels){
        	width = metric.widthPixels;  
        	height = metric.heightPixels;
        }
        else{
        	width = metric.heightPixels;
        	height = metric.widthPixels;
        }
        PhoneInfo.resolutionWidth = width;
        if(height == 800){
        	PhoneInfo.resolutionHeight = 750;
        }
        else{
        	PhoneInfo.resolutionHeight = height;
        }
        
        PhoneInfo.widthRatio = (double)PhoneInfo.resolutionWidth / 800;
        PhoneInfo.heightRatio = (double)PhoneInfo.resolutionHeight / 480;
        int densityDpi = metric.densityDpi;  // ÆÁÄ»ÃÜ¶ÈDPI£¨120 / 160 / 240 / 320£©
        PhoneInfo.densityDpi = densityDpi;
        if(densityDpi == 240){
        	PhoneInfo.figureWidthRatio = (double)PhoneInfo.resolutionWidth / 800;
        	PhoneInfo.figureHeightRatio = (double)PhoneInfo.resolutionHeight / 480;
        }
        else if(densityDpi == 160){
        	PhoneInfo.figureWidthRatio = (double)PhoneInfo.resolutionWidth / 480;
        	PhoneInfo.figureHeightRatio = (double)PhoneInfo.resolutionHeight / 320;
        }
        else if(densityDpi == 120){
        	PhoneInfo.figureWidthRatio = (double)PhoneInfo.resolutionWidth / 320;
        	PhoneInfo.figureHeightRatio = (double)PhoneInfo.resolutionHeight / 240;
        }      
        else if(densityDpi == 320){
        	PhoneInfo.figureWidthRatio = (double)PhoneInfo.resolutionWidth / 800;
        	PhoneInfo.figureHeightRatio = (double)PhoneInfo.resolutionHeight / 480;
        }
    }
    
    @Override
    //This function will process the event when the user press the back button
	public void onBackPressed() {
		if(GameSurfaceView.gameState == GameSurfaceView.GAME_MENU)
		{
			if(GameStartMenu.gameState == GameStartMenu.GAMEMENU){
				GameSurfaceView.myView.releaseGameMenuResource();
				instance.finish();
				System.exit(0);
			}
			else if(GameStartMenu.gameState == GameStartMenu.RANKING){
				GameStartMenu.gameState = GameStartMenu.GAMEMENU;
			}
		}
		else if(GameSurfaceView.gameState == GameSurfaceView.GAME_INTRO){
			GameSurfaceView.myView.releaseGameMenuResource();
			GameSurfaceView.myView.releaseGameIntroResource();
			GameSurfaceView.myView.initiateNewGame();			
			GameInfo.money = 100;
			GameInfo.startTime = System.currentTimeMillis();
			GameInfo.selectCropNumber = 0;
			GameInfo.choose = 0;
			GameInfo.isEnlarge = false;
			GameInfo.enlargeNumber = 6;
			GameInfo.remainOpportunity = 2;
			GameInfo.currentLoanNumber = 0;
			GameInfo.isWaterTankSelected = false;
			GameSurfaceView.gameState = GameSurfaceView.GAMMING;
		}
		else if(GameSurfaceView.gameState == GameSurfaceView.GAMMING){
			GameSurfaceView.myView.releaseGammingResource();
			GameSurfaceView.myView.initGame();
			GameSurfaceView.gameState = GameSurfaceView.GAME_MENU;
		}
	}
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("GameEngineActivity","onDestroy");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		if(GameSurfaceView.myView.gameState == GameSurfaceView.myView.GAMMING){
			GameInfo.isPause = true;
			GameInfo.pauseStartTime = System.currentTimeMillis();
		}
		*/
		Log.v("GameEngineActivity","onPause");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v("GameEngineActivity","onRestart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		if(GameSurfaceView.myView.gameState == GameSurfaceView.myView.GAMMING){
			GameInfo.pauseEndTime = System.currentTimeMillis();
			GameInfo.pauseTime = GameInfo.pauseEndTime - GameInfo.pauseStartTime;
			GameSurfaceView.myView.farmGamming.setPause();
			GameInfo.isPause = false;
		}
		*/
		Log.v("GameEngineActivity","onResume");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("GameEngineActivity","onStart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("GameEngineActivity","onStop");
	}
}