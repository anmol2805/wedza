package com.anmol.wedza.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.Adapters.StoryimageAdapter;
import com.anmol.wedza.Interfaces.ItemClickListener;
import com.anmol.wedza.Model.Storyimage;
import com.anmol.wedza.R;
import com.anmol.wedza.StoryMediaPreview;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.story,container,false);
        liked = (Button)vi.findViewById(R.id.likedit);
        wishes = (Button)vi.findViewById(R.id.wishes);
        storycontent = (TextView)vi.findViewById(R.id.storycontent);
        listimg = (RecyclerView)vi.findViewById(R.id.listimg);
        listimg.setHasFixedSize(true);
        listimg.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        storyimages = new ArrayList<>();
        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getActivity(), StoryMediaPreview.class);
                intent.putExtra("medialink",storyimages.get(pos).getMedialink());
                intent.putExtra("mediatype",storyimages.get(pos).getMediatype());
                getActivity().startActivity(intent);
            }
        };
        loadmedia();
        loadcontent();
        db.collection("users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot!=null && documentSnapshot.exists()){
                    String weddingid = documentSnapshot.getString("currentwedding");
                    db.collection("weddings")
                            .document(weddingid)
                            .collection("storylikes")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                if(doc.getId().contains(auth.getCurrentUser().getUid())){
                                    liked.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                }
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
            }
        });
        return vi;
    }

    private void loadcontent() {
        db.collection("weddings").document("wedding1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                storycontent.setText(task.getResult().getString("storycontent"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void loadmedia() {
        storyimages.clear();
        db.collection("weddings/wedding1/stories").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    Storyimage storyimage = new Storyimage(doc.getString("medialink"),doc.getString("mediatype"));
                    storyimages.add(storyimage);
                }
                storyimageAdapter = new StoryimageAdapter(getActivity(),storyimages,itemClickListener);
                listimg.setAdapter(storyimageAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
