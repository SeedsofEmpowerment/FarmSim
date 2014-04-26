/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : Crop.java
* description : The class for Crop
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

//This Class is used for store all the information for the crop
public class Crop {
	private Bitmap[] cropImage;
	public static final int SEED = 0;
	public static final int germinate = 1;
	public static final int SMALLLEAF = 2;
	public static final int BIGLEAF = 3;
	public static final int FLOWER = 4;
	public static final int FRUIT = 5;
	public static final int SERE = 6;
	private boolean isCroped;
	
	private Point coordinate;
	
    private long[] time;
    private int cost;
    private int sale;
    private int state;
    private int cropNumber;
    
    private long startTime;
    private long pauseTime;
    private long currentTime;
    
    private int cropWidth;
    private int cropHeight;
    
    public Crop(){
    	coordinate = new Point(0,0);
    	state = 0;
    	isCroped = false;
    	pauseTime = 0;
    }
    
    public void setPicture(Bitmap[] cropImage){
    	this.cropImage = cropImage;
    	cropWidth = PhoneInfo.getFigureWidth(cropImage[0].getWidth());
    	cropHeight = PhoneInfo.getFigureHeight(cropImage[0].getHeight());
    }
      
    public void setCost(int cost){
    	this.cost = cost;
    }
    
    public void setSale(int sale){
    	this.sale = sale;
    }
    
    public void setTime(long[] time){
    	this.time = time;
    }
    
    public void setCrop(){
    	isCroped = true;
    }
    
    public void setSere(){
    	this.state = SERE;
    }
    
    public boolean isCroped(){
    	return this.isCroped;
    }
    
    public boolean isSered(){
    	if(this.state == SERE){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public void setPoint(int x, int y){
		coordinate.x = x;
		coordinate.y = y;
	}
    
    public void setCropNumber(int cropNumber){
    	this.cropNumber = cropNumber;
    }
    
    public void setStartTime(long startTime){
    	this.startTime = startTime;
    }
    
    public long getStartTime(){
    	return startTime;
    }
    
    public void addPauseTime(long pauseTime){
    	this.pauseTime += pauseTime;
    }
    
    public void setPauseTime(long pauseTime){
    	this.pauseTime = pauseTime;
    }
    
    public long getPauseTime(){
    	return pauseTime;
    }
    
    public int getCost(){
    	return this.cost;
    }
    
    public int getSale(){
    	return this.sale;
    }
    
    public int getState(){
    	return this.state;
    }
    
    public int getCropNumber(){
    	return this.cropNumber;
    }
    
    public void setState(int state){
    	this.state = state;
    }
    
    public void removeCrop(){
    	this.state = 0;
    	this.isCroped = false;
    }
    
    public void logic(){
    	currentTime = System.currentTimeMillis();
    	if(state < 6 && (currentTime - pauseTime - startTime)*GameInfo.speedChange  >= time[state+1]){
    		++state;
    		pauseTime = 0;
    		startTime = currentTime;
    	}
    }
    
    public void draw(Canvas canvas, Paint paint){
    	canvas.drawBitmap(cropImage[state], null, new Rect(coordinate.x,coordinate.y,coordinate.x+cropWidth,coordinate.y+cropHeight),paint);
    }
}
