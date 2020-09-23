package com.nepalese.toollibs.Activity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.toollibs.R;

import java.util.List;

public class ListView_Adapter<T> extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<T> data;
    private InterListener interListener;

    public ListView_Adapter(Context context, List<T> list, InterListener interListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.interListener = interListener;
    }

    @Override
    public int getCount() {
        return data==null? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //============================================
    public interface InterListener {
        void itemClick(View v);
    }

    @Override
    public void onClick(View view) {
        interListener.itemClick(view);
    }

    //self define container for components in the layout;
    static class ViewHolder {
        public TextView tvData;
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_view, null);
            holder = new ViewHolder();

            holder.tvData = convertView.findViewById(R.id.tvData);
            holder.image = convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvData.setText(data.get(position).toString());
        holder.image.setImageResource(R.drawable.icon_del);

        //make component be able click from outside
        holder.image.setOnClickListener(this);
        holder.image.setTag(position);
        return convertView;
    }
}