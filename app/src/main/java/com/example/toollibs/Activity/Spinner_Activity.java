package com.example.toollibs.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Spinner_Activity extends AppCompatActivity {
    private Spinner spinner1, spinner2;
    private String data1;
    private int data2;
    private String[] strArray;
    private List<Object> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        init();
        setData();
        setListener();
    }

    private void init() {
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
    }


    private void setData() {
        strArray = getResources().getStringArray(R.array.Date);
        int[] intArray = getResources().getIntArray(R.array.google_colors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list = Arrays.stream(intArray).boxed().collect(Collectors.toList());
        }

        //set default value
        data1 = strArray[0];
        data2 = intArray[0];

        //set adapter
        ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strArray);
        strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(strAdapter);

        ArrayAdapter<Object> intAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        intAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(intAdapter);
    }

    private void setListener(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                data1 = strArray[i];
                SystemUtil.ShowToast(getApplicationContext(),data1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data2 = (int) list.get(i);
                SystemUtil.ShowToast(getApplicationContext(), data2+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
