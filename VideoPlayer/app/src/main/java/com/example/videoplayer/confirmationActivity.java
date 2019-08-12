package com.example.videoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;
import static com.example.videoplayer.MainActivity.fileArrayList;

public class confirmationActivity extends AppCompatActivity {

    private ArrayList<Integer> removedIndices = new ArrayList<Integer>();

    private static ArrayList<EditText> productNameArray = MainActivity.getProductNameArray();
    private static ArrayList<EditText> eCommerceInfoArray = MainActivity.geteCommerceInfoArray();
    private static ArrayList<EditText> appearanceTimeStartArray = MainActivity.getAppearanceTimeStartArray();
    private static ArrayList<EditText> appearanceTimeEndArray = MainActivity.getAppearanceTimeEndArray();
    private static ArrayList<EditText> quadrantNumberArray = MainActivity.getQuadrantNumberArray();
    private static ArrayList<Integer> indexArray = MainActivity.getIndexArray();

    RecyclerView myRecyclerView;
    MyAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    File directory;
    boolean boolean_permission;

    int indexForIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        final LinearLayout confirmationPage = findViewById(R.id.confirmation_page);

        //featureRowIndex is index for each textViews made -- aka productArray index.
        int featureRowIndex = confirmationPage.getChildCount();
        //indexForIndex is index for each time and quadrant -- aka indexArray index. But both indices start at the same spot - 0, or childCount.
        indexForIndex = featureRowIndex;

        printLog();

