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
import android.widget.ImageView;
import android.widget.TextView;

import com.anmol.wedza.Model.Comment2;
import com.anmol.wedza.Model.Event;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anmol on 1/8/2018.
 */

public class EventsAdapter extends ArrayAdapter<Event> {
    private Activity context;
    private int resource;
    private List<Event> events;

    public EventsAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Event> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        events = objects;
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
            TextView eventname = (TextView)v.findViewById(R.id.eventname);
            TextView eventtime = (TextView)v.findViewById(R.id.eventtime);
            TextView eventloc = (TextView)v.findViewById(R.id.eventloc);
            TextView eventteam = (TextView)v.findViewById(R.id.eventteam);
            TextView eventdes = (TextView)v.findViewById(R.id.eventdes);
            ImageView eventimg = (ImageView)v.findViewById(R.id.eventimg);
            ImageView teamicon = (ImageView)v.findViewById(R.id.teamicon);
            eventname.setText(events.get(position).getEventname());
            eventtime.setText(events.get(position).getEventtime());
            eventloc.setText(events.get(position).getEventlocation());
            eventteam.setText(events.get(position).getTeam());
            eventdes.setText(events.get(position).getEventdes());
            String team = events.get(position).getTeam();
            if(team.contains("groom")){
                teamicon.setBackgroundResource(R.drawable.groomr);
            }
            else if(team.contains("bride")){
                teamicon.setBackgroundResource(R.drawable.brider);
            }
            else if(team.contains("both")){
                teamicon.setBackgroundResource(R.drawable.bothsides);
            }
            Glide.with(context).load(events.get(position).getEventimg()).into(eventimg);
            return v;
        }

    }

}
