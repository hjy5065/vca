package com.example.videoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_log2);

        final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);


        final EditText productName = findViewById(R.id.editText_name);
        productNameArray.add(productName);
        final EditText eCommerceInfo = findViewById(R.id.editText_link);
        eCommerceInfoArray.add(eCommerceInfo);
        final EditText appearanceTimeStart1 = findViewById(R.id.editText_time_start);
        appearanceTimeStartArray.add(appearanceTimeStart1);
        final EditText appearanceTimeEnd1 = findViewById(R.id.editText_time_end);
        appearanceTimeEndArray.add(appearanceTimeEnd1);
        final EditText quadrantNumber1 = findViewById(R.id.editText_location);
        quadrantNumberArray.add(quadrantNumber1);


        final Button addButton = findViewById(R.id.button_add_times);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                //Create EditText for appearance time
                final EditText appearanceTimeStart = new EditText(MainActivity.this);
                appearanceTimeStart.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_DATETIME_VARIATION_TIME);
                appearanceTimeStart.setHint(R.string.time_hint_start);
                appearanceTimeStart.setId(appearanceTimeStart.generateViewId());


                final EditText appearanceTimeEnd = new EditText(MainActivity.this);
                appearanceTimeEnd.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_DATETIME_VARIATION_TIME);
                appearanceTimeEnd.setHint(R.string.time_hint_end);
                appearanceTimeEnd.setId(appearanceTimeEnd.generateViewId());


                constraintLayout.addView(appearanceTimeStart, 0);
                constraintLayout.addView(appearanceTimeEnd, 0);


                ConstraintSet set = new ConstraintSet();
                set.clone(constraintLayout);


                set.connect(appearanceTimeStart.getId(), ConstraintSet.TOP, R.id.editText_time_start, ConstraintSet.BOTTOM, 16);
                set.connect(appearanceTimeEnd.getId(), ConstraintSet.TOP, R.id.editText_time_end, ConstraintSet.BOTTOM, 16);
                set.connect(appearanceTimeEnd.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16);
                set.connect(appearanceTimeStart.getId(), ConstraintSet.END, appearanceTimeEnd.getId(), ConstraintSet.START, 8);


                set.applyTo(constraintLayout);

                /*
                appearanceTime.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_DATETIME_VARIATION_TIME);
                //appearanceTime.setHint(R.string.time_of_appearance);
                appearanceTime.setLayoutParams(new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
                appearanceTime.setPadding(20, 20, 20, 20);
                appearanceTimeArray.add(appearanceTime);

                //Create EditText for quadrant number
                final EditText quadrantNumber = new EditText(MainActivity.this);
                quadrantNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                quadrantNumber.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                quadrantNumber.setPadding(20, 20, 20, 20);
                quadrantNumberArray.add(quadrantNumber);

                //Add EditText to LinearLayout
                linearLayoutInfo.addView(appearanceTime);
                linearLayoutInfo.addView(quadrantNumber);

                */

            }

        });

        //Originally uploadvideobutton, so soon to replace R.id.button_confirm to button_upload
        final Button uploadVideoButton = findViewById(R.id.button_confirm);
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


        /* Process when selecting video
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView)findViewById(R.id.listVideoRecyler);

        //Phone memory and SD card
        directory = new File("/mnt/");

        //directory = new File("/storage/");

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        myRecyclerView.setLayoutManager(manager);

        permissionForVideo();
        */
    }

    private void permissionForVideo() {
        //if not granted
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            //If I should show UI with rationale for requesting a permission
            if((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){

            }
            //If I should now show UI with rationale
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
        //if permission granted
        else{
            boolean_permission = true;
            getFile(directory);
            obj_adapter = new MyAdapter(getApplicationContext(), fileArrayList);
            myRecyclerView.setAdapter(obj_adapter);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                boolean_permission = true;
                getFile(directory);
                obj_adapter = new MyAdapter(getApplicationContext(), fileArrayList);
                myRecyclerView.setAdapter(obj_adapter);
            }

            else{
                showDetails();
            }


        }
    }

    public void showDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Storage Write Permission")
                .setMessage("This permission is necessary to access the videos on this device.")
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    //When the user ok's the alert, onClick function is called.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
                        }

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
            //When the user cancels the alert, onClick function is called.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    public ArrayList<File> getFile(File directory){

        File listFile[] = directory.listFiles();
        if(listFile!=null && listFile.length>0){

            for(int i = 0; i<listFile.length; i++){
                //if listFile[0] is a directory, call a recursive getFile to get all files from that directory
                if(listFile[i].isDirectory()){
                    getFile(listFile[i]);
                }
                else{
                    boolean_permission = false;
                    //can or with whatever type of video file we want, for now I just or'ed with a .m4v to test it out
                    if(listFile[i].getName().endsWith(".mp4") || listFile[i].getName().endsWith(".m4v")){
                        //if file already in array, move on. If file not in array, add.
                        for(int j=0; j<fileArrayList.size();j++){
                            //go through all files in fileArrayList
                            //if the first file in listFile (media list) matches any of the files found in fileArrayList, set boolean = true.
                            if(fileArrayList.get(j).getName().equals(listFile[i].getName())){
                                boolean_permission = true;
                            }else {


                            }

                        }

                        if(boolean_permission){
                            boolean_permission = false;
                        }
                        else{
                            fileArrayList.add(listFile[i]);
                        }

                    }
                }

            }

        }

        return fileArrayList;

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

}
