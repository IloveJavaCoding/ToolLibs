package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.R;
import com.example.toollibs.SelfClass.Song;

import java.util.List;

public class ListView_Player_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Song> songs;

    public ListView_Player_Adapter(Context context, List<Song> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.songs = list;
    }

    @Override
    public int getCount() {
        return songs==null? 0:songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView tvDisplayName, tvOrder;
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_view_player, null);
            holder = new ViewHolder();

            holder.tvDisplayName = convertView.findViewById(R.id.tvDisplay);
            holder.tvOrder = convertView.findViewById(R.id.tvOrder);
            holder.image = convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvOrder.setText((position+1)+"");
        holder.tvDisplayName.setText(songs.get(position).getTitle()+" [" + songs.get(position).getArtist()+"]");
        holder.image.setImageResource(R.drawable.icon_arrow_right);

        return convertView;
    }
}
