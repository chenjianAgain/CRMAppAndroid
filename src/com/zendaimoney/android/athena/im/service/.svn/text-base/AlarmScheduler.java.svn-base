package com.zendaimoney.android.athena.im.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmScheduler {
	private static final String TAG = AlarmScheduler.class.getSimpleName();

	private Context mCtx;

	public AlarmScheduler(Context ctx) {
		mCtx = ctx;
	}

	public void rescheduleSyncLocation(long intervalTime) {

		// TODO: uncomment for testing
		// intervalTime = 3*60*1000;

		long now = System.currentTimeMillis();
		long next = now + intervalTime;	//下次心跳时间为15秒后
		setNextSyncLocationTime(next);
	}

	private void setNextSyncLocationTime(long triggerTime) {
		Intent intent = new Intent(mCtx, MyAlarmReceiver.class);
		PendingIntent alarmSender = PendingIntent.getBroadcast(mCtx, 0, intent,
				0);

		AlarmManager am = (AlarmManager) mCtx
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, triggerTime, alarmSender);

//		mLocalPref.setNextSyncLocationTime(triggerTime);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public void stop() {

		Intent intent = new Intent(mCtx, MyAlarmReceiver.class);
		PendingIntent alarmSender = PendingIntent
				.getBroadcast(mCtx, 0, intent, 0);

		AlarmManager am = (AlarmManager) mCtx
				.getSystemService(Context.ALARM_SERVICE);

		am.cancel(alarmSender);

		Log.d(TAG, "alarm scheduler stopped!");
	}

}
