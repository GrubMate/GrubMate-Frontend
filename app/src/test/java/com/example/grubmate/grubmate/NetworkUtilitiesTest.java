package com.example.grubmate.grubmate.utilities;

import android.net.Network;

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
    String baseURL;
    @Before
    public void setUp() throws Exception
    {
        baseURL = "https://4dc66e3c-3d29-434b-8a51-eb82dd0c9704.mock.pstmn.io";
    }

    @Test
    public void getNullUrlTest() throws Exception
    {
        String url = null;
        assertNull( NetworkUtilities.get(url) );
    }

    @Test
    public void getEmptyUrlTest() throws Exception
    {
        String url = "";
        assertNull( NetworkUtilities.get(url) );
    }

    @Test
    public void getTest() throws Exception
    {
        String expectResult = "get";
        String url = baseURL+"/getTest";

        assertEquals(expectResult, NetworkUtilities.get(url));
    }













}