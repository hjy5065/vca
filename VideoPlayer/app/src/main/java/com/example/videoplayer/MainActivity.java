package com.example.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

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

        //store editText values in arraylists
        final EditText productName = findViewById(R.id.editText_name);
        productNameArray.add(productName);
        final EditText eCommerceInfo = findViewById(R.id.editText_link);

        eCommerceInfo.setText("http://");
        Selection.setSelection(eCommerceInfo.getText(), eCommerceInfo.getText().length());
        eCommerceInfo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("http://")){
                    eCommerceInfo.setText("http://");
                    Selection.setSelection(eCommerceInfo.getText(), eCommerceInfo.getText().length());

                }

            }
        });

        eCommerceInfoArray.add(eCommerceInfo);

        final EditText appearanceTimeStart1 = findViewById(R.id.editText_time_start);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(5);
        appearanceTimeStart1.setFilters(filterArray);

        appearanceTimeStart1.addTextChangedListener(new TextWatcher() {
            int first = 0;
            int second;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable text) {
                second = first;
                first = text.length();
                if(appearanceTimeStart1.length()==2 && first>second){
                    appearanceTimeStart1.append(":");
                }
            }
        });

        appearanceTimeStartArray.add(appearanceTimeStart1);
        indexArray.add(productNameArray.size()-1);
        final EditText appearanceTimeEnd1 = findViewById(R.id.editText_time_end);

        appearanceTimeEnd1.addTextChangedListener(new TextWatcher() {
            int first = 0;
            int second;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable text) {
                second = first;
                first = text.length();
                if(appearanceTimeEnd1.length()==2 && first>second){
                    appearanceTimeEnd1.append(":");
                }
            }
        });

        appearanceTimeEndArray.add(appearanceTimeEnd1);
        final EditText quadrantNumber1 = findViewById(R.id.editText_location);
        quadrantNumberArray.add(quadrantNumber1);


        //at Add Feature, dynamically produce more editTexts.
        final Button addFeaturesButton = findViewById(R.id.button_add);

        addFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout logLayout = findViewById(R.id.linearLayoutInfo);

                int timeRowIndex = logLayout.getChildCount();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View newRowView = inflater.inflate(R.layout.add_feature, null);
                logLayout.addView(newRowView, timeRowIndex);

                View view = logLayout.getChildAt(timeRowIndex);

                EditText addedName = (EditText) view.findViewById(R.id.added_name);
                productNameArray.add(addedName);

                final EditText addedLink = (EditText) view.findViewById(R.id.added_link);

                addedLink.setText("http://");
                Selection.setSelection(addedLink.getText(), addedLink.getText().length());
                addedLink.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().startsWith("http://")){
                            addedLink.setText("http://");
                            Selection.setSelection(addedLink.getText(), addedLink.getText().length());

                        }

                    }
                });

                eCommerceInfoArray.add(addedLink);

                final EditText addedTimeStart = (EditText) view.findViewById(R.id.added_time_start);

                addedTimeStart.addTextChangedListener(new TextWatcher() {
                    int first = 0;
                    int second;
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void afterTextChanged(Editable text) {
                        second = first;
                        first = text.length();
                        if(addedTimeStart.length()==2 && first>second){
                            addedTimeStart.append(":");
                        }
                    }
                });

                appearanceTimeStartArray.add(addedTimeStart);
                indexArray.add(productNameArray.size()-1);

                final EditText addedTimeEnd = (EditText) view.findViewById(R.id.added_time_end);

                addedTimeEnd.addTextChangedListener(new TextWatcher() {
                    int first = 0;
                    int second;
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void afterTextChanged(Editable text) {
                        second = first;
                        first = text.length();
                        if(addedTimeEnd.length()==2 && first>second){
                            addedTimeEnd.append(":");
                        }
                    }
                });

                appearanceTimeEndArray.add(addedTimeEnd);

                EditText addedQuadrantNumber = (EditText) view.findViewById(R.id.added_loc);
                quadrantNumberArray.add(addedQuadrantNumber);

            }
        });

        //at Add times, dynamically produce more editTexts.
        final Button addButton = findViewById(R.id.button_add_times);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final LinearLayout logLayout = findViewById(R.id.linearLayoutInfo);

                int timeRowIndex = logLayout.getChildCount();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View newRowView = inflater.inflate(R.layout.add_time, null);
                logLayout.addView(newRowView, timeRowIndex);

                View view = logLayout.getChildAt(timeRowIndex);

                EditText appearanceTimeStart = (EditText) view.findViewById(R.id.editText_added_time_start);
                appearanceTimeStartArray.add(appearanceTimeStart);
                indexArray.add(productNameArray.size()-1);

                EditText appearanceTimeEnd = (EditText) view.findViewById(R.id.editText_added_time_end);
                appearanceTimeEndArray.add(appearanceTimeEnd);

                EditText quadrantNumber = (EditText) view.findViewById(R.id.editText_added_loc);
                quadrantNumberArray.add(quadrantNumber);

            }

        });

        //At confirm, if an editText is left blank, show a toast message.
        //If all editTexts are filled in, load confirmation page.
        final Button confirmButton = findViewById(R.id.button_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean next = true;
                for (int i = 0; i < productNameArray.size(); i++){
                    if (productNameArray.get(i).getText().toString().equals("") ||
                            eCommerceInfoArray.get(i).getText().toString().substring(7).equals("") ||
                            appearanceTimeStartArray.get(i).getText().toString().equals("") ||
                            appearanceTimeEndArray.get(i).getText().toString().equals("") ||
                            quadrantNumberArray.get(i).getText().toString().equals("")){
                        next = false;
                        break;
                    }
                }
                if (next){
                    Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please log all required information", Toast.LENGTH_SHORT).show();
                }

                /*
                if (true){
                    for (EditText j: appearanceTimeStartArray){
                        String hour = j.getText().toString();
                        if (hour.indexOf(":") != -1){
                            hour = hour.substring(0, hour.indexOf(":"));
                        }
                        if (Integer.parseInt(hour) > 59){
                            Toast.makeText(MainActivity.this, "Current timestamp available is limited to 59:59", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (EditText k: appearanceTimeEndArray){
                        String min = k.getText().toString();
                        if (min.indexOf(":") != -1){
                            min = min.substring(min.indexOf(":"));
                        }
                        if (Integer.parseInt(min) > 59){
                            Toast.makeText(MainActivity.this, "The biggest minute timestamp is 59", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                */
            }


        });
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    //Array getters
    public static ArrayList<EditText> getProductNameArray(){ return productNameArray; }
    public static ArrayList<EditText> geteCommerceInfoArray() { return eCommerceInfoArray; }
    public static ArrayList<EditText> getAppearanceTimeStartArray() { return appearanceTimeStartArray; }
    public static ArrayList<EditText> getAppearanceTimeEndArray() {return appearanceTimeEndArray;}
    public static ArrayList<EditText> getQuadrantNumberArray() { return quadrantNumberArray; }
    public static ArrayList<Integer> getIndexArray() {return indexArray;}

}