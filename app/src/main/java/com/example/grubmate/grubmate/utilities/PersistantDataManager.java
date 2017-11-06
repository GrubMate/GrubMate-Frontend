package com.example.grubmate.grubmate.utilities;

import android.util.Log;

import com.example.grubmate.grubmate.dataClass.*;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/5/17.
 */

public class PersistantDataManager {
    private static int userID = 0;
    private static ArrayList<Integer> groupIDs = new ArrayList<Integer>();
    private static ArrayList<Notification> notificationCache = com.example.grubmate.grubmate.dataClass.MockData.getNotificationList();
    private static Boolean[] allergyInfo = new Boolean[]{false, false, false};


    public static ArrayList<Notification> getNotificationCache() {
        return notificationCache;
    }
    public static void setNotificationCache(ArrayList<Notification> notificationCache) {
        PersistantDataManager.notificationCache = notificationCache;
    }
    public static void addNotification(Notification notification) {
        notificationCache.add(0, notification);
    }

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

    public static Boolean[] getAllergyInfo() {
        return allergyInfo;
    }

    public static void setAllergyInfo(Boolean[] newAllergyInfo) {
        allergyInfo = newAllergyInfo;
    }


}
