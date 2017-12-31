package com.anmol.wedza.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.anmol.wedza.Model.Gallery;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

/**
 * Created by anmol on 12/28/2017.
 */

public class GalleryAdapter extends BaseAdapter {
    private Context ctx;
    private int resource;
    private ArrayList<Gallery> galleries;

    public GalleryAdapter(Context ctx, int resource, ArrayList<Gallery> galleries) {
        this.ctx = ctx;
        this.resource = resource;
        this.galleries = galleries;
    }

    @Override
    public int getCount() {
        return galleries.size();
    }

    @Override
    public Object getItem(int position) {
        return galleries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageView img;
        VideoView mvid;
        RelativeLayout vidlayout;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource,null);
            holder.img = (ImageView)row.findViewById(R.id.galleryimg);
            holder.mvid = (VideoView)row.findViewById(R.id.mvid);
            holder.vidlayout = (RelativeLayout)row.findViewById(R.id.vidlayout);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }
        if(galleries.get(position).getMediatype().contains("image")){
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(ctx).load(galleries.get(position).getUrl()).into(holder.img);
        }
        else if(galleries.get(position).getMediatype().contains("video")){
            holder.vidlayout.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(ctx);
            mediaController.setAnchorView(holder.mvid);
            holder.mvid.setMediaController(mediaController);
            holder.mvid.setVideoURI(Uri.parse(galleries.get(position).getUrl()));
            holder.mvid.requestFocus();
            final ViewHolder finalHolder = holder;
            holder.mvid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    finalHolder.mvid.start();
                }
            });
        }

        return row;
    }
}
