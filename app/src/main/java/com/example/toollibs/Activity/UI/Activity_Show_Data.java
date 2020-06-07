package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.toollibs.Activity.Fragment.Fragment_GridView;
import com.example.toollibs.Activity.Fragment.Fragment_ListView;
import com.example.toollibs.Activity.Fragment.Fragment_Recycle_View;
import com.example.toollibs.R;

public class Activity_Show_Data extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private FragmentTransaction transaction;
    private Fragment_ListView fragmentListView;
    private Fragment_GridView fragmentGridView;
    private Fragment_Recycle_View fragmentRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__show__data);

        init();
        setData();
        setListener();
    }

    private void init() {
        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radio_listView);
    }

    private void setData(){

    }

    private void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(transaction);

                switch (i){
                    case R.id.radio_listView:
                        if(fragmentListView==null){
                            fragmentListView = new Fragment_ListView();
                            transaction.add(R.id.fragment_container, fragmentListView);
                        }else{
                            transaction.show(fragmentListView);
                        }
                        break;
                    case R.id.radio_GridView:
                        if(fragmentGridView==null){
                            fragmentGridView = new Fragment_GridView();
                            transaction.add(R.id.fragment_container, fragmentGridView);
                        }else{
                            transaction.show(fragmentGridView);
                        }
                        break;
                    case R.id.radio_recycleView:
                        if(fragmentRecycleView==null){
                            fragmentRecycleView = new Fragment_Recycle_View();
                            transaction.add(R.id.fragment_container, fragmentRecycleView);
                        }else{
                            transaction.show(fragmentRecycleView);
                        }
                        break;
                }
                transaction.commit();
            }
        });

        radioButton.setChecked(true);
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if(fragmentListView!=null){
            transaction.hide(fragmentListView);
            //transaction.remove(fragment_mine);
        }
        if(fragmentGridView!=null){
            transaction.hide(fragmentGridView);
        }
        if(fragmentRecycleView!=null){
            transaction.hide(fragmentRecycleView);
        }
    }
}
