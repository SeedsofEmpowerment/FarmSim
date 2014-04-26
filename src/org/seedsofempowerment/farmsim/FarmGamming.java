/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : FarmGamming.java
* description : This class is for processing all the information when the game start
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
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

//This class is for the gamming model
public class FarmGamming {
	private Bitmap[][] backGround;
	private int listBoxX;
	private int listBoxY;
	private int listBoxWidth;
	private int listBoxHeight;
	private int toolsWidth;
	private int toolsHeight;
	private int textX;
	private int textY;
	private int dateX;
	private int dateY;
	private int animationX;
	private int animationY;
	private int saveX;
	private int saveY;
	private int exitX;
	private int exitY;
	private int settingX;
	private int settingY;
	private int[] savePictureX;
	private int[] savePictureY;
	private long allPauseTime = 0;
	private Bitmap[] soilPicture;
	private Soil[] soils;
	private Bitmap[] tools;
	private Crop[] crops;
	private Bitmap[][] cropPicture;
	private Bitmap[][] animations;
	private Bitmap[] savePicture;
	private Bitmap coin;
	private Bitmap calenderPlace;
	private boolean spadeAnimation;
	private boolean handAnimation;
	private boolean waterAnimation;
	private boolean fertlizeAnimation;
	private boolean successfullySaved;
	private boolean failSaved;
	private boolean gameStarted;
	private boolean plantMoreText;
	private int animationNumber;
	private Typeface font;
	
	private FileOutputStream fos;
	private DataOutputStream dos;
	private FileInputStream fis;
	private DataInputStream dis;
	private int showSaveTime = 0;
	
	private static final int startX = PhoneInfo.getRealWidth(45);
	private static final int startY = PhoneInfo.getRealHeight(240);
	private static final int soilWidth = PhoneInfo.getRealWidth(160);
	private static final int soilHeight = PhoneInfo.getRealHeight(68);
	private static final int initialNumber = 6;
	public static final int SPRING = 0;
	public static final int SUMMER = 1;
	public static final int AUTUMN = 2;
	public static final int WINTER = 3;
	
