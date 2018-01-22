package com.anmol.wedza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.anmol.wedza.Model.Yourinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class Weddingsettings extends AppCompatActivity {
    Spinner spinner;
    Button done;
    ArrayAdapter<CharSequence> arrayAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RadioButton tbr,tgr;
    String team = "groom";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressBar prgbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weddingsettings);
        setTitle("Wedding Settings");
        spinner = (Spinner)findViewById(R.id.relation);
        done =  (Button)findViewById(R.id.done);
        tbr = (RadioButton)findViewById(R.id.tbr);
        tgr = (RadioButton)findViewById(R.id.tgr);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.GONE);
        arrayAdapter =  ArrayAdapter.createFromResource(this,R.array.relationtype,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String relation = (String) adapterView.getItemAtPosition(i);
                fun(relation);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tbr.setChecked(true);
        tbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team = "bride";
                tbr.setChecked(true);
                tgr.setChecked(false);
                showToast(team);
            }
        });
        tgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team = "groom";
                tbr.setChecked(false);
                tgr.setChecked(true);
                showToast(team);
            }
        });
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String weddingid = snapshot.getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot1 = task.getResult();
                            if(snapshot1.exists()){
                                int pos = arrayAdapter.getPosition(snapshot1.getString("relation"));
                                spinner.setSelection(pos);
                                String mteam = snapshot1.getString("team");
                                if(mteam.contains("groom")){
                                    team = "groom";
                                    tbr.setChecked(false);
                                    tgr.setChecked(true);
                                }
                                else if(mteam.contains("bride")){
                                    team = "bride";
                                    tbr.setChecked(true);
                                    tgr.setChecked(false);
                                }
                            }

                        }
                    });
                }

            }
        });


    }
    private void fun(final String relation) {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prgbr.setVisibility(View.VISIBLE);
                if(!relation.contains("Select Relation type")){
                    db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot = task.getResult();
                            if(snapshot.exists()){
                                String weddingid = snapshot.getString("currentwedding");
                                Map<String,Object> map = new HashMap<>();
                                map.put("relation",relation);
                                map.put("team",team);
                                db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid())
                                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showToast("updated settings successfully");
                                        prgbr.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast("Network error...unable to save changes.");
                                        prgbr.setVisibility(View.GONE);
                                    }
                                });
                            }


                        }
                    });


                }
            }
        });

    }
    private void showToast(String toast){
        Toast.makeText(this,toast, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
