package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.MapsActivity;
import com.example.grubmate.grubmate.PostActionActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.activities.ProfileActivity;
import com.example.grubmate.grubmate.adapters.BFeedAdapter;
import com.example.grubmate.grubmate.adapters.BOrderAdapter;
import com.example.grubmate.grubmate.adapters.PastPostAdapter;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.android.gms.appindexing.Action;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnProfileFragmentInteractionListener
 * FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements FeedFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    private OnProfileFragmentInteractionListener mListener;

    private ImageView mProfileAvatar;
    private TextView mProfileName;
    private RatingBar mProfileRatingBar;
    private Button mProfileMessengerButton;

    private Button mProfileVenmoButton;



    private Gson gson;
    private int userID;
    private String facebookID;
    private RecyclerView mRecyclerView;
    private PastPostAdapter mPastPostAdapter;
    private ArrayList<Post> mPastPostList;
    private LinearLayout mContentLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mOrderView;
    private BOrderAdapter mBOrderAdapter;
    private Context context;
    private final static boolean TEST = true;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(USER_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt(USER_ID, PersistantDataManager.getUserID());
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        mProfileAvatar = (ImageView) rootView.findViewById(R.id.iv_profile_avatar);
        mProfileName = (TextView) rootView.findViewById(R.id.tv_profile_name);
        mProfileRatingBar = (RatingBar) rootView.findViewById(R.id.rb_profile_rating);

        mProfileMessengerButton = (Button) rootView.findViewById(R.id.b_profile_messenger);
        mProfileMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://user/" + facebookID)));
                }
                catch(android.content.ActivityNotFoundException anfe)
                {
                    return;
                }
            }
        });

        mProfileVenmoButton = (Button) rootView.findViewById(R.id.b_profile_venmo);
        mProfileVenmoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("venmo://stories")));
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("venmo://paycharge?txn=pay&recipients=Alex-Beals&amount=10¬e=Note")));
                }
                catch(android.content.ActivityNotFoundException anfe)
                {
                    return;
                }
            }
        });

      //  mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_profile_orders);
        mOrderView = (RecyclerView) rootView.findViewById(R.id.rv_profile_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mOrderView.setLayoutManager(layoutManager);

        mBOrderAdapter= new BOrderAdapter(mPastPostList);
        mBOrderAdapter.openLoadAnimation();
        mBOrderAdapter.setEmptyView(R.layout.list_loading_layout, (ViewGroup) mOrderView.getParent());
        mOrderView.setAdapter(mBOrderAdapter);
        mBOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.b_feed_item_map:
                        Intent startMap = new Intent(getContext(),MapsActivity.class);
                        startMap.putExtra("Lat",mPastPostList.get(position).address[0]);
                        startMap.putExtra("Lng",mPastPostList.get(position).address[1]);
                        startActivity(startMap);
                        break;
                    case R.id.b_order_detail:
                        LinearLayout detailLayout = (LinearLayout) adapter.getViewByPosition(mOrderView, position, R.id.ll_order_detail);
                        if(detailLayout.getVisibility()==View.VISIBLE) {
                            detailLayout.setVisibility(View.GONE);
                        } else {
                            detailLayout.setVisibility(View.VISIBLE);;
                        }
                    default:
                }
            }
        });
        mContentLayout = rootView.findViewById(R.id.ll_profile_content);
        mProgressBar = rootView.findViewById(R.id.pb_profile_progress);
        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        new ProfileTask().execute();
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
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnNotificationFragmentInteractionListener");
            mListener = new OnProfileFragmentInteractionListener() {
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
    public void onFragmentInteraction(Uri uri) {

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
    public interface OnProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ProfileTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            mContentLayout.setVisibility(View.INVISIBLE);
            mProgressBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.pb_height);
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return NetworkUtilities.get(GrubMatePreference.getUserUrl(userID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return gson.toJson(MockData.getUser(PersistantDataManager.getUserID()));
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
                Log.d("profile", postActionResponse==null?"null":postActionResponse);
                if (postActionResponse == null || postActionResponse.length() == 0) {
                    Toast.makeText(context, "Error occurs during fetching user data", Toast.LENGTH_SHORT).show();
                } else {
                    User user = gson.fromJson(postActionResponse, User.class);

                    facebookID = user.facebookID;
                    System.out.println("Facebook ID is + " + facebookID);

                    mProfileName.setText(user.userName);
                    if (user.rating != null && user.rating >= 0) {
                        mProfileRatingBar.setRating(user.rating.intValue());
                    } else {
                        mProfileRatingBar.setRating(5);
                    }
                    Picasso.with(context).load(user.profilePhoto).into(mProfileAvatar);
                }
            mProgressBar.getLayoutParams().height = 0;
            mProgressBar.setVisibility(View.INVISIBLE);
            mContentLayout.setVisibility(View.VISIBLE);
            new PastPostTask().execute();
        }
    }

    public class PastPostTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            try {
                return NetworkUtilities.get(GrubMatePreference.getPastPostURL(userID));
            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            if(postActionResponse!=null && postActionResponse.contains("title")) {
                mPastPostList = JsonUtilities.getFeedItems(postActionResponse);
                mBOrderAdapter.setNewData(mPastPostList);
            } else {
                Toast.makeText(context, "Network Error: Please Retry", Toast.LENGTH_SHORT);
                mPastPostList = MockData.getPastPostList(2);
                mBOrderAdapter.setNewData(mPastPostList);
            }
        }
    }
}
