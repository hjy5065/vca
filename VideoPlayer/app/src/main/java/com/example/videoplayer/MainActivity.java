package com.example.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<File> fileArrayList = new ArrayList<>();
    private static ArrayList<EditText> productNameArray = new ArrayList<EditText>();
    private static ArrayList<EditText> eCommerceInfoArray = new ArrayList<EditText>();
    private static ArrayList<EditText> appearanceTimeStartArray = new ArrayList<EditText>();
    private static ArrayList<EditText> appearanceTimeEndArray = new ArrayList<EditText>();
    private static ArrayList<EditText> quadrantNumberArray = new ArrayList<EditText>();
    private static ArrayList<Integer> indexArray = new ArrayList<Integer>();


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
        indexArray.add(productNameArray.size()-1);
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

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View newRowView = inflater.inflate(R.layout.add_feature, null);
                linearLayout.addView(newRowView, timeRowIndex);

                View view = linearLayout.getChildAt(timeRowIndex);

                EditText addedName = (EditText) view.findViewById(R.id.added_name);
                productNameArray.add(addedName);

                EditText addedLink = (EditText) view.findViewById(R.id.added_link);
                eCommerceInfoArray.add(addedLink);

                EditText addedTimeStart = (EditText) view.findViewById(R.id.added_time_start);
                appearanceTimeStartArray.add(addedTimeStart);
                indexArray.add(productNameArray.size()-1);

                EditText addedTimeEnd = (EditText) view.findViewById(R.id.added_time_end);
                appearanceTimeEndArray.add(addedTimeEnd);

                EditText addedQuadrantNumber = (EditText) view.findViewById(R.id.added_loc);
                quadrantNumberArray.add(addedQuadrantNumber);

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
                indexArray.add(productNameArray.size()-1);

                EditText appearanceTimeEnd = (EditText) view.findViewById(R.id.editText_added_time_end);
                appearanceTimeEndArray.add(appearanceTimeEnd);

                EditText quadrantNumber = (EditText) view.findViewById(R.id.editText_added_loc);
                quadrantNumberArray.add(quadrantNumber);

            }

        });

        final Button confirmButton = findViewById(R.id.button_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean next = true;
                for (int i = 0; i < productNameArray.size(); i++){
                    if (productNameArray.get(i).getText().toString().equals("") ||
                            eCommerceInfoArray.get(i).getText().toString().equals("") ||
                            appearanceTimeStartArray.get(i).getText().toString().equals("") ||
                            appearanceTimeEndArray.get(i).getText().toString().equals("") ||
                            quadrantNumberArray.get(i).getText().toString().equals("")){
                        next = false;
                        break;
                    }
                }
                if (next){
                    Intent intent = new Intent(MainActivity.this, confirmationActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please log all required information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public static ArrayList<EditText> getProductNameArray(){ return productNameArray; }
    public static ArrayList<EditText> geteCommerceInfoArray() { return eCommerceInfoArray; }
    public static ArrayList<EditText> getAppearanceTimeStartArray() { return appearanceTimeStartArray; }
    public static ArrayList<EditText> getAppearanceTimeEndArray() {return appearanceTimeEndArray;}
    public static ArrayList<EditText> getQuadrantNumberArray() { return quadrantNumberArray; }
    public static ArrayList<Integer> getIndexArray() {return indexArray;}

}