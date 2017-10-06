package com.example.grubmate.grubmate;

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

public class PostActionActivity extends AppCompatActivity {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

}

/*
* Post send to server should have
* posterID posterPhoto tags timePeriod description postAddress
* totalQuality
* isPostActive
*
* */