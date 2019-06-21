package com.example.videoplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class videoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    int position = -1;
    private int stopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = (VideoView)findViewById(R.id.myPlayer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        position = getIntent().getIntExtra("position", -1);
        getSupportActionBar().hide();

        playVideo();

    }

    private void playVideo() {

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoPath(String.valueOf(MainActivity.fileArrayList.get(position)));
        videoView.requestFocus();

        //start
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                videoView.start();

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(String.valueOf(MainActivity.fileArrayList.get(position = position+1)));
                videoView.start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

}
