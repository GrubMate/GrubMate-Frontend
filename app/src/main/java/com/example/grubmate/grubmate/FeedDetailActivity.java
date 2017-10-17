package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.UserRequest;
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
import java.util.ArrayList;

import okhttp3.Request;

public class FeedDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private TextView mNameText;
    private TextView mQuantityText;
    private LinearLayout posterLayout;
    private Button mEditButton;
    private Button mDeleteButton;
    private LinearLayout requesterLayout;
    private Button mPosterButton;
    private Button mRequestButton;
    private Integer requesterID;
    private Integer targetPostID;
    private String status;
    private RecyclerView mRequestView;
    private RequestAdapter mRequestAdapter;
    private Double[] address;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<UserRequest> mUserRequests;
    private double Lat;
    private double Lng;

    private Post mPostData;
    private Gson gson;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        mNameText = (TextView) findViewById(R.id.tv_feed_detail_name);
        mQuantityText = (TextView) findViewById(R.id.tv_feed_quantitiy);
        mPosterButton = (Button) findViewById(R.id.b_poster);
        mPosterButton.setOnClickListener(new PosterButtonListener());
        mDeleteButton = (Button) findViewById(R.id.b_delete);
        mDeleteButton.setOnClickListener(new DeleteButtonListener());
        mEditButton = (Button) findViewById(R.id.b_edit);
        mEditButton.setOnClickListener(new EditButtonListener());
        mRequestButton = (Button) findViewById(R.id.b_request);
        mRequestButton.setOnClickListener(new RequestButtonListener());
        requesterLayout = (LinearLayout) findViewById(R.id.requester_layout);
        posterLayout = (LinearLayout) findViewById(R.id.poster_layout);

        mRequestView = (RecyclerView) findViewById(R.id.rv_post_requests);
        mUserRequests = new ArrayList<UserRequest>();
        mRequestAdapter = new RequestAdapter(R.id.request_list_item, mUserRequests);
        mRequestAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRequestAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("Request", "Button clicked");
                // TODO: send accept

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRequestView.setLayoutManager(layoutManager);
        mRequestView.setAdapter(mRequestAdapter);

        Intent callIntent = getIntent();
        gson = new Gson();
        // If intent has extra text message extrave it and display it.
        if(callIntent.hasExtra("post_data")) {
            String extraText = callIntent.getStringExtra("post_data");
            mPostData = gson.fromJson(callIntent.getStringExtra("post_data"), Post.class);
            mNameText.setText(mPostData.title);
            mQuantityText.setText(String.valueOf(mPostData.leftQuantity));
            if (mPostData.posterID == PersistantDataManager.getUserID()) {
               posterLayout.setVisibility(View.VISIBLE);
                requesterLayout.setVisibility(View.INVISIBLE);
                if(mPostData.leftQuantity != mPostData.totalQuantity) {
                    mEditButton.setEnabled(false);
                    mDeleteButton.setEnabled(false);
                }
            } else {
                posterLayout.setVisibility(View.INVISIBLE);
                requesterLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mNameText.setText("Error: no data received");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error: Connection failed", Toast.LENGTH_SHORT).show();
        finish();
    }


    class PosterButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent posterIntent = new Intent(FeedDetailActivity.this, ProfileActivity.class);
            posterIntent.putExtra("user_id", String.valueOf(mPostData.posterID));
            startActivity(posterIntent);

        }

    }

    class EditButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(FeedDetailActivity.this, PostActionActivity.class);
            intent.putExtra("post_data", gson.toJson(mPostData));
            startActivity(intent);
            
        }
    }

    class RequestButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            requesterID = PersistantDataManager.getUserID();
            targetPostID = mPostData.postID;
            status = "PENDING";
            address = new Double[2];
            mGoogleApiClient = new GoogleApiClient
                    .Builder(FeedDetailActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(FeedDetailActivity.this, FeedDetailActivity.this)
                    .build();

            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(FeedDetailActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        }
    }

    class DeleteButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            new DeleteTask().execute(GrubMatePreference.getPostDeleteURL(PersistantDataManager.getUserID(), mPostData.postID));

        }
    }

    // get user address
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                Lat = place.getLatLng().latitude;
                Lng = place.getLatLng().longitude;
                new RequestTask().execute(GrubMatePreference.getRequestURL(PersistantDataManager.getUserID()));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("test", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public class RequestTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            UserRequest newRequest = new UserRequest();
            newRequest.address = new Double[2];
            newRequest.address[0] = Lat;
            newRequest.address[1] = Lng;
            newRequest.requesterID = requesterID;
            newRequest.status = status;
            newRequest.targetPostID = targetPostID;
            Gson gson = new Gson();
            String postJson = gson.toJson(newRequest);
            Log.d("Detail", postJson);
            try {
                return NetworkUtilities.post(GrubMatePreference.getRequestURL(PersistantDataManager.getUserID()),postJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                finish();
            } else {
                Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
            }
        }
    }

    public class DeleteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            try {
                return NetworkUtilities.delete(GrubMatePreference.getPostDeleteURL(PersistantDataManager.getUserID(), mPostData.postID), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                finish();
            } else {
                Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
            }
        }
    }

    public class GetRequestTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            try {
                return NetworkUtilities.get(GrubMatePreference.getRequestListUrl(PersistantDataManager.getUserID(), mPostData.postID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                mRequestAdapter.setNewData(JsonUtilities.getRequestItems(postActionResponse));
            } else {
                Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
            }
        }
    }
}
