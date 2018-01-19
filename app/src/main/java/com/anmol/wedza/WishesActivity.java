package com.anmol.wedza;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.anmol.wedza.Adapters.CommentsAdapter;
import com.anmol.wedza.Adapters.WishesAdapter;
import com.anmol.wedza.Model.Comment;
import com.anmol.wedza.Model.Comment2;
import com.anmol.wedza.Model.Wish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WishesActivity extends AppCompatActivity {

    ListView wisheslist;
    FloatingActionButton wish;
    EditText wishtext;
    List<Wish> wishes;
    WishesAdapter wishesAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishes);
        setTitle("Wishes");
        wishtext = (EditText)findViewById(R.id.wishtext);
        wish = (FloatingActionButton) findViewById(R.id.wish);
        wisheslist = (ListView)findViewById(R.id.wishlist);
        wishes = new ArrayList<>();
        loadcomments();
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postcomment();
            }
        });
    }

    private void loadcomments() {
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String weddingid = task.getResult().getString("currentwedding");
                db.collection("weddings").document(weddingid).collection("storycomments").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        wishes.clear();
                        if(documentSnapshots!=null&&!documentSnapshots.isEmpty()){
                            for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                String username = doc.getString("commentedby");
                                String uid = doc.getString("uid");
                                String profilepicturepath = doc.getString("profilepic");
                                String wishtext = doc.getString("comment");
                                Wish wish = new Wish(wishtext,username,profilepicturepath,uid);
                                wishes.add(wish);
                            }
                            if(!wishes.isEmpty()){
                                wishesAdapter = new WishesAdapter(WishesActivity.this,R.layout.commentlayout,wishes);
                                wisheslist.setAdapter(wishesAdapter);
                                wisheslist.setSelection(wisheslist.getAdapter().getCount() - 1);
                            }

                        }

                    }
                });
            }
        });
    }

    private void postcomment() {
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String weddingid = task.getResult().getString("currentwedding");
                db.collection("weddings").document(weddingid).collection("users")
                        .document(auth.getCurrentUser().getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String profilepicturepath = task.getResult().getString("profilepicturepath");
                        String username = task.getResult().getString("username");
                        String commenttext = wishtext.getText().toString().trim();
                        String uid = auth.getCurrentUser().getUid();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(new Date());
                        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);
                        Comment mcomment = new Comment(commenttext,username,profilepicturepath,uid,timestamp);
                        DocumentReference documentReference = db.collection("weddings").document(weddingid).collection("storycomments").document();
                        String id = documentReference.getId();
                        db.collection("weddings").document(weddingid).collection("storycomments").document(id).set(mcomment);
                        wishtext.getText().clear();

                    }
                });
            }
        });
    }
}
