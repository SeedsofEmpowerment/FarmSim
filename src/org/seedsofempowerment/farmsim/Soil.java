/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : Soil.java
* description : The class for Soil
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Soil {
	public static final int WASTELAND = 0;
	public static final int ASSART = 1; 
	public static final int ASSARTPRESS = 2;
	public static final int ENLARGE = 3;
	public static final int ENLARGEPRESS = 4;
	public static final int DROUGHT = 5;
	public static final int DROUGHTPRESS = 6;
	public static final int FLOOD = 7;
	public static final int FLOODPRESS = 8;
	public static final int WEAKCULTIVATED = 9;
	public static final int WEAKCULTIVATEDPRESS = 10;
	
	private int state =  WASTELAND;
	private int soilWidth;
	private int soilHeight;
	public int humidity;
	public int nutrition;
	public int droughtDay;
	public int delugeDay;
	private Point coordinate;
	private Bitmap nullSoil;
	private Bitmap emptySoil;
	private Bitmap emptySoilLight;
	private Bitmap addSoil;
	private Bitmap addSoilLight;
	private Bitmap droughtSoil;
	private Bitmap droughtSoilLight;
	private Bitmap floodSoil;
	private Bitmap floodSoilLight;
	private Bitmap weakCultivated;
	private Bitmap weakCultivatedLight;
	
	public Soil(Bitmap[] soils){
		coordinate = new Point(0,0);
		this.nullSoil = soils[0];
		this.emptySoil = soils[1];
		this.emptySoilLight = soils[2];
		this.addSoil = soils[3];
		this.addSoilLight = soils[4];
		this.droughtSoil = soils[5];
		this.droughtSoilLight = soils[6];
		this.floodSoil = soils[7];
		this.floodSoilLight = soils[8];
		this.weakCultivated = soils[9];
		this.weakCultivatedLight = soils[10];
		soilWidth = PhoneInfo.getFigureWidth(soils[0].getWidth());
		soilHeight = PhoneInfo.getFigureHeight(soils[0].getHeight());
		humidity = 50;
		nutrition = 100;
		droughtDay = 0;
		delugeDay = 0;
	}
	
	public void setPoint(int x, int y){
		coordinate.x = x;
		coordinate.y = y;
	}
	
	public Point getPoint(){
		return new Point(coordinate.x, coordinate.y);
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public void setAssart(){
		this.state = ASSART;
	}
	
	public void setExtend(){
		this.state = ENLARGE;
	}
	
	public int getState(){
		return this.state;
	}
	
	public boolean isAssarted(){
		if(state == ASSART || state == ASSARTPRESS || state == DROUGHT || state == DROUGHTPRESS 
				|| state == WEAKCULTIVATED || state == WEAKCULTIVATEDPRESS || state == FLOOD || state == FLOODPRESS){
			return true;
		}
		return false;
	}
	
	public void setLight(){
		if(this.state == ENLARGE){
			this.state = ENLARGEPRESS;
		}
		else if(this.state == ASSART){
			this.state = ASSARTPRESS;
		}
		else if(this.state == DROUGHT){
			this.state = DROUGHTPRESS;
		}
		else if(this.state == FLOOD){
			this.state = FLOODPRESS;
		}
		else if(this.state == WEAKCULTIVATED){
			this.state = WEAKCULTIVATEDPRESS;
		}
	}
	
	public void cancelLight(){
		if(this.state == ENLARGEPRESS){
			this.state = ENLARGE;
		}
		else if(this.state == ASSARTPRESS){
			this.state = ASSART;
		}
		else if(this.state == DROUGHTPRESS){
			this.state = DROUGHT;
		}
		else if(this.state == FLOODPRESS){
			this.state = FLOOD;
		}
		else if(this.state == WEAKCULTIVATEDPRESS){
			this.state = WEAKCULTIVATED;
		}
	}
	
	public void becomeSunny(){
		humidity -= humidity * 0.1;
	}
	
	public void becomeCloudy(){
		humidity -= humidity * 0.05;
	}
	
	public void becomeRainny(){
		humidity += 20;
		if(humidity > 100){
			humidity = 100;
		}
	}
	
	public void becomeStorm(){
		humidity += 50;
		if(humidity > 100){
			humidity = 100;
		}
	}
	
	public void draw(Canvas canvas, Paint paint){
		if(state == WASTELAND){
			canvas.drawBitmap(nullSoil, null, new Rect(coordinate.x,coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight),paint);
		}
		else if(state == ASSART){
			canvas.drawBitmap(emptySoil, null, new Rect(coordinate.x,coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight),paint);
		} 
		else if(state == ASSARTPRESS) {
			canvas.drawBitmap(emptySoilLight, null, new Rect(coordinate.x,coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight),paint);
		}
		else if(state == ENLARGE) {
			canvas.drawBitmap(addSoil, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight),paint);
		}
		else if(state == ENLARGEPRESS) {
			canvas.drawBitmap(addSoilLight, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == DROUGHT) {
			canvas.drawBitmap(droughtSoil, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == DROUGHTPRESS){
			canvas.drawBitmap(droughtSoilLight, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == FLOOD){
			canvas.drawBitmap(floodSoil, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == FLOODPRESS){
			canvas.drawBitmap(floodSoilLight, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == WEAKCULTIVATED){
			canvas.drawBitmap(weakCultivated, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
		else if(state == WEAKCULTIVATEDPRESS){
			canvas.drawBitmap(weakCultivatedLight, null, new Rect(coordinate.x, coordinate.y,coordinate.x+soilWidth,coordinate.y+soilHeight), paint);
		}
	}
}
