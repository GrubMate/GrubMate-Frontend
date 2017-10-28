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
    public void setUp() throws Exception
    {

    }

    @Test
    public void nullUrlTest() throws Exception
    {
        String url = null;
        assertNull( NetworkUtilities.get(url) );

        url = "";
        assertNull( NetworkUtilities.get(url) );
    }

    @Test
    public void emptyUrlTest() throws Exception
    {
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
    

}