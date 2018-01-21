package com.anmol.wedza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateweddingActivity extends AppCompatActivity {
    EditText name,phone,email;
    Button done;
    ProgressBar prgbr;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createwedding);
        setTitle("Congratulations!!!");
        name = (EditText)findViewById(R.id.yname);
        phone = (EditText)findViewById(R.id.contactn);
        email = (EditText)findViewById(R.id.email);
        done = (Button)findViewById(R.id.done);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.GONE);
        result = (TextView)findViewById(R.id.result);
        result.setVisibility(View.INVISIBLE);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText()!=null
                        && !phone.getText().toString().isEmpty()
                        && email.getText()!=null
                        && !email.getText().toString().isEmpty()
                        && name.getText()!=null
                        && !name.getText().toString().isEmpty()){
                    prgbr.setVisibility(View.VISIBLE);
                    DocumentReference ref = db.collection("request").document();
                    String id = ref.getId();
                    Map<String,Object> map = new HashMap<>();
                    map.put("username",name.getText().toString());
                    map.put("contact",phone.getText().toString());
                    map.put("email",email.getText().toString());
                    map.put("uid",auth.getCurrentUser().getUid());
                    db.collection("request").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            prgbr.setVisibility(View.GONE);
                            result.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else{
                    Toast.makeText(CreateweddingActivity.this,"Please fill out all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
