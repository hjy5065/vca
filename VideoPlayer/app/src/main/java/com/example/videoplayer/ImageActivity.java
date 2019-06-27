package com.example.videoplayer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    private static final String FILEPATH = "filepath";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filePath = getIntent().getStringExtra(FILEPATH);
        ArrayList<String> f = new ArrayList<String>();

        File dir = new File(filePath);
        File[] listFile;

        listFile = dir.listFiles();



        for(File e:listFile)
        {
            f.add(e.getAbsolutePath());
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
