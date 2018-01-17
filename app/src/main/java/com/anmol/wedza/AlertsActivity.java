package com.anmol.wedza;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.anmol.wedza.Adapters.AlertsAdapter;
import com.anmol.wedza.Adapters.CommentsAdapter;
import com.anmol.wedza.Model.Alert;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertsActivity extends AppCompatActivity {

    ListView alertslist;
    Button post;
    EditText comment;
    List<Alert> alerts;
    AlertsAdapter alertsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FloatingActionButton authpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        comment = (EditText)findViewById(R.id.comment);
        post = (Button)findViewById(R.id.post);
        alertslist = (ListView)findViewById(R.id.alertlist);
        authpost = (FloatingActionButton)findViewById(R.id.postauth);
        authpost.setVisibility(View.GONE);
        alerts = new ArrayList<>();
        post.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);
        loadcomments();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String weddingid = task.getResult().getString("currentwedding");
                db.collection("weddings").document(weddingid).collection("users")
                        .document(auth.getCurrentUser().getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Boolean admin = task.getResult().getBoolean("admin");
                        Boolean keypeople = task.getResult().getBoolean("keypeople");
                        if(admin.equals(true) || keypeople.equals(true)){
                            authpost.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        authpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment.setVisibility(View.VISIBLE);
                post.setVisibility(View.VISIBLE);
                authpost.setVisibility(View.GONE);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
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
                db.collection("weddings").document(weddingid).collection("alerts").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        alerts.clear();
                        if(documentSnapshots!=null && !documentSnapshots.isEmpty()){
                            for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                                String username = doc.getString("postedby");
                                String uid = doc.getString("uid");
                                String alerttext = doc.getString("alerttext");
                                String team = doc.getString("team");
                                Alert alert = new Alert(username,team,alerttext,uid);
                                alerts.add(alert);
                            }
                            if(!alerts.isEmpty()){
                                alertsAdapter = new AlertsAdapter(AlertsActivity.this,R.layout.alertlayout,alerts);
                                alertslist.setAdapter(alertsAdapter);
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
                        String team = task.getResult().getString("team");
                        String username = task.getResult().getString("username");
                        String commenttext = comment.getText().toString().trim();
                        String uid = auth.getCurrentUser().getUid();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(new Date());
                        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);
                        Map<String,Object> map = new HashMap<>();
                        map.put("postedby",username);
                        map.put("uid",uid);
                        map.put("alerttext",commenttext);
                        map.put("team",team);
                        map.put("time",timestamp);
                        DocumentReference documentReference = db.collection("weddings").document(weddingid).collection("alerts").document();
                        String id = documentReference.getId();
                        db.collection("weddings").document(weddingid).collection("alerts").document(id).set(map);
                        comment.getText().clear();
                        post.setVisibility(View.GONE);
                        comment.setVisibility(View.GONE);
                        authpost.setVisibility(View.VISIBLE);

                    }
                });
            }
        });
    }
}
