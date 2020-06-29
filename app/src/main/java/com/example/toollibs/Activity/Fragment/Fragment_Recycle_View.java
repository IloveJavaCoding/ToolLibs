package com.example.toollibs.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toollibs.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Recycle_View extends Fragment {
    private View view;
    //private RecyclerView recyclerView;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycle_view, container, false);

        init();
        setData();
        setListener();
        return view;
    }

    private void init() {
        //recyclerView = view.findViewById(R.id.recycleView);
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

//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//VERTICAL
//        recyclerView.setAdapter(new RecyclerView_Adapter(getContext(), list));
    }

    private void setListener() {

    }
}