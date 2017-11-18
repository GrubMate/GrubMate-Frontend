package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Transaction;

import java.util.List;

/**
 * Created by tianhangliu on 11/13/17.
 */
public class BTransactionAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder>{

    public BTransactionAdapter(@Nullable List<Transaction> data) {
        super(R.layout.transaction_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Transaction item) {
            helper.setText(R.id.tv_transaction_post_name, item.postName)
                    .setText(R.id.tv_transaction_poster_name, item.posterName)
                    .setText(R.id.tv_transaction_requester_name, item.requesterName)
                    .addOnClickListener(R.id.b_transaction_rating_submit)
                    .addOnClickListener(R.id.b_transaction_request_cancel);
            if(!item.isActive&&!item.rated) {
                helper.setVisible(R.id.ll_transaction_rating, true);
            } else {
                helper.setGone(R.id.ll_transaction_rating, false);
            }
    }
}
