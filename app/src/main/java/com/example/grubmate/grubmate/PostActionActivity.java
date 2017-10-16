package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostActionActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private EditText postItemNameText;
    private EditText postItemTagsText;
    private EditText postItemDescriptionText;
    private EditText postItemQuantityText;
    private EditText postItemAllergyText;
    private Spinner postItemCategorySpinner;
    private Spinner postItemTimeSpinner;
    private Spinner postGroupSpinner;
    private CheckBox postHomeCheckBox;
    private Button postItemLocation;
    private TextView postItemLocationText;
    private GoogleApiClient mGoogleApiClient;
    private String postItemName, postItemDescription,postItemCategory,postItemTime;
    private String[] tags;
    private boolean[] postItemAllergy;
    private String timePeriod;
    private String category;
    private Integer postItemQuantity;
    private Double[] postItemAddress;
    private Integer userID;
    private Integer[] groupIDs;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private double Lat;
    private double Lng;
    private Boolean isHomeMade;
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
        postItemLocation = (Button) findViewById(R.id.et_location);
        postItemCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postItemCategorySpinner.setAdapter(categoryAdapter);
        postItemLocationText = (TextView) findViewById(R.id.et_location_text) ;
        //Auto_complete activity open when button clicked
        postItemLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient = new GoogleApiClient
                .Builder(PostActionActivity.this)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .enableAutoManage(PostActionActivity.this, PostActionActivity.this)
                        .build();

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(PostActionActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        postItemTimeSpinner = (Spinner) findViewById(R.id.spinner_time);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_period, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        postItemTimeSpinner.setAdapter(timeAdapter);

        postGroupSpinner = (Spinner) findViewById(R.id.spinner_group);
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this,R.array.group, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postGroupSpinner.setAdapter(groupAdapter);

        postHomeCheckBox = (CheckBox) findViewById(R.id.cb_post_home);
        // end of oncreate
    }

    //Store the location's Latitude to Lat and Logitude to Lng
    //Show location'name in the TextView of PostActionActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                Lat = place.getLatLng().latitude;
                Lng = place.getLatLng().longitude;
               postItemLocationText.setText(place.getName().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("test", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
        } else if (postItemTimeSpinner.getSelectedItem().toString() == "Time Period") {
            return false;
        } else if (postItemCategorySpinner.getSelectedItem().toString() == "Category") {
            return false;
        }
        return true;
    };

    @Override
    public void onClick(View view) {
        if(validateForm()) {

            postItemName = postItemNameText.getText().toString();
            postItemDescription = postItemDescriptionText.getText().toString();
            postItemQuantity = Integer.parseInt(postItemQuantityText.getText().toString());
            String tagString = postItemTagsText.getText().toString();
            tags = tagString.split(",");
            postItemAllergy = new boolean[3];
            postItemAddress = new Double[2];
            postItemAddress[0] = Lat;
            postItemAddress[1] = Lng;
            postItemCategory = postItemCategorySpinner.getSelectedItem().toString();
            if(postItemCategory == "Category") postItemCategory = null;
            isHomeMade = postHomeCheckBox.isChecked();
            timePeriod = postItemTimeSpinner.getSelectedItem().toString();
            if(timePeriod == "Time Period") timePeriod = null;
            groupIDs = new Integer[1];
            // TODO: change this to reald user id in production
            userID = 0;
            new PostActionTask().execute(GrubMatePreference.getPostActionURl(userID));
        } else {
            showShortToast("Please fill out all necessary fields before posting");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class PostActionTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            Post newPost = new Post();
//           newPost.postID = null;
            newPost.tags = tags;
            newPost.category = postItemCategory;
            newPost.description = postItemDescription;
            newPost.address = postItemAddress;
            newPost.totalQuantity = postItemQuantity;
            newPost.leftQuantity = postItemQuantity;
            newPost.isActive = true;
            newPost.title = postItemName;
//            newPost.allergyInfo = null;
            newPost.posterID = PersistantDataManager.getUserID();
            // TODO: change to real groups ids
            newPost.groupIDs = groupIDs;
            newPost.isHomeMade = isHomeMade;
//            newPost.postID = null;
//            newPost.postPhotos =null;
             newPost.timePeriod = null;
//            newPost.requestsIDs = null;
            Gson gson = new Gson();
            String postJson = gson.toJson(newPost);
            try {
               return NetworkUtilities.post(params[0],postJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                showShortToast("Succeed");
                finish();
            } else {
                showShortToast("Error: please retry");
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