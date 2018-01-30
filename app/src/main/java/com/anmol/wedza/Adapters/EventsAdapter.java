package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anmol.wedza.CreateeventActivity;
import com.anmol.wedza.Model.Comment2;
import com.anmol.wedza.Model.Event;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            TextView eventname = (TextView)v.findViewById(R.id.eventname);
            TextView eventtime = (TextView)v.findViewById(R.id.eventtime);
            TextView eventloc = (TextView)v.findViewById(R.id.eventloc);
            TextView eventteam = (TextView)v.findViewById(R.id.eventteam);
            TextView eventdes = (TextView)v.findViewById(R.id.eventdes);
            ImageView eventimg = (ImageView)v.findViewById(R.id.eventimg);
            ImageView teamicon = (ImageView)v.findViewById(R.id.teamicon);
            Button loc = (Button)v.findViewById(R.id.loc);
            LinearLayout locl = (LinearLayout)v.findViewById(R.id.locl);
            locl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String map = "http://maps.google.co.in/maps?q=" +events.get(position).getEventlocation();
                    Uri mapuri = Uri.parse("geo:0,0?q=" + Uri.encode(events.get(position).getEventlocation()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, mapuri);
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                }
            });
            loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri mapuri = Uri.parse("geo:0,0?q=" + Uri.encode(events.get(position).getEventlocation()));
                    String map = "http://maps.google.co.in/maps?q=" + events.get(position).getEventlocation();
                    Intent intent = new Intent(Intent.ACTION_VIEW, mapuri);
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                }
            });
//            final ImageButton edit = (ImageButton)v.findViewById(R.id.edit);
//            final FirebaseAuth auth = FirebaseAuth.getInstance();
//            final FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    String weddingid = task.getResult().getString("currentwedding");
//                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid())
//                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            Boolean admin = task.getResult().getBoolean("admin");
//                            if(admin){
//                                edit.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                }
//            });
//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final Dialog dialog = new Dialog(context);
//                    dialog.setTitle("Edit ");
//                    dialog.setContentView(R.layout.editkey);
//                }
//            });
            eventname.setText(events.get(position).getEventname());
            eventtime.setText(events.get(position).getEventtime());
            eventloc.setText(events.get(position).getEventlocation());
            eventteam.setText(events.get(position).getTeam());
            eventdes.setText(events.get(position).getEventdes());
            String team = events.get(position).getTeam();
            if(team.contains("groom")){
                teamicon.setBackgroundResource(R.drawable.suit);
            }
            else if(team.contains("bride")){
                teamicon.setBackgroundResource(R.drawable.gown);
            }
            else if(team.contains("both")){
                teamicon.setBackgroundResource(R.drawable.bothred);
            }
            Glide.with(context).load(events.get(position).getEventimg()).into(eventimg);
            return v;
        }

    }

}
