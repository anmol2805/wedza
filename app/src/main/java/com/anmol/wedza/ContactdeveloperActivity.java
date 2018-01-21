package com.anmol.wedza;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ContactdeveloperActivity extends AppCompatActivity {
    EditText feedback;
    Button done,gmail;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar prgbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactdeveloper);
        gmail = (Button)findViewById(R.id.gmail);
        feedback = (EditText)findViewById(R.id.feedback);
        done = (Button)findViewById(R.id.done);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.INVISIBLE);
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("mailto:"+"canopydevelopers@gmail.com"));
                startActivity(viewIntent);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feedback.getText()!=null && !feedback.getText().toString().isEmpty()){
                    prgbr.setVisibility(View.VISIBLE);
                    Map<String,Object> map = new HashMap<>();
                    map.put("feedback",feedback.getText().toString());
                    map.put("uid",auth.getCurrentUser().getUid());
                    DocumentReference ref = db.collection("feedback").document();
                    String id = ref.getId();
                    db.collection("feedback").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ContactdeveloperActivity.this,"Feedback submitted!!!",Toast.LENGTH_SHORT).show();
                            prgbr.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ContactdeveloperActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                            prgbr.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }
}
