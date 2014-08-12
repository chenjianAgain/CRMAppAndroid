package com.zendaimoney.android.athena.receiver;

import com.zendaimoney.android.athena.Constants;
//import com.zendaimoney.android.athena.im.service.ReConnectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShutDownBroadcastReceiver extends BroadcastReceiver{
	@Override  
    public void onReceive(Context context, Intent intent) {  
        //关机要处理的事情  
		SharedPreferences sp = context.getSharedPreferences(
				Constants.SHAREUSER,
				context.MODE_WORLD_WRITEABLE);
		Editor editor = sp.edit();
		editor.putInt(Constants.SHUTDOWN, 1);
		editor.commit();
		System.exit(0);
    }
}
