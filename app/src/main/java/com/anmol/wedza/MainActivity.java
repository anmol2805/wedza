package com.anmol.wedza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class MainActivity extends AppCompatActivity {
    Button join,login;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText wedid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join = (Button)findViewById(R.id.joinwed);
        login = (Button)findViewById(R.id.login);
        wedid = (EditText)findViewById(R.id.wedid);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String weddingid = wedid.getText().toString().trim();
                db.collection("weddings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc:task.getResult()){
                            if(doc.getId().contains(weddingid)){
                                if (auth.getCurrentUser()!=null){
                                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                    intent.putExtra("weddingid",weddingid);
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                    intent.putExtra("weddingid",weddingid);
                                    startActivity(intent);
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Wedding does not exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }
}
