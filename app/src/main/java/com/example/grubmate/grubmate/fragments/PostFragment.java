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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.PostActionActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.adapters.BPostAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFragment.OnPostFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "PostFragment";
    public Button postButton;
    public ArrayList<Post> mPostData;
    private RecyclerView mRecyclerView;
    private BPostAdapter mPostAdapter;
    private Gson gson;
    private Context context;

    private OnPostFragmentInteractionListener mListener;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        postButton = rootView.findViewById(R.id.b_post_post);
        postButton.setOnClickListener(postListener);
        mRecyclerView = rootView.findViewById(R.id.rv_post_list);
        mPostAdapter = new BPostAdapter(mPostData);
        mPostAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.b_post_edit:
                        Class destinationActivity = PostActionActivity.class;

                        // construct the intentø
                        Intent startDetailActivityIntent = new Intent(getContext(), destinationActivity);

                        // put extra data into this intent
                        startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());
                        startDetailActivityIntent.putExtra("post_data", gson.toJson(mPostData.get(position)));
                        // start the intent
                        startActivity(startDetailActivityIntent);
                        break;
                    case R.id.b_post_delete:
                        view.setEnabled(false);
                        new DeletePostTask().execute(mPostData.get(position).postID);
                        break;
                    case R.id.b_post_confirm:
                        view.setEnabled(false);
                        new ConfirmPostTask().execute(mPostData.get(position).postID);
                        break;
                }
            }
        });
        initRecyclerView(mPostAdapter, mRecyclerView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        new FetchPostListTask().execute(1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPostFragmentInteraction(uri);
        }
    }
    private void initRecyclerView(BaseQuickAdapter adapter, RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);
        adapter.openLoadAnimation();
        adapter.setEmptyView(R.layout.list_loading_layout, (ViewGroup) view.getParent());
        view.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostFragmentInteractionListener) {
            mListener = (OnPostFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnNotificationFragmentInteractionListener");
            mListener = new OnPostFragmentInteractionListener() {
                @Override
                public void onPostFragmentInteraction(Uri uri) {

                }
            };
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void showShortToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
    public interface OnPostFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPostFragmentInteraction(Uri uri);
    }

    private View.OnClickListener postListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Class destinationActivity = PostActionActivity.class;

            // construct the intentø
            Intent startDetailActivityIntent = new Intent(getContext(), destinationActivity);

            // put extra data into this intent
            startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, PersistantDataManager.getUserID());

            // start the intent
            startActivity(startDetailActivityIntent);

        }
    };

    public class FetchPostListTask extends AsyncTask<Integer, Integer, ArrayList<Post>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPostAdapter.setEmptyView(R.layout.list_loading_layout, (ViewGroup) mRecyclerView.getParent());
        }

        @Override
        protected ArrayList<Post> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }


            try {
                String response = NetworkUtilities.get(GrubMatePreference.getUserPostUrl(PersistantDataManager.getUserID()));
                Log.d(TAG, response);
                if (response == null || response.length() == 0)
                    return null;
                return JsonUtilities.getFeedItems(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MockData.getPostList(2);
        }
        protected void onPostExecute(ArrayList<Post> feedItems) {
            if (feedItems != null) {
                mPostData = feedItems;
                mPostAdapter.setNewData(mPostData);
                if(mPostData.size() == 0) {
                    mPostAdapter.setEmptyView(R.layout.list_empty_layout, (ViewGroup) mRecyclerView.getParent());
                }
            } else {
                    mPostAdapter.setEmptyView(R.layout.list_error_layout, (ViewGroup) mRecyclerView.getParent());

            }
        }
    }
    public class DeletePostTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            Integer postID = integers[0];
            try {
                return   NetworkUtilities.delete(GrubMatePreference.getPostDeleteURL(PersistantDataManager.getUserID(), postID), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                showShortToast("Your post was successfully deleted");
                new FetchPostListTask().execute();
            } else {
                showShortToast("Netowrk Error");
            }
            super.onPostExecute(s);
        }
    }
    public class ConfirmPostTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            Integer postID = integers[0];
            try {
                return   NetworkUtilities.post(GrubMatePreference.getConfimUrl(PersistantDataManager.getUserID(), postID), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                showShortToast("Your post was confirmed");
                new FetchPostListTask().execute();
            } else {
                showShortToast("Netowrk Error");
            }
            super.onPostExecute(s);
        }
    }

}