        for (int i = featureRowIndex; i < productNameArray.size(); i++){
            Log.e("featureRowIndex", String.valueOf(featureRowIndex));
            Log.e("indexForindex", String.valueOf(indexForIndex));

            final int finalI = i;
            Log.e("finalI", String.valueOf(finalI));

            LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View newRowView2 = inflater2.inflate(R.layout.add_confirmation, null);
            confirmationPage.addView(newRowView2, i);

            View view2 = confirmationPage.getChildAt(i);

            TextView addedFeature = (TextView) view2.findViewById(R.id.added_feature);

            addedFeature.setText("Product: " + productNameArray.get(i).getText().toString());
            if (eCommerceInfoArray.get(i).getText().toString().length() > 30){
                addedFeature.append("\nLink: " + eCommerceInfoArray.get(i).getText().toString().substring(0, 30) + "...");
            }
            else{
                addedFeature.append("\nLink: " + eCommerceInfoArray.get(i).getText().toString());
            }
            addedFeature.append("\n" + "Location: " + quadrantNumberArray.get(indexForIndex).getText().toString() +
                    ", Times: " + appearanceTimeStartArray.get(indexForIndex).getText().toString() + " - " + appearanceTimeEndArray.get(indexForIndex).getText().toString());

            if (indexForIndex+1 < indexArray.size()){
                while (indexArray.get(indexForIndex).equals(indexArray.get(indexForIndex+1))){
                    addedFeature.append("\nLocation: " + quadrantNumberArray.get(indexForIndex+1).getText().toString() +
                            ", Times: " + appearanceTimeStartArray.get(indexForIndex+1).getText().toString() +
                            " - " + appearanceTimeEndArray.get(indexForIndex+1).getText().toString());
                    indexForIndex++;
                    if ((indexForIndex+1) == (indexArray.size())){
                        break;
                    }
                }
                indexForIndex++;
            }


            Button addedButton = (Button) view2.findViewById(R.id.added_buttonRemove);
            addedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newI = finalI;

                    if (!removedIndices.isEmpty()){
                        for (int k : removedIndices) {
                            if (k < finalI) {
                                newI--;
                            }
                        }
                    }
                    Log.e("newI", String.valueOf(newI));


                    confirmationPage.removeView(newRowView2);

                    productNameArray.remove(newI);
                    eCommerceInfoArray.remove(newI);

                    //What is being removed is item of newI, therefore find the value newI in indexArray and get THAT value's index to get
                    //the indexNo for times.
                    int indexNo = indexArray.indexOf(newI);
                    Log.e("indexNo", String.valueOf(indexNo));



                    if (indexNo+1 < indexArray.size()){
                        while (indexArray.get(indexNo).equals(indexArray.get(indexNo+1))) {
                            appearanceTimeStartArray.remove(indexNo);
                            appearanceTimeEndArray.remove(indexNo);
                            indexArray.remove(indexNo);
                        }
                    }
                    appearanceTimeStartArray.remove(indexNo);
                    appearanceTimeEndArray.remove(indexNo);
                    indexArray.remove(indexNo);

                    for (int j = indexNo; j < indexArray.size(); j++){
                        indexArray.set(j, indexArray.get(j)-1);
                    }


                    removedIndices.add(finalI);

                    printLog();
                }
            });

            /*
            removeButtonArray.add(addedButton); //the two arrays' indices correspond with featureRowIndex value
            textViewArray.add(addedFeature);
            */
            featureRowIndex++;

        }


        final LinearLayout linearLayout = findViewById(R.id.confirmation_page);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRowView = inflater.inflate(R.layout.upload_back, null);
        linearLayout.addView(newRowView, featureRowIndex);

        Button backPress = findViewById(R.id.button_back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(confirmationActivity.this, MainActivity.class);
                startActivity(intent);            }
        });




        final Button uploadVideoButton = findViewById(R.id.buttonUpload);
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main);

                myRecyclerView = (RecyclerView)findViewById(R.id.listVideoRecyler);

                //Phone memory and SD card
                directory = new File("/mnt/");

                //directory = new File("/storage/");

                GridLayoutManager manager = new GridLayoutManager(confirmationActivity.this, 2);
                myRecyclerView.setLayoutManager(manager);

                permissionForVideo();
            }

        });
    }

    private void permissionForVideo() {
        //if not granted
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            //If I should show UI with rationale for requesting a permission
            if((ActivityCompat.shouldShowRequestPermissionRationale(confirmationActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){

            }
            //If I should now show UI with rationale
            else{
                ActivityCompat.requestPermissions(confirmationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
        AlertDialog.Builder builder = new AlertDialog.Builder(confirmationActivity.this);
        builder.setTitle("Storage Write Permission")
                .setMessage("This permission is necessary to access the videos on this device.")
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    //When the user ok's the alert, onClick function is called.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(confirmationActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
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

    public static ArrayList<String> getProductNameArray(){
        ArrayList<String> productNames = new ArrayList<String>();

        for (EditText element : productNameArray){
            productNames.add(element.getText().toString());
        }
        return productNames;
    }

    public static ArrayList<String> geteCommerceInfoArray() {
        ArrayList<String> info = new ArrayList<String>();

        for (EditText element : eCommerceInfoArray){
            info.add(element.getText().toString());
        }
        return info;

    }

    public static ArrayList<Integer> getAppearanceTimeStartArray() {
        ArrayList<Integer> startTimes = new ArrayList<Integer>();

        for (EditText element : appearanceTimeStartArray){
            startTimes.add(convertTime(element.getText().toString())*1000);
        }
        return startTimes;
    }
    public static ArrayList<Integer> getAppearanceTimeEndArray() {
        ArrayList<Integer> endTimes = new ArrayList<Integer>();

        for (EditText element : appearanceTimeEndArray){
            endTimes.add(convertTime(element.getText().toString())*1000);
        }
        return endTimes;
    }
    public static ArrayList<Integer> getQuadrantNumberArray() {
        ArrayList<Integer> locations = new ArrayList<Integer>();

        for (EditText element : quadrantNumberArray){
            locations.add(Integer.parseInt(element.getText().toString()));
        }
        return locations;
    }
    public static ArrayList<Integer> getIndexArray() {return indexArray;}

    public static int convertTime(String time){
        String[] units = time.split(":");
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        return 60 * minutes + seconds; //add up values

    }

    private void printLog(){
        int j = 0;
        for (EditText product : productNameArray) {
            Log.e("Product " + String.valueOf(j), product.getText().toString());
            j++;
        }

        int k = 0;
        for (EditText info : eCommerceInfoArray) {
            Log.e("Info " + String.valueOf(k), info.getText().toString());
            k++;
        }

        int l = 0;
        for (EditText startTime : appearanceTimeStartArray) {
            Log.e("Start time " + String.valueOf(l), String.valueOf(startTime.getText().toString()));
            l++;
        }

        int r = 0;
        for (EditText endTime : appearanceTimeEndArray) {
            Log.e("End time " + String.valueOf(r), String.valueOf(endTime.getText().toString()));
            r++;
        }

        int q = 0;
        for (EditText loc : quadrantNumberArray) {
            Log.e("Location " + String.valueOf(q), String.valueOf(loc.getText().toString()));
            q++;
        }

        int g = 0;
        for (int in : indexArray){
            Log.e("Index " + String.valueOf(g), String.valueOf(in));
            g++;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(confirmationActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
