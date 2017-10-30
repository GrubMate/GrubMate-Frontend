package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.GroupSettingsActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.SettingsActivity;
import com.example.grubmate.grubmate.adapters.BFeedAdapter;
import com.example.grubmate.grubmate.adapters.BGroupAdapter;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnGroupFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnGroupFragmentInteractionListener mListener;
    private ArrayList<Group> feedData;
    // used for recyclerview
    private RecyclerView mFeedView;
    private BGroupAdapter mFeedAdapter;
    private FloatingActionButton mAddGroupButton;
    private Gson gson;
    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        // Setting up feed
        mFeedView = (RecyclerView) rootView.findViewById(R.id.rv_Group_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mFeedView.setLayoutManager(layoutManager);
        mFeedAdapter = new BGroupAdapter(feedData);
        mFeedAdapter.openLoadAnimation();
        mFeedAdapter.setEmptyView(R.layout.list_empty_layout, (ViewGroup) mFeedView.getParent());
        mAddGroupButton = (FloatingActionButton)rootView.findViewById(R.id.fab_group_add);
        mFeedView.setAdapter(mFeedAdapter);

        //        mFeedAdapter.setFeedData(MockData.mockFeedData);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchGroupListTask().execute(2);
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
        if (context instanceof OnGroupFragmentInteractionListener) {
            mListener = (OnGroupFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        feedData = new ArrayList<Group>();
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
    public interface OnGroupFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchGroupListTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            mFeedView.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }


            try {
                String response = NetworkUtilities.get(GrubMatePreference.getGroupURL(PersistantDataManager.getUserID()));
                Log.d("group response", response==null?"null":response);
                if (response == null || response.length() == 0)
                    return null;
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  gson.toJson(MockData.getGroupList(2));
        }
          //  return MockData.getGroupList(2);
          protected void onPostExecute(String feedItems) {
              if (feedItems != null) {
                  feedData = JsonUtilities.getGroupList(feedItems);
                  if(feedData==null) feedData = new ArrayList<Group>();
                  mFeedAdapter.setNewData(feedData);
                  mFeedView.setVisibility(View.VISIBLE);
                  ArrayList<Integer> groupIDs = new ArrayList<Integer>();
                  for(int i=0;i<feedData.size();i++){
                      groupIDs.add(feedData.get(i).groupID);
                  }
                  PersistantDataManager.setGroupIDs(groupIDs);
                  final ArrayList<Group> list = feedData;
                  mFeedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                      @Override
                      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                          switch (view.getId()) {
                              case R.id.tv_group_item_name:

                                  break;
                              case R.id.b_group_item_edit:
                                  Intent intent = new Intent(getContext(),GroupSettingsActivity.class);
                                  intent.putExtra("groupsList", list);
                                  intent.putExtra("groupID",list.get(position).groupID);
                                  intent.putExtra("index",position);
                                  startActivity(intent);
                                  break;
                              default:
                          }
                      }
                  });

                  mAddGroupButton.setOnClickListener(new View.OnClickListener() {
                      public void onClick(View v) {

                          Intent intent = new Intent(getContext(),GroupSettingsActivity.class);
                          intent.putExtra("groupsList", list);
                          startActivity(intent);
                      }
                  });
              }
          }

    }

}
