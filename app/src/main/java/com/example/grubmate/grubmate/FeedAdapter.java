package com.example.grubmate.grubmate;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by tianhangliu on 10/2/17.
 */

// TODO 01: After the class for feedData has been solidized, should change String to that

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    public final static String TAG = "FeedAdapter";
    // Will be changed to an array of FeedListItem in future versions
    private String[] mFeedData;
    // Allows Activity to interact with this adapter
    private final FeedAdapterOnClickHandler mClickHandler;

    public interface FeedAdapterOnClickHandler {
        void onClick(String feedItemData);
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mFeedNameTextView;
        public FeedViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "itemCreate");
            mFeedNameTextView = itemView.findViewById(R.id.tv_feed_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String feedItemData = mFeedData[adapterPosition];
            mClickHandler.onClick(feedItemData);
        }
    }

    public FeedAdapter(FeedAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, parent.toString());
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.feed_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        String feedItemData = mFeedData[position];
//        Log.d(TAG, "onBind"+Arrays.toString(mFeedData));
        holder.mFeedNameTextView.setText(String.valueOf(feedItemData));
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "itemCount:" + Arrays.toString(mFeedData));
        if (null == mFeedData) return 0;
        return mFeedData.length;
    }

    // Allows data to be refreshed without creating new adapter
    public void setFeedData(String[] feedData) {
        mFeedData = feedData;
//        Log.d(TAG, Arrays.toString(feedData));
        notifyDataSetChanged();
    }

}
