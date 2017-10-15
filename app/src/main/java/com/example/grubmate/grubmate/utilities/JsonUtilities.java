package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    public static ArrayList<Post> getFeedItems(String jsonString) {
        FeedList feedList = gson.fromJson(jsonString, FeedList.class);
        return feedList.itemList;
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
