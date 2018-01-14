package com.anmol.wedza;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoryeditActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button upload,save;
    ImageButton browse;
    EditText storytext;
    ImageView play;
    Uri fileuri = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int PICK_REQUEST_CODE = 300;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyedit);
        upload = (Button)findViewById(R.id.upload);
        save = (Button)findViewById(R.id.savestory);
        browse = (ImageButton) findViewById(R.id.browse);
        storytext = (EditText)findViewById(R.id.storytext);
        play = (ImageView)findViewById(R.id.play);
        upload.setVisibility(View.GONE);
        String st = getIntent().getStringExtra("storycontent");
        storytext.setText(st);
        if(fileuri!=null){
            upload.setVisibility(View.VISIBLE);
        }
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference reference = storageReference.child(fileuri.getLastPathSegment());
                reference.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                        final String mediatype = taskSnapshot.getMetadata().getContentType();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(new Date());
                        final Timestamp timestamp = Timestamp.valueOf(date);
                        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String weddindid = task.getResult().getString("currentwedding");
                                Map<String,Object> map = new HashMap<>();
                                map.put("time",timestamp);
                                map.put("medialink",medialink);
                                map.put("mediatype",mediatype);
                                DocumentReference ref = db.collection("weddings").document(weddindid).collection("stories").document();
                                String id = ref.getId();
                                db.collection("weddings").document(weddindid).collection("stories").document(id).set(map);
                            }
                        });
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String storycon = storytext.getText().toString().trim();
                db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String weddindid = task.getResult().getString("currentwedding");
                        Map<String,Object> map = new HashMap<>();
                        map.put("storycontent",storycon);
                        db.collection("weddings").document(weddindid).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }
    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            pickmedia();

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions not given!!", Toast.LENGTH_SHORT).show();
            } else
                pickmedia();

        }




    }
    private void pickmedia() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("*/*");
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
                upload.setVisibility(View.VISIBLE);
                ContentResolver cr = getContentResolver();
                String type = cr.getType(uri);
                if(type.contains("image")){
                    play.setVisibility(View.GONE);
                }else if(type.contains("video")){
                    play.setVisibility(View.VISIBLE);
                }
                Glide.with(StoryeditActivity.this).load(uri).into(browse);

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
