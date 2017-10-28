package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.dataClass.Subscription;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PETER on 10/19/17.
 */

public class BSubscriptionAdapter extends BaseQuickAdapter<Subscription, BaseViewHolder> {
    public BSubscriptionAdapter(@Nullable List<Subscription> data) {
        super(R.layout.subscription_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Subscription item) {
        viewHolder.setText(R.id.tv_subscription_item_name, item.query)
                // integers has to be wraped as string to avoid android treating them as resource
                .setText(R.id.tv_subscription_item_subscriptionID, String.valueOf(item.subscriptionID))
                .addOnClickListener(R.id.tv_subscription_item_subscriptionID)
                .addOnClickListener(R.id.b_subscription_item_unsubscribe);

    }

}