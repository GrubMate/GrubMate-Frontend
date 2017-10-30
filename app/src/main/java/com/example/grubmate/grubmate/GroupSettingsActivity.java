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
import com.example.grubmate.grubmate.dataClass.MockData;
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
    private int localID;
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

            groupID = intent.getIntExtra("groupID", groupID);
            groupsList = (ArrayList<Group>) getIntent().getSerializableExtra("groupsList");
            localID = intent.getIntExtra("index",localID);
            isAdd = false;
        } else {
            groupsList = (ArrayList<Group>) getIntent().getSerializableExtra("groupsList");
            isAdd = true;
        }

        ll = (LinearLayout) findViewById(R.id.group_linearLayout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxList = new ArrayList<CheckBox>();
        fab = (FloatingActionButton) findViewById(R.id.groupSettingButton);
        nameText = (EditText) findViewById(R.id.group_name);
        Log.d("groupList",String.valueOf(groupsList.size()));
        new GetFriendListTask().execute("");
    }

    public class GetFriendListTask extends AsyncTask<String, Integer, ArrayList<Friend>> {

        @Override
        protected ArrayList<Friend> doInBackground(String... params) {
            try {
                String response = NetworkUtilities.get(GrubMatePreference.getFriendlistURL(PersistantDataManager.getUserID()));
                Log.d("friendURL",response==null?"null":response);
                return JsonUtilities.getfriendsList(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MockData.geFriendList(10);
        }


        @Override
        protected void onPostExecute(ArrayList<Friend> friendsList) {
            Log.d("friendlist 2222",friendsList==null?"null":friendsList.toString());

            allFriendsList = friendsList;
            friendsNum = friendsList.size();
            Log.d("friendName",friendsList.get(0).name);
            Log.d("friendNum",String.valueOf(friendsList.size()));
            showShortToast("friendsNum = " + friendsNum);

            for(int i=0;i<friendsNum;i++) {
                CheckBox newCheckbox = new CheckBox(GroupSettingsActivity.this);
                newCheckbox.setText(friendsList.get(i).name);
                checkBoxList.add(newCheckbox);
                ll.addView(newCheckbox, lp);

            }

            if(!isAdd) {
                nameText.setText(groupsList.get(localID).groupName);
                if(groupsList.get(localID).memberIDs.size() != 0) {
                    Log.i("memberid", groupsList.get(localID).memberIDs.get(0).toString());
                    Log.i("Friendid", friendsList.get(0).id.toString());
                    for (int i = 0; i < groupsList.get(localID).memberIDs.size(); i++) {
                        for (int j = 0; j < friendsList.size(); j++) {
                            if (groupsList.get(localID).memberIDs.get(i).equals(friendsList.get(j).id)) {
                                Log.i("checkboxID", String.valueOf(j));
                                checkBoxList.get(j).setChecked(true);
                            }
                        }
                    }
                }
            }
            // Log.i("groupList",groupsList.get(groupID).groupName);

            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("new?",String.valueOf(isAdd));

                    currentGroup = new Group();
                    if(!isAdd) {
                        currentGroup = groupsList.get(localID);
                        currentGroup.groupID = groupID;
                    }else{
                        currentGroup.groupID = null;
                    }
                    currentGroup.memberIDs = new ArrayList<Integer>();

                    // if(nameText.getText()!= null) {

                    currentGroup.groupName = nameText.getText().toString();
                    // }else{
                    //     currentGroup.groupName = "  ";
                    //  }

                    currentGroup.groupOwnerID = PersistantDataManager.getUserID();

                    for(int i=0;i<friendsNum;i++) {
                        if(checkBoxList.get(i).isChecked()){
                            currentGroup.memberIDs.add(allFriendsList.get(i).id);
                        }
                    }


                    if(!isAdd) {
                        groupsList.set(localID,currentGroup);
                    }else{

                        groupsList.add(currentGroup);
                    }

                    ArrayList<Integer> groupIDs = new ArrayList<Integer>();
                    for(int i=0;i<groupsList.size();i++){
                        groupIDs.add(groupsList.get(i).groupID);
                    }
                    PersistantDataManager.setGroupIDs(groupIDs);

                    if(currentGroup.memberIDs.size() == 0 || currentGroup.groupName.length() == 0){
                        showShortToast("please add at least one friend and fill out the group name");
                    }else {
                        new SetGroupsTask().execute();
                    }
                }
            });
        }
    }

    public class SetGroupsTask extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response;
                Gson gson = new Gson();
                String groupJson = gson.toJson(currentGroup);
                if(!isAdd){
                    response = NetworkUtilities.put(GrubMatePreference.getGroupURL(PersistantDataManager.getUserID()),groupJson);
                }else {
                    response = NetworkUtilities.post(GrubMatePreference.getGroupURL(PersistantDataManager.getUserID()),groupJson);
                }
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String reponse) {
            showShortToast("success"+reponse);

        }
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}