package com.example.grubmate.grubmate.utilities;

/**
 * Created by tianhangliu on 10/4/17.
 */

public class GrubMatePreference {
    public static final String domain = "http://10.120.78.167:8080";
    public static final String subscribeActionURL = "/subscription";
    public static final String postURL = "/post";
    public static final String searchURL = "/search";
    public static final String userURL = "/user";
    public static final String notificationURL = "/notification";
    public static final String requestURL = "/request";
    public static final String subscriptionURL = "/subscription";
    public static String getSubscribeActionURL(int ID) {
        return domain + subscribeActionURL + "/" + ID;
    }
    public static String getPostActionURl(int ID)
    {
        return domain + postURL + "/" + ID;
    }
    public static String getFeedUrl(int ID) {
        return domain + postURL + "/" + ID;
    }
    public static String getUserPostUrl(int ID)
    {
        return domain + postURL + "/" + ID+"?me=true";
    }
    public static String getNotificationURL(int ID) {
        return domain + notificationURL+"/"+ID;
    }
    public static String getSearchURL(int ID) {
        return domain + searchURL + "/" + ID;
    }
    public static String getUserUrl() {
        return domain + userURL;
    }
    public static String getUserUrl(int ID) {
        return domain + userURL + "/" + ID;
    }
    public static String getSubscriptionURL(int ID) {
        return domain + subscriptionURL + "/" + ID;
    }

    public static String getRequestURL() {
        return domain + requestURL;
    }

    public static  String getRequestURL(int ID) {
        return domain + requestURL+"/" + ID;
    }

}
