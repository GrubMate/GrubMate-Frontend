package com.example.grubmate.grubmate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import com.example.grubmate.grubmate.dataClass.Subscription;

public class SubscribeActionActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText subscribeItemNameText;
    private EditText subscribeItemTagsText;
    private EditText subscribeItemAllergyText;
    private Spinner subscribeItemCategorySpinner;
    private Spinner subscribeItemTimeSpinner;
    private Integer subscriberID;
    public String[] tags;
    public String category;
    public String query;
    public String timePeriod;
    public Integer[] matchedPostIDs;
    public Boolean[] allergyInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_action);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_subscribe);
        fab.setOnClickListener(this);

        subscribeItemNameText = (EditText) findViewById(R.id.et_subscribe_item_name);
        subscribeItemTagsText = (EditText) findViewById(R.id.et_subscribe_item_tags);

        subscribeItemCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subscribeItemCategorySpinner.setAdapter(categoryAdapter);


        subscribeItemTimeSpinner = (Spinner) findViewById(R.id.spinner_time);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_period, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        subscribeItemTimeSpinner.setAdapter(timeAdapter);
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validateForm() {
        if (subscribeItemNameText.getText().length() >0) {
            return true;
        } else if (subscribeItemTagsText.getText().length() > 0) {
            return true;
        } else if (subscribeItemCategorySpinner.getSelectedItem().toString() != "Category") {
            return true;
        } else if (subscribeItemTimeSpinner.getSelectedItem().toString() != "Time Period") {
            return true;
        }
        return false;
    };

    @Override
    public void onClick(View view) {
        if(validateForm()) {
            query = subscribeItemNameText.getText().toString();
            allergyInfo = new Boolean[3];
            category = subscribeItemCategorySpinner.getSelectedItem().toString();
            if(Objects.equals(category, "Category")) category = null;
            timePeriod = subscribeItemTimeSpinner.getSelectedItem().toString();
            if(Objects.equals(timePeriod, "Time Period")) timePeriod = null;
            // TODO: modify this into users' real id in production
            String tagString = subscribeItemTagsText.getText().toString();
            tags = tagString.split(",");
            subscriberID = 0;
            new subscribeActionTask().execute(GrubMatePreference.getSubscribeActionURL(subscriberID));
        } else {
            showShortToast("Please fill out all necessary fields before subscribeing");
        }
    }

    public class subscribeActionTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }
            String baseUrl = params[0];
            Subscription newSubscription = new Subscription();

            newSubscription.tags = tags;
            newSubscription.category = Objects.equals(category, "Category") ?null:category;
            newSubscription.allergyInfo = null;
            newSubscription.subscriberID = subscriberID;
            newSubscription.timePeriod = Objects.equals(timePeriod, "Time Period") ?null:timePeriod;
            newSubscription.query = query;
            newSubscription.isActive = true;
            newSubscription.matchedPostIDs = null;
            newSubscription.subscriberID = PersistantDataManager.getUserID();

            Gson gson = new Gson();
            String subscriptionJson = gson.toJson(newSubscription);
            try {
                String response = NetworkUtilities.post(GrubMatePreference.getSubscriptionURL(PersistantDataManager.getUserID()), subscriptionJson);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String subscribeActionResponse) {
            if (subscribeActionResponse != null) {
                showShortToast("Succeed");
                finish();
            } else {
                showShortToast("Error: please retry");
            }
        }
    }
}
