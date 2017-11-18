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
import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.dataClass.Transaction;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
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
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        mTransactionAdapter = new BTransactionAdapter(transactionData);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mTransactionAdapter);
            mTransactionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.b_transaction_request_cancel:
                            view.setEnabled(false);
                            break;
                        case R.id.b_transaction_rating_submit:
                            view.setEnabled(false);
                            RatingBar ratingBar = (RatingBar) adapter.getViewByPosition(recyclerView, position,R.id.rb_transaction_rating);
                            int score = ratingBar.getNumStars();
                            new SubmitRatingTask().execute(position, score);
                            break;
                    }
                }
            });
        }
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
                Toast.makeText(context, "You successfully submit a rating", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error: Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
