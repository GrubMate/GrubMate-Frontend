package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ArrayList<String> friends;
    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        profileImage = (ImageView) findViewById(R.id.profile_image);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        final AccessToken token;
        token = AccessToken.getCurrentAccessToken();

        if (token != null) {
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
                                Log.i("userid",userID);
                                for (int l = 0; l < rawName.length(); l++) {
                                    friends.add(rawName.getJSONObject(l).getString("name"));
                                }
                                //  Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                //  startActivity(startMainActivity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();


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
                                        Picasso.with(LoginActivity.this).load(profilePicUrl).into(profileImage);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeAsync();

        }else {
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult login_result) {

                    new GraphRequest(
                            login_result.getAccessToken(),

                            "/me/friends",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    Log.i("friendlist 1", response.toString());
                                    AccessToken token = login_result.getAccessToken();
                                    try {
                                        JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                        ArrayList<String> friends = new ArrayList<String>();
                                        String userID = token.getUserId();
                                        Log.i("userid",userID);

                                        for (int l = 0; l < rawName.length(); l++) {
                                            friends.add(rawName.getJSONObject(l).getString("name"));
                                        }
                                        // Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                        //  startActivity(startMainActivity);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();

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
                                                Picasso.with(LoginActivity.this).load(profilePicUrl).into(profileImage);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).executeAsync();


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

}
