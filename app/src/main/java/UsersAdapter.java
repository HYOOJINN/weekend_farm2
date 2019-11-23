package com.example.buyer_map;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<sellerData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<sellerData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView crop;
        protected TextView address;


        public CustomViewHolder(View view) {
            super(view);
            this.crop = (TextView) view.findViewById(R.id.textView_crop);
            this.address = (TextView) view.findViewById(R.id.textView_address);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_test, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.crop.setText(mList.get(position).getSeller_crop());
        viewholder.address.setText(mList.get(position).getSeller_address());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}