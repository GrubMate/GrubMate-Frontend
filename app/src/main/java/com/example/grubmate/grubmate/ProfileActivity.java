package com.example.grubmate.grubmate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        new PostActionTask().execute(GrubMatePreference.getUserUrl(PersistantDataManager.getUserID()));
        gson = new Gson();
    }

    public class PostActionTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }
            try {
                return NetworkUtilities.get(GrubMatePreference.getSearchURL(PersistantDataManager.getUserID()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if(postActionResponse == null || postActionResponse.length()==0) {
                Toast.makeText(ProfileActivity.this, "Error occurs during fetching user data", Toast.LENGTH_SHORT).show();
            } else {
                User user = gson.fromJson(postActionResponse, User.class);
                mProfileName.setText(user.userName);
                mProfileRatingBar.setRating(user.ratings[0]);
                Picasso.with(ProfileActivity.this).load(user.profilePhoto).into(mProfileAvatar);
            }
        }
    }

}
