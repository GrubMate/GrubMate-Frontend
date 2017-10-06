package com.example.grubmate.grubmate.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by tianhangliu on 10/4/17.
 */

public class JsonUtilities {
    private static final Gson gson = new Gson();

    private class FeedList {
        private int id;
        private String[] itemList;

        public FeedList(int id, String[] itemList) {
            this.id = id;
            this.itemList = itemList;
        }

        public int getId() {
            return id;
        }

        public String[] getItemList() {
            return itemList;
        }
    }

    private class post {
        public int postID;
        public int posterID;
        public String[] posterPhoto;
        public String[] tags;

    }

    public static String[] getFeedItems(String jsonString) {
        FeedList feedList = gson.fromJson(jsonString, FeedList.class);
        return feedList.getItemList();
    }
}
