package com.nepalese.toollibs.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nepalese.toollibs.Activity.Adapters.RecyclerView_Adapter;
import com.nepalese.toollibs.R;

public class Fragment_Recycle_View extends Fragment {
    private View view;
    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.recycleView);
    }

    private void setData() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//VERTICAL
        recyclerView.setAdapter(new RecyclerView_Adapter(getContext()));
    }

    private void setListener() {

    }
}