package com.zendaimoney.android.athena.im.service;

import android.content.Context;
import android.os.PowerManager;

public class AlarmAlertWakeLock {
	private static final String TAG = AlarmAlertWakeLock.class.getSimpleName();

	private static PowerManager.WakeLock sCpuWakeLock = null;

	static void aquire(Context context) {
		if (sCpuWakeLock == null) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			sCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					"DriverTracker");
			sCpuWakeLock.acquire();

//			AppLog.v(TAG, "cpu wake lock aquired!");
		}
	}

	static void release() {
		if (sCpuWakeLock != null) {
			sCpuWakeLock.release();
			sCpuWakeLock = null;

//			AppLog.v(TAG, "cpu wake lock release!");
		}
	}

}
