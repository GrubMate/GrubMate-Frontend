package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Group;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PETER on 10/23/17.
 */

public class BGroupAdapter extends BaseQuickAdapter<Group, BaseViewHolder> {
    public BGroupAdapter(@Nullable List<Group> data) {
        super(R.layout.group_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Group item) {
        viewHolder.setText(R.id.tv_group_item_name, item.groupName)
                // integers has to be wraped as string to avoid android treating them as resource
                .addOnClickListener(R.id.b_group_item_edit);

    }
}