package com.anmol.wedza;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class StoryMediaPreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mediashow);
        ImageView previmg = (ImageView)findViewById(R.id.previmg);
        final VideoView prevvid = (VideoView)findViewById(R.id.prevvid);
        String medialink = getIntent().getStringExtra("medialink");
        String mediatype = getIntent().getStringExtra("mediatype");
        if(mediatype.contains("image")){
            previmg.setVisibility(View.VISIBLE);
            Glide.with(this).load(medialink).into(previmg);
        }
        else if (mediatype.contains("video")){
            prevvid.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(prevvid);
            prevvid.setMediaController(mediaController);
            prevvid.setVideoURI(Uri.parse(medialink));
            prevvid.requestFocus();
            prevvid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    prevvid.start();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.still,R.anim.slide_left_out);
    }
}
