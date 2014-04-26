/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : LoanSurfaceView.java
* description : The class for LoanSurfaceView
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

public class LoanSurfaceView extends SurfaceView implements Callback, Runnable{
	public static LoanSurfaceView myView;
	public static int screenW, screenH;
	private Canvas canvas;
	private SurfaceHolder sfh;
	private Paint paint;
	
	private Resources res = this.getResources();
	private Bitmap loanBackGround;
	private Bitmap loanEnsureBackGround;
	private Bitmap haveLoanBackGround;
	private Bitmap[] loanButtons;
	private Bitmap[] loanEnsureButtons;
	private Bitmap[] haveLoanButtons;
	private int[] loanButtonX;
	private int[] loanButtonY;
	private int[] balanceLoanButtonX;
	private int[] balanceLoanButtonY;
	private int loanButtonWidth;
	private int loanButtonHeight;
	private int[] loanEnsureButtonX;
	private int[] loanEnsureButtonY;
	private int loanEnsureButtonWidth;
	private int loanEnsureButtonHeight;
	private int[] haveLoanButtonX;
	private int[] haveLoanButtonY;
	private int haveLoanButtonWidth;
	private int haveLoanButtonHeight;
	private int[] loanTextX;
	private int[] loanTextY;
	private int loanNumber;
	public static final int LOAN = 1;
	public static final int ENSURELOAN = 2;
	public static final int HAVELOAN = 3;
	public static final int BALANCEWARNING = 4;
	public static int gameState;
	private int chooseLoan;
	
	public LoanSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		gameState = LOAN;
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
		releaseResource();		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//get the point where user touch
		int pointX = (int)event.getX();
		int pointY = (int)event.getY();
		
