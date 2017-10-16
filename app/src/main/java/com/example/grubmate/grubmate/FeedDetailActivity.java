package com.example.grubmate.grubmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

public class FeedDetailActivity extends AppCompatActivity {
    private TextView mNameText;
    private TextView mQuantityText;
    private LinearLayout posterLayout;
    private Button mEditButton;
    private Button mDeleteButton;
    private LinearLayout requesterLayout;
    private Button mPosterButton;
    private Button mRequestButton;


    private Post mPostData;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        mNameText = (TextView) findViewById(R.id.tv_feed_detail_name);
        mQuantityText = (TextView) findViewById(R.id.tv_feed_quantitiy);
        mPosterButton = (Button) findViewById(R.id.b_poster);
        mPosterButton.setOnClickListener(new PosterButtonListener());
        mDeleteButton = (Button) findViewById(R.id.b_delete);
        mEditButton = (Button) findViewById(R.id.b_edit);
        mRequestButton = (Button) findViewById(R.id.b_request);
        requesterLayout = (LinearLayout) findViewById(R.id.requester_layout);
        posterLayout = (LinearLayout) findViewById(R.id.poster_layout);
        Intent callIntent = getIntent();
        gson = new Gson();
        // If intent has extra text message extrave it and display it.
        if(callIntent.hasExtra("post_data")) {
            String extraText = callIntent.getStringExtra("post_data");
            mPostData = gson.fromJson(callIntent.getStringExtra("post_data"), Post.class);
            mNameText.setText(mPostData.title);
            mQuantityText.setText(String.valueOf(mPostData.leftQuantity));
            if (mPostData.posterID == PersistantDataManager.getUserID()) {
               posterLayout.setVisibility(View.VISIBLE);
                requesterLayout.setVisibility(View.INVISIBLE);
            } else {
                posterLayout.setVisibility(View.INVISIBLE);
                requesterLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mNameText.setText("Error: no data received");
        }
    }


    class PosterButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent posterIntent = new Intent(FeedDetailActivity.this, ProfileActivity.class);
            posterIntent.putExtra("user_id", String.valueOf(mPostData.posterID));
            startActivity(posterIntent);

        }
    }

    class EditButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }

    class RequestButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    class RequesterButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
}
