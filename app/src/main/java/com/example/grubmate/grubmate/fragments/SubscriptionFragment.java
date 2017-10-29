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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.PostActionActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.SubscribeActionActivity;
import com.example.grubmate.grubmate.adapters.BFeedAdapter;
import com.example.grubmate.grubmate.adapters.BSubscriptionAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscriptionFragment.OnSubcriptionFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSubcriptionFragmentInteractionListener mListener;
    private ArrayList<Subscription> feedData;
    // used for recyclerview
    private RecyclerView mFeedView;
    private BSubscriptionAdapter mFeedAdapter;
    // used for better user experience when loading
    private ProgressBar mFeedProgressBar;
    private TextView mEmptyText;
    private Button mSubscribeButton;

    public SubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
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
        feedData = new ArrayList<Subscription>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_subscription, container, false);
        // Setting up feed
        mFeedView = (RecyclerView) rootView.findViewById(R.id.rv_subscription_subscriptionFeed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mFeedView.setLayoutManager(layoutManager);
        mFeedAdapter = new BSubscriptionAdapter(feedData);
        mFeedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.b_subscription_item_unsubscribe:
                        view.setEnabled(false);
                        new SubscriptionDeleteTask().execute(position);
                        break;
                }
            }
        });
        mFeedView.setAdapter(mFeedAdapter);
        mSubscribeButton = rootView.findViewById(R.id.b_subscription_subscribe);
        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = SubscribeActionActivity.class;

                // construct the intent
                Intent startDetailActivityIntent = new Intent(getContext(), destinationActivity);

                // put extra data into this intent
                startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

                // start the intent
                startActivity(startDetailActivityIntent);
            }
        });
        mFeedProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_subscription_progress);
        mEmptyText = (TextView) rootView.findViewById(R.id.tv_subscription_description);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
      // new FeedFragment.FetchFeedListTask().execute(2);
        new FetchSubscriptionFeedListTask().execute(2);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubcriptionFragmentInteractionListener) {
            mListener = (OnSubcriptionFragmentInteractionListener) context;
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
    public interface OnSubcriptionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchSubscriptionFeedListTask extends AsyncTask<Integer, Integer, ArrayList<Subscription>> {
        private static final String TAG = "FetchSubscription";

        @Override
        protected void onPreExecute() {
            mFeedView.setVisibility(View.INVISIBLE);
            mFeedProgressBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.pb_height);
            mFeedProgressBar.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Subscription> doInBackground(Integer... params) {
          try {
                String response = NetworkUtilities.get(GrubMatePreference.getSubscriptionURL(PersistantDataManager.getUserID()));
                Log.d(TAG, response==null?"null":response);
                if (response == null || response.length() == 0)
                    return MockData.getSubscriptionList(2);;
                return JsonUtilities.getSubscriptionItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MockData.getSubscriptionList(2);
        }

        @Override
        protected void onPostExecute(ArrayList<Subscription> feedItems) {
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
    public class SubscriptionDeleteTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int pos = params[0];
            try {
                Log.d("Subscription", "Sent");
                return NetworkUtilities.delete(GrubMatePreference.getSubscriptionDeleteURL(PersistantDataManager.getUserID(), feedData.get(pos).subscriptionID), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if (postActionResponse != null) {
                Toast.makeText(getContext(), "Unsubscribed Successfully", Toast.LENGTH_SHORT).show();
                new FetchSubscriptionFeedListTask().execute();
            } else {
                Toast.makeText(getContext(), "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
