package com.example.grubmate.grubmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grubmate.grubmate.adapters.SubscriptionAdapter;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class SubscriptionsActivity extends AppCompatActivity implements SubscriptionAdapter.SubscriptionAdapterOnClickHandler {
    private  SubscriptionAdapter mSubscriptionAdapter;
    private RecyclerView mSubscriptionView;
    private ProgressBar mSubscriptionProgressBar;
    private TextView mEmptyText;
    private ArrayList<Subscription> subscriptionData;
    Context context;
    private IntentFilter intentFilter;
    public static final String BROADCAST_ACTION = "com.example.grubmate.grubmate.notification.subscriptions";
    private BroadcastReceiver mNotificationReceiver;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        mSubscriptionView = (RecyclerView) findViewById(R.id.rv_subscription);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mSubscriptionView.setLayoutManager(layoutManager);
        mSubscriptionAdapter = new SubscriptionAdapter(this);
        mSubscriptionView.setAdapter(mSubscriptionAdapter);
        context = this;
        mSubscriptionProgressBar = (ProgressBar) findViewById(R.id.pb_subscription);
        mEmptyText = (TextView) findViewById(R.id.tv_subscription_empty);
        mNotificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                new SubscriptionsTask().execute(GrubMatePreference.getUserPostUrl(PersistantDataManager.getUserID()));
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        gson = new Gson();
        registerReceiver(mNotificationReceiver, intentFilter);
        new SubscriptionsTask().execute(GrubMatePreference.getSubscriptionURL(PersistantDataManager.getUserID()));
    }

    @Override
    public void onClick(Subscription SubscriptionItemData) {
        String serializedData = gson.toJson(SubscriptionItemData);
        Class destinationActivity = SubscriptionDetailActivity.class;
        Intent destinationIntent = new Intent(this, destinationActivity);
        destinationIntent.putExtra(Intent.EXTRA_TEXT, serializedData);
        startActivity(destinationIntent);
    }

    @Override
    protected void onRestart() {
        registerReceiver(mNotificationReceiver, intentFilter);
        new SubscriptionsTask().execute(GrubMatePreference.getSubscriptionURL(PersistantDataManager.getUserID()));
        super.onRestart();
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

    public class SubscriptionsTask extends AsyncTask<String, Integer, ArrayList<Subscription>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEmptyText.setVisibility(View.INVISIBLE);
            mSubscriptionView.setVisibility(View.INVISIBLE);
            mSubscriptionProgressBar.getLayoutParams().height = (int)getResources().getDimension(R.dimen.pb_height);
            mSubscriptionProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Subscription> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String body = "";
                String response = NetworkUtilities.get(GrubMatePreference.getSubscriptionURL(PersistantDataManager.getUserID()));
                return JsonUtilities.getSubscriptionItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Subscription> subscriptionsList) {
            if (subscriptionsList != null && subscriptionsList.size() > 0) {
                mSubscriptionAdapter.setSubscriptionData(subscriptionsList);
                mSubscriptionView.setVisibility(View.VISIBLE);
                mSubscriptionProgressBar.getLayoutParams().height = 0;
                mSubscriptionProgressBar.setVisibility(View.INVISIBLE);
            } else if (subscriptionsList==null || subscriptionsList.size() == 0) {
                mEmptyText.setVisibility(View.VISIBLE);
                mSubscriptionProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

}
