package com.anmol.wedza;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.anmol.wedza.Model.Yourinfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class IntroduceYourselfActivity extends AppCompatActivity {

    Spinner spinner;
    Button done;
    ArrayAdapter<CharSequence> arrayAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String profilePicturePath;
    String username;
    String weddingid;
    EditText name;
    CircleImageView uppic;
    RadioButton tbr,tgr,single,marrried;
    String team = "groom";
    String status = "single";
    String uname;
    String fbpagelink = null;
    EditText fbpglnk;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressBar prgbr;
    String authcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_yourself);
        spinner = (Spinner)findViewById(R.id.relation);
        done =  (Button)findViewById(R.id.done);
        name = (EditText)findViewById(R.id.name);
        uppic = (CircleImageView) findViewById(R.id.userimage);
        tbr = (RadioButton)findViewById(R.id.tbr);
        tgr = (RadioButton)findViewById(R.id.tgr);
        single = (RadioButton)findViewById(R.id.single);
        marrried = (RadioButton)findViewById(R.id.married);
        fbpglnk = (EditText)findViewById(R.id.fbpagelink);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.GONE);
        Intent intent = getIntent();
        // receiving all intents
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
        tgr.setChecked(true);
        single.setChecked(true);
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
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "single";
                single.setChecked(true);
                marrried.setChecked(false);
                showToast(status);
            }
        });
        marrried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "married";
                single.setChecked(false);
                marrried.setChecked(true);
                showToast(status);
            }
        });

    }

    private void fun(final String relation) {
        // subitting  form
        done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prgbr.setVisibility(View.VISIBLE);
                        uname = name.getText().toString().trim();
                        fbpagelink = fbpglnk.getText().toString().trim();
                        Boolean admin = false;
                        Boolean keypeople = false;
                        if(!relation.contains("Select Relation type")){
                            Yourinfo yourinfo = new Yourinfo(uname,fbpagelink,relation,team,status,profilePicturePath,weddingid,admin,keypeople);

                            //edit query

                            // updatig user to weddings--users
                            db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).set(yourinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    DocumentReference ref1 = db.collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document();
//                                    String id1 = ref1.getId();
                                    Map<String,Object> cmap = new HashMap<>();
                                    cmap.put("weddingid",weddingid);
                                    // updating users to users
                                    db.collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document(weddingid).set(cmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String,Object> map = new HashMap<>();
                                            map.put("currentwedding",weddingid);
                                            db.collection("users").document(auth.getCurrentUser().getUid()).set(map);
                                        }
                                    });
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("weddingid",weddingid);
                                    DocumentReference ref = db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document();
                                    String id = ref.getId();
                                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).collection("weddings").document(id)
                                            .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            prgbr.setVisibility(View.GONE);
                                            Intent intent = new Intent(IntroduceYourselfActivity.this,HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(IntroduceYourselfActivity.this,"network error",Toast.LENGTH_SHORT).show();
                                            prgbr.setVisibility(View.GONE);
                                        }
                                    });

                                }
                            });

                        }
                        else {
                            prgbr.setVisibility(View.GONE);
                            showToast("Please select relation with the couple");
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
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}
