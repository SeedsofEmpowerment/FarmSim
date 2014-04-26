/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : SoilInfoActivity.java
* description : The class for SoilInfoActivity
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

//This activity is called when the soil press the soil to look up the soil information
public class SoilInfoActivity extends Activity{
public static Activity instance;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //hidden the battery flag and any other parts
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hidden the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.soilinfolayout);
    }
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(SoilInfoActivity.instance, GameEngineActivity.class);
		SoilInfoActivity.instance.finish();
		GameInfo.isSoilInfoSelected = false;
	}
}
