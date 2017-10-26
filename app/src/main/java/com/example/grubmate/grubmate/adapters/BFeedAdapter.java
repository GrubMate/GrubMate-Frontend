package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tianhangliu on 10/18/17.
 */

public class BFeedAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {
    public BFeedAdapter(@Nullable List<Post> data) {
        super(R.layout.feed_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Post item) {
        viewHolder.setText(R.id.tv_feed_item_name, item.title)
                // integers has to be wraped as string to avoid android treating them as resource
                .setText(R.id.tv_feed_item_poster, item.posterName)
                .addOnClickListener(R.id.tv_feed_item_poster)
                .addOnClickListener(R.id.b_feed_item_request);
        if(item.postPhotos!=null && item.postPhotos.length > 0) {
            Picasso.with(this.mContext)
                    .load(GrubMatePreference.getImageUrl(item.postPhotos[0]))
                    .placeholder(R.drawable.mr_dialog_material_background_dark)
                    .into((ImageView) viewHolder.getView(R.id.iv_feed_item_image));
        } else {
            Picasso.with(this.mContext)
                    .load(R.drawable.mr_dialog_material_background_dark)
                    .into((ImageView) viewHolder.getView(R.id.iv_feed_item_image));
        }
                if(item.requestsIDs != null && item.requestsIDs.contains(PersistantDataManager.getUserID())) {
                    viewHolder.getView(R.id.b_feed_item_request).setEnabled(false);
                }
    }
}