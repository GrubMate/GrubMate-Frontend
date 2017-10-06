package com.example.grubmate.grubmate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;

import java.io.IOException;

public class PostActionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText postItemNameText;
    private EditText postItemTagsText;
    private EditText postItemDescriptionText;
    private EditText postItemQuantityText;
    private EditText postItemAllergyText;
    private Spinner postItemCategorySpinner;
    private Spinner postItemTimeSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_action);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_post);
        fab.setOnClickListener(this);

        postItemNameText = (EditText) findViewById(R.id.et_post_item_name);
        postItemTagsText = (EditText) findViewById(R.id.et_post_item_tags);
        postItemDescriptionText = (EditText) findViewById(R.id.et_post_item_description);
        postItemAllergyText = (EditText) findViewById(R.id.et_post_item_allergy);
        postItemQuantityText = (EditText) findViewById(R.id.et_post_item_quantity);

        postItemCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postItemCategorySpinner.setAdapter(categoryAdapter);


        postItemTimeSpinner = (Spinner) findViewById(R.id.spinner_time);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_period, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        postItemTimeSpinner.setAdapter(timeAdapter);
    }

    public void showShortToast(String msg) {
       Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validateForm() {
        if (postItemNameText.getText().length() == 0) {
            return false;
        } else if (postItemQuantityText.getText().length() == 0) {
            return false;
        } else if (postItemTagsText.getText().length() == 0) {
            return false;
        }
        return true;
    };

    @Override
    public void onClick(View view) {
        if(validateForm()) {
            new PostActionTask().execute(GrubMatePreference.postActionURl);

            } else {
            showShortToast("Please fill out all necessary fields before posting");
        }
    }

    public class PostActionTask extends AsyncTask<String, Integer, String> {

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
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                showShortToast("Succeed");
            }
        }
    }
}

/*
* Post send to server should have
* posterID posterPhoto tags timePeriod description postAddress
* totalQuality
* isPostActive
*
* */