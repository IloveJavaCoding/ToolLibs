package com.nepalese.toollibs.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nepalese.toollibs.Activity.Adapters.ListView_Adapter;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.SystemUtil;

import java.util.ArrayList;
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
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");

        adapter = new ListView_Adapter<>(view.getContext(), list, this);
        listView.setAdapter(adapter);
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //focus curtain line
                SystemUtil.showToast(getContext(), list.get(i));
            }
        });

    }

    @Override
    public void itemClick(View v) {
        //get the position of item that was clicked
        int position = (Integer)v.getTag();
        switch (v.getId()){
           case R.id.image:
               //do something
               Log.d("tag", "delete........" + list.get(position));
               list.remove(position);
               adapter.notifyDataSetChanged();
               break;
        }
    }
}