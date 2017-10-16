package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Friend;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;


public class GroupSettingsActivity extends AppCompatActivity {
    private ArrayList<CheckBox> checkBoxList;
    private int friendsNum;
    private LinearLayout ll;
    private LinearLayout.LayoutParams lp;
    private ArrayList<Group> groupsList;
    private FloatingActionButton fab;
    private int groupID;
    private EditText nameText;
    private boolean isAdd;
    private ArrayList<Friend> allFriendsList;
        private Group currentGroup;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.group_settings);
            Intent intent = getIntent();

        if (intent.hasExtra("groupID")) {
            groupID = intent.getIntExtra("groupID", 0);
            groupsList = (ArrayList<Group>) getIntent().getSerializableExtra("groupsList");
            isAdd = false;
        } else {
            isAdd = true;
        }

        ll = (LinearLayout) findViewById(R.id.group_linearLayout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxList = new ArrayList<CheckBox>();
        fab = (FloatingActionButton) findViewById(R.id.groupSettingButton);
        nameText = (EditText) findViewById(R.id.group_name);
        new GetFriendListTask().execute(GrubMatePreference.getFriendlistURL(9));
    }

    public class GetFriendListTask extends AsyncTask<String, Integer, ArrayList<Friend>> {

        @Override
        protected ArrayList<Friend> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response = NetworkUtilities.get(baseUrl);
                return JsonUtilities.getfriendsList(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Friend> friendsList) {
            Log.d("friendlist 2222",friendsList.toString());

            allFriendsList = friendsList;
            friendsNum = friendsList.size();
            Log.d("friendName",friendsList.get(0).name);
            Log.d("friendNum",String.valueOf(friendsList.size()));
            nameText.setText(groupsList.get(groupID).groupName);
            showShortToast("friendsNum = " + friendsNum);

            for(int i=0;i<friendsNum;i++) {
                CheckBox newCheckbox = new CheckBox(GroupSettingsActivity.this);
                newCheckbox.setText(friendsList.get(i).name);
                checkBoxList.add(newCheckbox);
                ll.addView(newCheckbox, lp);

            }
            for(int i=0;i<groupsList.get(i).memberIDs.size();i++){
                for (int j=0;j<friendsList.size();j++){
                    if(groupsList.get(i).memberIDs.get(i) == friendsList.get(j).id){
                        checkBoxList.get(j).setChecked(true);
                    }
                }
            }
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(!isAdd) {
                        currentGroup = groupsList.get(groupID);
                        currentGroup.groupID = groupID;
                    }else{
                        currentGroup = new Group() ;
                        currentGroup.groupID = null;
                    }
                    currentGroup.memberIDs = new ArrayList<Integer>();
                    currentGroup.groupName = nameText.getText().toString();

                   // currentGroup.groupOwnerID = PersistantDataManager.getUserID();
                    currentGroup.groupOwnerID = 9;
                    for(int i=0;i<friendsNum;i++) {
                        if(checkBoxList.get(i).isChecked()){
                            currentGroup.memberIDs.add(allFriendsList.get(i).id);
                        }
                    }

                    new SetGroupsTask().execute(GrubMatePreference.getGroupURL(9));

                }
            });
        }
    }

    public class SetGroupsTask extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];

            try {
                String response;
                Gson gson = new Gson();
                String groupJson = gson.toJson(currentGroup);
                if(!isAdd){
                   response = NetworkUtilities.put(baseUrl,groupJson);
                }else {
                   response = NetworkUtilities.post(baseUrl,groupJson);
                }
                    return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String reponse) {
            showShortToast("success");

        }
    }
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
