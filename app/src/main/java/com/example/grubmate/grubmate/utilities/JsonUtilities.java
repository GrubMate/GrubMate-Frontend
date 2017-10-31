package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Friend;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.Request;

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

    private class RequestList {
        public int id;
        public ArrayList<UserRequest> itemList;
    }

    public class GroupFeed {
        public Integer id;
        public ArrayList<Group> itemList;
        public GroupFeed () {
            itemList = new ArrayList<Group>();
        }
    }
    public class FriendFeed {
        public int id;
        public ArrayList<Friend> itemList;
        public FriendFeed() {
            itemList = new ArrayList<Friend>();
        }
    }

    public static ArrayList<Post> getFeedItems(String jsonString) {

        if(jsonString==null)
        {
            return null;
        }

        FeedList feedList = gson.fromJson(jsonString, FeedList.class);
        return feedList==null?null:feedList.itemList;

    }
    public static ArrayList<UserRequest> getRequestItems(String jsonString) {

        if(jsonString==null)
        {
            return null;
        }

        RequestList userRequestList = gson.fromJson(jsonString, RequestList.class);
        return userRequestList==null?null:userRequestList.itemList;
    }
    public static ArrayList<Group> getGroupList(String jsonString){
        if(jsonString==null) return null;
        ArrayList<Group> groupsList = new ArrayList<Group>();
        return gson.fromJson(jsonString,GroupFeed.class).itemList;
    }

    public static ArrayList<Friend> getfriendsList(String jsonString) {
        if(jsonString==null) return null;
        ArrayList<Friend> friendsList = new ArrayList<Friend>();
        return gson.fromJson(jsonString, FriendFeed.class).itemList;
    }
    public static ArrayList<Subscription> getSubscriptionItems(String jsonString) {

        if(jsonString ==null)
        {
            return null;
        }

        SubscriptionList subscriptionList = gson.fromJson(jsonString, SubscriptionList.class);
       return subscriptionList==null?null:subscriptionList.itemList;
    }
}
