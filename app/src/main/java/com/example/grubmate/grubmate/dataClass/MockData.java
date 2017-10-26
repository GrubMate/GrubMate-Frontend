package com.example.grubmate.grubmate.dataClass;

import android.icu.util.Freezable;

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

    public static ArrayList<Post> getSearchList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        for(int i = 0; i<num; i++) {
            Post localPost = new Post();
            localPost.leftQuantity = i;
            localPost.totalQuantity = i;
            localPost.postID = i;
            localPost.posterID = i;
            localPost.isHomeMade = i%2 == 0;
            localPost.title = "search";
            result.add(localPost);
        }
        return result;
    }

    public static ArrayList<Post> getPastPostList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        for(int i = 0; i<num; i++) {
            Post localPost = new Post();
            localPost.leftQuantity = i;
            localPost.totalQuantity = i;
            localPost.postID = i;
            localPost.posterID = i;
            localPost.isHomeMade = i%2 == 0;
            localPost.title = "Past Post";
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

    public static ArrayList<Group> getGroupList(int num) {
        ArrayList<Group> result = new ArrayList<Group>();
        for(int i = 0; i<num; i++) {
            Group localGroup = new Group();
            localGroup.memberIDs = new ArrayList<Integer>();
            localGroup.groupID = i;
            localGroup.groupName = "group number " + i;
            result.add(localGroup);
        }

        return result;
    }

    public static ArrayList<Friend> geFriendList(int num) {
        ArrayList<Friend> result = new ArrayList<Friend>();
        for(int i = 0; i<num; i++) {
            Friend localFriend = new Friend();
            localFriend.id = i;
            localFriend.name = "friend number " + i;
            result.add(localFriend);
        }

        return result;
    }

    public static ArrayList<Notification> getNotificationList() {
        ArrayList<Notification> result = new ArrayList<Notification>();
        Notification requestNotification = new Notification();
        requestNotification.type = Notification.REQUEST;
        requestNotification.title = "Jie JI";
        requestNotification.requesterName = "Pengxiang Zhu";
        result.add(requestNotification);

        Notification matchNotification = new Notification();
        matchNotification.type = Notification.MATCH;
        matchNotification.title = "Oreo";
        matchNotification.requesterName = "Pengxiang Zhu";
        result.add(matchNotification);

        Notification accptedNotification = new Notification();
        accptedNotification.type = Notification.ACCEPTED;
        accptedNotification.title = "Lolipop";
        result.add(accptedNotification);
        return result;
    }
}
