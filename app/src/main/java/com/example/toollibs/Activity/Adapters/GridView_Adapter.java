package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.R;

import java.util.List;

public class GridView_Adapter<T> extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<T> data;

    public GridView_Adapter(Context context, List<T> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }
    @Override
    public int getCount() {
        return data==null? 0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_grid_view,null);
        TextView tvData = view.findViewById(R.id.tvData);
        ImageView image = view.findViewById(R.id.image);

        tvData.setText(data.get(i).toString());
        image.setImageResource(R.drawable.img_del);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
