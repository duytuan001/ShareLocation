package com.example.tuanpd.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager mAlarmMgr;
    private PendingIntent mAlarmIntent;
    private Class mServiceClass = LocationService.class;
    private boolean mIsRestartAtDeviceReboot = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, mServiceClass);
        startWakefulService(context, service);
    }

    /**
     * For 15', 30', 60', half day and 1 day
     *
     * @param context
     * @param intervalTime
     */
    public void startAlarm(Context context, long intervalTime) {
        if (mAlarmMgr == null) {
            mAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        if (mAlarmIntent == null) {
            Intent intent = new Intent(context, mServiceClass);
            mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }

        long intervalTimeType = 0;
        if (intervalTime < 25 * 60) {
            intervalTimeType = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        } else if (intervalTime < 45 * 60) {
            intervalTimeType = AlarmManager.INTERVAL_HALF_HOUR;
        } else if (intervalTime < 120 * 60) {
            intervalTimeType = AlarmManager.INTERVAL_HOUR;
        } else {
            intervalTimeType = AlarmManager.INTERVAL_HALF_DAY;
        }

        mAlarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                intervalTimeType,
                intervalTimeType, mAlarmIntent);

        if (mIsRestartAtDeviceReboot) {
            setAutoStartWhenDeviceReboot(context, true);
        }
    }

    public void cancelAlarm(Context context) {
        if (mAlarmMgr != null) {
            mAlarmMgr.cancel(mAlarmIntent);
        }

        if (mIsRestartAtDeviceReboot) {
            setAutoStartWhenDeviceReboot(context, false);
        }
    }

    protected void setAutoStartWhenDeviceReboot(Context context, boolean isRestart) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(
                receiver,
                isRestart ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public Class getServiceClass() {
        return mServiceClass;
    }

    public void setServiceClass(Class serviceClass) {
        mServiceClass = serviceClass;
    }

    public boolean isIsRestartAtDeviceReboot() {
        return mIsRestartAtDeviceReboot;
    }

    public void setIsRestartAtDeviceReboot(boolean isRestartAtDeviceReboot) {
        mIsRestartAtDeviceReboot = isRestartAtDeviceReboot;
    }
}
