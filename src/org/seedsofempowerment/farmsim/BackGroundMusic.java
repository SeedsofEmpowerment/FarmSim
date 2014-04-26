/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : AccountAlreadyExistActivity.java
* description : If the user create a new account, and the user account has already existed. 
* 				It will pop up a new window                                
* 
* created by Wang Shiliang at 5/2/2012 21:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Context;
import android.media.MediaPlayer;

//This Class is used for playing the background music 
public class BackGroundMusic {
	private MediaPlayer mediaPlayer;
	
	public BackGroundMusic(Context context, int resid){
		mediaPlayer = MediaPlayer.create(context, resid);
	}
	
	public void play(){
		mediaPlayer.start();
	}
	
	public void stop(){
		mediaPlayer.stop();
	}
	
	public void setLooping(){
		mediaPlayer.setLooping(true);
	}
	
	public void release(){
		mediaPlayer.release();
	}
}
