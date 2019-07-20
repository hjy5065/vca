package com.example.videoplayer;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wseemann.media.FFmpegMediaMetadataRetriever;

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
    String appearanceTimeString;
    String quadrantNumberString;
    int appearanceTime;
    int quadrantNumber;

    Bitmap screenshot;
    Bitmap[] screenshotQuadrants;
    Bitmap finalScreenshot;

    FFmpegMediaMetadataRetriever retriever;
    String rotation;

    private Rect mSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        cVideoView = (CustomVideoView) findViewById(R.id.my_player);

        /*
        cVideoView.post(new Runnable() {
            @Override
            public void run() {
                // do resizing here
                // AR = aspect ratio

                float videoARWidth = 16.f;
                float videoARHeight = 9.f;

                // Phone screen aspect ratio height
                float screenARHeight = 9.f;

                // scale to screen AR height
                float videoScale = screenARHeight / videoARHeight;

                float videoARRatio = videoARWidth / videoARHeight;

                // scale the ratio to screen
                float videoScaledARRatio = videoARRatio * videoScale;

                ViewGroup.LayoutParams layoutParams = cVideoView.getLayoutParams();

                // make sure the VideoView matches the screen height
                layoutParams.width = (int)(cVideoView.getHeight() * videoScaledARRatio);
                cVideoView.setLayoutParams(layoutParams);
            }
        });

        */



        setValues();

        cVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                Toast.makeText(videoPlayerActivity.this, String.valueOf(appearanceTime), Toast.LENGTH_SHORT).show();
                cVideoView.bringToFront();

            }

            @Override
            public void onPause() {
                Toast.makeText(videoPlayerActivity.this, String.valueOf(quadrantNumber), Toast.LENGTH_SHORT).show();
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
        int currentPosition = cVideoView.getCurrentPosition();

        try {
            screenshot = retriever.getFrameAtTime(currentPosition*1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            retriever.release();


            if (rotation.equals("90")){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                screenshot = Bitmap.createBitmap(screenshot, 0, 0, screenshot.getWidth(), screenshot.getHeight(), matrix, true);
            }


            //screenshotQuadrants = splitBitmap(screenshot);
            //finalScreenshot = highlightImage(screenshotQuadrants[0]);


            /*
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            img.setLayoutParams(params);
            */


            img = (ImageView) findViewById(R.id.img);
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.bringToFront();
            img.setImageBitmap(screenshot);

            ImageView quadrant1 = findViewById(R.id.quadrant1);
            quadrant1.getLayoutParams().width = img.getWidth()/2;
            quadrant1.getLayoutParams().height = img.getHeight()/2;
            quadrant1.requestLayout();

            quadrant1.setColorFilter(0x808fd2ea);
            quadrant1.bringToFront();





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

    public void setValues(){
        productNameArrayList = MainActivity.getProductNameArray();
        productName = productNameArrayList.get(0).getText().toString();

        eCommerceInfoArrayList = MainActivity.geteCommerceInfoArray();
        eCommerceInfo = eCommerceInfoArrayList.get(0).getText().toString();


        appearanceTimeArrayList = MainActivity.getAppearanceTimeArray();
        appearanceTimeString = appearanceTimeArrayList.get(0).getText().toString();
        String[] units = appearanceTimeString.split(":");
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        appearanceTime = 60 * minutes + seconds; //add up values

        quadrantNumberArrayList = MainActivity.getQuadrantNumberArray();
        quadrantNumberString = quadrantNumberArrayList.get(0).getText().toString();
        quadrantNumber = Integer.parseInt(quadrantNumberString);
    }

    public Bitmap[] splitBitmap(Bitmap picture)
    {

        Bitmap[] imgs = new Bitmap[4];
        imgs[0] = Bitmap.createBitmap(picture, 0, 0, picture.getWidth()/2 , picture.getHeight()/2);
        imgs[1] = Bitmap.createBitmap(picture, picture.getWidth()/2, 0, picture.getWidth()/2, picture.getHeight()/2);
        imgs[2] = Bitmap.createBitmap(picture,0, picture.getHeight()/2, picture.getWidth()/2,picture.getHeight()/2);
        imgs[3] = Bitmap.createBitmap(picture, picture.getWidth()/2, picture.getHeight()/2, picture.getWidth()/2, picture.getHeight()/2);

        return imgs;
    }
/*
    public Bitmap highlightImage(Bitmap src) {
        // create new bitmap, which will be painted and becomes result image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        // setup canvas for painting
        Canvas canvas = new Canvas(bmOut);
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // create a blur paint for capturing alpha
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        // capture alpha into a bitmap
        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
        // create a color paint
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(0xFFFFFFFF);
        // paint color for captured alpha region (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        // free memory
        bmAlpha.recycle();

        // paint the image source
        canvas.drawBitmap(src, 0, 0, null);

        // return out final image
        return bmOut;
    }

     * reduces the size of the image
     * @param image
     * @return

    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = screenshot.getWidth()/2;
            height = (int) (width / bitmapRatio);
        } else {
            height = screenshot.getHeight()/2;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    */

}