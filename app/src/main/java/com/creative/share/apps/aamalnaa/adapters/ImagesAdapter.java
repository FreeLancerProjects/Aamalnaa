package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads.UpdateAdsActivity;
import com.creative.share.apps.aamalnaa.databinding.ImageRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<Uri> urlList;
    private Context context;
    private AddAdsActivity activity;
private UpdateAdsActivity updateAdsActivity;
    public ImagesAdapter(List<Uri> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;

if(context instanceof  AddAdsActivity){
    activity=(AddAdsActivity)context;
}
else if(context instanceof UpdateAdsActivity){
    updateAdsActivity=(UpdateAdsActivity)context;
}

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri url = urlList.get(position);

        holder.imageRowBinding.setUrl(url.toString());

        holder.imageRowBinding.imageDelete.setOnClickListener(view -> {
            if(context instanceof  AddAdsActivity){
                    activity.deleteImage(holder.getAdapterPosition());}
            else if(context instanceof  UpdateAdsActivity){
                updateAdsActivity.deleteImage(holder.getAdapterPosition());
            }

                }
        );


    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;

        public MyHolder(ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
