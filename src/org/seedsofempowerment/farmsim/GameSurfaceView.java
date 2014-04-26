/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : GameSurfaceView.java
* description : This is the mainly surfaceview for the whole game
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

//The main surfaceView of this game
public class GameSurfaceView extends SurfaceView implements Callback, Runnable{
	public static GameSurfaceView myView;
	private Thread th;
	public static int screenW, screenH;
	private Canvas canvas;
	private SurfaceHolder sfh;
	private Paint paint;
	private boolean flag;
	
	//define the game status
	public static final int GAME_MENU = 0;  //game menu
	public static final int START_GAME = 1; //start the game now
	public static final int LOAD_GAME = 2;  //load the game now
	public static final int SAVE_GAME = 3;
	public static final int GAME_INTRO = 4; //introduce the game
	public static final int GAMMING = 5;    //start playing
	
	//the current status in the game
	public static int gameState = GAME_MENU;
	
	//declare a resource to load the pictures
	private Resources res = this.getResources();
	
	private Bitmap backGround;
	private Bitmap rankBackGround;
	private Bitmap startScreen;
	private Bitmap[] startGameScreen;
	private Bitmap[] loadScreen;
	private Bitmap[] saveScreen;
	private Bitmap[][] gameBackGround;
	private Bitmap[] soils;
	private Bitmap[] tools;
	private Bitmap[][] crops;
	private Bitmap[][] animations;
	private Bitmap[] textPicture;
	private Bitmap[] savePicture;
	private Bitmap[] arrows;
	private Bitmap coin;
	private Bitmap calenderPlace;
	private Typeface font;
	
	private GameStartMenu gameStartMenu;
	private FarmGameIntro farmGameIntro;
	public static FarmGamming farmGamming;
	public boolean isStart;
	public boolean isLoad;
	private int loadingIndex = 0;
	
	public GameSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		font = Typeface.createFromAsset(context.getAssets(),"acme.ttf");
		paint.setColor(Color.WHITE);		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenW = this.getWidth();
		screenH = this.getHeight();
		
		myView = this;
		initGame();      //use to initialize this game
		flag = true;
		th = new Thread(this);
		th.start();
		myDraw();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		GameInfo.backGroundMusic.stop();
		GameInfo.backGroundMusic.release();
		for(int i = 0; i!=2 ;++i){
			GameInfo.soundEffect[i].release();
		}
		flag = false;	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			long start = System.currentTimeMillis();
			logic();
			myDraw();
			
