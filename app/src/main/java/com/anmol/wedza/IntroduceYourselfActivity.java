package com.anmol.wedza;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.anmol.wedza.Model.Yourinfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IntroduceYourselfActivity extends AppCompatActivity {

    Spinner spinner;
    Button done;
    ArrayAdapter<CharSequence> arrayAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = FirebaseFirestore.getInstance().document("weddings/mrxwedsmsy/users/user1");
    String profilePicturePath;
    String username;
    String weddingid;
    EditText name;
    ImageButton uppic;
    Button tbr,tgr,single,marrried;
    String team = "groom";
    String status = "single";
    String uname;
    String fbpagelink = null;
    EditText fbpglnk;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_yourself);
        spinner = (Spinner)findViewById(R.id.relation);
        done =  (Button)findViewById(R.id.done);
        name = (EditText)findViewById(R.id.name);
        uppic = (ImageButton)findViewById(R.id.userimage);
        tbr = (Button)findViewById(R.id.tbr);
        tgr = (Button)findViewById(R.id.tgr);
        single = (Button)findViewById(R.id.single);
        marrried = (Button)findViewById(R.id.married);
        fbpglnk = (EditText)findViewById(R.id.fbpagelink);
        Intent intent = getIntent();
        profilePicturePath = intent.getStringExtra("profilePicturePath");
        username = intent.getStringExtra("username");
        weddingid = intent.getStringExtra("weddingid");
        name.setText(username);
        Glide.with(this).load(profilePicturePath).into(uppic);
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
        tbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team = "bride";
                showToast(team);
            }
        });
        tgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team = "groom";
                showToast(team);
            }
        });
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "single";
                showToast(status);
            }
        });
        marrried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "married";
                showToast(status);
            }
        });

    }

    private void fun(final String relation) {
        done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uname = name.getText().toString().trim();
                        fbpagelink = fbpglnk.getText().toString().trim();
                        if(!relation.contains("Select Relation type")){
                            Yourinfo yourinfo = new Yourinfo(uname,fbpagelink,relation,team,status,profilePicturePath,weddingid);
                            db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).set(yourinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("weddingid",weddingid);
                                    DocumentReference ref = db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document();
                                    String id = ref.getId();
                                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document(id)
                                            .set(map);
                                    Intent intent = new Intent(IntroduceYourselfActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

    }
    private void showToast(String toast){
        Toast.makeText(this,toast, Toast.LENGTH_SHORT).show();
    }
}
