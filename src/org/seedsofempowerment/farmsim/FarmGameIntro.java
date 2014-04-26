/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : FarmGameIntro.java
* description : This activity is for the instruction of the game
* 
* created by Wang Shiliang at 5/10/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

//This class is the game introduction model
public class FarmGameIntro {
	private Bitmap[][] backGround;
	private Bitmap[] textPicture;
	private Bitmap[] arrows;
	private int listBoxX;
	private int listBoxY;
	private int listBoxWidth;
	private int listBoxHeight;
	private int toolsWidth;
	private int toolsHeight;
	private int textX;
	private int textY;
	private int monthX;
	private int monthY;
	private int animationX;
	private int animationY;
	private int saveX;
	private int saveY;
	private int exitX;
	private int exitY;
	private int settingX;
	private int settingY;
	private int[] textPictureX;
	private int[] textPictureY;
	private int[] arrowX;
	private int[] arrowY;
	private Bitmap[] soilPicture;
	private Soil[] soils;
	private Bitmap[] tools;
	private Crop[] crops;
	private Bitmap[][] cropPicture;
	private Bitmap[][] animations;
	private Bitmap coin;
	private Bitmap calenderPlace;
	private boolean spadeAnimation;
	private boolean handAnimation;
	private boolean waterAnimation;
	private boolean fertlizeAnimation;
	private boolean arrowAnimation;
	private int animationNumber;
	private int arrowNumber;
	private boolean cropSelected;
	private Typeface font;
	
	private static final int startX = PhoneInfo.getRealWidth(45);
	private static final int startY = PhoneInfo.getRealHeight(240);
	private static final int soilWidth = PhoneInfo.getRealWidth(160);
	private static final int soilHeight = PhoneInfo.getRealHeight(68);
	private static final int initialNumber = 6;
	public static final int SPRING = 0;
	public static final int SUMMER = 1;
	public static final int AUTUMN = 2;
	public static final int WINTER = 3;
	
	private int gameState;
	
	public static CropInfo[] CropInfo;
	
