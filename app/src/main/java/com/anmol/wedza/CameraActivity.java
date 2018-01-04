package com.anmol.wedza;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class CameraActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 123;
    int s=0,c=0;
    ArrayList<String> filepaths;
    Button allow,posttime,saveg;
    Spinner eventselect;
    ImageView img;
    LinearLayout imagelayout;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imagelayout = (LinearLayout)findViewById(R.id.imglayout);
        img = (ImageView)findViewById(R.id.selectedimg);
        eventselect = (Spinner)findViewById(R.id.eventselect);
        allow = (Button)findViewById(R.id.allow);
        posttime = (Button)findViewById(R.id.ptimeline);
        saveg = (Button)findViewById(R.id.saveg);
        filepaths = new ArrayList<>();
        imagelayout.setVisibility(View.GONE);
        allow.setVisibility(View.VISIBLE);
        db.collection("weddings/wedding1/events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    String eventname = doc.getId();
                    events.add(eventname);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CameraActivity.this,android.R.layout.simple_spinner_item,events);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventselect.setAdapter(adapter);
            }
        });
        permissionRequest();

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
            imageChooser();

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

    private void imageChooser() {
        imagelayout.setVisibility(View.VISIBLE);
        allow.setVisibility(View.GONE);
        filepaths.clear();
        FilePickerBuilder.getInstance().setMaxCount(1).setSelectedFiles(filepaths).setActivityTheme(R.style.AppTheme).pickPhoto(CameraActivity.this);
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
                imageChooser();

        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO && data!=null){
                for(String path : filepaths){
                    final Uri uri = Uri.fromFile(new File(path));
                    img.setImageURI(uri);
                    eventselectfun(uri);
//                    eventselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
//                            final String event = (String) adapterView.getItemAtPosition(i);
//                            posttime.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    posttotimeline(uri,event);
//                                }
//                            });
//                            saveg.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    savetogallery(uri,event);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
                }
            }
        }
    }

    private void eventselectfun(final Uri uri) {
        eventselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String event = (String) adapterView.getItemAtPosition(i);
                es(uri,event);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void es(final Uri uri, final String event) {
        posttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posttotimeline(uri,event);
            }
        });
        saveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savetogallery(uri,event);
            }
        });
    }

    private void savetogallery(Uri uri, final String event) {
        StorageReference reference = storageReference.child("photos").child(uri.getLastPathSegment());
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CameraActivity.this,"Saved successfully",Toast.LENGTH_SHORT).show();
                String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                String mediatype = taskSnapshot.getMetadata().getContentType();
                DocumentReference ref = db.collection("weddings/wedding1/gallery").document();
                String imageid = ref.getId();
                Map<String,Object> map = new HashMap<>();
                map.put("medialink",medialink);
                map.put("mediatype",mediatype);
                map.put("event",event);
                db.collection("weddings/wedding1/gallery").document(imageid).set(map);

            }
        });
    }

    private void posttotimeline(Uri uri, String event) {
        StorageReference reference = storageReference.child("photos").child(uri.getLastPathSegment());
    }
}
