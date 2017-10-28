package com.example.grubmate.grubmate.utilities;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
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
    public void nullUrlTest() throws Exception {
        String url = null;
        assertNull( NetworkUtilities.get(url) );

        url = "";
        assertNull( NetworkUtilities.get(url) );

        url = "https://api.github.com/users/SeasonJi";
        String expectResult = "{\"login\":\"SeasonJi\",\"id\":16678749,\"avatar_url\":\"https://avatars2.githubusercontent.com/u/16678749?v=4\",\"gravatar_id\":" +
                "\"\",\"url\":\"https://api.github.com/users/SeasonJi\",\"html_url\":\"https://github.com/SeasonJi\",\"followers_url\":" +
                "\"https://api.github.com/users/SeasonJi/followers\",\"following_url\":" +
                "\"https://api.github.com/users/SeasonJi/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/SeasonJi/gists{/gist_id}\"," +
                "\"starred_url\":\"https://api.github.com/users/SeasonJi/starred{/owner}{/repo}\",\"subscriptions_url\":" +
                "\"https://api.github.com/users/SeasonJi/subscriptions\",\"organizations_url\":\"https://api.github.com/users/SeasonJi/orgs\"," +
                "\"repos_url\":\"https://api.github.com/users/SeasonJi/repos\",\"events_url\":\"https://api.github.com/users/SeasonJi/events{/privacy}\"," +
                "\"received_events_url\":\"https://api.github.com/users/SeasonJi/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"Jie Ji\"," +
                "\"company\":null,\"blog\":\"https://seasonji.github.io/\",\"location\":\"Los Angeles, CA\",\"email\":null,\"hireable\":null,\"bio\":" +
                "\"USC'19 Computer Science & Applied Analytics\",\"public_repos\":6,\"public_gists\":0,\"followers\":5,\"following\":7," +
                "\"created_at\":\"2016-01-13T04:32:37Z\",\"updated_at\":\"2017-10-28T20:04:51Z\"}";


        assertEquals(expectResult, NetworkUtilities.get(url));
    }

    @Test
    public void emptyUrlTest() throws Exception {
        String url = "";
        assertNull( NetworkUtilities.get(url) );
    }

    @Test
    public void validUrlTest() throws Exception
    {
        String url = "https://api.github.com/users/SeasonJi";
        String expectResult = "{\"login\":\"SeasonJi\",\"id\":16678749,\"avatar_url\":\"https://avatars2.githubusercontent.com/u/16678749?v=4\",\"gravatar_id\":" +
                "\"\",\"url\":\"https://api.github.com/users/SeasonJi\",\"html_url\":\"https://github.com/SeasonJi\",\"followers_url\":" +
                "\"https://api.github.com/users/SeasonJi/followers\",\"following_url\":" +
                "\"https://api.github.com/users/SeasonJi/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/SeasonJi/gists{/gist_id}\"," +
                "\"starred_url\":\"https://api.github.com/users/SeasonJi/starred{/owner}{/repo}\",\"subscriptions_url\":" +
                "\"https://api.github.com/users/SeasonJi/subscriptions\",\"organizations_url\":\"https://api.github.com/users/SeasonJi/orgs\"," +
                "\"repos_url\":\"https://api.github.com/users/SeasonJi/repos\",\"events_url\":\"https://api.github.com/users/SeasonJi/events{/privacy}\"," +
                "\"received_events_url\":\"https://api.github.com/users/SeasonJi/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"Jie Ji\"," +
                "\"company\":null,\"blog\":\"https://seasonji.github.io/\",\"location\":\"Los Angeles, CA\",\"email\":null,\"hireable\":null,\"bio\":" +
                "\"USC'19 Computer Science & Applied Analytics\",\"public_repos\":6,\"public_gists\":0,\"followers\":5,\"following\":7," +
                "\"created_at\":\"2016-01-13T04:32:37Z\",\"updated_at\":\"2017-10-28T20:04:51Z\"}";


        assertEquals(expectResult, NetworkUtilities.get(url));
    }

    @Test
    public void post() throws Exception {
        String url = "https://api.github.com/repos/SeasonJi/ITP104";
        String toConvert = "{\"Time-Zone\":\"Europe/Amsterdam\"}";
        //toConvert = toConvert.replaceAll("\n", "\\n");
        String json = new Gson().toJson(toConvert);

        System.out.println(toConvert);
        assertEquals("whtevr", NetworkUtilities.put(url, json));

        //log in as an old user by facebook ID
//        User newUser = new User();
//        newUser.userName = "FILL_IN";
//        ArrayList<String> fList = new ArrayList<String>();
//        fList.add("a");
//        fList.add("b");
//        newUser.friendList = fList;
//        newUser.profilePhoto = "FILL_IN";
//        newUser.facebookID = "haha";
//        newUser.userID = null;
//        newUser.bio = null;
//        newUser.ratings = null;
//        newUser.allergy = null;
//        newUser.groupID = null;
//        newUser.postsID = null;
//        newUser.requestsID = null;
//        newUser.subscriptionID = null;
//        Gson gson = new Gson();
//        String userJson = gson.toJson(newUser);
//
//        //when url is null
//        String url = null;
//        assertNull( NetworkUtilities.post(url, userJson) );
//
//        //when url is null
//        url = "";
//        assertNull(NetworkUtilities.post(url, userJson));
//
//        //log in as old user
//        url = "http://10.120.67.208:8080" +  "/user";
//        assertEquals("23", NetworkUtilities.post(url, userJson));
//
//        //log in as new user
//        User newU = new User();
//        newU.userName = "FILL_IN";
//        ArrayList<String> frList = new ArrayList<String>();
//        frList.add("a");
//        frList.add("b");
//        newU.friendList = frList;
//        newU.profilePhoto = "FILL_IN";
//
//        Random rand = new Random();
//        int  n = rand.nextInt(10000000) + 1;
//        newU.facebookID = Integer.toString(n);//generate a random facebookID
//        newU.userID = null;
//        newU.bio = null;
//        newU.ratings = null;
//        newU.allergy = null;
//        newU.groupID = null;
//        newU.postsID = null;
//        newU.requestsID = null;
//        newU.subscriptionID = null;
//        Gson g = new Gson();
//        String uJson = gson.toJson(newU);
//
//        url = "http://10.120.67.208:8080" +  "/user";
//        assertEquals("27", NetworkUtilities.post(url, uJson));

    }

    @Test
    public void put() throws Exception {
        User newUser = new User();
        newUser.userID = 26;
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

        //when url is null
        String url = null;
        assertNull( NetworkUtilities.put(url, userJson) );

        //when url is null
        url = "";
        assertNull(NetworkUtilities.put(url, userJson));

        //update a old user
        url = "http://10.120.67.208:8080" +  "/user" + "/26";
        assertEquals("26", NetworkUtilities.put(url, userJson));


        //access a nonexistent user
        User newU = new User();
        newU.userID = 1000;
        newU.userName = "whatever";
        ArrayList<String> frList = new ArrayList<String>();
        frList.add("c");
        frList.add("d");
        newU.friendList = frList;
        newU.profilePhoto = "yay";
        newU.facebookID = "hurray";
        newU.bio = null;
        newU.ratings = null;
        newU.allergy = null;
        newU.groupID = null;
        newU.postsID = null;
        newU.requestsID = null;
        newU.subscriptionID = null;
        String uJson = gson.toJson(newU);

        url = "http://10.120.67.208:8080" +  "/user" + "/1000";
        //url = "https://api.github.com/resource?page=2";
        assertEquals("", NetworkUtilities.put(url, uJson));


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

        url = "http://10.120.67.208:8080" +  "/post" + "/22" + "/75";

        assertEquals("delete", NetworkUtilities.delete(url, postJson));


    }

}