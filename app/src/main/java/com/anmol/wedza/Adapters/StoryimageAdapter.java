package com.anmol.wedza.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.anmol.wedza.Interfaces.ItemClickListener;
import com.anmol.wedza.Model.Storyimage;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anmol on 12/30/2017.
 */

public class StoryimageAdapter extends RecyclerView.Adapter<StoryimageAdapter.MyViewHolder> {
    Context c;
    List<Storyimage> storyimages;
    private ItemClickListener mitemClickListener;

    public StoryimageAdapter(Context c, List<Storyimage> storyimages, ItemClickListener mitemClickListener) {
        this.c = c;
        this.storyimages = storyimages;
        this.mitemClickListener = mitemClickListener;
    }

    @Override
    public StoryimageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.storymedia,parent,false);
        return new MyViewHolder(v,mitemClickListener);
    }

    @Override
    public void onBindViewHolder(final StoryimageAdapter.MyViewHolder holder, int position) {
        if(storyimages.get(position).getMediatype().contains("image")){
            holder.mimg.setVisibility(View.VISIBLE);
            Glide.with(c).load(storyimages.get(position).getMedialink()).into(holder.mimg);
        }
        else if(storyimages.get(position).getMediatype().contains("video")){
            holder.mvid.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(c);
            mediaController.setAnchorView(holder.mvid);
            holder.mvid.setMediaController(mediaController);
            holder.mvid.setVideoURI(Uri.parse(storyimages.get(position).getMedialink()));
            holder.mvid.requestFocus();
            holder.mvid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    holder.mvid.start();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return storyimages.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mimg;
        VideoView mvid;
        private ItemClickListener mitemClickListener;
        public MyViewHolder(View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            mitemClickListener = itemClickListener;
            mimg = (ImageView)itemView.findViewById(R.id.mediaimg);
            mvid = (VideoView)itemView.findViewById(R.id.mediavid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mitemClickListener.onItemClick(this.getAdapterPosition());
        }
    }
}
