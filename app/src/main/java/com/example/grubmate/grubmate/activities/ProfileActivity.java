package com.example.grubmate.grubmate.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.PastPostAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileAvatar;
    private TextView mProfileName;
    private RatingBar mProfileRatingBar;
    private Gson gson;
    private int userID;
    private RecyclerView mRecyclerView;
    private PastPostAdapter mPastPostAdapter;
    private ArrayList<Post> mPastPostList;
    private static final boolean test = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfileAvatar = (ImageView) findViewById(R.id.i_profile_avatar);
        mProfileName = (TextView) findViewById(R.id.tv_profile_name);
        mProfileRatingBar = (RatingBar) findViewById(R.id.rb_profile);
        gson = new Gson();
        userID = PersistantDataManager.getUserID();
        Intent callIntent = getIntent();
        if(callIntent.hasExtra("user_id")) {
            userID = Integer.parseInt(callIntent.getStringExtra("user_id"));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_past_orders);
        mPastPostList = new ArrayList<Post>();
        mPastPostAdapter = new PastPostAdapter(mPastPostList);
        mPastPostAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPastPostAdapter);

        new PostActionTask().execute(GrubMatePreference.getUserUrl(PersistantDataManager.getUserID()));
        new PastPostTask().execute(userID);

    }

    public class PostActionTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }
            try {
                return NetworkUtilities.get(GrubMatePreference.getUserUrl(userID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if(test) {
                User user = MockData.getUser(0);
                mProfileName.setText(user.userName);
                if(user.ratings!=null&&user.ratings.length>0&&user.ratings[0]!=null) {
                    mProfileRatingBar.setRating(user.ratings[0]);
                } else {
                    mProfileRatingBar.setRating(5);
                }
                Picasso.with(ProfileActivity.this).load(user.profilePhoto).into(mProfileAvatar);
            } else {
                Log.d("profile", postActionResponse);
                if(postActionResponse == null || postActionResponse.length()==0) {
                    Toast.makeText(ProfileActivity.this, "Error occurs during fetching user data", Toast.LENGTH_SHORT).show();
                } else {
                    User user = gson.fromJson(postActionResponse, User.class);
                    mProfileName.setText(user.userName);
                    if(user.ratings!=null&&user.ratings.length>0&&user.ratings[0]!=null) {
                        mProfileRatingBar.setRating(user.ratings[0]);
                    } else {
                        mProfileRatingBar.setRating(5);
                    }
                    Picasso.with(ProfileActivity.this).load(user.profilePhoto).into(mProfileAvatar);
                }
            }
        }
    }

    public class PastPostTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            try {
                return NetworkUtilities.get(GrubMatePreference.getPastPostURL(userID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            mPastPostList = JsonUtilities.getFeedItems(postActionResponse);
            mPastPostAdapter.setNewData(mPastPostList);
        }
    }
}
