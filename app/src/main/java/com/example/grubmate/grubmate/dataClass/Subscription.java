package com.example.grubmate.grubmate.dataClass;

public class Subscription {
    public Integer subscriptionID;
    public Integer subscriberID;
    public String[] tags;
    public String category;
    public String query;
    // pos 0 start time, pos 1 end time
    public String[] timePeriod;
    public Integer[] matchedPostIDs;
    public Boolean[] allergyInfo;
    public Boolean isActive;
}
