package com.nepalese.toollibs.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nepalese.toollibs.Activity.Adapters.GridView_Adapter;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class Fragment_GridView extends Fragment {
    private View view;
    private GridView gridView;
    private GridView_Adapter adapter;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);

        init();
        setData();
        setListener();
        return view;
    }

    private void init() {
        gridView = view.findViewById(R.id.gridView);
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

        adapter = new GridView_Adapter(getContext(), list);
        gridView.setAdapter(adapter);
    }

    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SystemUtil.showToast(getContext(), list.get(i));
            }
        });
    }
}
