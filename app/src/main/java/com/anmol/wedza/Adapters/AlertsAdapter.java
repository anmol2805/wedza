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

import com.anmol.wedza.Model.Alert;
import com.anmol.wedza.Model.Comment2;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 1/8/2018.
 */

public class AlertsAdapter extends ArrayAdapter<Alert> {
    private Activity context;
    private int resource;
    private List<Alert> alerts;

    public AlertsAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Alert> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        alerts = objects;
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
            TextView textView = (TextView)v.findViewById(R.id.username);
            TextView commenttext = (TextView)v.findViewById(R.id.commenttext);
            textView.setText(alerts.get(position).getUsername());
            commenttext.setText(alerts.get(position).getContent());
            return v;
        }

    }
}
