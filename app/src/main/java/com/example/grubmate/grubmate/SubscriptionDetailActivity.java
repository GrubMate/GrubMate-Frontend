package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

public class SubscriptionDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mSubscriptionName;
    private Button mUnsubscribeButton;
    private Subscription mSubscriptionData;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        mSubscriptionName = (TextView) findViewById(R.id.tv_subscription_name);
        mUnsubscribeButton = (Button) findViewById(R.id.b_unsubscribe);
        mUnsubscribeButton.setOnClickListener(this);
        Intent callIntent = getIntent();
        gson = new Gson();
        // If intent has extra text message extrave it and display it.
        if(callIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String extraText = callIntent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d("Subscription Detail", extraText);
            mSubscriptionData = gson.fromJson(extraText, Subscription.class);
            mSubscriptionName.setText(mSubscriptionData.query);

        }
    }

    @Override
    public void onClick(View view) {
        new SubscriptionDeleteTask().execute();
    }

    public class SubscriptionDeleteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return NetworkUtilities.delete(GrubMatePreference.getSubscriptionDeleteURL(PersistantDataManager.getUserID(), mSubscriptionData.subscriptionID), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                finish();
            } else {
                Toast.makeText(SubscriptionDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
            }
        }
    }
}
