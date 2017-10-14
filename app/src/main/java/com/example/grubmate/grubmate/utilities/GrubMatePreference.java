package com.example.grubmate.grubmate.utilities;

/**
 * Created by tianhangliu on 10/4/17.
 */

public class GrubMatePreference {
    public static final String domain = "http://10.120.68.95:8080";
    public static final String subscribeActionURL = "/subscription";
    public static final String postURL = "/post";
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

    public static final String searchURL = "";
}
