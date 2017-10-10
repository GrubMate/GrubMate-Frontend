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

import java.io.IOException;

public class SubscribeActionActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText subscribeItemNameText;
    private EditText subscribeItemTagsText;
    private EditText subscribeItemAllergyText;
    private Spinner subscribeItemCategorySpinner;
    private Spinner subscribeItemTimeSpinner;
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
        subscribeItemAllergyText = (EditText) findViewById(R.id.et_subscribe_item_allergy);

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
        if (subscribeItemNameText.getText().length() == 0) {
            return false;
        } else if (subscribeItemTagsText.getText().length() == 0) {
            return false;
        }
        return true;
    };

    @Override
    public void onClick(View view) {
        if(validateForm()) {
            new SubscribeActionActivity.subscribeActionTask().execute(GrubMatePreference.subscribeActionURL);

        } else {
            showShortToast("Please fill out all necessary fields before subscribeing");
        }
    }

    public class subscribeActionTask extends AsyncTask<String, Integer, String> {

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
        protected void onPostExecute(String subscribeActionResponse) {
            if (subscribeActionResponse != null) {
                showShortToast("Succeed");
            }
        }
    }
}