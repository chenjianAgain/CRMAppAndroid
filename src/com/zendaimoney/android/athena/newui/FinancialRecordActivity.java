package com.zendaimoney.android.athena.newui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zendaimoney.android.athena.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FinancialRecordActivity extends Activity implements
		OnItemClickListener {
	private int index = -1;
	private LayoutInflater inflater;
	private ListView listview;
	private MyAdapter adapter;
	private List<HashMap<String, String>> mapList = null;
	private int[] bgArray = new int[] { R.color.product1_bg,
			R.color.product2_bg, R.color.product3_bg, R.color.product4_bg };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.activity_financial_record);
		listview = (ListView) findViewById(R.id.financial_record_listview);
		mapList = new ArrayList<HashMap<String, String>>();
		initData();
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	private void initData() {
		if (mapList != null && mapList.size() > 0)
			return;
		HashMap<String, String> map = null;
		for (int i = 0; i < 4; i++) {
			map = new HashMap<String, String>();
			map.put("name", "证大年丰");
			map.put("number", "00211312090001-01-170");
			map.put("sum", "20000");
			map.put("state", "质检通过");
			map.put("date", "2014-11-23");
			mapList.add(map);
		}
	}

	private void startAnimation() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.product_show);
		animation.start();
	}


	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mapList.size();
		}

		@Override
		public Object getItem(int position) {
			return mapList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.financial_record_listitem_detail, null);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.nameText = (TextView) convertView
					.findViewById(R.id.product_name);
			holder.numberText = (TextView) convertView
					.findViewById(R.id.product_number);
			holder.nameText.setText(mapList.get(position).get("name"));
			holder.numberText.setText(mapList.get(position).get("number"));

			holder.container = (LinearLayout) convertView
					.findViewById(R.id.container);
			holder.sumText = (TextView) convertView
					.findViewById(R.id.product_sum);
			holder.stateText = (TextView) convertView
					.findViewById(R.id.product_state);
			holder.dateText = (TextView) convertView
					.findViewById(R.id.product_date);
			

			convertView.setBackgroundResource(bgArray[position % 4]);
			convertView.setMinimumHeight(250);
			if (position == index) {
				holder.container.setVisibility(View.VISIBLE);
				holder.container.setBackgroundResource(R.color.produc_more_bg);
				holder.sumText.setText(mapList.get(position).get("sum"));
				holder.stateText.setText(mapList.get(position).get("state"));
				holder.dateText.setText(mapList.get(position).get("date"));
			} else {
				holder.container.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView nameText;
		TextView numberText;
		LinearLayout container;
		TextView sumText;
		TextView stateText;
		TextView dateText;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startAnimation();
		index = position;
		adapter.notifyDataSetChanged();
	}

}
