package com.anmol.wedza;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Button join,login;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText wedid;
    ProgressBar prgbr;
    int checker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join = (Button)findViewById(R.id.joinwed);
        login = (Button)findViewById(R.id.login);
        wedid = (EditText)findViewById(R.id.wedid);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.GONE);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgbr.setVisibility(View.VISIBLE);
                final String weddingid = wedid.getText().toString().trim();
                db.collection("weddings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        checker = 0;
                        for(DocumentSnapshot doc:task.getResult()){
                            if(doc.getId().contains(weddingid)){
                                if (auth.getCurrentUser()!=null){
                                    checker = 1;
                                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                    intent.putExtra("weddingid",weddingid);
                                    prgbr.setVisibility(View.GONE);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                                    finish();
                                }
                                else {
                                    checker = 1;
                                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                    intent.putExtra("weddingid",weddingid);
                                    prgbr.setVisibility(View.GONE);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                                    finish();
                                }
                            }
                        }
                        if(checker!=1){
                            checker = 0;
                            prgbr.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,"Wedding does not exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Network Error!!!",Toast.LENGTH_SHORT).show();
                        prgbr.setVisibility(View.GONE);
                    }
                });
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
