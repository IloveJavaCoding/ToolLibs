package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.toollibs.Activity.Bean.Students;
import com.example.toollibs.Activity.DataBase.DBHelper;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.TimeSelector;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.SystemUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_DataBase extends AppCompatActivity {
    private EditText etId, etName, etBirth;
    private Spinner sGender, sGrade;
    private Button bAdd;
    private TextView tvData;

    private DBHelper dbHelper;
    private String gender, grade;
    private int age;
    private List<Students> list;

    private final String minTime = "2000-01-01 01:01";
    private final String[] genders = {"男", "女"};
    private final String[] grades = {"一年级", "二年级", "三年级", "四年级", "五年级"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__data_base);

        init();
        setData();
        setListener();
    }

    private void init() {
        Log.d("tag", "...get in...");
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etBirth = findViewById(R.id.etBirth);

        sGender = findViewById(R.id.sGender);
        sGrade = findViewById(R.id.sGrade);
        bAdd = findViewById(R.id.bAdd);

        tvData = findViewById(R.id.tvData);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        getDataList();
    }

    private void setData() {
        gender = genders[0];
        grade = grades[0];

        ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGender.setAdapter(strAdapter);

        ArrayAdapter<String> strAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        strAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGrade.setAdapter(strAdapter2);
    }

    private void getDataList(){
        list = dbHelper.getAllStudents();
        tvData.setText(list.size()>0?getContent(list):"The data base is empty!");
    }

    private String getContent(List<Students> list) {
        StringBuilder builder = new StringBuilder("*");
        for(int i=0; i<list.size(); i++){
            builder.append("Id: ");
            builder.append(list.get(i).getId()+"\n");
            builder.append("StudentId: ");
            builder.append(list.get(i).getStudentId()+"\n");
            builder.append("Name: ");
            builder.append(list.get(i).getName()+"\n");
            builder.append("Gender: ");
            builder.append(list.get(i).getGender()+"\n");
            builder.append("Age: ");
            builder.append(list.get(i).getAge()+"\n");
            builder.append("Grade: ");
            builder.append(grades[list.get(i).getGrade()-1]+"\n");
            builder.append("\n");
        }

        return builder.toString();
    }

    private void setListener() {
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judge()){
                    Students students = new Students();
                    students.setStudentId(etId.getText().toString().trim());
                    students.setName(etName.getText().toString().trim());
                    students.setAge(age);
                    students.setGender(gender);
                    students.setGrade(getInt());

                    if(dbHelper.addStudent(students)){
                        getDataList();
                        SystemUtil.showToast(getApplicationContext(), "Add successful.");
                        clear();
                    }else{
                        SystemUtil.showToast(getApplicationContext(), "Add fail!!!");
                    }
                }
            }
        });

        etBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector();
            }
        });

        sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                gender = genders[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                grade = grades[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void clear() {
        etId.setText("");
        etName.setText("");
        etBirth.setText("");
    }

    private void timeSelector(){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(Date time) {
                age = DateUtil.getAge(time);
                etBirth.setText(DateUtil.date2String(time, "yyyy-MM-dd"));
            }
        }, minTime, DateUtil.date2String(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm"));
        timeSelector.setMode(TimeSelector.MODE.YMD);
        timeSelector.show();
        timeSelector.setIsLoop(false);
    }

    private int getInt() {
        for(int i=0; i<grade.length(); i++){
            if(grade.equals(grades[i])){
                return i+1;
            }
        }
        return -1;
    }

    private boolean judge() {
        String id = etId.getText().toString().trim();
        if(id==null){
            etId.setError("Empty!!!");
            return false;
        }

        if(id.length()!=10){
            etId.setError("length=10");
            return false;
        }

        if(etName.getText()==null){
            etName.setError("Empty!!!");
            return false;
        }

        return true;
    }
}
