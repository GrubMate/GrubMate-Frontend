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
    public RequestAdapter(ArrayList<UserRequest> data) {
        super(R.layout.request_list_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, UserRequest item) {
        helper.setText(R.id.item_name, String.valueOf(item.requesterID))
        .addOnClickListener(R.id.b_accept)
        .addOnClickListener(R.id.b_deny);
        if(item.status == "ACCEPTED" || item.status=="DENIED") {
            helper.setGone(R.id.b_accept, false)
                    .setGone(R.id.b_accept, false);

        }
    }
}
