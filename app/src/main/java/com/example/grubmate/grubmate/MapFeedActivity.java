package com.example.grubmate.grubmate;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.MockData;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.MapFragment;

import java.util.ArrayList;

public class MapFeedActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener{

    private ArrayList<Post> postFeed;
    private FeedFragment destinationFragment2;
    private FeedFragment mFeedFragment;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map_feed);
        Intent intent = getIntent();
        postFeed = new ArrayList<>();
        if (intent.hasExtra("postList")) {

            postFeed = (ArrayList<Post>) getIntent().getSerializableExtra("postList");

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment destinationFragment = MapFragment.newInstance(null, null,postFeed);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_map, destinationFragment);


        destinationFragment2 = FeedFragment.newInstance("horizontal", null,null);

        transaction.replace(R.id.fragment_list, destinationFragment2);

        transaction.commit();
        mFeedFragment = (FeedFragment) fragmentManager.findFragmentById(R.id.fragment_list);
    }

    @Override
    public void positionFromMapFragment(int position) {
        Log.i("position",String.valueOf(position));
        destinationFragment2.scrollToPosition(position);
    }
}
