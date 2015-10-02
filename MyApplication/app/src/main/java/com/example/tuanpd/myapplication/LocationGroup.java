package com.example.tuanpd.myapplication;

import java.util.List;

public class LocationGroup {
    private String id;
    private String password;
    private String title;
    private List<LocationUser> userList;
    private long intervalTime;
    private int noMembers;
    private long createdTime;

    public LocationGroup() {

    }

    public LocationGroup(String id, String pw) {
        this.id = id;
        this.password = pw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LocationUser> getUserList() {
        return userList;
    }

    public void setUserList(List<LocationUser> userList) {
        this.userList = userList;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getNoMembers() {
        return noMembers;
    }

    public void setNoMembers(int noMembers) {
        this.noMembers = noMembers;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
