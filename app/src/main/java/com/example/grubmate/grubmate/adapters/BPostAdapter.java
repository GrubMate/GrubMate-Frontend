package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tianhangliu on 10/21/17.
 */

public class BPostAdapter extends BaseQuickAdapter<Post, BaseViewHolder>{
    public BPostAdapter(@Nullable List<Post> data) {
        super(R.layout.post_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Post item) {
        viewHolder.setText(R.id.tv_post_name, item.title)
                // integers has to be wraped as string to avoid android treating them as resource
                .addOnClickListener(R.id.b_post_delete)
                .addOnClickListener(R.id.b_post_edit);
        if(item.totalQuantity - item.leftQuantity != 0) {
            viewHolder.getView(R.id.b_post_edit).setEnabled(false);
            viewHolder.getView(R.id.b_post_delete).setEnabled(false);
        }
    }
}
