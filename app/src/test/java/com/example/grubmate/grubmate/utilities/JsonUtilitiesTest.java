package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Friend;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.google.gson.Gson;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Ryan on 2017/10/27.
 */
public class JsonUtilitiesTest {
    Gson gson;
    private class FeedList {
        public int id;
        public ArrayList<Post> itemList;
    }

    private class SubscriptionList {
        public int id;
        public ArrayList<Subscription> itemList;
    }

    private class RequestList {
        public int id;
        public ArrayList<UserRequest> itemList;
    }

    public class GroupFeed {
        public Integer id;
        public ArrayList<Group> itemList;
        public GroupFeed () {
            itemList = new ArrayList<Group>();
        }
    }
    public class FriendFeed {
        public int id;
        public ArrayList<Friend> itemList;
        public FriendFeed() {
            itemList = new ArrayList<Friend>();
        }
    }
    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void getFeedItemsNullStringTest() throws Exception{
        assertNull(JsonUtilities.getFeedItems(null));
    }

    @Test
    public void getFeedItemsNullListTest() throws Exception
    {
        FeedList list = null;
        assertNull(JsonUtilities.getFeedItems(gson.toJson(list)));
    }

    @Test
    public void getFeedItemsEmptyListTest() throws Exception
    {
        FeedList list = new FeedList();
        assertNull(JsonUtilities.getFeedItems(gson.toJson(list)));
    }
    @Test
    public void getFeedItems() throws Exception {
        FeedList list = new FeedList();
        list.id = 0;
        list.itemList= new ArrayList<>();

        //empty list test
        assertEquals(list.itemList,JsonUtilities.getFeedItems(gson.toJson(list)));

        //content test
        for(int i = 0; i < 5; i++)
        {
            Post p = new Post();
            p.postID = i;
            p.posterID = i*i;
            p.category = Integer.toString(i);
            p.groupIDs = new Integer[3];
            for(int j= 0; j < 3;j++ )
            {
                p.groupIDs[j] = i*i*i;
            }
            list.itemList.add(p);
        }

        ArrayList<Post> results = JsonUtilities.getFeedItems(gson.toJson(list));
        assertEquals(list.itemList.size(),results.size());
        for(int i =0; i < list.itemList.size();i++)
        {
            assertEquals(gson.toJson(list.itemList.get(i)),gson.toJson(results.get(i)));
        }
    }

    @Test
    public void getRequestItemsNullStringTest() throws Exception
    {
        assertNull(JsonUtilities.getRequestItems(null));
    }

    @Test
    public void getRequestItemsNullListTest() throws Exception
    {
        RequestList list = null;
        assertNull(JsonUtilities.getRequestItems(gson.toJson(list)));
    }

    @Test
    public void getRequestItemsEmptyListTest() throws Exception
    {
        RequestList list = new RequestList();
        assertNull(JsonUtilities.getRequestItems(gson.toJson(list)));
    }

    @Test
    public void getRequestItems() throws Exception {
        RequestList list = new RequestList();
        list.id = 0;
        list.itemList= new ArrayList<>();

        //empty list test
        assertEquals(list.itemList,JsonUtilities.getRequestItems(gson.toJson(list)));


        //content test
        for(int i = 0; i < 5; i++)
        {
            UserRequest p = new UserRequest();
            p.requesterID = i;
            p.requesterID = i*i;
            p.status = Integer.toString(i*i*i);
            list.itemList.add(p);
        }
        ArrayList<UserRequest> results = JsonUtilities.getRequestItems(gson.toJson(list));
        assertEquals(list.itemList.size(),results.size());
        for(int i =0; i < list.itemList.size();i++)
        {
            assertEquals(gson.toJson(list.itemList.get(i)),gson.toJson(results.get(i)));
        }
    }

