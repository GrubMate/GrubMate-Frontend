package com.example.grubmate.grubmate;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.dataClass.Post;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/16/17.
 */

public class PastPostAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {
    public PastPostAdapter(ArrayList<Post> data) {
        super(R.layout.past_post_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Post item) {
        helper.setText(R.id.tv_item_name, item.title);
    }
}
