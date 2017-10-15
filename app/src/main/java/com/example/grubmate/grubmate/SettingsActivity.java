package com.example.grubmate.grubmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.JsonUtilities;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox allergy1, allergy2, allergy3;
    private boolean allergies[];
    private int groupsNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addGroupsButton);

        allergy1 = (CheckBox) findViewById(R.id.allergy1);
        allergy2 = (CheckBox) findViewById(R.id.allergy2);
        allergy3 = (CheckBox) findViewById(R.id.allergy3);

        allergies = new boolean[3];
        allergies[0] = allergy1.isChecked();
        allergies[1] = allergy2.isChecked();
        allergies[2] = allergy3.isChecked();

        final LinearLayout ll = (LinearLayout)findViewById(R.id.settingsLayout);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
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

     //   new SettingsTask().execute(groupURl);
        for(int i=0;i<3;i++){
            Button myButton = new Button(this);
            myButton.setText(String.valueOf(i));
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this,GroupSettingsActivity.class);
                    startActivity(intent);
                }
            });
            ll.addView(myButton, lp);
        }

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
                return JsonUtilities.getGroupList(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Group> groupsList) {
            groupsNum = groupsList.size();
            showShortToast("groupsNum = " + groupsNum);
        }
    }
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
