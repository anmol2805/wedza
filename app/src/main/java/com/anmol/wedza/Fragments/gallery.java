package com.anmol.wedza.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.anmol.wedza.Adapters.GalleryAdapter;
import com.anmol.wedza.Model.Gallery;
import com.anmol.wedza.R;
import com.facebook.FacebookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.gallery,container,false);
        gridView = (GridView)vi.findViewById(R.id.gridview);
        galleries = new ArrayList<>();
        allpics = (Button)vi.findViewById(R.id.allpics);
        albums = (Button)vi.findViewById(R.id.albums);
        allpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allpicsshow();
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumshow();
            }
        });
        return vi;
    }

    private void albumshow() {
        galleries.clear();
        db.collection("weddings/wedding1/events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    db.collection("weddings/wedding1/gallery").whereEqualTo("event",doc.getId()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc:task.getResult()){
                                Gallery gallery = new Gallery(doc.getString("medialink"));
                                galleries.add(gallery);
                            }
                            galleryAdapter = new GalleryAdapter(getActivity(),R.layout.gallerylayout, (ArrayList<Gallery>) galleries);
                            gridView.setAdapter(galleryAdapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void allpicsshow() {
        galleries.clear();
        db.collection("weddings/wedding1/gallery").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    Gallery gallery = new Gallery(doc.getString("medialink"));
                    galleries.add(gallery);
                }
                galleryAdapter = new GalleryAdapter(getActivity(),R.layout.gallerylayout, (ArrayList<Gallery>) galleries);
                gridView.setAdapter(galleryAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
