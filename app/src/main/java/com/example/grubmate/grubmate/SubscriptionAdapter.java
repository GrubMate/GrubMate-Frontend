package com.example.grubmate.grubmate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.grubmate.grubmate.dataClass.Subscription;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/9/17.
 */

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>{
    public final static String TAG = "SubscriptionAdapter";
    // Will be changed to an array of SubscriptionListItem in future versions
    private ArrayList<Subscription> mSubscriptionData;
    // Allows Activity to interact with this adapter
    private final SubscriptionAdapterOnClickHandler mClickHandler;

    public interface SubscriptionAdapterOnClickHandler {
        void onClick(Subscription SubscriptionItemData);
    }

    public class SubscriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mSubscriptionNameTextView;
        public SubscriptionViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "itemCreate");
            mSubscriptionNameTextView = itemView.findViewById(R.id.tv_subscription_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Subscription SubscriptionItemData = mSubscriptionData.get(adapterPosition);
            mClickHandler.onClick(SubscriptionItemData);
        }
    }

    public SubscriptionAdapter(SubscriptionAdapter.SubscriptionAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public SubscriptionAdapter.SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, parent.toString());
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.subscription_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new SubscriptionAdapter.SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubscriptionAdapter.SubscriptionViewHolder holder, int position) {
        Subscription SubscriptionItemData = mSubscriptionData.get(position);
//        Log.d(TAG, "onBind"+Arrays.toString(mSubscriptionData));
        holder.mSubscriptionNameTextView.setText(String.valueOf(SubscriptionItemData.query));
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "itemCount:" + Arrays.toString(mSubscriptionData));
        if (null == mSubscriptionData) return 0;
        return mSubscriptionData.size();
    }

    // Allows data to be refreshed without creating new adapter
    public void setSubscriptionData(ArrayList<Subscription> SubscriptionData) {
        mSubscriptionData = SubscriptionData;
//        Log.d(TAG, Arrays.toString(SubscriptionData));
        notifyDataSetChanged();
    }

}