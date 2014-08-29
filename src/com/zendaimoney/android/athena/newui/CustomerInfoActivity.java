package com.zendaimoney.android.athena.newui;

import com.zendaimoney.android.athena.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomerInfoActivity extends Activity implements OnClickListener{
	private TextView customerNameText;
	private TextView customerMobileText;
	private ImageView customer_ico;
	
	private RelativeLayout licaiLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_info);

		customerNameText = (TextView) findViewById(R.id.customer_name);
		customerMobileText = (TextView) findViewById(R.id.customer_mobile);
		customer_ico = (ImageView) findViewById(R.id.customer_big_icon);
		
		licaiLayout = (RelativeLayout)findViewById(R.id.licai_layout);
		licaiLayout.setOnClickListener(this);
		
		Intent intent = getIntent();

		if (intent != null) {

			customerNameText.setText(intent.getStringExtra("name"));
			customerMobileText.setText(intent.getStringExtra("mobile"));
			customer_ico
					.setImageResource(intent.getIntExtra("sex", 0) == 0 ? R.drawable.ico_woman_big_2x
							: R.drawable.ico_man_big_2x);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.licai_layout:
			intent = new Intent(this, FinancialRecordActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
