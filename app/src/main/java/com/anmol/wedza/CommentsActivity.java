package com.anmol.wedza;

import android.content.SyncRequest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.anmol.wedza.Adapters.CommentsAdapter;
import com.anmol.wedza.Model.Comment;
import com.anmol.wedza.Model.Comment2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    ListView commentslist;
    Button post;
    EditText comment;
    List<Comment> comments;
    List<Comment2> comment2s;
    CommentsAdapter commentsAdapter;
    String postid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        comment = (EditText)findViewById(R.id.comment);
        post = (Button)findViewById(R.id.post);
        commentslist = (ListView)findViewById(R.id.commentlist);
        comments = new ArrayList<>();
        comment2s = new ArrayList<>();
        postid = getIntent().getStringExtra("postid");
        loadcomments(postid);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postcomment(postid);
            }
        });
    }

    private void loadcomments(final String postid) {
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String weddingid = task.getResult().getString("currentwedding");
                db.collection("weddings").document(weddingid).collection("timeline").document(postid)
                        .collection("comments").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        comment2s.clear();
                        if(documentSnapshots!=null && !documentSnapshots.isEmpty()){
                            for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                String username = doc.getString("commentedby");
                                String uid = doc.getString("uid");
                                String profilepicturepath = doc.getString("profilepic");
                                String commenttext = doc.getString("comment");
                                Comment2 comment2 = new Comment2(commenttext,username,profilepicturepath,uid);
                                comment2s.add(comment2);
                            }
                            commentsAdapter = new CommentsAdapter(CommentsActivity.this,R.layout.commentlayout,comment2s);
                            commentslist.setAdapter(commentsAdapter);
                            commentslist.setSelection(commentslist.getAdapter().getCount() - 1);
                        }

                    }
                });
            }
        });
    }

    private void postcomment(final String postid) {
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
                        String commenttext = comment.getText().toString().trim();
                        String uid = auth.getCurrentUser().getUid();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(new Date());
                        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);
                        Comment mcomment = new Comment(commenttext,username,profilepicturepath,uid,timestamp);
                        DocumentReference documentReference = db.collection("weddings").document(weddingid).collection("timeline").document(postid)
                                .collection("comments").document();
                        String id = documentReference.getId();
                        db.collection("weddings").document(weddingid).collection("timeline").document(postid)
                                .collection("comments").document(id).set(mcomment);
                        comment.getText().clear();

                    }
                });
            }
        });
    }

}
