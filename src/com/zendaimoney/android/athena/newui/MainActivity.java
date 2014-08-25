package com.zendaimoney.android.athena.newui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.zendaimoney.android.athena.R;

public class MainActivity extends FragmentActivity {
	private LayoutInflater layoutInflater;
	private FragmentTabHost mTabHost;
	// 定义数组来存放Fragment界面
	private Class<?> fragmentArray[] = { ChanceFragment.class,
			CustomerFragment.class, ProductFragment.class,
			SettingFragment.class };

	// 定义数组来存放按钮图片
	private int mImageViewArray[] = { R.drawable.tab_chance_selector,
			R.drawable.tab_customer_selector, R.drawable.tab_product_selector,
			R.drawable.tab_setting_selector };

	// Tab选项卡的文字
	private String mTextviewArray[] = { "机会", "客户", "产品", "设置" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);
		initView();

	}

	// 初始化Tab页卡视图
	private void initView() {
		layoutInflater = LayoutInflater.from(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 去掉分隔线
		// mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.bg_tab_2x);
		}
	}

	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}

}
