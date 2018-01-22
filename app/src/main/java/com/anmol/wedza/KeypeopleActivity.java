package com.anmol.wedza;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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
    private static final int MY_PERMISSIONS_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypeople);
        setTitle("Key People");
        keypeoplelist = (ListView)findViewById(R.id.keypeoplelist);
        keypeoples = new ArrayList<>();
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String weddingid = snapshot.getString("currentwedding");
                    getteam(weddingid);
                }

            }
        });
        permissionRequest();

    }
    private void permissionRequest() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                    ) {
                Toast.makeText(this, "Permissions not given!!", Toast.LENGTH_SHORT).show();
            }

        }




    }


    private void getteam(final String weddingid) {
        db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String team = snapshot.getString("team");
                    getlist(weddingid,team);
                }

            }
        });
    }

    private void getlist(String weddingid, String team) {
        db.collection("weddings").document(weddingid).collection("users").whereEqualTo("team",team).whereEqualTo("keypeople",true)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    keypeoples.clear();
                    if(documentSnapshots!=null && !documentSnapshots.isEmpty()){
                        for(DocumentSnapshot doc:documentSnapshots.getDocuments()){
                            if(doc.exists()){
                                String name = doc.getString("username");
                                String work = doc.getString("userwork");
                                String image = doc.getString("profilepicturepath");
                                String contact = doc.getString("contactnumber");
                                Keypeople keypeople = new Keypeople(name,image,contact,work);
                                keypeoples.add(keypeople);
                            }

                        }
                        if(!keypeoples.isEmpty()){
                            keypeopleAdapter = new KeypeopleAdapter(KeypeopleActivity.this,R.layout.keypeoplelayout,keypeoples);
                            keypeoplelist.setAdapter(keypeopleAdapter);
                        }
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
