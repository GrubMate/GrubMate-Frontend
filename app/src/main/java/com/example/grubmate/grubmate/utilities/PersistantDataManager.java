package com.example.grubmate.grubmate.utilities;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/5/17.
 */

public class PersistantDataManager {
    public static int userID = 0;
    public static ArrayList<Integer> groupIDs = new ArrayList<Integer>();
    public static int getUserID() {
        return userID;
    }
    public static void setUserID(int newUserID)
    {
        userID = newUserID;
    }
    public static ArrayList<Integer> getGroupIDs() {
        return groupIDs;
    }

    public static void setGroupIDs(ArrayList<Integer> groupIDs) {
        PersistantDataManager.groupIDs = groupIDs;
    }
}
