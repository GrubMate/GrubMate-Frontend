package com.example.grubmate.grubmate.fragments;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.BFeedAdapter;
import com.example.grubmate.grubmate.adapters.FeedAdapter;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";
    public static final int FETCH_FEED_LIST = 2023423423;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    private TextView mEmptyText;


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
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        feedData = new ArrayList<Post>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        // Setting up feed
        mFeedView = (RecyclerView) rootView.findViewById(R.id.rv_feed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mFeedView.setLayoutManager(layoutManager);
        mFeedAdapter = new BFeedAdapter(feedData);
        mFeedView.setAdapter(mFeedAdapter);

        mFeedProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_feed);
        mEmptyText = (TextView) rootView.findViewById(R.id.tv_feed_empty_text);
        //        mFeedAdapter.setFeedData(MockData.mockFeedData);
        new FetchFeedListTask().execute();
        return rootView;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
            mEmptyText.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Post> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }


            try {
                String response = NetworkUtilities.get(GrubMatePreference.getFeedUrl(PersistantDataManager.getUserID()));
                Log.d(TAG, response);
                if (response == null || response.length() == 0)
                    return null;
                return JsonUtilities.getFeedItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> feedItems) {
            if (feedItems != null) {
                feedData = feedItems;
                mFeedAdapter.setNewData(feedData);
                mFeedProgressBar.setVisibility(View.INVISIBLE);
                mFeedProgressBar.getLayoutParams().height = 0;
                mFeedView.setVisibility(View.VISIBLE);
                if (feedData.size() < 1)
                    mEmptyText.setVisibility(View.VISIBLE);
            } else {
                mEmptyText.setVisibility(View.VISIBLE);
                mFeedProgressBar.setVisibility(View.INVISIBLE);
                mFeedProgressBar.getLayoutParams().height = 0;

            }
        }
    }
}
