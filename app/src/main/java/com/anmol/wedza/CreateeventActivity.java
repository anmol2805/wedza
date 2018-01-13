package com.anmol.wedza;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateeventActivity extends AppCompatActivity {
    EditText eventnameedit,eventlocedit,dateedit,timeedit,eventdesedit;
    RadioButton groom,bride,both;
    ImageButton eventimgedit;
    Button createevent;
    private static final int PICK_REQUEST_CODE = 300;
    Uri fileuri = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String team;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createevent);
        eventnameedit = (EditText)findViewById(R.id.eventnameedit);
        eventlocedit = (EditText)findViewById(R.id.eventlocationedit);
        dateedit = (EditText)findViewById(R.id.dateedit);
        timeedit = (EditText)findViewById(R.id.timeedit);
        eventdesedit = (EditText)findViewById(R.id.eventdesedit);
        groom = (RadioButton)findViewById(R.id.groom);
        bride = (RadioButton)findViewById(R.id.bride);
        both = (RadioButton)findViewById(R.id.both);
        eventimgedit = (ImageButton)findViewById(R.id.eventimgedit);
        createevent = (Button)findViewById(R.id.createevent);
        eventimgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickmedia();
            }
        });
        both.setChecked(true);
        groom.setChecked(false);
        bride.setChecked(false);
        team = "both";
        groom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bride.setChecked(false);
                both.setChecked(false);
                team = "groom";
            }
        });
        bride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groom.setChecked(false);
                both.setChecked(false);
                team = "bride";
            }
        });
        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groom.setChecked(false);
                bride.setChecked(false);
                team = "both";
            }
        });
        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String eventname = eventnameedit.getText().toString().trim();
                String eventloc = eventlocedit.getText().toString().trim();
                String date = dateedit.getText().toString().trim();
                String time = timeedit.getText().toString().trim();
                String des = eventdesedit.getText().toString().trim();
                StorageReference reference = storageReference.child(fileuri.getLastPathSegment());
                reference.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String weddindid = task.getResult().getString("currentwedding");
                                Map<String,Object> map = new HashMap<>();

                                db.collection("weddings").document(weddindid).collection("events").document(eventname).set(map);
                            }
                        });
                    }
                });
            }
        });
    }
    private void pickmedia() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.

        startActivityForResult(chooserIntent, PICK_REQUEST_CODE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        if(requestCode==PICK_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                fileuri = uri;
                eventimgedit.setImageURI(uri);

            }else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled the task", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to pick file", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
