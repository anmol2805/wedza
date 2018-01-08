package com.anmol.wedza;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.anmol.wedza.Adapters.KeypeopleAdapter;
import com.anmol.wedza.Model.Keypeople;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KeypeopleActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView keypeoplelist;
    List<Keypeople> keypeoples;
    KeypeopleAdapter keypeopleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypeople);
        keypeoplelist = (ListView)findViewById(R.id.keypeoplelist);
        keypeoples = new ArrayList<>();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String weddingid = task.getResult().getString("currentwedding");
                getteam(weddingid);
            }
        });
    }

    private void getteam(final String weddingid) {
        db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     String team = task.getResult().getString("team");
                     getlist(weddingid,team);
            }
        });
    }

    private void getlist(String weddingid, String team) {
        db.collection("weddings").document(weddingid).collection("users").whereEqualTo("team",team).whereEqualTo("keypeople",true)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    keypeoples.clear();
                    for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                        String name = doc.getString("username");
                        String work = doc.getString("userwork");
                        String image = doc.getString("profilepicturepath");
                        String contact = doc.getString("contactnumber");
                        Keypeople keypeople = new Keypeople(name,image,contact,work);
                        keypeoples.add(keypeople);
                    }
                    keypeopleAdapter = new KeypeopleAdapter(KeypeopleActivity.this,R.layout.keypeoplelayout,keypeoples);
                    keypeoplelist.setAdapter(keypeopleAdapter);
                }
            });
    }
}
