package com.example.videoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;

public class ConfirmationActivity extends AppCompatActivity {

    private static ArrayList<EditText> productNameArray = MainActivity.getProductNameArray();
    private static ArrayList<EditText> eCommerceInfoArray = MainActivity.geteCommerceInfoArray();
    private static ArrayList<EditText> appearanceTimeStartArray = MainActivity.getAppearanceTimeStartArray();
    private static ArrayList<EditText> appearanceTimeEndArray = MainActivity.getAppearanceTimeEndArray();
    private static ArrayList<EditText> quadrantNumberArray = MainActivity.getQuadrantNumberArray();
    private static ArrayList<Integer> indexArray = MainActivity.getIndexArray();
    private static ArrayList<Integer> removedIndices = new ArrayList<Integer>();

    public static int REQUEST_PERMISSION = 1;
    boolean boolean_permission;

    private int GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        final LinearLayout confirmationPage = findViewById(R.id.confirmation_page);

        //featureRowIndex is index for each textViews made -- aka productArray index.
        int featureRowIndex = confirmationPage.getChildCount();
        //indexForIndex is index for each time and quadrant -- aka indexArray index. But both indices start at the same spot - 0, or childCount.
        int indexForIndex = featureRowIndex;


        //Add logged information to confirmation page
        for (int i = featureRowIndex; i < productNameArray.size(); i++){
            final int finalI = i;

            LayoutInflater confirmLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View newRowView2 = confirmLayout.inflate(R.layout.add_confirmation, null);
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

            //Append additionally logged times to the confirmation page
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

            //Remove information about the removed product from all arrays
            Button removeButton = (Button) view2.findViewById(R.id.added_buttonRemove);
            removeButton.setOnClickListener(new View.OnClickListener() {
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

                    confirmationPage.removeView(newRowView2);

                    productNameArray.remove(newI);
                    eCommerceInfoArray.remove(newI);

                    int indexNo = indexArray.indexOf(newI);

                    if (indexNo+1 < indexArray.size()){
                        while (indexArray.get(indexNo).equals(indexArray.get(indexNo+1))) {
                            appearanceTimeStartArray.remove(indexNo);
                            appearanceTimeEndArray.remove(indexNo);
                            quadrantNumberArray.remove(indexNo);
                            indexArray.remove(indexNo);
                        }
                    }
                    appearanceTimeStartArray.remove(indexNo);
                    appearanceTimeEndArray.remove(indexNo);
                    quadrantNumberArray.remove(indexNo);
                    indexArray.remove(indexNo);

                    for (int j = indexNo; j < indexArray.size(); j++){
                        indexArray.set(j, indexArray.get(j)-1);
                    }


                    removedIndices.add(finalI);

                }
            });

            featureRowIndex++;
        }

        //Show the add_more and upload button
        final LinearLayout linearLayout = findViewById(R.id.confirmation_page);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRowView = inflater.inflate(R.layout.upload_back, null);
        linearLayout.addView(newRowView, featureRowIndex);

        //At add more, start the product log activity.
        final Button addMore = findViewById(R.id.button_back);
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
                startActivity(intent);            }
        });

        //At upload, get permission to access media on phone.
        final Button uploadVideoButton = findViewById(R.id.buttonUpload);
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                permissionForVideo();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result",""+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what","cancle");
            return;
        }
        Log.d("what","gale");
        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {
                Toast.makeText(this, "Please choose a video file", Toast.LENGTH_SHORT).show();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            } else if (selectedMediaUri.toString().contains("video")) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    Intent intent = new Intent(ConfirmationActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("URI", contentURI);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }
            }
        }
    }

    private void permissionForVideo() {
        //if not granted
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            //If I should show UI with rationale for requesting a permission
            if((ActivityCompat.shouldShowRequestPermissionRationale(ConfirmationActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){
            }
            else{
                ActivityCompat.requestPermissions(ConfirmationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
        //if permission granted, show list of videos on phone.
        else{
            boolean_permission = true;
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                boolean_permission = true;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);

            }

            else{
                showDetails();
            }


        }
    }

    public void showDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationActivity.this);
        builder.setTitle("Storage Write Permission")
                .setMessage("This permission is necessary to access the videos on this device.")
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    //When the user ok's the alert, onClick function is called.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(ConfirmationActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
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

}
