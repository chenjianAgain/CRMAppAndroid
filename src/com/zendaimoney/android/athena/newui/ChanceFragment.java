package com.zendaimoney.android.athena.newui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.model.CustomerBean;

public class ChanceFragment extends Fragment implements OnCheckedChangeListener {

	private SwipeListView listview;
	private ChanceAdapter adapter;
	private List<CustomerBean> list = new ArrayList<CustomerBean>();
	private RadioGroup group;
	public static int deviceWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		deviceWidth = getDeviceWidth();
		View view = inflater
				.inflate(R.layout.chance_fragment, container, false);
		group = (RadioGroup) view.findViewById(R.id.group_tab);
		group.setOnCheckedChangeListener(this);

		listview = (SwipeListView) view.findViewById(R.id.listview);
		initData();
		adapter = new ChanceAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setSwipeListViewListener(new SwipeListViewListener());
		reload();
		return view;
	}

	// 获取屏幕宽度
	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	private void initData() {
		CustomerBean customer;
		for (int i = 0; i < 10; i++) {
			customer = new CustomerBean();
			customer.setName("张" + getValue(i));
			customer.setGroup("一般");
			customer.setMobile("13800138000");
			list.add(customer);
		}
	}

	private String getValue(int i) {
		String value = "";
		switch (i) {
		case 0:
			value = "一";
			break;
		case 1:
			value = "二";
			break;
		case 2:
			value = "三";
			break;
		case 3:
			value = "四";
			break;
		case 4:
			value = "五";
			break;
		case 5:
			value = "六";
			break;
		case 6:
			value = "七";
			break;
		case 7:
			value = "八";
			break;
		case 8:
			value = "九";
			break;
		case 9:
			value = "十";
			break;

		default:
			break;
		}
		return value;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_btn_general:
			break;
		case R.id.radio_btn_intensity:
			break;
		case R.id.radio_btn_like:
			break;
		case R.id.radio_btn_file:
			break;
		default:
			break;
		}
	}

	class SwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			Toast.makeText(getActivity(), list.get(position).getName(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				list.remove(position);
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void reload() {
		listview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		listview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		listview.setOffsetLeft(deviceWidth * 1 / 2);
		listview.setAnimationTime(0);
		listview.setSwipeOpenOnLongPress(false);
	}

}
