/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : GameStartMenu.java
* description : This class is used for processing all the information when the user in the start menu
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

//This class is used for the game start model
public class GameStartMenu {
	private Bitmap backGround;
	private Bitmap rankBackGround;
	
	public static boolean isNameButtonPress;
	public static boolean isAgeButtonPress;
	public static boolean isGenderButtonPress; 
	
	public static int gameState;
	public static final int GAMEMENU = 0;
	public static final int RANKING = 1;
	
	private User[] users;
	private int newaccountX,newaccountY; //the positionX and PositionY of image new account
	private int settingX,settingY;//the positionX and PositionY of image setting
	private int loadaccountX,loadaccountY;//the positionX and PositionY of image load account
	private int startX,startY;//the positionX and PositionY of image start
	private int exitX,exitY;//the positionX and PositionY of image exit
	private int rankX,rankY;  
	
	private FileInputStream fis;
	private DataInputStream dis;
	private int userNumber;
	
	public GameStartMenu(Bitmap backGround, Bitmap rankBackGround){
		this.backGround = backGround;
		this.rankBackGround = rankBackGround;
		newaccountX  = PhoneInfo.getRealWidth(550);//new account
		newaccountY  = PhoneInfo.getRealHeight(170);//new account
		loadaccountX = PhoneInfo.getRealWidth(550);//load account
		loadaccountY = PhoneInfo.getRealHeight(250);//load account
		settingX = PhoneInfo.getRealWidth(550);//setting
		settingY = PhoneInfo.getRealHeight(310);//setting
		startX = PhoneInfo.getRealWidth(550);//start
		startY = PhoneInfo.getRealHeight(370);//start
		exitX = PhoneInfo.getRealWidth(730);//exit
		exitY = PhoneInfo.getRealHeight(420);//exit
		rankX = PhoneInfo.getRealWidth(550);
		rankY = PhoneInfo.getRealHeight(430);
		gameState = GAMEMENU;
	}
	
	//draw the start menu
	public void draw(Canvas canvas, Paint paint){
		if(gameState == GAMEMENU){
			canvas.drawBitmap(backGround, null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
		}
		else if(gameState == RANKING){
			canvas.drawBitmap(rankBackGround, null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
			//draw the userinfo
			float textSize = (float) (24 * PhoneInfo.heightRatio);
			String familyName = "Arial";
			Typeface font = Typeface.create(familyName,Typeface.NORMAL);
			paint.setTypeface(font);
			paint.setTextSize(textSize);
			paint.setColor(Color.BLACK);
			for(int i = 0;i!=userNumber;++i){
				canvas.drawText(users[i].getScore()+"", PhoneInfo.getRealWidth(15), PhoneInfo.getRealHeight(200+60*i), paint);
				String name = users[i].getName();
				canvas.drawText(name, PhoneInfo.getRealWidth(110), PhoneInfo.getRealHeight(200+60*i), paint);
				canvas.drawText(users[i].getAge()+"", PhoneInfo.getRealWidth(210), PhoneInfo.getRealHeight(200+60*i), paint);
				canvas.drawText(users[i].getBalance()+"", PhoneInfo.getRealWidth(300), PhoneInfo.getRealHeight(200+60*i), paint);
				int time = (int) (users[i].getTime() * 10) / 10;
				canvas.drawText(time+" min", PhoneInfo.getRealWidth(390), PhoneInfo.getRealHeight(200+60*i), paint);
				canvas.drawText(users[i].getTotalEarning()+"", PhoneInfo.getRealWidth(500), PhoneInfo.getRealHeight(200+60*i), paint);
				canvas.drawText(users[i].getBorrowed()+"", PhoneInfo.getRealWidth(670), PhoneInfo.getRealHeight(200+60*i), paint);	
			}		
		}
	}
	
	public void onTouchEvent(MotionEvent event) {
		//get the point where user touch
		int pointX = (int)event.getX();
		int pointY = (int)event.getY();
		if(gameState == GAMEMENU){
			if(pointX > newaccountX && pointX < PhoneInfo.getRealWidth(720) && pointY > newaccountY && pointY < PhoneInfo.getRealHeight(220)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
					//process the vibrate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	    		
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
					//go the the next action
	    			if(GameInfo.isCreateSurfaceView == false){
	    				GameInfo.isCreateSurfaceView = true;
						Intent intent = new Intent(GameEngineActivity.instance, CreateAccountActivity.class);
						GameEngineActivity.instance.startActivity(intent);
	    			}
				}
			}
			
			//if press the load account button means load a new game
			else if(pointX > loadaccountX && pointX < PhoneInfo.getRealWidth(720) && pointY > loadaccountY && pointY < PhoneInfo.getRealHeight(290)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
					//process the vibrate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
	    			//go the the next action
	    			if(GameInfo.isCreateSurfaceView == false){
	    				GameInfo.isCreateSurfaceView = true;
	    				//judge whether the account exist
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
	    						int i = 0;
	    						for(String temp: nameSequence){
	    							++i;
	    						}
	    						if(i > 0){
									Intent intent = new Intent(GameEngineActivity.instance, InputNameActivity.class);
									GameEngineActivity.instance.startActivity(intent);
	    						}
	    						else{
	    							Intent intent = new Intent(GameEngineActivity.instance, AccountNotExistActivity.class);
									GameEngineActivity.instance.startActivity(intent);
	    						}
	    					}
	    					else{
	    						Intent intent = new Intent(GameEngineActivity.instance, AccountNotExistActivity.class);
								GameEngineActivity.instance.startActivity(intent);
	    					}
	    				}
	    				else{
	    					File path = new File("/");
	    					if(path.exists()){
	    						final String nameSequence[];
	    						nameSequence = path.list();
	    						int i = 0;
	    						for(String temp: nameSequence){
	    							++i;
	    						}
	    						if(i > 0){
									Intent intent = new Intent(GameEngineActivity.instance, InputNameActivity.class);
									GameEngineActivity.instance.startActivity(intent);
	    						}
	    						else{
	    							Intent intent = new Intent(GameEngineActivity.instance, AccountNotExistActivity.class);
									GameEngineActivity.instance.startActivity(intent);
	    						}
	    					}
	    					else{
	    						Intent intent = new Intent(GameEngineActivity.instance, AccountNotExistActivity.class);
								GameEngineActivity.instance.startActivity(intent);
	    					}
	    				}
	    			}
				}
			}
					
