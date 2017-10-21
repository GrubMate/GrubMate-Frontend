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


    public static User getUser(int id) {
        User result = new User();
        result.userName = "Jie Ji";
        result.ratings = new Integer[]{5, 5};
        result.profilePhoto = "https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/p320x320/18157207_1865865683653472_4594581227614755828_n.jpg?oh=f4dd57613f14e50610f16ba17043bce7&oe=5A643985";
        result.facebookID = "100006901752615";

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
