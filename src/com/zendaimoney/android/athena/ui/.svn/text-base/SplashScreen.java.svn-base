package com.zendaimoney.android.athena.ui;

import com.zendaimoney.android.athena.MainActivity;
import com.zendaimoney.android.athena.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 启动界面
 * @author admin
 *
 */
public class SplashScreen extends Activity {
	public int useTime;
	Intent mainIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashview);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashScreen.this,
						MainActivity.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();
			}
		}, 2500); // 2500 for release
	}

	
}
