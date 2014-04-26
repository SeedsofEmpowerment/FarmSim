/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : GameInfo.java
* description : The class is used to store all the relevant information of the game
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

//This Class is used for providing the essential information for the whole game
public class GameInfo {
	public static User user;
	public static Vibrate vibrate;            
	public static BackGroundMusic backGroundMusic;
	public static SoundEffect[] soundEffect;
	public static double money = 100;
	public static long startTime;
	public static long currentTime;
	public static long pauseStartTime;
	public static long pauseEndTime;
	public static long pauseTime;
	public static int selectCropNumber; 
	public static int choose;
	public static boolean isEnlarge = false;
	public static int enlargeMoney = 200;
	public static int enlargeNumber = 6;
	public static int remainOpportunity = 2;
	public static int currentLoanNumber = 0;
	public static Loan[] myLoan;	
	public static int index = 0;
	public static int timeCropNumber = 0;
	public static int timeMoneyNumber = 0;
	public static CropTime[] timeCropPlanted;
	public static BorrowTime[] timeMoneyBorrowed;	
	public static int speed = 5;
	public static double speedChange = 1;
	public static boolean isPause = false;
	public static boolean isLoad = false;
	public static boolean isStartAllowed = false;
	public static Day day;
	public static WaterTank waterTank;
	public static boolean plantMore = false;
	public static boolean isSoilInfoSelected = false;
	public static boolean isWaterTankSelected = false;
	public static boolean isCreateSurfaceView = false;	
}
