package com.example.grubmate.grubmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.grubmate.grubmate.GroupSettingsActivity;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllergySetting.OnAllergyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllergySetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllergySetting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private OnAllergyFragmentInteractionListener mListener;
    private FloatingActionButton mEditAllergy;
    private Boolean[] allergies;
    private CheckBox allergy0Milk,allergy1Egg,allergy2Fish;

    private AdView mAdView;
    public AllergySetting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllergySetting.
     */
    // TODO: Rename and change types and number of parameters
    public static AllergySetting newInstance(String param1, String param2) {
        AllergySetting fragment = new AllergySetting();
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
        allergies = new Boolean[3];

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_allergy_setting, container, false);
        allergy0Milk = (CheckBox) rootView.findViewById(R.id.cb_Allergy0_milk);
        allergy1Egg = (CheckBox) rootView.findViewById(R.id.cb_Allergy1_egg);
        allergy2Fish = (CheckBox) rootView.findViewById((R.id.cb_Allergy2_fish));
        //allergy3Peanut = (CheckBox) rootView.findViewById(R.id.cb_Allergy3_peanut);
        mEditAllergy = (FloatingActionButton) rootView.findViewById(R.id.fab_allergy_edit);
        mEditAllergy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            allergies[0] = allergy0Milk.isChecked();
            allergies[1] = allergy1Egg.isChecked();
            allergies[2] = allergy2Fish.isChecked();
        //    allergies[3] = allergy3Peanut.isChecked();
            new SetAllergyListTask().execute();
            }
        });

        mAdView = (AdView) rootView.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .build();

        mAdView.loadAd(adRequest);

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
        context = getContext();
        new FetchAllergyListTask().execute();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAllergyFragmentInteractionListener) {
            mListener = (OnAllergyFragmentInteractionListener) context;
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
    public interface OnAllergyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchAllergyListTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                String response = NetworkUtilities.get(GrubMatePreference.getUserUrl(PersistantDataManager.getUserID()));
                Log.d("allergy response", response==null?"null":response);
                if (response == null || response.length() == 0)
                    return null;
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
        //  return MockData.getGroupList(2);
        protected void onPostExecute(String allergyItems) {
            Gson gson = new Gson();
            if (allergyItems != null) {
                allergies = gson.fromJson(allergyItems, User.class).allergy;
                PersistantDataManager.setAllergyInfo(allergies);
                allergy0Milk.setChecked(allergies[0]);
                allergy1Egg.setChecked(allergies[1]);
                allergy2Fish.setChecked(allergies[2]);
            }else {
                for(int i=0;i<3;i++){
                    allergies[i] = false;
                }
                Toast.makeText(context, "Network Error: Please Retry", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class SetAllergyListTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                Gson gson = new Gson();
                User user = new User();
                user.userID = PersistantDataManager.getUserID();
                user.allergy = allergies;
                PersistantDataManager.setAllergyInfo(allergies);
                String response = NetworkUtilities.put(GrubMatePreference.getAllergyURL(PersistantDataManager.getUserID()), gson.toJson(user));
                if (response == null || response.length() == 0)
                    return null;
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
        //  return MockData.getGroupList(2);
        protected void onPostExecute(String allergyItems) {
            if (allergyItems != null) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(context, "Network Error: Please Retry", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
