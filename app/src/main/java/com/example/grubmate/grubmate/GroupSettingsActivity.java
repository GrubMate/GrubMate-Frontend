package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private ArrayList<Pair<Integer,String>> allFriendsList;
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
        new GetFriendListTask().execute(GrubMatePreference.getFriendlistURL(PersistantDataManager.getUserID()));
    }

    public class GetFriendListTask extends AsyncTask<String, Integer, ArrayList<Pair<Integer,String>>> {

        @Override
        protected ArrayList<Pair<Integer,String>> doInBackground(String... params) {
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
        protected void onPostExecute(ArrayList<Pair<Integer,String>> friendsList) {
            allFriendsList = friendsList;
            friendsNum = friendsList.size();
            showShortToast("friendsNum = " + friendsNum);
            for(int i=0;i<friendsNum;i++) {
                CheckBox newCheckbox = new CheckBox(GroupSettingsActivity.this);
                newCheckbox.setText(friendsList.get(i).second);
                checkBoxList.add(newCheckbox);
                ll.addView(newCheckbox, lp);
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
                    currentGroup.groupName = nameText.getText().toString();

                    currentGroup.groupOwnerID = PersistantDataManager.getUserID();
                    for(int i=0;i<friendsNum;i++) {
                        if(checkBoxList.get(i).isChecked()){
                            currentGroup.memberIDs[i] = allFriendsList.get(i).first;
                        }
                    }

                    new SetGroupsTask().execute(GrubMatePreference.getGroupURL(PersistantDataManager.getUserID()));

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
                if(isAdd){
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
