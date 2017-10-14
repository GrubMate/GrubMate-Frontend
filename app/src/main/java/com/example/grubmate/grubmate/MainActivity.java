package com.example.grubmate.grubmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.MockData;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FeedAdapter.FeedAdapterOnClickHandler {
    public static final String TAG = "MainActivity";
    private RecyclerView mFeedView;
    private FeedAdapter mFeedAdapter;
    private Button mPostButton;
    private Button mSubscribeButton;
    private Context context;
    private BroadcastReceiver notificationReceiver;
    public final static String BROADCAST_ACTION = "com.example.action";

    private ProgressBar mFeedProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up feed
        mFeedView = (RecyclerView) findViewById(R.id.rv_feed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFeedView.setLayoutManager(layoutManager);
        mFeedAdapter = new FeedAdapter(this);
        mFeedView.setAdapter(mFeedAdapter);
        new FetchFeedListTask().execute(GrubMatePreference.getFeedUrl(PersistantDataManager.getUserID()));
//        mFeedAdapter.setFeedData(MockData.mockFeedData);

        mPostButton = (Button) findViewById(R.id.b_home_post);
        mSubscribeButton = (Button) findViewById(R.id.b_home_subscribe);

        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "update received", Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(notificationReceiver, intentFilter);

        context = this;
        mFeedProgressBar = (ProgressBar) findViewById(R.id.pb_feed);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = PostActionActivity.class;

                // construct the intent
                Intent startDetailActivityIntent = new Intent(context, destinationActivity);

                // put extra data into this intent
                startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

                // start the intent
                startActivity(startDetailActivityIntent);

            }
        });

        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = SubscribeActionActivity.class;

                // construct the intent
                Intent startDetailActivityIntent = new Intent(context, destinationActivity);

                // put extra data into this intent
                startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

                // start the intent
                startActivity(startDetailActivityIntent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Class destinationActivity = SearchActivity.class;

            // construct the intent
            Intent startDetailActivityIntent = new Intent(context, destinationActivity);

            // put extra data into this intent
            startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

            // start the intent
            startActivity(startDetailActivityIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class destinationActivity = MainActivity.class;
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_subscriptions) {
            destinationActivity = SubscriptionsActivity.class;
        } else if (id == R.id.nav_posts) {

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_profile) {
            destinationActivity = ProfileActivity.class;
        } else if (id == R.id.nav_notification_settings) {

        } else if (id==R.id.nav_application_settings) {

        }
        // construct the intent
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent startDetailActivityIntent = new Intent(context, destinationActivity);

        // put extra data into this intent
        startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

        // start the intent
        startActivity(startDetailActivityIntent);

        return true;
    }

    @Override
    public void onClick(Post feedItemData) {
        Class destinationActivity = FeedDetailActivity.class;

        // construct the intent
        Intent startDetailActivityIntent = new Intent(context, destinationActivity);

        // put extra data into this intent
        startDetailActivityIntent.putExtra("post_data", new Gson().toJson(feedItemData));

        // start the intent
        startActivity(startDetailActivityIntent);

    }

    public class FetchFeedListTask extends AsyncTask<String, Integer, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response = NetworkUtilities.get(baseUrl);
                return JsonUtilities.getFeedItems(response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> feedItems) {
            if (feedItems != null) {
                mFeedAdapter.setFeedData(feedItems);
                mFeedProgressBar.setVisibility(View.INVISIBLE);
                mFeedProgressBar.getLayoutParams().height = 0;
                mFeedView.setVisibility(View.VISIBLE);
            }
        }
    }


}
