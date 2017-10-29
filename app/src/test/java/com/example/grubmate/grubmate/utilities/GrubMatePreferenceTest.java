package com.example.grubmate.grubmate.utilities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ryan on 2017/10/27.
 */
public class GrubMatePreferenceTest {
    private String domain = "http://10.120.74.149:8080";
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getSubscribeActionURL() throws Exception {
        assertEquals(domain+"/subscription/123",GrubMatePreference.getSubscribeActionURL(123));
    }

    @Test
    public void getPostActionURl() throws Exception {
        assertEquals(domain+"/post/123",GrubMatePreference.getPostActionURl(123));
    }

    @Test
    public void getPostDeleteURL() throws Exception {
        assertEquals(domain+"/post/0/123",GrubMatePreference.getPostDeleteURL(0,123));
    }

    @Test
    public void getPastPostURL() throws Exception {
        assertEquals(domain+"/post/123/false",GrubMatePreference.getPastPostURL(123));
    }

    @Test
    public void getImageUrl() throws Exception {
        assertEquals(domain+"/image/123",GrubMatePreference.getImageUrl("123"));
    }

    @Test
    public void getFeedUrl() throws Exception {

    }

    @Test
    public void getUserPostUrl() throws Exception {
        assertEquals(domain+"/post/123/true",GrubMatePreference.getUserPostUrl(123));
    }

    @Test
    public void getNotificationURL() throws Exception {
        assertEquals(domain+"/notification/123",GrubMatePreference.getNotificationURL(123));
    }

    @Test
    public void getSearchURL() throws Exception {
        assertEquals(domain+"/search/123",GrubMatePreference.getSearchURL(123));
    }


    @Test
    public void getUserUrl() throws Exception {
        assertEquals(domain+"/user",GrubMatePreference.getUserUrl());
    }

    @Test
    public void getUserUrl1() throws Exception {
        assertEquals(domain+"/user/123",GrubMatePreference.getUserUrl(123));
    }

    @Test
    public void getSubscriptionURL() throws Exception {
        assertEquals(domain+"/subscription/123",GrubMatePreference.getSubscriptionURL(123));
    }

    @Test
    public void getSubscriptionDeleteURL() throws Exception {
        assertEquals(domain+"/subscription/42/123",GrubMatePreference.getSubscriptionDeleteURL(42,123));
    }

    @Test
    public void getRequestURL() throws Exception {
        assertEquals(domain+"/request",GrubMatePreference.getRequestURL());
    }

    @Test
    public void getAcceptRequestURL() throws Exception {
        assertEquals(domain+"/request/42/123/1",GrubMatePreference.getAcceptRequestURL(42,123));
    }

    @Test
    public void getDenyRequestURL() throws Exception {
        assertEquals(domain+"/request/42/123/0",GrubMatePreference.getDenyRequestURL(42,123));
    }

    @Test
    public void getRequestURL1() throws Exception {
        assertEquals(domain+"/request/123",GrubMatePreference.getRequestURL(123));
    }

    @Test
    public void getRequestListUrl() throws Exception {
        assertEquals(domain+"/request/42/123",GrubMatePreference.getRequestListUrl(42,123));
    }

}