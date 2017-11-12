package com.example.grubmate.grubmate.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.grubmate.grubmate.R;
import com.example.grubmate.grubmate.dataClass.Post;
import com.example.grubmate.grubmate.utilities.ArrayUtilities;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

/**
 * Created by tianhangliu on 11/11/17.
 */

public class BOrderAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {
    public BOrderAdapter(@Nullable List<Post> data) {
        super(R.layout.order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Post item) {
        String leftQuantity = "Left Quantity: "+String.valueOf(item.leftQuantity)+"/"+String.valueOf(item.totalQuantity);
        String allergies = "Allergies: ";
        if(item.allergyInfo!=null) {
            if(item.allergyInfo[0]) allergies +=(item.allergyNames[0]+" ");
            if(item.allergyInfo[1]) allergies +=(item.allergyNames[1]+" ");
            if(item.allergyInfo[2]) allergies += (item.allergyNames[2]+" ");
        }
        viewHolder.setText(R.id.tv_order_item_name, item.title)
                // integers has to be wraped as string to avoid android treating them as resource
                .setText(R.id.tv_order_item_tag, ArrayUtilities.join(item.tags, ","))
                .setText(R.id.tv_order_item_description, item.description==null?"No Description":item.description)
                .setText(R.id.tv_order_item_allergy, allergies)
                .setGone(R.id.ll_order_detail, false)
                .addOnClickListener(R.id.b_order_detail)
                .addOnClickListener(R.id.b_order_item_map);
        ;
        if(item.timePeriod!=null) {
            viewHolder.setText(R.id.tv_order_item_date, item.timePeriod[0] + " - " + item.timePeriod[1]);
        }
        if(item.postPhotos==null||item.postPhotos.length<1) {
            item.postPhotos = new String[]{"empty"};
        }
        CarouselView carouselView = viewHolder.getView(R.id.cv_order_item_image);
        carouselView.setPageCount(item.postPhotos.length);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.with(mContext)
                        .load(GrubMatePreference.getImageUrl(item.postPhotos[position]))
                        .placeholder(R.drawable.mr_dialog_material_background_dark)
                        .into(imageView);
            }
        };
        carouselView.setImageListener(imageListener);
    }
}