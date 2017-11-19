package com.example.grubmate.grubmate;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,FeedFragment.OnFragmentInteractionListener, DoubleDateAndTimePickerDialog.Listener{
    private EditText searchItemNameText;
    private Spinner searchItemCategorySpinner;
    private Button searchButton;
    private String title;
    private String[] tags;
    private String category;
    private String timePeriod;
    private Boolean[] allergyInfo;
    private LinearLayout advanceLayout;
    private TextView mAdvanceTextView;
    private Button timeButton;
    private CheckBox allergyCheckBox;
    private DoubleDateAndTimePickerDialog.Builder doubleDateAndTimePickerDialogBuilder;
    private String startTime;
    private String endTime;
    private Boolean isChecked;
    private Gson gson;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDateSelected(List<Date> dates) {
        Date start = dates.get(0);
        Date end = dates.get(1);
        startTime = start.toString();
        endTime = end.toString();
    }

    class SearchFields{
        public String title;
        public String[] tags;
        public String category;
        public String[] timePeriod;
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
        advanceLayout = (LinearLayout) findViewById(R.id.ll_search_advance);
        mAdvanceTextView = (TextView) findViewById(R.id.tv_search_advance_label);
        mAdvanceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(advanceLayout.getVisibility() == View.GONE) {
                    advanceLayout.setVisibility(View.VISIBLE);
                } else {
                    advanceLayout.setVisibility(View.GONE);
                }
            }
        });

        searchButton = (Button) findViewById(R.id.b_search_button);
        searchButton.setOnClickListener(this);
        gson = new Gson();
        startTime = null;
        endTime = null;
        timeButton = (Button) findViewById(R.id.b_search_time);
        doubleDateAndTimePickerDialogBuilder = new DoubleDateAndTimePickerDialog
                .Builder(this)
                .backgroundColor(Color.WHITE)
                .mainColor(Color.argb(255, 255,152,0))
                .title("Time Period")
                .minutesStep(30)
                .tab0Text("Start")
                .tab1Text("End")
                .listener(this);
        timeButton = (Button) findViewById(R.id.b_search_time);
        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                doubleDateAndTimePickerDialogBuilder.display();
            }
        });
        allergyCheckBox = (CheckBox) findViewById(R.id.cb_search_allergy);
        isChecked = false;
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validateForm() {
        if(title != null) return true;
        if(!Objects.equals(category, "Category")) return true;
        if (startTime!=null) return true;
        return false;
    };

    @Override
    public void onClick(View view) {
        title = searchItemNameText.getText().toString();
        tags = null;
        category = searchItemCategorySpinner.getSelectedItem().toString();
        allergyInfo = allergyCheckBox.isChecked()?PersistantDataManager.getAllergyInfo():null;
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
            if(Objects.equals(params[0], "get")) {
                try {
                    String baseUrl = GrubMatePreference.getFeedUrl(PersistantDataManager.getUserID());
                    String response = NetworkUtilities.get(baseUrl);
                    Log.d("Search",response);
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(params[0], "post")) {
                SearchFields searchFields = new SearchFields();
                searchFields.title = title;
                searchFields.category = Objects.equals(category, "Category")?null:category;
                searchFields.tags = tags;
                searchFields.timePeriod = startTime!=null?new String[]{startTime,endTime}:null;
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
            if (searchResponse != null && searchResponse.contains("itemList")) {
                showShortToast("Succeed");
                ArrayList<Post> searchPostList = JsonUtilities.getFeedItems(searchResponse);
                Fragment destinationFragment = FeedFragment.newInstance(null, "search",searchPostList);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_search, destinationFragment);

                transaction.commit();
            } else {
                Toast.makeText(SearchActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                ArrayList<Post> searchPostList = MockData.getSearchList(2);
                Fragment destinationFragment = FeedFragment.newInstance(null, "search",searchPostList);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_search, destinationFragment);

                transaction.commit();
            }
        }
    }
}
