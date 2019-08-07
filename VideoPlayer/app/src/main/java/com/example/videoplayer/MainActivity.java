package com.example.videoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    MyAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    File directory;
    boolean boolean_permission;

    public static ArrayList<File> fileArrayList = new ArrayList<>();
    private static ArrayList<EditText> productNameArray = new ArrayList<EditText>();
    private static ArrayList<EditText> eCommerceInfoArray = new ArrayList<EditText>();
    private static ArrayList<EditText> appearanceTimeStartArray = new ArrayList<EditText>();
    private static ArrayList<EditText> appearanceTimeEndArray = new ArrayList<EditText>();
    private static ArrayList<EditText> quadrantNumberArray = new ArrayList<EditText>();
    private static ArrayList<Integer> indexArray = new ArrayList<Integer>();

    static int timeArrayIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_log);

        final EditText productName = findViewById(R.id.editText_name);
        productNameArray.add(productName);
        final EditText eCommerceInfo = findViewById(R.id.editText_link);
        eCommerceInfoArray.add(eCommerceInfo);
        final EditText appearanceTimeStart1 = findViewById(R.id.editText_time_start);
        appearanceTimeStartArray.add(appearanceTimeStart1);
        indexArray.add(timeArrayIndex);
        final EditText appearanceTimeEnd1 = findViewById(R.id.editText_time_end);
        appearanceTimeEndArray.add(appearanceTimeEnd1);
        final EditText quadrantNumber1 = findViewById(R.id.editText_location);
        quadrantNumberArray.add(quadrantNumber1);

        final Button addFeaturesButton = findViewById(R.id.button_add);

        addFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayout = findViewById(R.id.linearLayoutInfo);

                int timeRowIndex = linearLayout.getChildCount();

                Log.e(String.valueOf(linearLayout.getChildCount()), String.valueOf(timeRowIndex));

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View newRowView = inflater.inflate(R.layout.add_feature, null);
                linearLayout.addView(newRowView, timeRowIndex);

                View view = linearLayout.getChildAt(timeRowIndex);

                EditText addedName = (EditText) view.findViewById(R.id.added_name);
                productNameArray.add(addedName);

                EditText addedLink = (EditText) view.findViewById(R.id.added_link);
                eCommerceInfoArray.add(addedLink);

                timeArrayIndex++;
                EditText addedTimeStart = (EditText) view.findViewById(R.id.added_time_start);
                appearanceTimeStartArray.add(addedTimeStart);
                indexArray.add(timeArrayIndex);

                EditText addedTimeEnd = (EditText) view.findViewById(R.id.added_time_end);
                appearanceTimeEndArray.add(addedTimeEnd);

                EditText addedQuadrantNumber = (EditText) view.findViewById(R.id.added_loc);
                quadrantNumberArray.add(addedQuadrantNumber);




            }
        });

        final Button confirmButton = findViewById(R.id.button_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, confirmationActivity.class);
                startActivity(intent);

            }
        });


        final Button addButton = findViewById(R.id.button_add_times);


        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final LinearLayout linearLayout = findViewById(R.id.linearLayoutInfo);

                int timeRowIndex = linearLayout.getChildCount();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View newRowView = inflater.inflate(R.layout.add_time, null);
                linearLayout.addView(newRowView, timeRowIndex);


                View view = linearLayout.getChildAt(timeRowIndex);

                EditText appearanceTimeStart = (EditText) view.findViewById(R.id.editText_added_time_start);
                appearanceTimeStartArray.add(appearanceTimeStart);
                indexArray.add(timeArrayIndex);

                EditText appearanceTimeEnd = (EditText) view.findViewById(R.id.editText_added_time_end);
                appearanceTimeEndArray.add(appearanceTimeEnd);

                EditText quadrantNumber = (EditText) view.findViewById(R.id.editText_added_loc);
                quadrantNumberArray.add(quadrantNumber);

            }

        });


        /* WHEN READY FOR UPLOAD
        final Button uploadVideoButton = findViewById(R.id.???);
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main);

                myRecyclerView = (RecyclerView)findViewById(R.id.listVideoRecyler);

                //Phone memory and SD card
                directory = new File("/mnt/");

                //directory = new File("/storage/");

                GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
                myRecyclerView.setLayoutManager(manager);

                permissionForVideo();
            }

        });
        */


    }










    public static ArrayList<EditText> getProductNameArray(){
        return productNameArray;
    }
    public static ArrayList<EditText> geteCommerceInfoArray() {
        return eCommerceInfoArray;
    }
    public static ArrayList<EditText> getAppearanceTimeStartArray() { return appearanceTimeStartArray; }
    public static ArrayList<EditText> getAppearanceTimeEndArray() {return appearanceTimeEndArray;}
    public static ArrayList<EditText> getQuadrantNumberArray() {
        return quadrantNumberArray;
    }
    public static ArrayList<Integer> getIndexArray() {return indexArray;}

}