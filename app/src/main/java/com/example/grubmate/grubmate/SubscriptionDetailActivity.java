package com.example.grubmate.grubmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.grubmate.grubmate.dataClass.Subscription;
import com.google.gson.Gson;

import org.w3c.dom.Text;

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
            mSubscriptionData = gson.fromJson(extraText, Subscription.class);
            mSubscriptionName.setText(mSubscriptionData.query);

        }
    }

    @Override
    public void onClick(View view) {

    }
}
