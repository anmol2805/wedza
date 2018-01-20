package com.anmol.wedza;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhoneVerification extends AppCompatActivity {

    //Views
    private CircleImageView profilePictureIV;

    private TextView usernameTV;
    private TextView goBackTV;
    private TextView refreshTV;

    private Button signOut;

    private FloatingActionButton verifyCode;
    private FloatingActionButton get_verification_code;

    private EditText phone_number;
    private EditText code;

    //Data from previous activity
    private String profilePicturePath;
    private String username;
    private String weddingid;

    //Final Values
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private static final String TAG ="PhoneVerification";

    //Values for verification onCodeSent
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

    //Callback for phone verification
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                phone_number.setError("Your number should start with +91");
                showToast("Invalid Phone number");
            } else if (e instanceof FirebaseTooManyRequestsException) {

            }
        }

        @Override
        public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken token) {
            Log.d(TAG, "onCodeSent:" + verificationId);
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    //Firebase Variable
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        setTitle("Verify");
        //Retrieve all the values from the previous activity
        Intent intent = getIntent();
        profilePicturePath = intent.getStringExtra("profilePicturePath");
        username = intent.getStringExtra("username");
        weddingid = intent.getStringExtra("weddingid");

        //Instantiate All the views
        profilePictureIV = (CircleImageView) findViewById(R.id.profile_picture);

        usernameTV = (TextView) findViewById(R.id.username);
        goBackTV = (TextView) findViewById(R.id.goBack);
        refreshTV = (TextView) findViewById(R.id.refreshTV);

        signOut = (Button) findViewById(R.id.sign_out);


        verifyCode = (FloatingActionButton) findViewById(R.id.verifyCode);
        get_verification_code = (FloatingActionButton) findViewById(R.id.get_verification_code);

        phone_number = (EditText) findViewById(R.id.phone_number);
        code = (EditText) findViewById(R.id.verification_code_ET);

        //Instantiate Firebase Variables
        mAuth = FirebaseAuth.getInstance();


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                logout();
            }
        });

        goBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackAnimation();
            }
        });

        refreshTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode();
            }
        });

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

        get_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForwardAnimation();
                phoneAuthentication();
            }
        });

        if (profilePicturePath != null) {
            Glide.with(this)
                    .load(profilePicturePath)
                    .into(profilePictureIV);
        }

        if (username != null)
            usernameTV.setText("Hi there , " + username + " !");

        startPermissionsRequest();
    }

    private void goBackAnimation() {
        goBackTV.animate()
                .translationX(4000f)
                .setDuration(300)
                .start();

        get_verification_code.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();

        get_verification_code.setVisibility(View.VISIBLE);

        phone_number.animate()
                .translationX(0f)
                .setDuration(300)
                .start();

        code.animate()
                .translationX(-4000f)
                .setDuration(300)
                .start();

        verifyCode.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(300)
                .start();

        verifyCode.setVisibility(View.INVISIBLE);

        refreshTV.animate()
                .alpha(0f)
                .setDuration(300)
                .start();
    }

    private void goForwardAnimation(){

        goBackTV.animate()
                .translationX(0f)
                .setDuration(300)
                .start();

        get_verification_code.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(300)
                .start();

        get_verification_code.setVisibility(View.INVISIBLE);

        phone_number.animate()
                .translationX(-4000f)
                .setDuration(300)
                .start();

        code.animate()
                .translationX(0f)
                .setDuration(300)
                .start();

        verifyCode.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();

        verifyCode.setVisibility(View.VISIBLE);

        refreshTV.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

    }

    private void logout() {
        Intent intent1 = new Intent(this, LoginActivity.class);
        startActivity(intent1);
    }

    private void startPermissionsRequest() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("We Want to Automatically detect Your Phone Number");
                alertDialogBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionRequest();
                    }
                });
                alertDialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialogBuilder.show();

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
            getPhoneNumber();
    }

    private void permissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   getPhoneNumber();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getPhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager)  getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        phone_number.setText(mPhoneNumber);
    }

    private void phoneAuthentication(){
        if(phone_number.getText()!=null){

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone_number.getText().toString(),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }

    private void verifyCode(){

        if(!((code.equals(null))||code.equals(""))) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }else
            showToast("Please enter the code");
    }

    private void resendCode(){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,
                mResendToken);        // OnVerificationStateChangedCallbacks

        showToast("Resent Verification Code !");

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PhoneVerification.this,IntroduceYourselfActivity.class);
                            intent.putExtra("profilePicturePath",profilePicturePath);
                            intent.putExtra("username",username);
                            intent.putExtra("weddingid",weddingid);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                            showToast("Successfully Verified and Logged in !");
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
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