		switch (gameState){
		case LOAN:
			if(pointX > loanButtonX[0] && pointX < loanButtonX[0] + PhoneInfo.getFigureWidth(loanButtons[0].getWidth())){
				if(pointY > loanButtonY[0] && pointY < loanButtonY[0] + PhoneInfo.getFigureHeight(loanButtons[0].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 100;
						gameState = ENSURELOAN;
					}
				}
			}
			if(pointX > loanButtonX[1] && pointX < loanButtonX[1] + PhoneInfo.getFigureWidth(loanButtons[1].getWidth())){
				if(pointY > loanButtonY[1] && pointY < loanButtonY[1] + PhoneInfo.getFigureHeight(loanButtons[1].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 300;
						gameState = ENSURELOAN;
					}
				}
			}
			if(pointX > loanButtonX[2] && pointX < loanButtonX[2] + PhoneInfo.getFigureWidth(loanButtons[2].getWidth())){
				if(pointY > loanButtonY[2] && pointY < loanButtonY[2] + PhoneInfo.getFigureHeight(loanButtons[2].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 500;
						gameState = ENSURELOAN;
					}
				}
			}
			if(pointX > loanButtonX[3] && pointX < loanButtonX[3] + PhoneInfo.getFigureWidth(loanButtons[3].getWidth())){
				if(pointY > loanButtonY[3] && pointY < loanButtonY[3] + PhoneInfo.getFigureHeight(loanButtons[3].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						Intent intent = new Intent();
						intent.setClass(LoanActivity.instance, GameEngineActivity.class);
						LoanActivity.instance.finish();
					}
				}
			}
			break;
		case ENSURELOAN:
			if(pointX > loanEnsureButtonX[0] && pointX < loanEnsureButtonX[0] + PhoneInfo.getFigureWidth(loanEnsureButtons[0].getWidth())){
				if(pointY > loanEnsureButtonY[0] && pointY < loanEnsureButtonY[0] + PhoneInfo.getFigureHeight(loanEnsureButtons[0].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						++(GameInfo.currentLoanNumber);
						GameInfo.money += chooseLoan;
						long currentTime = System.currentTimeMillis();
						long time = (currentTime - GameInfo.startTime) / 1000;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].money = chooseLoan;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].time = time;
						++(GameInfo.timeMoneyNumber);
						GameInfo.myLoan[GameInfo.index] = new Loan(chooseLoan, 0.1, 12);
						GameInfo.user.setBorrowed(GameInfo.user.getBorrowed() + chooseLoan);
						GameInfo.myLoan[GameInfo.index].setLoanDay(GameInfo.day.getDay());
						GameInfo.myLoan[GameInfo.index].setLoanMonth(GameInfo.day.getMonth());
						LoanActivity.instance.finish();
						GameInfo.index = 0;
					}
				}
			}
			if(pointX > loanEnsureButtonX[1] && pointX < loanEnsureButtonX[1] + PhoneInfo.getFigureWidth(loanEnsureButtons[1].getWidth())){
				if(pointY > loanEnsureButtonY[1] && pointY < loanEnsureButtonY[1] + PhoneInfo.getFigureHeight(loanEnsureButtons[1].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						LoanActivity.instance.finish();
					}
				}
			}
			break;
		case HAVELOAN:
			int arrowLeftX = PhoneInfo.getRealWidth(10);
			int arrowLeftY = PhoneInfo.getRealHeight(160);
			int arrowRightX = PhoneInfo.getRealWidth(410);
			int arrowRightY = PhoneInfo.getRealHeight(160);
			int arrowLength = PhoneInfo.getRealWidth(80);
			int arrowHeight = PhoneInfo.getRealHeight(80);
			if(pointX > haveLoanButtonX[0] && pointX < haveLoanButtonX[0] + PhoneInfo.getFigureWidth(haveLoanButtons[0].getWidth())){
				if(pointY > haveLoanButtonY[0] && pointY < haveLoanButtonY[0] + PhoneInfo.getFigureHeight(haveLoanButtons[0].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						LoanActivity.instance.finish();
					}
				}
			}
			//if press the left arrow button
			if(pointX > arrowLeftX && pointX < arrowLeftX + arrowLength){
				if(pointY > arrowLeftY && pointY < arrowLeftY + arrowHeight){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						--loanNumber;
						if(loanNumber < 0){
							loanNumber = GameInfo.currentLoanNumber - 1;
						}		
					}
				}
			}
			//if press the right arrow button
			if(pointX > arrowRightX && pointX < arrowRightX + arrowLength){
				if(pointY > arrowRightY && pointY < arrowRightY + arrowHeight){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						++loanNumber;
						if(loanNumber == GameInfo.currentLoanNumber){
							loanNumber = 0;
						}
					}
				}
			}
			break;
		case BALANCEWARNING:
			if(pointX > balanceLoanButtonX[0] && pointX < balanceLoanButtonX[0] + PhoneInfo.getFigureWidth(loanButtons[0].getWidth())){
				if(pointY > balanceLoanButtonY[0] && pointY < balanceLoanButtonY[0] + PhoneInfo.getFigureHeight(loanButtons[0].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 100;
						++(GameInfo.currentLoanNumber);
						GameInfo.money += chooseLoan;
						long currentTime = System.currentTimeMillis();
						long time = (currentTime - GameInfo.startTime) / 1000;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].money = chooseLoan;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].time = time;
						++(GameInfo.timeMoneyNumber);
						GameInfo.myLoan[GameInfo.index] = new Loan(chooseLoan, 0.1, 12);
						GameInfo.user.setBorrowed(GameInfo.user.getBorrowed() + chooseLoan);
						GameInfo.myLoan[GameInfo.index].setLoanDay(GameInfo.day.getDay());
						GameInfo.myLoan[GameInfo.index].setLoanMonth(GameInfo.day.getMonth());
						LoanActivity.instance.finish();
						BalanceWarningActivity.instance.finish();
						GameInfo.index = 0;
					}
				}
			}
			if(pointX > balanceLoanButtonX[1] && pointX < balanceLoanButtonX[1] + PhoneInfo.getFigureWidth(loanButtons[1].getWidth())){
				if(pointY > balanceLoanButtonY[1] && pointY < balanceLoanButtonY[1] + PhoneInfo.getFigureHeight(loanButtons[1].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 300;
						++(GameInfo.currentLoanNumber);
						GameInfo.money += chooseLoan;
						long currentTime = System.currentTimeMillis();
						long time = (currentTime - GameInfo.startTime) / 1000;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].money = chooseLoan;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].time = time;
						++(GameInfo.timeMoneyNumber);
						GameInfo.myLoan[GameInfo.index] = new Loan(chooseLoan, 0.1, 12);
						GameInfo.user.setBorrowed(GameInfo.user.getBorrowed() + chooseLoan);
						GameInfo.myLoan[GameInfo.index].setLoanDay(GameInfo.day.getDay());
						GameInfo.myLoan[GameInfo.index].setLoanMonth(GameInfo.day.getMonth());
						LoanActivity.instance.finish();
						BalanceWarningActivity.instance.finish();
						GameInfo.index = 0;
					}
				}
			}
			if(pointX > balanceLoanButtonX[2] && pointX < balanceLoanButtonX[2] + PhoneInfo.getFigureWidth(loanButtons[2].getWidth())){
				if(pointY > balanceLoanButtonY[2] && pointY < balanceLoanButtonY[2] + PhoneInfo.getFigureHeight(loanButtons[2].getHeight())){
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						GameInfo.vibrate.playVibrate(-1);
					}
					else if(event.getAction() == MotionEvent.ACTION_UP){
						chooseLoan = 500;
						++(GameInfo.currentLoanNumber);
						GameInfo.money += chooseLoan;
						long currentTime = System.currentTimeMillis();
						long time = (currentTime - GameInfo.startTime) / 1000;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].money = chooseLoan;
						GameInfo.timeMoneyBorrowed[GameInfo.timeMoneyNumber].time = time;
						++(GameInfo.timeMoneyNumber);
						GameInfo.myLoan[GameInfo.index] = new Loan(chooseLoan, 0.1, 12);
						GameInfo.user.setBorrowed(GameInfo.user.getBorrowed() + chooseLoan);
						GameInfo.myLoan[GameInfo.index].setLoanDay(GameInfo.day.getDay());
						GameInfo.myLoan[GameInfo.index].setLoanMonth(GameInfo.day.getMonth());
						LoanActivity.instance.finish();
						BalanceWarningActivity.instance.finish();
						GameInfo.index = 0;
					}
				}
			}
		}				
		myDraw();
		return true;
	}
	
	public void recycleResource(Bitmap picture){
		if(picture != null){
			picture.recycle();
		}
	}
	
	public void releaseResource(){
		recycleResource(loanBackGround);
		recycleResource(loanEnsureBackGround);
		recycleResource(haveLoanBackGround);

		for(int i = 0;i!=4;++i){
			recycleResource(loanButtons[i]);
		}
		for(int i = 0;i!=2;++i){
			recycleResource(loanEnsureButtons[i]);
		}
		recycleResource(haveLoanButtons[0]);
	}
	
	public void myDraw(){
		try{
			canvas = sfh.lockCanvas();
			if(canvas != null){
				canvas.drawColor(Color.BLACK);
				switch (gameState){
				case LOAN:
					canvas.drawBitmap(loanBackGround, null, new Rect(0,0,screenW,screenH) ,paint);
					for(int i = 0;i!=4;++i){
						canvas.drawBitmap(loanButtons[i],null,new Rect(loanButtonX[i],loanButtonY[i],loanButtonX[i]+loanButtonWidth,loanButtonY[i]+loanButtonHeight),paint);
					}
					break;
				case ENSURELOAN:
					canvas.drawBitmap(loanEnsureBackGround, null, new Rect(0,0,PhoneInfo.getRealWidth(500),PhoneInfo.getRealHeight(360)) ,paint);
					for(int i = 0;i!=2;++i){
						canvas.drawBitmap(loanEnsureButtons[i],null,new Rect(loanEnsureButtonX[i],loanEnsureButtonY[i],loanEnsureButtonX[i] + loanEnsureButtonWidth, loanEnsureButtonY[i] + loanEnsureButtonHeight), paint);
					}
					//draw the text
					paint = new Paint();
					paint.setColor(Color.WHITE);
					String familyName = "Arial";
					Typeface font = Typeface.create(familyName,Typeface.NORMAL);
					paint.setTypeface(font);
					float textSize = (float) (20 * PhoneInfo.heightRatio);
					paint.setTextSize(textSize);
					String[] text = new String[4];
					text[0] = "Loan amount: $" + chooseLoan;
					text[1] = "Interest: 10%";
					double totalAmount = chooseLoan + chooseLoan * 0.1;
					totalAmount = Loan.roundChange(totalAmount, 1);
					double monthPaid = totalAmount / 12;
					monthPaid = Loan.roundChange(monthPaid, 1);
					text[2] = "Total Amount: $" + totalAmount;
					text[3] = "Monthly payment: $" + monthPaid;
					for(int i = 0;i!=4;++i){
						canvas.drawText(text[i], loanTextX[i], loanTextY[i], paint);
					}
					break;
				case HAVELOAN:
					canvas.drawBitmap(haveLoanBackGround, null, new Rect(0,0,screenW,screenH), paint);
					for(int i = 0;i!=1;++i){
						canvas.drawBitmap(haveLoanButtons[i], null, new Rect(haveLoanButtonX[i], haveLoanButtonY[i], haveLoanButtonX[i] + haveLoanButtonWidth, haveLoanButtonY[i] + haveLoanButtonHeight), paint);
					}
					//draw the text
					paint = new Paint();
					paint.setColor(Color.WHITE);
					String familyName2 = "Arial";
					Typeface font2 = Typeface.create(familyName2,Typeface.NORMAL);
					paint.setTypeface(font2);
					paint.setTextSize((float) (20 * PhoneInfo.heightRatio));
					int number = 0;
					int index = 0;
					for(int i = 0;i!=3;++i){
						if(GameInfo.myLoan[i].getMonthLeft() > 0){
							if(number == loanNumber){	
								index = i;
								break;
							}
							++number;
						}
					}							
					String[] text2 = new String[3];
					text2[0] = "Amount Borrowed: $" + GameInfo.myLoan[index].getPrinciple();
					text2[1] = "Amount Paid Back: $" + GameInfo.myLoan[index].getAmountPaidBack();
					text2[2] = "Monthly Payment: $" + GameInfo.myLoan[index].getMonthPaid();
					for(int j = 0;j!=3;++j){
						canvas.drawText(text2[j], loanTextX[j] + PhoneInfo.getFigureWidth(50), loanTextY[j], paint);
					}
					break;
				case BALANCEWARNING:
					canvas.drawBitmap(loanBackGround, null, new Rect(0,0,screenW,screenH),paint);
					for(int i = 0;i!=3;++i){
						canvas.drawBitmap(loanButtons[i],null,new Rect(balanceLoanButtonX[i],balanceLoanButtonY[i],balanceLoanButtonX[i]+loanButtonWidth,balanceLoanButtonY[i]+loanButtonHeight),paint);
					}				
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
		loanBackGround = BitmapFactory.decodeResource(res, R.drawable.loanview);
		loanEnsureBackGround = BitmapFactory.decodeResource(res, R.drawable.loanensure);
		haveLoanBackGround = BitmapFactory.decodeResource(res, R.drawable.haveloan);
		loanButtons = new Bitmap[4];
		loanButtons[0] = BitmapFactory.decodeResource(res, R.drawable.onehundred);
		loanButtons[1] = BitmapFactory.decodeResource(res, R.drawable.threehundred);
		loanButtons[2] = BitmapFactory.decodeResource(res, R.drawable.fivehundred);
		loanButtons[3] = BitmapFactory.decodeResource(res, R.drawable.back_button);
		loanEnsureButtons = new Bitmap[2];
		loanEnsureButtons[0] = BitmapFactory.decodeResource(res, R.drawable.yes_button);
		loanEnsureButtons[1] = BitmapFactory.decodeResource(res, R.drawable.no_button);
		haveLoanButtons = new Bitmap[1];
		haveLoanButtons[0] = BitmapFactory.decodeResource(res, R.drawable.ok_button);
		loanButtonX = new int[4];
		loanButtonX[0] = PhoneInfo.getRealWidth(22);
		loanButtonX[1] = loanButtonX[0] + PhoneInfo.getFigureWidth(loanButtons[0].getWidth()) + PhoneInfo.getRealWidth(15);
		loanButtonX[2] = loanButtonX[1] + PhoneInfo.getFigureWidth(loanButtons[1].getWidth()) + PhoneInfo.getRealWidth(15);
		loanButtonX[3] = loanButtonX[1];
		balanceLoanButtonX = new int[3];
		balanceLoanButtonX[0] = loanButtonX[0];
		balanceLoanButtonX[1] = loanButtonX[1];
		balanceLoanButtonX[2] = loanButtonX[2];
		
		
		loanButtonWidth = PhoneInfo.getFigureWidth(loanButtons[0].getWidth());
		loanButtonY = new int[4];
		loanButtonY[0] = (PhoneInfo.getRealHeight(360)) / 2;
		loanButtonY[1] = loanButtonY[0];
		loanButtonY[2] = loanButtonY[0];
		loanButtonY[3] = (PhoneInfo.getRealHeight(360)) / 4 * 3;
		balanceLoanButtonY = new int[3];
		balanceLoanButtonY[0] = loanButtonY[0] + PhoneInfo.getRealHeight(50);
		balanceLoanButtonY[1] = loanButtonY[1] + PhoneInfo.getRealHeight(50);
		balanceLoanButtonY[2] = loanButtonY[2] + PhoneInfo.getRealHeight(50);
		loanButtonHeight = PhoneInfo.getFigureHeight(loanButtons[0].getHeight());
		
		loanEnsureButtonX = new int[2];
		loanEnsureButtonY = new int[2];
		loanEnsureButtonX[0] = PhoneInfo.getRealWidth(72);
		loanEnsureButtonX[1] = loanEnsureButtonX[0] + PhoneInfo.getFigureWidth(loanEnsureButtons[0].getWidth()) + PhoneInfo.getRealWidth(72);
		loanEnsureButtonY[0] = (PhoneInfo.getRealHeight(360)) / 6 * 5;
		loanEnsureButtonY[1] = loanEnsureButtonY[0];	
		loanEnsureButtonWidth = PhoneInfo.getFigureWidth(loanEnsureButtons[0].getWidth());
		loanEnsureButtonHeight = PhoneInfo.getFigureHeight(loanEnsureButtons[0].getHeight());
		haveLoanButtonX = new int[1];
		haveLoanButtonY = new int[1];
		haveLoanButtonWidth = PhoneInfo.getFigureWidth(haveLoanButtons[0].getWidth());
		haveLoanButtonHeight = PhoneInfo.getFigureHeight(haveLoanButtons[0].getHeight());
		haveLoanButtonX[0] = PhoneInfo.getRealWidth(179);
		haveLoanButtonY[0] = PhoneInfo.getRealWidth(360) / 6 * 5;
		loanTextX = new int[4];
		loanTextY = new int[4];
		loanTextX[0] = PhoneInfo.getRealWidth(50);
		loanTextX[1] = PhoneInfo.getRealWidth(50);
		loanTextX[2] = PhoneInfo.getRealWidth(50);
		loanTextX[3] = PhoneInfo.getRealWidth(50);
		loanTextY[0] = PhoneInfo.getRealHeight(150);
		loanTextY[1] = PhoneInfo.getRealHeight(190);
		loanTextY[2] = PhoneInfo.getRealHeight(230);
		loanTextY[3] = PhoneInfo.getRealHeight(270);
		loanNumber = 0;
		if(GameInfo.currentLoanNumber==0){
			gameState = LOAN;
		}
		else if(GameInfo.index != 0){
			gameState = BALANCEWARNING;
		}
		else{
			gameState = HAVELOAN;
		}
	}
}
