package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anmol.wedza.Adapters.GalleryAdapter;
import com.anmol.wedza.Adapters.GalleryAlbumAdapter;
import com.anmol.wedza.AlbumActivity;
import com.anmol.wedza.Model.Gallery;
import com.anmol.wedza.R;
import com.anmol.wedza.StoryMediaPreview;
import com.facebook.FacebookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 12/28/2017.
 */

public class gallery extends Fragment {
    List<Gallery> galleries;
    GalleryAdapter galleryAdapter;
    GridView gridView;
    Button allpics,albums;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GalleryAlbumAdapter galleryAlbumAdapter;
    int selectintent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    RelativeLayout ail,albuml;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.gallery,container,false);
        getActivity().setTitle("Gallery");
        gridView = (GridView)vi.findViewById(R.id.gridview);
        galleries = new ArrayList<>();
        allpics = (Button)vi.findViewById(R.id.allpics);
        albums = (Button)vi.findViewById(R.id.albums);
        ail = (RelativeLayout)vi.findViewById(R.id.ail);
        albuml = (RelativeLayout)vi.findViewById(R.id.albuml);
        allpics.setBackgroundResource(R.drawable.picsred);
        albums.setBackgroundResource(R.drawable.albumgreyun);
        ail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allpicsshow();
                allpics.setBackgroundResource(R.drawable.picsred);
                albums.setBackgroundResource(R.drawable.albumgreyun);
            }
        });
        albuml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumshow();
                allpics.setBackgroundResource(R.drawable.picgrey);
                albums.setBackgroundResource(R.drawable.albumredun);
            }
        });
        allpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allpicsshow();
                allpics.setBackgroundResource(R.drawable.picsred);
                albums.setBackgroundResource(R.drawable.albumgreyun);
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumshow();
                allpics.setBackgroundResource(R.drawable.picgrey);
                albums.setBackgroundResource(R.drawable.albumredun);
            }
        });
        allpicsshow();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectintent == 0){
                    Intent intent = new Intent(getActivity(), StoryMediaPreview.class);
                    intent.putExtra("medialink",galleries.get(i).getUrl());
                    intent.putExtra("mediatype",galleries.get(i).getMediatype());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.still);
                }
                else if(selectintent == 1){
                    Intent intent = new Intent(getActivity(), AlbumActivity.class);
                    intent.putExtra("event",galleries.get(i).getEvent());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
                }
            }
        });
        return vi;
    }

    private void albumshow() {
        selectintent = 1;
        galleries.clear();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    final String weddingid = snapshot.getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("events").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for(DocumentSnapshot doc:task.getResult()){
                                        if(doc.exists()){
                                            db.collection("weddings").document(weddingid).collection("gallery").whereEqualTo("event",doc.getId()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for(DocumentSnapshot doc:task.getResult()){
                                                        Gallery gallery = new Gallery(doc.getString("medialink"),doc.getString("mediatype"),doc.getString("event"));
                                                        galleries.add(gallery);
                                                    }
                                                    if(getActivity()!=null){
                                                        if(!galleries.isEmpty()){
                                                            galleryAlbumAdapter = new GalleryAlbumAdapter(getActivity(),R.layout.galleryalbumlayout, (ArrayList<Gallery>) galleries);
                                                            gridView.setAdapter(galleryAlbumAdapter);
                                                        }
                                                    }


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

            }
        });

    }

    private void allpicsshow() {
        selectintent = 0;
        galleries.clear();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    final String weddingid = snapshot.getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("gallery").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc:task.getResult()){
                                if(doc.exists()){
                                    Gallery gallery = new Gallery(doc.getString("medialink"),doc.getString("mediatype"),doc.getString("event"));
                                    galleries.add(gallery);
                                }

                            }
                            if(getActivity()!=null){
                                if(!galleries.isEmpty()){
                                    galleryAdapter = new GalleryAdapter(getActivity(),R.layout.gallerylayout, (ArrayList<Gallery>) galleries);
                                    gridView.setAdapter(galleryAdapter);
                                }
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

            }
        });

    }
}
