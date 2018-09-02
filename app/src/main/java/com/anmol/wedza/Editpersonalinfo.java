package com.anmol.wedza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editpersonalinfo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String profilePicturePath;
    EditText name;
    CircleImageView uppic;
    RadioButton single,marrried;
    String status = "single";
    String uname;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressBar prgbr;
    Button done;
    Uri fileuri = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int PICK_REQUEST_CODE = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpersonalinfo2);
        setTitle("Personal Settings");
        done =  (Button)findViewById(R.id.done);
        name = (EditText)findViewById(R.id.name);
        uppic = (CircleImageView) findViewById(R.id.userimage);
        single = (RadioButton)findViewById(R.id.single);
        marrried = (RadioButton)findViewById(R.id.married);
        prgbr = (ProgressBar)findViewById(R.id.prgbr);
        prgbr.setVisibility(View.GONE);
        single.setChecked(true);
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
        uppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest();
            }
        });
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    final String weddingid = snapshot.getString("currentwedding");
                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot1 = task.getResult();
                            if(snapshot1.exists()){
                                uname = snapshot1.getString("username");
                                name.setText(uname);
                                String mstatus = snapshot1.getString("status");
                                if(mstatus.contains("single")){
                                    status = "single";
                                    single.setChecked(true);
                                    marrried.setChecked(false);
                                }
                                else if(mstatus.contains("married")){
                                    status = "married";
                                    single.setChecked(false);
                                    marrried.setChecked(true);
                                }
                                profilePicturePath = snapshot1.getString("profilepicturepath");
                                Glide.with(Editpersonalinfo.this).load(profilePicturePath).into(uppic);
                                fun(weddingid);
                            }

                        }
                    });
                }


            }
        });
    }

    private void fun(final String weddingid) {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prgbr.setVisibility(View.VISIBLE);
                final String mname = name.getText().toString();
                final String mstatus = status;
                if(fileuri!=null){

                    StorageReference reference = storageReference.child(fileuri.getLastPathSegment());
                    reference.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String mprofilepicturepath = String.valueOf(taskSnapshot.getDownloadUrl());
                            Map<String,Object> map = new HashMap<>();
                            map.put("profilepicturepath",mprofilepicturepath);
                            map.put("username",mname);
                            map.put("status",mstatus);
                            db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Editpersonalinfo.this,"Updated successfully",Toast.LENGTH_SHORT).show();
                                    prgbr.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    prgbr.setVisibility(View.GONE);
                                    showToast("Network Error");
                                }
                            });
                        }
                    });
                }
                else{
                    Map<String,Object> map = new HashMap<>();
                    map.put("username",mname);
                    map.put("status",mstatus);
                    db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Editpersonalinfo.this,"Updated successfully",Toast.LENGTH_SHORT).show();
                            prgbr.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            prgbr.setVisibility(View.GONE);
                            showToast("Network Error");
                        }
                    });
                }
            }
        });
    }

    private void showToast(String toast){
        Toast.makeText(this,toast, Toast.LENGTH_SHORT).show();
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
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.RECORD_AUDIO)
                        ==PackageManager.PERMISSION_GRANTED)
            imageChooser();

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.RECORD_AUDIO)
                        !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO},
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
                    grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[3] !=PackageManager.PERMISSION_GRANTED
                    ) {
                Toast.makeText(this, "Permissions not given!!", Toast.LENGTH_SHORT).show();
            } else
                imageChooser();

        }




    }

    private void imageChooser() {
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
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                    File temp = File.createTempFile("profile", "pic");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    fileOutputStream.write(bytesBitmap);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileuri = Uri.fromFile(temp);
                    uppic.setImageURI(fileuri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
}
