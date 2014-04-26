/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : SoundEffect.java
* description : The class for sound effect
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

//This class is used for playing the sound effect of this game
public class SoundEffect {
	private SoundPool sp;
	private int soundId;
	
	public SoundEffect(Context context, int resid){
		sp = new SoundPool(4, AudioManager.STREAM_MUSIC,100);
		soundId = sp.load(context, resid, 1);
	}
	
	public void play(float volume){
		sp.play(soundId, volume, volume, 1, 0, 1);
	}
	
	public void stop(){
		sp.stop(soundId);
	}
	
	public void release(){
		sp.release();
	}
}
