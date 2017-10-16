package com.example.grubmate.grubmate.utilities;

import android.util.Log;

/**
 * Created by tianhangliu on 10/5/17.
 */

public class PersistantDataManager {
    public static int userID;
    public static int getUserID() {
        return userID;
    }
    public static void setUserID(int newUserID)
    {
        userID = newUserID;
    }
}
