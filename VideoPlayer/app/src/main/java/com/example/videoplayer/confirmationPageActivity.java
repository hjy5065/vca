package com.example.videoplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class confirmationPageActivity extends AppCompatActivity {
    ArrayList<String> featureInfoList;
    String beans = "beans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        Intent intent = getIntent();
        ArrayList<String> featureInfoList1 = intent.getStringArrayListExtra(MainActivity.EXTRA_FEATUREARRAY);

        featureInfoList = featureInfoList.add(0, beans);

        final TextView tv_feature = findViewById(R.id.tv_feature);
        tv_feature.setText(featureInfoList.get(0));
    }
}
