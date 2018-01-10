package com.anmol.wedza.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.anmol.wedza.CameraActivity;
import com.anmol.wedza.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by anmol on 1/10/2018.
 */

public class media extends Fragment {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    Button allow,posttime,saveg;
    Spinner eventselect;
    ImageView img;
    LinearLayout imagelayout,btnlayout;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> events = new ArrayList<>();
    EditText description;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Wedza";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.media,container,false);
        imgPreview = (ImageView)vi.findViewById(R.id.imgPreview);
        videoPreview = (VideoView)vi.findViewById(R.id.videoPreview);
        btnCapturePicture = (Button)vi.findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (Button)vi.findViewById(R.id.btnRecordVideo);
        imagelayout = (LinearLayout)vi.findViewById(R.id.imglayout);
        btnlayout = (LinearLayout)vi.findViewById(R.id.btnlayout); 
        eventselect = (Spinner)vi.findViewById(R.id.eventselect);
        allow = (Button)vi.findViewById(R.id.allow);
        posttime = (Button)vi.findViewById(R.id.ptimeline);
        saveg = (Button)vi.findViewById(R.id.saveg);
        imagelayout.setVisibility(View.GONE);
        btnlayout.setVisibility(View.GONE);
        allow.setVisibility(View.VISIBLE);
        description = (EditText)vi.findViewById(R.id.description);
        permissionRequest();
        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest();
            }
        });
        
        /**
         * Capture image button click event
         * */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String weddingid = task.getResult().getString("currentwedding");
                getteam(weddingid);
            }
        });

        return vi;
    }

    private void forward(final String weddingid, final String eventname) {
        posttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posttotimeline(weddingid,eventname);
            }
        });
        saveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savetogallery(weddingid,eventname);
            }
        });
    }

    private void savetogallery(final String weddingid, final String eventname) {
        StorageReference reference = storageReference.child("photos").child(fileUri.getLastPathSegment());
        reference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Saved successfully", Toast.LENGTH_SHORT).show();
                String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                String mediatype = taskSnapshot.getMetadata().getContentType();
                DocumentReference ref = db.collection("weddings").document(weddingid).collection("gallery").document();
                String imageid = ref.getId();
                Map<String, Object> map = new HashMap<>();
                map.put("medialink", medialink);
                map.put("mediatype", mediatype);
                map.put("event", eventname);
                db.collection("weddings").document(weddingid).collection("gallery").document(imageid).set(map);
            }
        });

    }

    private void posttotimeline(final String weddingid, final String eventname) {
        final String des = description.getText().toString().trim();
        StorageReference reference = storageReference.child("photos").child(fileUri.getLastPathSegment());
        reference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Posted successfully", Toast.LENGTH_SHORT).show();
                final String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                final String mediatype = taskSnapshot.getMetadata().getContentType();
                DocumentReference ref = db.collection("weddings").document(weddingid).collection("gallery").document();
                String imageid = ref.getId();
                Map<String, Object> map = new HashMap<>();
                map.put("medialink", medialink);
                map.put("mediatype", mediatype);
                map.put("event", eventname);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());
                final java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);

                db.collection("weddings").document(weddingid).collection("gallery").document(imageid).set(map);
                db.collection("weddings").document(weddingid).collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String name = task.getResult().getString("username");
                        Map<String,Object> objectMap = new HashMap<>();
                        objectMap.put("des",des);
                        objectMap.put("event",eventname);
                        objectMap.put("medialink",medialink);
                        objectMap.put("mediatype",mediatype);
                        objectMap.put("time",timestamp);
                        objectMap.put("username",name);
                        DocumentReference documentReference = db.collection("weddings").document(weddingid).collection("timeline").document();
                        String postid = documentReference.getId();
                        db.collection("weddings").document(weddingid).collection("timeline").document(postid).set(objectMap);
                    }
                });
            }
        });

    }

    private void getteam(final String weddingid) {
        db.collection("weddings").document(weddingid).collection("users")
                .document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                getevent(weddingid,task.getResult().getString("team"));
            }
        });
    }

    private void getevent(final String weddingid, final String team) {
        db.collection("weddings").document(weddingid).collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                events.clear();
                for(DocumentSnapshot doc:task.getResult()){
                    if(doc.getString("team").contains(team) || doc.getString("team").contains("both")){
                        String event = doc.getId();
                        events.add(event);
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,events);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventselect.setAdapter(adapter);
                eventselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String eventname = String.valueOf(adapterView.getItemAtPosition(i));
                        forward(weddingid,eventname);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        // 1- for high quality video
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);
            imgPreview.setImageURI(fileUri);
            // bimatp factory
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            // downsizing image as it throws OutOfMemory Exception for larger
//            // images
//            options.inSampleSize = 8;
//
//            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                    options);
//
//            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void previewVideo() {
        try {
            // hide image preview
            imgPreview.setVisibility(View.GONE);
            videoPreview.setVideoURI(fileUri);
            videoPreview.setVisibility(View.VISIBLE);
            //videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(getActivity(),"com.anmol.wedza", getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.RECORD_AUDIO)
                        ==PackageManager.PERMISSION_GRANTED)
            imageChooser();

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)
                !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        }
    }
    private void imageChooser() {
        imagelayout.setVisibility(View.VISIBLE);
        btnlayout.setVisibility(View.VISIBLE);
        allow.setVisibility(View.GONE);
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
                Toast.makeText(getActivity(), "Permissions not given!!", Toast.LENGTH_SHORT).show();
            } else
                imageChooser();

        }




    }
    
}