    @Test
    public void getGroupList() throws Exception {
        //null string test
        assertEquals(null,JsonUtilities.getGroupList(null));
        Gson gson = new Gson();
        ArrayList<Group> list = null;
        //null list test
        assertEquals(null,JsonUtilities.getGroupList(gson.toJson(list)));

        //empty list test
        GroupFeed emptyGroupFeed = new GroupFeed();
        list = new ArrayList<Group>();
        emptyGroupFeed.itemList = list;
        assertEquals(list,JsonUtilities.getGroupList(gson.toJson(emptyGroupFeed)));

        //content test
        for(int i = 0; i < 5; i++)
        {
            Group g = new Group();
            g.groupID = i;
            g.groupName = Integer.toString(i*i);
            switch(i){
                case 0: g.memberIDs = null;
                        break;
                case 1: g.memberIDs = new ArrayList<>();
                        break;
                default:
                        g.memberIDs = new ArrayList<>();
                        for(int j = 0; j < 3 ; j++)
                        {
                            g.memberIDs.add(i*i*i+j);
                        }
            }
            list.add(g);
            //System.out.println(gson.toJson(g));
        }

        GroupFeed groupFeed = new GroupFeed();
        groupFeed.itemList = list;

        ArrayList<Group> result = JsonUtilities.getGroupList(gson.toJson(groupFeed));
        assertEquals(list.size(),result.size());
        for(int i = 0; i < list.size(); i++)
        {
            //System.out.println(result.get(i).groupID);
           // assertEquals(list.get(i).groupID,result.get(i).groupID);
        }

    }

    @Test
    public void getfriendlistNullStringTest() throws Exception
    {
        assertNull(JsonUtilities.getfriendsList(null));
    }

    @Test
    public void getfriendListNullListTest() throws Exception
    {
        ArrayList<String> list = null;
        assertNull(JsonUtilities.getfriendsList(gson.toJson(list)));
    }

    @Test
    public void getfriendListEmptyListTest() throws Exception
    {
        ArrayList<String> list = new ArrayList<>();
        assertEquals(list,JsonUtilities.getfriendsList(gson.toJson(list)));
    }
    @Test
    public void getfriendsList() throws Exception {

        //empty list test
        ArrayList<String>list = new ArrayList<>();
        assertEquals(list,JsonUtilities.getGroupList(gson.toJson(list)));

        //content test
        for(int i =0; i < 5; i++)
        {
            Friend friend = new Friend();
            switch(i){
                case 0: friend.name = null;
                    break;
                case 1: friend.name = "";
                    break;
                default:
                    friend.name = String.valueOf(i);
            }
            list.add(friend);
        }
        FriendFeed friendFeed = new FriendFeed();
        friendFeed.itemList=list;
        ArrayList<Friend> result = JsonUtilities.getfriendsList(gson.toJson(friendFeed));

        assertEquals(list.size(),result.size());
        for(int i = 0; i < list.size(); i++)
        {
            assertEquals(list.get(i).name,result.get(i).name);
        }

    }

    @Test
    public void getSubscriptionItemsNullStringTest() throws Exception
    {
        assertNull(JsonUtilities.getSubscriptionItems(null));
    }

    @Test
    public void getSubscriptionItemsNullListTest() throws Exception
    {
        SubscriptionList list = null;
        assertNull(JsonUtilities.getSubscriptionItems(gson.toJson(list)));
    }

    @Test
    public void getSubscriptionItemEmptyListTest() throws Exception
    {
        SubscriptionList list = new SubscriptionList();
        assertNull(JsonUtilities.getSubscriptionItems(gson.toJson(list)));
    }

    @Test
    public void getSubscriptionItems() throws Exception {

         SubscriptionList list = new SubscriptionList();
         //empty list test
         list.id = 0;
         list.itemList = new ArrayList<>();

        //content test
        for(int i = 0; i < 5; i++)
        {
            Subscription s = new Subscription();
            s.subscriptionID = i;
            s.category = Integer.toString(i*i);
            if(i>0)
            {
                s.tags = new String[2];
                s.tags[0] = Integer.toString(i*i*i+0);
                s.tags[1] = Integer.toString(i*i*i+1);
            }
            list.itemList.add(s);
        }
        ArrayList<Subscription> result = JsonUtilities.getSubscriptionItems(gson.toJson(list));
        assertEquals(list.itemList.size(),result.size());
        for(int i = 0; i < list.itemList.size();i++)
        {
            assertEquals(gson.toJson(list.itemList.get(i)),gson.toJson(result.get(i)));
        }
    }

}