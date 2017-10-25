package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.NotificationAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.grubmate.grubmate.R.id.pb_notification_progress;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNotificationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationCenterFragment extends Fragment {
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
    private double Lat;
    private double Lng;

    private OnNotificationFragmentInteractionListener mListener;

    public NotificationCenterFragment() {
        // Required empty public constructor
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
        notificationData = new ArrayList<Notification>();
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
                        Button denyButton = (Button) adapter.getViewByPosition(position, R.id.b_notification_deny);
                        if(denyButton!=null)denyButton.setEnabled(false);
                        new AcceptRequestTask().execute(position);
                        break;
                    case R.id.b_notification_deny:
                        view.setEnabled(false);
                        Button acceptButton = (Button) adapter.getViewByPosition(position, R.id.b_notification_accept);
                        if(acceptButton!=null)acceptButton.setEnabled(false);
                        new DenyRequestTask().execute(position);
                        break;
                    case R.id.b_notification_request:
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mNotificationAdapter);
        mProgressBar = rootView.findViewById(R.id.pb_notification_progress);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchNotificationTask().execute(1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNotificationFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationFragmentInteractionListener) {
            mListener = (OnNotificationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotificationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    class FetchNotificationTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.pb_height);
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                return NetworkUtilities.get(GrubMatePreference.getNotificationURL(PersistantDataManager.getUserID()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.contains("404") && !s.contains("505")) {
                // TODO: modify this to real parse function later
                notificationData = MockData.getNotificationList();
                mNotificationAdapter.setNewData(notificationData);
            }
            notificationData = MockData.getNotificationList();
            mNotificationAdapter.setNewData(notificationData);
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.getLayoutParams().height = 0;
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    public class AcceptRequestTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Feed Detail", "Request sent");
            Integer notificationPos = params[0];
            try {
                return NetworkUtilities.get(GrubMatePreference.getAcceptRequestURL(PersistantDataManager.getUserID(), notificationData.get(notificationPos).requestID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Post Detail", postActionResponse);
            if (postActionResponse != null) {
                Toast.makeText(getContext(), "You successfully accept a request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class DenyRequestTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Feed Detail", "Request sent");
            Integer notificationPos = params[0];
            try {
                return NetworkUtilities.get(GrubMatePreference.getDenyRequestURL(PersistantDataManager.getUserID(), notificationData.get(notificationPos).requestID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Post Detail", postActionResponse);
            if (postActionResponse != null) {
                Toast.makeText(getContext(), "You successfully denied a request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
