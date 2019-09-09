package com.example.videoplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CustomVideoView cVideoView;
    private int position = -1;

    private ArrayList<String> productNameArray = ConfirmationActivity.getProductNameArray();
    private ArrayList<String> eCommerceInfoArray = ConfirmationActivity.geteCommerceInfoArray();
    private ArrayList<Integer> appearanceTimeStartArray = ConfirmationActivity.getAppearanceTimeStartArray();
    private ArrayList<Integer> appearanceTimeEndArray = ConfirmationActivity.getAppearanceTimeEndArray();
    private ArrayList<Integer> quadrantNumberArray = ConfirmationActivity.getQuadrantNumberArray();
    private ArrayList<Integer> indexArray = ConfirmationActivity.getIndexArray();
    private ArrayList<Integer> takenQuad = new ArrayList<Integer>();
    private ArrayList<Spinner> creditsText = new ArrayList<Spinner>();

    private int quadrantNumber;

    private boolean executed = false;
    private int featureIndex;

    private Spinner dropDown;
    private int currentPosition;

    private boolean creditCalled;
    private boolean creditSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        cVideoView = (CustomVideoView) findViewById(R.id.my_player);

        cVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                cVideoView.bringToFront();
                if (creditSelected){
                    cVideoView.seekTo(cVideoView.getDuration()+999);
                    creditSelected = false;
                }
                else if(executed){
                    hideDropDown();
                    cVideoView.seekTo(currentPosition);
                    executed = false;
                }
            }

            @Override
            public void onPause() {
                currentPosition = cVideoView.getCurrentPosition();

                for (int i = 0; i < appearanceTimeStartArray.size(); i++){
                    if (appearanceTimeStartArray.get(i) <= currentPosition && currentPosition < appearanceTimeEndArray.get(i)+1000){
                        quadrantNumber = quadrantNumberArray.get(i);
                        if (!takenQuad.contains(quadrantNumber)){
                            takenQuad.add(quadrantNumber);
                            featureIndex = indexArray.get(i);
                            enableSpinner();
                        }
                    }
                }
                takenQuad.clear();
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


        cVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                cVideoView.start();
            }
        });

        cVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (creditCalled){
                    resetSelection();
                    showCredits();
                }
                else{
                    makeCreditsAppendix();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (creditCalled) {
            creditCalled = false;
            finish();
            startActivity(getIntent());
        }
        else{
            super.onBackPressed();
            cVideoView.stopPlayback();
        }
    }

    private void enableSpinner() {
        dropDownMenu();
        executed = true;
        creditCalled = false;
    }

    private void hideDropDown(){
        Spinner spinner1 = findViewById(R.id.spinner1);
        spinner1.setEnabled(false);
        spinner1.setClickable(false);
        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setEnabled(false);
        spinner2.setClickable(false);
        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setEnabled(false);
        spinner3.setClickable(false);
        Spinner spinner4 = findViewById(R.id.spinner4);
        spinner4.setEnabled(false);
        spinner4.setClickable(false);
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

        String[] items = new String[]{"Interested in " + productNameArray.get(featureIndex) + "?", "Order now ", "Receive a product message", "View information"};

        if (quadrantNumber == 1 || quadrantNumber == 3){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview13, R.id.textview_spinner13, items);
            dropDown.setAdapter(adapter);
            dropDown.setOnItemSelectedListener(this);
            dropDown.bringToFront();
        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview24, R.id.textview_spinner24, items);
            dropDown.setAdapter(adapter);
            dropDown.setOnItemSelectedListener(this);
            dropDown.bringToFront();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();
        if (selected.equals("View information")){
            Intent intent = new Intent(VideoPlayerActivity.this, WebPageActivity.class);
            if (creditCalled){
                creditSelected = true;
                intent.putExtra("link", eCommerceInfoArray.get(productNameArray.indexOf(parent.getItemAtPosition(0).toString())));
                startActivity(intent);
            }
            else{
                creditSelected = false;
                String substring = parent.getItemAtPosition(0).toString().substring(14, parent.getItemAtPosition(0).toString().indexOf("?"));
                intent.putExtra("link", eCommerceInfoArray.get(productNameArray.indexOf(substring)));
                startActivity(intent);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void showCredits(){
        NestedScrollView scroll = findViewById(R.id.scroll);
        scroll.bringToFront();
        creditCalled = true;
    }

    private void resetSelection(){
        for (Spinner i : creditsText){
            i.setSelection(0);
        }
    }

    private void makeCreditsAppendix(){
        LinearLayout credits = findViewById(R.id.credits);

        for (int i = 0; i < productNameArray.size(); i++){
            Spinner product = new Spinner(VideoPlayerActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.setMargins(0,2,2,0);
            product.setLayoutParams(params);
            product.setGravity(Gravity.CENTER);
            product.setBackgroundResource(R.drawable.bubble3);
            product.setPopupBackgroundResource(R.drawable.spinner_credit_bg);
            product.setDropDownVerticalOffset(40);
            product.setEnabled(true);
            product.setClickable(true);

            String[] items = new String[]{productNameArray.get(i), "Order now ", "Receive a product message", "View information"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(VideoPlayerActivity.this, R.layout.spinner_credits_textview, R.id.credit, items);
            product.setAdapter(adapter);
            featureIndex = i;
            product.setOnItemSelectedListener(VideoPlayerActivity.this);
            credits.addView(product);
            creditsText.add(product);
        }
        showCredits();
    }

}
