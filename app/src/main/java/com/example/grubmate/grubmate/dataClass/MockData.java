package com.example.grubmate.grubmate.dataClass;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/19/17.
 */

public class MockData {
    public static ArrayList<Post> getPostList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        for(int i = 0; i<num; i++) {
            Post localPost = new Post();
            localPost.leftQuantity = i;
            localPost.totalQuantity = i;
            localPost.postID = i;
            localPost.posterID = i;
            localPost.isHomeMade = i%2 == 0;
            localPost.title = "Pengxiang";
            result.add(localPost);
        }
        return result;
    }

    public static ArrayList<Subscription> getSubscriptionList(int num) {
        ArrayList<Subscription> result = new ArrayList<Subscription>();
        for(int i = 0; i<num; i++) {
            Subscription localSubscription = new Subscription();
            localSubscription.subscriptionID = i;
            localSubscription.subscriberID = i*2;
            localSubscription.query = "Subscription title";
            localSubscription.isActive = true;
            result.add(localSubscription);
        }
        return result;
    }
}
