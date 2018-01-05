package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.anmol.wedza.Adapters.TimelineAdapter;
import com.anmol.wedza.AlertsActivity;
import com.anmol.wedza.EventsActivity;
import com.anmol.wedza.KeypeopleActivity;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 12/25/2017.
 */

public class home extends Fragment implements AbsListView.OnScrollListener{
    ListView lv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView coverpic;
    int lastTopValue = 0;
    List<Timeline> timelines;
    TimelineAdapter timelineAdapter;
    Button keypeople,alerts,events;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home,container,false);
        lv = (ListView)view.findViewById(R.id.newsfeed);
        keypeople = (Button)view.findViewById(R.id.keypeople);
        alerts = (Button)view.findViewById(R.id.alerts);
        events = (Button)view.findViewById(R.id.events);
        timelines = new ArrayList<>();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup)layoutInflater.inflate(R.layout.listheader,lv,false);
        lv.addHeaderView(header,null,false);
        coverpic = (ImageView)header.findViewById(R.id.listHeaderImage);
        db.collection("weddings").document("wedding1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Glide.with(getActivity()).load(task.getResult().getString("coverpic")).into(coverpic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        lv.setOnScrollListener(this);
        timelines.clear();
        db.collection("weddings/wedding1/timeline").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    Timeline timeline = new Timeline(doc.getString("medialink"),doc.getString("event"),doc.getString("mediatype"));
                    timelines.add(timeline);
                }
                timelineAdapter = new TimelineAdapter(getActivity(),R.layout.timeline,timelines);
                lv.setAdapter(timelineAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        keypeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KeypeopleActivity.class));
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventsActivity.class));
            }
        });
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AlertsActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Rect rect = new Rect();
        coverpic.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            coverpic.setY((float) (rect.top / 2.0));
        }
    }
}
