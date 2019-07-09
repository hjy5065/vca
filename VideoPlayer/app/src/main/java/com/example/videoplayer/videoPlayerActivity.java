package com.example.videoplayer;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class videoPlayerActivity extends AppCompatActivity {

    CustomVideoView cVideoView;
    int position = -1;
    ImageView img;
    private ArrayList<EditText> productNameArrayList = null;
    private ArrayList<EditText> eCommerceInfoArrayList = null;
    private ArrayList<EditText> appearanceTimeArrayList = null;
    private ArrayList<EditText> quadrantNumberArrayList = null;

    String productName;
    String eCommerceInfo;
    String appearanceTime;
    String quadrantNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        cVideoView = (CustomVideoView) findViewById(R.id.my_player);

        setStrings();

        cVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                Toast.makeText(videoPlayerActivity.this, productName, Toast.LENGTH_SHORT).show();
                cVideoView.bringToFront();

            }

            @Override
            public void onPause() {
                Toast.makeText(videoPlayerActivity.this, quadrantNumber, Toast.LENGTH_SHORT).show();
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

        int currentPosition = cVideoView.getCurrentPosition();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(String.valueOf(MainActivity.fileArrayList.get(position)));

            img.setImageBitmap(retriever.getFrameAtTime(currentPosition*1000, MediaMetadataRetriever.OPTION_CLOSEST));


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

    public void setStrings(){
        productNameArrayList = MainActivity.getProductNameArray();
        productName = productNameArrayList.get(0).getText().toString();

        quadrantNumberArrayList = MainActivity.getQuadrantNumberArray();
        quadrantNumber = quadrantNumberArrayList.get(2).getText().toString();
    }

    /*
    private void editImage(){

    }
    */
}