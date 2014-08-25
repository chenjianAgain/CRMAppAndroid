package com.zendaimoney.android.athena.newui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendaimoney.android.athena.R;

public class CustomerFragment extends Fragment {
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.customer_fragment, container, false);
	}

}
