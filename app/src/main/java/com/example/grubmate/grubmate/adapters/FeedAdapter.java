package com.example.grubmate.grubmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.PostActionActivity;
import com.example.grubmate.grubmate.ProfileActivity;
import com.example.grubmate.grubmate.R;
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

/**
 * Created by tianhangliu on 10/2/17.
 */

// TODO 01: After the class for feedData has been solidized, should change String to that

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    public final static String TAG = "FeedAdapter";
    // Will be changed to an array of FeedListItem in future versions
    private ArrayList<Post> mFeedData;
    // Allows Activity to interact with this adapter
    private final FeedAdapterOnClickHandler mClickHandler;

    public interface FeedAdapterOnClickHandler {
        void onClick(Post feedItemData);
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mFeedNameTextView;
        public FeedViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "itemCreate");
            mFeedNameTextView = itemView.findViewById(R.id.tv_feed_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Post feedItemData = mFeedData.get(adapterPosition);
            mClickHandler.onClick(feedItemData);
        }
    }

    public FeedAdapter(FeedAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, parent.toString());
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.feed_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Post feedItemData = mFeedData.get(position);
//        Log.d(TAG, "onBind"+Arrays.toString(mFeedData));
        holder.mFeedNameTextView.setText(feedItemData.title);
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "itemCount:" + Arrays.toString(mFeedData));
        if (null == mFeedData) return 0;
        return mFeedData.size();
    }

    // Allows data to be refreshed without creating new adapter
    public void setFeedData(ArrayList<Post> feedData) {
        mFeedData = feedData;
//        Log.d(TAG, Arrays.toString(feedData));
        notifyDataSetChanged();
    }

    public static class FeedDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
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
            mRequestAdapter = new RequestAdapter(mUserRequests);
            mRequestAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRequestAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){

                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch(view.getId()) {
                        case R.id.b_accept:
                            new AcceptRequestTask().execute(position);
                            break;
                        case R.id.b_deny:
                            new DenyRequestTask().execute(position);
                            break;
                        default:
                            Log.d("Feed Detail", "Network Error");
                    }

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
                    new GetRequestTask().execute();
                    Log.d("Post Detail", "Get Request Sent");
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
                Log.d("Feed Detail", "Request sent");

                try {
                    return NetworkUtilities.get(GrubMatePreference.getRequestListUrl(PersistantDataManager.getUserID(), mPostData.postID));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String postActionResponse) {
                Log.d("Post Detail", postActionResponse);
                if (postActionResponse != null) {
                    mUserRequests = JsonUtilities.getRequestItems(postActionResponse);
                    mRequestAdapter.setNewData(mUserRequests);
                } else {
                    Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
                }
            }
        }

        public class AcceptRequestTask extends AsyncTask<Integer, Integer, String> {

            @Override
            protected String doInBackground(Integer... params) {
                Log.d("Feed Detail", "Request sent");

                try {
                    return NetworkUtilities.get(GrubMatePreference.getAcceptRequestURL(PersistantDataManager.getUserID(), mUserRequests.get(params[0]).requestID));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String postActionResponse) {
                Log.d("Post Detail", postActionResponse);
                if (postActionResponse != null) {
                    new GetRequestTask().execute();
                } else {
                    Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
                }
            }
        }
        public class DenyRequestTask extends AsyncTask<Integer, Integer, String> {

            @Override
            protected String doInBackground(Integer... params) {
                Log.d("Feed Detail", "Request sent");

                try {
                    return NetworkUtilities.get(GrubMatePreference.getDenyRequestURL(PersistantDataManager.getUserID(), mUserRequests.get(params[0]).requestID));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String postActionResponse) {
                Log.d("Post Detail", postActionResponse);
                if (postActionResponse != null) {
                    new GetRequestTask().execute();
                } else {
                    Toast.makeText(FeedDetailActivity.this, "Error: Network Error", Toast.LENGTH_SHORT);
                }
            }
        }

    }
}