	public static CropInfo[] CropInfo;
	public FarmGamming(Bitmap[][] backGround, Bitmap[] soilPicture, Bitmap[] tools, Bitmap[][] cropPicture, Bitmap[][] animations, Bitmap[] savePicture, Bitmap coin, Bitmap calenderPlace,Typeface font){
		this.backGround = backGround;
		this.soilPicture = soilPicture;
		this.savePicture = savePicture;
		this.tools = tools;
		this.cropPicture = cropPicture;
		this.animations = animations;
		this.coin = coin;
		this.calenderPlace = calenderPlace;
		this.font = font;
		//initialize the soils
		soils = new Soil[18];
		for(int i = 0;i!=18;++i){
			soils[i] = new Soil(soilPicture);
		}
		listBoxX = 0;
		listBoxY = PhoneInfo.getRealHeight(415);
		listBoxWidth = PhoneInfo.getFigureWidth(tools[0].getWidth());
		listBoxHeight = PhoneInfo.getFigureHeight(tools[0].getHeight());
		toolsWidth = PhoneInfo.getFigureWidth(tools[1].getWidth());
		toolsHeight = PhoneInfo.getFigureHeight(tools[1].getHeight());
		dateX =  PhoneInfo.getRealWidth(500);
		dateY = PhoneInfo.getRealHeight(50);	
		saveX = PhoneInfo.resolutionWidth - PhoneInfo.getRealWidth(3 * 50);
		saveY = PhoneInfo.resolutionHeight -  PhoneInfo.getRealHeight(50);
		exitX = saveX + PhoneInfo.getRealWidth(50);
		exitY = saveY;
		settingX = exitX + PhoneInfo.getRealWidth(50);
		settingY = saveY;
		
		GameInfo.waterTank = new WaterTank();
		GameInfo.myLoan = new Loan[3];
		for(int i = 0;i!=3;++i){
			GameInfo.myLoan[i] = new Loan();
		}
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
		GameInfo.choose = 0;
		GameInfo.day = new Day();
		spadeAnimation = false;
		handAnimation = false;
		waterAnimation = false;
		fertlizeAnimation = false;
		successfullySaved = false;
		plantMoreText = false;
		failSaved = false;
		gameStarted = true;
		animationNumber = 0;
		allPauseTime = 0;
		savePictureX = new int[4];
		savePictureY = new int[4];
		for(int i = 0;i!=4;++i){
			savePictureX[i] = (PhoneInfo.resolutionWidth - PhoneInfo.getFigureWidth(savePicture[i].getWidth())) / 2;
			savePictureY[i] = (PhoneInfo.resolutionHeight - PhoneInfo.getFigureHeight(savePicture[i].getHeight())) / 2;
		}
		GameInfo.timeCropPlanted = new CropTime[1000];
		GameInfo.timeMoneyBorrowed = new BorrowTime[1000];
		for(int i = 0;i!=1000;++i){
			GameInfo.timeCropPlanted[i] = new CropTime();
			GameInfo.timeMoneyBorrowed[i] = new BorrowTime();
		}
		if(GameInfo.isLoad == false){
			initiateSoil();
			initiateCropInfo();
			initiateCrop();
		}
		
		else{
			boolean isHaveSDCard = false;
			if(Environment.getExternalStorageState() != null && !Environment.getExternalStorageState().equals("removed")){
				Log.v("wang","read from files");
				isHaveSDCard = true;
			}
			try{
				if(isHaveSDCard){
					File path = new File("/sdcard/FarmSim/save");
					File file = new File("/sdcard/FarmSim/save/"+GameInfo.user.getName()+".txt");
					if(path.exists()&&file.exists()){
						fis = new FileInputStream(file);
					}
				}
				else{
					if(GameEngineActivity.instance.openFileInput(GameInfo.user.getName()+".txt") != null){
						fis = GameEngineActivity.instance.openFileInput(GameInfo.user.getName()+".txt");
					}
				}
				dis = new DataInputStream(fis);
				loadGameInfo();
				initiateCropInfo();
				initiateCrop();
				loadSoil();
				loadCrop();
				allPauseTime = dis.readLong();
				GameInfo.pauseStartTime = System.currentTimeMillis();
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				try{
					if(fis != null){
						fis.close();
					}
					if(dis != null){
						dis.close();
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initiateSoil(){
		for(int i = 0; i != initialNumber; ++i){
			soils[i].setAssart();
		}
		soils[initialNumber].setExtend();
	}
	
	private void loadGameInfo() throws IOException{
		String name = dis.readUTF();
		GameInfo.user.setAge(dis.readInt());
		GameInfo.user.setGender(dis.readInt());
		GameInfo.user.setBalance(dis.readInt());
		GameInfo.user.setTime(dis.readDouble());
		GameInfo.user.setTotalEarning(dis.readInt());
		GameInfo.user.setBorrowed(dis.readInt());
		GameInfo.user.setScore(dis.readInt());
		GameInfo.money = dis.readDouble();
		GameInfo.startTime = System.currentTimeMillis() - dis.readLong();
		GameInfo.currentTime = System.currentTimeMillis();
		GameInfo.pauseStartTime = 0;
		GameInfo.pauseEndTime = 0;
		GameInfo.pauseTime = 0;
		GameInfo.isEnlarge = false;
		GameInfo.selectCropNumber = dis.readInt();	
		GameInfo.enlargeNumber = dis.readInt();
		GameInfo.remainOpportunity = dis.readInt();
		GameInfo.currentLoanNumber = dis.readInt();
		
		//read the loan
		if(GameInfo.currentLoanNumber > 0){
			for(int i = 0;i!=3;++i){
				int loanDay = dis.readInt();
				int loanMonth = dis.readInt();
				int principle = dis.readInt();
				double interest = dis.readDouble() / 100;
				//read the amount;
				dis.readDouble();
				int month = dis.readInt();
				int monthPast = dis.readInt();
				//read the month left;
				dis.readInt();
				GameInfo.myLoan[i] = new Loan(principle, interest, month);
				GameInfo.myLoan[i].setLoanDay(loanDay);
				GameInfo.myLoan[i].setLoanMonth(loanMonth);
				GameInfo.myLoan[i].setMounthPast(monthPast);
			}
		}			
		int timeCropNumber = dis.readInt();
		int timeMoneyNumber = dis.readInt();
		GameInfo.timeCropNumber = timeCropNumber;
		GameInfo.timeMoneyNumber = timeMoneyNumber;
		for(int i = 0;i!=GameInfo.timeCropNumber;++i){
			GameInfo.timeCropPlanted[i].crop = dis.readLine();
			GameInfo.timeCropPlanted[i].time = dis.readLong();
		}
		for(int i = 0;i!=GameInfo.timeMoneyNumber;++i){
			GameInfo.timeMoneyBorrowed[i].money = dis.readInt();
			GameInfo.timeMoneyBorrowed[i].time = dis.readLong();
		}
		GameInfo.speed = dis.readInt();		
		GameInfo.speedChange = dis.readDouble();
		GameInfo.isPause = dis.readBoolean();
		GameInfo.day.setSeason(dis.readInt());
		GameInfo.day.setLastDay(dis.readInt());
		GameInfo.day.setLastMonth(dis.readInt());
		GameInfo.day.setLastYear(dis.readInt());
		GameInfo.day.setMonth(dis.readInt());
		GameInfo.day.setDay(dis.readInt());
		GameInfo.day.setClimate(dis.readInt());
		GameInfo.waterTank.setCurrentVolume(dis.readInt());
		GameInfo.plantMore = dis.readBoolean();
		GameInfo.isWaterTankSelected = dis.readBoolean();
	}
	
	private void loadSoil() throws IOException{
		for(int i = 0;i!=18;++i){
			soils[i].setState(dis.readInt());
			soils[i].humidity = dis.readInt();
			soils[i].nutrition = dis.readInt();
			soils[i].droughtDay = dis.readInt();
			soils[i].delugeDay = dis.readInt();
		}
	}
	
	private void loadCrop() throws IOException{
		for(int i = 0;i!=18;++i){
			Boolean isCroped = dis.readBoolean();
			if(isCroped == true){
				int cropNumber = dis.readInt();
				crops[i].setCrop();
				crops[i].setCropNumber(cropNumber);
				crops[i].setPicture(CropInfo[cropNumber].cropImage);
				crops[i].setCost(dis.readInt());
				crops[i].setSale(dis.readInt());
				crops[i].setState(dis.readInt());
				crops[i].setStartTime(System.currentTimeMillis() - dis.readLong());
				crops[i].setPauseTime(dis.readLong());
				crops[i].setTime(CropInfo[cropNumber].time);
				Point point = soils[i].getPoint();
				crops[i].setPoint(point.x, point.y - soilHeight);
			}
		}
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
	
	
	
	public void setPause(){
		for(int i = 0;i!=18;++i){
			if(crops[i].isCroped()==true){
				crops[i].addPauseTime(GameInfo.pauseTime);
			}
		}
		allPauseTime+=GameInfo.pauseTime;
		GameInfo.pauseTime = 0;
	}
	
	public void logic(){
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
							if(GameInfo.isWaterTankSelected == false || GameInfo.waterTank.getCurrentVolume()-20+soils[i].humidity < 0){
								soils[i].droughtDay += 3;
								soils[i].delugeDay = 0;
								soils[i].setState(Soil.DROUGHT);
							}
							else{
								GameInfo.waterTank.setCurrentVolume(GameInfo.waterTank.getCurrentVolume()-20+soils[i].humidity);
								soils[i].humidity = 20;
							}
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
					if(soils[i].nutrition < 60 && soils[i].getState() == Soil.ASSART){
						soils[i].setState(Soil.WEAKCULTIVATED);
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
		
		//decrease the money for loan
		if(GameInfo.currentLoanNumber > 0){
			for(int i = 0;i!=3;++i){
				if(GameInfo.myLoan[i].getMonthLeft()>0){
					if(GameInfo.day.getDay() == GameInfo.myLoan[i].getLoanDay() && GameInfo.day.getMonth() == GameInfo.myLoan[i].getLoanMonth() + 1){	
						if(GameInfo.money >= GameInfo.myLoan[i].getMonthPaid()){
							GameInfo.soundEffect[6].play((float) 1.0);
							GameInfo.money -= GameInfo.myLoan[i].getMonthPaid();
							GameInfo.myLoan[i].setNextMonth();
							++(GameInfo.myLoan[i].monthPast);
							if(GameInfo.myLoan[i].getMonthLeft()==0){
								--(GameInfo.currentLoanNumber);
							}
						}
						//when the user is broken
						else if(GameInfo.money < GameInfo.myLoan[i].getMonthPaid() && GameInfo.index == 0){
							GameInfo.soundEffect[6].play((float) 1.0);
							GameInfo.money -= GameInfo.myLoan[i].getMonthPaid();
							GameInfo.myLoan[i].setNextMonth();
							++(GameInfo.myLoan[i].monthPast);
							if(GameInfo.myLoan[i].getMonthLeft()==0){
								--(GameInfo.currentLoanNumber);
							}
							//we need to take out another loan
							if(GameInfo.remainOpportunity == 2){
								GameInfo.index = 1;
							}
							else if(GameInfo.remainOpportunity == 1){
								GameInfo.index = 2;
							}
							Intent intent = new Intent(GameEngineActivity.instance, BalanceWarningActivity.class);
							GameEngineActivity.instance.startActivity(intent);
						}
					}	
				}
			}
		}
		
		if(GameInfo.money >= 1000 && GameInfo.plantMore == false){
			GameInfo.plantMore = true;
			plantMoreText = true;
		}
		
		if(successfullySaved || failSaved || gameStarted || plantMoreText){
			++showSaveTime;
			if(showSaveTime == 10){
				successfullySaved = false;
				failSaved = false;
				gameStarted = false;
				plantMoreText = false;
				showSaveTime = 0;
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
		//draw the water
		int waterY = (int) (listBoxY + PhoneInfo.getRealHeight(50) - PhoneInfo.getRealHeight(36)*(float)GameInfo.waterTank.getCurrentVolume()/GameInfo.waterTank.getVolume());
		int waterX = listBoxX + width * 6;
		if(GameInfo.choose == 0 || GameInfo.choose == 7){
			canvas.drawBitmap(tools[15],null,new Rect(waterX,waterY,waterX+width,listBoxY+PhoneInfo.getRealHeight(10)+toolsHeight),paint);
		}
		else{
			canvas.drawBitmap(tools[16],null,new Rect(waterX,waterY,waterX+width,listBoxY+PhoneInfo.getRealHeight(10)+toolsHeight),paint);
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
		
		//draw the total time
		for(int i = 0;i!=3;++i){
			canvas.drawBitmap(calenderPlace,null,new Rect(PhoneInfo.getRealWidth(50)+i*PhoneInfo.getFigureWidth(calenderPlace.getWidth()),PhoneInfo.getRealHeight(40),PhoneInfo.getRealWidth(50)+(i+1)*PhoneInfo.getFigureWidth(calenderPlace.getWidth()),PhoneInfo.getFigureHeight(coin.getHeight())+PhoneInfo.getRealHeight(40)),paint);
		}
		textSize = (float) (18 * PhoneInfo.heightRatio);
		paint.setTextSize(textSize);
		dateX = PhoneInfo.getRealWidth(75);
		dateY = PhoneInfo.getRealHeight(63);
		canvas.drawText("Y", dateX, dateY, paint);
		dateX = PhoneInfo.getRealWidth(140);
		canvas.drawText("M", dateX, dateY, paint);
		dateX = PhoneInfo.getRealWidth(205);
		canvas.drawText("D", dateX, dateY, paint);
		dateY = PhoneInfo.getRealHeight(83);
		dateX = PhoneInfo.getRealWidth(75);
		canvas.drawText(GameInfo.day.getLastYear()+"", dateX, dateY, paint);
		dateX = PhoneInfo.getRealWidth(140);
		canvas.drawText(GameInfo.day.getLastMonth()+"", dateX, dateY, paint);
		dateX = PhoneInfo.getRealWidth(205);
		canvas.drawText(GameInfo.day.getLastDay()+"", dateX, dateY, paint);
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
		//draw the saveText
		if(gameStarted == true){
			canvas.drawBitmap(savePicture[2],null,new Rect(savePictureX[2],savePictureY[2],savePictureX[2]+PhoneInfo.getFigureWidth(savePicture[2].getWidth()),savePictureY[2] + PhoneInfo.getFigureHeight(savePicture[2].getHeight())), paint);
		}
		if(successfullySaved == true){
			canvas.drawBitmap(savePicture[0],null,new Rect(savePictureX[0],savePictureY[0],savePictureX[0]+PhoneInfo.getFigureWidth(savePicture[0].getWidth()),savePictureY[0] + PhoneInfo.getFigureHeight(savePicture[0].getHeight())), paint);
		}
		if(failSaved == true){
			canvas.drawBitmap(savePicture[1],null,new Rect(savePictureX[1],savePictureY[1],savePictureX[1]+PhoneInfo.getFigureWidth(savePicture[1].getWidth()),savePictureY[0] + PhoneInfo.getFigureHeight(savePicture[1].getHeight())), paint);
		}
		if(plantMoreText == true){
			canvas.drawBitmap(savePicture[3],null,new Rect(savePictureX[3],savePictureY[3],savePictureX[3]+PhoneInfo.getFigureWidth(savePicture[3].getWidth()),savePictureY[3] + PhoneInfo.getFigureHeight(savePicture[3].getHeight())), paint);
		}
	}
	
	public void onTouchEvent(MotionEvent event) {
		//get the point where user touch
		int pointX = (int)event.getX();
		int pointY = (int)event.getY();
		int width = PhoneInfo.getRealWidth(60);
		if(pointY > listBoxY+PhoneInfo.getRealHeight(10)&&pointY < listBoxY+PhoneInfo.getRealHeight(55)
				&&pointX > listBoxX && pointX < listBoxX+listBoxWidth){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				for(int i = 1; i <= 7; ++i){
					if(pointX > listBoxX + width*(i-1) && pointX < listBoxX + width * i){
						GameInfo.choose = i;
						break;
					}
				}
				
				if(GameInfo.choose == 2){
					Intent intent = new Intent(GameEngineActivity.instance, SelectPlantActivity.class);
					GameEngineActivity.instance.startActivity(intent);
				}
				
				else if(GameInfo.choose == 4){
					Intent intent = new Intent(GameEngineActivity.instance, LoanActivity.class);
					GameEngineActivity.instance.startActivity(intent);
					GameInfo.choose = 0;
				}
				
				else if(GameInfo.choose == 7){
					if(GameInfo.isWaterTankSelected == false){
						Intent intent = new Intent(GameEngineActivity.instance, EnsureTankActivity.class);
						GameEngineActivity.instance.startActivity(intent);
						GameInfo.choose = 0;
					}
					else{
						Intent intent = new Intent(GameEngineActivity.instance, EnsureFillWaterActivity.class);
						GameEngineActivity.instance.startActivity(intent);
						GameInfo.choose = 0;
					}
				}
			}
		}
		//the save function
		else if(pointY > saveY && pointY < saveY + PhoneInfo.getRealHeight(50)
			&&pointX > saveX && pointX < saveX + PhoneInfo.getRealWidth(50)){	
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
				GameSurfaceView.gameState = GameSurfaceView.SAVE_GAME;
				try{
					//judge whether exist the SD card
					if(Environment.getExternalStorageState() != null && !Environment.getExternalStorageState().equals("removed")){
						Log.v("wang","exist sd card");
						//declare the path
						File path = new File("/sdcard/FarmSim/save");
						if(!path.exists()){
							path.mkdirs();
						}
						//create the store file
						File file = new File("/sdcard/FarmSim/save/"+GameInfo.user.getName()+".txt");
						if(!file.exists()){
							file.createNewFile();		
						}
						fos = new FileOutputStream(file,false);
					}
					
					else{
						fos = GameEngineActivity.instance.openFileOutput(GameInfo.user.getName()+".txt", Context.MODE_PRIVATE);				
					}
					dos = new DataOutputStream(fos);
					//save the info of GameInfo
					dos.writeUTF(GameInfo.user.getName());
					dos.writeInt(GameInfo.user.getAge());
					dos.writeInt(GameInfo.user.getGender());
					dos.writeInt(GameInfo.user.getBalance());
					dos.writeDouble(GameInfo.user.getTime());
					dos.writeInt(GameInfo.user.getTotalEarning());
					dos.writeInt(GameInfo.user.getBorrowed());
					dos.writeInt(GameInfo.user.getScore());
					
					dos.writeDouble(GameInfo.money);
					dos.writeLong(System.currentTimeMillis() - GameInfo.startTime);
					dos.writeInt(GameInfo.selectCropNumber);
					dos.writeInt(GameInfo.enlargeNumber);
					dos.writeInt(GameInfo.remainOpportunity);
					dos.writeInt(GameInfo.currentLoanNumber);
					//save the Loan
					if(GameInfo.currentLoanNumber > 0){
						for(int i = 0;i!=3;++i){
							dos.writeInt(GameInfo.myLoan[i].getLoanDay());
							dos.writeInt(GameInfo.myLoan[i].getLoanMonth());
							dos.writeInt(GameInfo.myLoan[i].getPrinciple());
							dos.writeDouble(GameInfo.myLoan[i].getInterest());
							dos.writeDouble(GameInfo.myLoan[i].getAmount());
							dos.writeInt(GameInfo.myLoan[i].getMounth());
							dos.writeInt(GameInfo.myLoan[i].getMounthPast());
							dos.writeInt(GameInfo.myLoan[i].getMonthLeft());
						}
					}			
					dos.writeInt(GameInfo.timeCropNumber);
					dos.writeInt(GameInfo.timeMoneyNumber);
					for(int i = 0;i!=GameInfo.timeCropNumber;++i){
						dos.writeChars(GameInfo.timeCropPlanted[i].crop);
						dos.writeChar('\n');
						dos.writeLong(GameInfo.timeCropPlanted[i].time);
					}
					for(int i = 0;i!=GameInfo.timeMoneyNumber;++i){
						dos.writeInt(GameInfo.timeMoneyBorrowed[i].money);
						dos.writeLong(GameInfo.timeMoneyBorrowed[i].time);
					}
					dos.writeInt(GameInfo.speed);
					dos.writeDouble(GameInfo.speedChange);
					dos.writeBoolean(GameInfo.isPause);
					dos.writeInt(GameInfo.day.getSeason());
					dos.writeInt(GameInfo.day.getLastDay());
					dos.writeInt(GameInfo.day.getLastMonth());
					dos.writeInt(GameInfo.day.getLastYear());
					dos.writeInt(GameInfo.day.getMonth());
					dos.writeInt(GameInfo.day.getDay());
					dos.writeInt(GameInfo.day.getClimate());
					dos.writeInt(GameInfo.waterTank.getCurrentVolume());
					dos.writeBoolean(GameInfo.plantMore);
					dos.writeBoolean(GameInfo.isWaterTankSelected);
					//save soils info
					for(int i = 0;i!=18;++i){
						dos.writeInt(soils[i].getState());
						dos.writeInt(soils[i].humidity);
						dos.writeInt(soils[i].nutrition);
						dos.writeInt(soils[i].droughtDay);
						dos.writeInt(soils[i].delugeDay);
					}	
					//save the crop info
					for(int i = 0;i!=18;++i){
						dos.writeBoolean(crops[i].isCroped());
						if(crops[i].isCroped() == true){
							dos.writeInt(crops[i].getCropNumber());
							dos.writeInt(crops[i].getCost());
							dos.writeInt(crops[i].getSale());
							dos.writeInt(crops[i].getState());
							dos.writeLong(System.currentTimeMillis() - crops[i].getStartTime());
							dos.writeLong(crops[i].getPauseTime());
						}
					}
					//save allPauseTime
					dos.writeLong(allPauseTime);	
					successfullySaved = true;
					GameSurfaceView.gameState = GameSurfaceView.GAMMING;			
				}	
				catch(FileNotFoundException e) {
					e.printStackTrace();
					failSaved = true;
					GameSurfaceView.gameState = GameSurfaceView.GAMMING;
				}
				catch(IOException e){
					e.printStackTrace();
					failSaved = true;
					GameSurfaceView.gameState = GameSurfaceView.GAMMING;
				}
				finally{
					try{
						if(fos != null)
							fos.close();
						if(dos != null)
							dos.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				}
			}

		//the exist function
		else if(pointY > exitY && pointY < exitY + PhoneInfo.getRealHeight(50)
			&&pointX > exitX && pointX < exitX + PhoneInfo.getRealWidth(50)){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
					GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				Intent intent = new Intent(GameEngineActivity.instance, EnsureExitActivity.class);
				GameEngineActivity.instance.startActivity(intent);
			}
		}
		else if(pointY > settingY && pointY < settingY + PhoneInfo.getRealHeight(50)
			&&pointX > settingX && pointX < settingX + PhoneInfo.getRealWidth(50)){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				GameInfo.vibrate.playVibrate(-1);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				Intent intent = new Intent(GameEngineActivity.instance, SettingActivity.class);
				GameEngineActivity.instance.startActivity(intent);
			}
		}
		else{
			int index = getSoilNumber(new Point(pointX, pointY));
			if(index >= 0){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					soils[index].setLight();
					GameInfo.vibrate.playVibrate(-1);
					if(GameInfo.choose == 1){
						spadeAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(GameInfo.choose == 3){
						handAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(GameInfo.choose == 5){
						waterAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
					else if(GameInfo.choose == 6){
						fertlizeAnimation = true;
						Point point = soils[index].getPoint();
						animationX = point.x;
						animationY = point.y - soilHeight;
					}
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					for(int i = 0; i!=18;++i){
						soils[i].cancelLight();
					}
					if(GameInfo.choose == 0){
						//show the state of each soil
						if(soils[index].isAssarted() == true && GameInfo.isSoilInfoSelected == false){
							GameInfo.isSoilInfoSelected = true;
							Intent intent = new Intent(GameEngineActivity.instance, SoilInfoActivity.class);
							intent.putExtra("humidity", soils[index].humidity);
							intent.putExtra("nutrition", soils[index].nutrition);
							String cropName = "null";
							if(crops[index].isCroped()){
								if(crops[index].getCropNumber() == 0){
									cropName = "capsicum";
								}
								else if(crops[index].getCropNumber() == 1){
									cropName = "watermelon";
								}
								else if(crops[index].getCropNumber() == 2){
									cropName = "beans";
								}
								else if(crops[index].getCropNumber() == 3){
									cropName = "peach";
								}
								else if(crops[index].getCropNumber() == 4){
									cropName = "rice";
								}
								else if(crops[index].getCropNumber() == 5){
									cropName = "wheat";
								}
								else if(crops[index].getCropNumber() == 6){
									cropName = "guava";
								}
								else if(crops[index].getCropNumber() == 7){
									cropName = "apple";
								}
								else if(crops[index].getCropNumber() == 8){
									cropName = "rose";
								}
								intent.putExtra("cropName", cropName);
							}
							
							else{
								intent.putExtra("cropName", cropName);
							}
							intent.putExtra("cost", crops[index].getCost());
							intent.putExtra("sale", crops[index].getSale());
							intent.putExtra("state", crops[index].getState());
							int predictSale = 0;
							if(crops[index].isSered()==false){
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
							}
							intent.putExtra("predictSale", predictSale);
							GameEngineActivity.instance.startActivity(intent);
						}
					}
					else if(GameInfo.choose == 1){
						GameInfo.choose = 0;
						if(soils[index].getState() == Soil.ENLARGE){
							if(GameInfo.money >= GameInfo.enlargeMoney){
								GameInfo.soundEffect[1].play((float) 1.0);
								soils[index].setAssart();
								if(index < 17){
									soils[index+1].setExtend();
								}
								GameInfo.money -= GameInfo.enlargeMoney;
								++(GameInfo.enlargeNumber);
							}
							else{
								Intent intent = new Intent(GameEngineActivity.instance, EnsureEnlargeCancelActivity.class);
								GameEngineActivity.instance.startActivity(intent);
							}
						}
						else if(soils[index].isAssarted()){
							if(crops[index].isSered()){
								GameInfo.soundEffect[1].play((float) 1.0);
								crops[index].removeCrop();
								GameInfo.choose = 1;
							}
						}
					}
					else if(GameInfo.choose == 2){
						GameInfo.choose = 0;
						if(index < GameInfo.enlargeNumber && crops[index].isCroped() == false && GameInfo.money >= CropInfo[GameInfo.selectCropNumber].cost){
							GameInfo.soundEffect[2].play((float) 1.0);
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
							GameInfo.choose = 2;
						}	 
					}
					else if(GameInfo.choose == 3){
						GameInfo.choose = 0;
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
							GameInfo.money += predictSale;
							crops[index].setSere();
							GameInfo.choose = 3;
						}
					}
					else if(GameInfo.choose == 5){
						GameInfo.choose = 0;
						if(soils[index].isAssarted() && GameInfo.money >=5 && soils[index].humidity<80){
							GameInfo.soundEffect[4].play((float) 1.0);
							soils[index].humidity = 80;
							if(soils[index].getState() == Soil.DROUGHT){
								soils[index].setState(Soil.ASSART);
							}
							GameInfo.choose = 5;
							GameInfo.money -= 5;
						}	
					}
					else if(GameInfo.choose == 6){
						GameInfo.choose = 0;
						if(soils[index].isAssarted() && GameInfo.money >= 5 && soils[index].nutrition!=100){
							GameInfo.soundEffect[5].play((float) 1.0);
							soils[index].nutrition = 100;
							if(soils[index].getState() == Soil.WEAKCULTIVATED){
								soils[index].setState(Soil.ASSART);
							}
							GameInfo.choose = 6;
							GameInfo.money -= 5;
						}
					}
					spadeAnimation = false;
					handAnimation = false;
					waterAnimation = false;
					fertlizeAnimation = false;
					animationNumber = 0;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_UP){
					for(int i = 0; i!=18;++i){
						soils[i].cancelLight();
					}
					spadeAnimation = false;
					handAnimation = false;
					waterAnimation = false;
					fertlizeAnimation = false;
					animationNumber = 0;
				}
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
