package com.anmol.wedza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anmol.wedza.Fragments.gallery;
import com.anmol.wedza.Fragments.guestlist;
import com.anmol.wedza.Fragments.home;
import com.anmol.wedza.Fragments.media;
import com.anmol.wedza.Fragments.story;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button timeline,guestlist,gallery,story;
    ImageButton camera;
    private static long back_pressed;
    String authcheck = "2805";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }
        else {
            //checks if user data is present or not
            //edit query
            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(!snapshot.exists()){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        Toast.makeText(HomeActivity.this,"Please complete your authentication procedure",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{

                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);



                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                                HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        navigationView.setNavigationItemSelectedListener(HomeActivity.this);

                        setFragment(new home());

                        camera = (ImageButton)findViewById(R.id.camera);
                        timeline = (Button)findViewById(R.id.vtimeline);
                        guestlist = (Button)findViewById(R.id.vguestlist);
                        gallery = (Button)findViewById(R.id.vgallery);
                        story = (Button)findViewById(R.id.vstory);
                        timeline.setBackgroundResource(R.drawable.homeredfilled);
                        guestlist.setBackgroundResource(R.drawable.barratgrey);
                        camera.setBackgroundResource(R.drawable.round_button);
                        gallery.setBackgroundResource(R.drawable.gallerygreyfilled);
                        story.setBackgroundResource(R.drawable.storygrey);
                        camera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeline.setBackgroundResource(R.drawable.homegrey);
                                guestlist.setBackgroundResource(R.drawable.barratgrey);
                                camera.setBackgroundResource(R.drawable.round_button1);
                                gallery.setBackgroundResource(R.drawable.gallerygreyfilled);
                                story.setBackgroundResource(R.drawable.storygrey);
                                //startActivity(new Intent(HomeActivity.this,CameraActivity.class));
                                setFragment(new media());
                            }
                        });
                        guestlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeline.setBackgroundResource(R.drawable.homegrey);
                                guestlist.setBackgroundResource(R.drawable.barratred);
                                camera.setBackgroundResource(R.drawable.round_button);
                                gallery.setBackgroundResource(R.drawable.gallerygreyfilled);
                                story.setBackgroundResource(R.drawable.storygrey);
                                setFragment(new guestlist());
                            }
                        });
                        timeline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeline.setBackgroundResource(R.drawable.homeredfilled);
                                guestlist.setBackgroundResource(R.drawable.barratgrey);
                                camera.setBackgroundResource(R.drawable.round_button);
                                gallery.setBackgroundResource(R.drawable.gallerygreyfilled);
                                story.setBackgroundResource(R.drawable.storygrey);
                                setFragment(new home());
                            }
                        });
                        gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeline.setBackgroundResource(R.drawable.homegrey);
                                guestlist.setBackgroundResource(R.drawable.barratgrey);
                                camera.setBackgroundResource(R.drawable.round_button);
                                gallery.setBackgroundResource(R.drawable.galleryr);
                                story.setBackgroundResource(R.drawable.storygrey);
                                setFragment(new gallery());
                            }
                        });
                        story.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeline.setBackgroundResource(R.drawable.homegrey);
                                guestlist.setBackgroundResource(R.drawable.barratgrey);
                                camera.setBackgroundResource(R.drawable.round_button);
                                gallery.setBackgroundResource(R.drawable.gallerygreyfilled);
                                story.setBackgroundResource(R.drawable.storyred);
                                setFragment(new story());
                            }
                        });
                    }
                }
            });

        }


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.still,R.anim.slide_out_down);
        }else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content,new home()).commit();
            Toast.makeText(getBaseContext(), "Double tap to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edtpersonalinfo) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(HomeActivity.this,Editpersonalinfo.class));
                }
            },100);

        }
        else if(id == R.id.nav_wedset){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(HomeActivity.this,Weddingsettings.class));
                }
            },100);

        }
        else if (id == R.id.nav_createwed) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(HomeActivity.this,CreateweddingActivity.class));
                }
            },100);

        } else if (id == R.id.nav_contactwed) {
            Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                    startActivity(new Intent(HomeActivity.this,ContactdeveloperActivity.class));
                }
            },100);

        } else if (id == R.id.nav_like) {
            Uri uri = Uri.parse("market://details?id=" + "com.anmol.wedza");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + "com.anmol.hibiscus")));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
        fragmentManager.executePendingTransactions();
    }
}
