package com.example.paymeback;

public class GroupItem {
    private int mImageResource;
    private String mGroupName;
    private String mGroupMembers;

    //constructor
    public GroupItem(int imageResource, String groupName, String groupMembers) {
        mImageResource = imageResource;
        mGroupName = groupName;
        mGroupMembers = groupMembers;
    }

    public void changeGroupName(String name) {
        mGroupName = name;
    }

    //getters
    public int getmImageResource() {
        return mImageResource;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public String getmGroupMembers() {
        return mGroupMembers;
    }
}

