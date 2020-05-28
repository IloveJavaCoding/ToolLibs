package com.example.toollibs.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.toollibs.R;

public class Fragment_GridView extends Fragment {
    private View view;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);

        init();
        setListener();
        return view;
    }

    private void init() {
        gridView = view.findViewById(R.id.gridView);
    }

    private void setListener() {
    }
}
