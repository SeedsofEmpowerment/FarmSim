/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : Vibrate.java
* description : The class for Vibrate
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Context;
import android.os.Vibrator;

//This class is used for playing the vibrate
public class Vibrate {
	final static String TAG = "GameEngine";
	long[] pattern = {0, 50};
	private Vibrator vibrator;
	public Vibrate(Context context){
		vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
	}
	
	public void playVibrate(int times){
		vibrator.vibrate(pattern, times);
	}
	
	public void Stop(){
		vibrator.cancel();
	}
}
