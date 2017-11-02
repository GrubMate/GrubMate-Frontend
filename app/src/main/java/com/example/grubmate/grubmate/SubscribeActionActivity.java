package com.example.grubmate.grubmate;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.grubmate.grubmate.dataClass.Subscription;

public class SubscribeActionActivity extends AppCompatActivity implements View.OnClickListener, DoubleDateAndTimePickerDialog.Listener{
    private EditText subscribeItemNameText;
    private EditText subscribeItemTagsText;
    private EditText subscribeItemAllergyText;
    private Spinner subscribeItemCategorySpinner;
    private Button timeButton;
    private Integer subscriberID;
    public String[] tags;
    public String category;
    public String query;
    public String timePeriod;
    private String startTime;
    private String endTime;
    public Integer[] matchedPostIDs;
    public Boolean[] allergyInfo;
    public DoubleDateAndTimePickerDialog.Builder doubleDateAndTimePickerDialogBuilder;
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
        startTime = null;
        endTime = null;
        doubleDateAndTimePickerDialogBuilder = new DoubleDateAndTimePickerDialog
                .Builder(this)
                .backgroundColor(Color.WHITE)
                .mainColor(Color.argb(255, 63,81,181))
                .title("Time Period")
                .minutesStep(30)
                .tab0Text("Start")
                .tab1Text("End")
                .listener(this);
        timeButton = (Button) findViewById(R.id.b_subscription_item_unsubscribe);
        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                doubleDateAndTimePickerDialogBuilder.display();
            }
        });




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
        } else if (startTime!=null&&endTime!=null) {
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

    @Override
    public void onDateSelected(List<Date> dates) {
        Date start = dates.get(0);
        Date end = dates.get(1);
        startTime = start.toString();
        endTime = end.toString();
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
            newSubscription.timePeriod = new String[]{startTime, endTime};
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
