package com.anmol.wedza.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.Adapters.StoryimageAdapter;
import com.anmol.wedza.CreateeventActivity;
import com.anmol.wedza.EventsActivity;
import com.anmol.wedza.Interfaces.ItemClickListener;
import com.anmol.wedza.Model.Storyimage;
import com.anmol.wedza.R;
import com.anmol.wedza.StoryMediaPreview;
import com.anmol.wedza.StoryeditActivity;
import com.anmol.wedza.WishesActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anmol on 12/29/2017.
 */

public class story extends Fragment {
    Button liked,wishes;
    RecyclerView listimg;
    TextView storycontent;
    ItemClickListener itemClickListener;
    StoryimageAdapter storyimageAdapter;
    List<Storyimage> storyimages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FloatingActionButton authpost;
    RelativeLayout likel,bwl;
    TextView nlikes,likestatus,nwishes;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.story,container,false);
        getActivity().setTitle("Story");
        liked = (Button)vi.findViewById(R.id.likedit);
        wishes = (Button)vi.findViewById(R.id.wishes);
        storycontent = (TextView)vi.findViewById(R.id.storycontent);
        listimg = (RecyclerView)vi.findViewById(R.id.listimg);
        likel = (RelativeLayout)vi.findViewById(R.id.likel);
        bwl = (RelativeLayout)vi.findViewById(R.id.bwl);
        listimg.setHasFixedSize(true);
        listimg.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        storyimages = new ArrayList<>();
        authpost = (FloatingActionButton)vi.findViewById(R.id.authpost);
        authpost.setVisibility(View.GONE);
        nlikes = (TextView)vi.findViewById(R.id.nlikes);
        nwishes = (TextView)vi.findViewById(R.id.nwishes);
        likestatus = (TextView)vi.findViewById(R.id.likestatus);
        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getActivity(), StoryMediaPreview.class);
                intent.putExtra("medialink",storyimages.get(pos).getMedialink());
                intent.putExtra("mediatype",storyimages.get(pos).getMediatype());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.still);
            }
        };
        liked.setBackgroundResource(R.drawable.unlike);

        db.collection("users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot!=null && documentSnapshot.exists()){
                    String weddingid = documentSnapshot.getString("currentwedding");
                    loadmedia(weddingid);
                    loadcontent(weddingid);
                    db.collection("weddings").document(weddingid).collection("users")
                            .document(auth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Boolean admin = task.getResult().getBoolean("admin");
                            if(admin.equals(true)){
                                authpost.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    db.collection("weddings")
                            .document(weddingid)
                            .collection("storylikes")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                if(doc.getId().contains(auth.getCurrentUser().getUid())){
                                    liked.setBackgroundResource(R.drawable.like);
                                    likestatus.setText("You loved it!");
                                }
                            }
                            String size = String.valueOf(documentSnapshots.size());
                            nlikes.setText(size + " loved this story");
                        }
                    });
                    db.collection("weddings")
                            .document(weddingid)
                            .collection("storycomments")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                                    String size = String.valueOf(documentSnapshots.size());
                                    nwishes.setText(size + " Best Wishes");
                                }
                            });
                }
            }
        });
        likel.setOnClickListener(new View.OnClickListener() {
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
                                .collection("storylikes")
                                .document(auth.getCurrentUser().getUid()).set(map);
                    }
                });
            }
        });
        bwl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WishesActivity.class));
            }
        });
        liked.setOnClickListener(new View.OnClickListener() {
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
                                .collection("storylikes")
                                .document(auth.getCurrentUser().getUid()).set(map);
                    }
                });
            }
        });
        wishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WishesActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        return vi;
    }

    private void loadcontent(String weddingid) {
        db.collection("weddings").document(weddingid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                final String st = task.getResult().getString("storycontent");
                storycontent.setText(st);
                authpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), StoryeditActivity.class);
                        intent.putExtra("storycontent",st);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.still);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void loadmedia(String weddingid) {
        storyimages.clear();
        db.collection("weddings").document(weddingid).collection("stories").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    Storyimage storyimage = new Storyimage(doc.getString("medialink"),doc.getString("mediatype"));
                    storyimages.add(storyimage);
                }
                if(!storyimages.isEmpty()){
                    storyimageAdapter = new StoryimageAdapter(getActivity(),storyimages,itemClickListener);
                    listimg.setAdapter(storyimageAdapter);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
