package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.BTransactionAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Transaction;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTransactionInteractionListener}
 * interface.
 */
public class TransactionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnTransactionInteractionListener mListener;
    private BTransactionAdapter mTransactionAdapter;
    private ArrayList<Transaction> transactionData;
    private RecyclerView mRecyclerView;
    private int userID;
    private Gson gson;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TransactionFragment newInstance(int columnCount) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        transactionData = new ArrayList<>();
        userID = PersistantDataManager.getUserID();
        gson = new Gson();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        mTransactionAdapter = new BTransactionAdapter(transactionData);
        // Set the adapter
            Context context = view.getContext();
            mRecyclerView = view.findViewById(R.id.rv_transaction_list);
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(mTransactionAdapter);
            mTransactionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.b_transaction_rating_submit:
                            view.setEnabled(false);
                            RatingBar ratingBar = (RatingBar) adapter.getViewByPosition(mRecyclerView, position,R.id.rb_transaction_rating);
                            int score = (int) ratingBar.getRating();
                            new SubmitRatingTask().execute(position, score);
                            break;
                    }
                }
            });
        new FetchTransactionListTask().execute();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTransactionInteractionListener) {
            mListener = (OnTransactionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTransactionInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTransactionInteractionListener {
        // TODO: Update argument type and name
        void onTransactionInteraction();
    }
    public class FetchTransactionListTask extends AsyncTask<Integer, Integer, ArrayList<Transaction>> {
        @Override
        protected void onPreExecute() {
            mTransactionAdapter.setEmptyView(R.layout.list_loading_layout, (ViewGroup) mRecyclerView.getParent() );
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Transaction> doInBackground(Integer... params) {
            try {
                String response = NetworkUtilities.get(GrubMatePreference.getTransactionURL(PersistantDataManager.getUserID()));
                Log.d("Transaction", response);
                if (response == null || response.length() == 0) {
                    MockData.getTransactionList();
                }
                return JsonUtilities.getTransactionList(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MockData.getTransactionList();
        }
        protected void onPostExecute(ArrayList<Transaction> transactionItems) {
            if (transactionItems != null) {
                transactionData= transactionItems;
                mTransactionAdapter.setNewData(transactionData);
            } else {
                mTransactionAdapter.setEmptyView(R.layout.list_empty_layout, mRecyclerView);
            }
        }
    }

    public class SubmitRatingTask extends AsyncTask<Integer, Integer, String> {
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            Log.d("Rating", "Rating Sent");
            pos = params[0];
            Integer score = params[1];
            Notification notification = new Notification();
            int requesterID = transactionData.get(pos).requesterID;
            int posterID = transactionData.get(pos).posterID;
            notification.toUserID = posterID==userID?requesterID:posterID;
            notification.fromUserID = posterID==userID?posterID:requesterID;
            notification.rating = score;
            notification.postID = transactionData.get(pos).postID;
            notification.requestID = transactionData.get(pos).requestID;
            notification.type = Notification.RATING;
            try {
//                return NetworkUtilities.post(GrubMatePreference.getRatingUrl(notification.fromUserID, notification.toUserID, notification.rating), gson.toJson(notification));
                return NetworkUtilities.post(GrubMatePreference.getRatingUrl( notification.postID, notification.requestID, notification.fromUserID, notification.toUserID, notification.rating), gson.toJson(notification));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Rating Response", postActionResponse==null?"null":postActionResponse);
            if (postActionResponse != null) {
                PersistantDataManager.removeRatingNotification(transactionData.get(pos).requestID, transactionData.get(pos).postID);
                Toast.makeText(context, "You successfully submit a rating", Toast.LENGTH_SHORT).show();
                new FetchTransactionListTask().execute();
            } else {
                Toast.makeText(context, "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