	//initialize the menu
	public FarmGameIntro(Bitmap[][] backGround, Bitmap[] soilPicture, Bitmap[] tools, Bitmap[][] cropPicture, Bitmap[][] animations, Bitmap[] textPicture, Bitmap[] arrows,  Bitmap coin, Bitmap calenderPlace,Typeface font){
		this.backGround = backGround;
		this.soilPicture = soilPicture;
		this.arrows = arrows;
		this.tools = tools;
		this.cropPicture = cropPicture;
		this.animations = animations;
		this.textPicture = textPicture;
		this.coin = coin;
		this.calenderPlace = calenderPlace;
		this.font = font;
		//initialize the soils
		soils = new Soil[18];
		for(int i = 0;i!=18;++i){
			soils[i] = new Soil(soilPicture);
		}
		GameInfo.waterTank = new WaterTank();
		listBoxX = 0;
		listBoxY = PhoneInfo.getRealHeight(415);
		listBoxWidth = PhoneInfo.getFigureWidth(tools[0].getWidth());
		listBoxHeight = PhoneInfo.getFigureHeight(tools[0].getHeight());
		toolsWidth = PhoneInfo.getFigureWidth(tools[1].getWidth());
		toolsHeight = PhoneInfo.getFigureHeight(tools[1].getHeight());
		monthY = PhoneInfo.getRealHeight(10);
		saveX = PhoneInfo.resolutionWidth - PhoneInfo.getRealWidth(3 * 50);
		saveY = PhoneInfo.resolutionHeight -  PhoneInfo.getRealHeight(50);
		exitX = saveX + PhoneInfo.getRealWidth(50);
		exitY = saveY;
		settingX = exitX + PhoneInfo.getRealWidth(50);
		settingY = saveY;
		GameInfo.myLoan = new Loan[3];
		for(int i = 0;i!=3;++i){
			GameInfo.myLoan[i] = new Loan();
		}
		cropSelected = false;
		textPictureX = new int[18];
		textPictureY = new int[18];
		for(int i = 0;i!=17;++i){
			textPictureX[i] = PhoneInfo.getRealWidth(510);
			textPictureY[i] = PhoneInfo.getRealHeight(70);
		}
		textPictureX[17] = (PhoneInfo.resolutionWidth - PhoneInfo.getFigureWidth(textPicture[17].getWidth())) / 2;
		textPictureY[17] = (PhoneInfo.resolutionHeight - PhoneInfo.getFigureHeight(textPicture[17].getHeight()) ) / 2;		
		int currentX = startX;
		int currentY = startY;
		for(int i = 0; i < 6; ++i){
			currentX = startX;
			currentY = startY;
			currentX += i * soilWidth / 2;
			currentY += i * soilHeight / 2;
			for(int j = 0; j < 3; ++j){
				soils[i*3 + j].setPoint(currentX, currentY);
				currentX += soilWidth / 2;
				currentY -= soilHeight / 2;
			}
		}
		arrowX = new int[7];
		arrowY = new int[7];
		arrowX[0] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[0] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		arrowX[1] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[1] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		arrowX[2] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[2] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		arrowX[3] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[3] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		arrowX[4] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[4] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()) - PhoneInfo.getRealHeight(40); 
		arrowX[5] = soils[0].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[5] = soils[0].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		arrowX[6] = soils[6].getPoint().x + PhoneInfo.getRealWidth(52);
		arrowY[6] = soils[6].getPoint().y - PhoneInfo.getRealHeight(arrows[0].getHeight()); 
		GameInfo.choose = 0;
		GameInfo.day = new Day();
		spadeAnimation = false;
		handAnimation = false;
		waterAnimation = false;
		fertlizeAnimation = false;
		animationNumber = 0;
		arrowNumber = 0;
		initiateSoil();
		initiateCropInfo();
		initiateCrop();	
	}
	
	private void initiateSoil(){
		for(int i = 0; i != initialNumber; ++i){
			soils[i].setAssart();
		}
		soils[initialNumber].setExtend();
	}
	
	private void initiateCropInfo(){
		CropInfo = new CropInfo[9];
		long time[][] = new long[9][7];
		time[0][0] = 0;
		time[0][1] = 15000;
		time[0][2] = 15000;
		time[0][3] = 15000;
		time[0][4] = 30000;
		time[0][5] = 15000;
		time[0][6] = 30000;
		time[1][0] = 0;
		time[1][1] = 15000;
		time[1][2] = 15000;
		time[1][3] = 15000;
		time[1][4] = 30000;
		time[1][5] = 15000;
		time[1][6] = 30000;
		time[2][0] = 0;
		time[2][1] = 15000;
		time[2][2] = 15000;
		time[2][3] = 15000;
		time[2][4] = 30000;
		time[2][5] = 15000;
		time[2][6] = 30000;
		time[3][0] = 0;
		time[3][1] = 15000;
		time[3][2] = 15000;
		time[3][3] = 30000;
		time[3][4] = 30000;
		time[3][5] = 30000;
		time[3][6] = 30000;
		time[4][0] = 0;
		time[4][1] = 30000;
		time[4][2] = 30000;
		time[4][3] = 30000;
		time[4][4] = 45000;
		time[4][5] = 45000;
		time[4][6] = 30000;
		time[5][1] = 30000;
		time[5][2] = 30000;
		time[5][3] = 30000;
		time[5][4] = 30000;
		time[5][5] = 30000;
		time[5][6] = 30000;
		time[6][1] = 30000;
		time[6][2] = 30000;
		time[6][3] = 30000;
		time[6][4] = 30000;
		time[6][5] = 30000;
		time[6][6] = 30000;
		time[7][0] = 0;
		time[7][1] = 30000;
		time[7][2] = 30000;
		time[7][3] = 30000;
		time[7][4] = 45000;
		time[7][5] = 45000;
		time[7][6] = 30000;
		time[8][0] = 0;
		time[8][1] = 20000;
		time[8][2] = 40000;
		time[8][3] = 40000;
		time[8][4] = 40000;
		time[8][5] = 40000;
		time[8][6] = 30000;
		
		int[] cost = new int[9];
		cost[0] = 30;
		cost[1] = 20;
		cost[2] = 30;
		cost[3] = 38;
		cost[4] = 50;
		cost[5] = 45;
		cost[6] = 70;
		cost[7] = 60;
		cost[8] = 80;
		int[] sale = new int[9]; 
		sale[0] = 65;
		sale[1] = 55;
		sale[2] = 65;
		sale[3] = 78;
		sale[4] = 128;
		sale[5] = 103;
		sale[6] = 150;
		sale[7] = 180;
		sale[8] = 200;
		for(int i = 0; i != 9; ++i){
			CropInfo[i] = new CropInfo();
			CropInfo[i].cropImage = cropPicture[i];
			CropInfo[i].cost = cost[i];
			CropInfo[i].sale = sale[i];
			CropInfo[i].time = time[i];
		}
	}
	
	private void initiateCrop(){
		crops = new Crop[18];
		for(int i = 0; i != 18 ; ++i){
			crops[i] = new Crop();
		}
	}
	
	public void logic(){
		if(GameInfo.isPause == false){
			//change the date of this game
			GameInfo.currentTime = System.currentTimeMillis();
			if((GameInfo.currentTime - GameInfo.day.dayStartTime) * GameInfo.speedChange >= 1000){
				GameInfo.day.dayStartTime = GameInfo.currentTime;
				GameInfo.day.logic();
				if((GameInfo.day.getDay()) % 3 == 0){
					//change the humidity of each soil
					for(int i = 0;i!=18;++i){
						if(soils[i].isAssarted()){
							if(GameInfo.day.getClimate() == GameInfo.day.SUNNY){
								soils[i].becomeSunny();
							}
							else if(GameInfo.day.getClimate() == GameInfo.day.CLOUDY){
								soils[i].becomeCloudy();
							}
							else if(GameInfo.day.getClimate() == GameInfo.day.RAINY){
								soils[i].becomeRainny();
							}
							else if(GameInfo.day.getClimate() == GameInfo.day.STORM){
								soils[i].becomeStorm();
							}
							if(soils[i].humidity < 20){
								soils[i].droughtDay += 3;
								soils[i].delugeDay = 0;
								soils[i].setState(Soil.DROUGHT);
							}
							else if(soils[i].humidity > 80){
								soils[i].delugeDay += 3;
								soils[i].droughtDay = 0;
								soils[i].setState(Soil.FLOOD);
							}
							else{
								soils[i].delugeDay  = 0;
								soils[i].droughtDay = 0;
								soils[i].setState(Soil.ASSART);
							}
						}
					}
					//change the nutrition of each soil
					for(int i = 0;i!=18;++i){
						if(crops[i].isCroped()&&soils[i].nutrition>0){
							soils[i].nutrition -= 1;
						}
					}
				}
			}
			//change the crop along with the times
			for(int i = 0;i!=18;++i){
				if(crops[i].isCroped()==true){
					crops[i].logic();
				}
			}
			
			//set the animation
			if(spadeAnimation || handAnimation || waterAnimation || fertlizeAnimation){
				if(animationNumber == 0){
					animationNumber = 1;
				}
				else if(animationNumber == 1){
					animationNumber = 2;
				}
				else if(animationNumber == 2){
					animationNumber = 1;
				}
			}
			
			//set the arrowAnimation
			if(arrowAnimation){
				++arrowNumber;
				if(arrowNumber == 3){
					arrowNumber = 0;
				}
			}
		}
	}
	
	public void draw(Canvas canvas, Paint paint){
		canvas.drawBitmap(backGround[GameInfo.day.getSeason()][GameInfo.day.getClimate()], null, new Rect(0,0,PhoneInfo.resolutionWidth,PhoneInfo.resolutionHeight), paint);
		//draw the listbox
		canvas.drawBitmap(tools[0], null, new Rect(listBoxX, listBoxY, listBoxX + listBoxWidth, listBoxY + listBoxHeight), paint);
		int width = PhoneInfo.getRealWidth(60);
		for(int i = 1; i<=7; ++i){
			if(GameInfo.choose == 0 || GameInfo.choose == i){
				canvas.drawBitmap(tools[i], null, new Rect(listBoxX + width*(i-1),listBoxY + PhoneInfo.getRealHeight(12), listBoxX + width*(i-1) + toolsWidth, listBoxY + PhoneInfo.getRealHeight(10) + toolsHeight) , paint);
			}
			else{
				canvas.drawBitmap(tools[i+7], null, new Rect(listBoxX +width*(i-1),listBoxY + PhoneInfo.getRealHeight(12), listBoxX + width*(i-1) + toolsWidth, listBoxY + PhoneInfo.getRealHeight(10) + toolsHeight) , paint);
			}
		}
		//draw the money
		canvas.drawBitmap(coin,null,new Rect(PhoneInfo.getRealWidth(15),PhoneInfo.getRealHeight(0),PhoneInfo.getRealWidth(15)+PhoneInfo.getFigureWidth(coin.getWidth()),PhoneInfo.getFigureHeight(coin.getHeight())),paint);
		textY = PhoneInfo.getRealHeight(40);
		textX = PhoneInfo.getRealWidth(65);
		paint.setColor(Color.BLACK);
		paint.setTypeface(font);
		float textSize;
		textSize = (float) (20 * PhoneInfo.heightRatio);
		paint.setTextSize(textSize);
		canvas.drawText("" + (int)(GameInfo.money), textX, textY, paint);
		
		//draw the month
		textY = PhoneInfo.getRealHeight(40);
		textX = PhoneInfo.getRealWidth(130);
		canvas.drawText(GameInfo.day.getMonthString(), textX, textY, paint);
		//draw the soil
		for(int i = 0;i<18;++i){
			soils[i].draw(canvas, paint);
		}
		//draw the crop	
		for(int i = 0;i<18;++i){
			if(crops[i].isCroped()==true){
				crops[i].draw(canvas, paint);
			}
		}
		//draw the animation
		if(spadeAnimation == true){
			canvas.drawBitmap(animations[0][animationNumber - 1],null,new Rect(animationX,animationY,animationX+PhoneInfo.getFigureWidth(animations[0][animationNumber - 1].getWidth()),animationY+PhoneInfo.getFigureHeight(animations[0][animationNumber - 1].getHeight())),paint);
		}
		else if(handAnimation == true){
			canvas.drawBitmap(animations[1][animationNumber - 1],null,new Rect(animationX,animationY,animationX+PhoneInfo.getFigureWidth(animations[1][animationNumber - 1].getWidth()),animationY+PhoneInfo.getFigureHeight(animations[1][animationNumber - 1].getHeight())),paint);
		}
		else if(waterAnimation == true){
			canvas.drawBitmap(animations[2][animationNumber - 1],null,new Rect(animationX,animationY,animationX+PhoneInfo.getFigureWidth(animations[2][animationNumber - 1].getWidth()),animationY+PhoneInfo.getFigureHeight(animations[2][animationNumber - 1].getHeight())),paint);
		}
		else if(fertlizeAnimation == true){
			canvas.drawBitmap(animations[3][animationNumber - 1],null,new Rect(animationX,animationY,animationX+PhoneInfo.getFigureWidth(animations[3][animationNumber - 1].getWidth()),animationY+PhoneInfo.getFigureHeight(animations[3][animationNumber - 1].getHeight())),paint);
		}
		//draw the tools
		canvas.drawBitmap(tools[17], null, new Rect(settingX, settingY, settingX + PhoneInfo.getRealHeight(50), settingY + PhoneInfo.getRealHeight(50)), paint);
		canvas.drawBitmap(tools[18], null, new Rect(saveX, saveY, saveX + PhoneInfo.getRealWidth(50),saveY + PhoneInfo.getRealHeight(50)),paint);
		canvas.drawBitmap(tools[19], null, new Rect(exitX, exitY, exitX + PhoneInfo.getRealWidth(50),exitY + PhoneInfo.getRealHeight(50)),paint);
		//draw the soilInfo
		if(gameState>=4&&gameState<=7){
			canvas.drawBitmap(textPicture[17],null,new Rect(textPictureX[17],textPictureY[17],textPictureX[17]+PhoneInfo.getFigureWidth(textPicture[17].getWidth()),textPictureY[17]+PhoneInfo.getFigureHeight(textPicture[17].getHeight())),paint);
		}
		//draw the textPicture
		canvas.drawBitmap(textPicture[gameState],null,new Rect(textPictureX[gameState],textPictureY[gameState],textPictureX[gameState]+PhoneInfo.getFigureWidth(textPicture[gameState].getWidth()),textPictureY[gameState]+PhoneInfo.getFigureHeight(textPicture[gameState].getHeight())),paint);
		//draw the arrows
		if(gameState == 1){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[0],arrowY[0],arrowX[0]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[0]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 3){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[1],arrowY[1],arrowX[1]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[1]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 8 && cropSelected == true){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[2],arrowY[2],arrowX[2]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[2]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 9 && cropSelected == true){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[3],arrowY[3],arrowX[3]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[3]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 10 && cropSelected == true){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[4],arrowY[4],arrowX[4]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[4]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 11 && cropSelected == true){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[5],arrowY[5],arrowX[5]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[5]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
		else if(gameState == 13 && cropSelected == true){
			canvas.drawBitmap(arrows[arrowNumber],null,new Rect(arrowX[6],arrowY[6],arrowX[6]+PhoneInfo.getFigureWidth(arrows[arrowNumber].getWidth()),arrowY[6]+PhoneInfo.getFigureHeight(arrows[arrowNumber].getHeight())),paint);
		}
	}
	
	public void onTouchEvent(MotionEvent event) {
		//get the point where user touch
		int pointX = (int)event.getX();
		int pointY = (int)event.getY();
		int width = PhoneInfo.getRealWidth(60);
		if(gameState == 0){	
			if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
			&&pointX > listBoxX + width * 1 && pointX < listBoxX + width * 2){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					GameInfo.choose = 2;
					GameInfo.money -= 20;	
					arrowAnimation = true;
					++gameState;
				}
			}
		}
		else if(gameState == 1){
			int index = getSoilNumber(new Point(pointX, pointY));
			if(index == 0){
				if(index < GameInfo.enlargeNumber && crops[index].isCroped() == false && GameInfo.money >= CropInfo[GameInfo.selectCropNumber].cost){	
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						GameInfo.soundEffect[2].play((float) 1.0);								
						soils[0].cancelLight();
						arrowAnimation = false;
						crops[index].setPicture(CropInfo[GameInfo.selectCropNumber].cropImage);
						crops[index].setCost(CropInfo[GameInfo.selectCropNumber].cost);
						crops[index].setSale(CropInfo[GameInfo.selectCropNumber].sale);
						crops[index].setTime(CropInfo[GameInfo.selectCropNumber].time);
						crops[index].setCrop();
						crops[index].setCropNumber(GameInfo.selectCropNumber);
						crops[index].setStartTime(System.currentTimeMillis());
						Point point = soils[index].getPoint();
						crops[index].setPoint(point.x, point.y - soilHeight);
						GameInfo.money -= CropInfo[GameInfo.selectCropNumber].cost;
						GameInfo.choose = 0;
						++gameState;
					}
				}
			}
		}
		else if(gameState == 2){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				++gameState;
				arrowAnimation = true;
			}
		}
		else if(gameState == 3){
			int index = getSoilNumber(new Point(pointX, pointY));
			if(index == 0){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					++gameState;
					arrowAnimation = false;
					soils[0].cancelLight();
				}
			}
		}
		else if(gameState == 4){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				++gameState;
			}
		}
		else if(gameState == 5){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				++gameState;
			}
		}
		else if(gameState == 6){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				++gameState;
			}
		}
		else if(gameState == 7){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				++gameState;		
			}
		}
		else if(gameState == 8){
			if(cropSelected == false){
				if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
						&&pointX > listBoxX + width * 4 && pointX < listBoxX + width * 5){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						GameInfo.soundEffect[4].play((float) 1.0);
						soils[0].setLight();
						cropSelected = true;
						arrowAnimation = true;
						GameInfo.choose = 5;
					}
				}
			}
			else if(cropSelected == true){
				int index = getSoilNumber(new Point(pointX, pointY));
				if(index == 0){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
						waterAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						arrowAnimation = false;
						waterAnimation = false;
						soils[index].humidity = 80;
						soils[index].setState(Soil.ASSART);
						++gameState;
						GameInfo.money -= 5;
						cropSelected = false;
						soils[0].cancelLight();
						GameInfo.choose = 0;
					}
				}				
			}
		}
		else if(gameState == 9){
			if(cropSelected == false){
				if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
						&&pointX > listBoxX + width * 5 && pointX < listBoxX + width * 6){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						soils[0].setLight();
						GameInfo.soundEffect[5].play((float) 1.0);
						cropSelected = true;
						arrowAnimation = true;
						GameInfo.choose = 6;
					}
				}
			}
			else{
				int index = getSoilNumber(new Point(pointX, pointY));
				if(index == 0){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
						fertlizeAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						++gameState;
						cropSelected = false;
						arrowAnimation = false;
						soils[0].setState(Soil.ASSART);
						GameInfo.choose = 0;
						GameInfo.money -= 5;
						crops[0].setState(Crop.FRUIT);
						fertlizeAnimation = false;
					}
				}				
			}
		}
		else if(gameState == 10){
			if(cropSelected == false){
				if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
						&&pointX > listBoxX + width * 2 && pointX < listBoxX + width * 3){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						soils[0].setLight();
						cropSelected = true;
						arrowAnimation = true;
					}
				}
			}
			else{
				int index = getSoilNumber(new Point(pointX, pointY));
				if(index == 0){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
						handAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){	
						if(crops[index].getState() == 5){
							GameInfo.soundEffect[3].play((float) 1.0);
							int predictSale = 0;
							if(soils[index].droughtDay > 0){
								predictSale = (int) (crops[index].getSale() - (soils[index].droughtDay / 100.0) * crops[index].getSale()); 
								if(predictSale < 0){
									predictSale = 0;
								}
							}
							else if(soils[index].delugeDay > 0){
								predictSale = (int) (crops[index].getSale() - (soils[index].delugeDay / 100.0) * crops[index].getSale());
								if(predictSale < 0){
									predictSale = 0;
								}
							}
							else{
								predictSale = crops[index].getSale();
							}
						
							if(soils[index].nutrition < 60){
								predictSale -= (60 - soils[index].nutrition) / 100.0 * predictSale; 
							}
							handAnimation = false;
							arrowAnimation = false;
							GameInfo.money += predictSale;
							soils[index].cancelLight();
							crops[index].setSere();
							cropSelected = false;
							++gameState;
						}
					}
				}
			}
		}
		else if(gameState == 11){
			if(cropSelected == false){
				if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
						&&pointX > listBoxX && pointX < listBoxX + width){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						soils[0].setLight();
						arrowAnimation = true;
						cropSelected = true;
						GameInfo.choose = 1;
					}
				}
			}
			else{
				int index = getSoilNumber(new Point(pointX, pointY));
				if(index == 0){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
						spadeAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						GameInfo.soundEffect[1].play((float) 1.0);
						spadeAnimation = false;
						arrowAnimation = false;
						++gameState;
						cropSelected = false;
						soils[0].cancelLight();
						GameInfo.choose = 0;
						crops[0].removeCrop();
					}
				}					
			}
		}	
		else if(gameState == 12){
			if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
					&&pointX > listBoxX + width * 3 && pointX < listBoxX + width * 4){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					++gameState;
					GameInfo.money += 100;
				}
			}
		}
		else if(gameState == 13){
			if(cropSelected == false){
				if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
						&&pointX > listBoxX && pointX < listBoxX + width){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						soils[6].setLight();
						cropSelected = true;
						arrowAnimation = true;
						GameInfo.money += 200;
					}
				}
			}
			else{
				int index = getSoilNumber(new Point(pointX, pointY));
				if(index == 6){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
						spadeAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						GameInfo.soundEffect[1].play((float) 1.0);
						spadeAnimation = false;
						arrowAnimation = false;
						++gameState;
						cropSelected = false;
						if(soils[index].getState() == Soil.ENLARGEPRESS){
							if(GameInfo.money >= GameInfo.enlargeMoney){
								soils[index].setAssart();
								soils[index+1].setExtend();
								GameInfo.money -= GameInfo.enlargeMoney;
								++(GameInfo.enlargeNumber);
								soils[6].cancelLight();
								GameInfo.money += 2000;
							}
							else{
								Intent intent = new Intent(GameEngineActivity.instance, EnsureEnlargeCancelActivity.class);
								GameEngineActivity.instance.startActivity(intent);
							}
						}
					}
				}
			}
		}
		else if(gameState == 14){
			if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
					&&pointX > listBoxX + 6*width && pointX < listBoxX + 7*width){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					GameInfo.choose = 0;
					GameInfo.money -= 2000;
					++gameState;
				}
			}
		}
		else if(gameState == 15){
			if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
					&&pointX > listBoxX + 6*width && pointX < listBoxX + 7*width){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					GameInfo.choose = 0;
					GameInfo.money -= 50;
					++gameState;
				}
			}
		}
		else if(gameState == 16){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				GameSurfaceView.myView.releaseGameIntroResource();
				GameSurfaceView.myView.initiateNewGame();			
				GameInfo.money = 100;
				GameInfo.startTime = System.currentTimeMillis();
				GameInfo.selectCropNumber = 0;
				GameInfo.choose = 0;
				GameInfo.isEnlarge = false;
				GameInfo.enlargeNumber = 6;
				GameInfo.currentLoanNumber = 0;
				GameInfo.isWaterTankSelected = false;
				GameSurfaceView.myView.gameState = GameSurfaceView.myView.GAMMING;
			}			
		}
	}	
	
	private int getSoilNumber(Point coordinate){
		int originalX = startX;
		int originalY = startY;
		double k = (double)soilHeight / soilWidth * -1;
		double[] b = new double[7];
		double[] tempValue = new double[7];
		b[0] = originalY - k * originalX;
		tempValue[0] = coordinate.y - k * coordinate.x - b[0];
		for(int i = 1; i < 7 ; ++i){
			b[i] = b[i-1] + soilHeight; 
			tempValue[i] = coordinate.y - k * coordinate.x - b[i];
		}
		double[] b2 = new double[4];
		double[] tempValue2 = new double[4];
		b2[0] = originalY + k * originalX;
		tempValue2[0] =  -1 * k * coordinate.x - coordinate.y + b2[0];
		for(int i = 1; i < 4; ++i){
			b2[i] = b2[i-1] - soilHeight;
			tempValue2[i] = -1 * k * coordinate.x - coordinate.y + b2[i];
		}
		if(tempValue[0] >= 0 && tempValue[1] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 0;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 1;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 2;
			}
		}
		else if(tempValue[1] >= 0 && tempValue[2] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 3;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 4;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 5;
			}
		}
		else if(tempValue[2] >= 0 && tempValue[3] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 6;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 7;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 8;
			}
		}
		else if(tempValue[3] >= 0 && tempValue[4] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 9;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 10;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 11;
			}
		}
		else if(tempValue[4] >= 0 && tempValue[5] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 12;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 13;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 14;
			}
		}
		else if(tempValue[5] >= 0 && tempValue[6] < 0){
			if(tempValue2[0] >= 0 && tempValue2[1] < 0){
				return 15;
			}
			else if(tempValue2[1] >= 0 && tempValue2[2] < 0){
				return 16;
			}
			else if(tempValue2[2] >= 0 && tempValue2[3] < 0){
				return 17;
			}
		}
		return -1;
	}
}
