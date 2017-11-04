package com.example.grubmate.grubmate;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.grubmate.grubmate.fragments.AllergySetting;
import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.GroupFragment;
import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;
import com.example.grubmate.grubmate.fragments.PostFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;
import com.example.grubmate.grubmate.fragments.SubscriptionFragment;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FeedFragment.OnFragmentInteractionListener,
        ProfileFragment.OnProfileFragmentInteractionListener,
        SubscriptionFragment.OnSubcriptionFragmentInteractionListener,
        PostFragment.OnPostFragmentInteractionListener,
        NotificationCenterFragment.OnNotificationFragmentInteractionListener
        , GroupFragment.OnGroupFragmentInteractionListener,
        AllergySetting.OnAllergyFragmentInteractionListener{
    public static final String TAG = "MainActivity";
    private Context context;
    public static final int SEARCH_IDENTIFICATION_CODE = 91;
    // used for service
    private BroadcastReceiver mNotificationReceiver;
    private IntentFilter intentFilter;
    private FrameLayout fragmentContainer;

    public final static String BROADCAST_ACTION = "com.example.grubmate.grubmate.notification";
    private NotificationService.NotificationBinder notificationBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            notificationBinder = (NotificationService.NotificationBinder) service;
            notificationBinder.startPolling();
            ;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNotificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mNotificationReceiver, intentFilter);
        // start the service for notification

        Intent bindIntent = new Intent(context, NotificationService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        Intent startIntent = new Intent(this, NotificationService.class);
        startService(startIntent);

        // Put in default fragment
        FeedFragment feedFragment = FeedFragment.newInstance(null, null,null);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment_container, feedFragment).commit();

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
            Intent startActivityIntent = new Intent(context, destinationActivity);

            // put extra data into this intent
            startActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

            // start the intent
            startActivityForResult(startActivityIntent, SEARCH_IDENTIFICATION_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment destinationFragment = null;
        Class destinationActivity = MainActivity.class;
        if (id == R.id.nav_home) {
            destinationFragment = FeedFragment.newInstance(null, null,null);
        } else if (id == R.id.nav_subscriptions) {
            destinationFragment = SubscriptionFragment.newInstance(null,null);
        } else if (id == R.id.nav_posts) {
            destinationFragment = PostFragment.newInstance(null, null);
        } else if (id == R.id.nav_notification) {
            destinationFragment = NotificationCenterFragment.newInstance(null, null);
        } else if (id == R.id.nav_profile) {
            destinationFragment = ProfileFragment.newInstance(PersistantDataManager.getUserID(),null);
        } else if (id == R.id.nav_group_settings) {
            destinationFragment = GroupFragment.newInstance(null,null);
        } else if (id == R.id.nav_allergy_settings) {
            destinationFragment = AllergySetting.newInstance(null,null);
        }else if(id == R.id.nav_logout){

            GraphRequest delPermRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/{user-id}/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    if(graphResponse!=null){
                        FacebookRequestError error =graphResponse.getError();
                        if(error!=null){
                            Log.e(TAG, error.toString());
                        }else {
                            finish();
                        }
                    }
                }
            });
            Log.d(TAG,"Executing revoke permissions with graph path" + delPermRequest.getGraphPath());
            delPermRequest.executeAsync();
            LoginManager.getInstance().logOut();
            Intent loginscreen=new Intent(this,LoginActivity.class);
            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginscreen);
            this.finish();
           // android.os.Process.killProcess(android.os.Process.myPid());
        }
        // construct the intent
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(destinationFragment !=null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_main_fragment_container, destinationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        return true;
    }

    @Override
    public void onNotificationFragmentInteraction(Uri uri) {
        Log.d("Main", uri.toString());
    }



    protected void onRestart() {
        registerReceiver(mNotificationReceiver, intentFilter);
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(mNotificationReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void onPostFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
