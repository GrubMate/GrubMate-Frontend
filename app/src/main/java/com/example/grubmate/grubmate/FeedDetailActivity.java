package com.example.grubmate.grubmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedDetailActivity extends AppCompatActivity {
    private TextView mDetailNameDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        mDetailNameDisplay = (TextView) findViewById(R.id.tv_feed_detail_name);
        Intent callIntent = getIntent();

        // If intent has extra text message extrave it and display it.
        if(callIntent.hasExtra("post_data")) {
            String extraText = callIntent.getStringExtra("post_data");
            mDetailNameDisplay.setText(extraText);
        }
    }
}
