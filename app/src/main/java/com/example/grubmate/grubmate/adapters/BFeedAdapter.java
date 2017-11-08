package com.example.grubmate.grubmate.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by tianhangliu on 10/18/17.
 */

public class BFeedAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {
    public BFeedAdapter(@Nullable List<Post> data) {
        super(R.layout.feed_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Post item) {
        if(item.isActive){
            String leftQuantity = "Left Quantity: "+String.valueOf(item.leftQuantity)+"/"+String.valueOf(item.totalQuantity);
            String allergies = "Allergies: ";
            if(item.allergyInfo!=null) {
                if(item.allergyInfo[0]) allergies +=(item.allergyNames[0]+" ");
                if(item.allergyInfo[1]) allergies +=(item.allergyNames[1]+" ");
                if(item.allergyInfo[2]) allergies += (item.allergyNames[2]+" ");
            }
            viewHolder.setText(R.id.tv_feed_item_name, item.title)
                    // integers has to be wraped as string to avoid android treating them as resource
                    .setText(R.id.tv_feed_item_poster, item.posterName)
                    .setText(R.id.tv_feed_item_quantity,leftQuantity)
                    .setText(R.id.tv_feed_item_tag, ArrayUtilities.join(item.tags, ","))
                    .setText(R.id.tv_feed_item_description, item.description==null?"No Description":item.description)
                    .setText(R.id.tv_feed_item_allergy, allergies)
                    .addOnClickListener(R.id.tv_feed_item_poster)
                    .addOnClickListener(R.id.b_feed_item_request);
            if(item.timePeriod!=null) {
                viewHolder.setText(R.id.tv_feed_item_date, item.timePeriod[0] + " - " + item.timePeriod[1]);
            }
        }else{
            viewHolder.setText(R.id.tv_feed_item_name, item.title)
                    // integers has to be wraped as string to avoid android treating them as resource
                    .setText(R.id.tv_feed_item_poster, item.posterName)
                    .addOnClickListener(R.id.tv_feed_item_poster)
                    .setVisible(R.id.b_feed_item_request,false);
        }
        if(item.postPhotos==null||item.postPhotos.length<1) {
            item.postPhotos = new String[]{"empty"};
        }
            CarouselView carouselView = viewHolder.getView(R.id.cv_feed_item_image);
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

        if(item.requestsIDs != null && item.requestsIDs.contains(PersistantDataManager.getUserID())) {
                    viewHolder.getView(R.id.b_feed_item_request).setEnabled(false);
        }
    }
}