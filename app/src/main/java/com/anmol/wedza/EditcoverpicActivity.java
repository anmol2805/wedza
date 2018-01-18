package com.anmol.wedza;

import android.*;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class EditcoverpicActivity extends AppCompatActivity {
    ImageButton uploadpic;
    EditText weddate;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int PICK_REQUEST_CODE = 300;
    Uri fileuri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcoverpic);
        uploadpic = (ImageButton)findViewById(R.id.uploadpic);
        weddate = (EditText)findViewById(R.id.weddate);
        permissionRequest();
        weddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(EditcoverpicActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                            selectedmonth = selectedmonth + 1;
                            weddate.setText("" + selectedday + "." + selectedmonth + "." + selectedyear);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            }
        });
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest();
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
                fileuri = uri;
                uploadpic.setImageURI(uri);

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