			//if press the setting button 
			else if(pointX > settingX && pointX < PhoneInfo.getRealWidth(720) && pointY > settingY && pointY < PhoneInfo.getRealHeight(350)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			//process the vibrate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
	    			//go the the next action
	    			if(GameInfo.isCreateSurfaceView == false){
	    				GameInfo.isCreateSurfaceView = true;
						Intent intent = new Intent(GameEngineActivity.instance, SettingActivity.class);
						GameEngineActivity.instance.startActivity(intent);
	    			}
				}	
			}
			
			//if press the start button means start the game
			else if(pointX > startX && pointX < PhoneInfo.getRealWidth(720) && pointY > startY && pointY < PhoneInfo.getRealHeight(405)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
					//process the vibrate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
	    			if(GameInfo.isStartAllowed == true){
						GameSurfaceView.gameState = GameSurfaceView.START_GAME;
						GameSurfaceView.myView.isStart = true;
	    			}
	    			else{
	    				//go the the next action
	    				Intent intent = new Intent(GameEngineActivity.instance, CreateAccountWarningActivity.class);
	    				GameEngineActivity.instance.startActivity(intent);
	    			}
				}
			}
	
			//if press the rank button 
			else if(pointX > rankX && pointX < PhoneInfo.getRealWidth(720) && pointY > rankY && pointY < PhoneInfo.getRealHeight(465)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	//				//process the vibrate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
	    			try{
						File file = new File("/sdcard/FarmSim/leaderboard/"+"leaderboard.txt");
						if(!file.exists()){
							userNumber = 0;
						}
						else{
							fis = new FileInputStream(file);
							dis = new DataInputStream(fis);
							userNumber = dis.readInt();
							users = new User[userNumber];
							for(int i = 0;i!=userNumber;++i){
								users[i] = new User();
								users[i].setName(dis.readUTF());
								users[i].setAge(dis.readInt());
								users[i].setBalance(dis.readInt());
								users[i].setTime(dis.readDouble());
								users[i].setTotalEarning(dis.readInt());
								users[i].setBorrowed(dis.readInt());
								users[i].setScore(dis.readInt());
							}
						}
					}
					catch(FileNotFoundException e) {
						userNumber = 0;
					}
					catch(IOException e){
						userNumber = 0;
					}
					finally{
						try{
							if(fis != null)
								fis.close();
							if(dis != null)
								dis.close();
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
					gameState = RANKING;
				}
			}
			
			//if press the exit button 	
			else if(pointX > exitX && pointX < PhoneInfo.getRealWidth(800) && pointY > exitY && pointY < PhoneInfo.getRealHeight(480)){
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	//				//process the viberate and sound effect
					GameInfo.soundEffect[0].play((float) 0.3);
					GameInfo.vibrate.playVibrate(-1);
				}
	
	    		else if(event.getAction() == MotionEvent.ACTION_UP){
					System.exit(0);
				}
			}
		}
		
		else if(gameState == RANKING){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				//process the vibrate and sound effect
				//GameInfo.soundEffect[0].play((float) 0.3);
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){	
				gameState = GAMEMENU;
			}
		}
	}
}
