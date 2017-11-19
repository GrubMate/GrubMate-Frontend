package com.example.grubmate.grubmate.dataClass;

import android.icu.util.Freezable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/19/17.
 */

public class MockData {
    public final static boolean TESTING = true;
    public static ArrayList<Post> getPostList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Post localPost = new Post();
                localPost.leftQuantity = i;
                localPost.totalQuantity = i;
                localPost.posterName="Jieji";
                localPost.postID = i;
                localPost.posterID = i;
                localPost.isHomeMade = i%2 == 0;
                localPost.title = "Pengxiang" + i;
                localPost.isActive = true;
                localPost.postPhotos = new String[]{"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/p320x320/18157207_1865865683653472_4594581227614755828_n.jpg?oh=f4dd57613f14e50610f16ba17043bce7&oe=5A643985","https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/p320x320/18157207_1865865683653472_4594581227614755828_n.jpg?oh=f4dd57613f14e50610f16ba17043bce7&oe=5A643985"};
                localPost.description = "test";
                localPost.timePeriod = new String[]{"1", "2"};
                localPost.allergyInfo = new Boolean[]{true, false, false};
                Double[] address = new Double[2];
                address[0]=  34.0227552 + i;
                address[1]= -118.2823193 + i;
                localPost.address = address;
                result.add(localPost);
            }
        }
        return result;
    }

    public static ArrayList<Post> getSearchList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Post localPost = new Post();
                localPost.leftQuantity = i;
                localPost.totalQuantity = i;
                localPost.postID = i;
                localPost.posterID = i;
                localPost.posterName="Jieji";
                localPost.isHomeMade = i%2 == 0;
                localPost.title = "search";
                localPost.isActive = true;
                result.add(localPost);
            }
        }
        return result;
    }

    public static ArrayList<Post> getPastPostList(int num) {
        ArrayList<Post> result = new ArrayList<Post>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Post localPost = new Post();
                localPost.leftQuantity = i;
                localPost.totalQuantity = i;
                localPost.postID = i;
                localPost.posterID = i;
                localPost.isHomeMade = i%2 == 0;
                localPost.title = "Past Post" + i;
                localPost.isActive = false;
                localPost.description = "PP description";
                Double[] address = new Double[2];
                address[0]=  34.0227552 + i;
                address[1]= -118.2823193 + i;
                localPost.address = address;
                result.add(localPost);
            }

        }
        return result;
    }
    public static User getUser(int id) {
        User result = new User();
        if(TESTING) {
            result.userName = "Jie Ji";
            result.rating = 4.5;
            result.profilePhoto = "https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/p320x320/18157207_1865865683653472_4594581227614755828_n.jpg?oh=f4dd57613f14e50610f16ba17043bce7&oe=5A643985";
            result.facebookID = "100006901752615";//Jie's fb id
            //result.facebookID = "100006296499746";//Zhaoqi's fb id
        }
        return result;
    }

    public static ArrayList<Subscription> getSubscriptionList(int num) {
        ArrayList<Subscription> result = new ArrayList<Subscription>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Subscription localSubscription = new Subscription();
                localSubscription.subscriptionID = i;
                localSubscription.subscriberID = i*2;
                localSubscription.query = "Subscription title";
                localSubscription.isActive = true;
                result.add(localSubscription);
            }
        }
        return result;
    }

    public static ArrayList<Group> getGroupList(int num) {
        ArrayList<Group> result = new ArrayList<Group>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Group localGroup = new Group();
                localGroup.memberIDs = new ArrayList<Integer>();
                localGroup.groupID = i;
                localGroup.groupName = "group number " + i;
                result.add(localGroup);
            }
        }
        return result;
    }

    public static ArrayList<Friend> geFriendList(int num) {
        ArrayList<Friend> result = new ArrayList<Friend>();
        if(TESTING) {
            for(int i = 0; i<num; i++) {
                Friend localFriend = new Friend();
                localFriend.id = i;
                localFriend.name = "friend number " + i;
                result.add(localFriend);
            }
        }
        return result;
    }

    public static ArrayList<Transaction> getTransactionList() {
        ArrayList<Transaction> result = new ArrayList<>();
        if(TESTING) {
            Transaction transaction = new Transaction();
            transaction.posterRated = false;
            transaction.requesterRated = false;
            transaction.isActive = true;
            transaction.posterID = 1;
            transaction.requesterID = 1;
            transaction.posterName = "PIPI poster";
            transaction.postName = "PIPI item";
            transaction.requesterName = "PIPI requester";
            result.add(transaction);
            Transaction transaction2 = new Transaction();
            transaction.posterRated = false;
            transaction.requesterRated = false;
            transaction2.isActive = false;
            transaction2.posterID = 1;
            transaction2.requesterID = 1;
            transaction2.posterName = "PIPI poster";
            transaction2.postName = "PIPI item";
            transaction2.requesterName = "PIPI requester";
            result.add(transaction2);
        }
        return result;
    }

    public static ArrayList<Notification> getNotificationList() {
        ArrayList<Notification> result = new ArrayList<Notification>();
        if(TESTING) {
            Notification requestNotification = new Notification();
            requestNotification.type = Notification.REQUEST;
            requestNotification.title = "Jie JI";
            requestNotification.requesterName = "Pengxiang Zhu";
            requestNotification.requestID = 0;
            Double[] address = new Double[2];
            address[0]=  34.0227552 ;
            address[1]= -118.2823193 ;
            requestNotification.address = address;
            result.add(requestNotification);

            Notification matchNotification = new Notification();
            matchNotification.type = Notification.MATCH;
            matchNotification.title = "Oreo";
            matchNotification.requesterName = "Pengxiang Zhu";
            matchNotification.address = address;
            result.add(matchNotification);

            Notification accptedNotification = new Notification();
            accptedNotification.type = Notification.ACCEPTED;
            accptedNotification.title = "Lolipop";
            result.add(accptedNotification);
            Notification ratingNotification = new Notification();
            ratingNotification.type = Notification.RATING;
            ratingNotification.title = "Lolipop";
            ratingNotification.fromUserName = "Pengxiang";
            ratingNotification.fromUserID = 0;
            ratingNotification.toUserName = "Jieji";
            ratingNotification.toUserID = 1;
            result.add(ratingNotification);
        }
        return result;
    }
}
