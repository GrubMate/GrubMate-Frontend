package com.example.grubmate.grubmate.utilities;

import android.util.Log;

import com.example.grubmate.grubmate.dataClass.Friend;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/4/17.
 */

public class JsonUtilities {
    private static final Gson gson = new Gson();

    private class FeedList {
        public int id;
        public ArrayList<Post> itemList;
    }

    private class SubscriptionList {
        public int id;
        public ArrayList<Subscription> itemList;
    }

    private class GroupList{
        public int id;
        public ArrayList<Group> itemList;
    }

    public static ArrayList<Post> getFeedItems(String jsonString) {
        FeedList feedList = gson.fromJson(jsonString, FeedList.class);
        return feedList.itemList;
    }

    public class FriendFeed{
        public int id;
        public ArrayList<Friend> itemList;

    }
    public static ArrayList<Group> getGroupList(String jsonString){
        try {
            GroupList groupList = gson.fromJson(jsonString, GroupList.class);
            return groupList.itemList;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Friend> getfriendsList(String jsonString) {

        try {
            FriendFeed friendList = gson.fromJson(jsonString, FriendFeed.class);

            Log.d("getfriendsList: ", friendList.itemList.toString());

            return friendList.itemList;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Subscription> getSubscriptionItems(String jsonString) {
        try {
            SubscriptionList subscriptionList = gson.fromJson(jsonString, SubscriptionList.class);
            return subscriptionList.itemList;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
