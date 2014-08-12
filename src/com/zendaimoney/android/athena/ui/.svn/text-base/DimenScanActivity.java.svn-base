package com.zendaimoney.android.athena.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.zendaimoney.android.athena.ExitApplication;
import com.zendaimoney.android.athena.R;

public class DimenScanActivity extends Activity{
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scannerdimen);
		initContentView();
		ExitApplication.getInstance().addActivity(this); 
	}
	
	public void initContentView(){
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}
}
