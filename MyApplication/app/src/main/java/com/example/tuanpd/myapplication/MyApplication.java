package com.example.tuanpd.myapplication;

import android.app.Application;
import android.content.Context;
//import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private LocationGroup mLocationGroup = null;
    private LocationUser mMyLocationUser = null;

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (MPreference.isGroupConnected(this)) {
        mLocationGroup = new LocationGroup();
        mLocationGroup.setId(MPreference.getCurrentGroupId(this));
        mLocationGroup.setPassword(MPreference.getCurrentGroupPw(this));
        mLocationGroup.setIntervalTime(MPreference.getIntervalPeriodIndex(this));
        mLocationGroup.setUserList(getDummyUsers());
//        }

        mMyLocationUser = new LocationUser();
        mMyLocationUser.setPhoneNumber(AppUtils.getMyPhoneNumber(this));
        mMyLocationUser.setUsername(AppUtils.getContactName(this, mMyLocationUser.getPhoneNumber()));
    }

    public LocationGroup getLocationGroup() {
        return mLocationGroup;
    }

    public void setLocationGroup(LocationGroup locationGroup) {
        this.mLocationGroup = locationGroup;
    }

    public LocationUser getMyLocationUser() {
        return mMyLocationUser;
    }

    public void setMyLocationUser(LocationUser myLocationUser) {
        this.mMyLocationUser = myLocationUser;
    }

    private List<LocationUser> getDummyUsers() {
        List<LocationUser> list = new ArrayList<LocationUser>();
        LocationUser user;
        for (int i = 0; i < 5; i++) {
            user = new LocationUser();
            user.setPhoneNumber(i + "");
            user.setLatitude(10.8554456 + 0.008 * i);
            user.setLongitude(106.6407154 + 0.006 * i);
            user.setUsername("User " + i);
            user.setUpdatedTime(System.currentTimeMillis());
            list.add(user);
        }
        return list;
    }
}
