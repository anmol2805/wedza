package com.anmol.wedza;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.anmol.wedza.Adapters.EventsAdapter;
import com.anmol.wedza.Model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Event> events = new ArrayList<>();
    EventsAdapter eventsAdapter;
    ListView eventlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        eventlist = (ListView)findViewById(R.id.eventlist);
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String weddingid = task.getResult().getString("currentwedding");
                getevent(weddingid);
            }
        });
    }

    private void getevent(String weddingid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);
        db.collection("weddings").document(weddingid)
                .collection("events")
                .orderBy("time")
                .whereGreaterThanOrEqualTo("time",timestamp).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                events.clear();
                for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                    String eventname = doc.getString("eventname");
                    String eventdes = doc.getString("eventdes");
                    String eventimg = doc.getString("eventimg");
                    String eventlocation = doc.getString("eventlocation");
                    String team = doc.getString("team");
                    Timestamp time = (Timestamp) doc.getDate("time");
                    Event event = new Event(eventname,eventdes,eventimg,eventlocation,team,time);
                    events.add(event);
                }
                eventsAdapter = new EventsAdapter(EventsActivity.this,R.layout.eventlayout,events);
                eventlist.setAdapter(eventsAdapter);

            }
        });
    }
}
