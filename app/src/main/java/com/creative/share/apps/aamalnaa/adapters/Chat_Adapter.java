package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aamalnaa.databinding.ChatAddressLeftRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatAddressRightRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatImageLeftRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatImageRightRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatMessageLeftRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatMessageRightRowBinding;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_image_LEFT = 3;
    private final int ITEM_image_RIGHT = 4;
    private final int ITEM_address_LEFT = 5;
    private final int ITEM_address_RIGHT = 6;
    private final String lang;


    private List<MessageModel.SingleMessageModel> messageModelList;
    private int current_user_id;
    private Context context;
    private LayoutInflater inflater;
private ChatActivity activity;
    public Chat_Adapter(List<MessageModel.SingleMessageModel> messageModelList, int current_user_id, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity=(ChatActivity)context;
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_MESSAGE_RIGHT)
        {
            ChatMessageRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row,parent,false);
            return new RightMessageEventHolder(binding);

        }
        else  if (viewType==ITEM_MESSAGE_LEFT)
        {
            ChatMessageLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row,parent,false);
            return new LeftMessageEventHolder(binding);

        }
        else  if (viewType==ITEM_image_LEFT)
        {
            ChatImageLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row,parent,false);
            return new LeftImageEventHolder(binding);

        }
        else  if (viewType==ITEM_address_LEFT)
        {
            ChatAddressLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_address_left_row,parent,false);
            return new LeftAddressEventHolder(binding);

        }
        else  if (viewType==ITEM_address_RIGHT)
        {
            ChatAddressRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_address_right_row,parent,false);

            return new RightAddressEventHolder(binding);

        }
        else
        {
            ChatImageRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row,parent,false);
            return new RightImageEventHolder(binding);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      MessageModel.SingleMessageModel messageModel = messageModelList.get(position);
        if (holder instanceof RightImageEventHolder)
        {
            RightImageEventHolder eventHolder = (RightImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
eventHolder.binding.setLang(lang);
            eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatActivity productActivity=(ChatActivity) context;
                    productActivity.showimage(messageModelList.get(position).getMessage());
                }
            });

        }
        else   if (holder instanceof LeftImageEventHolder)
        {
            LeftImageEventHolder eventHolder = (LeftImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);
            eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatActivity productActivity=(ChatActivity) context;
                    productActivity.showimage(messageModelList.get(position).getMessage());
                }
            });

        }
        else   if (holder instanceof RightMessageEventHolder)
        {
            RightMessageEventHolder eventHolder = (RightMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof LeftMessageEventHolder)
        {
            LeftMessageEventHolder eventHolder = (LeftMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof RightAddressEventHolder)
        {
            RightAddressEventHolder eventHolder = (RightAddressEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);
eventHolder.pos=position;

        }
        else   if (holder instanceof LeftAddressEventHolder)
        {
            LeftAddressEventHolder eventHolder = (LeftAddressEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);
eventHolder.pos=position;

        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }
    public class RightAddressEventHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public ChatAddressRightRowBinding binding;
        private GoogleMap mMap;
        int pos;
        public RightAddressEventHolder(@NonNull ChatAddressRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.mapview.onCreate(null);
            binding.mapview.onResume();
binding.mapview.getMapAsync(this);

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (googleMap != null) {
                mMap = googleMap;
                mMap.setTrafficEnabled(false);
                mMap.setBuildingsEnabled(false);
                mMap.setIndoorEnabled(true);

            mMap.addMarker(new MarkerOptions().position(new LatLng(messageModelList.get(pos).getLat(),messageModelList.get(pos).getLng())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(messageModelList.get(pos).getLat(),messageModelList.get(pos).getLng())));
            }
        }

    }
    public class LeftAddressEventHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public ChatAddressLeftRowBinding binding;
        private GoogleMap mMap;
        int pos;
        public LeftAddressEventHolder(@NonNull ChatAddressLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.mapview.onCreate(null);
            binding.mapview.onResume();
          binding.mapview.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (googleMap != null) {
                mMap = googleMap;
                mMap.setTrafficEnabled(false);
                mMap.setBuildingsEnabled(false);
                mMap.setIndoorEnabled(true);
                mMap.addMarker(new MarkerOptions().position(new LatLng(messageModelList.get(pos).getLat(),messageModelList.get(pos).getLng())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(messageModelList.get(pos).getLat(),messageModelList.get(pos).getLng())));
            }
        }



    }

    public class RightMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageRightRowBinding binding;
        public RightMessageEventHolder(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageLeftRowBinding binding;
        public LeftMessageEventHolder(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class LeftImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageLeftRowBinding binding;
        public LeftImageEventHolder(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class RightImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageRightRowBinding binding;
        public RightImageEventHolder(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel.SingleMessageModel messageModel = messageModelList.get(position);

        if (messageModel.getReceiver_id() == current_user_id) {
            Log.e("type",messageModel.getType());
            if (messageModel.getType().equals("text")) {
                return ITEM_MESSAGE_LEFT;
            }
            else if(messageModel.getType().equals("address")){
                return ITEM_address_LEFT;
            }
            else  {
                return ITEM_image_LEFT;
            }
        }
        else {
            if (messageModel.getType().equals("text")) {
                return ITEM_MESSAGE_RIGHT;
            }
            else if(messageModel.getType().equals("address")){
                return ITEM_address_RIGHT;
            }
            else  {
                return ITEM_image_RIGHT;
            }


        }


}}
