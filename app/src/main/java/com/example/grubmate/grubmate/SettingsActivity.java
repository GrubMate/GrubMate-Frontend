package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox allergy1, allergy2, allergy3;
    private ArrayList<Boolean> allergies;
    private int groupsNum;
    private LinearLayout ll;
    private LinearLayout.LayoutParams lp;
    static final int RETURN_REQUEST = 1;
    public static int id = 0;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addGroupsButton);
        FloatingActionButton allergyFab = (FloatingActionButton) findViewById(R.id.addAllergyButton);
        allergy1 = (CheckBox) findViewById(R.id.allergy1);
        allergy2 = (CheckBox) findViewById(R.id.allergy2);
        allergy3 = (CheckBox) findViewById(R.id.allergy3);

        allergies = new ArrayList<Boolean>();


        ll = (LinearLayout)findViewById(R.id.settingsLayout);
         lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button myButton = new Button(SettingsActivity.this);
                myButton.setText("added button");
                myButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingsActivity.this,GroupSettingsActivity.class);
                        startActivity(intent);
                    }
                });
                ll.addView(myButton, lp);
                Intent intent = new Intent(SettingsActivity.this,GroupSettingsActivity.class);
                startActivity(intent);
            }
        });
        allergyFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                allergies.add(0,allergy1.isChecked());
                allergies.add(1,allergy2.isChecked());
                allergies.add(2,allergy3.isChecked());
              new SetUserTask().execute(GrubMatePreference.getUserForAllergyUrl(20));
            }
        });

        new SettingsTask().execute(GrubMatePreference.getGroupURL(20));



    }
    public class SettingsTask extends AsyncTask<String, Integer, ArrayList<Group>> {

        @Override
        protected ArrayList<Group> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response = NetworkUtilities.get(baseUrl);
                Log.i("baseUrl",baseUrl);
                return JsonUtilities.getGroupList(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Group> groupsList) {
            if(groupsList == null){
                groupsNum = 0;
            }else {
                groupsNum = groupsList.size();
            }
            Log.i("groupsNum",String.valueOf(groupsNum));
            final ArrayList<Group> list = groupsList;
            for(int i=0;i<groupsNum;i++){
                final int groupID = groupsList.get(i).groupID;
                final int index = i;
                Button myButton = new Button(SettingsActivity.this);
                myButton.setText("ID:"+String.valueOf(groupID)+"name:"+list.get(i).groupName);
                myButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingsActivity.this,GroupSettingsActivity.class);
                        intent.putExtra("groupsList", list);
                        intent.putExtra("groupID",groupID);
                        intent.putExtra("index",index);
                        startActivity(intent);
                    }
                });
                ll.addView(myButton, lp);
            }
            new getUserTask().execute(GrubMatePreference.getUserForAllergyUrl(20));
            showShortToast("groupsNum = " + groupsNum);
        }
    }



    public class getUserTask extends AsyncTask<String, Integer, User> {

        @Override
        protected User doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response = NetworkUtilities.get(baseUrl);
                Log.i("user url",baseUrl);
                Gson gson = new Gson();
                User user = gson.fromJson(response,User.class);
                Log.i("user infor",user.userID.toString());
                return user;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(User user) {
            currentUser = user;
            if(user.allergy == null){
                allergy1.setChecked(false);
                allergy2.setChecked(false);
                allergy3.setChecked(false);
            }else{
                allergy1.setChecked(user.allergy.get(0));
                allergy2.setChecked(user.allergy.get(1));
                allergy3.setChecked(user.allergy.get(2));
            }


        }
    }

    public class SetUserTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {

                currentUser.allergy = allergies;
                Gson gson = new Gson();
                String userJson = gson.toJson(currentUser);
                String response = NetworkUtilities.put(baseUrl,userJson);
                Log.i("baseUrl",baseUrl);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String response) {
            showShortToast("success"+response);
        }
    }
    @Override
    public void onRestart()
    {  // After a pause OR at startup
        super.onRestart();

        //Refresh your stuff here
        ll.removeAllViews();
        new SettingsTask().execute(GrubMatePreference.getGroupURL(20));
    }
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
