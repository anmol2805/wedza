package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.AlbumActivity;
import com.anmol.wedza.CommentsActivity;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.anmol.wedza.StoryMediaPreview;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.models.Document;

/**
 * Created by anmol on 12/26/2017.
 */

public class TimelineAdapter extends ArrayAdapter<Timeline> {
    private Activity context;
    private int resource;
    private List<Timeline> timelines;

    public TimelineAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Timeline> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        timelines = objects;
    }
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userid = auth.getCurrentUser().getUid();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            ImageView mediaimg = (ImageView)v.findViewById(R.id.mediaphoto);
            ImageView playicon = (ImageView)v.findViewById(R.id.playicon);
            ImageView userpic = (ImageView)v.findViewById(R.id.userpic);
            final Button like = (Button)v.findViewById(R.id.like);
            final Button unlike = (Button)v.findViewById(R.id.unlike);
            Button comment = (Button)v.findViewById(R.id.comment);
            final Button share = (Button)v.findViewById(R.id.share);
            TextView uname = (TextView)v.findViewById(R.id.uname);
            TextView event = (TextView)v.findViewById(R.id.eventname);
            TextView des = (TextView)v.findViewById(R.id.des);
            final TextView numlike = (TextView)v.findViewById(R.id.numlike);
            uname.setText(timelines.get(position).getUsername());
            event.setText(timelines.get(position).getEvent());
            des.setText(timelines.get(position).getDes());
            unlike.setVisibility(View.INVISIBLE);
            Glide.with(context).load(timelines.get(position).getUserpic()).into(userpic);
            if(timelines.get(position).getMediatype().contains("image")){
                playicon.setVisibility(View.GONE);
                Glide.with(getContext()).load(timelines.get(position).getMedialink()).into(mediaimg);
            }
            else if(timelines.get(position).getMediatype().contains("video")){
                playicon.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(timelines.get(position).getMedialink()).into(mediaimg);
            }
            mediaimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StoryMediaPreview.class);
                    intent.putExtra("medialink",timelines.get(position).getMedialink());
                    intent.putExtra("mediatype",timelines.get(position).getMediatype());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_left_in,R.anim.still);
                }
            });
//            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    String weddingid = task.getResult().getString("currentwedding");
//                    db.collection("weddings")
//                            .document(weddingid)
//                            .collection("timeline")
//                            .document(timelines.get(position).getPostid())
//                            .collection("likes").get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    for(DocumentSnapshot doc:task.getResult()){
//                                        if(doc.getId().contains(auth.getCurrentUser().getUid())){
//                                            like.setVisibility(View.INVISIBLE);
//                                        }
//                                    }
//                                }
//                            });
//                }
//            });
            db.collection("users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        String weddingid = documentSnapshot.getString("currentwedding");
                        db.collection("weddings")
                                .document(weddingid)
                                .collection("timeline")
                                .document(timelines.get(position).getPostid())
                                .collection("likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if(documentSnapshots!=null && !documentSnapshots.isEmpty()){
                                    for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                        if(doc.exists()){
                                            if(doc.getId().contains(auth.getCurrentUser().getUid())){
                                                like.setVisibility(View.INVISIBLE);
                                                unlike.setVisibility(View.VISIBLE);
                                            }

                                        }

                                    }
                                    int size = documentSnapshots.size();
                                    if(size == 0){
                                        numlike.setVisibility(View.GONE);
                                    }
                                    else if (size == 1){
                                        numlike.setVisibility(View.VISIBLE);
                                        numlike.setText(String.valueOf(size) + " like");
                                    }
                                    else {
                                        numlike.setVisibility(View.VISIBLE);
                                        numlike.setText(String.valueOf(size) + " likes");
                                    }
                                }

                            }
                        });
                    }
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("like",true);
                            String weddingid = task.getResult().getString("currentwedding");
                            db.collection("weddings")
                                    .document(weddingid)
                                    .collection("timeline")
                                    .document(timelines.get(position).getPostid())
                                    .collection("likes").document(auth.getCurrentUser().getUid()).set(map);
                        }
                    });
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("postid",timelines.get(position).getPostid());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_left_in,R.anim.still);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharepic(timelines.get(position).getMedialink());
                }
            });
            return v;
        }

    }

    private void sharepic(String medialink) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,medialink);
        context.startActivity(Intent.createChooser(shareIntent, "Share..."));

    }


}
