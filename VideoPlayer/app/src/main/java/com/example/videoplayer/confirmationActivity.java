package com.example.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class confirmationActivity extends AppCompatActivity {

    int featureRowIndex = 1;

    private ArrayList<EditText> productNameArray = MainActivity.getProductNameArray();
    private ArrayList<EditText> eCommerceInfoArray = MainActivity.geteCommerceInfoArray();
    private ArrayList<EditText> appearanceTimeStartArray = MainActivity.getAppearanceTimeStartArray();
    private ArrayList<EditText> appearanceTimeEndArray = MainActivity.getAppearanceTimeEndArray();
    private ArrayList<EditText> quadrantNumberArray = MainActivity.getQuadrantNumberArray();
    private ArrayList<Button> removeButtonArray;
    private ArrayList<TextView> textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        int j = 0;
        for (EditText editText : productNameArray) {
            Log.e(String.valueOf(j), editText.getText().toString());
            j++;
        }



        final TextView feature1 = findViewById(R.id.tv_feature);
        feature1.setText(productNameArray.get(0).getText().toString() + eCommerceInfoArray.get(0).getText().toString() +
                appearanceTimeStartArray.get(0).getText().toString() + appearanceTimeEndArray.get(0).getText().toString() +quadrantNumberArray.get(0).getText().toString());

        //textViewArray.add(feature1);

        Button removeButton = findViewById(R.id.buttonRemove);
        //removeButtonArray.add(removeButton);



        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout confirmationPage = findViewById(R.id.confirmation_page);
                final LinearLayout firstList = confirmationPage.findViewById(R.id.firstList);
                confirmationPage.removeView(firstList);
                productNameArray.remove(0);
                appearanceTimeStartArray.remove(0);
                appearanceTimeEndArray.remove(0);
                quadrantNumberArray.remove(0);
                eCommerceInfoArray.remove(0);

                int j = 0;
                for (EditText editText : productNameArray) {
                    Log.e(String.valueOf(j), editText.getText().toString());
                    j++;
                }
            }
        });



        for (int i = featureRowIndex; i < productNameArray.size(); i++){
            final int finalI = i;

            final LinearLayout confirmationPage = findViewById(R.id.confirmation_page);
            LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View newRowView2 = inflater2.inflate(R.layout.add_confirmation, null);
            confirmationPage.addView(newRowView2, featureRowIndex);

            View view2 = confirmationPage.getChildAt(featureRowIndex);

            TextView addedFeature = (TextView) view2.findViewById(R.id.added_feature);
            addedFeature.setText(productNameArray.get(i).getText().toString() + eCommerceInfoArray.get(i).getText().toString() +
                    appearanceTimeStartArray.get(i).getText().toString() + appearanceTimeEndArray.get(i).getText().toString() +quadrantNumberArray.get(i).getText().toString());


            Button addedButton = (Button) view2.findViewById(R.id.added_buttonRemove);
            addedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("Index", String.valueOf(finalI));
                    Log.e("Size of string", String.valueOf(productNameArray.size()));

                    confirmationPage.removeView(newRowView2);
                    productNameArray.remove(finalI);
                    appearanceTimeStartArray.remove(finalI);
                    appearanceTimeEndArray.remove(finalI);
                    quadrantNumberArray.remove(finalI);
                    eCommerceInfoArray.remove(finalI);

                    //IF THE ORIGINAL REMOVEBUTTON IS PRESSED, finalI = finalI - 1.
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
    }

}
