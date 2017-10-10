package com.example.grubmate.grubmate;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.grubmate.grubmate.utilities.NetworkUtilities;

import java.io.IOException;

public class SubscriptionsActivity extends AppCompatActivity implements SubscriptionAdapter.SubscriptionAdapterOnClickHandler {
    private  SubscriptionAdapter mSubscriptionAdapter;
    private RecyclerView mSubscriptionView;
    Context context;
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
    }

    @Override
    public void onClick(String SubscriptionItemData) {

    }

    public class SubscriptionsTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String body = "";
                String response = NetworkUtilities.post(baseUrl, body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String searchResponse) {
            if (searchResponse != null) {
                Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
