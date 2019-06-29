package com.example.videoplayer;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

public class videoPlayerActivity extends AppCompatActivity {

    CustomVideoView cVideoView;
    int position = -1;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        cVideoView = (CustomVideoView) findViewById(R.id.my_player);
        cVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                Toast.makeText(videoPlayerActivity.this, "Playing!", Toast.LENGTH_SHORT).show();
                cVideoView.bringToFront();

            }

            @Override
            public void onPause() {
                Toast.makeText(videoPlayerActivity.this, "Paused!", Toast.LENGTH_SHORT).show();
                execMetaDataRetriever();
            }
        });


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        position = getIntent().getIntExtra("position", -1);
        getSupportActionBar().hide();

        playVideo();

    }

    private void playVideo() {

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(cVideoView);

        cVideoView.setMediaController(mediaController);
        cVideoView.setVideoPath(String.valueOf(MainActivity.fileArrayList.get(position)));
        cVideoView.requestFocus();

        //start
        cVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                cVideoView.start();

            }
        });

        cVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoPlayerActivity.super.onBackPressed();
                cVideoView.stopPlayback();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cVideoView.stopPlayback();
    }


    private void execMetaDataRetriever() {
        img = (ImageView) findViewById(R.id.img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.bringToFront();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(String.valueOf(MainActivity.fileArrayList.get(position)));

            img.setImageBitmap(retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST));


        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }


    }


}