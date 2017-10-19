package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Post;
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
                .setText(R.id.tv_feed_item_poster, item.posterID)
                .addOnClickListener(R.id.b_feed_item_request);
        Picasso.with(this.mContext).load(R.drawable.ic_media_play_dark).into((ImageView) viewHolder.getView(R.id.iv_feed_item_image));

    }
}