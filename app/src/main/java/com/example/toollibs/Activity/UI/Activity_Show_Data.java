package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.toollibs.Activity.Fragment.Fragment_GridView;
import com.example.toollibs.Activity.Fragment.Fragment_ListView;
import com.example.toollibs.Activity.Fragment.Fragment_Recycle_View;
import com.example.toollibs.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Show_Data extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private List<Fragment> views;

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

        fragmentListView = new Fragment_ListView();
        fragmentGridView = new Fragment_GridView();
        fragmentRecycleView = new Fragment_Recycle_View();
        views = new ArrayList<>();
        views.add(fragmentListView);
        views.add(fragmentGridView);
        views.add(fragmentRecycleView);
    }

    private void setData(){
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return views.get(i);
            }

            @Override
            public int getCount() {
                return views.size();
            }
        });

        viewPager.setCurrentItem(0);
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                transaction = getSupportFragmentManager().beginTransaction();
//                hideAllFragment(transaction);
                switch (i){
                    case R.id.radio_listView:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.radio_GridView:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.radio_recycleView:
                        viewPager.setCurrentItem(2);
                        break;
                }
                //transaction.commit();
            }
        });
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

    private void showListFragment(){
        if(fragmentListView==null){
            fragmentListView = new Fragment_ListView();
            transaction.add(R.id.fragment_container, fragmentListView);
        }else{
            transaction.show(fragmentListView);
        }
    }

    private void showGridFragment(){
        if(fragmentGridView==null){
            fragmentGridView = new Fragment_GridView();
            transaction.add(R.id.fragment_container, fragmentGridView);
        }else{
            transaction.show(fragmentGridView);
        }
    }

    private void showRecyclerFragment(){
        if(fragmentRecycleView==null){
            fragmentRecycleView = new Fragment_Recycle_View();
            transaction.add(R.id.fragment_container, fragmentRecycleView);
        }else{
            transaction.show(fragmentRecycleView);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i){
            case 0:
                radioGroup.check(R.id.radio_listView);
                break;
            case 1:
                radioGroup.check(R.id.radio_GridView);
                break;
            case 2:
                radioGroup.check(R.id.radio_recycleView);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
