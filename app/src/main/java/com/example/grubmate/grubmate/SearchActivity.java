package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText searchItemNameText;
    private Spinner searchItemCategorySpinner;
    private Spinner searchItemTimeSpinner;
    private Button searchButton;
    private String title;
    private String[] tags;
    private String category;
    private String timePeriod;
    private Boolean[] allergyInfo;
    private Gson gson;

    class SearchFields{
        public String title;
        public String[] tags;
        public String category;
        public String timePeriod;
        public Boolean[] allergyInfo;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchItemNameText = (EditText) findViewById(R.id.et_search_item_name);

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
        gson = new Gson();

        Fragment destinationFragment = FeedFragment.newInstance(null, null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(1, destinationFragment).commit();
//        transaction.replace(R.id.fragment_search, destinationFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validateForm() {
        if(title != null) return true;
        if(category != "Category") return true;
        if(timePeriod != "Time Period") return true;
        return false;
    };

    @Override
    public void onClick(View view) {
        title = searchItemNameText.getText().toString();
        tags = null;
        category = searchItemCategorySpinner.getSelectedItem().toString();
        timePeriod = searchItemTimeSpinner.getSelectedItem().toString();
        allergyInfo = new Boolean[3];
        if(validateForm()) {
            new SearchActivity.SearchTask().execute("post");
        } else {
            new SearchActivity.SearchTask().execute("get");
        }
    }

    public class SearchTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            if(params[0] == "get") {
                try {
                    String baseUrl = GrubMatePreference.getFeedUrl(PersistantDataManager.getUserID());
                    String response = NetworkUtilities.get(baseUrl);
                    Log.d("Search",response);
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (params[0] == "post") {
                SearchFields searchFields = new SearchFields();
                searchFields.title = title;
                searchFields.category = category;
                searchFields.tags = tags;
                searchFields.timePeriod = timePeriod;
                searchFields.allergyInfo = allergyInfo;

                String response = null;
                try {
                    response = NetworkUtilities.post(GrubMatePreference.getSearchURL(PersistantDataManager.getUserID()), gson.toJson(searchFields));
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String searchResponse) {
            if (searchResponse != null) {
                showShortToast("Succeed");
                Intent intent = new Intent();
                Log.d("SearchActivity",searchResponse);
                intent.putExtra("data_return", searchResponse);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
