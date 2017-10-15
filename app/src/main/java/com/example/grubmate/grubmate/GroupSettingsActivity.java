package com.example.grubmate.grubmate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;

import java.io.IOException;
import java.util.ArrayList;

public class GroupSettingsActivity extends AppCompatActivity {
    private ArrayList<CheckBox> checkBoxList;
    private int friendsNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_settings);
        LinearLayout ll = (LinearLayout)findViewById(R.id.group_linearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxList = new ArrayList<CheckBox>();

        for(int i=0;i<10;i++) {
            CheckBox newCheckbox = new CheckBox(this);
            newCheckbox.setText("friends "+ String.valueOf(i));
            checkBoxList.add(newCheckbox);
            ll.addView(newCheckbox, lp);
        }
        }

    public class GetGroupsTask extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
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
        protected void onPostExecute(ArrayList<String> friendsList) {
            friendsNum = friendsList.size();
            showShortToast("friendsNum = " + friendsNum);
        }
    }
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
