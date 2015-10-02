package com.example.tuanpd.myapplication;

import android.app.IntentService;
import android.content.Intent;

public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmReceiver.completeWakefulIntent(intent);
    }
}
