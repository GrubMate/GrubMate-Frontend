package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.google.gson.Gson;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Ryan on 2017/10/27.
 */
public class JsonUtilitiesTest {
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
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getFeedItems() throws Exception {
        Gson gson = new Gson();
        //null string test
        assertEquals(null,JsonUtilities.getFeedItems(null));
        //nullptr test
        FeedList list = null;
        assertEquals(null, JsonUtilities.getFeedItems(gson.toJson(list)));
        list = new FeedList();
        //null list test
        assertEquals(null,JsonUtilities.getFeedItems(gson.toJson(list)));
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
    public void getRequestItems() throws Exception {
        //null string test
        assertEquals(null,JsonUtilities.getRequestItems(null));
        Gson gson = new Gson();
        //nullptr test
        RequestList list = null;
        assertEquals(null,JsonUtilities.getRequestItems(gson.toJson(list)));
        list = new RequestList();
        //null list test
        assertEquals(null,JsonUtilities.getRequestItems(gson.toJson(list)));
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
        list = new ArrayList<Group>();
        assertEquals(list,JsonUtilities.getGroupList(gson.toJson(list)));

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

        ArrayList<Group> result = JsonUtilities.getGroupList(gson.toJson(list));
        assertEquals(list.size(),result.size());
        for(int i = 0; i < list.size(); i++)
        {
            //System.out.println(result.get(i).groupID);
            assertEquals(list.get(i).groupID,result.get(i).groupID);
        }

    }

    @Test
    public void getfriendsList() throws Exception {
        //null string test
        assertEquals(null,JsonUtilities.getfriendsList(null));
        Gson gson = new Gson();
        ArrayList<String> list = null;
        //null list test
        assertEquals(null,JsonUtilities.getGroupList(gson.toJson(list)));

        //empty list test
        list = new ArrayList<>();
        assertEquals(list,JsonUtilities.getGroupList(gson.toJson(list)));

        //content test
        for(int i =0; i < 5; i++)
        {
            String s;
            switch(i){
                case 0: s = null;
                    break;
                case 1: s = "";
                    break;
                default:
                    s = Integer.toString(i);
            }
            list.add(s);
        }

        ArrayList<Group> result = JsonUtilities.getGroupList(gson.toJson(list));

        assertEquals(list.size(),result.size());
        for(int i = 0; i < list.size(); i++)
        {
            assertEquals(list.get(i),result.get(i));
        }

    }

    @Test
    public void getSubscriptionItems() throws Exception {
        //null string test
        assertEquals(null,JsonUtilities.getSubscriptionItems(null));
        Gson gson = new Gson();
        //null pointer test
         SubscriptionList list  = null;
         assertEquals(null,JsonUtilities.getSubscriptionItems(gson.toJson(list)));

         list = new SubscriptionList();
         //empty list test
         list.id = 0;
         list.itemList = new ArrayList<>();
         assertEquals(list.itemList,JsonUtilities.getSubscriptionItems(gson.toJson(list)));

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