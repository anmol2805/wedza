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

import com.anmol.wedza.Model.Event;
import com.anmol.wedza.Model.Keypeople;

import java.util.List;

/**
 * Created by anmol on 1/8/2018.
 */

public class KeypeopleAdapter extends ArrayAdapter<Keypeople> {
    private Activity context;
    private int resource;
    private List<Keypeople> keypeoples;

    public KeypeopleAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Keypeople> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        keypeoples = objects;
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

            return v;
        }

    }
}
