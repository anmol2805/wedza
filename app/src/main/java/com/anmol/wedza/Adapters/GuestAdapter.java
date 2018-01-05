package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.Model.Guest;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anmol on 12/27/2017.
 */

public class GuestAdapter extends ArrayAdapter<Guest> {
    private Activity context;
    private int resource;
    private List<Guest> guests;

    public GuestAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Guest> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        guests = objects;
    }
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            TextView name = (TextView)v.findViewById(R.id.name);
            ImageView profpic = (ImageView)v.findViewById(R.id.profilepic);
            Glide.with(context).load(guests.get(position).getProfilepicturepath()).into(profpic);
            name.setText(guests.get(position).getName());
            return v;
        }

    }
}
