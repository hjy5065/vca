package com.example.videoplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class videoPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CustomVideoView cVideoView;
    int position = -1;
    ImageView img;

    private ArrayList<String> productNameArray = confirmationActivity.getProductNameArray();
    private ArrayList<String> eCommerceInfoArray = confirmationActivity.geteCommerceInfoArray();
    private ArrayList<Integer> appearanceTimeStartArray = confirmationActivity.getAppearanceTimeStartArray();
    private ArrayList<Integer> appearanceTimeEndArray = confirmationActivity.getAppearanceTimeEndArray();
    private ArrayList<Integer> quadrantNumberArray = confirmationActivity.getQuadrantNumberArray();
    private ArrayList<Integer> indexArray = confirmationActivity.getIndexArray();
    private ArrayList<Integer> takenQuad = new ArrayList<Integer>();
    private ArrayList<Spinner> creditsText = new ArrayList<Spinner>();

    int quadrantNumber;

    FFmpegMediaMetadataRetriever retriever;
    String rotation;


    boolean executed = false;
    int timeIndex;
    int featureIndex;

    Spinner dropDown;
    LinearLayout glow;

    int currentPosition;

    AnimatorSet animatorSet;

    private TextView creditsTitle;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        cVideoView = (CustomVideoView) findViewById(R.id.my_player);

        int j = 0;
        for (String product : productNameArray) {
            Log.e("Product " + String.valueOf(j), product);
            j++;
        }

        int k = 0;
        for (String info : eCommerceInfoArray) {
            Log.e("Info " + String.valueOf(k), info);
            k++;
        }

        int l = 0;
        for (int startTime : appearanceTimeStartArray) {
            Log.e("Start time " + String.valueOf(l), String.valueOf(startTime));
            l++;
        }

        int r = 0;
        for (int endTime : appearanceTimeEndArray) {
            Log.e("End time " + String.valueOf(r), String.valueOf(endTime));
            r++;
        }

        int q = 0;
        for (int loc : quadrantNumberArray) {
            Log.e("Location " + String.valueOf(q), String.valueOf(loc));
            q++;
        }

        int d = 0;
        for (int ind : indexArray) {
            Log.e(String.valueOf(d), String.valueOf(ind));
            d++;
        }

        cVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                cVideoView.bringToFront();
                if(executed){
                    hideDropDown();
                    cVideoView.seekTo(currentPosition);
                    executed = false;
                }

            }

            @Override
            public void onPause() {
                currentPosition = cVideoView.getCurrentPosition();
                Log.e("current position", String.valueOf(currentPosition));

                for (int i = 0; i < appearanceTimeStartArray.size(); i++){
                    Log.e("Inside", "for loop");
                    if (appearanceTimeStartArray.get(i) <= currentPosition &&
                            currentPosition < appearanceTimeEndArray.get(i)+1000){
                        quadrantNumber = quadrantNumberArray.get(i);
                        Log.e("Quadrant number", String.valueOf(quadrantNumber));
                        if (!takenQuad.contains(quadrantNumber)){
                            takenQuad.add(quadrantNumber);
                            featureIndex = indexArray.get(i);
                            enableSpinner();


                        }
                    }
                }
                takenQuad.clear();

                /*

                int closestStart = appearanceTimeStartArray.get(0);
                int distanceStart = Math.abs(closestStart - currentPosition);
                for(int i: appearanceTimeStartArray){
                    int distanceI = Math.abs(i - currentPosition);
                    if(distanceStart > distanceI) {
                        closestStart = i;
                        distanceStart = distanceI;
                    }
                }

                int closestEnd = appearanceTimeEndArray.get(0);
                int distanceEnd = Math.abs(closestEnd - currentPosition);
                for(int i: appearanceTimeEndArray){
                    int distanceI = Math.abs(i - currentPosition);
                    if(distanceEnd > distanceI) {
                        closestEnd = i;
                        distanceEnd = distanceI;
                    }
                }

                if ((Math.abs(closestStart-currentPosition) < Math.abs(closestEnd-currentPosition)) &&
                currentPosition >= closestStart){
                    Log.e("Start value is", "closer");
                    timeIndex = appearanceTimeStartArray.indexOf(closestStart); //when getting times and locations, use timeIndex
                    featureIndex = indexArray.get(timeIndex); //when getting name and info, use featureIndex
                    int corresEnd = appearanceTimeEndArray.get(timeIndex);

                    Log.e("current position", String.valueOf(currentPosition));
                    Log.e("Closest starttime is", String.valueOf(closestStart));
                    Log.e("Starttime's index is", String.valueOf(timeIndex));
                    Log.e("Correspond endtime is", String.valueOf(corresEnd));
                    Log.e("feature index", String.valueOf(featureIndex));

                    // If current is 5860, and closestStart is 5000, it should execute.
                    // If 5860 is less than 5000+1000 = 6000, and if 5860 is greater than or equal to 5000, execute.

                    //If current is 5860, closesStart is 2000, and end is 8000, it should execute.
                    //If 5860 is less than 8000+1000 = 9000, and if 5860 is greater than or equal to 2000, execute.

                    if (currentPosition < corresEnd+1000){
                        quadrantNumber = quadrantNumberArray.get(timeIndex);
                        Log.e("Quadrant number", String.valueOf(quadrantNumber));
                        Log.e("Product", productNameArray.get(featureIndex));
                        enableSpinner();
                    }
                }
                else if ((Math.abs(closestStart-currentPosition) > Math.abs(closestEnd-currentPosition)) &&
                        currentPosition < closestEnd+1000) {
                    Log.e("End value is", "closer");

                    timeIndex = appearanceTimeEndArray.indexOf(closestEnd); //when getting times and locations, use timeIndex
                    featureIndex = indexArray.get(timeIndex); //when getting name and info, use featureIndex
                    int corresStart = appearanceTimeStartArray.get(timeIndex);

                    Log.e("current position", String.valueOf(currentPosition));
                    Log.e("Closest endTime is", String.valueOf(closestEnd));
                    Log.e("EndTime's index is", String.valueOf(timeIndex));
                    Log.e("Correspond startTime is", String.valueOf(corresStart));
                    Log.e("feature index", String.valueOf(featureIndex));

                    // If current is 5860, and closestEnd is 6000, it should execute.
                    // If 5860 is less than 5000+1000 = 6000, and if 5860 is greater than or equal to 5000, execute.

                    //If current is 5860, closesEnd is 6000, and start is 5000, it should execute.
                    //If 5860 is less than 8000+1000 = 9000, and if 5860 is greater than or equal to 2000, execute.

                    if (currentPosition >= corresStart){
                        quadrantNumber = quadrantNumberArray.get(timeIndex);
                        Log.e("Quadrant number", String.valueOf(quadrantNumber));
                        Log.e("Product", productNameArray.get(featureIndex));
                        enableSpinner();
                    }
                }
                */

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

        retriever = new FFmpegMediaMetadataRetriever();
        retriever.setDataSource(String.valueOf(MainActivity.fileArrayList.get(position)));
        rotation = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

        if (rotation.equals("90")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


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
                LinearLayout credits = findViewById(R.id.credits);
                credits.bringToFront();

                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.credits_anim);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        cVideoView.stopPlayback();
                        videoPlayerActivity.super.onBackPressed();
                    }
                });

                creditsTitle = findViewById(R.id.tv_credits);
                creditsTitle.setText("Credits \n");

                for (int i = 0; i < productNameArray.size(); i++){
                    Spinner product = new Spinner(videoPlayerActivity.this);
                    //product.setTextColor(Color.parseColor("#ffffff"));
                    product.setGravity(Gravity.CENTER_HORIZONTAL);
                    //product.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_font));
                    //product.setText(productNameArray.get(i) + "\n");
                    product.setEnabled(true);
                    product.setClickable(true);

                    String[] items = new String[]{productNameArray.get(i),
                            "Order now ", "Receive a product message", "View information"}; //eCommerceInfoArray.get(featureIndex)

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(videoPlayerActivity.this,
                            android.R.layout.simple_list_item_1, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    product.setAdapter(adapter);
                    featureIndex = i;
                    product.setOnItemSelectedListener(videoPlayerActivity.this);
                    credits.addView(product);
                    creditsText.add(product);
                }

                creditsTitle.startAnimation(animation);
                for (Spinner credit : creditsText){
                    credit.startAnimation(animation);
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cVideoView.stopPlayback();
    }


    private void enableSpinner() {
        dropDownMenu();
        executed = true;
    }



    private void hideDropDown(){
        dropDown.setEnabled(false);
        dropDown.setClickable(false);
    }

    private void dropDownMenu(){
        if (quadrantNumber == 1){
            dropDown = findViewById(R.id.spinner1);
        }
        else if(quadrantNumber == 2){
            dropDown = findViewById(R.id.spinner2);
        }
        else if(quadrantNumber == 3){
            dropDown = findViewById(R.id.spinner3);
        }
        else if(quadrantNumber == 4){
            dropDown = findViewById(R.id.spinner4);
        }

        dropDown.setEnabled(true);
        dropDown.setClickable(true);

        String[] items = new String[]{"Interested in " + productNameArray.get(featureIndex) + "?",
                "Order now ", "Receive a product message", "View information"}; //eCommerceInfoArray.get(featureIndex)

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview, R.id.textview_spinner, items);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);
        dropDown.bringToFront();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();
        if (selected.equals("View information")){
            Intent intent = new Intent(videoPlayerActivity.this, webPageActivity.class);
            intent.putExtra("link", eCommerceInfoArray.get(featureIndex));
            Log.e("Link in videoplayer", eCommerceInfoArray.get(featureIndex));
            startActivity(intent);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}