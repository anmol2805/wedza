package com.anmol.wedza;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TimePicker;
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateeventActivity extends AppCompatActivity {
    EditText eventnameedit,eventlocedit,eventdesedit;
    RadioButton groom,bride,both;
    ImageButton eventimgedit;
    Button createevent;
    Button dateedit,timeedit;
    private static final int PICK_REQUEST_CODE = 300;
    Uri fileuri = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String team;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int MY_PERMISSIONS_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createevent);
        eventnameedit = (EditText)findViewById(R.id.eventnameedit);
        eventlocedit = (EditText)findViewById(R.id.eventlocationedit);
        dateedit = (Button) findViewById(R.id.dateedit);
        timeedit = (Button) findViewById(R.id.timeedit);
        eventdesedit = (EditText)findViewById(R.id.eventdesedit);
        groom = (RadioButton)findViewById(R.id.groom);
        bride = (RadioButton)findViewById(R.id.bride);
        both = (RadioButton)findViewById(R.id.both);
        eventimgedit = (ImageButton)findViewById(R.id.eventimgedit);
        createevent = (Button)findViewById(R.id.createevent);
//        dateedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//
//                }
//
//            }
//        });
        dateedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(CreateeventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        dateedit.setText("" + selectedyear + "-" + selectedmonth + "-" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        timeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateeventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeedit.setText( selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
//        timeedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//
//                }
//
//
//            }
//        });

        eventimgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest();
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
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String eventname = eventnameedit.getText().toString().trim();
                final String eventloc = eventlocedit.getText().toString().trim();
                String date = dateedit.getText().toString().trim();
                String time = timeedit.getText().toString().trim();
                final String des = eventdesedit.getText().toString().trim();
                try {
                    Date mdate = sdf.parse(date + " " + time);
                    final String finaltime = sdf.format(mdate);
                    final Timestamp timestamp = Timestamp.valueOf(finaltime);
                    StorageReference reference = storageReference.child(fileuri.getLastPathSegment());
                    reference.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String medialink = String.valueOf(taskSnapshot.getDownloadUrl());
                            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String weddindid = task.getResult().getString("currentwedding");
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("eventdes",des);
                                    map.put("eventimg",medialink);
                                    map.put("eventlocation",eventloc);
                                    map.put("team",team);
                                    map.put("eventtime",finaltime);
                                    map.put("time",timestamp);
                                    db.collection("weddings").document(weddindid).collection("events").document(eventname).set(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    finish();
                                                }
                                            });
                                }
                            });
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }


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


}