			long end = System.currentTimeMillis();
			try {
				if (end - start < 250) {
					Thread.sleep(250 - (end - start));
				}
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			} 
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (gameState){
		case GAME_MENU:
			break;
		case GAME_INTRO:
			break;
		case GAMMING:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (gameState){
		case GAME_MENU:
			break;
		case GAME_INTRO:
			break;
		case GAMMING:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (gameState){
		case GAME_MENU:
			gameStartMenu.onTouchEvent(event);
			if(event.getAction() == MotionEvent.ACTION_UP){
				myDraw();
				if(isStart == true){
					releaseGameMenuResource();
					initiateGameIntro();
					gameState = GAME_INTRO;
					isStart = false;
				}
				else if(isLoad == true){
					;
				}
			}
			break;
		case START_GAME:
			break;
		case LOAD_GAME:
			break;
		case SAVE_GAME:
			break;
		case GAME_INTRO:
			farmGameIntro.onTouchEvent(event);
			myDraw();
			break;
		case GAMMING:
			farmGamming.onTouchEvent(event);
			myDraw();
			break;
		}
		return true;
	}
	
	public void logic(){
		switch(gameState){
		case GAME_MENU:
			break;
		case START_GAME:
			++loadingIndex;
			if(loadingIndex >=4){
				loadingIndex = 0;
			}
			break;
		case LOAD_GAME:
			++loadingIndex;
			if(loadingIndex >=4){
				loadingIndex = 0;
			}
			break;
		case GAME_INTRO:
			farmGameIntro.logic();
			break;
		case SAVE_GAME:
			++loadingIndex;
			if(loadingIndex >=4){
				loadingIndex = 0;
			}
			break;
		case GAMMING:
			farmGamming.logic();
			break;
		}
	}
	
	public void myDraw(){
		try{
			canvas = sfh.lockCanvas();
			if(canvas != null){
				canvas.drawColor(Color.BLACK);
				switch (gameState){
				case GAME_MENU:
					gameStartMenu.draw(canvas, paint);
					break;
				case START_GAME:
					canvas.drawBitmap(startGameScreen[loadingIndex], null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
					break;
				case LOAD_GAME:
					canvas.drawBitmap(loadScreen[loadingIndex], null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
					break;
				case SAVE_GAME:
					canvas.drawBitmap(saveScreen[loadingIndex], null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
				case GAME_INTRO:
					farmGameIntro.draw(canvas, paint);
					break;
				case GAMMING:				
					farmGamming.draw(canvas, paint);
					break;
				}
			}
		}
		catch(Exception e){
			Log.v("wang","Draw error");
		}
		finally{
			if(canvas != null)
			{
				sfh.unlockCanvasAndPost(canvas);
			}
		}	
	}
	
	public void initGame(){	
		//set the startScreen image
		startScreen = BitmapFactory.decodeResource(res, R.drawable.start_screen);
		try{
			canvas = sfh.lockCanvas();
			if(canvas != null){
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(startScreen, null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
			}
		}
		catch(Exception e){
			Log.v("wang","Draw error");
		}
		finally{
			if(canvas != null)
			{
				sfh.unlockCanvasAndPost(canvas);
			}
		}
		
		if(gameState == GAME_MENU){
			//load the resources of the game
			GameInfo.backGroundMusic = new BackGroundMusic(GameEngineActivity.instance, R.raw.background_music);
			GameInfo.backGroundMusic.setLooping();
			
			GameInfo.soundEffect = new SoundEffect[7];
			GameInfo.soundEffect[0] = new SoundEffect(GameEngineActivity.instance, R.raw.button_beep);
			GameInfo.soundEffect[1] = new SoundEffect(GameEngineActivity.instance, R.raw.shovel);
			GameInfo.soundEffect[2] = new SoundEffect(GameEngineActivity.instance, R.raw.plant);
			GameInfo.soundEffect[3] = new SoundEffect(GameEngineActivity.instance, R.raw.money);
			GameInfo.soundEffect[4] = new SoundEffect(GameEngineActivity.instance, R.raw.water);
			GameInfo.soundEffect[5] = new SoundEffect(GameEngineActivity.instance, R.raw.fertlizer);
			GameInfo.soundEffect[6] = new SoundEffect(GameEngineActivity.instance, R.raw.moneyfalls);
			
			GameInfo.vibrate = new Vibrate(GameEngineActivity.instance);
			backGround = BitmapFactory.decodeResource(res, R.drawable.background);
			rankBackGround = BitmapFactory.decodeResource(res, R.drawable.rankbackground);
			gameStartMenu = new GameStartMenu(backGround, rankBackGround);	
			startGameScreen = new Bitmap[4];
			startGameScreen[0] = BitmapFactory.decodeResource(res, R.drawable.startprogress);
			startGameScreen[1] = BitmapFactory.decodeResource(res, R.drawable.startprogress1);
			startGameScreen[2] = BitmapFactory.decodeResource(res, R.drawable.startprogress2);
			startGameScreen[3] = BitmapFactory.decodeResource(res, R.drawable.startprogress3);
			loadScreen = new Bitmap[4];
			loadScreen[0] = BitmapFactory.decodeResource(res, R.drawable.loadingprogress);
			loadScreen[1] = BitmapFactory.decodeResource(res, R.drawable.loadingprogress1);
			loadScreen[2] = BitmapFactory.decodeResource(res, R.drawable.loadingprogress2);
			loadScreen[3] = BitmapFactory.decodeResource(res, R.drawable.loadingprogress3);
			GameInfo.backGroundMusic.play();
		}
	}
	
	public void recycleResource(Bitmap picture){
		if(picture != null){
			picture.recycle();
		}
	}
	
	public void releaseGameMenuResource(){
		recycleResource(backGround);
		recycleResource(rankBackGround);
		if(isStart == true){
			for(int i = 0;i!=4;++i){
				recycleResource(loadScreen[i]);
			}
		}
		if(isLoad == true){
			for(int i = 0;i!=4;++i){
				recycleResource(startGameScreen[i]);
			}
		}
	}
	
	public void releaseGameIntroResource(){
		for(int i = 0;i!=16;++i){
			recycleResource(textPicture[i]);
		}

		for(int i = 0;i!=4;++i){
			recycleResource(startGameScreen[i]);
			recycleResource(loadScreen[i]);
		}
	}
	
	public void releaseGammingResource(){
		for(int i = 0;i!=4;++i){
			for(int j = 0;j!=4;++j){
				recycleResource(gameBackGround[i][j]);
			}
		}
		
		for(int i = 0;i!=9;++i){
			recycleResource(soils[i]);
		}
		
		for(int i = 0;i!=16;++i){
			recycleResource(tools[i]);
		}
		
		for(int i = 0;i!=9;++i){
			for(int j = 0;j!=7;++j){
				recycleResource(crops[i][j]);
			}
		}
		
		for(int i = 0;i!=4;++i){
			for(int j = 0;j!=2;++j){
				recycleResource(animations[i][j]);
			}
		}
		
		for(int i = 0;i!=4;++i){
			recycleResource(saveScreen[i]);
		}
		
		for(int i = 0;i!=4;++i){
			recycleResource(savePicture[i]);
		}	
	}
	
	public void initiateGameIntro(){
		gameBackGround = new Bitmap[4][4];
		gameBackGround[0][0] = BitmapFactory.decodeResource(res, R.drawable.background_spring_sunny);
		gameBackGround[0][1] = BitmapFactory.decodeResource(res, R.drawable.background_spring_cloudy);
		gameBackGround[0][2] = BitmapFactory.decodeResource(res, R.drawable.background_spring_rainy);
		gameBackGround[0][3] = BitmapFactory.decodeResource(res, R.drawable.background_spring_storm);
		gameBackGround[1][0] = BitmapFactory.decodeResource(res, R.drawable.background_summer_sunny);
		gameBackGround[1][1] = BitmapFactory.decodeResource(res, R.drawable.background_summer_cloudy);
		gameBackGround[1][2] = BitmapFactory.decodeResource(res, R.drawable.background_summer_rainy);
		gameBackGround[1][3] = BitmapFactory.decodeResource(res, R.drawable.background_summer_storm);
		gameBackGround[2][0] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_sunny);
		gameBackGround[2][1] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_cloudy);
		gameBackGround[2][2] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_rainy);
		gameBackGround[2][3] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_storm);
		gameBackGround[3][0] = BitmapFactory.decodeResource(res, R.drawable.background_winter_sunny);
		gameBackGround[3][1] = BitmapFactory.decodeResource(res, R.drawable.background_winter_cloudy);
		gameBackGround[3][2] = BitmapFactory.decodeResource(res, R.drawable.background_winter_snowy);
		gameBackGround[3][3] = BitmapFactory.decodeResource(res, R.drawable.background_winter_storm);
		
		soils = new Bitmap[11];
		soils[0] = BitmapFactory.decodeResource(res, R.drawable.nullsoil);
		soils[1] = BitmapFactory.decodeResource(res, R.drawable.emptysoil);
		soils[2] = BitmapFactory.decodeResource(res, R.drawable.emptysoillight);
		soils[3] = BitmapFactory.decodeResource(res, R.drawable.addsoil);
		soils[4] = BitmapFactory.decodeResource(res, R.drawable.addsoillight);
		soils[5] = BitmapFactory.decodeResource(res, R.drawable.droughtsoil);
		soils[6] = BitmapFactory.decodeResource(res, R.drawable.droughtsoillight);
		soils[7] = BitmapFactory.decodeResource(res, R.drawable.floodsoil);
		soils[8] = BitmapFactory.decodeResource(res, R.drawable.floodsoillight);
		soils[9] = BitmapFactory.decodeResource(res, R.drawable.weakcultivatedsoil);
		soils[10] = BitmapFactory.decodeResource(res, R.drawable.weakcultivatedsoillight);
		tools = new Bitmap[20];
		tools[0] = BitmapFactory.decodeResource(res, R.drawable.toolbar);
		tools[1] = BitmapFactory.decodeResource(res, R.drawable.shovel);
		tools[2] = BitmapFactory.decodeResource(res, R.drawable.seeds);
		tools[3] = BitmapFactory.decodeResource(res, R.drawable.hand);
		tools[4] = BitmapFactory.decodeResource(res, R.drawable.money);
		tools[5] = BitmapFactory.decodeResource(res, R.drawable.water_can);
		tools[6] = BitmapFactory.decodeResource(res, R.drawable.fertlise);
		tools[7] = BitmapFactory.decodeResource(res, R.drawable.tank);
		tools[8] = BitmapFactory.decodeResource(res, R.drawable.shovel_grey);
		tools[9] = BitmapFactory.decodeResource(res, R.drawable.seeds_grey);
		tools[10] = BitmapFactory.decodeResource(res, R.drawable.hand_grey);
		tools[11] = BitmapFactory.decodeResource(res, R.drawable.money_grey);
		tools[12] = BitmapFactory.decodeResource(res, R.drawable.water_can_grey);
		tools[13] = BitmapFactory.decodeResource(res, R.drawable.fertlise_grey);
		tools[14] = BitmapFactory.decodeResource(res, R.drawable.tank_grey);
		tools[15] = BitmapFactory.decodeResource(res, R.drawable.water);
		tools[16] = BitmapFactory.decodeResource(res, R.drawable.water_grey);
		tools[17] = BitmapFactory.decodeResource(res, R.drawable.settings);
		tools[18] = BitmapFactory.decodeResource(res, R.drawable.save);
		tools[19] = BitmapFactory.decodeResource(res, R.drawable.exit_button);
		
		crops = new Bitmap[9][7];
		crops[0][0] = BitmapFactory.decodeResource(res, R.drawable.capsicum_0);
		crops[0][1] = BitmapFactory.decodeResource(res, R.drawable.capsicum_1);
		crops[0][2] = BitmapFactory.decodeResource(res, R.drawable.capsicum_2);
		crops[0][3] = BitmapFactory.decodeResource(res, R.drawable.capsicum_3);
		crops[0][4] = BitmapFactory.decodeResource(res, R.drawable.capsicum_4);
		crops[0][5] = BitmapFactory.decodeResource(res, R.drawable.capsicum_5);
		crops[0][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[1][0] = BitmapFactory.decodeResource(res, R.drawable.watermelon_0);
		crops[1][1] = BitmapFactory.decodeResource(res, R.drawable.watermelon_1);
		crops[1][2] = BitmapFactory.decodeResource(res, R.drawable.watermelon_2);
		crops[1][3] = BitmapFactory.decodeResource(res, R.drawable.watermelon_3);
		crops[1][4] = BitmapFactory.decodeResource(res, R.drawable.watermelon_4);
		crops[1][5] = BitmapFactory.decodeResource(res, R.drawable.watermelon_5);
		crops[1][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[2][0] = BitmapFactory.decodeResource(res, R.drawable.bean0);
		crops[2][1] = BitmapFactory.decodeResource(res, R.drawable.bean1);
		crops[2][2] = BitmapFactory.decodeResource(res, R.drawable.bean2);
		crops[2][3] = BitmapFactory.decodeResource(res, R.drawable.bean3);
		crops[2][4] = BitmapFactory.decodeResource(res, R.drawable.bean4);
		crops[2][5] = BitmapFactory.decodeResource(res, R.drawable.bean5);
		crops[2][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[3][0] = BitmapFactory.decodeResource(res, R.drawable.peach_0);
		crops[3][1] = BitmapFactory.decodeResource(res, R.drawable.peach_1);
		crops[3][2] = BitmapFactory.decodeResource(res, R.drawable.peach_2);
		crops[3][3] = BitmapFactory.decodeResource(res, R.drawable.peach_3);
		crops[3][4] = BitmapFactory.decodeResource(res, R.drawable.peach_4);
		crops[3][5] = BitmapFactory.decodeResource(res, R.drawable.peach_5);
		crops[3][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[4][0] = BitmapFactory.decodeResource(res, R.drawable.rice0);
		crops[4][1] = BitmapFactory.decodeResource(res, R.drawable.rice1);
		crops[4][2] = BitmapFactory.decodeResource(res, R.drawable.rice2);
		crops[4][3] = BitmapFactory.decodeResource(res, R.drawable.rice3);
		crops[4][4] = BitmapFactory.decodeResource(res, R.drawable.rice4);
		crops[4][5] = BitmapFactory.decodeResource(res, R.drawable.rice5);
		crops[4][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[5][0] = BitmapFactory.decodeResource(res, R.drawable.wheat0);
		crops[5][1] = BitmapFactory.decodeResource(res, R.drawable.wheat1);
		crops[5][2] = BitmapFactory.decodeResource(res, R.drawable.wheat2);
		crops[5][3] = BitmapFactory.decodeResource(res, R.drawable.wheat3);
		crops[5][4] = BitmapFactory.decodeResource(res, R.drawable.wheat4);
		crops[5][5] = BitmapFactory.decodeResource(res, R.drawable.wheat5);
		crops[5][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[6][0] = BitmapFactory.decodeResource(res, R.drawable.guava_0);
		crops[6][1] = BitmapFactory.decodeResource(res, R.drawable.guava_1);
		crops[6][2] = BitmapFactory.decodeResource(res, R.drawable.guava_2);
		crops[6][3] = BitmapFactory.decodeResource(res, R.drawable.guava_3);
		crops[6][4] = BitmapFactory.decodeResource(res, R.drawable.guava_4);
		crops[6][5] = BitmapFactory.decodeResource(res, R.drawable.guava_5);
		crops[6][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[7][0] = BitmapFactory.decodeResource(res, R.drawable.apple_0);
		crops[7][1] = BitmapFactory.decodeResource(res, R.drawable.apple_1);
		crops[7][2] = BitmapFactory.decodeResource(res, R.drawable.apple_2);
		crops[7][3] = BitmapFactory.decodeResource(res, R.drawable.apple_3);
		crops[7][4] = BitmapFactory.decodeResource(res, R.drawable.apple_4);
		crops[7][5] = BitmapFactory.decodeResource(res, R.drawable.apple_5);
		crops[7][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[8][0] = BitmapFactory.decodeResource(res, R.drawable.rose_0);
		crops[8][1] = BitmapFactory.decodeResource(res, R.drawable.rose_1);
		crops[8][2] = BitmapFactory.decodeResource(res, R.drawable.rose_2);
		crops[8][3] = BitmapFactory.decodeResource(res, R.drawable.rose_3);
		crops[8][4] = BitmapFactory.decodeResource(res, R.drawable.rose_4);
		crops[8][5] = BitmapFactory.decodeResource(res, R.drawable.rose_5);
		crops[8][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		animations = new Bitmap[4][2];
		animations[0][0] = BitmapFactory.decodeResource(res, R.drawable.spade_1);
		animations[0][1] = BitmapFactory.decodeResource(res, R.drawable.spade_2);
		animations[1][0] = BitmapFactory.decodeResource(res, R.drawable.hand_1);
		animations[1][1] = BitmapFactory.decodeResource(res, R.drawable.hand_2);
		animations[2][0] = BitmapFactory.decodeResource(res, R.drawable.water_1);
		animations[2][1] = BitmapFactory.decodeResource(res, R.drawable.water_2);
		animations[3][0] = BitmapFactory.decodeResource(res, R.drawable.fertlize_1);
		animations[3][1] = BitmapFactory.decodeResource(res, R.drawable.fertlize_2);
		
		textPicture = new Bitmap[18];
		textPicture[0] = BitmapFactory.decodeResource(res, R.drawable.remind1);
		textPicture[1] = BitmapFactory.decodeResource(res, R.drawable.remind2);
		textPicture[2] = BitmapFactory.decodeResource(res, R.drawable.remind3);
		textPicture[3] = BitmapFactory.decodeResource(res, R.drawable.remind4);
		textPicture[4] = BitmapFactory.decodeResource(res, R.drawable.remind5);
		textPicture[5] = BitmapFactory.decodeResource(res, R.drawable.remind6);
		textPicture[6] = BitmapFactory.decodeResource(res, R.drawable.remind7);
		textPicture[7] = BitmapFactory.decodeResource(res, R.drawable.remind8);
		textPicture[8] = BitmapFactory.decodeResource(res, R.drawable.remind9);
		textPicture[9] = BitmapFactory.decodeResource(res, R.drawable.remind10);
		textPicture[10] = BitmapFactory.decodeResource(res, R.drawable.remind11);
		textPicture[11] = BitmapFactory.decodeResource(res, R.drawable.remind12);
		textPicture[12] = BitmapFactory.decodeResource(res, R.drawable.remind13);
		textPicture[13] = BitmapFactory.decodeResource(res, R.drawable.remind14);
		textPicture[14] = BitmapFactory.decodeResource(res, R.drawable.remind15);
		textPicture[15] = BitmapFactory.decodeResource(res, R.drawable.remind16);
		textPicture[16] = BitmapFactory.decodeResource(res, R.drawable.remind17);
		textPicture[17] = BitmapFactory.decodeResource(res, R.drawable.screeninfo);
		
		coin = BitmapFactory.decodeResource(res, R.drawable.coin);
		calenderPlace = BitmapFactory.decodeResource(res, R.drawable.calenderplace);
		arrows = new Bitmap[3];
		arrows[0] = BitmapFactory.decodeResource(res, R.drawable.arrow1);
		arrows[1] = BitmapFactory.decodeResource(res, R.drawable.arrow2);
		arrows[2] = BitmapFactory.decodeResource(res, R.drawable.arrow3);
		farmGameIntro = new FarmGameIntro(gameBackGround,soils,tools,crops,animations,textPicture,arrows,coin,calenderPlace,font);
	}
	
	public void initiateNewGame(){	
		saveScreen = new Bitmap[4];
		saveScreen[0] = BitmapFactory.decodeResource(res, R.drawable.savingprogress);
		saveScreen[1] = BitmapFactory.decodeResource(res, R.drawable.savingprogress1);
		saveScreen[2] = BitmapFactory.decodeResource(res, R.drawable.savingprogress2);
		saveScreen[3] = BitmapFactory.decodeResource(res, R.drawable.savingprogress3);
		
		savePicture = new Bitmap[4];
		savePicture[0] = BitmapFactory.decodeResource(res, R.drawable.savesuccess);
		savePicture[1] = BitmapFactory.decodeResource(res, R.drawable.savefail);
		savePicture[2] = BitmapFactory.decodeResource(res, R.drawable.gamestarted);	
		savePicture[3] = BitmapFactory.decodeResource(res, R.drawable.selectmore);
		farmGamming = new FarmGamming(gameBackGround,soils,tools,crops,animations,savePicture,coin,calenderPlace,font);	
	}
	
	public void initiateLoadGame(){
		gameBackGround = new Bitmap[4][4];
		gameBackGround[0][0] = BitmapFactory.decodeResource(res, R.drawable.background_spring_sunny);
		gameBackGround[0][1] = BitmapFactory.decodeResource(res, R.drawable.background_spring_cloudy);
		gameBackGround[0][2] = BitmapFactory.decodeResource(res, R.drawable.background_spring_rainy);
		gameBackGround[0][3] = BitmapFactory.decodeResource(res, R.drawable.background_spring_storm);
		gameBackGround[1][0] = BitmapFactory.decodeResource(res, R.drawable.background_summer_sunny);
		gameBackGround[1][1] = BitmapFactory.decodeResource(res, R.drawable.background_summer_cloudy);
		gameBackGround[1][2] = BitmapFactory.decodeResource(res, R.drawable.background_summer_rainy);
		gameBackGround[1][3] = BitmapFactory.decodeResource(res, R.drawable.background_summer_storm);
		gameBackGround[2][0] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_sunny);
		gameBackGround[2][1] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_cloudy);
		gameBackGround[2][2] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_rainy);
		gameBackGround[2][3] = BitmapFactory.decodeResource(res, R.drawable.background_autumn_storm);
		gameBackGround[3][0] = BitmapFactory.decodeResource(res, R.drawable.background_winter_sunny);
		gameBackGround[3][1] = BitmapFactory.decodeResource(res, R.drawable.background_winter_cloudy);
		gameBackGround[3][2] = BitmapFactory.decodeResource(res, R.drawable.background_winter_snowy);
		gameBackGround[3][3] = BitmapFactory.decodeResource(res, R.drawable.background_winter_storm);
		soils = new Bitmap[11];
		soils[0] = BitmapFactory.decodeResource(res, R.drawable.nullsoil);
		soils[1] = BitmapFactory.decodeResource(res, R.drawable.emptysoil);
		soils[2] = BitmapFactory.decodeResource(res, R.drawable.emptysoillight);
		soils[3] = BitmapFactory.decodeResource(res, R.drawable.addsoil);
		soils[4] = BitmapFactory.decodeResource(res, R.drawable.addsoillight);
		soils[5] = BitmapFactory.decodeResource(res, R.drawable.droughtsoil);
		soils[6] = BitmapFactory.decodeResource(res, R.drawable.droughtsoillight);
		soils[7] = BitmapFactory.decodeResource(res, R.drawable.floodsoil);
		soils[8] = BitmapFactory.decodeResource(res, R.drawable.floodsoillight);
		soils[9] = BitmapFactory.decodeResource(res, R.drawable.weakcultivatedsoil);
		soils[10] = BitmapFactory.decodeResource(res, R.drawable.weakcultivatedsoillight);
		tools = new Bitmap[20];
		tools[0] = BitmapFactory.decodeResource(res, R.drawable.toolbar);
		tools[1] = BitmapFactory.decodeResource(res, R.drawable.shovel);
		tools[2] = BitmapFactory.decodeResource(res, R.drawable.seeds);
		tools[3] = BitmapFactory.decodeResource(res, R.drawable.hand);
		tools[4] = BitmapFactory.decodeResource(res, R.drawable.money);
		tools[5] = BitmapFactory.decodeResource(res, R.drawable.water_can);
		tools[6] = BitmapFactory.decodeResource(res, R.drawable.fertlise);
		tools[7] = BitmapFactory.decodeResource(res, R.drawable.tank);
		tools[8] = BitmapFactory.decodeResource(res, R.drawable.shovel_grey);
		tools[9] = BitmapFactory.decodeResource(res, R.drawable.seeds_grey);
		tools[10] = BitmapFactory.decodeResource(res, R.drawable.hand_grey);
		tools[11] = BitmapFactory.decodeResource(res, R.drawable.money_grey);
		tools[12] = BitmapFactory.decodeResource(res, R.drawable.water_can_grey);
		tools[13] = BitmapFactory.decodeResource(res, R.drawable.fertlise_grey);
		tools[14] = BitmapFactory.decodeResource(res, R.drawable.tank_grey);
		tools[15] = BitmapFactory.decodeResource(res, R.drawable.water);
		tools[16] = BitmapFactory.decodeResource(res, R.drawable.water_grey);
		tools[17] = BitmapFactory.decodeResource(res, R.drawable.settings);
		tools[18] = BitmapFactory.decodeResource(res, R.drawable.save);
		tools[19] = BitmapFactory.decodeResource(res, R.drawable.exit_button);
		crops = new Bitmap[9][7];
		crops[0][0] = BitmapFactory.decodeResource(res, R.drawable.capsicum_0);
		crops[0][1] = BitmapFactory.decodeResource(res, R.drawable.capsicum_1);
		crops[0][2] = BitmapFactory.decodeResource(res, R.drawable.capsicum_2);
		crops[0][3] = BitmapFactory.decodeResource(res, R.drawable.capsicum_3);
		crops[0][4] = BitmapFactory.decodeResource(res, R.drawable.capsicum_4);
		crops[0][5] = BitmapFactory.decodeResource(res, R.drawable.capsicum_5);
		crops[0][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[1][0] = BitmapFactory.decodeResource(res, R.drawable.watermelon_0);
		crops[1][1] = BitmapFactory.decodeResource(res, R.drawable.watermelon_1);
		crops[1][2] = BitmapFactory.decodeResource(res, R.drawable.watermelon_2);
		crops[1][3] = BitmapFactory.decodeResource(res, R.drawable.watermelon_3);
		crops[1][4] = BitmapFactory.decodeResource(res, R.drawable.watermelon_4);
		crops[1][5] = BitmapFactory.decodeResource(res, R.drawable.watermelon_5);
		crops[1][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[2][0] = BitmapFactory.decodeResource(res, R.drawable.bean0);
		crops[2][1] = BitmapFactory.decodeResource(res, R.drawable.bean1);
		crops[2][2] = BitmapFactory.decodeResource(res, R.drawable.bean2);
		crops[2][3] = BitmapFactory.decodeResource(res, R.drawable.bean3);
		crops[2][4] = BitmapFactory.decodeResource(res, R.drawable.bean4);
		crops[2][5] = BitmapFactory.decodeResource(res, R.drawable.bean5);
		crops[2][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[3][0] = BitmapFactory.decodeResource(res, R.drawable.peach_0);
		crops[3][1] = BitmapFactory.decodeResource(res, R.drawable.peach_1);
		crops[3][2] = BitmapFactory.decodeResource(res, R.drawable.peach_2);
		crops[3][3] = BitmapFactory.decodeResource(res, R.drawable.peach_3);
		crops[3][4] = BitmapFactory.decodeResource(res, R.drawable.peach_4);
		crops[3][5] = BitmapFactory.decodeResource(res, R.drawable.peach_5);
		crops[3][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[4][0] = BitmapFactory.decodeResource(res, R.drawable.rice0);
		crops[4][1] = BitmapFactory.decodeResource(res, R.drawable.rice1);
		crops[4][2] = BitmapFactory.decodeResource(res, R.drawable.rice2);
		crops[4][3] = BitmapFactory.decodeResource(res, R.drawable.rice3);
		crops[4][4] = BitmapFactory.decodeResource(res, R.drawable.rice4);
		crops[4][5] = BitmapFactory.decodeResource(res, R.drawable.rice5);
		crops[4][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[5][0] = BitmapFactory.decodeResource(res, R.drawable.wheat0);
		crops[5][1] = BitmapFactory.decodeResource(res, R.drawable.wheat1);
		crops[5][2] = BitmapFactory.decodeResource(res, R.drawable.wheat2);
		crops[5][3] = BitmapFactory.decodeResource(res, R.drawable.wheat3);
		crops[5][4] = BitmapFactory.decodeResource(res, R.drawable.wheat4);
		crops[5][5] = BitmapFactory.decodeResource(res, R.drawable.wheat5);
		crops[5][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[6][0] = BitmapFactory.decodeResource(res, R.drawable.guava_0);
		crops[6][1] = BitmapFactory.decodeResource(res, R.drawable.guava_1);
		crops[6][2] = BitmapFactory.decodeResource(res, R.drawable.guava_2);
		crops[6][3] = BitmapFactory.decodeResource(res, R.drawable.guava_3);
		crops[6][4] = BitmapFactory.decodeResource(res, R.drawable.guava_4);
		crops[6][5] = BitmapFactory.decodeResource(res, R.drawable.guava_5);
		crops[6][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[7][0] = BitmapFactory.decodeResource(res, R.drawable.apple_0);
		crops[7][1] = BitmapFactory.decodeResource(res, R.drawable.apple_1);
		crops[7][2] = BitmapFactory.decodeResource(res, R.drawable.apple_2);
		crops[7][3] = BitmapFactory.decodeResource(res, R.drawable.apple_3);
		crops[7][4] = BitmapFactory.decodeResource(res, R.drawable.apple_4);
		crops[7][5] = BitmapFactory.decodeResource(res, R.drawable.apple_5);
		crops[7][6] = BitmapFactory.decodeResource(res, R.drawable.sere);
		
		crops[8][0] = BitmapFactory.decodeResource(res, R.drawable.rose_0);
		crops[8][1] = BitmapFactory.decodeResource(res, R.drawable.rose_1);
		crops[8][2] = BitmapFactory.decodeResource(res, R.drawable.rose_2);
		crops[8][3] = BitmapFactory.decodeResource(res, R.drawable.rose_3);
		crops[8][4] = BitmapFactory.decodeResource(res, R.drawable.rose_4);
		crops[8][5] = BitmapFactory.decodeResource(res, R.drawable.rose_5);
		crops[8][6] = BitmapFactory.decodeResource(res, R.drawable.sere);	
		
		coin = BitmapFactory.decodeResource(res, R.drawable.coin);
		calenderPlace = BitmapFactory.decodeResource(res, R.drawable.calenderplace);
		
		animations = new Bitmap[4][2];
		animations[0][0] = BitmapFactory.decodeResource(res, R.drawable.spade_1);
		animations[0][1] = BitmapFactory.decodeResource(res, R.drawable.spade_2);
		animations[1][0] = BitmapFactory.decodeResource(res, R.drawable.hand_1);
		animations[1][1] = BitmapFactory.decodeResource(res, R.drawable.hand_2);
		animations[2][0] = BitmapFactory.decodeResource(res, R.drawable.water_1);
		animations[2][1] = BitmapFactory.decodeResource(res, R.drawable.water_2);
		animations[3][0] = BitmapFactory.decodeResource(res, R.drawable.fertlize_1);
		animations[3][1] = BitmapFactory.decodeResource(res, R.drawable.fertlize_2);
		saveScreen = new Bitmap[4];
		saveScreen[0] = BitmapFactory.decodeResource(res, R.drawable.savingprogress);
		saveScreen[1] = BitmapFactory.decodeResource(res, R.drawable.savingprogress1);
		saveScreen[2] = BitmapFactory.decodeResource(res, R.drawable.savingprogress2);
		saveScreen[3] = BitmapFactory.decodeResource(res, R.drawable.savingprogress3);
		GameInfo.isLoad = true;
		savePicture = new Bitmap[4];
		savePicture[0] = BitmapFactory.decodeResource(res, R.drawable.savesuccess);
		savePicture[1] = BitmapFactory.decodeResource(res, R.drawable.savefail);
		savePicture[2] = BitmapFactory.decodeResource(res, R.drawable.gamestarted);	
		savePicture[3] = BitmapFactory.decodeResource(res, R.drawable.selectmore);
		farmGamming = new FarmGamming(gameBackGround,soils,tools,crops,animations,savePicture,coin,calenderPlace,font);	
	}
}
