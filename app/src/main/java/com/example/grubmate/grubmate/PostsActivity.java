package com.example.grubmate.grubmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grubmate.grubmate.adapters.FeedAdapter;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.grubmate.grubmate.adapters.FeedAdapter.FeedDetailActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class PostsActivity extends AppCompatActivity implements FeedAdapter.FeedAdapterOnClickHandler, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "PostActivity";
    private ArrayList<Post> postsData;
    private RecyclerView mPostView;
    private FeedAdapter mPostAdapter;
    private ProgressBar mPostProgressBar;
    private TextView mEmptyText;
    private BroadcastReceiver mNotificationReceiver;
    private Gson gson;
    private IntentFilter intentFilter;


    public static final String BROADCAST_ACTION = "com.example.grubmate.grubmate.notification.post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        gson = new Gson();

        mPostView = (RecyclerView) findViewById(R.id.rv_post);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPostView.setLayoutManager(layoutManager);
        mPostAdapter = new FeedAdapter(this);
        mPostView.setAdapter(mPostAdapter);

        mPostProgressBar = (ProgressBar) findViewById(R.id.pb_post);
        mEmptyText = (TextView) findViewById(R.id.tv_post_empty_text);
        mNotificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                new FetchPostListTask().execute(GrubMatePreference.getUserPostUrl(PersistantDataManager.getUserID()));
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mNotificationReceiver, intentFilter);
        new FetchPostListTask().execute(GrubMatePreference.getUserPostUrl(PersistantDataManager.getUserID()));
    }

    protected void onRestart() {
        super.onRestart();
        registerReceiver(mNotificationReceiver, intentFilter);
        new FetchPostListTask().execute(GrubMatePreference.getUserPostUrl(PersistantDataManager.getUserID()));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNotificationReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(Post feedItemData) {
        // start detail page with the data;
        Class destinationActivity = FeedAdapter.FeedDetailActivity.class;

        Intent startDetailActivityIntent = new Intent(this, destinationActivity);

        startDetailActivityIntent.putExtra("post_data", gson.toJson(feedItemData));

        startActivity(startDetailActivityIntent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class FetchPostListTask extends AsyncTask<String, Integer, ArrayList<Post>> {
        @Override
        protected void onPreExecute() {
            mPostView.setVisibility(View.INVISIBLE);
            mPostProgressBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.pb_height);
            mPostProgressBar.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Post> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response = NetworkUtilities.get(baseUrl);
                if (response == null || response.length() == 0)
                    return null;
                return JsonUtilities.getFeedItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> postItems) {
            if (postItems != null) {
                postsData = postItems;
                mPostAdapter.setFeedData(postsData);
                mPostProgressBar.setVisibility(View.INVISIBLE);
                mPostProgressBar.getLayoutParams().height = 0;
                mPostView.setVisibility(View.VISIBLE);
                if (postsData.size() < 1)
                    mEmptyText.setVisibility(View.VISIBLE);
            } else {
                mEmptyText.setVisibility(View.VISIBLE);
                mPostProgressBar.setVisibility(View.INVISIBLE);
                mPostProgressBar.getLayoutParams().height = 0;

            }
        }
    }





}
