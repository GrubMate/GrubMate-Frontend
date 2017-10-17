package com.example.grubmate.grubmate;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.dataClass.User;
import com.example.grubmate.grubmate.dataClass.UserRequest;
import com.facebook.places.internal.LocationPackageManager;

import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/16/17.
 */

public class RequestAdapter extends BaseQuickAdapter<UserRequest, BaseViewHolder>{
    public RequestAdapter(int layoutResId, ArrayList<UserRequest> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, UserRequest item) {
        helper.setText(R.id.item_name, item.requesterID)
        .addOnClickListener(R.id.b_accept);
    }
}
