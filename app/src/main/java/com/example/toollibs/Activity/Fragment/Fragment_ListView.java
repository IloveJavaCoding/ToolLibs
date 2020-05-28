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
import android.widget.ListView;
import android.widget.TextView;

import com.example.toollibs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_ListView extends Fragment {
    private View view;
    private ListView listView;
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
        listView.setAdapter(new ListView_Adapter<>(view.getContext(), list));
    }

    private void setListener() {

    }
}

class ListView_Adapter<T> extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<T> data;

    public ListView_Adapter(Context context, List<T> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public TextView tvData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            convertView = inflater.inflate(R.layout.layout_view,null);
            holder = new ViewHolder();

            holder.tvData = convertView.findViewById(R.id.tvData);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvData.setText(data.get(position).toString());
        return convertView;
    }
}
