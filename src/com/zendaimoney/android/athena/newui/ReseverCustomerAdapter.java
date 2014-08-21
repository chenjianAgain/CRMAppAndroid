package com.zendaimoney.android.athena.newui;

import java.util.List;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.model.CustomerBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReseverCustomerAdapter extends BaseAdapter {
	private Context mContext;
	private List<CustomerBean> data;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.reserve_customer_listitem,
					null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.customer_icon = (ImageView) convertView
				.findViewById(R.id.customer_icon);
		holder.customer_name = (TextView) convertView
				.findViewById(R.id.customer_name);
		holder.customer_group = (TextView) convertView
				.findViewById(R.id.customer_group);
		holder.customer_mobile = (ImageView) convertView
				.findViewById(R.id.customer_mobile);
		holder.customer_name.setText(data.get(position).getName());
		holder.customer_group.setText(data.get(position).getGroup());

		return convertView;
	}

	class ViewHolder {
		ImageView customer_icon;
		TextView customer_name;
		TextView customer_group;
		ImageView customer_mobile;
	}

}
