package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileAvatar;
    private TextView mProfileName;
    private RatingBar mProfileRatingBar;
    private Gson gson;
    private int userID;
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
        new PostActionTask().execute(GrubMatePreference.getUserUrl(PersistantDataManager.getUserID()));

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
