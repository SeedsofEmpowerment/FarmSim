/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : SoilInfoSurfaceView.java
* description : The class for SoilInfoSurfaceView
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
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;


public class SoilInfoSurfaceView  extends SurfaceView implements Callback, Runnable{
	public static SoilInfoSurfaceView myView;
	public static int screenW, screenH;
	private Canvas canvas;
	private SurfaceHolder sfh;
	private Paint paint;
	private Bitmap backGround;
	private Typeface font;
	
	private Resources res = this.getResources();
	
	public SoilInfoSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		font = Typeface.createFromAsset(context.getAssets(), "regv2.ttf");
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
		recycleResource(backGround);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			GameInfo.vibrate.playVibrate(-1);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			Intent intent = new Intent();
			intent.setClass(SoilInfoActivity.instance, GameEngineActivity.class);
			SoilInfoActivity.instance.finish();
			GameInfo.isSoilInfoSelected = false;
		}
		myDraw();
		return true;
	}
	public void myDraw(){
		try{
			canvas = sfh.lockCanvas();
			if(canvas != null){
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(backGround,null,new Rect(0,0,screenW,screenH),paint);
				//for text
				paint.setTypeface(font);
				float textSize = (float) (32 * PhoneInfo.heightRatio);
				paint.setTextSize(textSize);
				paint.setColor(Color.BLACK);
				Paint paint2 = new Paint();
				//for number
				String familyName2 = "Arial";
				Typeface font2 = Typeface.create(familyName2,Typeface.NORMAL);
				paint2.setTypeface(font2);
				paint2.setTextSize(textSize);
				paint2.setColor(Color.BLACK);
				Intent intent = SoilInfoActivity.instance.getIntent();
				int humidity = intent.getIntExtra("humidity", 0);
				int nutrition = intent.getIntExtra("nutrition", 0);
				String cropName = intent.getStringExtra("cropName");
				int state = intent.getIntExtra("state", 0);
				int cost = intent.getIntExtra("cost", 0);
				int sale = intent.getIntExtra("sale", 0);
				int predictSale = intent.getIntExtra("predictSale", 0);
				canvas.drawText("Humidity: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(45), paint);
				if(humidity<20||humidity>80){
					paint2.setColor(Color.RED);
				}
				else{
					paint2.setColor(Color.BLACK);
				}
				canvas.drawText(Integer.toString(humidity)+"/100", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(45), paint2);
				
				canvas.drawText("Nutrition: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(90), paint);
				if(nutrition<60){
					paint2.setColor(Color.RED);
				}
				else{
					paint2.setColor(Color.BLACK);
				}
				canvas.drawText(Integer.toString(nutrition)+"/100", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(90), paint2);
				
				if(!cropName.equals("null")){
					canvas.drawText("Crop name: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(135), paint);		
					paint2.setColor(Color.BLACK);
					canvas.drawText(cropName, PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(135), paint2);
					
					canvas.drawText("Crop State: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(180), paint);
					if(state == 0){
						canvas.drawText("Seed stage", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 1){
						canvas.drawText("Germination period", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 2){
						canvas.drawText("Small leaf", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 3){
						canvas.drawText("Big leaf", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 4){
						canvas.drawText("Flowering stage", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 5){
						canvas.drawText("Fruiting period", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					else if(state == 6){
						canvas.drawText("Withering period", PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(180), paint2);
					}
					canvas.drawText("Cost: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(225), paint);
					canvas.drawText(Integer.toString(cost), PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(225), paint2);
					
					canvas.drawText("Sale: ", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(270), paint);
					if(predictSale < sale){
						paint2.setColor(Color.RED);
					}
					else{
						paint2.setColor(Color.BLACK);
					}
					canvas.drawText(Integer.toString(predictSale)+"/"+Integer.toString(sale), PhoneInfo.getRealWidth(250), PhoneInfo.getRealHeight(270), paint2);
					if(nutrition < 60){
						canvas.drawText("Tips: You should fertilize this soil", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(315), paint2);
					}
					else if(humidity < 20){
						canvas.drawText("Tips: You should watering this soil", PhoneInfo.getRealWidth(30), PhoneInfo.getRealHeight(315), paint2);
					}
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
	
	public void recycleResource(Bitmap picture){
		if(picture != null){
			picture.recycle();
		}
	}
	
	public void initGame(){
		backGround = BitmapFactory.decodeResource(res, R.drawable.soilattribute);
	}
}
