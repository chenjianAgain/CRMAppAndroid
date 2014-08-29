package com.zendaimoney.android.athena.newui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.model.CustomerBean;

public class CustomerAdapter extends BaseAdapter {
	private Context mContext;
	private List<CustomerBean> data;

	public CustomerAdapter(Context context, List<CustomerBean> list) {
		this.mContext = context;
		this.data = list;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.customer_listitem_detail,
					parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.customer_icon = (ImageView) convertView
				.findViewById(R.id.customer_icon);
		holder.customer_icon
				.setImageResource(data.get(position).getSex() == 0 ? R.drawable.ico_woman_small_2x
						: R.drawable.ico_man_small_2x);
		holder.customer_name = (TextView) convertView
				.findViewById(R.id.customer_name);
		holder.business_count = (TextView) convertView
				.findViewById(R.id.business_count);
		holder.customer_mobile = (ImageView) convertView
				.findViewById(R.id.customer_mobile);
		holder.customer_name.setText(data.get(position).getName());
		holder.business_count.setText("共"
				+ data.get(position).getBusinessCount() + "笔业务");

		return convertView;
	}

	class ViewHolder {
		ImageView customer_icon;
		TextView customer_name;
		TextView business_count;
		ImageView customer_mobile;
	}

}
