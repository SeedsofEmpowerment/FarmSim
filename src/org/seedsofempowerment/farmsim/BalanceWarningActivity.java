/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : BalanceWarningActivity.java
* description : If the user's balance is below zero, it will pop up a new window.                                
* 
* created by Wang Shiliang at 5/1/2012 21:19:50
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
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class BalanceWarningActivity extends Activity{
	public static Activity instance;
	private Button okButton;
	private TextView tv;
	private PrintWriter out;
	private DataOutputStream dos;
	private FileInputStream fis;
	private DataInputStream dis;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//hidden the battery flag and any other parts
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//hidden the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.balancewarninglayout);
		tv = (TextView)findViewById(R.id.tv);
		tv.setTextColor(Color.BLACK);
		if(GameInfo.remainOpportunity == 2){
			tv.setText("Your balance is below 0. You have only two more chances to take out an additional loan.");
		}
		else if(GameInfo.remainOpportunity == 1){
			tv.setText("Your balance is below 0 again. You have only one last chance to take out an additional loan.");
		}
		else if(GameInfo.remainOpportunity == 0){
			tv.setText("You have defauted and the game is over");
		}
		okButton = (Button) findViewById(R.id.okbutton);
		okButton.setOnTouchListener(new OnTouchListener(){
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					GameInfo.vibrate.playVibrate(-1);
 					GameInfo.soundEffect[0].play((float) 0.3);
 				}
 				else if(event.getAction() == MotionEvent.ACTION_UP){
 					if(GameInfo.remainOpportunity == 2||GameInfo.remainOpportunity == 1){
 						--(GameInfo.remainOpportunity);
						Intent intent = new Intent();
						intent.setClass(BalanceWarningActivity.this, LoanActivity.class);
						BalanceWarningActivity.this.startActivity(intent);
 					}
 					else{
 						--(GameInfo.remainOpportunity);
 						//Save the data to file
 						try{
 							//judge whether exist the SD card
 							if(Environment.getExternalStorageState() != null && !Environment.getExternalStorageState().equals("removed")){
 								Log.v("wang","exist sd card");
 								//declare the path
 								File path = new File("/sdcard/FarmSim/performance");
 								if(!path.exists()){
 									path.mkdirs();
 								}
 								//create the store file
 								File file = new File("/sdcard/FarmSim/performance/"+GameInfo.user.getName()+".txt");
 								if(!file.exists()){
 									file.createNewFile();
 									FileOutputStream fos = new FileOutputStream(file,true);
 									out = new PrintWriter(fos);
 									out.printf("Name:                    %s\r\n", GameInfo.user.getName());
 									if(GameInfo.user.getGender() == 0){
 										out.printf("Gender:                  %s\r\n", "Boy");
 									}
 									else if(GameInfo.user.getGender() == 1){
 										out.printf("Gender:                  %s\r\n", "Girl");
 									}
 									else{
 										out.printf("Gender:                  %s\r\n", "Not mention");
 									}
 									out.printf("Age:                     %d\r\n", GameInfo.user.getAge());
 								}
 								else{
 									FileOutputStream fos = new FileOutputStream(file,true);
 									out = new PrintWriter(fos);
 								}
 								out.printf("Money Borrowed:\r\n");
 								for(int i = 0;i!=GameInfo.timeMoneyNumber;++i){
 									out.printf("%d, %d", GameInfo.timeMoneyBorrowed[i].time, GameInfo.timeMoneyBorrowed[i].money);
 									out.printf("\t");
 								}
 								out.printf("\r\n");
 								out.printf("Crop planted:\r\n");
 								for(int i = 0;i!=GameInfo.timeCropNumber;++i){
 									out.printf("%d, %s", GameInfo.timeCropPlanted[i].time, GameInfo.timeCropPlanted[i].crop);
 									out.printf("\t");
 								}
 								out.printf("\r\n");
 								long currentTime = System.currentTimeMillis();
 								long time = (currentTime - GameInfo.startTime) / 1000;
 								out.printf("Total time:              %d\r\n", time);
 								out.printf("Final balance:           %d\r\n", (int)(GameInfo.money));
 								out.printf("\r\n\r\n\r\n"); 
 								out.close();
 								
 								//store the score
 								path = new File("/sdcard/FarmSim/leaderboard");
 								if(!path.exists()){
 									path.mkdirs();
 								}
 								//create the store file
 								file = new File("/sdcard/FarmSim/leaderboard/"+"leaderboard.txt");
 								if(!file.exists()){								
 									file.createNewFile();
 									FileOutputStream fos = new FileOutputStream(file,true);
 									out = new PrintWriter(fos);
 									dos = new DataOutputStream(fos);
 									dos.writeInt(1);
 									dos.writeUTF(GameInfo.user.getName());
 									//dos.writeChar('\n');
 									dos.writeInt(GameInfo.user.getAge());
 									int balance = (int) GameInfo.money;
 									if(GameInfo.currentLoanNumber > 0){
 										for(int i = 0;i!=3;++i){
 											if(GameInfo.myLoan[i].getMonthLeft() > 0){
 												balance -= GameInfo.myLoan[i].getMonthLeft() * GameInfo.myLoan[i].getMonthPaid();
 											}
 										}
 									}
 									GameInfo.user.setBalance(balance);
 									dos.writeInt(balance);
 									currentTime = System.currentTimeMillis();
 									GameInfo.user.setTime(currentTime);
 									dos.writeDouble((currentTime - GameInfo.startTime)*1.0 / 60000);
 									dos.writeInt((int) GameInfo.money);
 									dos.writeInt(GameInfo.user.getBorrowed());
 									dos.writeInt(GameInfo.user.getScore());
 								}
 								else{
 									//read the scoreboard to the array first
 									fis = new FileInputStream(file);
 									dis = new DataInputStream(fis);
 									int number = dis.readInt();
 									User[] user = new User[number+1];
 									for(int i = 0;i!=number;++i){
 										user[i] = new User();
 										user[i].setName(dis.readUTF());
 										user[i].setAge(dis.readInt());
 										user[i].setBalance(dis.readInt());
 										user[i].setTime(dis.readDouble());
 										user[i].setTotalEarning(dis.readInt());
 										user[i].setBorrowed(dis.readInt());
 										user[i].setScore(dis.readInt());
 									}
 									user[number] = new User();
 									user[number].setName(GameInfo.user.getName());
 									user[number].setAge(GameInfo.user.getAge());
 									int balance = (int) GameInfo.money;
 									if(GameInfo.currentLoanNumber > 0){
 										for(int i = 0;i!=3;++i){
 											if(GameInfo.myLoan[i].getMonthLeft() > 0){
 												balance -= GameInfo.myLoan[i].getMonthLeft() * GameInfo.myLoan[i].getMonthPaid();
 											}
 										}
 									}
 									GameInfo.user.setBalance(balance);
 									user[number].setBalance(balance);
 									currentTime = System.currentTimeMillis();
 									double timeMin = (currentTime - GameInfo.startTime)*1.0 / 60000;
 									GameInfo.user.setTime(timeMin);
 									user[number].setTime(timeMin);
 									user[number].setTotalEarning((int) GameInfo.money);
 									user[number].setBorrowed(GameInfo.user.getBorrowed());
 									user[number].setScore(GameInfo.user.getScore());
 									//insert this user to the user list
 									for(int i = 0;i!=number;++i){
 										if(user[number].getScore() >= user[i].getScore()){
 											User temp = user[number];
 											for(int j = number;j!=i;--j){
 												user[j] = user[j-1];
 											}
 											user[i] = temp;
 										}
 									}
 									fis.close();
 									dis.close();
 									//store the userInfo to the leaderBoard
 									FileOutputStream fos = new FileOutputStream(file,false);
 									out = new PrintWriter(fos);
 									dos = new DataOutputStream(fos);
 									if(number < 5){
 										++number;
 									}
 									dos.writeInt(number);
 									for(int i = 0;i!=number;++i){	
 										dos.writeUTF(user[i].getName());
 										//dos.writeChar('\n');
 										dos.writeInt(user[i].getAge());
 										dos.writeInt(user[i].getBalance());
 										dos.writeDouble(user[i].getTime());
 										dos.writeInt(user[i].getTotalEarning());
 										dos.writeInt(user[i].getBorrowed());
 										dos.writeInt(user[i].getScore());
 									}
 									out.close();
 									dos.close();
 								}
 							}	
 						}	
 						catch(FileNotFoundException e) {
 							e.printStackTrace();
 						}
 						catch(IOException e){
 							e.printStackTrace();
 						}
 						finally{
 							try{
 								if(out != null)
 									out.close();
 								if(out != null)
 									out.close();
 							}
 							catch(Exception e){
 								e.printStackTrace();
 							}
 						}
 						System.exit(0);
 					}
 				}
				return false;
 			}
         });
	}
}
