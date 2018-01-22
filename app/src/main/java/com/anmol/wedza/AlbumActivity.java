package com.anmol.wedza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.anmol.wedza.Adapters.GalleryAdapter;
import com.anmol.wedza.Adapters.GalleryAlbumAdapter;
import com.anmol.wedza.Model.Gallery;
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

public class AlbumActivity extends AppCompatActivity {
    GridView albumgridview;
    List<Gallery> galleries;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GalleryAdapter galleryAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        albumgridview = (GridView)findViewById(R.id.albumgridview);
        String event = getIntent().getStringExtra("event");
        setTitle(event);
        galleries = new ArrayList<>();
        show(event);
        albumgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumActivity.this, StoryMediaPreview.class);
                intent.putExtra("medialink",galleries.get(i).getUrl());
                intent.putExtra("mediatype",galleries.get(i).getMediatype());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in,R.anim.still);
            }
        });
    }

    private void show(final String event) {
        galleries.clear();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String weddingid = snapshot.getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("gallery").whereEqualTo("event",event).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc:task.getResult()){
                                if(doc.exists()){
                                    Gallery gallery = new Gallery(doc.getString("medialink"),doc.getString("mediatype"),doc.getString("event"));
                                    galleries.add(gallery);
                                }

                            }
                            if(!galleries.isEmpty()){
                                galleryAdapter = new GalleryAdapter(AlbumActivity.this,R.layout.gallerylayout, (ArrayList<Gallery>) galleries);
                                albumgridview.setAdapter(galleryAdapter);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}
