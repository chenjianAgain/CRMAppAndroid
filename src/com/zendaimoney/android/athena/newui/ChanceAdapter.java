package com.zendaimoney.android.athena.newui;

import java.util.List;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.im.model.CustomerBean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChanceAdapter extends BaseAdapter{
	private Context mContext;
	private List<CustomerBean> data;
	
	
	public ChanceAdapter(Context context, List<CustomerBean> list){
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
			convertView = inflater.inflate(R.layout.chance_listitem_detail,
					parent,false);
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
		holder.customer_mobile.setOnClickListener(new OnClickListtenerImpl(position)); 
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

	
	public class OnClickListtenerImpl implements OnClickListener{
		private int index = -1;
		
		public OnClickListtenerImpl(int index){
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+data.get(index).getMobile()));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
	}

}
