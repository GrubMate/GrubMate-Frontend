package com.example.grubmate.grubmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SubscriptionDetailActivity extends AppCompatActivity {
    private TextView mSubscriptionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        mSubscriptionName = (TextView) findViewById(R.id.tv_subscription_name);
        Intent callIntent = getIntent();

        // If intent has extra text message extrave it and display it.
        if(callIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String extraText = callIntent.getStringExtra(Intent.EXTRA_TEXT);
            mSubscriptionName.setText(extraText);
        }
    }
}
