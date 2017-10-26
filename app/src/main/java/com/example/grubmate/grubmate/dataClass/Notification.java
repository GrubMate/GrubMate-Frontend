package com.example.grubmate.grubmate.dataClass;

public class Notification {
    public int type;
    public static final int REQUEST = 1;
    public static final int MATCH = 2;
    public static final int ACCEPTED = 3;
    public static final int RATING = 4;
    public static final int DENIED = 5;


    public Integer requestID;
    public Integer requesterID;
    public Integer targetPostID;
    public String requesterName;
    public String status;
    public Double[] address;
    public Integer postID;
    public Integer posterID;
    public String posterName;
    public String title;
    public Integer fromUserID;
    public String fromUserName;
    public Integer toUserID;
    public String toUserName;
    public Integer rating;


    /*
        REQUEST should have
        reuestID
        requesterID
        requesterName
        targetPostID
        title
        address
     */

    /*
        MATCH should have
        postID
        posterID
        posterName
        title
     */

    /*
        ACCEPTED should have
        requestID
        title
        posterID
     */
}
