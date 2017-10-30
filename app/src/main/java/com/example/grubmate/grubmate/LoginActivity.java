package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ArrayList<String> friends;
    private ImageView profileImage;
    private String facebookID;
    private String photoURL;
    private ArrayList<String> friendList;
    private String facebookUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
       // profileImage = (ImageView) findViewById(R.id.profile_image);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        final AccessToken token;
        token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            getFriendList(token);
        } else {
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult login_result) {
                    getFriendList(login_result.getAccessToken());

//                    new GraphRequest(
//                            token,
//                            "/me/friends",
//                            null,
//                            HttpMethod.GET,
//                            new GraphRequest.Callback() {
//                                public void onCompleted(GraphResponse response) {
//                                    Log.i("friendlist 1", response.toString());
//                                    try {
//                                        JSONArray rawName = response.getJSONObject().getJSONArray("data");
//                                        ArrayList<String> friends = new ArrayList<String>();
//                                        String userID = token.getUserId();
//                                        facebookID = userID;
//                                        Log.i("userid",userID);
//                                        for (int l = 0; l < rawName.length(); l++) {
//                                            friends.add(rawName.getJSONObject(l).getString("id"));
//                                        }
//                                        friendList = friends;
//                                        getProfileImage(token);
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                    ).executeAsync();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void getFriendList(final AccessToken token){
        new GraphRequest(
                token,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.i("friendlist 1", response.toString());
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            ArrayList<String> friends = new ArrayList<String>();
                            String userID = token.getUserId();
                            facebookID = userID;
                            Log.i("userid",userID);
                            for (int l = 0; l < rawName.length(); l++) {
                                friends.add(rawName.getJSONObject(l).getString("id"));
                            }
                            friendList = friends;
                            getProfileImage(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
        ).executeAsync();
    }
    public void getProfileImage(final AccessToken token){
        Bundle params = new Bundle();
        params.putString("fields", "id,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();

                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Log.i("url1",profilePicUrl);
                                    photoURL = profilePicUrl;
                                    // Picasso.with(LoginActivity.this).load(profilePicUrl).into(profileImage);
                                    getName(token);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }
    public void getName(final AccessToken token){
        GraphRequest.Callback gCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                if (response != null && response.getJSONObject() != null && response.getJSONObject().has("first_name"))
                {
                    try {
                        facebookUsername = response.getJSONObject().getString("name");
                        new LoginActionTask().execute(GrubMatePreference.getUserUrl());
                    } catch (JSONException e) {

                    }
                }
            }
        };
        new GraphRequest(AccessToken.getCurrentAccessToken(),"/me?fields=id,name,gender,email,first_name,last_name", null,HttpMethod.GET, gCallback).executeAsync();
    }






// Async Task

    public class LoginActionTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            User newUser = new User();
            newUser.userName = facebookUsername;
            newUser.friendList = friendList;
            newUser.profilePhoto = photoURL;
            newUser.facebookID = facebookID;
            newUser.userID = null;
            newUser.bio = null;
            newUser.rating = null;
            newUser.allergy = null;
            newUser.groupID = null;
            newUser.postsID = null;
            newUser.requestsID = null;
            newUser.subscriptionID = null;
            Gson gson = new Gson();
            String userJson = gson.toJson(newUser);
            Log.d("Login", userJson);
            try {

                return NetworkUtilities.post(GrubMatePreference.getUserUrl(),userJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if(postActionResponse==null||postActionResponse.length() == 0 || postActionResponse.length()>20) return;
            PersistantDataManager.setUserID(Integer.parseInt(postActionResponse));
            Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(startMainActivity);
        }
    }

}











