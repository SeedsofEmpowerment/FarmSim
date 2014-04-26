/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : WaterTank.java
* description : The class for WaterTank
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

public class WaterTank {
	private final int volume = 1000;
	private int currentVolume;
	
	public WaterTank(){
		currentVolume = 0;
	}
	
	public void logic(){
	}
	
	public void setCurrentVolume(int currentVolume){
		this.currentVolume = currentVolume;
	}
	
	public int getCurrentVolume(){
		return this.currentVolume;
	}   
	
	public int getVolume(){
		return this.volume;
	}
}
