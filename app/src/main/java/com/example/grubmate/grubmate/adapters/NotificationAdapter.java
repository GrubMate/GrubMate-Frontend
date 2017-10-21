package com.example.grubmate.grubmate.adapters;

import android.os.NetworkOnMainThreadException;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Notification;

import java.util.List;

/**
 * Created by tianhangliu on 10/20/17.
 */

public class NotificationAdapter extends BaseQuickAdapter<Notification, BaseViewHolder> {

    public NotificationAdapter(List<Notification> notificationData) {
        super(R.layout.notification_list_item, notificationData);
    }

    @Override
    protected void convert(BaseViewHolder helper, Notification item) {
        helper.addOnClickListener(R.id.b_notification_accept)
                .addOnClickListener(R.id.b_notification_deny)
                .addOnClickListener(R.id.b_notification_request);
        switch (item.type) {
            case  Notification.REQUEST:
                helper.setVisible(R.id.ll_notification_request, true)
                        .setText(R.id.tv_notification_request_name, item.title)
                        .setText(R.id.tv_notification_request_requester_name, item.requesterName);
                break;
            case Notification.MATCH:
                helper.setVisible(R.id.ll_notification_accepted, true)
                        .setText(R.id.tv_notification_match_name, item.title)
                        .setText(R.id.tv_notification_match_poster_name, item.posterName);
                break;
            case Notification.ACCEPTED:
                helper.setVisible(R.id.ll_notification_accepted, true)
                        .setText(R.id.tv_notification_accepted_name, item.title);
                break;
            default:
        }
    }
}