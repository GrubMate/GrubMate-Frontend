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
    private static ArrayList<Integer> blockIDs = new ArrayList<Integer>();

    public static ArrayList<Notification> getNotificationCache() {
        return notificationCache;
    }
    public static void setNotificationCache(ArrayList<Notification> notificationCache) {
        PersistantDataManager.notificationCache = notificationCache;
    }
    public static void addNotification(Notification notification) {
        notificationCache.add(0, notification);
    }

    public static void removeRatingNotification(int requestID, int postID) {
        for(int i = 0; i<notificationCache.size(); i++) {
            if(notificationCache.get(i).postID!=null
                    &&notificationCache.get(i).postID==postID
                    &&notificationCache.get(i).requestID!=null
                    &&notificationCache.get(i).requestID==requestID) {
                notificationCache.remove(i);
                break;
            }
        }
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
    public static void addBlockIDs(int id){blockIDs.add(id);}
    public static ArrayList<Integer> getBlockIDs(){
        return blockIDs;
    }
    public static Boolean[] getAllergyInfo() {
        return allergyInfo;
    }

    public static void setAllergyInfo(Boolean[] newAllergyInfo) {
        allergyInfo = newAllergyInfo;
    }


}
