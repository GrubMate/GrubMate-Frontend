package com.example.grubmate.grubmate.utilities;

/**
 * Created by tianhangliu on 10/4/17.
 */

public class GrubMatePreference {
    public static final String domain = "http://10.120.66.208:8080";
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

    public static String getPostDeleteURL(int ID, int postID) {
        return domain + postURL + "/" + ID + "/" + postID;
    }

    public static String getPastPostURL(int ID) {
        return domain + postURL + "/" + ID+"/false";
    }



    public static String getFeedUrl(int ID) {
        return domain + postURL + "/" + ID;
    }
    public static String getUserPostUrl(int ID)
    {
        return domain + postURL + "/" + ID+"/true";
    }
    public static String getNotificationURL(int ID) {
        return domain + notificationURL+"/"+ID;
    }
    public static String getSearchURL(int ID) {
        return domain + searchURL + "/" + ID;
    }
    public static String getUserURL(){return domain+userURL;}
    public static String getUserUrl() {
        return domain + userURL;
    }
    public static String getUserUrl(int ID) {
        return domain + userURL + "/" + ID;
    }
    public static String getSubscriptionURL(int ID) {
        return domain + subscriptionURL + "/" + ID;
    }
    public static String getSubscriptionDeleteURL(int ID, int subscriptionID) {
        return domain + subscriptionURL + "/" + ID + "/" + subscriptionID;
    }

    public static String getRequestURL() {
        return domain + requestURL;
    }

    public static String getAcceptRequestURL(int ID, int requestID) {
        return domain + requestURL + "/" + ID + "/" + requestID + "/" + "1";
    }
    public static String getDenyRequestURL(int ID, int requestID) {
        return domain + requestURL + "/" + ID + "/" + requestID + "/" + "0";
    }

    public static  String getRequestURL(int ID) {
        return domain + requestURL+"/" + ID;
    }
    public static String getRequestListUrl(int ID, int postID) {
        return domain + requestURL + "/" + ID + "/" + postID;
    }
}
