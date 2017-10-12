package com.example.grubmate.grubmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    ImageView mProfileAvatar;
    TextView mProfileName;
    EditText mProfileDescription;
    RatingBar mProfileRatingBar;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfileAvatar = (ImageView) findViewById(R.id.i_profile_avatar);
        mProfileName = (TextView) findViewById(R.id.tv_profile_name);
        mProfileDescription = (EditText) findViewById(R.id.et_profile_description);
        mProfileRatingBar = (RatingBar) findViewById(R.id.rb_profile);
    }
}
