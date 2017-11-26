package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.MapFeedActivity;
import com.example.grubmate.grubmate.MapsActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.activities.ProfileActivity;
import com.example.grubmate.grubmate.adapters.BFeedAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
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
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG = "FeedFragment";
    public static final int FETCH_FEED_LIST = 2023423423;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Post> feedData;
    // used for recyclerview
    private RecyclerView mFeedView;
    private BFeedAdapter mFeedAdapter;
    // used for better user experience when loading
    private ProgressBar mFeedProgressBar;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<UserRequest> mUserRequests;
    private Double[] address;
    private Integer requesterID;
    private Integer targetPostID;
    private double Lat;
    private double Lng;
    private ArrayList<Post> mPastPostList;
    private FloatingActionButton mapListButton;
    private Boolean isRecommendation;
    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2, ArrayList<Post> pastPostList) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable("pastPost",pastPostList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mPastPostList = (ArrayList<Post>) getArguments().getSerializable("pastPost");
        }
        feedData = new ArrayList<Post>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        // Setting up feed
        mFeedView = (RecyclerView) rootView.findViewById(R.id.rv_feed);
        LinearLayoutManager layoutManager;
        isRecommendation = false;
        if(Objects.equals(mParam1, "horizontal")){
             layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else if (Objects.equals(mParam1, "recommendation")) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            isRecommendation = true;
        } else {
             layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        mFeedView.setLayoutManager(layoutManager);

        mFeedAdapter = new BFeedAdapter(feedData);
        mFeedAdapter.openLoadAnimation();
        mFeedAdapter.setEmptyView(R.layout.list_loading_layout, (ViewGroup) mFeedView.getParent());
        mFeedView.setAdapter(mFeedAdapter);
        mFeedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_feed_item_poster:
                        Intent posterIntent = new Intent(getContext(), ProfileActivity.class);
                        Object mPostData;
                        posterIntent.putExtra("user_id", String.valueOf(feedData.get(position).posterID));
                        startActivity(posterIntent);
                        break;
                    case R.id.b_feed_item_request:
                        view.setEnabled(false);
                        requestPost(position);
                        break;
                    case R.id.b_feed_item_map:
                        Intent startMap = new Intent(getContext(),MapsActivity.class);
                        startMap.putExtra("Lat",feedData.get(position).address[0]);
                        startMap.putExtra("Lng",feedData.get(position).address[1]);
                        startActivity(startMap);
                        break;
                    case R.id.b_feed_toggle_detail:
                        LinearLayout detailLayout = (LinearLayout) adapter.getViewByPosition(mFeedView, position, R.id.ll_feed_detail);
                        if(detailLayout.getVisibility()==View.VISIBLE) {
                            detailLayout.setVisibility(View.GONE);
                        } else {
                            detailLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.b_feed_item_spam:
                        new ReportTask().execute(feedData.get(position).postID);
                        break;
                    default:
                }
            }
        });

        mapListButton = (FloatingActionButton) rootView.findViewById(R.id.fab_feed_map);

        if(mParam1 == "horizontal"){
            mapListButton.setVisibility(View.INVISIBLE);
        }else {

            mapListButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(),MapFeedActivity.class);
                    intent.putExtra("postList", feedData);
                    startActivity(intent);
                }
            });
        }
        mFeedView.setAdapter(mFeedAdapter);

        mFeedProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_feed);
        //        mFeedAdapter.setFeedData(MockData.mockFeedData);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mGoogleApiClient==null)mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();

        mGoogleApiClient.connect();
        if(mParam2==null) {
            new FetchFeedListTask().execute(2);
        }else if(Objects.equals(mParam2, "search")){
            Intent i = getActivity().getIntent();

            ArrayList<Post> searchResult= (ArrayList<Post>) i.getSerializableExtra("pastPost");
            feedData = mPastPostList;
            mFeedAdapter.setNewData(feedData);
            mFeedProgressBar.setVisibility(View.INVISIBLE);
            mFeedProgressBar.getLayoutParams().height = 0;
            mFeedView.setVisibility(View.VISIBLE);
           // new FetchFeedListTask().execute(2);
        }else if(Objects.equals(mParam2, "profile")){
            Log.i("profile","");
            feedData = mPastPostList;
            mFeedAdapter.setNewData(feedData);
            mFeedProgressBar.setVisibility(View.INVISIBLE);
            mFeedProgressBar.getLayoutParams().height = 0;
            mFeedView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnNotificationFragmentInteractionListener");
            mListener = new OnFragmentInteractionListener() {
                @Override
                public void onFragmentInteraction(Uri uri) {

                }
            };
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchFeedListTask extends AsyncTask<Integer, Integer, ArrayList<Post>> {
        @Override
        protected void onPreExecute() {
            mFeedView.setVisibility(View.INVISIBLE);
            mFeedProgressBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.pb_height);
            mFeedProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Post> doInBackground(Integer... params) {

            try {
                String response = null;
                if(isRecommendation) {
                    response = NetworkUtilities.get(GrubMatePreference.getRecommendationURL(PersistantDataManager.getUserID()));
                } else {
                    response = NetworkUtilities.get(GrubMatePreference.getFeedUrl(PersistantDataManager.getUserID()));
                }

                Log.d(TAG, response);
                if (response == null || response.length() == 0)
                    return MockData.getPostList(2);
                return JsonUtilities.getFeedItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MockData.getPostList(2);
        }
        protected void onPostExecute(ArrayList<Post> feedItems) {
            if (feedItems != null) {
                feedData = feedItems;

                for(int i = 0; i< feedData.size();i++){
                    for(int j=0;j<PersistantDataManager.getBlockIDs().size();j++){
                        if(feedData.get(i).posterID.equals(PersistantDataManager.getBlockIDs().get(j))){
                            feedData.remove(i);
                        }
                    }
                }
                mFeedAdapter.setNewData(feedData);
                mFeedProgressBar.setVisibility(View.INVISIBLE);
                mFeedProgressBar.getLayoutParams().height = 0;
                mFeedView.setVisibility(View.VISIBLE);
                if(feedItems.size()==0) {
                    mFeedAdapter.setEmptyView(R.layout.list_empty_layout, mFeedView);
                }
            } else {
                mFeedProgressBar.setVisibility(View.INVISIBLE);
                mFeedProgressBar.getLayoutParams().height = 0;

            }
        }
    }

        public class RequestTask extends AsyncTask<Integer, Integer, String> {
            @Override

            protected String doInBackground(Integer ...params) {
                Integer postID = params[0];
                if (postID < 0) {
                    return null;
                }
                Log.d("Feed", "Ready to send request");

                UserRequest newRequest = new UserRequest();
                newRequest.address = new Double[2];
                newRequest.address[0] = Lat;
                newRequest.address[1] = Lng;
                newRequest.requesterID = requesterID;
                newRequest.status = "Pending";
                newRequest.targetPostID = targetPostID;
                Gson gson = new Gson();
                String requestJson = gson.toJson(newRequest);
                Log.d("Detail", requestJson);
                try {
                    return NetworkUtilities.post(GrubMatePreference.getRequestURL(PersistantDataManager.getUserID()), requestJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String response) {

            }
        }
    public class ReportTask extends AsyncTask<Integer, Integer, String> {
        @Override

        protected String doInBackground(Integer ...params) {
            Integer postID = params[0];
            if (postID < 0) {
                return null;
            }
            try {
                return NetworkUtilities.get(GrubMatePreference.getReportURL(PersistantDataManager.getUserID(), postID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

        }
    }

    private void requestPost(int pos) {
        requesterID = PersistantDataManager.getUserID();
        targetPostID = feedData.get(pos).postID;
        address = new Double[2];
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Feed", "returned from google");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);

                Lat = place.getLatLng().latitude;
                Lng = place.getLatLng().longitude;
                new RequestTask().execute(targetPostID);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i("test", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void scrollToPosition(int position){
        mFeedView.getLayoutManager().scrollToPosition(position);
    }
}
