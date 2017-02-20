package com.zc.guessmusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zc.guessmusic.R;
import com.zc.guessmusic.model.Photo;
import com.zc.guessmusic.ui.SecondActivity;

import java.util.List;

/**
 * Created by Panda_Program on 2017/2/18 0018.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder>{

    private Context mContext;
    private List<Photo> mPhtotList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView photoImage;
        TextView phoneName;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            photoImage= (ImageView) itemView.findViewById(R.id.first_phone_image);
            phoneName= (TextView) itemView.findViewById(R.id.first_phone_name);
        }
    }
    public PhoneAdapter(List<Photo> phtotList){
        mPhtotList=phtotList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.cardview,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Photo photo=mPhtotList.get(position);
                Intent intent=new Intent(mContext, SecondActivity.class);
                intent.putExtra(SecondActivity.PHOTO_NAME,photo.getName());
                intent.putExtra(SecondActivity.PHOTO_IMAGE_ID,photo.getImageId());
                intent.putExtra(SecondActivity.PHOTO_CONTEXT,photo.getContext());
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo=mPhtotList.get(position);

        holder.phoneName.setText(photo.getName());
        Glide.with(mContext).load(photo.getImageId()).into(holder.photoImage);
        Log.e("cardView---------",holder.phoneName.getText().toString());
    }


    @Override
    public int getItemCount() {
        return mPhtotList.size();
    }
}
