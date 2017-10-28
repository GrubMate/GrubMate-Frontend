package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static org.junit.Assert.*;

/**
 * Created by jieji on 27/10/2017.
 */
public class NetworkUtilitiesTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void get() throws Exception {
        String url = null;
        assertNull( NetworkUtilities.get(url) );

        url = "";
        assertNull( NetworkUtilities.get(url) );

        url = "http://10.120.67.208:8080/user/21";
        String expectResult = "{\"userID\":21,\"userName\":\"FILL_IN\",\"facebookID\":\"FILL_IN\",\"profilePhoto\":\"FILL_IN\",\"bio\":null,\"ratings\":null,\"allergy\":null,\"allFriends\":[21],\"groupID\":null,\"postsID\":[],\"requestsID\":null,\"subscriptionID\":null,\"friendList\":[\"a\",\"b\"],\"commentsAsProvider\":null,\"commentsAsConsumer\":null}";
        assertEquals(expectResult, NetworkUtilities.get(url));
    }

    @Test
    public void getLong() throws Exception {
        String url = null;
        assertNull( NetworkUtilities.get(url) );

        url = "";
        assertNull( NetworkUtilities.get(url) );

        url = "http://10.120.67.208:8080/user/21";
        String expectResult = "{\"userID\":21,\"userName\":\"FILL_IN\",\"facebookID\":\"FILL_IN\",\"profilePhoto\":\"FILL_IN\",\"bio\":null,\"ratings\":null,\"allergy\":null,\"allFriends\":[21],\"groupID\":null,\"postsID\":[],\"requestsID\":null,\"subscriptionID\":null,\"friendList\":[\"a\",\"b\"],\"commentsAsProvider\":null,\"commentsAsConsumer\":null}";
        assertEquals(expectResult, NetworkUtilities.get(url));
    }

    @Test
    public void post() throws Exception {
    //add a new user
        User newUser = new User();
        newUser.userName = "FILL_IN";
        ArrayList<String> fList = new ArrayList<String>();
        fList.add("a");
        fList.add("b");
        newUser.friendList = fList;
        newUser.profilePhoto = "FILL_IN";
        newUser.facebookID = "haha";
        newUser.userID = null;
        newUser.bio = null;
        newUser.ratings = null;
        newUser.allergy = null;
        newUser.groupID = null;
        newUser.postsID = null;
        newUser.requestsID = null;
        newUser.subscriptionID = null;
        Gson gson = new Gson();
        String userJson = gson.toJson(newUser);


        String url = null;
        assertNull( NetworkUtilities.post(url, userJson) );


        url = "";
        assertNull(NetworkUtilities.post(url, userJson));

        //url = GrubMatePreference.getUserURL();
        url = "http://10.120.67.208:8080" +  "/user";
        assertEquals("23", NetworkUtilities.post(url, userJson));
    }

    @Test
    public void put() throws Exception {
        User newUser = new User();
        newUser.userID = 21;
        newUser.userName = "FILL_IN";
        ArrayList<String> fList = new ArrayList<String>();
        fList.add("a");
        fList.add("b");
        newUser.friendList = fList;
        newUser.profilePhoto = "FILL_IN";
        newUser.facebookID = "FILL_IN";
        newUser.bio = null;
        newUser.ratings = null;
        newUser.allergy = null;
        newUser.groupID = null;
        newUser.postsID = null;
        newUser.requestsID = null;
        newUser.subscriptionID = null;
        Gson gson = new Gson();
        String userJson = gson.toJson(newUser);


        String url = null;
        assertNull( NetworkUtilities.put(url, userJson) );


        url = "";
        assertNull(NetworkUtilities.put(url, userJson));

        //url = GrubMatePreference.getUserURL();
        url = "http://10.120.67.208:8080" +  "/user" + "/21";
        assertEquals("21", NetworkUtilities.put(url, userJson));
    }

    @Test
    public void delete() throws Exception {
        Post toDelete = new Post();
        toDelete.postID = 59;

        Gson gson = new Gson();
        String postJson = gson.toJson(toDelete);

        String url = null;
        assertNull(NetworkUtilities.delete(url, postJson));

        url = null;
        assertNull(NetworkUtilities.delete(url, postJson));

        url = "http://10.120.67.208:8080" +  "/post" + "/22" + "/74";

        assertEquals("delete", NetworkUtilities.delete(url, postJson));


    }

}