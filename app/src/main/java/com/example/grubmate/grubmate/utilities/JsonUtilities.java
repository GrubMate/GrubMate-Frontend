package com.example.grubmate.grubmate.utilities;

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

        if(jsonString==null)
        {
            return null;
        }
        //System.out.println(jsonString);

        return gson.fromJson(jsonString,new ArrayList<Group>().getClass());
    }

    public static ArrayList<String> getfriendsList(String jsonString) {

        if(jsonString==null)
        {
            return null;
        }

        ArrayList<String> friendsList = new ArrayList<String>();
        return gson.fromJson(jsonString, friendsList.getClass());
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
