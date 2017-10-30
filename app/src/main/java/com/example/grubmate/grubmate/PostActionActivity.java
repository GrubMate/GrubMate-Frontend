package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.EventLogTags;
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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import android.Manifest;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Button mPhotoButton;
    private GoogleApiClient mGoogleApiClient;
    private int REQUEST_CODE_CHOOSE = 9191;
    private String postItemName, postItemDescription,postItemCategory,postItemTime;
    private String[] tags;
    private boolean[] postItemAllergy;
    private String timePeriod;
    private String category;
    private Integer postItemQuantity;
    private Double[] postItemAddress;
    private Integer userID;
    private Integer[] groupIDs;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private double Lat;
    private double Lng;
    private Boolean isHomeMade;
    private Post mPostData;
    private Gson gson;
    private Integer mPostID;
    private List<Uri> mSelected;
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
        ArrayList<String> groups = new ArrayList<>();
        groups.add("All");
        for(int i = 0; i<PersistantDataManager.getGroupIDs().size(); i++) {
            groups.add(PersistantDataManager.getGroupIDs().get(i).toString());
        }
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postGroupSpinner.setAdapter(groupAdapter);

        mPhotoButton = (Button) findViewById(R.id.b_post_action_photo);
        mPhotoButton.setOnClickListener(new PhotoButtonClickListener());

        postHomeCheckBox = (CheckBox) findViewById(R.id.cb_post_home);

        // end of oncreate
        Intent callIntent = getIntent();
        gson = new Gson();
        mPostData = null;
        mPostID = null;
        if(callIntent.hasExtra("post_data")) {
            String extraText = callIntent.getStringExtra("post_data");
            mPostData = gson.fromJson(callIntent.getStringExtra("post_data"), Post.class);
            postItemNameText.setText(mPostData.title);
            postItemQuantityText.setText(String.valueOf(mPostData.leftQuantity));
            if(mPostData.isHomeMade) {
                postHomeCheckBox.setChecked(true);
            } else {
                postHomeCheckBox.setChecked(false);
            }
            postItemDescriptionText.setText(mPostData.description);
            mPostID = mPostData.postID;
        }

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
        } else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
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
        } else if (Objects.equals(postItemTimeSpinner.getSelectedItem().toString(), "Time Period")) {
            return false;
        } else if (Objects.equals(postItemCategorySpinner.getSelectedItem().toString(), "Category")) {
            return false;
        } else if (postItemDescriptionText.getText().length()==0) {
            return false;
        } else if (postItemQuantityText.getText().length()==0) {
            return false;
        }
        return true;
    };

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop GoogleApiClient
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }
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
            groupIDs[0] = Objects.equals(postGroupSpinner.getSelectedItem().toString(), "All")?null:Integer.parseInt(postGroupSpinner.getSelectedItem().toString());
            // TODO: change this to reald user id in production
            userID = 0;
            new PostActionTask().execute();
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

            Post newPost = mPostData == null?new Post():mPostData;
            newPost.tags = tags;
            newPost.category = postItemCategory;
            newPost.description = postItemDescription;
            newPost.address = postItemAddress;
            newPost.totalQuantity = postItemQuantity;
            newPost.leftQuantity = postItemQuantity;
            newPost.isActive = true;
            newPost.title = postItemName;
            ArrayList<String> encodedImages = new ArrayList<>();
            if(mSelected!=null) {
                for (int i = 0; i < mSelected.size(); i++) {
                    Bitmap bitmap = null;
                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(mSelected.get(i));
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                        String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                        encodedImages.add(encodedImage);
                    } else {

                    }
                }
                if (encodedImages.size() > 0)
                    newPost.postPhotos = encodedImages.toArray(new String[]{});
            }else{
                newPost.postPhotos = null;
            }
//            newPost.allergyInfo = null;
            newPost.posterID = PersistantDataManager.getUserID();
            // TODO: change to real groups ids
            newPost.groupIDs = groupIDs;
            newPost.isHomeMade = isHomeMade;
             newPost.timePeriod = timePeriod;
//            newPost.requestsIDs = null;
            Gson gson = new Gson();
            String postJson = gson.toJson(newPost);
            Post post = gson.fromJson(postJson, Post.class);
            if(mPostData==null) {
                try {
                    return NetworkUtilities.post(GrubMatePreference.getPostActionURl(PersistantDataManager.getUserID()),postJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    return NetworkUtilities.put(GrubMatePreference.getPostActionURl(PersistantDataManager.getUserID()), postJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    class PhotoButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else {
                // Android version is lesser than 6.0 or the permission is already granted.
                Matisse.from(PostActionActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
//                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);

            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Matisse.from(PostActionActivity.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(5)
//                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new PicassoEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
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