package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anmol.wedza.Model.Comment2;
import com.anmol.wedza.Model.Wish;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 1/12/2018.
 */

public class WishesAdapter extends ArrayAdapter<Wish> {
    private Activity context;
    private int resource;
    private List<Wish> wishes;

    public WishesAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Wish> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        wishes = objects;
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
            CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.picuser);
            TextView textView = (TextView)v.findViewById(R.id.username);
            TextView commenttext = (TextView)v.findViewById(R.id.commenttext);
            Glide.with(context).load(wishes.get(position).getProfilepic()).into(circleImageView);
            textView.setText(wishes.get(position).getWishedby());
            commenttext.setText(wishes.get(position).getWish());
            return v;
        }

    }
}
