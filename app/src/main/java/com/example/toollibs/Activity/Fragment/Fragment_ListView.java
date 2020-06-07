package com.example.toollibs.Activity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toollibs.Activity.Adapters.ListView_Adapter;
import com.example.toollibs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_ListView extends Fragment implements ListView_Adapter.InterListener {
    private View view;
    private ListView listView;
    private ListView_Adapter adapter;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_view, container, false);

        init();
        setData();
        setListener();
        return view;
    }

    private void init() {
        listView = view.findViewById(R.id.listView);
    }

    private void setData() {
        list = new ArrayList<>();
        String[] strArray = getResources().getStringArray(R.array.Date);
        list = Arrays.asList(strArray);

        adapter = new ListView_Adapter<>(view.getContext(), list, this);
        listView.setAdapter(adapter);
    }

    private void setListener() {

    }

    @Override
    public void itemClick(View v) {
        //get the position of item that was clicked
        int position = (Integer)v.getTag();
        switch (v.getId()){
           case R.id.image:
               //do something
               list.remove(position);
               adapter.notifyDataSetChanged();
               break;
        }
    }
}