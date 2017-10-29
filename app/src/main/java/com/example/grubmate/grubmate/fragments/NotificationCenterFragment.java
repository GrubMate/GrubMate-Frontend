package com.example.grubmate.grubmate.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.NotificationAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.grubmate.grubmate.R.id.poster_layout;
import static com.example.grubmate.grubmate.R.id.radio;
import static com.example.grubmate.grubmate.R.id.recyclerview;
import static com.example.grubmate.grubmate.adapters.FeedAdapter.FeedDetailActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNotificationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationCenterFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private NotificationAdapter mNotificationAdapter;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Notification> notificationData;
    private Double[] address;
    private Integer requesterID;
    private Integer targetPostID;
    private int targetPostPos;
    private BroadcastReceiver mNotificationReceiver;
    private IntentFilter intentFilter;
    private Gson gson;
    public final static String BROADCAST_ACTION = "com.example.grubmate.grubmate.notification";
    private double Lat;
    private double Lng;

    private OnNotificationFragmentInteractionListener mListener;

    public NotificationCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationCenterFragment newInstance(String param1, String param2) {
        NotificationCenterFragment fragment = new NotificationCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        gson = new Gson();
        mNotificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String payload = intent.getStringExtra("notification");
                Log.d("Notification Center", payload==null?"null":payload);
                updateNotification(payload);
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        // start the service for notification
        notificationData = new ArrayList<Notification>();
    }

    public boolean updateNotification(String payload) {
        if(payload!=null&&payload.length()>0) {
            notificationData = PersistantDataManager.getNotificationCache();
            mNotificationAdapter.setNewData(notificationData);
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification_center, container, false);
        this.mRecyclerView = rootView.findViewById(R.id.rv_notification_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mNotificationAdapter = new NotificationAdapter(notificationData);
        mNotificationAdapter.openLoadAnimation();
        mNotificationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.b_notification_accept:
                        view.setEnabled(false);
                        new AcceptRequestTask().execute(position);
                        break;
                    case R.id.b_notification_deny:
                        view.setEnabled(false);
                        new DenyRequestTask().execute(position);
                        break;
                    case R.id.b_notification_request:
                        requestPost(position);
                        break;
                    case R.id.b_notification_submit:
                        RatingBar ratingBar = (RatingBar) adapter.getViewByPosition(mRecyclerView, position,R.id.rb_notification_rating);
                        int score = ratingBar.getNumStars();
                        new SubmitRatingTask().execute(position,score);
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mNotificationAdapter);
//        mProgressBar = rootView.findViewById(R.id.pb_notification_progress);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mGoogleApiClient==null)mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(),1024, this)
                .build();
        mGoogleApiClient.connect();
        notificationData = PersistantDataManager.getNotificationCache();
        mNotificationAdapter.setNewData(notificationData);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNotificationFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mNotificationReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNotificationReceiver);
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationFragmentInteractionListener) {
            mListener = (OnNotificationFragmentInteractionListener) context;
        } else {
            mListener = new OnNotificationFragmentInteractionListener() {
                @Override
                public void onNotificationFragmentInteraction(Uri uri) {

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
    public interface OnNotificationFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNotificationFragmentInteraction(Uri uri);
    }


    public class AcceptRequestTask extends AsyncTask<Integer, Integer, String> {
        int pos;
        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Feed Detail", "Request sent");
            pos = params[0];
            try {
                int requestID = notificationData.get(pos).requestID;
                return NetworkUtilities.get(GrubMatePreference.getAcceptRequestURL(PersistantDataManager.getUserID(), requestID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Post Detail", postActionResponse);
            if (postActionResponse != null) {
                notificationData.remove(pos);
                mNotificationAdapter.setNewData(notificationData);
                PersistantDataManager.setNotificationCache(notificationData);
                Toast.makeText(getContext(), "You successfully accept a request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class DenyRequestTask extends AsyncTask<Integer, Integer, String> {
        int pos;
        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Feed Detail", "Request sent");
            Integer pos = params[0];
            try {
                return NetworkUtilities.get(GrubMatePreference.getDenyRequestURL(PersistantDataManager.getUserID(), notificationData.get(pos).requestID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Post Detail", postActionResponse);
            if (postActionResponse != null) {
                notificationData.remove(pos);
                mNotificationAdapter.setNewData(notificationData);
                PersistantDataManager.setNotificationCache(notificationData);
                Toast.makeText(getContext(), "You successfully denied a request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class RequestTask extends AsyncTask<Integer, Integer, String> {
        int pos;
        @Override

        protected String doInBackground(Integer ...params) {
            targetPostID = params[0];
            pos = params[1];
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
            if(response!=null && !response.contains("error")) {
                notificationData.remove(pos);
                mNotificationAdapter.setNewData(notificationData);
                PersistantDataManager.setNotificationCache(notificationData);
                Toast.makeText(getContext(), "You successfully request a post", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class SubmitRatingTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        int pos;
        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Rating", "Rating Sent");
            pos = params[0];
            Integer score = params[1];
            Notification notification = new Notification();
            notification.toUserID = notificationData.get(pos).toUserID;
            notification.fromUserID = notificationData.get(pos).fromUserID;
            notification.rating = score;
            notification.type = Notification.RATING;
            try {
                return NetworkUtilities.post(GrubMatePreference.getRatingUrl(notification.fromUserID, notification.toUserID, notification.rating), gson.toJson(notification));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Rating Response", postActionResponse==null?"null":postActionResponse);
            if (postActionResponse != null) {
                notificationData.remove(pos);
                mNotificationAdapter.setNewData(notificationData);
                PersistantDataManager.setNotificationCache(notificationData);
                Toast.makeText(getContext(), "You successfully submit a rating", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPost(int pos) {
        requesterID = PersistantDataManager.getUserID();
        targetPostPos = pos;
        targetPostID = notificationData.get(pos).postID;
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
                new NotificationCenterFragment.RequestTask().execute(targetPostID, targetPostPos);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i("test", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
