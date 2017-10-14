package com.example.grubmate.grubmate;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText searchItemNameText;
    private EditText searchItemTagsText;
    private EditText searchItemAllergyText;
    private Spinner searchItemCategorySpinner;
    private Spinner searchItemTimeSpinner;
    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchItemNameText = (EditText) findViewById(R.id.et_search_item_name);
        searchItemTagsText = (EditText) findViewById(R.id.et_search_item_tags);
        searchItemAllergyText = (EditText) findViewById(R.id.et_search_item_allergy);
        searchItemCategorySpinner = (Spinner) findViewById(R.id.search_spinner_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchItemCategorySpinner.setAdapter(categoryAdapter);


        searchItemTimeSpinner = (Spinner) findViewById(R.id.search_spinner_time);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_period, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        searchItemTimeSpinner.setAdapter(timeAdapter);
        searchButton = (Button) findViewById(R.id.b_search_button);
        searchButton.setOnClickListener(this);
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validateForm() {
//        if (searchItemNameText.getText().length() == 0) {
//            return false;
//        } else if (searchItemTagsText.getText().length() == 0) {
//            return false;
//        }
        return true;
    };

    @Override
    public void onClick(View view) {
        if(validateForm()) {
            new SearchActivity.SearchTask().execute(GrubMatePreference.searchURL);
        } else {
            showShortToast("Please fill out all necessary fields before searching");
        }
    }

    public class SearchTask extends AsyncTask<String, Integer, String> {

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
                showShortToast("Succeed");
            }
        }
    }
}
