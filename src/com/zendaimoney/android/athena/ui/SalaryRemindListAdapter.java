package com.zendaimoney.android.athena.ui;

import java.util.List;

import com.zendaimoney.android.athena.R;
import com.zendaimoney.android.athena.data.SalaryRemindData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SalaryRemindListAdapter extends BaseAdapter{

	private static String TAG = "RemindListAdapter";
	int currwidget;
	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<SalaryRemindData> mList;
	int mlayout;

	public SalaryRemindListAdapter(Context paramContext, List<SalaryRemindData> paramList, Boolean paramBoolean, int paramInt)
	{
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = paramList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<SalaryRemindData> mList){
		for(int i = 0; i<mList.size(); i++){
			this.mList.add(mList.get(i));
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder localViewHolder;
		SalaryRemindData localSimpleString;
		if(convertView == null){
			convertView = mInflater.inflate(mlayout, parent, false);
			localViewHolder = new ViewHolder();
			localViewHolder.text1 = (TextView) convertView.findViewById(R.id.customname);
			localViewHolder.text2 = (TextView)convertView.findViewById(R.id.title);
			localViewHolder.text3 = (TextView)convertView.findViewById(R.id.time);
			localViewHolder.text4 = (TextView)convertView.findViewById(R.id.line2name);
			localViewHolder.text5 = (TextView)convertView.findViewById(R.id.line2num);
			convertView.setTag(localViewHolder);
		}
		else{
			localViewHolder = (ViewHolder) convertView.getTag();
			localViewHolder.text1 = (TextView) convertView.findViewById(R.id.customname);
			localViewHolder.text2 = (TextView)convertView.findViewById(R.id.title);
			localViewHolder.text3 = (TextView)convertView.findViewById(R.id.time);
			localViewHolder.text4 = (TextView)convertView.findViewById(R.id.line2name);
			localViewHolder.text5 = (TextView)convertView.findViewById(R.id.line2num);
		}
		
		localSimpleString = (SalaryRemindData)mList.get(position);
		localViewHolder.text1.setText(localSimpleString.getUserName());
		localViewHolder.text2.setText(localSimpleString.getOverTime());
		localViewHolder.text3.setText(localSimpleString.getTime());
		localViewHolder.text4.setText(localSimpleString.getCategory());
		localViewHolder.text5.setText(localSimpleString.getMoney());
		
		return convertView;
	}

	static class ViewHolder{
		TextView text1;
		TextView text2;
		TextView text3;
		TextView text4;
		TextView text5;
	}
}
