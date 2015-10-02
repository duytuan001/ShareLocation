package com.example.tuanpd.myapplication;

import java.util.Timer;
import java.util.TimerTask;

public class LocationTimer {

    private Timer mTimer;

    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            //
        }
    };

    public void cancel() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void start(long intervalTime) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(mTask, 0, intervalTime);

    }
}
