package com.example.videoplayer;

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
import android.view.View;
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

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class videoPlayerActivity extends AppCompatActivity {

    CustomVideoView cVideoView;
    int position = -1;
    ImageView img;

    private ArrayList<String> productNameArray = confirmationActivity.getProductNameArray();
    private ArrayList<String> eCommerceInfoArray = confirmationActivity.geteCommerceInfoArray();
    private ArrayList<Integer> appearanceTimeStartArray = confirmationActivity.getAppearanceTimeStartArray();
    private ArrayList<Integer> appearanceTimeEndArray = confirmationActivity.getAppearanceTimeEndArray();
    private ArrayList<Integer> quadrantNumberArray = confirmationActivity.getQuadrantNumberArray();
    private ArrayList<Integer> indexArray = confirmationActivity.getIndexArray();

    int quadrantNumber;

    Bitmap screenshot;
    Bitmap[] screenshotQuadrants;
    Bitmap finalScreenshot;

    FFmpegMediaMetadataRetriever retriever;
    String rotation;

    private Rect mSelection;

    ImageView quadrant1;
    ImageView quadrant2;
    ImageView quadrant3;
    ImageView quadrant4;

    boolean executed = false;

    private MediaPlayer mediaPlayer;

    private FFmpeg ffmpeg;
    private String filePath;
    private Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        loadFFMpegBinary();

        cVideoView = (CustomVideoView) findViewById(R.id.my_player);

        int j = 0;
        for (String product : productNameArray) {
            Log.e(String.valueOf(j), product);
            j++;
        }

        int k = 0;
        for (String info : eCommerceInfoArray) {
            Log.e(String.valueOf(k), info);
            k++;
        }

        int l = 0;
        for (int startTime : appearanceTimeStartArray) {
            Log.e(String.valueOf(l), String.valueOf(startTime));
            l++;
        }

        int r = 0;
        for (int endTime : appearanceTimeEndArray) {
            Log.e(String.valueOf(r), String.valueOf(endTime));
            r++;
        }

        int q = 0;
        for (int loc : quadrantNumberArray) {
            Log.e(String.valueOf(q), String.valueOf(loc));
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
                    //hideQuadrants();
                    executed = false;
                }

            }

            @Override
            public void onPause() {

                int closestStart = appearanceTimeStartArray.get(0);
                int distance = Math.abs(closestStart - cVideoView.getCurrentPosition());
                for(int i: appearanceTimeStartArray){
                    int distanceI = Math.abs(i - cVideoView.getCurrentPosition());
                    if(distance > distanceI) {
                        closestStart = i;
                        distance = distanceI;
                    }
                }

                int timeIndex = appearanceTimeStartArray.indexOf(closestStart); //when getting times and locations, use timeIndex
                int featureIndex = indexArray.get(timeIndex); //when getting name and info, use featureIndex

                int end = appearanceTimeEndArray.get(timeIndex);

                Log.e("current position", String.valueOf(cVideoView.getCurrentPosition()));
                Log.e("Closest starttime is", String.valueOf(closestStart));
                Log.e("Starttime's index is", String.valueOf(timeIndex));
                Log.e("Correspond endtime is", String.valueOf(end));

                //Log.e("feature index", String.valueOf(featureIndex));


                // If current is 5860, and closestStart is 5000, it should execute.
                // If 5860 is less than 5000+1000 = 6000, and if 5860 is greater than or equal to 5000, execute.

                //If current is 5860, closesStart is 2000, and end is 8000, it should execute.
                //If 5860 is less than 8000+1000 = 9000, and if 5860 is greater than or equal to 2000, execute.

                if (cVideoView.getCurrentPosition() < end+1000 && cVideoView.getCurrentPosition() >= closestStart){
                    Log.e("We are", "In conditional");
                    quadrantNumber = quadrantNumberArray.get(timeIndex);
                    Log.e("Quadrant number", String.valueOf(quadrantNumber));
                    Log.e("Product", productNameArray.get(featureIndex));
                    execMetaDataRetriever(timeIndex);
                }


            }

            @Override
            public void onResume() {

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


    private void execMetaDataRetriever(int quadrantIndex) {
        int currentPosition = cVideoView.getCurrentPosition();
        double position = (double)currentPosition/(double)1000;
        extractImagesVideo(position);



        /*
        try {
            screenshot = retriever.getFrameAtTime(currentPosition*1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            retriever.release();
            Log.e("Took a screenshot", "Yes");
            if (rotation.equals("90")){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                screenshot = Bitmap.createBitmap(screenshot, 0, 0, screenshot.getWidth(), screenshot.getHeight(), matrix, true);
            }
            screenshotQuadrants = splitBitmap(screenshot);
            //screenshotQuadrants[2] = highlightImage(screenshotQuadrants[2]);
            img = (ImageView) findViewById(R.id.img);
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.bringToFront();
            img.setImageBitmap(screenshot);
            quadrantNumber = quadrantNumberArrayList.get(quadrantIndex);
            setQuadrants();
            executed = true;
        } catch (IllegalArgumentException ex) {
            Log.e("Catch", "Illegal argument exception");
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            Log.e("Catch", "RuntimeException");
            ex.printStackTrace();
        } finally {
            Log.e("Finally","retriever.release");
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                Log.e("Catch inside finally", "Runtime Exception");
            }
        }
        */


    }

    public void setQuadrants(){

        if (quadrantNumber == 1){
            Log.e("Quadrant 1", "Highlighted");
            quadrant1 = findViewById(R.id.quadrant1);
            quadrant1.setImageBitmap(screenshotQuadrants[0]);
            quadrant1.getLayoutParams().width = img.getWidth()/2;
            quadrant1.getLayoutParams().height = img.getHeight()/2;
            quadrant1.requestLayout();
            quadrant1.setColorFilter(0x808fd2ea);
            quadrant1.bringToFront();
        }
        else if (quadrantNumber == 2){
            Log.e("Quadrant 2", "Highlighted");
            quadrant2 = findViewById(R.id.quadrant2);
            quadrant2.setImageBitmap(screenshotQuadrants[1]);
            quadrant2.getLayoutParams().width = img.getWidth()/2;
            quadrant2.getLayoutParams().height = img.getHeight()/2;
            quadrant2.requestLayout();
            quadrant2.setColorFilter(0x808fd2ea);
            quadrant2.bringToFront();
        }
        else if (quadrantNumber == 3){
            Log.e("Quadrant 3", "Highlighted");
            quadrant3 = findViewById(R.id.quadrant3);
            quadrant3.setImageBitmap(screenshotQuadrants[2]);
            quadrant3.getLayoutParams().width = (img.getWidth()/2);
            quadrant3.getLayoutParams().height = (img.getHeight()/2);
            quadrant3.requestLayout();
            quadrant3.setColorFilter(0x808fd2ea);
            quadrant3.bringToFront();
        }
        else{
            Log.e("Quadrant 4", "Highlighted");
            quadrant4 = findViewById(R.id.quadrant4);
            quadrant4.setImageBitmap(screenshotQuadrants[3]);
            quadrant4.getLayoutParams().width = img.getWidth()/2;
            quadrant4.getLayoutParams().height = img.getHeight()/2;
            quadrant4.requestLayout();
            quadrant4.setColorFilter(0x808fd2ea);
            quadrant4.bringToFront();
        }
    }

    /*
    public void hideQuadrants(){
        if (quadrantNumber == 1){
            quadrant1.setColorFilter(null);
        }
        else if (quadrantNumber == 2){
            quadrant2.setColorFilter(null);
        }
        else if (quadrantNumber == 3){
            quadrant3.setColorFilter(null);
        }
        else{
            quadrant4.setColorFilter(null);
        }
    }
    */

    public Bitmap[] splitBitmap(Bitmap picture)
    {
        Log.e("Split bitmap", "Yes");
        Bitmap[] imgs = new Bitmap[4];
        imgs[0] = Bitmap.createBitmap(picture, 0, 0, picture.getWidth()/2 , picture.getHeight()/2);
        imgs[1] = Bitmap.createBitmap(picture, picture.getWidth()/2, 0, picture.getWidth()/2, picture.getHeight()/2);
        imgs[2] = Bitmap.createBitmap(picture,0, picture.getHeight()/2, picture.getWidth()/2,picture.getHeight()/2);
        imgs[3] = Bitmap.createBitmap(picture, picture.getWidth()/2, picture.getHeight()/2, picture.getWidth()/2, picture.getHeight()/2);

        return imgs;
    }

    /*
    public Bitmap highlightImage(Bitmap src) {
        Log.e("Highlight","Done");
        // create new bitmap, which will be painted and becomes result image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth()+7, src.getHeight()+7, Bitmap.Config.ARGB_8888);
        // setup canvas for painting
        Canvas canvas = new Canvas(bmOut);
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // create a blur paint for capturing alpha
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(14, BlurMaskFilter.Blur.NORMAL));
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
    /*
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

    private void extractImagesVideo(double startTime) {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );

        String filePrefix = "extract_picture";
        String fileExtn = ".jpg";


        File dir = new File(moviesDir, "VCA");
        int fileNo = 0;

        if (!dir.exists()){
            dir.mkdir();
            Log.e("Just made one", "VCA folder");
        }

        /*If VCA20 already exists, next folder will be VCA21.
        while (dir.exists()) {
            fileNo++;
            dir = new File(moviesDir, "VCA" + fileNo);
        }
        */
        dir.mkdir();
        filePath = dir.getAbsolutePath();
        File dest = new File(dir, filePrefix + "%03d" + fileExtn);
        String[] complexCommand = {"-y", "-i", String.valueOf(MainActivity.fileArrayList.get(position)), "-preset", "ultrafast", "-vframes", "1", "-ss", "" + startTime, dest.getAbsolutePath()};
        execFFmpegBinary(complexCommand);

    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d("VCA", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d("VCA", "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d("VCA", "EXception no controlada : " + e);
        }
    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(videoPlayerActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoPlayerActivity.this.finish();
                    }
                })
                .create()
                .show();

    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d("VCA", "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("VCA", "SUCCESS" + s);

                    File dir = new File(filePath);

                    File[] listFile = dir.listFiles();


                    bmp = BitmapFactory.decodeFile(String.valueOf(listFile[0]));
                    img = findViewById(R.id.img);
                    img.setImageBitmap(bmp);
                    img.bringToFront();

                    screenshotQuadrants = splitBitmap(bmp);
                    setQuadrants();
                }

                @Override
                public void onProgress(String s) {
                    Log.d("VCA", "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d("VCA", "Started command : ffmpeg " + command);
                }

                @Override
                public void onFinish() {
                    Log.d("VCA", "Finished command : ffmpeg " + command);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }



}