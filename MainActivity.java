package com.example.videoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView)findViewById(R.id.listVideoRecyler);

        //Phone memory and SD card
        directory = new File("/mnt/");

        //directory = new File("/storage/");

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        myRecyclerView.setLayoutManager(manager);

        permissionForVideo();

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
}
