/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : SelectPlantSurfaceView.java
* description : The class for Selecting the plant
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class SelectPlantSurfaceView extends SurfaceView implements Callback, Runnable{
	public static SelectPlantSurfaceView myView;
	public static int screenW, screenH;
	private Canvas canvas;
	private SurfaceHolder sfh;
	private Paint paint;
	
	private Resources res = this.getResources();
	private Bitmap[] plantInfo;
	
	private int state;
	
	public SelectPlantSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		screenW = this.getWidth();
		screenH = this.getHeight();
		myView = this;
		initGame();      //use to initialize this game
		myDraw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void recycleResource(Bitmap picture){
		if(picture != null){
			picture.recycle();
		}
	}
	
	public void releaseResource(){
		for(int i = 0;i!=9;++i){
			recycleResource(plantInfo[i]);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//get the point where user touch
		int pointX = (int)event.getX();
		int pointY = (int)event.getY();
		//for normal screen
		int arrowLength = PhoneInfo.getRealWidth(80);
		int arrowHeight = PhoneInfo.getRealHeight(80);
		int pictureLength = PhoneInfo.getRealWidth(300);
		int pictureHeight = PhoneInfo.getRealHeight(300);
		int arrowLeftX = PhoneInfo.getRealWidth(10);
		int arrowLeftY = PhoneInfo.getRealHeight(160);	
		int arrowRightX = PhoneInfo.getRealWidth(410);
		int arrowRightY = PhoneInfo.getRealHeight(160);
		int pictureX = PhoneInfo.getRealWidth(100);
		int pictureY = PhoneInfo.getRealHeight(50);
		
		//for SamSung s3 screen 1280*720
		if(PhoneInfo.resolutionWidth == 1280 && PhoneInfo.resolutionHeight == 720 && PhoneInfo.densityDpi == 320){
			arrowLength = 100;
			arrowHeight = 100;
			pictureLength = 377;
			pictureHeight = 377;
			arrowLeftX = 12;
			arrowLeftY = 201;
			arrowRightX = 517;
			arrowRightY = 201;
			pictureX = 126;
			pictureY = 63;
		}
		
		//for Motorola ME525 screen 858*480
		else if(PhoneInfo.resolutionWidth == 854 && PhoneInfo.resolutionHeight == 480 && PhoneInfo.densityDpi == 240){
			arrowLength = PhoneInfo.getRealWidth(92);
			arrowHeight = PhoneInfo.getRealHeight(80);
			pictureLength = PhoneInfo.getRealWidth(320);
			pictureHeight = PhoneInfo.getRealHeight(300);
			arrowLeftX = PhoneInfo.getRealWidth(11);
			arrowLeftY = PhoneInfo.getRealHeight(160);	
			arrowRightX = PhoneInfo.getRealWidth(440);
			arrowRightY = PhoneInfo.getRealHeight(160);
			pictureX = PhoneInfo.getRealWidth(107);
			pictureY = PhoneInfo.getRealHeight(50);
		}
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			GameInfo.vibrate.playVibrate(-1);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			if(pointX > arrowLeftX && pointX < arrowLeftX + arrowLength){
				if(pointY > arrowLeftY && pointY < arrowLeftY + arrowHeight){
					--state;
					if(state < 0){
						if(GameInfo.plantMore == false){
							state = 5;
						}
						else{
							state = 8;
						}
					}
				}
				else{
					SelectPlantActivity.instance.finish();
				}
			}
			else if(pointX > arrowRightX && pointX < arrowRightX + arrowLength){
				if(pointY > arrowRightY && pointY < arrowRightY + arrowHeight){
					++state;
					if(GameInfo.plantMore == false){
						if(state > 5){
							state = 0;
						}
					}
					else{
						if(state > 8){
							state = 0;
						}
					}
				}
				else{
					SelectPlantActivity.instance.finish();
				}
			}
			else if(pointX > pictureX && pointX < pictureX + pictureLength){
				if(pointY > pictureY && pointY < pictureY + pictureHeight){
					GameInfo.selectCropNumber = state;
					Intent intent = new Intent(SelectPlantActivity.instance, EnsurePlantActivity.class);
					SelectPlantActivity.instance.startActivity(intent);
				}
			}		
			myDraw();
		}
		return true;
	}
	
	public void myDraw(){
		try{
			canvas = sfh.lockCanvas();
			if(canvas != null){
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(plantInfo[state], null, new Rect(0,0,screenW,screenH), paint);
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
		plantInfo = new Bitmap[9];
		plantInfo[0] = BitmapFactory.decodeResource(res, R.drawable.capsicumplant);
		plantInfo[1] = BitmapFactory.decodeResource(res, R.drawable.watermelonplant);
		plantInfo[2] = BitmapFactory.decodeResource(res, R.drawable.beansplant);
		plantInfo[3] = BitmapFactory.decodeResource(res, R.drawable.peachplant);
		plantInfo[4] = BitmapFactory.decodeResource(res, R.drawable.riceplant);
		plantInfo[5] = BitmapFactory.decodeResource(res, R.drawable.wheatplant);
		plantInfo[6] = BitmapFactory.decodeResource(res, R.drawable.guavaplant);
		plantInfo[7] = BitmapFactory.decodeResource(res, R.drawable.appleplant);
		plantInfo[8] = BitmapFactory.decodeResource(res, R.drawable.roseplant);
		state = 0;
	}
}
